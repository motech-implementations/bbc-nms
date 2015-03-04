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
    public static final String PACK_72 = "72WEEK";
    public static final String STILL_BIRTH_ZERO = "0";
    public static final String MOTHER_DEATH_NINE = "9";
    public static final String ABORTION_NONE = "none";
    public static final String SUBSCRIPTION_EXIST_ERR_DESC =
            "Upload Unsuccessful as Subscription to MSISDN already Exist";
    public static final String SUBSCRIPTION_EXIST_EXCEPTION_MSG =
            "Subscription to MSISDN already Exist";

    //@Autowired
    private MotherMctsCsvService motherMctsCsvService;

    //@Autowired
    private SubscriptionService subscriptionService;

    //@Autowired
    private SubscriberService subscriberService;

    //@Autowired
    private LocationValidatorService locationValidator;

    //@Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    //@Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    //@Autowired
    private ConfigurationService configurationService;

    private static Logger logger = LoggerFactory.getLogger(MotherMctsCsvHandler.class);

    /*public MotherMctsCsvHandler(){

    }*/
    @Autowired
    public MotherMctsCsvHandler(MotherMctsCsvService motherMctsCsvService, 
            SubscriptionService subscriptionService,
            SubscriberService subscriberService,
            LocationValidatorService locationValidator,
            LanguageLocationCodeService languageLocationCodeService,
            BulkUploadErrLogService bulkUploadErrLogService,
            ConfigurationService configurationService){
        this.motherMctsCsvService = motherMctsCsvService;
        this.subscriptionService = subscriptionService;
        this.locationValidator = locationValidator;
        this.subscriberService = subscriberService;
        this.languageLocationCodeService = languageLocationCodeService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.configurationService = configurationService;

    }

    /**
     * This method is used to process record when csv upload successfully.
     * 
     * @param motechEvent This is motechEvent having uploaded record details 
     */
    @MotechListener(subjects = "mds.crud.kilkari.MotherMctsCsv.csv-import.success")
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

                    if(motherMctsCsv.getOperation() != null && motherMctsCsv.getOperation().equalsIgnoreCase("DEL")) {
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
                logger.warn("DataValidationException ::::", dve);
                errorDetails.setRecordDetails(motherMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);

                summary.incrementFailureCount();

            } catch (Exception e) {
                logger.error("Generic Exception caught ::::", e);
                errorDetails.setRecordDetails("Some Error Occurred");
                errorDetails.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetails.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                summary.incrementFailureCount();
            }finally {
                logger.debug("Inside finally");
                if (motherMctsCsv != null) {
                    logger.info("Deleting motherMctsCsv record id[{}]", motherMctsCsv.getId());
                    motherMctsCsvService.delete(motherMctsCsv);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFile, summary);
        logger.info("Success[motherMctsCsvSuccess] method finished for MotherMctsCsv");
    }

    /**
     *  This method is used to validate csv uploaded record 
     *  and map Mother mcts to subscriber
     * 
     *  @param motherMctsCsv csv uploaded subscriber
     */
    private Subscriber motherMctsToSubscriberMapper(MotherMctsCsv motherMctsCsv) throws DataValidationException {

        Subscriber motherSubscriber = new Subscriber();

        logger.info("Validation and map to entity process start");
        Long stateCode = ParseDataHelper.parseLong("State Code", motherMctsCsv.getStateCode(),  true);
        State state = locationValidator.stateConsistencyCheck(stateCode);

        Long districtCode = ParseDataHelper.parseLong("District Code", motherMctsCsv.getDistrictCode(), true);
        District district = locationValidator.districtConsistencyCheck(state, districtCode);

        String talukaCode = ParseDataHelper.parseString("Taluka Code", motherMctsCsv.getTalukaCode(), false);
        Taluka taluka = locationValidator.talukaConsistencyCheck(district, talukaCode);

        Long healthBlockCode = ParseDataHelper.parseLong("Health Block Code", motherMctsCsv.getHealthBlockCode(), false);
        HealthBlock healthBlock = locationValidator.healthBlockConsistencyCheck(talukaCode, taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.parseLong("Phc Code", motherMctsCsv.getPhcCode(), false);
        HealthFacility healthFacility = locationValidator.phcConsistencyCheck(healthBlockCode, healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.parseLong("Sub centered Code", motherMctsCsv.getSubCentreCode(), false);
        HealthSubFacility healthSubFacility = locationValidator.subCenterCodeCheck(phcCode, healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.parseLong("Village id", motherMctsCsv.getVillageCode(), false);
        Village village = locationValidator.villageConsistencyCheck(talukaCode, taluka, villageCode);


        motherSubscriber.setState(state);
        motherSubscriber.setDistrict(district);
        motherSubscriber.setTaluka(taluka);
        motherSubscriber.setHealthBlockCode(healthBlock);
        motherSubscriber.setPhc(healthFacility);
        motherSubscriber.setSubCentre(healthSubFacility);
        motherSubscriber.setVillage(village);
        motherSubscriber.setCreator(motherMctsCsv.getCreator());
        motherSubscriber.setOwner(motherMctsCsv.getOwner());

        motherSubscriber.setMsisdn(ParseDataHelper.parseString("Whom Phone Num", motherMctsCsv.getWhomPhoneNo(), true));
        motherSubscriber.setMotherMctsId(ParseDataHelper.parseString("idNo", motherMctsCsv.getIdNo(), true));
        motherSubscriber.setAge(ParseDataHelper.parseInt("Age", motherMctsCsv.getAge(), false));
        motherSubscriber.setAadharNumber(ParseDataHelper.parseString("AAdhar Num", motherMctsCsv.getAadharNo(), true));
        motherSubscriber.setName(ParseDataHelper.parseString("Name", motherMctsCsv.getName(),false));

        motherSubscriber.setLmp(ParseDataHelper.parseDate("Lmp Date", motherMctsCsv.getLmpDate(), true));
        motherSubscriber.setStillBirth(STILL_BIRTH_ZERO.equalsIgnoreCase(motherMctsCsv.getOutcomeNos()));

        String abortion = ParseDataHelper.parseString("Abortion", motherMctsCsv.getAbortion(), false);
        motherSubscriber.setAbortion(!(abortion == null || ABORTION_NONE.equalsIgnoreCase(abortion)));
        motherSubscriber.setMotherDeath(MOTHER_DEATH_NINE.equalsIgnoreCase(ParseDataHelper.parseString("Entry Type", motherMctsCsv.getEntryType(), false)));
        motherSubscriber.setBeneficiaryType(BeneficiaryType.MOTHER);

        motherSubscriber.setModifiedBy(motherMctsCsv.getModifiedBy());
        motherSubscriber.setCreator(motherMctsCsv.getCreator());
        motherSubscriber.setOwner(motherMctsCsv.getOwner());

        logger.info("Validation and map to entity process finished");
        return motherSubscriber;
    }

    /**
     *  This method is used to insert/update subscription and subscriber
     * 
     *  @param subscriber csv uploaded subscriber
     */
    public void insertSubscriptionSubccriber(Subscriber subscriber) throws DataValidationException {
        /* Find subscription from database based on msisdn, packName, status */
        Subscription dbSubscription = subscriptionService.getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), PACK_72);
        if (dbSubscription == null) {
            logger.info("Not found active subscription from database based on msisdn[{}], packName[{}]", subscriber.getMsisdn(), PACK_72);
            /* Find subscription from database based on mctsid, packName, status */
            dbSubscription = subscriptionService.getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), PACK_72, subscriber.getState().getId());
            if (dbSubscription == null) {
                logger.info("Not found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), PACK_72);
                Configuration configuration = configurationService.getConfiguration();
                long activeUserCount = subscriptionService.getActiveUserCount();
                /* check for maximum allowed beneficiary */
                if (activeUserCount < configuration.getNmsKkMaxAllowedActiveBeneficiaryCount()) {
                    Subscriber dbSubscriber = subscriberService.create(subscriber); /* CREATE new subscriber */
                    createSubscription(subscriber, null, dbSubscriber); /* create subscription for above created subscriber */
                } else {
                    logger.info("Reached maximum beneficery count can't add any more");
                    throw new DataValidationException("Overload Beneficery" ,"Overload Beneficery" ,"Overload Beneficery");
                }
            } else { /* Record found based on mctsid than update subscriber and subscription */
                logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_72, Status.Active);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
            }
        } else {
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.Active);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getMotherMctsId())) {
                logger.info("Found matching msisdn [{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.Active);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);

            } else {
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
    private void updateSubscriberSubscription(Subscriber subscriber,
            Subscription dbSubscription, Subscriber dbSubscriber) {

        if (subscriber.getAbortion() || subscriber.getStillBirth() || subscriber.getMotherDeath()) {
            updateSubscription(subscriber, dbSubscription, true);
        } else {
            if (!dbSubscriber.getLmp().equals(subscriber.getLmp())) {
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
        dbSubscription.setMctsId(subscriber.getMotherMctsId());
        dbSubscription.setPackName(PACK_72);
        subscriptionService.update(dbSubscription);
    }

    /**
     *  This method is used to create Subscription in database
     * 
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private Subscription createSubscription(Subscriber subscriber, Subscription dbSubscription, Subscriber dbSubscriber) {

        Subscription newSubscription = MctsCsvHelper.populateNewSubscription(subscriber, dbSubscription, dbSubscriber);
        newSubscription.setMctsId(subscriber.getMotherMctsId());
        newSubscription.setPackName(PACK_72);

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
     */
    private void deactivateSubscription(Subscriber subscriber) throws DataValidationException {
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(subscriber.getMotherMctsId(),
                subscriber.getState().getStateCode());
        if(dbSubscription != null) {
            dbSubscription.setStatus(Status.Deactivated);
            subscriptionService.update(dbSubscription);
        }else {
            throw new DataValidationException("RECORD_NOT_FOUND", "RECORD_NOT_FOUND", "RECORD_NOT_FOUND", "");
        }
    }

}
