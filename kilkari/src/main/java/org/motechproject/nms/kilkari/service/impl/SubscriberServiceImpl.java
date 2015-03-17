package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on Subscriber
 */

@Service("subscriberService")
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberDataService subscriberDataService;

    /**
     * update Subscriber in database
     *
     * @param record Subscriber type object
     */
    @Override
    public void update(Subscriber record) {
        subscriberDataService.update(record);

    }

    /**
     * create new Subscriber record  in database
     *
     * @param subscriber Subscriber type object
     */
    @Override
    public Subscriber create(Subscriber subscriber) {
        return subscriberDataService.create(subscriber);
    }

    /**
     * Delete all subscribers from database
     */
    @Override
    public void deleteAll() {
        subscriberDataService.deleteAll();

    }


}
