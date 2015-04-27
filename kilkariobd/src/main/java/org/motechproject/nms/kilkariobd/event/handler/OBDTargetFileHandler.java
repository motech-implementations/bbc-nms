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
import org.motechproject.nms.util.helper.ParseDataHelper;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * This method defines a daily event to be raised by scheduler to prepare target file.
     */
    @MotechListener(subjects = PREPARE_OBD_TARGET_EVENT_SUBJECT)
    public void prepareOBDTargetEventHandler() {
        /*
        delete all the records from outboundCallRequest before preparing fresh ObdTargetFile
         */
        requestService.deleteAll();
        String obdFileName = getCsvFileName();
        OutboundCallFlow todayCallFlow = callFlowService.findRecordByFileName(obdFileName);
        if (todayCallFlow != null) {
            logger.error("Duplicate event received for Subject :" + PREPARE_OBD_TARGET_EVENT_SUBJECT);
            return;
        } else {
            /*
            create new record for OutboundCallFlow to track status of files processing for current day
            */
            OutboundCallFlow callFlow = new OutboundCallFlow();
            callFlow.setStatus(CallFlowStatus.OUTBOUND_FILE_PREPARATION_EVENT_RECEIVED);
            callFlow.setObdFileName(obdFileName);
            callFlowService.create(callFlow);

            prepareObdTargetFile(obdFileName, null, FIRST_ATTEMPT);
        }
    }

    @MotechListener(subjects = RETRY_PREPARE_OBD_TARGET_EVENT_SUBJECT)
    public void ObdRetryHandler(MotechEvent motechEvent) {
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
    }

    /* Daily event to be raised by scheduler to notify IVr of target file has been copied. */
    @MotechListener(subjects = NOTIFY_OBD_TARGET_EVENT_SUBJECT)
    public void copyAndNotifyOBDTargetFile() {

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
        recordsCount = todayCallFlow.getObdRecordCount();
        checksum = todayCallFlow.getObdChecksum();
        client.notifyTargetFile(remoteFileName, checksum, recordsCount);

        /* Update the call flow status as file notification sent */
        callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.OBD_FILE_NOTIFICATION_SENT_TO_IVR);

    }

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

        try {
            switch (callFlowStatus) {
                case CDR_SUMMARY_PROCESSING_FAILED :
                case OUTBOUND_FILE_PREPARATION_EVENT_RECEIVED:
                    /* processCDRSummary */
                    downloadAndProcessCdrSummaryFile(freshObdFile, oldCallFlow.getObdFileName(), configuration, settings);
                    processCdrDetail(freshObdFile,oldCallFlow.getObdFileName(), configuration, settings);
                    createFreshOBDRecords(configuration, settings, freshObdFile);
                    break;

                case CDR_DETAIL_PROCESSING_FAILED:
                    processCdrDetail(freshObdFile,oldCallFlow.getObdFileName(), configuration, settings);
                    createFreshOBDRecords(configuration, settings, freshObdFile);
                    break;

                default:
                    logger.error("Invalid State in OBD Call Flow");
                    break;

            }

        } catch (CDRFileProcessingFailedException cdrEx) {
            callFlowService.updateCallFlowStatus(oldObdFileName, CallFlowStatus.CDR_FILES_PROCESSING_FAILED);

            /* repeat Event */
            if (retryNumber < configuration.getMaxObdPreparationRetryCount()) {
                retryPrepareOBDTargetFile(freshObdFile, oldCallFlow.getObdFileName(), ++retryNumber, configuration);
            } else {
                /* Process Fresh */
                createFreshOBDRecords(configuration, settings, freshObdFile);
            }
        }
    }

    private void processCdrDetail(String obdFileName, String oldObdFileName, Configuration configuration, Settings settings)
            throws CDRFileProcessingFailedException{
        /* ProcessCDRDetail */
        downloadAndProcessCdrDetailFile(obdFileName, oldObdFileName, configuration, settings);
        callFlowService.updateCallFlowStatus(oldObdFileName, CallFlowStatus.CDR_FILES_PROCESSED);

        /* Notify IVR of file processing finished successfully */
        client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_PROCESSED_SUCCESSFULLY, oldObdFileName);
    }

    private void downloadAndProcessCdrSummaryFile(String obdFileName, String oldObdFileName, Configuration configuration, Settings settings) throws CDRFileProcessingFailedException {
        String cdrSummaryFileName = "/Cdr_Summary_" + oldObdFileName;
        String localCdrSummaryFileName = settings.getObdFileLocalPath() + cdrSummaryFileName;
        String remoteCdrSummaryFileName = configuration.getObdFilePathOnServer() + cdrSummaryFileName;
        try {
            /* Copy cdrSummaryFile from remote location to local */
            SecureCopy.fromRemote(settings.getObdFileLocalPath(), remoteCdrSummaryFileName);
            processCDRSummaryCSV(obdFileName, oldObdFileName, localCdrSummaryFileName, configuration);
        } catch (FileNotFoundException fex) {
            client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_NOT_ACCESSIBLE, cdrSummaryFileName);
            callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_SUMMARY_PROCESSING_FAILED);
            throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_NOT_ACCESSIBLE);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Transactional
    private void processCDRSummaryCSV(String obdFileName, String oldObdFileName, String cdrSummaryFileName,
                                      Configuration configuration) throws CDRFileProcessingFailedException {
        List<Map<String, String >> cdrSummaryRecords;
        Integer retryDayNumber;
        //todo apply null checks where ever possible.
        OutboundCallFlow oldCallFlow = callFlowService.findRecordByFileName(oldObdFileName);
        try {
            cdrSummaryRecords = CSVMapper.readWithCsvMapReader(cdrSummaryFileName);
            /*
            send error if cdr summary processing has errors for invalid records count
             */
            if (oldCallFlow != null && oldCallFlow.getCdrSummaryRecordCount() != cdrSummaryRecords.size()) {
                client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_RECORDCOUNT_ERROR, oldObdFileName);
                callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_SUMMARY_PROCESSING_FAILED);
                throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_RECORDCOUNT_ERROR);
            }

            /*
            send error if cdr summary processing has errors for invalid checksum.
             */
            if (oldCallFlow != null && oldCallFlow.getCdrSummaryChecksum().equals(MD5Checksum.findChecksum(cdrSummaryFileName))) {
                client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_CHECKSUM_ERROR, oldObdFileName);
                callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_SUMMARY_PROCESSING_FAILED);
                throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_CHECKSUM_ERROR);
            }

            /*
            read and parse CDRSummary CSV and create entry in OutboundCallRequest table for each record.
             */
            for (Map<String, String> map : cdrSummaryRecords) {
                CallStatus finalStatus = CallStatus.getByInteger(Integer.parseInt(map.get(Constants.FINAL_STATUS)));
                ObdStatusCode statusCode = ObdStatusCode.getByInteger(Integer.parseInt(map.get(Constants.STATUS_CODE)));
                Long subscriptionId = ParseDataHelper.validateAndParseLong(Constants.REQUEST_ID, map.get(Constants.REQUEST_ID), true);
                String weekId = map.get(Constants.WEEK_ID);
                String[] weekNoMsgNo = weekId.split("_");
                retryDayNumber = subscriptionService.retryAttempt(subscriptionId);
                Integer weekNumber = Integer.parseInt(weekNoMsgNo[0]);

                if (isValidForRetry(finalStatus, statusCode, subscriptionId, weekNumber)) {

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
                } else {

                    /*
                    If for any cdrSummary record callStatus is FAILED and status code is either OBD_FAILED_INVALIDNUMBER
                    or OBD_DNIS_IN_DND then deactivate the subscription for that record.
                     */
                    if (finalStatus.equals(CallStatus.FAILED) && statusCode.equals(ObdStatusCode.OBD_FAILED_INVALIDNUMBER)) {
                        subscriptionService.deactivateSubscription(
                                Long.parseLong(map.get(Constants.REQUEST_ID)), DeactivationReason.INVALID_MSISDN);
                    } else if (finalStatus.equals(CallStatus.FAILED) && statusCode.equals(ObdStatusCode.OBD_DNIS_IN_DND)) {
                        subscriptionService.deactivateSubscription(
                                Long.parseLong(map.get(Constants.REQUEST_ID)), DeactivationReason.MSISDN_IN_DND);
                    } else if (weekNumber.equals(Constants.MAX_WEEK_NUMBER)) {
                        /* If call was successful or failed for its last retry number for Last Week's message */
                        subscriptionService.completeSubscription(subscriptionId);
                    }
                }
                /*
                if for any cdrSummary record final status is Failed and statusCode is OBD_FAILED_NOATTEMPT then
                create a record in OutboundCallDetail table.
                 */
                if (finalStatus.equals(CallStatus.FAILED) && statusCode.equals(ObdStatusCode.OBD_FAILED_NOATTEMPT)) {
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
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

    }

    private void downloadAndProcessCdrDetailFile(String obdFileName, String oldObdFileName, Configuration configuration, Settings settings) throws CDRFileProcessingFailedException {

        String cdrDetailFileName = "/CDR_detail_" + oldObdFileName;
        String localCdrDetailFileName = settings.getObdFileLocalPath() + cdrDetailFileName;
        String remoteCdrDetailFileName = configuration.getObdFilePathOnServer() + cdrDetailFileName;

        try {
            /*
            Copy cdrDetailFile from remote location to local
             */
            SecureCopy.fromRemote(settings.getObdFileLocalPath(), remoteCdrDetailFileName);
            processCDRDetail(obdFileName, oldObdFileName, localCdrDetailFileName);
        } catch (FileNotFoundException fex) {
            client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_NOT_ACCESSIBLE, cdrDetailFileName);
            callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
            throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_NOT_ACCESSIBLE);
        } catch (IOException ex) {
            logger.error((ex.getMessage()));
        }
    }

    @Transactional
    private void processCDRDetail(String obdFileName, String oldObdFileName, String cdrDetailFileName) {
        List<Map<String, String>> cdrDetailRecords;
        OutboundCallFlow oldCallFlow = callFlowService.findRecordByFileName(oldObdFileName);

        try {
            cdrDetailRecords = CSVMapper.readWithCsvMapReader(cdrDetailFileName);
            /*
            send error if cdr summary processing has errors.
             */
            if (oldCallFlow.getCdrDetailRecordCount() != cdrDetailRecords.size()) {
                client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_RECORDCOUNT_ERROR, cdrDetailFileName);
                callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
                throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_RECORDCOUNT_ERROR);
            }

            /*
            send error if cdr summary processing has errors.
             */
            if (oldCallFlow.getCdrDetailChecksum().equals(MD5Checksum.findChecksum(cdrDetailFileName))) {
                client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_CHECKSUM_ERROR, obdFileName);
                callFlowService.updateCallFlowStatus(obdFileName, CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
                throw new CDRFileProcessingFailedException(FileProcessingStatus.FILE_CHECKSUM_ERROR);
            }

            /*
            read and parse CDRDetail CSV and create entry in CdrCallDetail table for each record.
             */
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
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    /*
    This method checks if a particular call valid for retry based on callStatus and final status.
     */
    private Boolean isValidForRetry(CallStatus finalStatus, ObdStatusCode statusCode, Long subscriptionId, Integer weekNumber) {
        Integer retryDayNumber = null;
        /* Check If the call is rejected or failed with reason other than DND and Invali Number */
        if(finalStatus.equals(CallStatus.REJECTED) ||
                (finalStatus.equals(CallStatus.FAILED) &&
                        !(statusCode.equals(ObdStatusCode.OBD_FAILED_INVALIDNUMBER) || statusCode.equals(ObdStatusCode.OBD_DNIS_IN_DND)))){
            /* Get the retry day number from subscription service */
            retryDayNumber = subscriptionService.retryAttempt(subscriptionId);

            /* Retry number is -1 (i.e not allowed) then return false, also mark completed if it was last
            * message of pack
            */
            if (!retryDayNumber.equals(-1)) {
                /* If retry is allowed */
                return true;
            }
        }
        return false;
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

    /*
    This method prepares the obd file name
     */
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
            /*
            set languageLocationCode and circleCode in callRequest
             */
            Integer llcCode = subscription.getSubscriber().getLanguageLocationCode();
            if (llcCode != null) {
                String contentFileName = contentUploadService.getContentFileName("W" + callRequest.getWeekId(), llcCode);
                if (contentFileName != null) {
                    callRequest.setContentFileName(contentFileName);
                }
                else {
                    /*
                    if this file name is returned null then create an error log for this record and don't add this record.
                     */
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
}

