package org.motechproject.nms.kilkariobd.service.impl;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkariobd.domain.OutboundCallDetail;
import org.motechproject.nms.kilkariobd.event.handler.PurgeHandler;
import org.motechproject.nms.kilkariobd.repository.OutboundCallDetailDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to implement method defined in OutboundCallDetailService Interface
 */
@Service("outboundCallDetailService")
public class OutboundCallDetailServiceImpl implements OutboundCallDetailService {

    Logger logger = LoggerFactory.getLogger(OutboundCallDetailServiceImpl.class);
    
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
     * This method is used to delete OutboundCallDetail of those subscriber 
     * whose subscription have completed or deactivated n days earlier. 
     * Where n is configurable in Kilkari and also used to trigger perge event 
     * of Kilkari which will delete corresponding subscription and subscriber.
     */
    @Override
    public void purgeOutboundCallDetail() {
        List<Long> subscriptionIds = subscriptionService.getSubscriptionIdOfNDaysEarlierSubscription();
        for (Long subscriptionId : subscriptionIds) {
            List<OutboundCallDetail> outboundCallDetails = outboundCallDetailDataService.findRecordsByRequestId(Long.toString(subscriptionId));
            for (OutboundCallDetail outboundCallDetail : outboundCallDetails) {
                outboundCallDetailDataService.delete(outboundCallDetail);
                logger.debug("Deleted OutboundCallDetail record having subscriptionId[{}]", outboundCallDetail.getRequestId());
            }
        }
        subscriptionService.purgeOldSubscriptionSubscriberRecords();
    }
}
