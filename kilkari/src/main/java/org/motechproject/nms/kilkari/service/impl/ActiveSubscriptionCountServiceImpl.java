package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.ActiveSubscriptionCount;
import org.motechproject.nms.kilkari.initializer.Initializer;
import org.motechproject.nms.kilkari.repository.ActiveSubscriptionCountDataService;
import org.motechproject.nms.kilkari.service.ActiveSubscriptionCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class creates and get active user count
 */
@Service("activeSubscriptionCountService")
public class ActiveSubscriptionCountServiceImpl implements ActiveSubscriptionCountService {

    @Autowired
    private ActiveSubscriptionCountDataService activeSubscriptionCountDataService;
    
    /**
     * This method creates active subscription count.
     * @param activeSubscriptionCount ActiveSubscriptionCount type object
     * @return ActiveSubscriptionCount type object
     */
    @Override
    public ActiveSubscriptionCount create(ActiveSubscriptionCount activeSubscriptionCount) {
        return activeSubscriptionCountDataService.create(activeSubscriptionCount);
    }

    /**
     * finds active user count by index
     * @return Long type object
     */
    @Override
    public Long getActiveSubscriptionCount() {
        return activeSubscriptionCountDataService.findActiveSubscriptionCountByIndex(Initializer.ACTIVEUSER_INDEX).getCount();
    }

    /**
     * Checks ActiveSubscriptionCount record exists
     * @return true if exists else false
     */
    @Override
    public Boolean isActiveSubscriptionCountPresent() {
        return (activeSubscriptionCountDataService.count() > Constants.ACTIVE_SUBSCRIPTION_COUNT_ZERO);
    }

    /**
     * TBD : For active subscription count perspective we just made a structure to increment Active Subscription
     * and method will be implemented in next scalability requirement. currently it is under discussion
     */
    @Override
    public void incrementActiveSubscriptionCount() {
         
    }

    /**
     * TBD : For active subscription count perspective we just made a structure to decrement Active Subscription
     * and method will be implemented in next scalability requirement. currently it is under discussion
     */
    @Override
    public void decrementActiveSubscriptionCount() {
        
    }

}
