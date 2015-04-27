package org.motechproject.nms.kilkariobd.service.impl;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.service.SubscriptionService;
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
    
    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * Method to create record of type OutboundCallDetail in database
     * @param record OutboundCallDetail type object
     * @return OutboundCallDetail type object
     */
    @Override
    public OutboundCallDetail create(OutboundCallDetail record) {
        return outboundCallDetailDataService.create(record);
    }
    
    /**
     * This method is used to perge records of OutboundCallDetail.
     * 
     */
    @Override
    public void purgeOutboundCallDetail() {
        List<Long> subscriptionIds = subscriptionService.getSubscriptionIdOfNDaysEarlierSubscription();
        for (Long subscriptionId : subscriptionIds) {
            List<OutboundCallDetail> outboundCallDetails = outboundCallDetailDataService.findRecordsByRequestId(Long.toString(subscriptionId));
            for (OutboundCallDetail outboundCallDetail : outboundCallDetails) {
                outboundCallDetailDataService.delete(outboundCallDetail);
            }
        }
        subscriptionService.purgeOldSubscriptionSubscriberRecords();
    }
}
