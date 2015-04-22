package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.SubscriptionMeasure;


/**
 *This interface provides methods to perform crud operations on SubscriptionMeasure object
 */
public interface SubscriptionMeasureService {

    /**
     * This method creates SubscriptionMeasure entry
     * @param subscriptionMeasure SubscriptionMeasure type object
     * @return SubscriptionMeasure type object
     */
    SubscriptionMeasure create(SubscriptionMeasure subscriptionMeasure);
    
}
