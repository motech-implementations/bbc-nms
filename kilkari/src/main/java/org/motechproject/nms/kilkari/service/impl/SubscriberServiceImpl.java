package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("subscriberService")
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberDataService subscriberDataService;
    

    @Override
    public void add(Subscriber record) {
        subscriberDataService.create(record);
    }


    @Override
    public List<Subscriber> getRecords() {
        return subscriberDataService.retrieveAll();
    }

    @Override
    public void delete(Subscriber record) {
        subscriberDataService.delete(record);

    }
    
    @Override
    public void deleteAll() {
        subscriberDataService.deleteAll();

    }

    @Override
    public void update(Subscriber record) {
        subscriberDataService.update(record);

    }
    
    public List<Subscriber> findRecordByMsisdn(String msisdn) { 
        return subscriberDataService.findRecordByMsisdn(msisdn);
    }

    @Override
    public Subscriber create(Subscriber subscriber) {
        return subscriberDataService.create(subscriber);
    }

}
