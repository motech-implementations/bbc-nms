package org.motechproject.nms.kilkari.event.handler;

import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
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
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    @Autowired
    private ChildMctsCsvService childMctsCsvService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private LocationValidator locationValidator;

    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private SubscriberService subscriberService;
    
    @Autowired
    private ConfigurationService configurationService;

    @MotechListener(subjects = "mds.crud.kilkarimodule.ChildMctsCsv.csv-import.success")
    public void motherMctsCsvSuccess(MotechEvent uploadEvent) {

        Map<String, Object> parameters = uploadEvent.getParameters();
        List<Long> uploadedIDs = (List<Long>) parameters.get(CSV_IMPORT_CREATED_IDS);
        String csvFileName = (String) parameters.get(CSV_IMPORT_FILE_NAME);

        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary summary = new CsvProcessingSummary();
        BulkUploadError errorDetails = new BulkUploadError();
        ChildMctsCsv childMctsCsv = null;

        for (Long id : uploadedIDs) {

            try {
                childMctsCsv = childMctsCsvService.findRecordById(id);
                if (childMctsCsv != null) {
                    Subscriber subscriber = childMctsToSubscriberMapper(childMctsCsv);
                    insertSubscriptionSubccriber(subscriber);
                    summary.incrementSuccessCount();
                } else {
                    summary.incrementFailureCount();
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                }
            } catch (DataValidationException dve) {
                errorDetails.setRecordDetails(childMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                summary.incrementFailureCount();

            } catch (Exception e) {
                summary.incrementFailureCount();
            }
        }

        childMctsCsvService.deleteAll();
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("username", csvFileName, logFile, summary);
    }

    private Subscriber childMctsToSubscriberMapper(ChildMctsCsv childMctsCsv) throws DataValidationException {

        Subscriber childSubscriber = new Subscriber();

        Long stateCode = ParseDataHelper.parseLong("State Id", childMctsCsv.getStateId(),  true);
        State state = locationValidator.stateConsistencyCheck(childMctsCsv.getStateId(), stateCode);

        Long districtCode = ParseDataHelper.parseLong("District Id", childMctsCsv.getDistrictId(), true);
        District district = locationValidator.districtConsistencyCheck(childMctsCsv.getDistrictId(), state, districtCode);

        String talukaCode = ParseDataHelper.parseString("Taluka Id", childMctsCsv.getTalukaId(), false);
        Taluka taluka = locationValidator.talukaConsistencyCheck(childMctsCsv.getTalukaId(), district, talukaCode);


        Long healthBlockCode = ParseDataHelper.parseLong("Block ID", childMctsCsv.getHealthBlockId(), false);
        HealthBlock healthBlock = locationValidator.healthBlockConsistencyCheck(childMctsCsv.getHealthBlockId(), childMctsCsv.getTalukaId(), taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.parseLong("Phc Id", childMctsCsv.getPhcId(), false);
        HealthFacility healthFacility = locationValidator.phcConsistencyCheck(childMctsCsv.getPhcId(), childMctsCsv.getHealthBlockId(), healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.parseLong("Sub centered ID", childMctsCsv.getSubCentreId(), false);
        HealthSubFacility healthSubFacility = locationValidator.subCenterCodeCheck(childMctsCsv.getSubCentreId(), childMctsCsv.getPhcId(), healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.parseLong("Village id", childMctsCsv.getVillageId(), false);
        Village village = locationValidator.villageConsistencyCheck(childMctsCsv.getVillageId(), childMctsCsv.getTalukaId(), taluka, villageCode);

        childSubscriber.setState(state);
        childSubscriber.setDistrictId(district);
        childSubscriber.setTalukaId(taluka);
        childSubscriber.setHealthBlockId(healthBlock);
        childSubscriber.setPhcId(healthFacility);
        childSubscriber.setSubCentreId(healthSubFacility);
        childSubscriber.setVillageId(village);

        childSubscriber.setMsisdn(ParseDataHelper.parseString(childMctsCsv.getWhomPhoneNo(), "Whom Phone Num", true));
        childSubscriber.setChildMctsId(ParseDataHelper.parseString(childMctsCsv.getIdNo(), "idNo", true));
        childSubscriber.setMotherMctsId(ParseDataHelper.parseString(childMctsCsv.getMotherId(), "idNo", false));
        childSubscriber.setChildDeath("Death".equalsIgnoreCase(ParseDataHelper.parseString(childMctsCsv.getEntryType(), "Entry Type", true)));
        childSubscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        childSubscriber.setName(ParseDataHelper.parseString(childMctsCsv.getMotherName(), "Mother Name", false));
        childSubscriber.setLanguageLocationCode(languageLocationCodeService.getLanguageLocationCodeKKByLocationCode(stateCode, districtCode));
        childSubscriber.setDob(ParseDataHelper.parseDate(childMctsCsv.getBirthdate(), "Birth Date", true));

        childSubscriber.setModifiedBy(childMctsCsv.getModifiedBy());
        childSubscriber.setCreator(childMctsCsv.getCreator());
        childSubscriber.setOwner(childMctsCsv.getOwner());

        return childSubscriber;
    }



    @MotechListener(subjects = "mds.crud.kilkarimodule.ChildMctsCsv.csv-import.failure")
    public void childMctsCsvFailure(MotechEvent uploadEvent) {

        Map<String, Object> params = uploadEvent.getParameters();
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");
        List<Long> updatedIds = (List<Long>) params.get("csv-import.updated_ids");

        for (Long id : createdIds) {
            ChildMctsCsv childMctsCsv = childMctsCsvService.findById(id);
            childMctsCsvService.delete(childMctsCsv);
        }

        for (Long id : updatedIds) {
            ChildMctsCsv childMctsCsv = childMctsCsvService.findById(id);
            childMctsCsvService.delete(childMctsCsv);
        }

    }

    public void insertSubscriptionSubccriber(Subscriber subscriber) throws DataValidationException {
        Subscription dbSubscription = subscriptionService.getSubscriptionByMsisdnPackStatus(subscriber.getMsisdn(), PACK_48, Status.Active);
        if (dbSubscription == null) {
            dbSubscription = subscriptionService.getPackSubscriptionByMctsIdPackStatus(subscriber.getChildMctsId(), PACK_48, Status.Active);
            if (dbSubscription == null) {
                dbSubscription = subscriptionService.getPackSubscriptionByMctsIdPackStatus(subscriber.getMotherMctsId(), PACK_72, Status.Active);
                if (dbSubscription == null) {
                    Configuration configuration = configurationService.getConfiguration();
                    long activeUserCount = subscriptionService.getActiveUserCount();
                    
                    if (activeUserCount < configuration.getNmsKkMaxAllowedActiveBeneficiaryCount()) {
                        Subscriber dbSubscriber = subscriberService.create(subscriber); //CREATE
                        createSubscription(subscriber, null, dbSubscriber);
                    } else {
                        return; //Reached maximum beneficery count
                    }
                    
                } else {
                    Subscriber dbSubscriber = dbSubscription.getSubscriber();
                    updateSubscription(subscriber, dbSubscription, true);
                    if (!subscriber.getChildDeath()) {
                        createSubscription(subscriber, dbSubscription, dbSubscriber);
                    }
                    updateDbSubscriber(subscriber, dbSubscriber);
                
                }
            } else {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
            }
        } else {
            if (dbSubscription.getMctsId() == null || dbSubscription.getMctsId() == subscriber.getChildMctsId()) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
            } else {
                throw new DataValidationException("RECORD_ALREADY_EXIST", "RECORD_ALREADY_EXIST", "RECORD_ALREADY_EXIST", "");
            }
        }
    }
    
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
    
    private void updateSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag) {
        if (statusFlag) {
            dbSubscription.setStatus(Status.Deactivated);
        }
        dbSubscription.setStateCode(subscriber.getState().getStateCode());
        dbSubscription.setMctsId(subscriber.getChildMctsId());
        dbSubscription.setChannel(Channel.MCTS);
        dbSubscription.setMsisdn(subscriber.getMsisdn());
        dbSubscription.setModifiedBy(subscriber.getModifiedBy());
        subscriptionService.update(dbSubscription);
        
    }
    
    private void createSubscription(Subscriber subscriber,
            Subscription dbSubscription, Subscriber dbSubscriber) {
        
        Subscription newSubscription;
        newSubscription = new Subscription();
        if (dbSubscription != null) {
            newSubscription.setOldSubscritptionId(dbSubscription);
        }
        newSubscription.setStatus(Status.PendingActivation);
        newSubscription.setMctsId(subscriber.getChildMctsId());
        newSubscription.setChannel(Channel.MCTS);
        newSubscription.setMsisdn(subscriber.getMsisdn());
        newSubscription.setPackName(PACK_48);
        newSubscription.setModifiedBy(subscriber.getModifiedBy());
        newSubscription.setCreator(subscriber.getCreator());
        newSubscription.setOwner(subscriber.getOwner());
        newSubscription.setSubscriber(dbSubscriber);
        
        subscriptionService.create(newSubscription);
    }

    private void updateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {

        if (!dbSubscriber.getMsisdn().equals(subscriber.getMsisdn())) {
            dbSubscriber.setOldMsisdn(dbSubscriber.getMsisdn());
        }  

        dbSubscriber.setChildMctsId(subscriber.getChildMctsId());
        dbSubscriber.setName(subscriber.getName());
        dbSubscriber.setAge(subscriber.getAge());
        dbSubscriber.setState(subscriber.getState());
        dbSubscriber.setDistrictId(subscriber.getDistrictId());
        dbSubscriber.setTalukaId(subscriber.getTalukaId());
        dbSubscriber.setHealthBlockId(subscriber.getHealthBlockId());
        dbSubscriber.setPhcId(subscriber.getPhcId());
        dbSubscriber.setSubCentreId(subscriber.getSubCentreId());
        dbSubscriber.setVillageId(subscriber.getVillageId());
        dbSubscriber.setModifiedBy(subscriber.getModifiedBy());
        
        dbSubscriber.setDob(subscriber.getDob());

        subscriberService.update(dbSubscriber);
    }

}
