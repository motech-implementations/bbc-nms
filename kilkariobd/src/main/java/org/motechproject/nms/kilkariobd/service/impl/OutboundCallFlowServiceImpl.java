package org.motechproject.nms.kilkariobd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.repository.OutboundCallFlowDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("outboundCallFlowService")
public class OutboundCallFlowServiceImpl implements OutboundCallFlowService {

    @Autowired
    OutboundCallFlowDataService callFlowDataService;

    @Override
    public OutboundCallFlow findRecordByCallStatus(CallFlowStatus status) {
        return callFlowDataService.findRecordByCallStatus(status);
    }

    @Override
    public OutboundCallFlow create(OutboundCallFlow record) {
        return callFlowDataService.create(record);
    }

    @Override
    public OutboundCallFlow update(OutboundCallFlow record) {
        return callFlowDataService.update(record);
    }

    @Override
    public OutboundCallFlow findRecordByFileName(String filename) {
       return callFlowDataService.findRecordByFileName(filename);
    }
}