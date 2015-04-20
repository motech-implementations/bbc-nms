package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.SubscriptionMeasure;
import org.motechproject.nms.kilkari.repository.SubscriptionMeasureDataService;
import org.motechproject.nms.kilkari.service.SubscriptionMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to create,delete and get Subscriber object
 */
@Service("subscriptionMeasureService")
public class SubscriptionMeasureServiceImpl implements SubscriptionMeasureService {

    @Autowired
    private SubscriptionMeasureDataService subscriptionMeasureDataService;

    /**
     * This method creates SubscriptionMeasure
     * @param subscriptionMeasure SubscriptionMeasure type object
     * @return SubscriptionMeasure type object
     */
    @Override
    public SubscriptionMeasure create(SubscriptionMeasure subscriptionMeasure) {
        return subscriptionMeasureDataService.create(subscriptionMeasure);
    }

}
