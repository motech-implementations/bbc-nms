package org.motechproject.nms.kilkari.service.impl;

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

    private final Long COUNT_ZERO = 0L;
    
    @Autowired
    private ActiveSubscriptionCountDataService activeSubscriptionCountDataService;
    
    /**
     * This method creates active user.
     * @param activeUser ActiveUser type object
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
    public Long getActiveUserCount() {
        return activeSubscriptionCountDataService.findActiveSubscriptionCountByIndex(Initializer.ACTIVEUSER_INDEX).getActiveUserCount();
    }

    /**
     * Checks ActiveUserCount record exists
     * @return true if exists else false
     */
    @Override
    public Boolean isActiveSubscriptionCountPresent() {
        return (activeSubscriptionCountDataService.count() > COUNT_ZERO);
        
    }

}
