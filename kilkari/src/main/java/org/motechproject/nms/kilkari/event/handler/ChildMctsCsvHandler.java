package org.motechproject.nms.kilkari.event.handler;

import java.util.List;
import java.util.Map;

import org.apache.commons.digester.SetRootRule;
import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.LocationValidatorService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsUtils;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used to handle to success 
 * and failure event of csv upload of ChildMcts
 */
@Component
public class ChildMctsCsvHandler {

    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_UPDATED_IDS = CSV_IMPORT_PREFIX + "updated_ids";
    public static final String CSV_IMPORT_CREATED_COUNT = CSV_IMPORT_PREFIX + "created_count";
    public static final String CSV_IMPORT_UPDATED_COUNT = CSV_IMPORT_PREFIX + "updated_count";
    public static final String CSV_IMPORT_TOTAL_COUNT = CSV_IMPORT_PREFIX + "total_count";
    public static final String CSV_IMPORT_FAILURE_MSG = CSV_IMPORT_PREFIX + "failure_message";
    public static final String CSV_IMPORT_FAILURE_STACKTRACE = CSV_IMPORT_PREFIX + "failure_stacktrace";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";
    public static final String PACK_48 = "48WEEK";
    public static final String PACK_72 = "72WEEK";
    public static final String CHILD_DEATH_NINE = "9";
    public static final String SUBSCRIPTION_EXIST_ERR_DESC =
            "Upload Unsuccessful as Subscription to MSISDN already Exist";
    public static final String SUBSCRIPTION_EXIST_EXCEPTION_MSG =
            "Subscription to MSISDN already Exist";


    private ChildMctsCsvService childMctsCsvService;

    private SubscriptionService subscriptionService;

    private LocationValidatorService locationValidator;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private SubscriberService subscriberService;
    
    private ConfigurationService configurationService;
    
    private static Logger logger = LoggerFactory.getLogger(ChildMctsCsvHandler.class);
    
    @Autowired
    public ChildMctsCsvHandler(ChildMctsCsvService childMctsCsvService, 
            SubscriptionService subscriptionService,
            SubscriberService subscriberService,
            LocationValidatorService locationValidator,
            BulkUploadErrLogService bulkUploadErrLogService,
            ConfigurationService configurationService){
        this.childMctsCsvService = childMctsCsvService;
        this.subscriptionService = subscriptionService;
        this.locationValidator = locationValidator;
        this.subscriberService = subscriberService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.configurationService = configurationService;

    }

    /**
     * This method is used to process record when ChildMctsCsv upload is successful.
     * 
     * @param motechEvent This is motechEvent having uploaded record details 
     */
    @MotechListener(subjects = "mds.crud.kilkari.ChildMctsCsv.csv-import.success")
    public void childMctsCsvSuccess(MotechEvent motechEvent) {
        logger.info("Success[childMctsCsvSuccess] method start for ChildMctsCsv");
        Map<String, Object> parameters = motechEvent.getParameters();
        List<Long> uploadedIDs = (List<Long>) parameters.get(CSV_IMPORT_CREATED_IDS);
        String csvFileName = (String) parameters.get(CSV_IMPORT_FILE_NAME);
        
        logger.info("Processing Csv file[{}]", csvFileName);
        BulkUploadStatus uploadedStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        DateTime timeOfUpload = NmsUtils.getCurrentTimeStamp();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(timeOfUpload);
        errorDetails.setRecordType(RecordType.CHILD_MCTS);
        
        ChildMctsCsv childMctsCsv = null;
        String userName = null;
        
        for (Long id : uploadedIDs) {
            try {
                logger.info("Processing record id[{}]", id);
                childMctsCsv = childMctsCsvService.findRecordById(id);
                
                if (childMctsCsv != null) {
                    logger.info("Record found in database for record id[{}]", id);
                    userName = childMctsCsv.getOwner();
                    Subscriber subscriber = childMctsToSubscriberMapper(childMctsCsv);
                    insertSubscriptionSubccriber(subscriber);
                    uploadedStatus.incrementSuccessCount();
                } else {
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    logger.error("Record not found for uploaded id [{}]", id);
                    uploadedStatus.incrementFailureCount();
                }
                logger.info("Processing finished for record id[{}]", id);
            } catch (DataValidationException dve) {
                logger.error("DataValidationException ::::", dve.getMessage());
                errorDetails.setRecordDetails(childMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                uploadedStatus.incrementFailureCount();

            } catch (Exception e) {
                errorDetails.setRecordDetails("Some Error Occurred");
                errorDetails.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetails.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                uploadedStatus.incrementFailureCount();
                
            } finally {
                if (childMctsCsv != null) {
                    childMctsCsvService.delete(childMctsCsv);
                }
            }
        }
        uploadedStatus.setUploadedBy(userName);
        uploadedStatus.setBulkUploadFileName(csvFileName);
        uploadedStatus.setTimeOfUpload(timeOfUpload);
        
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(uploadedStatus);
        logger.info("Success[childMctsCsvSuccess] method finished for ChildMctsCsv");
    }

    /**
     *  This method is used to validate csv uploaded record 
     *  and map Child mcts to subscriber
     * 
     *  @param childMctsCsv csv uploaded record
     */
    private Subscriber childMctsToSubscriberMapper(ChildMctsCsv childMctsCsv) throws DataValidationException {

        Subscriber childSubscriber = new Subscriber();
        
        logger.info("Validation and map to entity process start");
        Long stateCode = ParseDataHelper.parseLong("State Code", childMctsCsv.getStateCode(),  true);
        State state = locationValidator.stateConsistencyCheck(stateCode);

        Long districtCode = ParseDataHelper.parseLong("District Code", childMctsCsv.getDistrictCode(), true);
        District district = locationValidator.districtConsistencyCheck(state, districtCode);

        String talukaCode = ParseDataHelper.parseString("Taluka Code", childMctsCsv.getTalukaCode(), false);
        Taluka taluka = locationValidator.talukaConsistencyCheck(district, talukaCode);

        Long healthBlockCode = ParseDataHelper.parseLong("Health Block Code", childMctsCsv.getHealthBlockCode(), false);
        HealthBlock healthBlock = locationValidator.healthBlockConsistencyCheck(talukaCode, taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.parseLong("Phc Code", childMctsCsv.getPhcCode(), false);
        HealthFacility healthFacility = locationValidator.phcConsistencyCheck(healthBlockCode, healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.parseLong("Sub centered Code", childMctsCsv.getSubCentreCode(), false);
        HealthSubFacility healthSubFacility = locationValidator.subCenterCodeCheck(phcCode, healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.parseLong("Village Code", childMctsCsv.getVillageCode(), false);
        Village village = locationValidator.villageConsistencyCheck(talukaCode, taluka, villageCode);

        childSubscriber.setState(state);
        childSubscriber.setDistrict(district);
        childSubscriber.setTaluka(taluka);
        childSubscriber.setHealthBlock(healthBlock);
        childSubscriber.setPhc(healthFacility);
        childSubscriber.setSubCentre(healthSubFacility);
        childSubscriber.setVillage(village);

        String msisdn = ParseDataHelper.parseString("Whom Phone Num", childMctsCsv.getWhomPhoneNo(), true);
        int msisdnCsvLength = msisdn.length();
        if(msisdnCsvLength > 10){
            msisdn = msisdn.substring(msisdnCsvLength-10, msisdnCsvLength);
        }
        childSubscriber.setMsisdn(msisdn);
        childSubscriber.setChildMctsId(ParseDataHelper.parseString("idNo", childMctsCsv.getIdNo(), true));
        childSubscriber.setMotherMctsId(ParseDataHelper.parseString("Mother Id", childMctsCsv.getMotherId(), false));
        childSubscriber.setChildDeath(CHILD_DEATH_NINE.equalsIgnoreCase(ParseDataHelper.parseString("Entry Type", childMctsCsv.getEntryType(), false)));
        childSubscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        childSubscriber.setName(ParseDataHelper.parseString("Mother Name", childMctsCsv.getMotherName(), false));
        childSubscriber.setDob(ParseDataHelper.parseDate("Birth Date", childMctsCsv.getBirthdate(), true));

        childSubscriber.setModifiedBy(childMctsCsv.getModifiedBy());
        childSubscriber.setCreator(childMctsCsv.getCreator());
        childSubscriber.setOwner(childMctsCsv.getOwner());

        logger.info("Validation and map to entity process finished");
        return childSubscriber;
    }

    /**
     *  This method is used to insert/update subscription and subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     */
    public void insertSubscriptionSubccriber(Subscriber subscriber) throws DataValidationException {

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
        Subscription dbSubscription = subscriptionService.getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), PACK_48);
        if (dbSubscription == null) { 
            
            logger.info("Not found active subscription from database based on msisdn[{}], packName[{}]", subscriber.getMsisdn(), PACK_48);
            /* Find subscription from database based on mctsid(ChildMcts), packName, status */
            dbSubscription = subscriptionService.getActiveSubscriptionByMctsIdPack(subscriber.getChildMctsId(), PACK_48, subscriber.getState().getStateCode());
            if (dbSubscription == null) {
                logger.debug("Not found active subscription from database based on Childmctsid[{}], packName[{}]", subscriber.getChildMctsId(), PACK_48);
                /* Find subscription from database based on mctsid(MotherMcts), packName, status */
                dbSubscription = subscriptionService.getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), PACK_72, subscriber.getState().getStateCode());
                if (dbSubscription == null) {
                    logger.debug("Not Found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), PACK_48);
                    Configuration configuration = configurationService.getConfiguration();
                    long activeUserCount = subscriptionService.getActiveUserCount();
                    /* check for maximum allowed beneficiary */
                    if (activeUserCount < configuration.getNmsKkMaxAllowedActiveBeneficiaryCount()) {
                        Subscriber dbSubscriber = subscriberService.create(subscriber); 
                        createSubscription(subscriber, null, dbSubscriber);
                    } else {
                        logger.info("Reached maximum beneficery count, can't add any more");
                        throw new DataValidationException("Overload Beneficery" ,"Overload Beneficery" ,"Overload Beneficery");
                    }
                    
                } else {  /* Record found based on mctsid(MotherMcts) than */ 
                    logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_48, Status.ACTIVE);
                    Subscriber dbSubscriber = dbSubscription.getSubscriber();
                    MctsCsvHelper.populateSubscription(subscriber, dbSubscription, true);
                    subscriptionService.update(dbSubscription);
                    if (!subscriber.getChildDeath()) {
                        /* add new subscription for child */
                        Subscription newSubscription = createSubscription(subscriber, dbSubscription, dbSubscriber);
                        dbSubscriber.getSubscriptionList().add(newSubscription);
                    }
                    updateDbSubscriber(subscriber, dbSubscriber); /* update subscriber info */
                
                }
            } else { /* Record found based on mctsid(ChildMcts) than */
                logger.info("Found active subscription from database based on Childmctsid[{}], packName[{}], status[{}]", subscriber.getChildMctsId(), PACK_48, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber); /* update subscriber and subscription info */
            }
        } else { /* Record found based on msisdn than */
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_48, Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getChildMctsId())) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber); /* update subscriber and subscription info */
            } else { /* can't subscribe subscription for two phone num. */
                throw new DataValidationException(SUBSCRIPTION_EXIST_EXCEPTION_MSG,
                        ErrorCategoryConstants.INCONSISTENT_DATA, SUBSCRIPTION_EXIST_ERR_DESC, "");
            }
        }
    }
    
    /**
     *  This method is used to update subscriber and subscription
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void updateSubscriberSubscription(Subscriber subscriber, Subscription dbSubscription, Subscriber dbSubscriber) {
        
        if (subscriber.getChildDeath()) {
            updateSubscription(subscriber, dbSubscription, true);
            
        } else {
            if (!dbSubscriber.getDob().equals(subscriber.getDob())) {
                updateSubscription(subscriber, dbSubscription, true);
                Subscription newSubscription = createSubscription(subscriber, dbSubscription, dbSubscriber);
                dbSubscriber.getSubscriptionList().add(newSubscription);
            } else {
                updateSubscription(subscriber, dbSubscription, false);
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
    private void updateSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag) {
        MctsCsvHelper.populateSubscription(subscriber, dbSubscription, statusFlag);
        dbSubscription.setMctsId(subscriber.getChildMctsId());
        dbSubscription.setPackName(PACK_48);;
        subscriptionService.update(dbSubscription);
        
    }
    
    /**
     *  This method is used to create Subscription in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private Subscription createSubscription(Subscriber subscriber,
            Subscription dbSubscription, Subscriber dbSubscriber) {
        
        Subscription newSubscription = MctsCsvHelper.populateNewSubscription(subscriber, dbSubscription, dbSubscriber);
        newSubscription.setMctsId(subscriber.getChildMctsId());
        newSubscription.setPackName(PACK_48);
        
        return subscriptionService.create(newSubscription);
    }

    /**
     *  This method is used to update Subscriber info in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscriber database Subscriber
     */
    private void updateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {

        MctsCsvHelper.polpulateDbSubscriber(subscriber, dbSubscriber);
        dbSubscriber.setChildMctsId(subscriber.getChildMctsId());
        dbSubscriber.setMotherMctsId(subscriber.getMotherMctsId());
        dbSubscriber.setDob(subscriber.getDob());
        dbSubscriber.setBeneficiaryType(BeneficiaryType.CHILD);

        subscriberService.update(dbSubscriber);
    }
}
