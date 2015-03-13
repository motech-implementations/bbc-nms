package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("subscriberService")
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberDataService subscriberDataService;
    
    @Override
    public void update(Subscriber record) {
        subscriberDataService.update(record);

    }
    
    @Override
    public Subscriber create(Subscriber subscriber) {
        return subscriberDataService.create(subscriber);
    }
    
    @Override
    public void deleteAll() {
        subscriberDataService.deleteAll();

    }

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
