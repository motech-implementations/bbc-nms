package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is used for crud operations on Subscription
 */
@Service("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionDataService subscriptionDataService;

    /**
     * deletes all subscriptions
     */
    @Override
    public void deleteAll() {
        subscriptionDataService.deleteAll();

    }

    /**
     * updates subscription
     * @param record Subscription type object
     */
    @Override
    public void update(Subscription record) {
        subscriptionDataService.update(record);

    }

    /**
     * gets active subscription by msisdn, packname
     * @param msisdn String type object
     * @param packName String type object
     * @return Subscription type object
     */
    @Override
    public Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.ACTIVE);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.PENDING_ACTIVATION);
        }
        return subscription;
    }

    /**
     * gets active subscription by mctsid, packname and state code
     * @param mctsId String type object
     * @param packName String type object
     * @param stateCode Long type object
     * @return Subscription type object
     */
    @Override
    public Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Long stateCode) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.ACTIVE, stateCode);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.PENDING_ACTIVATION, stateCode);
        }
        return subscription;
    }

    /**
     * Creates subscription
     * @param subscription Subscription type object
     * @return Subscription type value
     */
    @Override
    public Subscription create(Subscription subscription) {
        return subscriptionDataService.create(subscription); 
    }

    /**
     * gets active users count
     * @return long type value
     */
    @Override
    public long getActiveUserCount() {
        
        List<Subscription> activeRecord = subscriptionDataService.getSubscriptionByStatus(Status.ACTIVE);
        List<Subscription> pendingRecord = subscriptionDataService.getSubscriptionByStatus(Status.PENDING_ACTIVATION);
        return activeRecord.size() + pendingRecord.size();
    }

    /**
     * gets subscription by Mcts Id and State Code
     * @param mctsId String type object
     * @param stateCode Long type object
     * @return Subscription type object
     */
    @Override
    public Subscription getSubscriptionByMctsIdState(String mctsId, Long stateCode){
        return subscriptionDataService.getSubscriptionByMctsIdState(mctsId, stateCode);
    }
}
