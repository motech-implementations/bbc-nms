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
    public void create(String name, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void add(Subscriber record) {
        

    }

    @Override
    public Subscriber findRecordByName(String recordName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Subscriber> getRecords() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Subscriber record) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Subscriber record) {
        // TODO Auto-generated method stub

    }
    
    public List<Subscriber> findRecordByMsisdn(String msisdn) { 
        return subscriberDataService.findRecordByMsisdn(msisdn);
    }

    @Override
    public Subscriber create(Subscriber subscriber) {
        return subscriberDataService.create(subscriber);
    }

}
