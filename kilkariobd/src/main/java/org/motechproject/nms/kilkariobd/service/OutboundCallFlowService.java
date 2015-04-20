package org.motechproject.nms.kilkariobd.service;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;

public interface OutboundCallFlowService {

    public OutboundCallFlow findByCreateDate(DateTime date);
}
