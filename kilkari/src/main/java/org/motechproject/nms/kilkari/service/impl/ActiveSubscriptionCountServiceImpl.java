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
     * This method creates active user.
     * @param activeSubscriptionCount ActiveSubscriptionCount type object
     * @return ActiveUser type object
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
     * Checks ActiveUserCount record exists
     * @return true if exists else false
     */
    @Override
    public Boolean isActiveSubscriptionCountPresent() {
        return (activeSubscriptionCountDataService.count() > Constants.ACTIVE_SUBSCRIPTION_COUNT_ZERO);
    }

}
