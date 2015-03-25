package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.util.helper.DataValidationException;

import java.util.List;

/**
 *This interface provides methods to perform crud operations on Subscription object
 */
public interface SubscriptionService {

    /**
     * Gets active subscription by msisdn and pack name
     * @param msisdn String type object
     * @param packName SubscriptionPack type object
     * @return Subscription type object
     */
    Subscription getActiveSubscriptionByMsisdnPack(String msisdn, SubscriptionPack packName);

    /**
     * Gets active subscription by MctsId
     * @param mctsId String type object
     * @param packName SubscriptionPack type object
     * @param stateCode Long type object
     * @return Subscription type object
     */
    Subscription getActiveSubscriptionByMctsIdPack(String mctsId, SubscriptionPack packName, Long stateCode);

    /**
     * Gets actives users count
     * @return long type object
     */
    long getActiveUserCount();

    /**
     * Get subscription by mctsId and state code
     * @param mctsId String type object
     * @param id Long type object
     * @return Subscription type object
     */
    Subscription getSubscriptionByMctsIdState(String mctsId, Long id);

    /**
     * Deletes all subscriptions
     */
    void deleteAll();

    /**
     * Get active subscription packs by msisdn
     * @param msisdn String type object
     * @return List<SubscriptionPack> type object
     */
    List<SubscriptionPack> getActiveSubscriptionPacksByMsisdn(String msisdn);

    /**
     *  This method is used to insert/update subscription and subscriber
     *
     *  @param subscriber csv uploaded subscriber
     */
    void handleMctsSubscriptionRequestForChild(Subscriber subscriber, Channel channel)
            throws DataValidationException;

    /**
     * creates new subscriber and subscription
     * @param subscriber Subscriber type object
     * @param channel Channel type object
     * @param operatorCode String type object
     * @param circleCode String type object
     * @throws DataValidationException
     */
    void createNewSubscriberAndSubscription(Subscriber subscriber, Channel channel, String operatorCode, String circleCode)
            throws DataValidationException;

    /**
     *  This method is used to insert/update subscription and subscriber
     *
     *  @param subscriber csv uploaded subscriber
     */
    void handleMctsSubscriptionRequestForMother(Subscriber subscriber, Channel channel)
            throws DataValidationException;

    /**
     * This method deactivates the subscription corresponding to subscriptionId
     * @param subscriptionId Long type object
     * @param operatorCode String type object
     * @param circleCode String type object
     */
    void deactivateSubscription(Long subscriptionId, String operatorCode, String circleCode)
            throws DataValidationException;

    /**
     * This method subscription through IVR
     * @param subscriber object of type Susbcriber
     * @param operatorCode String type
     * @param circleCode String type
     * @param llcCode Integer type
     * @throws DataValidationException
     */
    void handleIVRSubscriptionRequest(Subscriber subscriber, String operatorCode, String circleCode, Integer llcCode)
        throws DataValidationException;
}
