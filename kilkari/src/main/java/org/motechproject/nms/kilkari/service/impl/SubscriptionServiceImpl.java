package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import javax.jdo.Query;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.event.handler.MctsCsvHelper;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
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
    
    private static Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    
    public static final String PACK_48 = "48WEEK";
    public static final String PACK_72 = "72WEEK";
    
    public static final String SUBSCRIPTION_EXIST_ERR_DESC =
            "Upload Unsuccessful as Subscription to MSISDN already Exist";
    public static final String SUBSCRIPTION_EXIST_EXCEPTION_MSG =
            "Subscription to MSISDN already Exist";


    /**
     * Query to find list of Active and Pending subscription packs for given msisdn.
     */
    private class ActiveSubscriptionQuery implements QueryExecution<List<String>> {
        private String msisdn;
        private String resultParamName;

        public ActiveSubscriptionQuery(String msisdn, String resultParamName) {
            this.msisdn = msisdn;
            this.resultParamName = resultParamName;
        }

        @Override
        public List<String> execute(Query query, InstanceSecurityRestriction restriction) {
            query.setFilter("msisdn == '" + msisdn + "'");
            query.setFilter("subscriptionPack == ACTIVE or subscriptionPack == PENDING_ACTIVATION");
            query.setResult(resultParamName);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        subscriptionDataService.deleteAll();
    }

    @Override
    public void update(Subscription record) {
        subscriptionDataService.update(record);
    }
    
    @Override
    public Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.ACTIVE);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.PENDING_ACTIVATION);
        }
        return subscription;
    }

    @Override
    public Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Long stateCode) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.ACTIVE, stateCode);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.PENDING_ACTIVATION, stateCode);
        }
        return subscription;
    }

    @Override
    public Subscription create(Subscription subscription) {
        return subscriptionDataService.create(subscription); 
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
    public List<String> getActiveSubscriptionByMsisdn(String msisdn) {
        ActiveSubscriptionQuery query = new ActiveSubscriptionQuery(msisdn, "subscriptionPack");
        List<String> subscriptionPackList = subscriptionDataService.executeQuery(query);
        return subscriptionPackList;
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
        Subscription dbSubscription = getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), PACK_48);
        if (dbSubscription == null) { 
            
            logger.info("Not found active subscription from database based on msisdn[{}], packName[{}]", subscriber.getMsisdn(), PACK_48);
            /* Find subscription from database based on mctsid(ChildMcts), packName, status */
            dbSubscription = getActiveSubscriptionByMctsIdPack(subscriber.getChildMctsId(), PACK_48, subscriber.getState().getStateCode());
            if (dbSubscription == null) {
                logger.debug("Not found active subscription from database based on Childmctsid[{}], packName[{}]", subscriber.getChildMctsId(), PACK_48);
                /* Find subscription from database based on mctsid(MotherMcts), packName, status */
                dbSubscription = getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), PACK_72, subscriber.getState().getStateCode());
                if (dbSubscription == null) {
                    logger.debug("Not Found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), PACK_48);
                    createSubscriptionSubscriber(subscriber, channel);
                    
                } else {  /* Record found based on mctsid(MotherMcts) than */ 
                    logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_48, Status.ACTIVE);
                    Subscriber dbSubscriber = dbSubscription.getSubscriber();
                    MctsCsvHelper.populateDBSubscription(subscriber, dbSubscription, true, channel);
                    update(dbSubscription);
                    if (!subscriber.getChildDeath()) {
                        /* add new subscription for child */
                        Subscription newSubscription = MctsCsvHelper.populateNewSubscription(dbSubscriber, channel);
                        create(newSubscription);
                        dbSubscriber.getSubscriptionList().add(newSubscription);
                    }
                    updateDbSubscriber(subscriber, dbSubscriber); /* update subscriber info */
                
                }
            } else { /* Record found based on mctsid(ChildMcts) than */
                logger.info("Found active subscription from database based on Childmctsid[{}], packName[{}], status[{}]", subscriber.getChildMctsId(), PACK_48, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscriptionForChild(subscriber, dbSubscription, dbSubscriber, channel); /* update subscriber and subscription info */
            }
        } else { /* Record found based on msisdn than */
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_48, Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getChildMctsId())) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscriptionForChild(subscriber, dbSubscription, dbSubscriber, channel); /* update subscriber and subscription info */
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
        Subscription dbSubscription = getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), PACK_72);
        if (dbSubscription == null) {
            logger.debug("Not found active subscription from database based on msisdn[{}], packName[{}]", subscriber.getMsisdn(), PACK_72);
            /* Find subscription from database based on mctsid, packName, status */
            dbSubscription = getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), PACK_72, subscriber.getState().getStateCode());
            if (dbSubscription == null) {
                logger.debug("Not found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), PACK_72);
                createSubscriptionSubscriber(subscriber, channel);
                
            } else { /* Record found based on mctsid than update subscriber and subscription */
                logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_72, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscriptionForMother(subscriber, dbSubscription, dbSubscriber, channel);
            }
        } else {
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getMotherMctsId())) {
                logger.info("Found matching msisdn [{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscriptionForMother(subscriber, dbSubscription, dbSubscriber, channel);

            } else {
                throw new DataValidationException(SUBSCRIPTION_EXIST_EXCEPTION_MSG,
                        ErrorCategoryConstants.INCONSISTENT_DATA, SUBSCRIPTION_EXIST_ERR_DESC, "");
            }
        }
    }

    @Override
    public void createSubscriptionSubscriber(Subscriber subscriber, Channel channel)
            throws DataValidationException {
        Configuration configuration = configurationService.getConfiguration();
        long activeUserCount = getActiveUserCount();
        /* check for maximum allowed beneficiary */
        if (activeUserCount < configuration.getMaxAllowedActiveBeneficiaryCount()) {
            Subscriber dbSubscriber = subscriberDataService.create(subscriber); 
            Subscription newSubscription = MctsCsvHelper.populateNewSubscription(dbSubscriber, channel);
            create(newSubscription);
        } else {
            logger.info("Reached maximum beneficery count, can't add any more");
            throw new DataValidationException("Overload Beneficery" ,"Overload Beneficery" ,"Overload Beneficery");
        }
    }
    
    /**
     *  This method is used to update subscriber and subscription
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void updateSubscriberSubscriptionForChild(Subscriber subscriber, Subscription dbSubscription, Subscriber dbSubscriber, Channel channel) {
        
        if (subscriber.getChildDeath()) {
            updateSubscription(subscriber, dbSubscription, true, channel);
            
        } else {
            if (!dbSubscriber.getDob().equals(subscriber.getDob())) {
                updateSubscription(subscriber, dbSubscription, true, channel);
                Subscription newSubscription = MctsCsvHelper.populateNewSubscription(dbSubscriber, channel);
                create(newSubscription);
                dbSubscriber.getSubscriptionList().add(newSubscription);
            } else {
                updateSubscription(subscriber, dbSubscription, false, channel);
            }
        }

        updateDbSubscriber(subscriber, dbSubscriber);
    }
    
    /**
     *  This method is used to update subscriber and subscription
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void updateSubscriberSubscriptionForMother(Subscriber subscriber,
            Subscription dbSubscription, Subscriber dbSubscriber, Channel channel) {

        if (subscriber.getAbortion() || subscriber.getStillBirth() || subscriber.getMotherDeath()) {
            updateSubscription(subscriber, dbSubscription, true, channel);
        } else {
            if (!dbSubscriber.getLmp().equals(subscriber.getLmp())) {
                updateSubscription(subscriber, dbSubscription, true, channel);
                Subscription newSubscription = MctsCsvHelper.populateNewSubscription(dbSubscriber, channel);
                create(newSubscription);
                dbSubscriber.getSubscriptionList().add(newSubscription);
            } else {
                updateSubscription(subscriber, dbSubscription, false, channel);
            }
        }

        updateDbSubscriber(subscriber, dbSubscriber);

    }
    
    /**
     *  This method is used to update Subscription info in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     */
    private void updateSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag, Channel channel) {
        MctsCsvHelper.populateDBSubscription(subscriber, dbSubscription, statusFlag, channel);
        update(dbSubscription);
    }
    
    /**
     *  This method is used to update Subscriber info in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscriber database Subscriber
     */
    private void updateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {

        MctsCsvHelper.polpulateDbSubscriber(subscriber, dbSubscriber);
        subscriberDataService.update(dbSubscriber);

    }
}
