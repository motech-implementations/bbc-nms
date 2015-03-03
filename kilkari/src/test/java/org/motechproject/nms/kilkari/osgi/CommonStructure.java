package org.motechproject.nms.kilkari.osgi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.event.handler.ChildMctsCsvHandler;
import org.motechproject.nms.kilkari.event.handler.MotherMctsCsvHandler;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
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
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.HealthBlockRecordsDataService;
import org.motechproject.nms.masterdata.repository.HealthFacilityRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.BasePaxIT;

public class CommonStructure extends BasePaxIT {
    
    @Inject
    private StateRecordsDataService stateRecordsDataService;
    
    @Inject
    private DistrictRecordsDataService districtRecordsDataService;
    
    @Inject
    private TalukaRecordsDataService talukaRecordsDataService;
    
    @Inject
    private HealthBlockRecordsDataService healthBlockRecordsDataService;
    
    @Inject
    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;
    
    @Inject
    protected SubscriberService subscriberService;
    
    @Inject
    protected MotherMctsCsvDataService motherMctsCsvDataService;
    
    @Inject
    protected MotherMctsCsvService motherMctsCsvService;
    
    @Inject
    protected ChildMctsCsvDataService childMctsCsvDataService;
    
    @Inject
    protected ChildMctsCsvService childMctsCsvService;

    @Inject
    protected SubscriptionService subscriptionService;

    @Inject
    protected LanguageLocationCodeService languageLocationCodeService;

    @Inject
    protected BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    protected ConfigurationService configurationService;

    @Inject
    protected LocationValidatorService locationValidatorService;

    private static boolean setUpIsDone = false;
    
    @Before
    public void setUp() {
        if (!setUpIsDone) {
            createState();
            createDistrict();
            createTaluka();
            createHealthBlock();
            createHealthFacility();
            createHealthSubFacility();
            createVillage();
        }
        // do the setup
        setUpIsDone = true;
    }

    private void createTaluka() {
        Taluka newRecord = new Taluka();
        newRecord.setName("taluka");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode("1");
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");
        District districtData = districtRecordsDataService.findDistrictByParentCode(newRecord.getDistrictCode(), newRecord.getStateCode());
        districtData.getTaluka().add(newRecord);
        districtRecordsDataService.update(districtData);
        System.out.println("Taluka data is successfully inserted.");
    }
    
    

    private void createDistrict() {
        District district = new District();
        district.setName("GGN");
        district.setStateCode(1L);
        district.setDistrictCode(1L);
        district.setCreator("Deepak");
        district.setOwner("Deepak");
        district.setModifiedBy("Deepak");
        State stateData = stateRecordsDataService.findRecordByStateCode(district.getStateCode());
        stateData.getDistrict().add(district);
        stateRecordsDataService.update(stateData);
        System.out.println("District data is successfully inserted.");
    }

    private void createState() {
        State state = new State();
        state.setName("HR");
        state.setStateCode(1L);
        state.setCreator("Deepak");
        state.setOwner("Deepak");
        state.setModifiedBy("Deepak");
        stateRecordsDataService.create(state);
        System.out.println("State data is successfully inserted.");
        
    }
    
    private void createHealthFacility(){
        HealthFacility newRecord = new HealthFacility();
        newRecord.setName("healthFacilityName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1l);
        newRecord.setTalukaCode("1");
        newRecord.setHealthBlockCode(1l);
        newRecord.setHealthFacilityCode(1l);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        HealthBlock healthBlockData = healthBlockRecordsDataService.findHealthBlockByParentCode(
                newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode(), newRecord.getHealthBlockCode());
        healthBlockData.getHealthFacility().add(newRecord);
        healthBlockRecordsDataService.update(healthBlockData);
        System.out.println("HealthFacility data is successfully inserted.");
    }
    
    private void createHealthBlock(){
        HealthBlock newRecord = new HealthBlock();
        newRecord.setName("healthBlockName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode("1");
        newRecord.setHealthBlockCode(1L);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");
        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());
        talukaRecord.getHealthBlock().add(newRecord);
        talukaRecordsDataService.update(talukaRecord);
        System.out.println("HealthBlock data is successfully inserted.");
    }
    
    private void createHealthSubFacility(){
        HealthSubFacility newRecord = new HealthSubFacility();
        newRecord.setName("healthSubFacilityName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1l);
        newRecord.setTalukaCode("1");
        newRecord.setHealthBlockCode(1l);
        newRecord.setHealthFacilityCode(1l);
        newRecord.setHealthSubFacilityCode(1l);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        HealthFacility healthFacilityData = healthFacilityRecordsDataService.findHealthFacilityByParentCode(
                newRecord.getStateCode(), newRecord.getDistrictCode(),
                newRecord.getTalukaCode(), newRecord.getHealthBlockCode(),
                newRecord.getHealthFacilityCode());

        healthFacilityData.getHealthSubFacility().add(newRecord);
        healthFacilityRecordsDataService.update(healthFacilityData);
        System.out.println("HealthSubFacility Permanent data is successfully inserted.");
    }
    
    private void createVillage(){
        Village newRecord = new Village();
        newRecord.setName("villageName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1l);
        newRecord.setTalukaCode("1");
        newRecord.setVillageCode(1l);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());
        talukaRecord.getVillage().add(newRecord);
        talukaRecordsDataService.update(talukaRecord);
        System.out.println("Village data is successfully inserted.");
    }
    
    protected MotherMctsCsv createMotherMcts(MotherMctsCsv csv) {
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setTalukaCode("1");
        csv.setHealthBlockCode("1");
        csv.setPhcCode("1");
        csv.setSubCentreCode("1");
        csv.setVillageCode("1");
        csv.setName("test");
        csv.setLmpDate("2014-12-01 08:08:08");
        csv.setAbortion("Abortion");
        csv.setOutcomeNos("0");
        csv.setAge("30");
        csv.setEntryType("Birth");
        csv.setAadharNo("123456789876");
        return csv;
    }
    
    protected void callMotherMctsCsvHandlerSuccessEvent(List<Long> uploadedIds){
        System.out.println("Inside  callMotherMctsCsvHandlerSuccessEvent");
        Map<String, Object> parameters = new HashMap<>();
        System.out.println("uploadCsv().size()::::::::::::::::" +uploadedIds.size());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "MotherMctsCsv.csv");
        
        MotherMctsCsvHandler motherMctsCsvHandler = new MotherMctsCsvHandler(motherMctsCsvService, 
                subscriptionService, 
                subscriberService, 
                locationValidatorService, 
                languageLocationCodeService, 
                bulkUploadErrLogService, 
                configurationService);
        
        MotechEvent motechEvent = new MotechEvent("MotherMctsCsv.csv_success", parameters);
        motherMctsCsvHandler.motherMctsCsvSuccess(motechEvent);
    }
    
    protected void callChildMctsCsvHandlerSuccessEvent(List<Long> uploadedIds){
        System.out.println("Inside  callChildMctsCsvHandlerSuccessEvent");
        Map<String, Object> parameters = new HashMap<>();
        System.out.println("uploadCsv().size()::::::::::::::::" +uploadedIds.size());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ChildMctsCsv.csv");
        
        ChildMctsCsvHandler childMctsCsvHandler = new ChildMctsCsvHandler(childMctsCsvService, 
                subscriptionService, 
                subscriberService, 
                locationValidatorService, 
                languageLocationCodeService, 
                bulkUploadErrLogService, 
                configurationService);
        
        MotechEvent motechEvent = new MotechEvent("ChildMctsCsv.csv_success", parameters);
        childMctsCsvHandler.childMctsCsvSuccess(motechEvent);
    }
    
    protected ChildMctsCsv createChildMcts(ChildMctsCsv csv) {
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setTalukaCode("1");
        csv.setHealthBlockCode("1");
        csv.setPhcCode("1");
        csv.setSubCentreCode("1");
        csv.setVillageCode("1");
        csv.setMotherName("motherName");
        csv.setBirthdate("2001-01-01 00:00:00");
        csv.setEntryType("Death");
        csv.setCreator("Deepak");
        csv.setOwner("Deepak");
        csv.setModifiedBy("Deepak");
        return csv;
    }
}
