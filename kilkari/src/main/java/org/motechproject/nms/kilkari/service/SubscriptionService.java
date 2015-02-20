package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;

public interface SubscriptionService {

	void create(String name, String message);

    void add(Subscription record);

    Subscription findRecordByName(String recordName);

    List<Subscription> getRecords();

    void delete(Subscription record);

    void update(Subscription record);
    
    public Subscription findRecordIsDeactivatedBySystem(boolean isDeactivatedBySystem);
}
