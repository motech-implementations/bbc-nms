package org.motechproject.nms.kilkariobd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkariobd.client.HttpClient;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.Configuration;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.dto.request.CdrNotificationRequest;
import org.motechproject.nms.kilkariobd.dto.request.FileProcessedStatusRequest;
import org.motechproject.nms.kilkariobd.event.handler.OBDTargetFileHandler;
import org.motechproject.nms.kilkariobd.helper.MD5Checksum;
import org.motechproject.nms.kilkariobd.repository.OutboundCallFlowDataService;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Class to implement method defined in OutboundCallFlowService Interface
 */
@Service("outboundCallFlowService")
public class OutboundCallFlowServiceImpl implements OutboundCallFlowService {

    @Autowired
    OutboundCallFlowDataService callFlowDataService;

    @Autowired
    @Qualifier("kilkariObdSettings")
    private static SettingsFacade settingsFacade;

    @Autowired
    private ConfigurationService configurationService;


    /**
     * Method to find OutboundCallFlow based on callFlow Status
     * @param status CallFlowStatus enum
     * @return OutboundCallFlow type object
     */
    @Override
    public OutboundCallFlow findRecordByCallStatus(CallFlowStatus status) {
        return callFlowDataService.findRecordByCallStatus(status);
    }

    /**
     * Method to create record of type OutboundCallFlow
     * @param record OutboundCallFlow type object
     * @return OutboundCallFlow type object
     */
    @Override
    public OutboundCallFlow create(OutboundCallFlow record) {
        return callFlowDataService.create(record);
    }

    /**
     * Method to update OutboundCallFlow record
     * @param record OutboundCallFlow
     * @return OutboundCallFlow
     */
    @Override
    public OutboundCallFlow update(OutboundCallFlow record) {
        return callFlowDataService.update(record);
    }

    /**
     * Method to find OutboundCallFlow based on ObdFileName
     * @param filename ObdFileName
     * @return OutboundCallFlow type object
     */
    @Override
    public OutboundCallFlow findRecordByFileName(String filename) {
       return callFlowDataService.findRecordByFileName(filename);
    }

    @Override
    public OutboundCallFlow findRecordByCreationDate(DateTime date) {
        return callFlowDataService.findRecordByCreationDate(date);
    }

    /**
     * Method to update OutboundCallFlow status based on CDR notification
     * @param cdrNotificationRequest Cdr notification object
     */
    public void handleCdrNotification(CdrNotificationRequest cdrNotificationRequest)
            throws DataValidationException {

        OutboundCallFlow callFlow = findRecordByFileName(cdrNotificationRequest.getFileName());
        if (callFlow != null) {
            callFlow.setStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED);
            callFlow.setCdrDetailChecksum(cdrNotificationRequest.getCdrDetail().getCdrChecksum());
            callFlow.setCdrDetailRecordCount(cdrNotificationRequest.getCdrDetail().getRecordsCount());
            callFlow.setCdrSummaryChecksum(cdrNotificationRequest.getCdrSummary().getCdrChecksum());
            callFlow.setCdrSummaryRecordCount(cdrNotificationRequest.getCdrSummary().getRecordsCount());
            update(callFlow);
        } else {
            /* If file not found then fail the request */
            ParseDataHelper.raiseApiParameterInvalidDataException(Constants.CDR_DETAIL_INFO, null);
        }
    }


    /**
     * Method to update OutboundCallFlow status based on OBD File processing status notification
     * @param request File processed status notification object
     */
    @Override
    public void handleFileProcessedStatusNotification(FileProcessedStatusRequest request)
            throws DataValidationException {

        String obdFileName = request.getFileName();
        OutboundCallFlow callFlow = findRecordByFileName(obdFileName);
        OBDTargetFileHandler handler = new OBDTargetFileHandler();

        if (callFlow != null) {
            if (request.getStatus() != FileProcessingStatus.FILE_PROCESSED_SUCCESSFULLY) {
                Settings settings = new Settings(settingsFacade);
                Configuration configuration = configurationService.getConfiguration();
                String localFileName = settings.getObdFileLocalPath() + obdFileName;
                String remoteFileName = configuration.getObdFilePathOnServer();

                HttpClient client = new HttpClient();

                /* Export the file */
                Long recordCount = handler.exportOutBoundCallRequest(localFileName);

                /* Update Checksum and Record count */
                String obdChecksum = MD5Checksum.findChecksum(localFileName);
                handler.updateChecksumAndRecordCount(obdFileName, obdChecksum, recordCount);

                handler.copyTargetObdFileOnServer(localFileName, remoteFileName, request.getFileName());
                client.notifyTargetFile(remoteFileName, callFlow.getObdChecksum(), callFlow.getObdRecordCount());

                handler.updateCallFlowStatus(obdFileName, CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_PROCESSING_FAILED_AT_IVR);
            } else {
                handler.updateCallFlowStatus(obdFileName, CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_PROCESSED_AT_IVR);
            }

        }else {
            ParseDataHelper.raiseApiParameterInvalidDataException(Constants.FILE_NAME, request.getFileName());
        }

    }
}
