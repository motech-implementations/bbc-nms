package org.motechproject.nms.kilkariobd.event.handler;

import org.joda.time.DateTime;
import org.motechproject.mds.service.impl.csv.CsvImporterExporter;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkariobd.client.Checksum;
import org.motechproject.nms.kilkariobd.client.CopyFile;
import org.motechproject.nms.kilkariobd.client.IvrHttpClient;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.*;
import org.motechproject.nms.kilkariobd.mapper.ReadByCSVMapper;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class PrepareOBDTargetFileHandler {

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
    private CsvImporterExporter csvImporterExporter;

    @Autowired
    private ContentUploadService contentUploadService;

    @Autowired
    private SettingsFacade kilkariObdSettings;

    private Settings settings = new Settings(kilkariObdSettings);

    Configuration configuration = configurationService.getConfiguration();

    //Daily event to be raised by scheduler
    public void prepareOBDTargetFile() {

        IvrHttpClient client = new IvrHttpClient();

        //delete all the records from outboundCallRequest before preparing fresh ObdTargetFile
        requestService.deleteAll();
        OutboundCallFlow callFlow = new OutboundCallFlow();
        callFlow.setStatus(CallFlowStatus.OUTBOUND_FILE_PREPARATION_EVENT_RECEIVED);
        callFlowService.create(callFlow);
        OutboundCallFlow oldCallFlow = callFlowService.findRecordByCallStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED);

        try {
            createFreshOBDRecords();
            //Old call flow will be null is service is deployed first time.
            if (oldCallFlow != null) {
                String obdFileName = oldCallFlow.getObdFileName();
                downloadCdrSummaryAndDetailFiles(obdFileName);
                processCDRSummaryCSV(obdFileName);
                processCDRDetail(obdFileName);
                oldCallFlow.setStatus(CallFlowStatus.CDR_FILES_PROCESSED);
                callFlowService.update(oldCallFlow);
            }
            exportOutBoundCallRequest();

        } catch (DataValidationException ex) {
            ex.printStackTrace();
        }

        client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_PROCESSED_SUCCESSFULLY);
        //todo : send error if cdrsummary and cdrdetail has errors while processing

    }

    //Daily event to be raised by scheduler
    public void CopyAndNotifyOBDTargetFile() {
        OutboundCallFlow todayCallFlow = callFlowService.findRecordByCallStatus(
                CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_CREATED);
        String fileName = todayCallFlow.getObdFileName();
        Long recordsCount = todayCallFlow.getObdRecordCount();
        CopyFile.toRemote(fileName);

        //notify IVR
        IvrHttpClient client = new IvrHttpClient();
        client.notifyTargetFile(fileName, todayCallFlow.getObdChecksum(), recordsCount);
    }

    private Boolean isValidForRetry(Integer retryDayNumber, CallStatus finalStatus, ObdStatusCode statusCode) {
        return (finalStatus.equals(CallStatus.REJECTED) ||
                (finalStatus.equals(CallStatus.FAILED) &&
                        !(statusCode.equals(ObdStatusCode.OBD_FAILED_INVALIDNUMBER) || statusCode.equals(ObdStatusCode.OBD_DNIS_IN_DND)))) &&
                isValidRetryDayNumber(retryDayNumber);
    }

    private boolean isValidRetryDayNumber(Integer retryDayNumber) {
        //todo msg_per_week constant?
        final Integer MSG_PER_WEEK = 1;
        if (MSG_PER_WEEK == 1) {
            return retryDayNumber < 3;
        } else
            if (MSG_PER_WEEK == 2) {
                return retryDayNumber < 1;
            }
        return false;
    }

    private void exportOutBoundCallRequest() {
        updateCallFlowStatus(CallFlowStatus.OUTBOUND_FILE_PREPARATION_EVENT_RECEIVED,
                CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_CREATED);

        String fileName = settings.getObdFileLocalPath() + "/OBD_NMS_" + getCsvFileName();
        Long recordsCount = 0l;
        File file = new File(fileName);
        try {
            FileWriter fos = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf = new BufferedWriter(fos);

            recordsCount = csvImporterExporter.exportCsv("OUTBOUNDCALLREQUEST", bf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String obdChecksum = Checksum.checkSum(fileName);
        updateCallFlowStatus(CallFlowStatus.OUTBOUND_FILE_PREPARATION_EVENT_RECEIVED,
                CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_CREATED, obdChecksum, recordsCount);

    }

    private void copyFileToRemoteServer(String fileName, Long recordsCount) {
        IvrHttpClient client = new IvrHttpClient();
        CopyFile.toRemote(fileName);
    }

    private String getCsvFileName() {
        DateTime date = new DateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date) + ".csv";
    }

    private void createFreshOBDRecords() throws DataValidationException{
        List<Subscription> scheduledActiveSubscription = subscriptionService.getScheduledSubscriptions();
        for (Subscription subscription : scheduledActiveSubscription) {
            OutboundCallRequest callRequest = new OutboundCallRequest();
            callRequest.setRequestId(subscription.getId().toString());
            callRequest.setServiceId(configuration.getFreshObdServiceId());
            callRequest.setMsisdn(subscription.getMsisdn());
            callRequest.setPriority(configuration.getFreshObdPriority());

            //set languageLocationCode and circleCode in callRequest
            Integer llcCode = subscription.getSubscriber().getLanguageLocationCode();
            if (llcCode != null) {
                String contentFileName = contentUploadService.getContentFileName("W" + callRequest.getWeekId(), llcCode);
                if (contentFileName != null) {
                    callRequest.setContentFileName(contentFileName);
                }
                else {
                    //todo if this file name is returned null then create an error log for this record and don't add this record.
                    ParseDataHelper.raiseMissingDataException(Constants.CONTENT_FILE_NAME, null);
                }
                callRequest.setLanguageLocationCode(subscription.getSubscriber().getLanguageLocationCode());
                LanguageLocationCode record = llcService.findLLCByCode(llcCode);
                if (record.getCircleCode() != null) {
                    callRequest.setCircleCode(record.getCircleCode());
                }
            }
            callRequest.setWeekId(
                    subscription.getWeekNumber().toString() + "_" + subscription.getMessageNumber().toString());
            callRequest.setRetryDayNumber(0);
            callRequest.setCli(null);
            callRequest.setCallFlowURL(null);
            requestService.create(callRequest);
        }
    }

    private void processCDRSummaryCSV(String obdFileName) {
        List<Map<String, Object>> cdrSummaryRecords;
        try {
            cdrSummaryRecords = ReadByCSVMapper.readWithCsvMapReader("Cdr_Summary_"  + obdFileName);
            for (Map<String, Object> map : cdrSummaryRecords) {
                Integer retryDayNumber = (Integer) map.get("retryDayNumber");
                CallStatus finalStatus = (CallStatus) map.get("finalStatus");
                ObdStatusCode statusCode = (ObdStatusCode) map.get("statusCode");
                if (isValidForRetry(retryDayNumber, finalStatus, statusCode)) {
                    OutboundCallRequest callRequestRetry = new OutboundCallRequest();
                    callRequestRetry.setRequestId(map.get("RequestId").toString());
                    callRequestRetry.setServiceId(map.get("ServiceId").toString());
                    callRequestRetry.setMsisdn(map.get("msisdn").toString());
                    callRequestRetry.setPriority((Integer) map.get("priority"));
                    callRequestRetry.setLanguageLocationCode((Integer) map.get("languageLocationCode"));
                    callRequestRetry.setCircleCode(map.get("circle").toString());
                    callRequestRetry.setWeekId(map.get("WeekId").toString());
                    callRequestRetry.setContentFileName(map.get("contentFileName").toString());
                    callRequestRetry.setCli(map.get("cli").toString());
                    callRequestRetry.setCallFlowURL(map.get("callFlowURL").toString());
                    callRequestRetry.setRetryDayNumber(retryDayNumber + 1);
                    requestService.create(callRequestRetry);
                }
                if (finalStatus.equals(CallStatus.FAILED) ||
                        (statusCode.equals(ObdStatusCode.OBD_FAILED_INVALIDNUMBER) || statusCode.equals(ObdStatusCode.OBD_DNIS_IN_DND))) {
                    //todo : subscription reason?
                    subscriptionService.deactivateSubscription(
                            (Long) map.get("requestId"), DeactivationReason.INVALID_MSISDN);
                }
                if (finalStatus.equals(CallStatus.FAILED) && statusCode.equals(ObdStatusCode.OBD_FAILED_NOATTEMPT)) {
                    OutboundCallDetail record = new OutboundCallDetail();
                    record.setCallStartTime(null);
                    record.setCallAnswerTime(null);
                    record.setCallEndTime(null);
                    callDetailService.create(record);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void processCDRDetail(String obdFileName) {
        List<Map<String, Object>> cdrDetailRecords;
        //read and parse CDRDetail CSV and create entry in CdrCallDetail table for each record.
        try {
            cdrDetailRecords = ReadByCSVMapper.readWithCsvMapReader("CDR_detail_" + obdFileName);
            for (Map<String, Object> cdrDetailMap : cdrDetailRecords) {
                OutboundCallDetail callDetail = new OutboundCallDetail();
                callDetail.setRequestId(cdrDetailMap.get("requestId").toString());
                callDetail.setMsisdn(cdrDetailMap.get("msisdn").toString());
                callDetail.setCallId(cdrDetailMap.get("callId").toString());
                callDetail.setAttemptNo((Integer) cdrDetailMap.get("attemptNo"));
                callDetail.setCallStartTime((Long) cdrDetailMap.get("callStartTime"));
                callDetail.setCallAnswerTime((Long) cdrDetailMap.get("callAnswerTime"));
                callDetail.setCallEndTime((Long) cdrDetailMap.get("callEndTime"));
                callDetail.setCallDurationInPulse((Long) cdrDetailMap.get("callDurationInPulse"));
                callDetail.setCallStatus((Integer) cdrDetailMap.get("callStatus"));
                callDetail.setLanguageLocationCode((Integer) cdrDetailMap.get("languageLocationId"));
                callDetail.setContentFile(cdrDetailMap.get("contentFile").toString());
                callDetail.setMsgPlayEndTime((Integer) cdrDetailMap.get("msgPlayEndTime"));
                callDetail.setMsgPlayStartTime((Integer) cdrDetailMap.get("msgPlayStartTime"));
                callDetail.setCircleCode(cdrDetailMap.get("circleId").toString());
                callDetail.setOperatorCode(cdrDetailMap.get("operatorId").toString());
                callDetail.setPriority((Integer) cdrDetailMap.get("priority"));
                CallDisconnectReason disconnectReason = CallDisconnectReason.getByString(
                        cdrDetailMap.get("callDisconnectReason").toString());
                callDetail.setCallDisconnectReason(disconnectReason);
                callDetail.setWeekId(cdrDetailMap.get("weekId").toString());
                callDetailService.create(callDetail);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void downloadCdrSummaryAndDetailFiles(String fileName) {
        CopyFile.fromRemote("Cdr_Summary_" + fileName);
        CopyFile.fromRemote("CDR_detail_" + fileName);
    }

    private void updateCallFlowStatus(CallFlowStatus fetchBy, CallFlowStatus updateTo) {
        OutboundCallFlow todayCallFlow = callFlowService.findRecordByCallStatus(fetchBy);
        todayCallFlow.setStatus(updateTo);
        callFlowService.update(todayCallFlow);
    }

    private void updateCallFlowStatus(
            CallFlowStatus fetchBy, CallFlowStatus updateTo, String obdChecksum, Long obdRecordsCount) {
        OutboundCallFlow todayCallFlow = callFlowService.findRecordByCallStatus(fetchBy);
        todayCallFlow.setStatus(updateTo);
        todayCallFlow.setObdChecksum(obdChecksum);
        todayCallFlow.setObdRecordCount(obdRecordsCount);
        callFlowService.update(todayCallFlow);
    }
}

