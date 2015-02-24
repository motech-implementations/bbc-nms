package org.motechproject.nms.kilkari.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.service.*;
import org.motechproject.nms.masterdata.domain.*;
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

import java.util.List;
import java.util.Map;

/**
 * This class is used to handle to success 
 * event of csv upload of ChildMcts
 */
@Component
public class ChildMctsCsvHandler {

    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";
    public static final String PACK_48 = "48WEEK";
    public static final String PACK_72 = "72WEEK";

    /* Constant value of entryType field that specifies child death in Mcts Csv */
    public static final String CHILD_DEATH_NINE = "9";

    /* Valid length of MSISDN */
    public static final Integer MSISDN_VALID_LENGTH = 10;

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
                    insertSubscriptionSubscriber(subscriber);
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
        Long stateCode = ParseDataHelper.validateAndParseLong("State Code", childMctsCsv.getStateCode(), true);
        State state = locationValidator.stateConsistencyCheck(stateCode);

        Long districtCode = ParseDataHelper.validateAndParseLong("District Code", childMctsCsv.getDistrictCode(), true);
        District district = locationValidator.districtConsistencyCheck(state, districtCode);

        Long talukaCode = ParseDataHelper.validateAndParseLong("Taluka Code", childMctsCsv.getTalukaCode(), false);
        Taluka taluka = locationValidator.talukaConsistencyCheck(district, talukaCode);

        Long healthBlockCode = ParseDataHelper.validateAndParseLong("Health Block Code", childMctsCsv.getHealthBlockCode(), false);
        HealthBlock healthBlock = locationValidator.healthBlockConsistencyCheck(talukaCode, taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.validateAndParseLong("Phc Code", childMctsCsv.getPhcCode(), false);
        HealthFacility healthFacility = locationValidator.phcConsistencyCheck(healthBlockCode, healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.validateAndParseLong("Sub centered Code", childMctsCsv.getSubCentreCode(), false);
        HealthSubFacility healthSubFacility = locationValidator.subCenterCodeCheck(phcCode, healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.validateAndParseLong("Village Code", childMctsCsv.getVillageCode(), false);
        Village village = locationValidator.villageConsistencyCheck(talukaCode, taluka, villageCode);

        childSubscriber.setState(state);
        childSubscriber.setDistrict(district);
        childSubscriber.setTaluka(taluka);
        childSubscriber.setHealthBlock(healthBlock);
        childSubscriber.setPhc(healthFacility);
        childSubscriber.setSubCentre(healthSubFacility);
        childSubscriber.setVillage(village);

        String msisdn = ParseDataHelper.validateAndParseString("Whom Phone Num", childMctsCsv.getWhomPhoneNo(), true);
        int msisdnCsvLength = msisdn.length();
        if(msisdnCsvLength > MSISDN_VALID_LENGTH){
            msisdn = msisdn.substring(msisdnCsvLength - MSISDN_VALID_LENGTH, msisdnCsvLength);
        }
        childSubscriber.setMsisdn(msisdn);
        childSubscriber.setChildMctsId(ParseDataHelper.validateAndParseString("idNo", childMctsCsv.getIdNo(), true));
        childSubscriber.setMotherMctsId(ParseDataHelper.validateAndParseString("Mother Id", childMctsCsv.getMotherId(), false));
        childSubscriber.setChildDeath(CHILD_DEATH_NINE.equalsIgnoreCase(ParseDataHelper.validateAndParseString("Entry Type", childMctsCsv.getEntryType(), false)));
        childSubscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        childSubscriber.setName(ParseDataHelper.validateAndParseString("Mother Name", childMctsCsv.getMotherName(), false));
        childSubscriber.setDob(ParseDataHelper.validateAndParseDate("Birth Date", childMctsCsv.getBirthdate(), true));

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
    public void insertSubscriptionSubscriber(Subscriber subscriber) throws DataValidationException {

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
                    logger.debug("Not Found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), PACK_72);
                    Configuration configuration = configurationService.getConfiguration();
                    long activeUserCount = subscriptionService.getActiveUserCount();
                    /* check for maximum allowed beneficiary */
                    if (activeUserCount < configuration.getMaxAllowedActiveBeneficiaryCount()) {
                        Subscriber dbSubscriber = subscriberService.create(subscriber); 
                        createSubscription(subscriber, dbSubscriber);
                    } else {
                        logger.info("Reached maximum beneficiary count, can't add any more");
                        throw new DataValidationException("Beneficiary Count Exceeded" ,"Beneficiary Count Exceeded" , null);
                    }
                    
                } else {  /* Record found based on mctsid(MotherMcts) than */ 
                    logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_72, Status.ACTIVE);
                    Subscriber dbSubscriber = dbSubscription.getSubscriber();
                    MctsCsvHelper.populateDbSubscription(subscriber, dbSubscription, true);
                    subscriptionService.update(dbSubscription);
                    if (!subscriber.getChildDeath()) {
                        /* add new subscription for child */
                        Subscription newSubscription = createSubscription(subscriber, dbSubscriber);
                        dbSubscriber.getSubscriptionList().add(newSubscription);
                    }
                    updateDbSubscriber(subscriber, dbSubscriber); /* update subscriber info */
                
                }
            } else { /* Record found based on mctsid(ChildMcts) than */
                logger.info("Found active subscription from database based on Childmctsid[{}], packName[{}], status[{}]", subscriber.getChildMctsId(), PACK_48, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndDbSubscription(subscriber, dbSubscription, dbSubscriber); /* update subscriber and subscription info */
            }
        } else { /* Record found based on msisdn than */
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_48, Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getChildMctsId())) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndDbSubscription(subscriber, dbSubscription, dbSubscriber); /* update subscriber and subscription info */
            } else { /* can't subscribe subscription for two phone num. */
                throw new DataValidationException(SUBSCRIPTION_EXIST_EXCEPTION_MSG,
                        ErrorCategoryConstants.INCONSISTENT_DATA, SUBSCRIPTION_EXIST_ERR_DESC, "");
            }
        }
    }

    /**
     *  This method is used to create new Subscription in database
     *
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscriber database subscriber
     */
    private Subscription createSubscription(Subscriber subscriber, Subscriber dbSubscriber) {

        Subscription newSubscription = MctsCsvHelper.populateNewSubscription(subscriber, dbSubscriber);
        newSubscription.setMctsId(subscriber.getChildMctsId());
        newSubscription.setPackName(PACK_48);

        return subscriptionService.create(newSubscription);
    }

    /**
     *  This method is used to update Subscription info in database
     *
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     */
    private void updateDbSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag) {
        MctsCsvHelper.populateDbSubscription(subscriber, dbSubscription, statusFlag);
        dbSubscription.setMctsId(subscriber.getChildMctsId());
        dbSubscription.setPackName(PACK_48);;
        subscriptionService.update(dbSubscription);

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

    /**
     *  This method is used to update DB subscriber and DB subscription.
     *
     *  For Existing Subscription :
     *  This method updates the status  to "Deactivated"
     *  1. if childDeath
     *  2. If DOB is changed and also creates a new Subscription with new DOB.
     *
     *  In all other cases updates the Subscription's MCTSId, MSISDN and Location fields.
     *
     *
     *  For an Existing Subscriber:
     *  This method updates the all the fields as per MCTS Csv record.
     *  Also adds the new Subscription in the subscription list if created for change in DOB.
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void updateDbSubscriberAndDbSubscription(Subscriber subscriber, Subscription dbSubscription, Subscriber dbSubscriber) {
        
        if (subscriber.getChildDeath()) {
            updateDbSubscription(subscriber, dbSubscription, true);
            
        } else {
            if (!dbSubscriber.getDob().equals(subscriber.getDob())) {
                updateDbSubscription(subscriber, dbSubscription, true);
                Subscription newSubscription = createSubscription(subscriber, dbSubscriber);
                dbSubscriber.getSubscriptionList().add(newSubscription);
            } else {
                updateDbSubscription(subscriber, dbSubscription, false);
            }
        }

        updateDbSubscriber(subscriber, dbSubscriber);
    }
    

}
