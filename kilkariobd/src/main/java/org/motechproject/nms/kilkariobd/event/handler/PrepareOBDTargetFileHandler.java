package org.motechproject.nms.kilkariobd.event.handler;

import org.motechproject.mds.service.impl.csv.CsvImporterExporter;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkariobd.client.IvrHttpClient;
import org.motechproject.nms.kilkariobd.domain.*;
import org.motechproject.nms.kilkariobd.mapper.ReadByCSVMapper;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.ArrayList;
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

    Configuration configuration = configurationService.getConfiguration();

    public void prepareOBDTargetFile() {

        IvrHttpClient client = new IvrHttpClient();

        //delete all the records from outboundCallRequest before preparing fresh ObdTargetFile
        requestService.deleteAll();
        OutboundCallFlow callFlow = new OutboundCallFlow();
        callFlow.setStatus(CallFlowStatus.OUTBOUND_CALL_NOTIFICATION_EVENT_RECEIVED);
        callFlowService.create(callFlow);
        OutboundCallFlow olCallFlow = callFlowService.findRecordByCallStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED);
        createFreshOBDRecords();
        processCDRSummaryCSV(olCallFlow.getObdFileName());
        exportOutBoundCallRequest();
        processCDRDetail(olCallFlow.getObdFileName());
        olCallFlow.setStatus(CallFlowStatus.CDR_FILES_PROCESSED);
        callFlowService.update(olCallFlow);

        client.notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_PROCESSED_SUCCESSFULLY);
        //todo : send error if cdrsummary and cdrdetail has errors while processing

    }

    public void SubmitTargetFile() {

    }

    private Boolean isValidForRetry(Integer retryDayNumber, CallStatus finalStatus, ObdStatusCode statusCode) {
        return (finalStatus.equals(CallStatus.REJECTED) ||
                (finalStatus.equals(CallStatus.FAILED) &&
                        !(statusCode.equals(ObdStatusCode.OBD_FAILED_INVALIDNUMBER) || statusCode.equals(ObdStatusCode.OBD_DNIS_IN_DND)))) &&
                isValidRetryDayNumber(retryDayNumber);
    }

    private boolean isValidRetryDayNumber(Integer retryDayNumber) {
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
        String filename = "filePath";
        File file = new File(filename);
        try {
            FileWriter fos = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bf = new BufferedWriter(fos);

            csvImporterExporter.exportCsv("OUTBOUNDCALLREQUEST", bf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createFreshOBDRecords() {
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
                    //throw error
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
            //todo cdrCallSummaryCSV file name?
            cdrSummaryRecords = ReadByCSVMapper.readWithCsvMapReader("cdrSummary"  + obdFileName);
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
        //todo cdrCallDetailCSV file name?
        try {
            cdrDetailRecords = ReadByCSVMapper.readWithCsvMapReader("cdrDetail" + obdFileName);
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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

