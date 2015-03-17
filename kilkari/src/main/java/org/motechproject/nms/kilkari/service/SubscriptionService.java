package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.util.helper.DataValidationException;

import java.util.List;

public interface SubscriptionService {

    void update(Subscription record);
    
    Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName);
    
    Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Long stateCode);
    
    Subscription create(Subscription subscription);
    
    long getActiveUserCount();

    Subscription getSubscriptionByMctsIdState(String mctsId, Long id);

    void deleteAll();

    List<String> getActiveSubscriptionByMsisdn(String msisdn);

    void handleMctsSubscriptionRequestForChild(Subscriber subscriber, Channel channel)
            throws DataValidationException;

    void createSubscriptionSubscriber(Subscriber subscriber, Channel channel)
            throws DataValidationException;

    void handleMctsSubscriptionRequestForMother(Subscriber subscriber, Channel channel)
            throws DataValidationException;

}
