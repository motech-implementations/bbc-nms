package org.motechproject.nms.kilkari.event.handler;

import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.LocationValidatorService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
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


    //@Autowired
    private ChildMctsCsvService childMctsCsvService;

    //@Autowired
    private SubscriptionService subscriptionService;

    //@Autowired
    private LocationValidatorService locationValidator;

    //@Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    //@Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    //@Autowired
    private SubscriberService subscriberService;
    
    //@Autowired
    private ConfigurationService configurationService;
    
    private static Logger logger = LoggerFactory.getLogger(ChildMctsCsvHandler.class);
    
    @Autowired
    public ChildMctsCsvHandler(ChildMctsCsvService childMctsCsvService, 
            SubscriptionService subscriptionService,
            SubscriberService subscriberService,
            LocationValidatorService locationValidator,
            LanguageLocationCodeService languageLocationCodeService,
            BulkUploadErrLogService bulkUploadErrLogService,
            ConfigurationService configurationService){
        this.childMctsCsvService = childMctsCsvService;
        this.subscriptionService = subscriptionService;
        this.locationValidator = locationValidator;
        this.subscriberService = subscriberService;
        this.languageLocationCodeService = languageLocationCodeService;
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
        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary summary = new CsvProcessingSummary();
        BulkUploadError errorDetails = new BulkUploadError();
        
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
                    if(childMctsCsv.getOperation() != null && childMctsCsv.getOperation().equalsIgnoreCase(Operation.DEL.toString())) {
                        deactivateSubscription(subscriber);
                    } else {
                        insertSubscriptionSubccriber(subscriber);
                    }
                    summary.incrementSuccessCount();
                } else {
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                    logger.error("Record not found for uploaded id [{}]", id);
                    summary.incrementFailureCount();
                }
                logger.info("Processing finished for record id[{}]", id);
            } catch (DataValidationException dve) {
                errorDetails.setRecordDetails(childMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                summary.incrementFailureCount();

            } catch (Exception e) {
                errorDetails.setRecordDetails("");
                errorDetails.setErrorCategory("");
                errorDetails.setErrorDescription("");
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                summary.incrementFailureCount();
                
            } finally {
                if (childMctsCsv != null) {
                    childMctsCsvService.delete(childMctsCsv);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFile, summary);
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
        childSubscriber.setHealthBlockCode(healthBlock);
        childSubscriber.setPhc(healthFacility);
        childSubscriber.setSubCentre(healthSubFacility);
        childSubscriber.setVillage(village);

        String msisdn = ParseDataHelper.parseString(childMctsCsv.getWhomPhoneNo(), "Whom Phone Num", true);
        int msisdnCsvLength = msisdn.length();
        if(msisdnCsvLength > 10){
            msisdn = msisdn.substring(msisdnCsvLength-10, msisdnCsvLength);
        }
        childSubscriber.setMsisdn(msisdn);
        childSubscriber.setChildMctsId(ParseDataHelper.parseString(childMctsCsv.getIdNo(), "idNo", true));
        childSubscriber.setMotherMctsId(ParseDataHelper.parseString(childMctsCsv.getMotherId(), "Mother Id", false));
        childSubscriber.setChildDeath("Death".equalsIgnoreCase(ParseDataHelper.parseString(childMctsCsv.getEntryType(), "Entry Type", true)));
        childSubscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        childSubscriber.setName(ParseDataHelper.parseString(childMctsCsv.getMotherName(), "Mother Name", false));
        childSubscriber.setDob(ParseDataHelper.parseDate(childMctsCsv.getBirthdate(), "Birth Date", true));

        childSubscriber.setModifiedBy(childMctsCsv.getModifiedBy());
        childSubscriber.setCreator(childMctsCsv.getCreator());
        childSubscriber.setOwner(childMctsCsv.getOwner());

        logger.info("Validation and map to entity process finished");
        return childSubscriber;
    }


    /**
     * This method is used to process record when ChildMctscsv upload fails.
     * 
     * @param uploadEvent This is motechEvent having uploaded record details
     */
    @MotechListener(subjects = "mds.crud.kilkari.ChildMctsCsv.csv-import.failure")
    public void childMctsCsvFailure(MotechEvent uploadEvent) {
        logger.info("Failure[childMctsCsvFailure] method start for MotherMctsCsv");
        Map<String, Object> params = uploadEvent.getParameters();
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");
        List<Long> updatedIds = (List<Long>) params.get("csv-import.updated_ids");

        for (Long id : createdIds) {
            logger.info("Processing uploaded id[{}]", id);
            ChildMctsCsv childMctsCsv = childMctsCsvService.findById(id);
            childMctsCsvService.delete(childMctsCsv);
        }

        logger.info("Failure[childMctsCsvFailure] method finished for MotherMctsCsv");
    }

    /**
     *  This method is used to insert/update subscription and subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     */
    public void insertSubscriptionSubccriber(Subscriber subscriber) throws DataValidationException {
        /* Find subscription from database based on msisdn, packName, status */
        Subscription dbSubscription = subscriptionService.getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), PACK_48);
        if (dbSubscription == null) { 
            
            logger.info("Not found active subscription from database based on msisdn[{}], packName[{}]", subscriber.getMsisdn(), PACK_48);
            /* Find subscription from database based on mctsid(ChildMcts), packName, status */
            dbSubscription = subscriptionService.getActiveSubscriptionByMctsIdPack(subscriber.getChildMctsId(), PACK_48, subscriber.getState().getId());
            if (dbSubscription == null) {
                logger.info("Not found active subscription from database based on Childmctsid[{}], packName[{}]", subscriber.getChildMctsId(), PACK_48);
                /* Find subscription from database based on mctsid(MotherMcts), packName, status */
                dbSubscription = subscriptionService.getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), PACK_72, subscriber.getState().getId());
                if (dbSubscription == null) {
                    logger.info("Not Found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), PACK_48);
                    Configuration configuration = configurationService.getConfiguration();
                    long activeUserCount = subscriptionService.getActiveUserCount();
                    /* check for maximum allowed beneficiary */
                    if (activeUserCount < configuration.getNmsKkMaxAllowedActiveBeneficiaryCount()) {
                        Subscriber dbSubscriber = subscriberService.create(subscriber); 
                        createSubscription(subscriber, null, dbSubscriber);
                    } else {
                        logger.info("Reached maximum beneficery count can't add any more");
                        throw new DataValidationException("Overload Beneficery" ,"Overload Beneficery" ,"Overload Beneficery");
                    }
                    
                } else {  /* Record found based on mctsid(MotherMcts) than */ 
                    logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_48, Status.Active);
                    Subscriber dbSubscriber = dbSubscription.getSubscriber();
                    updateSubscription(subscriber, dbSubscription, true);  /* Deactivate mother subscription */
                    if (!subscriber.getChildDeath()) {
                        createSubscription(subscriber, dbSubscription, dbSubscriber); /* add new subscription for child */
                    }
                    updateDbSubscriber(subscriber, dbSubscriber); /* update subscriber info */
                
                }
            } else { /* Record found based on mctsid(ChildMcts) than */
                logger.info("Found active subscription from database based on Childmctsid[{}], packName[{}], status[{}]", subscriber.getChildMctsId(), PACK_48, Status.Active);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber); /* update subscriber and subscription info */
            }
        } else { /* Record found based on msisdn than */
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_48, Status.Active);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId() == subscriber.getChildMctsId()) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber); /* update subscriber and subscription info */
            } else { /* can't subscribe subscription for two phone num. */
                throw new DataValidationException("RECORD_ALREADY_EXIST", "RECORD_ALREADY_EXIST", "RECORD_ALREADY_EXIST", "");
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
                createSubscription(subscriber, dbSubscription, dbSubscriber);
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
        subscriptionService.update(dbSubscription);
        
    }
    
    /**
     *  This method is used to create Subscription in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void createSubscription(Subscriber subscriber,
            Subscription dbSubscription, Subscriber dbSubscriber) {
        
        Subscription newSubscription = MctsCsvHelper.populateNewSubscription(subscriber, dbSubscription, dbSubscriber);
        newSubscription.setMctsId(subscriber.getChildMctsId());
        newSubscription.setPackName(PACK_48);
        
        subscriptionService.create(newSubscription);
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

        subscriberService.update(dbSubscriber);
    }
    
    /**
     *  This method is used to deactivate subscription based on csv operation
     * 
     *  @param subscriber csv uploaded subscriber
     */
    private void deactivateSubscription(Subscriber subscriber) throws DataValidationException{
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(subscriber.getChildMctsId(), subscriber.getState().getId());
        if(dbSubscription != null) {
            dbSubscription.setStatus(Status.Deactivated);
            subscriptionService.update(dbSubscription);
        }else {
            throw new DataValidationException("RECORD_NOT_FOUND", "RECORD_NOT_FOUND", "RECORD_NOT_FOUND", "");
        }
    }
}
