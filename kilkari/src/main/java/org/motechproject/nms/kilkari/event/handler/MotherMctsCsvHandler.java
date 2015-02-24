package org.motechproject.nms.kilkari.event.handler;

import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.service.ConfigurationService;
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
 * and failure event of csv upload of MotherMcts
 */
@Component
public class MotherMctsCsvHandler {

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

    @Autowired
    private MotherMctsCsvService motherMctsCsvService;

    @Autowired
    private SubscriptionService subscriptionService;
    
    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private LocationValidator locationValidator;
    
    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;
    
    @Autowired
    private ConfigurationService configurationService;

    private static Logger logger = LoggerFactory.getLogger(MotherMctsCsvHandler.class);
    
    /**
     * This method is used to process record when csv upload successfully.
     * 
     * @param motechEvent This is motechEvent having uploaded record details 
     */
    @MotechListener(subjects = "mds.crud.kilkarimodule.MotherMctsCsv.csv-import.success")
    public void motherMctsCsvSuccess(MotechEvent motechEvent) {
        logger.info("Success[motherMctsCsvSuccess] method start for MotherMctsCsv");
        Map<String, Object> parameters = motechEvent.getParameters();
        List<Long> uploadedIDs = (List<Long>) parameters.get(CSV_IMPORT_CREATED_IDS);
        String csvFileName = (String) parameters.get(CSV_IMPORT_FILE_NAME);
        
        logger.info("Processing Csv file[{}]", csvFileName);
        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary summary = new CsvProcessingSummary();
        BulkUploadError errorDetails = new BulkUploadError();
        
        MotherMctsCsv motherMctsCsv = null;
        String userName = null;
        
        for (Long id : uploadedIDs) {
            try {
                logger.info("Processing record id[{}]", id);
                motherMctsCsv = motherMctsCsvService.findRecordById(id);
                if (motherMctsCsv != null) {
                    logger.info("Record found in database for uploaded id[{}]", id);
                    userName = motherMctsCsv.getOwner();
                    Subscriber subscriber = motherMctsToSubscriberMapper(motherMctsCsv);
                    if(motherMctsCsv.getOperation().equalsIgnoreCase("Delete")) {
                        deleteSubscription(subscriber);
                    } else {
                        insertSubscriptionSubccriber(subscriber);
                    }
                    motherMctsCsvService.delete(motherMctsCsv);
                    summary.incrementSuccessCount();
                } else {
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                    summary.incrementFailureCount();
                    logger.error("Record not found for uploaded id [{}]", id);
                }
                logger.info("Processing finished for record id[{}]", id);
            } catch (DataValidationException dve) {
                errorDetails.setRecordDetails(motherMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                summary.incrementFailureCount();

            } catch (Exception e) {
                summary.incrementFailureCount();
            }
        }
        
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFile, summary);
        logger.info("Success[motherMctsCsvSuccess] method finished for MotherMctsCsv");
    }

    /**
     *  This method is used to validate csv uploaded record 
     *  and map Mother mcts to subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private Subscriber motherMctsToSubscriberMapper(MotherMctsCsv motherMctsCsv) throws DataValidationException {

        Subscriber motherSubscriber = new Subscriber();
        
        logger.info("Validation and map to entity process start");
        Long stateCode = ParseDataHelper.parseLong("State Id", motherMctsCsv.getStateId(),  true);
        State state = locationValidator.stateConsistencyCheck(motherMctsCsv.getStateId(), stateCode);

        Long districtCode = ParseDataHelper.parseLong("District Id", motherMctsCsv.getDistrictId(), true);
        District district = locationValidator.districtConsistencyCheck(motherMctsCsv.getDistrictId(), state, districtCode);

        String talukaCode = ParseDataHelper.parseString("Taluka Id", motherMctsCsv.getTalukaId(), false);
        Taluka taluka = locationValidator.talukaConsistencyCheck(motherMctsCsv.getTalukaId(), district, talukaCode);

        Long healthBlockCode = ParseDataHelper.parseLong("Block ID", motherMctsCsv.getHealthBlockId(), false);
        HealthBlock healthBlock = locationValidator.healthBlockConsistencyCheck(motherMctsCsv.getHealthBlockId(), motherMctsCsv.getTalukaId(), taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.parseLong("Phc Id", motherMctsCsv.getPhcId(), false);
        HealthFacility healthFacility = locationValidator.phcConsistencyCheck(motherMctsCsv.getPhcId(), motherMctsCsv.getHealthBlockId(), healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.parseLong("Sub centered ID", motherMctsCsv.getSubCentreId(), false);
        HealthSubFacility healthSubFacility = locationValidator.subCenterCodeCheck(motherMctsCsv.getSubCentreId(), motherMctsCsv.getPhcId(), healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.parseLong("Village id", motherMctsCsv.getVillageId(), false);
        Village village = locationValidator.villageConsistencyCheck(motherMctsCsv.getVillageId(), motherMctsCsv.getTalukaId(), taluka, villageCode);


        motherSubscriber.setState(state);
        motherSubscriber.setDistrictId(district);
        motherSubscriber.setTalukaId(taluka);
        motherSubscriber.setHealthBlockId(healthBlock);
        motherSubscriber.setPhcId(healthFacility);
        motherSubscriber.setSubCentreId(healthSubFacility);
        motherSubscriber.setVillageId(village);
        
        motherSubscriber.setMsisdn(ParseDataHelper.parseString(motherMctsCsv.getWhomPhoneNo(), "Whom Phone Num", true));
        motherSubscriber.setMotherMctsId(ParseDataHelper.parseString(motherMctsCsv.getIdNo(), "idNo", true));
        motherSubscriber.setAge(ParseDataHelper.parseInt(motherMctsCsv.getAge(), "Age", false));
        motherSubscriber.setAadharNumber(ParseDataHelper.parseString(motherMctsCsv.getAadharNo(), "AAdhar Num", true));
        motherSubscriber.setName(ParseDataHelper.parseString(motherMctsCsv.getName(), "Name", false));

        motherSubscriber.setLmp(ParseDataHelper.parseDate(motherMctsCsv.getLmpDate(), "Lmp Date", true));
        motherSubscriber.setStillBirth("0".equalsIgnoreCase(motherMctsCsv.getOutcomeNos()));
        motherSubscriber.setAbortion(!"NONE".equalsIgnoreCase(ParseDataHelper.parseString(motherMctsCsv.getAbortion(), "Abortion", true)));
        motherSubscriber.setMotherDeath("Death".equalsIgnoreCase(ParseDataHelper.parseString(motherMctsCsv.getEntryType(), "Entry Type", true)));
        motherSubscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        motherSubscriber.setLanguageLocationCode(languageLocationCodeService.getLanguageLocationCodeKKByLocationCode(stateCode, districtCode));
        
        motherSubscriber.setModifiedBy(motherMctsCsv.getModifiedBy());
        motherSubscriber.setCreator(motherMctsCsv.getCreator());
        motherSubscriber.setOwner(motherMctsCsv.getOwner());

        logger.info("Validation and map to entity process finished");
        return motherSubscriber;
    }

    /**
     * This method is used to process record when csv upload fails.
     * 
     * @param motechEvent This is motechEvent having uploaded record details 
     */
    @MotechListener(subjects = "mds.crud.kilkarimodule.MotherMctsCsv.csv-import.failure")
    public void motherMctsCsvFailure(MotechEvent motechEvent) {
        logger.info("Failure[motherMctsCsvFailure] method start for MotherMctsCsv");
        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");
        List<Long> updatedIds = (List<Long>) params.get("csv-import.updated_ids");
        
        for (Long id : createdIds) {
            MotherMctsCsv motherMctsCsv = motherMctsCsvService.findById(id);
            motherMctsCsvService.delete(motherMctsCsv);
        }
        
        logger.info("Failure[motherMctsCsvFailure] method finished for MotherMctsCsv");
        
    }
    
    /**
     *  This method is used to insert/update subscription and subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     */
    public void insertSubscriptionSubccriber(Subscriber subscriber) throws DataValidationException {
        /* Find subscription from database based on msisdn, packName, status */
        Subscription dbSubscription = subscriptionService.getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), PACK_72, Status.Active);
        if (dbSubscription == null) {
            logger.info("Not found subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.Active);
            /* Find subscription from database based on mctsid, packName, status */
            dbSubscription = subscriptionService.getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), PACK_72, Status.Active, subscriber.getState().getId());
            if (dbSubscription == null) {
                logger.info("Not found subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_72, Status.Active);
                Configuration configuration = configurationService.getConfiguration();
                long activeUserCount = subscriptionService.getActiveUserCount();
                /* check for maximum allowed beneficiary */
                if (activeUserCount < configuration.getNmsKkMaxAllowedActiveBeneficiaryCount()) {
                    Subscriber dbSubscriber = subscriberService.create(subscriber); /* CREATE new subscriber */
                    createSubscription(subscriber, null, dbSubscriber); /* create subscription for above created subscriber */
                } else {
                    logger.info("Reached maximum beneficery count can't add any more");
                    return; /* Reached maximum beneficery count */
                }
            } else { /* Record found based on mctsid than update subscriber and subscription */
                logger.info("Found subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_72, Status.Active);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
            }
        } else {
            logger.info("Found subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.Active);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId() == subscriber.getMotherMctsId()) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
                
            } else {
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
    private void updateSubscriberSubscription(Subscriber subscriber,
            Subscription dbSubscription, Subscriber dbSubscriber) {
        
        if (subscriber.getAbortion() || subscriber.getStillBirth() || subscriber.getMotherDeath()) {
            updateSubscription(subscriber, dbSubscription, true);
        } else {
            if (!dbSubscriber.getLmp().equals(subscriber.getLmp())) {
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
        dbSubscription.setMctsId(subscriber.getMotherMctsId());
        subscriptionService.update(dbSubscription);
    }
    
    /**
     *  This method is used to create Subscription in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void createSubscription(Subscriber subscriber, Subscription dbSubscription, Subscriber dbSubscriber) {
        
        Subscription newSubscription = MctsCsvHelper.populateNewSubscription(subscriber, dbSubscription, dbSubscriber);
        newSubscription.setMctsId(subscriber.getMotherMctsId());
        newSubscription.setPackName(PACK_72);
        
        subscriptionService.create(newSubscription);
    }
    
    /**
     *  This method is used to update Subscriber info in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     */
    private void updateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {
        
        MctsCsvHelper.polpulateDbSubscriber(subscriber, dbSubscriber);
        dbSubscriber.setMotherMctsId(subscriber.getMotherMctsId());
        dbSubscriber.setAbortion(subscriber.getAbortion());
        dbSubscriber.setStillBirth(subscriber.getStillBirth());
        dbSubscriber.setMotherDeath(subscriber.getMotherDeath());
        dbSubscriber.setLmp(subscriber.getLmp());
        
        subscriberService.update(dbSubscriber);
    }
    
    /**
     *  This method is used to deactivate subscription based on csv operation
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     */
    private void deleteSubscription(Subscriber subscriber) throws DataValidationException{
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(subscriber.getMotherMctsId(), subscriber.getState().getId());
        if(dbSubscription != null) { 
            updateSubscription(subscriber, dbSubscription, true);
            updateDbSubscriber(subscriber, dbSubscription.getSubscriber());
        }else {
            throw new DataValidationException("RECORD_NOT_FOUND", "RECORD_NOT_FOUND", "RECORD_NOT_FOUND", "");
        }
    }
    
}
