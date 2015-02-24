package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscription;

public interface SubscriptionService {

    void create(String name, String message);

    void add(Subscription record);

    Subscription findRecordByName(String recordName);

    List<Subscription> getRecords();

    void delete(Subscription record);

    void update(Subscription record);
    
    Subscription findRecordIsDeactivatedBySystem(boolean isDeactivatedBySystem);
    
    Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName, Status status);
    
    Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Status status);
    
    Subscription create(Subscription subscription);
    
    long getActiveUserCount();

}
