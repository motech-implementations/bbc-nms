package org.motechproject.nms.kilkari.event.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
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
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
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
    private BulkUploadErrLogService bulkUploadErrLogService;

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

        for (Long id : uploadedIDs) {
            try {
                motherMctsCsv = motherMctsCsvService.findRecordById(id);
                if(motherMctsCsv!=null ){
                    Subscriber subscriber = motherMctsToSubscriberMapper(motherMctsCsv);
                    summary.incrementSuccessCount();
                }
                else {
                    summary.incrementFailureCount();
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
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
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("username", csvFileName, logFile, summary);
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

        motherSubscriber.setModifiedBy(motherMctsCsv.getModifiedBy());

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
        Subscription dbSubscription = subscriptionService.getSubscriptionByMsisdnPackStatus(subscriber.getMsisdn(), "PackName", Status.Active);
        if (dbSubscription == null ){
            dbSubscription = subscriptionService.getPackSubscriptionByMctsIdPackStatus(subscriber.getMotherMctsId(), "72WeeksPack", Status.Active);
            if (dbSubscription == null){
                
                createSubscriberSubscription(subscriber);
    
            }else{
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
                return;
            }
        }else{
            if(dbSubscription.getMctsId() == null || dbSubscription.getMctsId() == subscriber.getMotherMctsId()) {
                //Update Subscriber (motherMctsId, name, age, location, abort, still, motherDeath, lmp)
                Subscriber dbSubscriber = dbSubscription.getSubscriber();
                updateSubscriberSubscription(subscriber, dbSubscription, dbSubscriber);
                
            }else {
                throw new DataValidationException("RECORD_ALREADY_EXIST","RECORD_ALREADY_EXIST","RECORD_ALREADY_EXIST", "");
            }
        }
    }

    private void createSubscriberSubscription(Subscriber subscriber) {
        Subscription subscription = new Subscription();
        subscription.setMctsId(subscriber.getMotherMctsId());
        subscription.setMsisdn(subscriber.getMsisdn());
        subscription.setStateCode(subscriber.getState().getStateCode());
        subscription.setSubscriber(subscriber);
        List<Subscription> subscriptionList = new ArrayList<Subscription>();
        subscriptionList.add(subscription);
        subscriber.setSubscriptionList(subscriptionList);
        subscriberService.create(subscriber);//CREATE
        subscriptionService.create(subscription);//Create
    }

    private void updateSubscriberSubscription(Subscriber subscriber,
            Subscription dbSubscription, Subscriber dbSubscriber) {
        
        if(!dbSubscriber.getMsisdn().equals(subscriber.getMsisdn())){
            dbSubscriber.setOldMsisdn(dbSubscriber.getMsisdn());
            dbSubscription.setMsisdn(subscriber.getMsisdn());
        }                                 
        
        if (subscriber.getAbortion() || subscriber.getStillBirth() || subscriber.getMotherDeath()){
            
            dbSubscription.setStatus(Status.Deactivated);
            dbSubscription.setMctsId(subscriber.getMotherMctsId());
            dbSubscription.setChannel(Channel.MCTS);
            dbSubscription.setMsisdn(subscriber.getMsisdn());
            subscriptionService.update(dbSubscription);
            
        }else {
            if(!dbSubscriber.getLmp().equals(subscriber.getLmp())){
                dbSubscription.setStatus(Status.Deactivated);
                Subscription newSubscription = new Subscription();
                newSubscription.setOldSubscritptionId(dbSubscription);
                newSubscription.setStatus(Status.PendingActivation);
                newSubscription.setMctsId(subscriber.getMotherMctsId());
                newSubscription.setChannel(Channel.MCTS);
                newSubscription.setMsisdn(subscriber.getMsisdn());
                
                subscriptionService.create(newSubscription);
                subscriptionService.update(dbSubscription);

            }else{
                dbSubscription.setChannel(Channel.MCTS);
                dbSubscription.setMctsId(subscriber.getMotherMctsId());
                dbSubscription.setMsisdn(subscriber.getMsisdn());
                subscriptionService.update(dbSubscription);
            }
        }
        
        /*
         * Update dbSubscriber with update Subscriber
         * motherMctsId, name, age, location, abort, still, motherDeath, lmp
         */
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
        dbSubscriber.setAbortion(subscriber.getAbortion());
        dbSubscriber.setStillBirth(subscriber.getStillBirth());
        dbSubscriber.setMotherDeath(subscriber.getMotherDeath());
        dbSubscriber.setLmp(subscriber.getLmp());
        
        subscriberService.update(dbSubscriber);      
                
    }
}
