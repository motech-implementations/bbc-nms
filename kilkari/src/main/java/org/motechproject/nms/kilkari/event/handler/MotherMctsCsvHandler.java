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
 * event of csv upload of MotherMcts
 */
@Component
public class MotherMctsCsvHandler {

    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";
    public static final String PACK_72 = "72WEEK";

    /* Constant value of field numberOfOutcomes that specifies Still Birth in Mcts Csv */
    public static final Integer STILL_BIRTH_ZERO = 0;

    /* Constant value of entryType field that specifies mother death in Mcts Csv */
    public static final String MOTHER_DEATH_NINE = "9";

    /* Constant value of Abortion field that specifies No Abortion in Mcts Csv */
    public static final String ABORTION_NONE = "none";

    /* Valid length of MSISDN */
    public static final Integer MSISDN_VALID_LENGTH = 10;

    public static final String SUBSCRIPTION_EXIST_ERR_DESC =
            "Upload Unsuccessful as Subscription to MSISDN already Exist";
    public static final String SUBSCRIPTION_EXIST_EXCEPTION_MSG =
            "Subscription to MSISDN already Exist";

    private MotherMctsCsvService motherMctsCsvService;

    private SubscriptionService subscriptionService;

    private SubscriberService subscriberService;

    private LocationValidatorService locationValidator;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private ConfigurationService configurationService;

    private static Logger logger = LoggerFactory.getLogger(MotherMctsCsvHandler.class);

    @Autowired
    public MotherMctsCsvHandler(MotherMctsCsvService motherMctsCsvService, 
            SubscriptionService subscriptionService,
            SubscriberService subscriberService,
            LocationValidatorService locationValidator,
            BulkUploadErrLogService bulkUploadErrLogService,
            ConfigurationService configurationService){
        this.motherMctsCsvService = motherMctsCsvService;
        this.subscriptionService = subscriptionService;
        this.locationValidator = locationValidator;
        this.subscriberService = subscriberService;
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
        BulkUploadStatus uploadedStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        DateTime timeOfUpload = NmsUtils.getCurrentTimeStamp();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(timeOfUpload);
        errorDetails.setRecordType(RecordType.MOTHER_MCTS);

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
                logger.warn("DataValidationException ::::", dve.getMessage());
                errorDetails.setRecordDetails(motherMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);

                uploadedStatus.incrementFailureCount();

            } catch (Exception e) {
                logger.error("****Generic exception occured****", e);
                errorDetails.setRecordDetails(motherMctsCsv.toString());
                errorDetails.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetails.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                uploadedStatus.incrementFailureCount();
            }finally {
                logger.debug("Inside finally");
                if (motherMctsCsv != null) {
                    logger.info("Deleting motherMctsCsv record id[{}]", motherMctsCsv.getId());
                    motherMctsCsvService.delete(motherMctsCsv);
                }
            }
        }
        uploadedStatus.setUploadedBy(userName);
        uploadedStatus.setBulkUploadFileName(csvFileName);
        uploadedStatus.setTimeOfUpload(timeOfUpload);
        
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(uploadedStatus);
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
        Long stateCode = ParseDataHelper.validateAndParseLong("State Code", motherMctsCsv.getStateCode(), true);
        State state = locationValidator.stateConsistencyCheck(stateCode);

        Long districtCode = ParseDataHelper.validateAndParseLong("District Code", motherMctsCsv.getDistrictCode(), true);
        District district = locationValidator.districtConsistencyCheck(state, districtCode);

        Long talukaCode = ParseDataHelper.validateAndParseLong("Taluka Code", motherMctsCsv.getTalukaCode(), false);
        Taluka taluka = locationValidator.talukaConsistencyCheck(district, talukaCode);

        Long healthBlockCode = ParseDataHelper.validateAndParseLong("Health Block Code", motherMctsCsv.getHealthBlockCode(), false);
        HealthBlock healthBlock = locationValidator.healthBlockConsistencyCheck(talukaCode, taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.validateAndParseLong("Phc Code", motherMctsCsv.getPhcCode(), false);
        HealthFacility healthFacility = locationValidator.phcConsistencyCheck(healthBlockCode, healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.validateAndParseLong("Sub centered Code", motherMctsCsv.getSubCentreCode(), false);
        HealthSubFacility healthSubFacility = locationValidator.subCenterCodeCheck(phcCode, healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.validateAndParseLong("Village id", motherMctsCsv.getVillageCode(), false);
        Village village = locationValidator.villageConsistencyCheck(talukaCode, taluka, villageCode);


        motherSubscriber.setState(state);
        motherSubscriber.setDistrict(district);
        motherSubscriber.setTaluka(taluka);
        motherSubscriber.setHealthBlock(healthBlock);
        motherSubscriber.setPhc(healthFacility);
        motherSubscriber.setSubCentre(healthSubFacility);
        motherSubscriber.setVillage(village);
        motherSubscriber.setCreator(motherMctsCsv.getCreator());
        motherSubscriber.setOwner(motherMctsCsv.getOwner());
        
        String msisdn = ParseDataHelper.validateAndParseString("Whom Phone Num", motherMctsCsv.getWhomPhoneNo(), true);
        int msisdnCsvLength = msisdn.length();
        if(msisdnCsvLength > MSISDN_VALID_LENGTH){
            msisdn = msisdn.substring(msisdnCsvLength - MSISDN_VALID_LENGTH, msisdnCsvLength);
        }
        motherSubscriber.setMsisdn(msisdn);
        motherSubscriber.setMotherMctsId(ParseDataHelper.validateAndParseString("idNo", motherMctsCsv.getIdNo(), true));
        motherSubscriber.setAge(ParseDataHelper.validateAndParseInt("Age", motherMctsCsv.getAge(), false));
        motherSubscriber.setAadharNumber(ParseDataHelper.validateAndParseString("AAdhar Num", motherMctsCsv.getAadharNo(), false));
        motherSubscriber.setName(ParseDataHelper.validateAndParseString("Name", motherMctsCsv.getName(), false));

        motherSubscriber.setLmp(ParseDataHelper.validateAndParseDate("Lmp Date", motherMctsCsv.getLmpDate(), true));
        motherSubscriber.setStillBirth(STILL_BIRTH_ZERO.equals(ParseDataHelper.validateAndParseInt("OutcomeNos", motherMctsCsv.getOutcomeNos(), false)));

        String abortion = ParseDataHelper.validateAndParseString("Abortion", motherMctsCsv.getAbortion(), false);
        if(abortion!=null){
            if(!MctsCsvHelper.checkValidAbortionValue(abortion)){
                ParseDataHelper.raiseInvalidDataException("Abortion", abortion);
            }
        }
        motherSubscriber.setAbortion(!(abortion == null || ABORTION_NONE.equalsIgnoreCase(abortion)));
        
        String entryType = ParseDataHelper.validateAndParseString("Entry Type", motherMctsCsv.getEntryType(), false);
        if(entryType!=null){
            if(!MctsCsvHelper.checkValidEntryType(entryType)){
                ParseDataHelper.raiseInvalidDataException("Entry Type", entryType);
            }
        }
        motherSubscriber.setMotherDeath(MOTHER_DEATH_NINE.equalsIgnoreCase(entryType));
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
    public void insertSubscriptionSubscriber(Subscriber subscriber) throws DataValidationException {

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
        Subscription dbSubscription = subscriptionService.getActiveSubscriptionByMsisdnPack(subscriber.getMsisdn(), PACK_72);
        if (dbSubscription == null) {
            logger.debug("Not found active subscription from database based on msisdn[{}], packName[{}]", subscriber.getMsisdn(), PACK_72);
            /* Find subscription from database based on mctsid, packName, status */
            dbSubscription = subscriptionService.getActiveSubscriptionByMctsIdPack(subscriber.getMotherMctsId(), PACK_72, subscriber.getState().getStateCode());
            if (dbSubscription == null) {
                logger.debug("Not found active subscription from database based on Mothermctsid[{}], packName[{}]", subscriber.getMotherMctsId(), PACK_72);
                Configuration configuration = configurationService.getConfiguration();
                long activeUserCount = subscriptionService.getActiveUserCount();
                /* check for maximum allowed beneficiary */
                if (activeUserCount < configuration.getMaxAllowedActiveBeneficiaryCount()) {
                    Subscriber dbSubscriber = subscriberService.create(subscriber); /* CREATE new subscriber */
                    createSubscription(subscriber, dbSubscriber); /* create subscription for above created subscriber */
                } else {
                    logger.warn("Reached maximum beneficiary, count can't add any more");
                    throw new DataValidationException("Beneficiary Count Exceeded" ,"Beneficiary Count Exceeded" , null);
                }
            } else { /* Record found based on mctsid than update subscriber and subscription */
                logger.info("Found active subscription from database based on Mothermctsid[{}], packName[{}], status[{}]", subscriber.getMotherMctsId(), PACK_72, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndDbSubscription(subscriber, dbSubscription, dbSubscriber);
            }
        } else {
            logger.info("Found active subscription from database based on msisdn[{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.ACTIVE);
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId().equals(subscriber.getMotherMctsId())) {
                logger.info("Found matching msisdn [{}], packName[{}], status[{}]", subscriber.getMsisdn(), PACK_72, Status.ACTIVE);
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateDbSubscriberAndDbSubscription(subscriber, dbSubscription, dbSubscriber);

            } else {
                throw new DataValidationException(SUBSCRIPTION_EXIST_EXCEPTION_MSG,
                        ErrorCategoryConstants.INCONSISTENT_DATA, SUBSCRIPTION_EXIST_ERR_DESC, "");
            }
        }
    }

    /**
     *  This method is used to create Subscription in database
     *
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscriber database subscriber
     */
    private Subscription createSubscription(Subscriber subscriber, Subscriber dbSubscriber) {

        Subscription newSubscription = MctsCsvHelper.populateNewSubscription(subscriber, dbSubscriber);
        newSubscription.setMctsId(subscriber.getMotherMctsId());
        newSubscription.setPackName(PACK_72);

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
        dbSubscription.setMctsId(subscriber.getMotherMctsId());
        dbSubscription.setPackName(PACK_72);
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
        dbSubscriber.setMotherMctsId(subscriber.getMotherMctsId());
        dbSubscriber.setAbortion(subscriber.getAbortion());
        dbSubscriber.setStillBirth(subscriber.getStillBirth());
        dbSubscriber.setMotherDeath(subscriber.getMotherDeath());
        dbSubscriber.setAadharNumber(subscriber.getAadharNumber());
        dbSubscriber.setAge(subscriber.getAge());
        dbSubscriber.setLmp(subscriber.getLmp());

        subscriberService.update(dbSubscriber);
    }

    /**
     *  This method is used to update DB subscriber and DB subscription.
     *
     *  For Existing Subscription :
     *  This method updates the status  to "Deactivated"
     *  1. if motherDeath, Abortion or StillBirth is reported
     *  2. If LMP is changed and also creates a new Subscription with new LMP.
     *
     *  In all other cases updates the Subscription's MCTSId, MSISDN and Location fields.
     *
     *
     *  For an Existing Subscriber:
     *  This method updates the all the fields as per MCTS Csv record.
     *  Also adds the new Subscription in the subscription list if created for change in LMP.
     *
     *  @param subscriber csv uploaded subscriber
     *  @param dbSubscription database Subscription
     *  @param dbSubscriber database subscriber
     */
    private void updateDbSubscriberAndDbSubscription(Subscriber subscriber,
                                                     Subscription dbSubscription, Subscriber dbSubscriber) {

        if (subscriber.getAbortion() || subscriber.getStillBirth() || subscriber.getMotherDeath()) {
            updateDbSubscription(subscriber, dbSubscription, true);
        } else {
            if (!dbSubscriber.getLmp().equals(subscriber.getLmp())) {
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
