package org.motechproject.nms.kilkariobd.service;

import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;

public interface OutboundCallRequestService {

    public void deleteAll();

    public OutboundCallRequest create(OutboundCallRequest record);
}
