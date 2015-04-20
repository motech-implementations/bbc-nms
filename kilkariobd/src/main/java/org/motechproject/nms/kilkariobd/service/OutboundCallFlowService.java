package org.motechproject.nms.kilkariobd.service;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;

public interface OutboundCallFlowService {

    public OutboundCallFlow findRecordByCallStatus(CallFlowStatus status);

    public OutboundCallFlow create(OutboundCallFlow record);

    public OutboundCallFlow update(OutboundCallFlow record);
}
