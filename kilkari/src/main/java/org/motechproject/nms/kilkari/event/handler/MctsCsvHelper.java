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

    private static final String MIGRATED_OUT_ENTRY = "3";
    private static final String MIGRATED_IN_ENTRY = "2";
    private static final String DEATH_ENTRY = "9";
    private static final String ACTIVE_ENTRY = "1";
    private static final String NONE = "None";
    private static final String SPONTANEOUS = "Spontaneous";
    private static final String MTP_L12_WEEKS = "MTP<12 Weeks";
    private static final String MTP_G12_WEEKS = "MTP>12 Weeks";
    
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
    
    /**
     * This method is used to validate EntryType possible values
     *
     * @param entryType csv uploaded entryType value
     * @return boolean according to valid or invalid value
     */
    static boolean checkValidEntryType(String entryType) {
        boolean isRightEntryType = false;

        if(entryType.equals(ACTIVE_ENTRY)){
            isRightEntryType = true;
        }else if (entryType.equals(DEATH_ENTRY)) {
            isRightEntryType = true;
        }else if (entryType.equals(MIGRATED_IN_ENTRY)) {
            isRightEntryType = true;
        }else if (entryType.equals(MIGRATED_OUT_ENTRY)) {
            isRightEntryType = true;
        }
        return isRightEntryType;
    }

    /**
     * This method is used to validate abortion's possible values
     *
     * @param abortion csv uploaded abortion value
     * @return boolean according to valid or invalid value
     */
    static boolean checkValidAbortionValue(String abortion) {
        boolean isRightAbortionValue = false ;
        
        if(abortion.equalsIgnoreCase(MTP_G12_WEEKS)){
            isRightAbortionValue = true;
        }else if (abortion.equalsIgnoreCase(MTP_L12_WEEKS)) {
            isRightAbortionValue = true;
        }else if (abortion.equalsIgnoreCase(SPONTANEOUS)) {
            isRightAbortionValue = true;
        }else if (abortion.equalsIgnoreCase(NONE)) {
            isRightAbortionValue = true;
        }
        return isRightAbortionValue;
   }
    
    
    
}
