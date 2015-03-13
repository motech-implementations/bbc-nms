package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
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
    public static void polpulateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {
        
        dbSubscriber.setMsisdn(subscriber.getMsisdn());
        dbSubscriber.setName(subscriber.getName());
        dbSubscriber.setAge(subscriber.getAge());
        dbSubscriber.setState(subscriber.getState());
        dbSubscriber.setDistrict(subscriber.getDistrict());
        dbSubscriber.setTaluka(subscriber.getTaluka());
        dbSubscriber.setHealthBlock(subscriber.getHealthBlock());
        dbSubscriber.setPhc(subscriber.getPhc());
        dbSubscriber.setSubCentre(subscriber.getSubCentre());
        dbSubscriber.setVillage(subscriber.getVillage());
        dbSubscriber.setMotherMctsId(subscriber.getMotherMctsId());
        dbSubscriber.setChildMctsId(subscriber.getChildMctsId());
        dbSubscriber.setDob(subscriber.getDob());
        dbSubscriber.setLmp(subscriber.getLmp());
        dbSubscriber.setBeneficiaryType(subscriber.getBeneficiaryType());
        dbSubscriber.setModifiedBy(subscriber.getModifiedBy());
    }

    /**
     * This method is used to create and populate the Subscription object for new subscription
     * @param dbSubscriber Subscriber type object
     * @param channel channel of subscription
     * @return Subscription type object
     */
     public static Subscription populateNewSubscription(Subscriber dbSubscriber, Channel channel)  {

        Subscription newSubscription;
        newSubscription = new Subscription();
        newSubscription.setMsisdn(dbSubscriber.getMsisdn());
        newSubscription.setMctsId(dbSubscriber.getSuitableMctsId());
        newSubscription.setStateCode(dbSubscriber.getState().getStateCode());
        newSubscription.setPackName(dbSubscriber.getSuitablePackName());
        newSubscription.setChannel(channel);
        newSubscription.setStatus(Status.PENDING_ACTIVATION);
        newSubscription.setDeactivationReason(DeactivationReason.NONE);
        newSubscription.setModifiedBy(dbSubscriber.getModifiedBy());
        newSubscription.setCreator(dbSubscriber.getCreator());
        newSubscription.setOwner(dbSubscriber.getOwner());
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
    
    public static void populateDbSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag, Channel channel) {
        if (statusFlag) {
            dbSubscription.setStatus(Status.DEACTIVATED);
        }
        dbSubscription.setMsisdn(subscriber.getMsisdn());
        dbSubscription.setMctsId(subscriber.getSuitableMctsId());
        dbSubscription.setStateCode(subscriber.getState().getStateCode());
        dbSubscription.setChannel(channel);
        dbSubscription.setDeactivationReason(subscriber.getDeactivationReason());
        dbSubscription.setModifiedBy(subscriber.getModifiedBy());
        //dbSubscription.setPackName(subscriber.getSuitablePackName());
    }

}
