package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;

public final class MctsCsvHelper  {
    
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
        dbSubscriber.setModifiedBy(subscriber.getModifiedBy());
    }
    
    public static Subscription populateNewSubscription(Subscriber dbSubscriber, Channel channel)  {
        
        Subscription newSubscription;
        newSubscription = new Subscription();
        newSubscription.setStatus(Status.PENDING_ACTIVATION);
        newSubscription.setChannel(channel);
        newSubscription.setMsisdn(dbSubscriber.getMsisdn());
        newSubscription.setStateCode(dbSubscriber.getState().getStateCode());
        newSubscription.setModifiedBy(dbSubscriber.getModifiedBy());
        newSubscription.setCreator(dbSubscriber.getCreator());
        newSubscription.setOwner(dbSubscriber.getOwner());
        newSubscription.setSubscriber(dbSubscriber);
        if (dbSubscriber.getBeneficiaryType()==BeneficiaryType.CHILD){
            newSubscription.setMctsId(dbSubscriber.getChildMctsId());
            newSubscription.setPackName(BeneficiaryType.CHILD.getPack(BeneficiaryType.CHILD));
        }else{
            newSubscription.setMctsId(dbSubscriber.getMotherMctsId());
            newSubscription.setPackName(BeneficiaryType.MOTHER.getPack(BeneficiaryType.MOTHER));
        }
        return newSubscription;
    }
    
    public static void populateDBSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag) {
        if (statusFlag) {
            dbSubscription.setStatus(Status.DEACTIVATED);
        }
        dbSubscription.setStateCode(subscriber.getState().getStateCode());
        dbSubscription.setChannel(Channel.MCTS);
        dbSubscription.setMsisdn(subscriber.getMsisdn());
        dbSubscription.setModifiedBy(subscriber.getModifiedBy());
    }
}
