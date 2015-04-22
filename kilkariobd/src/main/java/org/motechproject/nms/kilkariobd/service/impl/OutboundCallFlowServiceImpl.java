package org.motechproject.nms.kilkariobd.service.impl;

import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.repository.OutboundCallFlowDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
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
}
