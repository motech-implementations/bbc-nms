package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;

/**
 * This class provides methods to populate Subscriber and Subscription 
 * with information received from Mcts Csv records.
 */
public final class MctsCsvHelper {

    /**
     * This method id used to populate the existing Subscriber Object
     * @param subscriber Subscriber type object
     * @param dbSubscriber Subscriber type object
     */
    static void polpulateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {
        
        dbSubscriber.setMsisdn(subscriber.getMsisdn());
        dbSubscriber.setName(subscriber.getName());
        dbSubscriber.setState(subscriber.getState());
        dbSubscriber.setDistrict(subscriber.getDistrict());
        dbSubscriber.setTaluka(subscriber.getTaluka());
        dbSubscriber.setHealthBlock(subscriber.getHealthBlock());
        dbSubscriber.setPhc(subscriber.getPhc());
        dbSubscriber.setSubCentre(subscriber.getSubCentre());
        dbSubscriber.setVillage(subscriber.getVillage());
        dbSubscriber.setModifiedBy(subscriber.getModifiedBy());
    }

    /**
     * This method is used to create and populate the Subscription object for new subscription
     * @param subscriber Subscriber type object
     * @param dbSubscriber Subscriber type object
     * @return Subscription type object
     */
    static Subscription populateNewSubscription(Subscriber subscriber, Subscriber dbSubscriber) {

        Subscription newSubscription;
        newSubscription = new Subscription();
        newSubscription.setStatus(Status.PENDING_ACTIVATION);
        newSubscription.setChannel(Channel.MCTS);
        newSubscription.setMsisdn(subscriber.getMsisdn());
        newSubscription.setStateCode(subscriber.getState().getStateCode());
        newSubscription.setModifiedBy(subscriber.getModifiedBy());
        newSubscription.setCreator(subscriber.getCreator());
        newSubscription.setOwner(subscriber.getOwner());
        newSubscription.setSubscriber(dbSubscriber);
        return newSubscription;
    }

    /**
     * This method is used to populate the existing Subscription object
     *
     * @param subscriber Subscriber type object
     * @param dbSubscription Subscription type object
     * @param statusFlag boolean type object
     */
    static void populateDbSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag) {

        if (statusFlag) {
            dbSubscription.setStatus(Status.DEACTIVATED);
        }
        dbSubscription.setStateCode(subscriber.getState().getStateCode());
        dbSubscription.setChannel(Channel.MCTS);
        dbSubscription.setMsisdn(subscriber.getMsisdn());
        dbSubscription.setModifiedBy(subscriber.getModifiedBy());
    }
}
