package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;

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
            throws DataValidationException, NmsInternalServerError;

    /**
     *  This method is used to insert/update subscription and subscriber
     *
     *  @param subscriber csv uploaded subscriber
     */
    void handleMctsSubscriptionRequestForMother(Subscriber subscriber, Channel channel)
            throws DataValidationException, NmsInternalServerError;

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
        throws DataValidationException, NmsInternalServerError;

    /**
     * This method is used to delete subscriber and subscription 
     * which are deactivated or completed six week before
     */
    void deleteSubscriberSubscriptionAfter6Weeks();

    /**
     * This method is used to get those subscriber 
     * whose OBD message is to send today.
     */
    List<Subscription> getScheduledSubscriptions();

    /**
     * This method is used by kilkari-obd module for deactivating a subscription
     * and we have not to deactivate those subscription who have subscribed through
     * IVR and having deactivation reason 'MSISDN_IN_DND'.
     * 
     * @param subscriptionId Long type object
     * @param reason DeactivateReason
     */
    void deactivateSubscription(Long subscriptionId, DeactivationReason reason);
}
