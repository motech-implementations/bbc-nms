package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.util.helper.DataValidationException;

import java.util.List;

public interface SubscriptionService {
    
    Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName);
    
    Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Long stateCode);
    
    long getActiveUserCount();

    Subscription getSubscriptionByMctsIdState(String mctsId, Long id);

    void deleteAll();

    List<SubscriptionPack> getActiveSubscriptionPacksByMsisdn(String msisdn);

    void handleMctsSubscriptionRequestForChild(Subscriber subscriber, Channel channel)
            throws DataValidationException;

    void createNewSubscriberAndSubscription(Subscriber subscriber, Channel channel, Operator operator)
            throws DataValidationException;

    void handleMctsSubscriptionRequestForMother(Subscriber subscriber, Channel channel)
            throws DataValidationException;

}
