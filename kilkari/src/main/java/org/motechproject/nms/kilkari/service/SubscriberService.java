package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.mds.annotations.LookupField;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;

public interface SubscriberService {

	void create(String name, String message);

    void add(Subscriber record);

    Subscriber findRecordByName(String recordName);

    List<Subscriber> getRecords();

    void delete(Subscriber record);

    void update(Subscriber record);
    
    public List<Subscriber> findRecordByMsisdn(String msisdn);
    
    public Subscriber create(Subscriber subscriber);
    
}
