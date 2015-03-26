package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ActiveSubscriptionCount;


/**
 * This interface provide methods to creates and get active user count
 */
public interface ActiveSubscriptionCountService {


    /**
     * This method creates ActiveSubscriptionCount record.
     * @param activeSubscriptionCount ActiveSubscriptionCount type object
     * @return ActiveSubscriptionCount type object
     */
    ActiveSubscriptionCount create(ActiveSubscriptionCount activeSubscriptionCount);

    /**
     * finds active subscription count by index
     * @return Long type object
     */
    Long getActiveSubscriptionCount();
    
    
    /**
     * Checks ActiveSubscriptionCount record exists
     * @return true if exists else false
     */
    Boolean isActiveSubscriptionCountPresent();
    
    /**
     * This method increments the active subscription count by one 
     */
    void incrementActiveSubscriptionCount();
    
    /**
     * This method decrements the active subscription count by one 
     */
    void decrementActiveSubscriptionCount();

}
