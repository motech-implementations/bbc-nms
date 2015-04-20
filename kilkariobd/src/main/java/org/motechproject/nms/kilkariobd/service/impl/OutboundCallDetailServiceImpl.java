package org.motechproject.nms.kilkariobd.service.impl;

import org.motechproject.nms.kilkariobd.domain.OutboundCallDetail;
import org.motechproject.nms.kilkariobd.repository.OutboundCallDetailDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.springframework.beans.factory.annotation.Autowired;

public class OutboundCallDetailServiceImpl implements OutboundCallDetailService {

    @Autowired
    private OutboundCallDetailDataService outboundCallDetailDataService;

    @Override
    public OutboundCallDetail create(OutboundCallDetail record) {
        return outboundCallDetailDataService.create(record);
    }
}
