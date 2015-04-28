package org.motechproject.nms.kilkariobd.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkariobd.client.HttpClient;
import org.motechproject.nms.kilkariobd.client.ex.CDRFileProcessingFailedException;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.*;
import org.motechproject.nms.kilkariobd.helper.MD5Checksum;
import org.motechproject.nms.kilkariobd.helper.SecureCopy;
import org.motechproject.nms.kilkariobd.mapper.CSVMapper;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.motechproject.nms.kilkariobd.commons.Constants.*;

/**
 * This class handle the events for prepare the target file and notify the target file to the IVR.
 */
@Component
public class OBDTargetFileHandler {

    @Autowired
    private OutboundCallRequestService requestService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private LanguageLocationCodeService llcService;

    @Autowired
    private OutboundCallFlowService callFlowService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private OutboundCallDetailService callDetailService;

    @Autowired
    private ContentUploadService contentUploadService;

    @Autowired
    private SettingsFacade kilkariObdSettings;

    @Autowired
    private MotechSchedulerService motechSchedulerService;

    private HttpClient client = new HttpClient();

    Logger logger = LoggerFactory.getLogger(OBDTargetFileHandler.class);

    /**
     * This method handles the event raised by scheduler to prepare target file.
     */
    @MotechListener(subjects = PREPARE_OBD_TARGET_EVENT_SUBJECT)
    public void prepareOBDTargetEventHandler() {

        logger.info(String.format("Started processing of event with subject %s", PREPARE_OBD_TARGET_EVENT_SUBJECT));

        String obdFileName = getCsvFileName();
        OutboundCallFlow todayCallFlow = callFlowService.findRecordByFileName(obdFileName);
        if (todayCallFlow != null) {
            logger.error("Duplicate event received for Subject :" + PREPARE_OBD_TARGET_EVENT_SUBJECT);
            return;
        } else {
            /* delete all the records from outboundCallRequest before preparing fresh ObdTargetFile */
            requestService.deleteAll();

            /* create new record for OutboundCallFlow to track status of files processing for current day */
            OutboundCallFlow callFlow = new OutboundCallFlow();
            callFlow.setStatus(CallFlowStatus.OUTBOUND_FILE_PREPARATION_EVENT_RECEIVED);
            callFlow.setObdFileName(obdFileName);
            callFlowService.create(callFlow);

            prepareObdTargetFile(obdFileName, null, FIRST_ATTEMPT);
        }

        logger.info(String.format("Finished processing of event with subject %s", PREPARE_OBD_TARGET_EVENT_SUBJECT));
    }

    /**
     *  This method handles Event raised in case Retry is needed for OBD Preparation while waiting for CDR notification
     *  @param motechEvent object of MotechEvent
     */
    @MotechListener(subjects = RETRY_PREPARE_OBD_TARGET_EVENT_SUBJECT)
    public void obdRetryHandler(MotechEvent motechEvent) {

        logger.info(String.format("Started processing of event with subject %s", RETRY_PREPARE_OBD_TARGET_EVENT_SUBJECT));

        Object cdrObdFile = motechEvent.getParameters().get(Constants.CURRENT_CDR_OBD_FILE);
        Object obdFile = motechEvent.getParameters().get(Constants.CURRENT_OBD_FILE);
        String obdFileName = null;
        String cdrObdFileName = null;
        if (obdFile != null) {
            obdFileName = obdFile.toString();
        }

        if (cdrObdFile != null) {
            cdrObdFileName = cdrObdFile.toString();
        }

        Integer retryNumber = (Integer)motechEvent.getParameters().get(Constants.OBD_PREPARATION_RETRY_NUMBER);
        prepareObdTargetFile(obdFileName, cdrObdFileName, retryNumber);

        logger.info(String.format("Finished processing of event with subject %s", RETRY_PREPARE_OBD_TARGET_EVENT_SUBJECT));
    }

    /**
     *  This method handles the event raised by scheduler to notify IVr of target file has been copied.
     */
    @MotechListener(subjects = NOTIFY_OBD_TARGET_EVENT_SUBJECT)
    public void copyAndNotifyOBDTargetFile() {
        logger.info(String.format("Started processing of event with subject %s", NOTIFY_OBD_TARGET_EVENT_SUBJECT));

        OutboundCallFlow todayCallFlow =  null;
        Long recordsCount = null;
        String checksum = null;
        String localFileName  = null;
        String remoteFileName = null;
        Settings settings = new Settings(kilkariObdSettings);
        Configuration configuration = configurationService.getConfiguration();
        String obdFileName = getCsvFileName();

        /* update the callFlow status with notify outbound file event received.*/
        callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.NOTIFY_OUTBOUND_FILE_EVENT_RECEIVED);

        /* notify IVR */
        //todo we have not fetch the record from the database ,but yet using the object to find record count
        //todo it will produce null pointer exception
        recordsCount = todayCallFlow.getObdRecordCount();
        checksum = todayCallFlow.getObdChecksum();
        client.notifyTargetFile(remoteFileName, checksum, recordsCount);

        /* Update the call flow status as file notification sent */
        callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.OBD_FILE_NOTIFICATION_SENT_TO_IVR);

        logger.info(String.format("Finished processing of event with subject %s", NOTIFY_OBD_TARGET_EVENT_SUBJECT));
    }

    /**
     *  This method handles the Obd Preparation Call Flow
     * @param freshObdFile name of the Obd File to be prepared
     * @param oldObdFileName name of the obd file for which CDR is to be processed
     * @param retryNumber attempt number for obd preparation
     */
    public void prepareObdTargetFile(String freshObdFile, String oldObdFileName, Integer retryNumber) {
        Configuration configuration  = configurationService.getConfiguration();
        Settings settings = new Settings();
        OutboundCallFlow oldCallFlow =  null;

        OutboundCallFlow todayCallStatus = callFlowService.findRecordByFileName(freshObdFile);
        CallFlowStatus callFlowStatus = todayCallStatus.getStatus();

        if( oldObdFileName == null) {
            /* etch the OutboundCallFlow record of previous day to update the status of CDR files processing */
            oldCallFlow = callFlowService.findRecordByCallStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED); //todo: find order by createDate ?
        } else {
            oldCallFlow = callFlowService.findRecordByFileName(oldObdFileName);
        }

        if (oldCallFlow == null || oldCallFlow.getStatus().equals(CallFlowStatus.CDR_FILES_PROCESSING_FAILED)) {
                if (retryNumber < configuration.getMaxObdPreparationRetryCount()) {
                    retryPrepareOBDTargetFile(freshObdFile, oldObdFileName, ++retryNumber, configuration);
                    return;
                } else {
                    /* Process Fresh */
                    createFreshOBDRecords(configuration, settings, freshObdFile);
                    return;
                }
        }

        /* State machine to prepare OBD target based on the status of the call flow */
        try {
            switch (callFlowStatus) {
                case CDR_SUMMARY_PROCESSING_FAILED :
                case OUTBOUND_FILE_PREPARATION_EVENT_RECEIVED:
                    /* process CDR Summary & CDR Detail, then create  call base using
                    fresh and retry call request records*/
                    downloadAndProcessCdrSummaryFile(freshObdFile, oldCallFlow.getObdFileName(), configuration, settings);
                    downloadAndProcessCdrDetailFile(freshObdFile, oldObdFileName, configuration, settings);
                    createFreshOBDRecords(configuration, settings, freshObdFile);
                    break;

                case CDR_DETAIL_PROCESSING_FAILED:
                    /* process CDR Detail, then create  call base using
                    fresh and retry call request records*/
                    downloadAndProcessCdrDetailFile(freshObdFile, oldObdFileName, configuration, settings);
                    createFreshOBDRecords(configuration, settings, freshObdFile);
                    break;

                default:
                    logger.error("Invalid State in OBD Call Flow");
                    break;
            }

        } catch (CDRFileProcessingFailedException cdrEx) {
            callFlowService.updateCallFlowStatus(oldObdFileName, CallFlowStatus.CDR_FILES_PROCESSING_FAILED);

            /* repeat Event if retry count not exceeded */
            if (retryNumber < configuration.getMaxObdPreparationRetryCount()) {
                retryPrepareOBDTargetFile(freshObdFile, oldCallFlow.getObdFileName(), ++retryNumber, configuration);
            } else {
                /* Process Fresh base records */
                createFreshOBDRecords(configuration, settings, freshObdFile);
            }
        }
    }

    /*
    This method downloads (using SCP) the CDR Summary file and then invokes the method for CDR Processing
     */
    private void downloadAndProcessCdrSummaryFile(String obdFileName, String oldObdFileName, Configuration configuration, Settings settings) throws CDRFileProcessingFailedException {
        String cdrSummaryFileName = CDR_SUMMARY_FILE_PREFIX + oldObdFileName;
        String localCdrSummaryFileName = settings.getObdFileLocalPath() + "/" + cdrSummaryFileName;
        String remoteCdrSummaryFileName = configuration.getObdFilePathOnServer() + "/" + cdrSummaryFileName;
        try {
            /* Copy cdrSummaryFile from remote location to local */
            SecureCopy.fromRemote(settings.getObdFileLocalPath(), remoteCdrSummaryFileName);
            processCDRSummaryCSV(obdFileName, oldObdFileName, localCdrSummaryFileName, configuration);
        } catch (FileNotFoundException fex) {
            String failureReason = String.format(Constants.FILE_NOT_ACCESSIBLE,
                    configuration.getObdFileServerIp(), configuration.getObdFilePathOnServer(),
                    cdrSummaryFileName, cdrSummaryFileName);
            client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_NOT_ACCESSIBLE, cdrSummaryFileName, failureReason);
            callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_SUMMARY_PROCESSING_FAILED);
            throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_NOT_ACCESSIBLE);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Transactional
    private void processCDRSummaryCSV(String obdFileName, String oldObdFileName, String cdrSummaryFileName,
                                      Configuration configuration) throws FileNotFoundException, CDRFileProcessingFailedException  {
        List<Map<String, String >> cdrSummaryRecords;
        OutboundCallFlow oldCallFlow = callFlowService.findRecordByFileName(oldObdFileName);

        /* validate record count */
        cdrSummaryRecords = CSVMapper.readWithCsvMapReader(cdrSummaryFileName);
        validateRecordCount(obdFileName, oldObdFileName, CDR_SUMMARY_FILE_PREFIX + oldObdFileName,
                oldCallFlow.getCdrSummaryRecordCount(), cdrSummaryRecords.size(), CallFlowStatus.CDR_SUMMARY_PROCESSING_FAILED);

        /* validate checksum */
        String checksum = MD5Checksum.findChecksum(cdrSummaryFileName);
        validateChecksum(obdFileName, oldObdFileName, CDR_SUMMARY_FILE_PREFIX + oldObdFileName,
                oldCallFlow.getCdrSummaryChecksum(), checksum, CallFlowStatus.CDR_SUMMARY_PROCESSING_FAILED);

        /* read and parse CDRSummary CSV and create entry in OutboundCallRequest table for each record. */
        for (Map<String, String> map : cdrSummaryRecords) {
            CallStatus finalStatus = CallStatus.getByInteger(Integer.parseInt(map.get(Constants.FINAL_STATUS)));
            ObdStatusCode statusCode = ObdStatusCode.getByInteger(Integer.parseInt(map.get(Constants.STATUS_CODE)));

            Long subscriptionId = Long.parseLong(map.get(Constants.REQUEST_ID));

            String weekId = map.get(Constants.WEEK_ID);
            String[] weekNoMsgNo = weekId.split("_");

            Integer weekNumber = Integer.parseInt(weekNoMsgNo[0]);

            Integer retryDayNumber = Constants.RETRY_NONE;
            /* Deactivated subscriptions based on Status codes or check for retry or mark complete */
            if (statusCode.equals(ObdStatusCode.OBD_FAILED_INVALIDNUMBER)) {
                /*
                If for any cdrSummary record status code is OBD_FAILED_INVALIDNUMBER
                then deactivate and don't retry.
                 */
                subscriptionService.deactivateSubscription(
                        subscriptionId, DeactivationReason.INVALID_MSISDN);
            } else if (statusCode.equals(ObdStatusCode.OBD_DNIS_IN_DND) &&
                subscriptionService.deactivateSubscription(subscriptionId, DeactivationReason.MSISDN_IN_DND)){
                /*
                If for any cdrSummary record status code is OBD_DNIS_IN_DND then try to deactivate and if
                deactivated then don't retry.
                 */
            }else if ((retryDayNumber = isValidForRetry(finalStatus, statusCode, subscriptionId)) != Constants.RETRY_NONE) {

                /* If valid for retry then create corresponding call request record */
                OutboundCallRequest callRequestRetry = new OutboundCallRequest();
                callRequestRetry.setRequestId(map.get(Constants.REQUEST_ID));

                callRequestRetry.setMsisdn(map.get(Constants.MSISDN));

                callRequestRetry.setLanguageLocationCode(Integer.parseInt(map.get(Constants.LANGUAGE_LOCATION_CODE)));
                callRequestRetry.setCircle(map.get(Constants.CIRCLE));

                callRequestRetry.setContentFileName(map.get(Constants.CONTENT_FILE_NAME));
                callRequestRetry.setCli(map.get(Constants.CLI));
                callRequestRetry.setCallFlowURL(map.get(Constants.CALL_FLOW_URL));
                if (retryDayNumber.equals(Constants.RETRY_DAY_ONE)) {
                    callRequestRetry.setPriority(configuration.getRetryDay1ObdPriority());
                    callRequestRetry.setServiceId(configuration.getRetryDay1ObdServiceId());
                } else if(retryDayNumber.equals(Constants.RETRY_DAY_TWO)) {
                    callRequestRetry.setPriority(configuration.getRetryDay2ObdPriority());
                    callRequestRetry.setServiceId(configuration.getRetryDay2ObdServiceId());
                } else {
                    callRequestRetry.setPriority(configuration.getRetryDay3ObdPriority());
                    callRequestRetry.setServiceId(configuration.getRetryDay3ObdServiceId());
                }

                callRequestRetry.setWeekId(map.get(Constants.WEEK_ID));
                requestService.create(callRequestRetry);
            } else if (weekNumber.equals(Constants.MAX_WEEK_NUMBER)) {
                    /* If call was successful or failed for its last retry number for Last Week's message */
                    subscriptionService.completeSubscription(subscriptionId);
            }

            /*
            if for any cdrSummary record statusCode is OBD_FAILED_NOATTEMPT then
            create a record in OutboundCallDetail table.
             */
            if (statusCode.equals(ObdStatusCode.OBD_FAILED_NOATTEMPT)) {
                OutboundCallDetail record = new OutboundCallDetail();
                record.setWeekId(map.get(Constants.WEEK_ID));
                record.setPriority(Integer.parseInt(map.get(Constants.PRIORITY)));
                record.setContentFile(map.get(Constants.CONTENT_FILE_NAME));
                record.setCircleCode(map.get(Constants.CIRCLE));
                record.setCallStatus(Integer.parseInt(map.get(Constants.FINAL_STATUS)));
                record.setLanguageLocationCode(Integer.parseInt(map.get(Constants.LANGUAGE_LOCATION_CODE)));
                record.setMsisdn(map.get(Constants.MSISDN));
                record.setRequestId(map.get(Constants.REQUEST_ID));
                record.setCallStartTime(null);
                record.setCallAnswerTime(null);
                record.setCallEndTime(null);
                callDetailService.create(record);
            }
        }
    }

    /* This method download the CDR details file , processes and also changes the status of the call flow for
    the OBD file (whose CDR was processed) . And notifies to IVR
     */
    private void downloadAndProcessCdrDetailFile(String obdFileName, String oldObdFileName, Configuration configuration,
                                                 Settings settings) throws CDRFileProcessingFailedException {

        String cdrDetailFileName = CDR_DETAIL_FILE_PREFIX + oldObdFileName;
        String localCdrDetailFileName = settings.getObdFileLocalPath() + "/" + cdrDetailFileName;
        String remoteCdrDetailFileName = configuration.getObdFilePathOnServer() + "/" + cdrDetailFileName;

        try {
            /* copy cdrDetailFile from remote location to local */
            SecureCopy.fromRemote(settings.getObdFileLocalPath(), remoteCdrDetailFileName);
            processCDRDetail(obdFileName, oldObdFileName, localCdrDetailFileName);
        } catch (FileNotFoundException fex) {
            String failureReason = String.format(Constants.FILE_NOT_ACCESSIBLE,
                    configuration.getObdFileServerIp(), configuration.getObdFilePathOnServer(),
                    cdrDetailFileName, cdrDetailFileName);
            client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_NOT_ACCESSIBLE, cdrDetailFileName, failureReason);
            callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
            throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_NOT_ACCESSIBLE);
        } catch (IOException ex) {
            logger.error((ex.getMessage()));
        }

        callFlowService.updateCallFlowStatus(oldObdFileName, CallFlowStatus.CDR_FILES_PROCESSED);

        /* Notify IVR of file processing finished successfully */
        client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_PROCESSED_SUCCESSFULLY, oldObdFileName, null);
    }

    @Transactional
    private void processCDRDetail(String obdFileName, String oldObdFileName, String cdrDetailFileName)
            throws FileNotFoundException, CDRFileProcessingFailedException {
        List<Map<String, String>> cdrDetailRecords;
        OutboundCallFlow oldCallFlow = callFlowService.findRecordByFileName(oldObdFileName);


        /* validate record count */
        cdrDetailRecords = CSVMapper.readWithCsvMapReader(cdrDetailFileName);
        validateRecordCount(obdFileName, oldObdFileName, CDR_DETAIL_FILE_PREFIX + oldObdFileName,
                oldCallFlow.getCdrDetailRecordCount(), cdrDetailRecords.size(), CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);

        /* validate checksum */
        String checksum = MD5Checksum.findChecksum(cdrDetailFileName);
        validateChecksum(obdFileName, oldObdFileName, CDR_DETAIL_FILE_PREFIX + oldObdFileName,
                oldCallFlow.getCdrDetailChecksum(), checksum, CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);

        /* read and parse CDRDetail CSV and create entry in CdrCallDetail table for each record. */
        for (Map<String, String> cdrDetailMap : cdrDetailRecords) {
            OutboundCallDetail callDetail = new OutboundCallDetail();
            callDetail.setRequestId(cdrDetailMap.get(Constants.REQUEST_ID));
            callDetail.setMsisdn(cdrDetailMap.get(Constants.MSISDN));
            callDetail.setCallId(cdrDetailMap.get(Constants.CALL_ID));
            callDetail.setAttemptNo(Integer.parseInt(cdrDetailMap.get(Constants.ATTEMPT_NO)));
            callDetail.setCallStartTime(Long.parseLong(cdrDetailMap.get(Constants.CALL_START_TIME)));
            callDetail.setCallAnswerTime(Long.parseLong(cdrDetailMap.get(Constants.CALL_ANSWER_TIME)));
            callDetail.setCallEndTime(Long.parseLong(cdrDetailMap.get(Constants.CALL_END_TIME)));
            callDetail.setCallDurationInPulse(Long.parseLong(cdrDetailMap.get(Constants.CALL_DURATION_IN_PULSE)));
            callDetail.setCallStatus(Integer.parseInt(cdrDetailMap.get(Constants.CALL_STATUS)));
            callDetail.setLanguageLocationCode(Integer.parseInt(cdrDetailMap.get(Constants.LANGUAGE_LOCATION_ID)));
            callDetail.setContentFile(cdrDetailMap.get(Constants.CONTENT_FILE));
            callDetail.setMsgPlayEndTime(Integer.parseInt(cdrDetailMap.get(Constants.MSG_PLAY_END_TIME)));
            callDetail.setMsgPlayStartTime(Integer.parseInt(cdrDetailMap.get(Constants.MSG_PLAY_START_TIME)));
            callDetail.setCircleCode(cdrDetailMap.get(Constants.CIRCLE_ID));
            callDetail.setOperatorCode(cdrDetailMap.get(Constants.OPERATOR_ID));
            callDetail.setPriority(Integer.parseInt(cdrDetailMap.get(Constants.PRIORITY)));
            CallDisconnectReason disconnectReason = CallDisconnectReason.getByString(
                    cdrDetailMap.get(Constants.CALL_DISCONNECT_REASON));
            callDetail.setCallDisconnectReason(disconnectReason);
            callDetail.setWeekId(cdrDetailMap.get(Constants.WEEK_ID));
            callDetailService.create(callDetail);
        }

    }

    /* This method checks if a particular call valid for retry based on final status. */
    private Integer isValidForRetry(CallStatus finalStatus, ObdStatusCode statusCode, Long subscriptionId) {
        Integer retryDayNumber = Constants.RETRY_NONE;

        /* Check for retry If the call is failed or rejected */
        if(!finalStatus.equals(CallStatus.SUCCESS)) {
            /* Get the retry day number from subscription service */
            retryDayNumber = subscriptionService.retryAttempt(subscriptionId);
        }
        return retryDayNumber;
    }

    /**
     * This method exports the records from OutboundCallRequest
     * @param fileName Complete file name to which to export
     * @return returns the count of records exported
     */
    public Long exportOutBoundCallRequest(String fileName) {
        List<OutboundCallRequest> callRequests = requestService.retrieveAll();
        Long recordCount = requestService.getCount();
        CSVMapper.writeByCsvMapper(fileName, callRequests);
        return recordCount;
    }

    /* This method prepares the obd file name */
    private String getCsvFileName() {
        DateTime date = DateTime.now().toDateMidnight().toDateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "OBD_NMS_" + sdf.format(date) + ".csv";
    }

    private void createFreshOBDRecords(Configuration configuration, Settings settings, String obdFileName) {
        List<Subscription> scheduledActiveSubscription = subscriptionService.getScheduledSubscriptions();
        for (Subscription subscription : scheduledActiveSubscription) {
            OutboundCallRequest callRequest = new OutboundCallRequest();
            callRequest.setRequestId(subscription.getId().toString());
            callRequest.setServiceId(configuration.getFreshObdServiceId());
            callRequest.setMsisdn(subscription.getMsisdn());
            callRequest.setPriority(configuration.getFreshObdPriority());
            callRequest.setWeekId(
                    subscription.getWeekNumber().toString() + "_" + subscription.getMessageNumber().toString());
            /* set languageLocationCode and circleCode in callRequest */
            Integer llcCode = subscription.getSubscriber().getLanguageLocationCode();
            if (llcCode != null) {
                String contentFileName = contentUploadService.getContentFileName("W" + callRequest.getWeekId(), llcCode);
                if (contentFileName != null) {
                    callRequest.setContentFileName(contentFileName);
                }
                else {
                    /*
                    if this file name is returned null then create an error log for this record and
                     don't add this record. */
                    logger.error(Constants.CONTENT_FILE_NAME + " not found");
                    continue;
                }

                LanguageLocationCode record = llcService.findLLCByCode(llcCode);
                if (record.getCircleCode() != null) {
                    callRequest.setCircle(record.getCircleCode());
                }else {
                    logger.error(Constants.CIRCLE + " not found");
                    continue;
                }

                callRequest.setLanguageLocationCode(llcCode);
            } else {
                logger.error(Constants.LANGUAGE_LOCATION_CODE + " not found");
                continue;
            }

            callRequest.setCli(null);
            callRequest.setCallFlowURL(null);
            requestService.create(callRequest);
        }

        String localFileName = settings.getObdFileLocalPath() + obdFileName;
        String remoteFileName = configuration.getObdFilePathOnServer();

        /* Export the Records to CSV File */
        Long recordsCount = exportOutBoundCallRequest(localFileName);
        String obdChecksum = MD5Checksum.findChecksum(localFileName);
        callFlowService.updateChecksumAndRecordCount(obdFileName, obdChecksum, recordsCount);

         /* Update the call flow status as file created */
        callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_CREATED);

         /* copy this target file to remote location. */
        copyTargetObdFileOnServer(localFileName, remoteFileName, obdFileName);

        /* Update the call flow status as file copied */
        callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_COPIED);
    }

    /*
    Method to retry for the preparation of OBDTarget file if some error occurred.
     */
    private void retryPrepareOBDTargetFile(
            String currentObdFile, String currentCdrObdFile, Integer retryNumber, Configuration configuration) {
        int retryInterval = configuration.getRetryIntervalForObdPreparationInMins();
        Map<String, Object> params = new HashMap<>();
        params.put(MotechSchedulerService.JOB_ID_KEY, Constants.OBD_PREPARATION_RETRY_JOB);
        params.put(Constants.CURRENT_OBD_FILE, currentObdFile);
        params.put(Constants.CURRENT_CDR_OBD_FILE, currentCdrObdFile);
        params.put(Constants.OBD_PREPARATION_RETRY_NUMBER, retryNumber);
        MotechEvent motechEvent = new MotechEvent(Constants.RETRY_PREPARE_OBD_TARGET_EVENT_SUBJECT, params);

        Date retryTime = DateTime.now().plusMinutes(retryInterval).toDate();
        RunOnceSchedulableJob oneTimeJob = new RunOnceSchedulableJob(motechEvent, retryTime);
        motechSchedulerService.safeScheduleRunOnceJob(oneTimeJob);
    }

    /**
     * This method copies the targetObdFile to the remote server and update the callFlow status
     * @param localFileName name of local file to be copied on remote server
     * @param remoteFileName remote path on which file is to copied.
     * @param obdFileName name of obd file name to saved in database.
     */
    public void copyTargetObdFileOnServer(String localFileName, String remoteFileName, String obdFileName) {
        /* copy this target file to remote location. */
        SecureCopy.toRemote(localFileName, remoteFileName);
    }

    /*
    This method validates the record count in CSV file against the value sent in CDR notification already stored in DB,
    and raises exception if count doesn't matches
     */
    private void validateRecordCount(String obdFileName, String oldObdFileName, String cdrFileName,
                                     long recordCountInDb, int recordCountInCsv, CallFlowStatus toStatus) throws CDRFileProcessingFailedException {
        /* send error if cdr file processing has errors in record count */
        if (recordCountInCsv != recordCountInDb) {
            String failureReason = String.format(Constants.INVALID_RECORD_COUNT,
                    recordCountInDb, recordCountInCsv, cdrFileName);
            client.notifyCDRFileProcessedStatus(
                    FileProcessingStatus.FILE_RECORDCOUNT_ERROR, oldObdFileName, failureReason);
            callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
            throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_RECORDCOUNT_ERROR);
        }
    }

    /*
    This method validates the checksum of CSV file against the value sent in CDR notification already stored in DB,
    and raises exception if checksum fails
     */
    private void validateChecksum(String obdFileName, String oldObdFileName, String cdrFileName,
                                     String checksumInDb, String ChecksumCsv, CallFlowStatus toStatus) throws CDRFileProcessingFailedException {
        /* send error if cdr file processing has errors in checksum */
        if (!ChecksumCsv.equals(checksumInDb)) {
            String failureReason = String.format(Constants.INVALID_CHECKSUM,
                    checksumInDb, ChecksumCsv, cdrFileName);
            client.notifyCDRFileProcessedStatus(
                    FileProcessingStatus.FILE_CHECKSUM_ERROR, oldObdFileName, failureReason);
            callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
            throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_CHECKSUM_ERROR);
        }
    }
}

