package org.motechproject.nms.kilkari.event.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @MotechListener(subjects = "mds.crud.kilkarimodule.MotherMctsCsv.csv-import.success")
    public void motherMctsCsvSuccess(MotechEvent uploadEvent){

        System.out.println(uploadEvent.getSubject());
        System.out.println("success");
        System.out.println(motherMctsCsvService.getClass().getName());
        
        Map<String, Object> parameters = uploadEvent.getParameters();
        List<Long> uploadedIDs = (List<Long>) parameters.get(CSV_IMPORT_CREATED_IDS);
        String csvFileName = (String) parameters.get(CSV_IMPORT_FILE_NAME);
        
        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary summary = new CsvProcessingSummary();
        BulkUploadError errorDetails = new BulkUploadError();
        
        MotherMctsCsv motherMctsCsv = null;
        String userName = null;
        for (Long id : uploadedIDs) {
            try {
                
                motherMctsCsv = motherMctsCsvService.findRecordById(id);
                if(motherMctsCsv!=null ){
                    userName = motherMctsCsv.getOwner();
                    Subscriber subscriber = motherMctsToSubscriberMapper(motherMctsCsv);
                    insertSubscriptionSubccriber(subscriber);
                    summary.incrementSuccessCount();
                }
                else {
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                    summary.incrementFailureCount();
                }
            }catch(DataValidationException dve) {
                errorDetails.setRecordDetails(motherMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                summary.incrementFailureCount();

            }catch(Exception e){
                summary.incrementFailureCount();
            }
        }
        
        motherMctsCsvService.deleteAll();
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFile, summary);
    }

    private Subscriber motherMctsToSubscriberMapper(MotherMctsCsv motherMctsCsv) throws DataValidationException  {

        Subscriber motherSubscriber = new Subscriber();

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

        return motherSubscriber;
    }

    @MotechListener(subjects = "mds.crud.kilkarimodule.MotherMctsCsv.csv-import.failure")
    public void motherMctsCsvFailure(MotechEvent uploadEvent){

        System.out.println("Inside Failure");

        Map<String, Object> params = uploadEvent.getParameters();
        List<Long> createdIds = (List<Long>)params.get("csv-import.created_ids");
        List<Long> updatedIds = (List<Long>)params.get("csv-import.updated_ids");
        
        for(Long id : createdIds) {
            MotherMctsCsv motherMctsCsv= motherMctsCsvService.findById(id);
            motherMctsCsvService.delete(motherMctsCsv);
        }
        
        for(Long id : updatedIds) {
            MotherMctsCsv motherMctsCsv= motherMctsCsvService.findById(id);
            motherMctsCsvService.delete(motherMctsCsv);
        }
        
    }
    
    public void insertSubscriptionSubccriber(Subscriber subscriber) throws DataValidationException{
        
        Subscription dbSubscription = subscriptionService.getSubscriptionByMsisdnPackStatus(subscriber.getMsisdn(), "72WeeksPack", Status.Active);
        if (dbSubscription == null ){
            
            dbSubscription = subscriptionService.getPackSubscriptionByMctsIdPackStatus(subscriber.getMotherMctsId(), "72WeeksPack", Status.Active);
            if (dbSubscription == null){
                
                Configuration configuration = configurationService.getConfiguration();
                long activeUserCount = subscriptionService.getActiveUserCount();
                
                if(activeUserCount < configuration.getNmsKkMaxAllowedActiveBeneficiaryCount()) {
                    Subscriber dbSubscriber = subscriberService.create(subscriber);//CREATE
                    createSubscription(subscriber, null, dbSubscriber);
                } else {
                     //logging
                }
            }else{
                
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
            }
        }else{
            if(dbSubscription.getMctsId() == null || dbSubscription.getMctsId() == subscriber.getMotherMctsId()) {
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
                
            }else {
                throw new DataValidationException("RECORD_ALREADY_EXIST","RECORD_ALREADY_EXIST","RECORD_ALREADY_EXIST", "");
            }
        }
    }

    private void updateSubscriberSubscription(Subscriber subscriber,
            Subscription dbSubscription, Subscriber dbSubscriber) {
        
        if (subscriber.getAbortion() || subscriber.getStillBirth() || subscriber.getMotherDeath()){
            updateSubscription(subscriber, dbSubscription, true);
        }else {
            if(!dbSubscriber.getLmp().equals(subscriber.getLmp())){
                updateSubscription(subscriber, dbSubscription, true);
                createSubscription(subscriber, dbSubscription, dbSubscriber);
            }else{
                updateSubscription(subscriber, dbSubscription, false);
            }
        }
        
        updateDbSubscriber(subscriber, dbSubscriber);
                
    }
    
    private void updateSubscription(Subscriber subscriber, Subscription dbSubscription, boolean statusFlag) {
        
        if(statusFlag){
            dbSubscription.setStatus(Status.Deactivated);
        }
        dbSubscription.setStateCode(subscriber.getState().getStateCode());
        dbSubscription.setMctsId(subscriber.getMotherMctsId());
        dbSubscription.setChannel(Channel.MCTS);
        dbSubscription.setMsisdn(subscriber.getMsisdn());
        dbSubscription.setModifiedBy(subscriber.getModifiedBy());
        subscriptionService.update(dbSubscription);
    }
    
    private void createSubscription(Subscriber subscriber, Subscription dbSubscription, Subscriber dbSubscriber) {
        
        Subscription newSubscription;
        newSubscription = new Subscription();
        if (dbSubscription != null) {
            newSubscription.setOldSubscritptionId(dbSubscription);
        }
        newSubscription.setStatus(Status.PendingActivation);
        newSubscription.setMctsId(subscriber.getMotherMctsId());
        newSubscription.setMsisdn(subscriber.getMsisdn());
        newSubscription.setChannel(Channel.MCTS);
        newSubscription.setPackName("Pack_72");
        newSubscription.setModifiedBy(subscriber.getModifiedBy());
        newSubscription.setCreator(subscriber.getCreator());
        newSubscription.setOwner(subscriber.getOwner());
        newSubscription.setSubscriber(dbSubscriber);
        
        subscriptionService.create(newSubscription);
    }
    
    private void updateDbSubscriber(Subscriber subscriber, Subscriber dbSubscriber){

        if(!dbSubscriber.getMsisdn().equals(subscriber.getMsisdn())){
            dbSubscriber.setOldMsisdn(dbSubscriber.getMsisdn());
        }  

        dbSubscriber.setMotherMctsId(subscriber.getMotherMctsId());
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
        
        dbSubscriber.setAbortion(subscriber.getAbortion());
        dbSubscriber.setStillBirth(subscriber.getStillBirth());
        dbSubscriber.setMotherDeath(subscriber.getMotherDeath());
        dbSubscriber.setLmp(subscriber.getLmp());
        

        subscriberService.update(dbSubscriber);
    }
    
    
}
