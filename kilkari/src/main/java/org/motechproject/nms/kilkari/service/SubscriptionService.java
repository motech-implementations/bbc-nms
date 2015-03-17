package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.Subscription;

/**
 * This interface is used for crud operations on Subscription
 */

public interface SubscriptionService {

    /**
     * updates subscription
     * @param record Subscription type object
     */
    void update(Subscription record);

    /**
     * gets active subscription by msisdn, packname
     * @param msisdn String type object
     * @param packName String type object
     * @return Subscription type object
     */
    Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName);

    /**
     * gets active subscription by mctsid, packname and state code
     * @param mctsId String type object
     * @param packName String type object
     * @param stateCode Long type object
     * @return Subscription type object
     */
    Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Long stateCode);

    /**
     * Creates subscription
     * @param subscription Subscription type object
     * @return Subscription type value
     */
    Subscription create(Subscription subscription);

    /**
     * gets active users count
     * @return long type value
     */
    long getActiveUserCount();

    /**
     * gets subscription by Mcts Id and State Code
     * @param mctsId String type object
     * @param id Long type object
     * @return Subscription type object
     */
    Subscription getSubscriptionByMctsIdState(String mctsId, Long id);

    /**
     * deletes all subscriptions
     */
    void deleteAll();

}
