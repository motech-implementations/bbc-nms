package org.motechproject.nms.kilkariobd.web;

import org.motechproject.nms.kilkariobd.dto.request.CdrNotificationRequest;
import org.motechproject.nms.kilkariobd.dto.request.FileProcessedStatusRequest;
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

/**
 * This class register the controller methods for CDRFileNotification, and FileProcessedStatusNotification
 */
@Controller
public class FileNotificationController extends BaseController{
    Logger logger = LoggerFactory.getLogger(FileNotificationController.class);

    @Autowired
    private OutboundCallFlowService callFlowService;

    /**
     * Maps CDRFileNotification request to the controller
     * @param apiRequest CdrNotificationRequest
     * @throws DataValidationException
     */
    @RequestMapping(value = "/cdrFileNotification", method = RequestMethod.POST)
    @ResponseBody
    public void cdrFileNotification(@RequestBody CdrNotificationRequest apiRequest) throws DataValidationException{
        logger.debug("CDRFileNotification: started");
        logger.debug("CDRFileNotification Request Parameters");
        logger.debug("fileName : [" + apiRequest.getFileName() + "]");

        if (apiRequest.getCdrSummary() != null) {
            logger.debug("cdrSummary parameters");
            logger.debug("cdrFile : [" + apiRequest.getCdrSummary().getCdrFile() + "]");
            logger.debug("cdrChecksum : [" + apiRequest.getCdrSummary().getCdrChecksum() + "]");
            logger.debug("recordsCount : [%d]", apiRequest.getCdrSummary().getRecordsCount());
        }

        if (apiRequest.getCdrDetail() != null) {
            logger.debug("cdrDetail parameters");
            logger.debug("cdrFile : [" + apiRequest.getCdrDetail().getCdrFile() + "]");
            logger.debug("cdrChecksum : [" + apiRequest.getCdrDetail().getCdrChecksum() + "]");
            logger.debug("recordsCount : [%d]", apiRequest.getCdrDetail().getRecordsCount());

        }

        apiRequest.validateMandatoryParameters();

        callFlowService.handleCdrNotification(apiRequest);

        logger.debug("CDRFileNotification: Ended");

    }

    /**
     * Maps FileProcessedStatusNotification request to the controller
     * @param request FileProcessedStatusRequest
     */
    @RequestMapping(value = "/obdFileProcessedNotification", method = RequestMethod.POST)
    public void fileProcessedStatusNotification(@RequestBody FileProcessedStatusRequest request) throws DataValidationException{
        logger.debug("FileProcessedStatusNotification: started");
        logger.debug("FileProcessedStatusNotification Request Parameters");
        logger.debug("fileName" + request.getFileName());
        logger.debug("fileProcessingStatus" + request.getCdrFileProcessingStatus());

        request.validateMandatoryParams();

        callFlowService.handleFileProcessedStatusNotification(request);

        logger.debug("fileProcessedStatusNotification: Ended");
    }
}
