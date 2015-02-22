package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Subscriber;

public interface SubscriberService {

    void create(String name, String message);

    void add(Subscriber record);

    Subscriber findRecordByName(String recordName);

    List<Subscriber> getRecords();

    void delete(Subscriber record);

    void update(Subscriber record);
    
    List<Subscriber> findRecordByMsisdn(String msisdn);
    
    Subscriber create(Subscriber subscriber);
    
}
