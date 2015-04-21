package org.motechproject.nms.kilkariobd.web;

import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.dto.request.CdrNotificationRequest;
import org.motechproject.nms.kilkariobd.dto.request.FileProcessedStatusRequest;
import org.motechproject.nms.kilkariobd.event.handler.PrepareOBDTargetFileHandler;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileNotificationController extends BaseController{
    Logger logger = LoggerFactory.getLogger(FileNotificationController.class);

    @Autowired
    private OutboundCallFlowService callFlowService;

    @RequestMapping(value = "/cdrFileNotification", method = RequestMethod.POST)
    @ResponseBody
    public void CDRFileNotification(@RequestBody CdrNotificationRequest apiRequest) throws DataValidationException{
        logger.debug("CDRFileNotification: started");
        logger.debug("CDRFileNotification Request Parameters");
        logger.debug("fileName : [" + apiRequest.getFileName() + "]");
        logger.debug("cdrSummary parameters");
        logger.debug("cdrFile : [" + apiRequest.getCdrSummary().getCdrFile() + "]");
        logger.debug("cdrChecksum : [" + apiRequest.getCdrSummary().getCdrChecksum() + "]");
        logger.debug("recordsCount : [%d]", apiRequest.getCdrSummary().getRecordsCount());
        logger.debug("cdrDetail parameters");
        logger.debug("cdrFile : [" + apiRequest.getCdrDetail().getCdrFile() + "]");
        logger.debug("cdrChecksum : [" + apiRequest.getCdrDetail().getCdrChecksum() + "]");
        logger.debug("recordsCount : [%d]", apiRequest.getCdrDetail().getRecordsCount());
        apiRequest.validateMandatoryParameters();
        //todo : update outboundcallflow record in DB.
        OutboundCallFlow callFlow = callFlowService.findRecordByFileName(apiRequest.getFileName());
        callFlow.setStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED);
        callFlow.setCdrDetailChecksum(apiRequest.getCdrDetail().getCdrChecksum());
        callFlow.setCdrDetailRecordCount(apiRequest.getCdrDetail().getRecordsCount());
        callFlow.setCdrSummaryChecksum(apiRequest.getCdrSummary().getCdrChecksum());
        callFlow.setCdrSummaryRecordCount(apiRequest.getCdrSummary().getRecordsCount());
        callFlowService.update(callFlow);
        logger.debug("CDRFileNotification: Ended");

    }

    @RequestMapping(value = "/obdFileProcessedNotification", method = RequestMethod.POST)
    public void FileProcessedStatusNotification(@RequestBody FileProcessedStatusRequest request) {
        logger.debug("FileProcessedStatusNotification: started");
        logger.debug("FileProcessedStatusNotification Request Parameters");
        logger.debug("fileName" + request.getFileName());
        logger.debug("fileProcessingStatus" + request.getCdrFileProcessingStatus().name());
        OutboundCallFlow callFlow = callFlowService.findRecordByFileName(request.getFileName());
        if (!request.getCdrFileProcessingStatus().equals(FileProcessingStatus.FILE_PROCESSED_SUCCESSFULLY)) {
            callFlow.setStatus(CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_PROCESSING_FAILED_AT_IVR);
            PrepareOBDTargetFileHandler handler = new PrepareOBDTargetFileHandler();
            handler.prepareOBDTargetFile();
        } else {
            callFlow.setStatus(CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_PROCESSED_AT_IVR);
        }
        callFlowService.update(callFlow);
    }
}
