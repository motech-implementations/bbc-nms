package org.motechproject.nms.kilkariobd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.mds.filter.Filter;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.util.Order;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.dto.request.CdrNotificationRequest;
import org.motechproject.nms.kilkariobd.repository.OutboundCallFlowDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to implement method defined in OutboundCallFlowService Interface
 */
@Service("outboundCallFlowService")
public class OutboundCallFlowServiceImpl implements OutboundCallFlowService {

    @Autowired
    OutboundCallFlowDataService callFlowDataService;

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
            ParseDataHelper.raiseInvalidDataException(Constants.CDR_DETAIL_INFO, null);
        }
    }
}
