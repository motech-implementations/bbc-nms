package org.motechproject.nms.kilkari.service.impl;


import java.util.List;

import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.repository.CustomQueries;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.ActiveSubscriptionCountService;
import org.motechproject.nms.kilkari.service.CommonValidatorService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *This class is used to perform crud operations on Subscription object
 */
@Service("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionDataService subscriptionDataService;
    
    @Autowired
    private SubscriberService subscriberService;
    
    @Autowired
    private ConfigurationService configurationService;
    
    @Autowired
    private ActiveSubscriptionCountService activeSubscriptionCountService;

    @Autowired
    private CommonValidatorService commonValidatorService;

    private static Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    
    /**
     * Deletes all subscriptions
     */
    @Override
    public void deleteAll() {
        subscriptionDataService.deleteAll();
    }

    /**
     * Gets active subscription by msisdn and pack name
     * @param msisdn String type object
     * @param packName SubscriptionPack type object
     * @return Subscription type object
     */
    @Override
    public Subscription getActiveSubscriptionByMsisdnPack(String msisdn, SubscriptionPack packName) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.ACTIVE);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.PENDING_ACTIVATION);
        }
        return subscription;
    }

    /**
     * Gets active subscription by MctsId
     * @param mctsId String type object
     * @param packName SubscriptionPack type object
     * @param stateCode Long type object
     * @return Subscription type object
     */
    @Override
    public Subscription getActiveSubscriptionByMctsIdPack(String mctsId, SubscriptionPack packName, Long stateCode) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.ACTIVE, stateCode);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.PENDING_ACTIVATION, stateCode);
        }
        return subscription;
    }

    /**
     * Get subscription by mctsId and state code
     * @param mctsId String type object
     * @param stateCode Long type object
     * @return Subscription type object
     */
    @Override
    public Subscription getSubscriptionByMctsIdState(String mctsId, Long stateCode){
        return subscriptionDataService.getSubscriptionByMctsIdState(mctsId, stateCode);
    }


    /**
     * Get active subscription packs by msisdn
     * @param msisdn String type object
     * @return List<SubscriptionPack> type object
     */
    @Override
    public List<SubscriptionPack> getActiveSubscriptionPacksByMsisdn(String msisdn) {
        CustomQueries.ActiveSubscriptionQuery query = new CustomQueries.ActiveSubscriptionQuery(msisdn, "packName");
        return subscriptionDataService.executeQuery(query);
    }
    
    /**
     *  This method is used to insert/update subscription and subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     */
    @Override
    public void handleMctsSubscriptionRequestForChild(Subscriber subscriber, Channel channel)
            throws DataValidationException, NmsInternalServerError {

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
                    /* set deactivate reason in dbSubscriber */
                    updateDbSubscription(subscriber, dbSubscription, true, DeactivationReason.PACK_CHANGED);
                    
                    if (subscriber.getDeactivationReason() == DeactivationReason.NONE) {
                        /* add new subscription for child */
                        Subscription newSubscription = createNewSubscription(subscriber, dbSubscriber, channel, null);
                        dbSubscriber.getSubscriptionList().add(newSubscription);
                    }
                    updateDbSubscriber(subscriber, dbSubscriber); /* update subscriber info */
                
                }
            } else { /* Record found based on mctsid(ChildMcts) than */
                logger.info("Found active subscription from database for BeneficeryType[{}] based on mctsid[{}], packName[{}], status[{}]", subscriber.getBeneficiaryType(), mctsId, pack.toString(), Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                handleMotherRecordIfFoundInChildCsvRecord(subscriber, otherPack, otherMctsId, dbSubscriber);
                updateDbSubscriberAndSubscription(subscriber, dbSubscription, dbSubscriber, channel); /* update subscriber and subscription info */
            }
        } else { /* Record found based on msisdn than */
            logger.info("Found active subscription from database for BeneficeryType[{}] based on msisdn[{}], packName[{}], status[{}]", subscriber.getBeneficiaryType(), mctsId, pack.toString(), Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getSuitableMctsId())) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                handleMotherRecordIfFoundInChildCsvRecord(subscriber, otherPack, otherMctsId, dbSubscriber);
                updateDbSubscriberAndSubscription(subscriber, dbSubscription, dbSubscriber, channel); /* update subscriber and subscription info */
            } else { /* can't subscribe subscription for two phone num. */
                throw new DataValidationException(Constants.SUBSCRIPTION_EXIST_EXCEPTION_MSG,
                        ErrorCategoryConstants.INCONSISTENT_DATA, Constants.SUBSCRIPTION_EXIST_ERR_DESC, "");
            }
        }
    }

    /**
     * This method deactivate subscription of mother record, delete mother subscriber 
     * and link deactivated mother record with child subscriber
     *  
     * @param subscriber csv uploaded Subscriber object
     * @param otherPack mother subscription pack name
     * @param otherMctsId motherMctsId
     * @param dbSubscriber Subscriber type Object
     */
    private void handleMotherRecordIfFoundInChildCsvRecord(Subscriber subscriber,
            SubscriptionPack otherPack, String otherMctsId,
            Subscriber dbSubscriber) {
        if(subscriber.getMotherMctsId() != null) {
            Subscription dbSubscription = getActiveSubscriptionByMctsIdPack(otherMctsId, otherPack, subscriber.getState().getStateCode());
            if(dbSubscription != null){
                Subscriber dbMotherSubscriber = dbSubscription.getSubscriber();
                if(dbMotherSubscriber!=null) {
                    dbSubscription.setStatus(Status.DEACTIVATED);
                    dbSubscription.setDeactivationReason(DeactivationReason.PACK_CHANGED);
                    dbSubscriber.getSubscriptionList().add(dbSubscription);
                    dbMotherSubscriber.getSubscriptionList().clear();
                    subscriberService.delete(dbMotherSubscriber);
                }
            }
        }
    }
    
    
    /**
     *  This method is used to insert/update subscription and subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param channel Channel type object
     */
    @Override
    public void handleMctsSubscriptionRequestForMother(Subscriber subscriber, Channel channel )
            throws DataValidationException, NmsInternalServerError {

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
            logger.debug("Not found active subscription from database based on msisdn[{}], packName[{}]", subscriber.getMsisdn(), SubscriptionPack.PACK_72_WEEKS);
            /* Find subscription from database based on mctsid, packName, status */
            dbSubscription = getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), pack, subscriber.getState().getStateCode());
            if (dbSubscription == null) {
                logger.debug("Not found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), SubscriptionPack.PACK_72_WEEKS);
                createNewSubscriberAndSubscription(subscriber, channel);
                
            } else { /* Record found based on mctsid than update subscriber and subscription */
                logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), SubscriptionPack.PACK_72_WEEKS, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndSubscription(subscriber, dbSubscription, dbSubscriber, channel);
            }
        } else {
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), SubscriptionPack.PACK_72_WEEKS, Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getMotherMctsId())) {
                logger.info("Found matching msisdn [{}], packName[{}], status[{}]", subscriber.getMsisdn(), SubscriptionPack.PACK_72_WEEKS, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndSubscription(subscriber, dbSubscription, dbSubscriber, channel);

            } else {
                throw new DataValidationException(Constants.SUBSCRIPTION_EXIST_EXCEPTION_MSG,
                        ErrorCategoryConstants.INCONSISTENT_DATA, Constants.SUBSCRIPTION_EXIST_ERR_DESC, "");
            }
        }
    }

    private void createNewSubscriberAndSubscription(Subscriber subscriber, Channel channel)
            throws NmsInternalServerError {
        createNewSubscriberAndSubscription(subscriber, channel, null);
    }

    /**
     * creates new subscriber and subscription
     * @param subscriber Subscriber type object
     * @param channel Channel type object
     * @param operatorCode String type object
     * @throws DataValidationException
     */
    private void createNewSubscriberAndSubscription(Subscriber subscriber, Channel channel, String operatorCode)
            throws NmsInternalServerError {
            Configuration configuration = configurationService.getConfiguration();
            long activeUserCount = activeSubscriptionCountService.getActiveSubscriptionCount();
        /*If DeactivationReason is NONE then create Subscriber and subscription*/
        if (subscriber.getDeactivationReason() == DeactivationReason.NONE) {
            /* check for maximum allowed beneficiary */
            if (activeUserCount < configuration.getMaxAllowedActiveBeneficiaryCount()) {
                Subscriber dbSubscriber = null;
                if (subscriber.getBeneficiaryType().equals(BeneficiaryType.CHILD)) {
                    dbSubscriber = subscriberService.getSubscriberByMsisdnAndChildMctsId(subscriber.getMsisdn(), null);
                } else {
                    dbSubscriber = subscriberService.getSubscriberByMsisdnAndMotherMctsId(subscriber.getMsisdn(), null);
                }

                if (dbSubscriber != null) {
                    updateDbSubscriber(subscriber, dbSubscriber);
                } else {
                    dbSubscriber = subscriberService.create(subscriber);
                }

                createNewSubscription(subscriber, dbSubscriber, channel, operatorCode);
            } else {
                logger.info("Reached maximum Active Subscriptions Count, can't add any more");
                throw new NmsInternalServerError("Active Subscriptions Count Exceeded",
                        ErrorCategoryConstants.INCONSISTENT_DATA,
                        "Active Subscriptions Count Exceeded");
            }
        } else {
            logger.warn("Inconsistent Data : Upload unsuccessfull as stillBorn/Abortion/Death reported got non existing subscription");
            throw new NmsInternalServerError("Active Subscriptions Count Exceeded",
                    ErrorCategoryConstants.INCONSISTENT_DATA,
                    "Inconsistent Data : Upload unsuccessfull as stillBorn/Abortion/Death reported got non existing subscription");
        }

    }

    /**
     * This method subscription through IVR
     * @param subscriber object of type Susbcriber
     * @param operatorCode String type
     * @param circleCode String type
     * @param llcCode Integer type
     * @throws DataValidationException
     */
    @Override
    public void handleIVRSubscriptionRequest(Subscriber subscriber, String operatorCode, String circleCode,
                                             Integer llcCode) throws DataValidationException, NmsInternalServerError {
        commonValidatorService.validateCircle(circleCode);
        commonValidatorService.validateOperator(operatorCode);
        commonValidatorService.validateLanguageLocationCode(llcCode);

        createNewSubscriberAndSubscription(subscriber, Channel.IVR, operatorCode);
    }
    
    /**
     *  This method is used to update subscriber and subscription
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void updateDbSubscriberAndSubscription(Subscriber subscriber, Subscription dbSubscription,
                                                   Subscriber dbSubscriber, Channel channel) {
        
        if (subscriber.getDeactivationReason() != DeactivationReason.NONE) {
            updateDbSubscription(subscriber, dbSubscription, true, subscriber.getDeactivationReason());
            
        } else {
            if (!dbSubscriber.getDobLmp().equals(subscriber.getDobLmp())) {
                updateDbSubscription(subscriber, dbSubscription, true, DeactivationReason.PACK_SCHEDULE_CHANGED);
                Subscription newSubscription = createNewSubscription(subscriber, dbSubscriber, channel, null);
                dbSubscriber.getSubscriptionList().add(newSubscription);
            } else {
                updateDbSubscription(subscriber, dbSubscription, false, subscriber.getDeactivationReason());
            }
        }
        
        updateDbSubscriber(subscriber, dbSubscriber);
    }
    
    /**
     *  This method is used to create subscription
     *
     *  @param channel channel by which request is invoked
     *  @param operatorCode code of the operator
     *  @param dbSubscriber database subscriber
     */
    private Subscription createNewSubscription(Subscriber subscriber, Subscriber dbSubscriber, Channel channel,
                                               String operatorCode) {

        Subscription newSubscription = new Subscription();

        newSubscription.setMsisdn(subscriber.getMsisdn());
        newSubscription.setMctsId(subscriber.getSuitableMctsId());
        if (subscriber.getState() != null) {
            newSubscription.setStateCode(subscriber.getState().getStateCode());
        }
        newSubscription.setPackName(subscriber.getSuitablePackName());
        newSubscription.setChannel(channel);
        newSubscription.setStatus(Status.PENDING_ACTIVATION);
        newSubscription.setDeactivationReason(DeactivationReason.NONE);
        newSubscription.setOperatorCode(operatorCode);
        newSubscription.setModifiedBy(subscriber.getModifiedBy());
        newSubscription.setCreator(subscriber.getCreator());
        newSubscription.setOwner(subscriber.getOwner());
        newSubscription.setSubscriber(dbSubscriber);

        newSubscription =  subscriptionDataService.create(newSubscription);
        activeSubscriptionCountService.incrementActiveSubscriptionCount();
        
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
        if(DeactivationReason.PACK_CHANGED!=deactivationReason) {
            dbSubscription.setMctsId(subscriber.getSuitableMctsId());
        }
        dbSubscription.setStateCode(subscriber.getState().getStateCode());
        dbSubscription.setModifiedBy(subscriber.getModifiedBy());
        
        if (statusFlag) {
            dbSubscription.setStatus(Status.DEACTIVATED);
        }

        subscriptionDataService.update(dbSubscription);

        if (statusFlag) {
            activeSubscriptionCountService.decrementActiveSubscriptionCount();
        } else {
            activeSubscriptionCountService.incrementActiveSubscriptionCount();
        }
    }
    
    /**
     *  This method is used to update Subscriber info in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscriber database Subscriber
     */
    private void updateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {

        if(subscriber.getBeneficiaryType()==BeneficiaryType.MOTHER){
            dbSubscriber.setAge(subscriber.getAge());
            dbSubscriber.setAadharNumber(subscriber.getAadharNumber());
            dbSubscriber.setLmp(subscriber.getLmp());
        } else {
            dbSubscriber.setDob(subscriber.getDob());
        }
        dbSubscriber.setMsisdn(subscriber.getMsisdn());
        dbSubscriber.setName(subscriber.getName());
        dbSubscriber.setState(subscriber.getState());
        dbSubscriber.setDistrict(subscriber.getDistrict());
        dbSubscriber.setTaluka(subscriber.getTaluka());
        dbSubscriber.setHealthBlock(subscriber.getHealthBlock());
        dbSubscriber.setPhc(subscriber.getPhc());
        dbSubscriber.setSubCentre(subscriber.getSubCentre());
        dbSubscriber.setVillage(subscriber.getVillage());
        dbSubscriber.setMotherMctsId(subscriber.getMotherMctsId());
        dbSubscriber.setChildMctsId(subscriber.getChildMctsId());
        dbSubscriber.setBeneficiaryType(subscriber.getBeneficiaryType());
        dbSubscriber.setModifiedBy(subscriber.getModifiedBy());

        subscriberService.update(dbSubscriber);

    }

    /**
     * This method deactivates the subscription corresponding to subscriptionId
     * @param subscriptionId Long type object
     */
    @Override
    public void deactivateSubscription(Long subscriptionId, String operatorCode, String circleCode)
            throws DataValidationException{
        commonValidatorService.validateCircle(circleCode);
        commonValidatorService.validateOperator(operatorCode);

        Subscription subscription = subscriptionDataService.findById(subscriptionId);
        if (subscription != null) {
            subscription.setStatus(Status.DEACTIVATED);
            subscription.setDeactivationReason(DeactivationReason.USER_DEACTIVATED);
            subscriptionDataService.update(subscription);
            activeSubscriptionCountService.decrementActiveSubscriptionCount();
        }
    }

}
