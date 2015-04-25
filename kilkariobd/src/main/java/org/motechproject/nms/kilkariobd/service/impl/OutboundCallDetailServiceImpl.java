package org.motechproject.nms.kilkariobd.service.impl;

import org.motechproject.nms.kilkariobd.domain.OutboundCallDetail;
import org.motechproject.nms.kilkariobd.repository.OutboundCallDetailDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to implement method defined in OutboundCallDetailService Interface
 */
@Service("outboundCallDetailService")
public class OutboundCallDetailServiceImpl implements OutboundCallDetailService {

    @Autowired
    private OutboundCallDetailDataService outboundCallDetailDataService;

    /**
     * Method to create record of type OutboundCallDetail in database
     * @param record OutboundCallDetail type object
     * @return OutboundCallDetail type object
     */
    @Override
    public OutboundCallDetail create(OutboundCallDetail record) {
        return outboundCallDetailDataService.create(record);
    }
}
