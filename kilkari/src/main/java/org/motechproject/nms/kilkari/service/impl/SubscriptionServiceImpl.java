package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.repository.ActiveUserDataService;
import org.motechproject.nms.kilkari.repository.CustomeQueries;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionDataService subscriptionDataService;
    
    @Autowired
    private SubscriberDataService subscriberDataService;
    
    @Autowired
    private ConfigurationService configurationService;
    
    @Autowired
    private ActiveUserDataService activeUserDataService;

    @Autowired
    private OperatorService operatorService;
    
    private static Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    
    public static final String PACK_48 = "48WEEK";
    public static final String PACK_72 = "72WEEK";
    
    public static final String SUBSCRIPTION_EXIST_ERR_DESC =
            "Upload Unsuccessful as Subscription to MSISDN already Exist";
    public static final String SUBSCRIPTION_EXIST_EXCEPTION_MSG =
            "Subscription to MSISDN already Exist";




    @Override
    public void deleteAll() {
        subscriptionDataService.deleteAll();
    }

    @Override
    public Subscription getActiveSubscriptionByMsisdnPack(String msisdn, SubscriptionPack packName) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.ACTIVE);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.PENDING_ACTIVATION);
        }
        return subscription;
    }

    @Override
    public Subscription getActiveSubscriptionByMctsIdPack(String mctsId, SubscriptionPack packName, Long stateCode) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.ACTIVE, stateCode);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.PENDING_ACTIVATION, stateCode);
        }
        return subscription;
    }

    @Override
    public long getActiveUserCount() {
        
        List<Subscription> activeRecord = subscriptionDataService.getSubscriptionByStatus(Status.ACTIVE);
        List<Subscription> pendingRecord = subscriptionDataService.getSubscriptionByStatus(Status.PENDING_ACTIVATION);
        return activeRecord.size() + pendingRecord.size();
    }
    
    @Override
    public Subscription getSubscriptionByMctsIdState(String mctsId, Long stateCode){
        return subscriptionDataService.getSubscriptionByMctsIdState(mctsId, stateCode);
    }


    @Override
    public List<SubscriptionPack> getActiveSubscriptionPacksByMsisdn(String msisdn) {
        CustomeQueries.ActiveSubscriptionQuery query = new CustomeQueries.ActiveSubscriptionQuery(msisdn, "packName");
        return subscriptionDataService.executeQuery(query);
    }
    
    /**
     *  This method is used to insert/update subscription and subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     */
    @Override
    public void handleMctsSubscriptionRequestForChild(Subscriber subscriber, Channel channel) throws DataValidationException {

        /*
        Create new subscriber and subscription if there is no existing record for childMctsId or motherMctsId
             with msisdn matching the one in childCsvRecord.  Also check that number of existing
             active subscribers is not exceeding the value of Max Allowed Active Kilkari Subscribers.

        Update the subscriber record if there is an existing record
            - Having MCTS Id same as the one in childMctsCsv Record.
            - Having null or empty MCTS Id and MSISDN matching the one in childMctsCsv Record.

        Update an existing subscription’s status as Deactivated, if in  childMctsCsv Record
            - EntryType is Death i.e. child death is reported.
            - DOB is modified (also create a new subscription in this case)
            - motherMCTSid matches to the existing Subscription (having status as Active/PendingActivation),
              create new subscription as per child DOB.

         */

        /* Find subscription from database based on msisdn, packName, status */

        SubscriptionPack pack = subscriber.getSuitablePackName();
        String mctsId = null;
        SubscriptionPack otherPack = null;
        String otherMctsId = null;
        
        if(BeneficiaryType.CHILD == subscriber.getBeneficiaryType()) {
            mctsId = subscriber.getChildMctsId();
            otherMctsId = subscriber.getMotherMctsId();
            otherPack = SubscriptionPack.PACK_72_WEEKS;
        }else {
            mctsId = subscriber.getMotherMctsId();
            otherMctsId = subscriber.getChildMctsId();
            otherPack = SubscriptionPack.PACK_48_WEEKS;
        }
        
        Subscription dbSubscription = getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), pack);
        if (dbSubscription == null) { 
            
            logger.info("Not found active subscription from database for BeneficeryType[{}] based on msisdn[{}], packName[{}], status[{}]", subscriber.getBeneficiaryType(), subscriber.getMsisdn(), pack.toString(), Status.ACTIVE);
            /* Find subscription from database based on mctsid, packName, status */
            dbSubscription = getActiveSubscriptionByMctsIdPack(mctsId, pack, subscriber.getState().getStateCode());
            if (dbSubscription == null) {
                logger.debug("Not found active subscription from database for BeneficeryType[{}] based on mctsid[{}], packName[{}], status[{}]", subscriber.getBeneficiaryType(), mctsId, pack.toString(), Status.ACTIVE);
                /* Find subscription from database based on mctsid(MotherMcts), packName, status */
                dbSubscription = getActiveSubscriptionByMctsIdPack(otherMctsId, otherPack, subscriber.getState().getStateCode());
                if (dbSubscription == null) {
                    logger.debug("Not Found active subscription from database for BeneficeryType[{}] based on othermctsid[{}], packName[{}], status[{}]", subscriber.getBeneficiaryType(), otherMctsId, otherPack.toString(), Status.ACTIVE);
                    createNewSubscriberAndSubscription(subscriber, channel);
                    
                } else {  /* Record found based on mctsid(MotherMcts) than */ 
                    logger.info("Found active subscription from database for BeneficeryType[{}] based on otherMctsId[{}], packName[{}], status[{}]", subscriber.getBeneficiaryType(), otherMctsId, otherPack.toString(), Status.ACTIVE);
                    Subscriber dbSubscriber = dbSubscription.getSubscriber();
                    //set deactivate reason in dbSubscriber
                    updateDbSubscription(subscriber, dbSubscription, true, DeactivationReason.PACK_CHANGED);
                    
                    if (subscriber.getDeactivationReason() == DeactivationReason.NONE) {
                        /* add new subscription for child */
                        Subscription newSubscription = createNewSubscription(dbSubscriber, channel, null);
                        dbSubscriber.getSubscriptionList().add(newSubscription);
                    }
                    updateDbSubscriber(subscriber, dbSubscriber); /* update subscriber info */
                
                }
            } else { /* Record found based on mctsid(ChildMcts) than */
                logger.info("Found active subscription from database for BeneficeryType[{}] based on mctsid[{}], packName[{}], status[{}]", subscriber.getBeneficiaryType(), mctsId, pack.toString(), Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndSubscription(subscriber, dbSubscription, dbSubscriber, channel); /* update subscriber and subscription info */
            }
        } else { /* Record found based on msisdn than */
            logger.info("Found active subscription from database for BeneficeryType[{}] based on msisdn[{}], packName[{}], status[{}]", subscriber.getBeneficiaryType(), mctsId, pack.toString(), Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getSuitableMctsId())) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndSubscription(subscriber, dbSubscription, dbSubscriber, channel); /* update subscriber and subscription info */
            } else { /* can't subscribe subscription for two phone num. */
                throw new DataValidationException(SUBSCRIPTION_EXIST_EXCEPTION_MSG,
                        ErrorCategoryConstants.INCONSISTENT_DATA, SUBSCRIPTION_EXIST_ERR_DESC, "");
            }
        }
    }
    
    
    /**
     *  This method is used to insert/update subscription and subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     */
    @Override
    public void handleMctsSubscriptionRequestForMother(Subscriber subscriber, Channel channel) throws DataValidationException {

        /*
        Create a new Subscriber and Subscription record in NMS database, if
           - there is no existing subscription record with MSISDN and MCTSId (having status as
             Active/PendingActivation) matching the ones in motherMctsCsv Record. Also check that number of existing
             active subscribers is not exceeding the value of Max Allowed Active Kilkari Subscribers.

        Update the existing subscriber record (with MSISDN, motherMCTSId, Location, name , age and LMP), if a
           subscription record ((having status as Active/PendingActivation ) exists in NMS database
            - Having MCTS Id same as the one in motherMctsCsv Record.
            - Having null or empty MCTS Id and MSISDN matching the one in motherMctsCsv Record.

        Update an existing subscription’s status as Deactivated if in  motherMctsCsv Record
            - Number of outcome is 0 i.e. stillbirth is reported
            - EntryType is Death i.e. mother death is reported.
            - Abortion is not “none” i.e. abortion is reported.
            - LMP is modified (also create a new subscription in this case)

         */


        /* Find subscription from database based on msisdn, packName, status */
        
        SubscriptionPack pack = subscriber.getSuitablePackName();
        
        Subscription dbSubscription = getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), pack);
        if (dbSubscription == null) {
            logger.debug("Not found active subscription from database based on msisdn[{}], packName[{}]", subscriber.getMsisdn(), PACK_72);
            /* Find subscription from database based on mctsid, packName, status */
            dbSubscription = getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), pack, subscriber.getState().getStateCode());
            if (dbSubscription == null) {
                logger.debug("Not found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), PACK_72);
                createNewSubscriberAndSubscription(subscriber, channel);
                
            } else { /* Record found based on mctsid than update subscriber and subscription */
                logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_72, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndSubscription(subscriber, dbSubscription, dbSubscriber, channel);
            }
        } else {
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getMotherMctsId())) {
                logger.info("Found matching msisdn [{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndSubscription(subscriber, dbSubscription, dbSubscriber, channel);

            } else {
                throw new DataValidationException(SUBSCRIPTION_EXIST_EXCEPTION_MSG,
                        ErrorCategoryConstants.INCONSISTENT_DATA, SUBSCRIPTION_EXIST_ERR_DESC, "");
            }
        }
    }

    private void createNewSubscriberAndSubscription(Subscriber subscriber, Channel channel)
            throws DataValidationException {
        createNewSubscriberAndSubscription(subscriber, channel, null);
    }

    @Override
    public void createNewSubscriberAndSubscription(Subscriber subscriber, Channel channel, String operatorCode)
            throws DataValidationException {
        if (operatorCode != null) {
            Operator operator = operatorService.getRecordByCode(operatorCode);
        }
        Configuration configuration = configurationService.getConfiguration();
        long activeUserCount = getActiveUserCount();
        /* check for maximum allowed beneficiary */
        if (activeUserCount < configuration.getMaxAllowedActiveBeneficiaryCount()) {
            Subscriber dbSubscriber = subscriberDataService.create(subscriber); 
            if (dbSubscriber.getDeactivationReason() == DeactivationReason.NONE) {
                createNewSubscription(dbSubscriber, channel, operatorCode);
            }
        } else {
            logger.info("Reached maximum beneficiary count, can't add any more");
            throw new DataValidationException("Beneficiary Count Exceeded" ,"Beneficiary Count Exceeded" , null);
        }
    }
    
    /**
     *  This method is used to update subscriber and subscription
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void updateDbSubscriberAndSubscription(Subscriber subscriber, Subscription dbSubscription, Subscriber dbSubscriber, Channel channel) {
        
        if (subscriber.getDeactivationReason() != DeactivationReason.NONE) {
            updateDbSubscription(subscriber, dbSubscription, true, subscriber.getDeactivationReason());
            
        } else {
            if (!dbSubscriber.getDobLmp().equals(subscriber.getDobLmp())) {
                updateDbSubscription(subscriber, dbSubscription, true, DeactivationReason.PACK_SCHEDULE_CHANGED);
                Subscription newSubscription = createNewSubscription(dbSubscriber, channel, null);
                dbSubscriber.getSubscriptionList().add(newSubscription);
            } else {
                updateDbSubscription(subscriber, dbSubscription, false, subscriber.getDeactivationReason());
            }
        }
        
        updateDbSubscriber(subscriber, dbSubscriber);
    }
    
    private Subscription createNewSubscription(Subscriber dbSubscriber, Channel channel, String operatorCode) {

        Subscription newSubscription = new Subscription();

        newSubscription.setMsisdn(dbSubscriber.getMsisdn());
        newSubscription.setMctsId(dbSubscriber.getSuitableMctsId());
        newSubscription.setStateCode(dbSubscriber.getState().getStateCode());
        newSubscription.setPackName(dbSubscriber.getSuitablePackName());
        newSubscription.setChannel(channel);
        newSubscription.setStatus(Status.PENDING_ACTIVATION);
        newSubscription.setDeactivationReason(DeactivationReason.NONE);
        newSubscription.setOperatorCode(operatorCode);
        newSubscription.setModifiedBy(dbSubscriber.getModifiedBy());
        newSubscription.setCreator(dbSubscriber.getCreator());
        newSubscription.setOwner(dbSubscriber.getOwner());
        newSubscription.setSubscriber(dbSubscriber);

        newSubscription =  subscriptionDataService.create(newSubscription);
        //activeUserDataService.executeSQLQuery(new CustomeQueries.ActiveUserCountIncrementQuery());
        
        return newSubscription;
    }
    
    /**
     *  This method is used to update Subscription info in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     */
    private void updateDbSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag,
                                      DeactivationReason deactivationReason) {
        
        dbSubscription.setDeactivationReason(deactivationReason);
        dbSubscription.setMsisdn(subscriber.getMsisdn());
        dbSubscription.setMctsId(subscriber.getSuitableMctsId());
        dbSubscription.setStateCode(subscriber.getState().getStateCode());
        dbSubscription.setModifiedBy(subscriber.getModifiedBy());
        
        if (statusFlag) {
//            deactivateSubscription()
            dbSubscription.setStatus(Status.DEACTIVATED);
        }

        subscriptionDataService.update(dbSubscription);

        if (statusFlag) {
           // activeUserDataService.executeSQLQuery(new CustomeQueries.ActiveUserCountDecrementQuery());
        } else {
           // activeUserDataService.executeSQLQuery(new CustomeQueries.ActiveUserCountIncrementQuery());
        }
    }
    
    /**
     *  This method is used to update Subscriber info in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscriber database Subscriber
     */
    private void updateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {

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

        subscriberDataService.update(dbSubscriber);

    }

    @Override
    public void deactivateSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionDataService.findById(subscriptionId);
        if (subscription != null) {
            subscription.setStatus(Status.DEACTIVATED);
            subscriptionDataService.update(subscription);
           // activeUserDataService.executeSQLQuery(new CustomeQueries.ActiveUserCountDecrementQuery());
        }
    }
}
