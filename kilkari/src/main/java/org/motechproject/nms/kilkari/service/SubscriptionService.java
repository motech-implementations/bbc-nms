package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Subscription;

public interface SubscriptionService {

    void add(Subscription record);

    void delete(Subscription record);

    void update(Subscription record);
    
    Subscription findRecordIsDeactivatedBySystem(boolean isDeactivatedBySystem);
    
    Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName);
    
    Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Long stateCode);
    
    Subscription create(Subscription subscription);
    
    long getActiveUserCount();

    Subscription getSubscriptionByMctsIdState(String mctsId, Long id);

    void deleteAll();

}
