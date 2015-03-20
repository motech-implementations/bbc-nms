package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is used to create,delete and get Subscriber object
 */
@Service("subscriberService")
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberDataService subscriberDataService;

    /**
     * Updates Subscriber record
     * @param record Subscriber type object
     */
    @Override
    public void update(Subscriber record) {
        subscriberDataService.update(record);

    }

    /**
     * This method creates subscriber
     * @param subscriber Subscriber type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber create(Subscriber subscriber) {
        return subscriberDataService.create(subscriber);
    }

    /**
     * Deletes all subscribers from database
     */
    @Override
    public void deleteAll() {
        subscriberDataService.deleteAll();

    }

    /**
     * This method gets subscriber by msisdn
     * @param msisdn String type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber getSubscriberByMsisdn(String msisdn) {
        List<Subscriber> subscribers;
        subscribers =  subscriberDataService.findRecordByMsisdn(msisdn);
        if(subscribers !=null && subscribers.size() > 0) {
            return subscribers.get(0);
        }
        return null;
    }


}
