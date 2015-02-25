package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;

public final class MctsCsvHelper {
    
    static void polpulateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {
        
        dbSubscriber.setMsisdn(subscriber.getMsisdn());
        dbSubscriber.setName(subscriber.getName());
        dbSubscriber.setAge(subscriber.getAge());
        dbSubscriber.setState(subscriber.getState());
        dbSubscriber.setDistrict(subscriber.getDistrict());
        dbSubscriber.setTaluka(subscriber.getTaluka());
        dbSubscriber.setHealthBlockCode(subscriber.getHealthBlock());
        dbSubscriber.setPhc(subscriber.getPhc());
        dbSubscriber.setSubCentre(subscriber.getSubCentre());
        dbSubscriber.setVillage(subscriber.getVillage());
        dbSubscriber.setModifiedBy(subscriber.getModifiedBy());
    }
    
    static Subscription populateNewSubscription(Subscriber subscriber, Subscription dbSubscription, Subscriber dbSubscriber) {
        
        Subscription newSubscription;
        newSubscription = new Subscription();
        if (dbSubscription != null) {
            newSubscription.setOldSubscritptionId(dbSubscription);
        }
        newSubscription.setStatus(Status.PendingActivation);
        newSubscription.setChannel(Channel.MCTS);
        newSubscription.setMsisdn(subscriber.getMsisdn());
        newSubscription.setModifiedBy(subscriber.getModifiedBy());
        newSubscription.setCreator(subscriber.getCreator());
        newSubscription.setOwner(subscriber.getOwner());
        newSubscription.setSubscriber(dbSubscriber);
        return newSubscription;
    }
    
    static void populateSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag) {
        if (statusFlag) {
            dbSubscription.setStatus(Status.Deactivated);
        }
        dbSubscription.setStateCode(subscriber.getState().getStateCode());
        dbSubscription.setChannel(Channel.MCTS);
        dbSubscription.setMsisdn(subscriber.getMsisdn());
        dbSubscription.setModifiedBy(subscriber.getModifiedBy());
    }
}
