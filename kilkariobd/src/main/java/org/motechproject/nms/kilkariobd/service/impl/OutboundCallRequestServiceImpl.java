package org.motechproject.nms.kilkariobd.service.impl;


import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;
import org.motechproject.nms.kilkariobd.repository.OutboundCallRequestDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("outboundCallRequestService")
public class OutboundCallRequestServiceImpl implements OutboundCallRequestService {

    @Autowired
    OutboundCallRequestDataService requestDataService;

    @Override
    public void deleteAll() {
        requestDataService.deleteAll();
    }

    @Override
    public OutboundCallRequest create(OutboundCallRequest record) {
        return requestDataService.create(record);
    }
}
