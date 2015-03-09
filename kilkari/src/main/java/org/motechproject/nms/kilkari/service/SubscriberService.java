package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Subscriber;

public interface SubscriberService {

    void add(Subscriber record);

    List<Subscriber> getRecords();

    void delete(Subscriber record);

    void update(Subscriber record);
    
    List<Subscriber> findRecordByMsisdn(String msisdn);
    
    Subscriber create(Subscriber subscriber);

    void deleteAll();
    
}
