package org.motechproject.nms.kilkari.it.event.handler;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.event.handler.ChildMctsCsvHandler;
import org.motechproject.nms.kilkari.event.handler.MotherMctsCsvHandler;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.*;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.*;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jdo.JDOObjectNotFoundException;
import java.util.*;

import static org.junit.Assert.assertNull;

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
    
    private static Logger logger = LoggerFactory.getLogger(MotherMctsCsvHandlerIT.class);
    
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

    private void deleteAll() {
        try {
            subscriberService.deleteAll();
        } catch(JDOObjectNotFoundException | NucleusObjectNotFoundException n){}
        try {
            subscriptionService.deleteAll();
        } catch(JDOObjectNotFoundException | NucleusObjectNotFoundException n){}
        try {
            stateRecordsDataService.deleteAll();
        } catch(JDOObjectNotFoundException | NucleusObjectNotFoundException n){}
        
        logger.info("Deleted all location data.");
        
    }

    private void createTaluka() {
        Taluka newRecord = new Taluka();
        newRecord.setName("taluka");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode(1L);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");
        District districtData = districtRecordsDataService.findDistrictByParentCode(newRecord.getDistrictCode(), newRecord.getStateCode());
        districtData.getTaluka().add(newRecord);
        districtRecordsDataService.update(districtData);
        logger.info("Taluka data is successfully inserted.");
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
        logger.info("District data is successfully inserted.");
    }

    private void createState() {
        State state = new State();
        state.setName("HR");
        state.setStateCode(1L);
        state.setCreator("Deepak");
        state.setOwner("Deepak");
        state.setModifiedBy("Deepak");
        State dbState = stateRecordsDataService.findRecordByStateCode(state.getStateCode());
        if(dbState==null) {
            stateRecordsDataService.create(state);
        }
        logger.info("State data is successfully inserted.");
        
    }
    
    private void createHealthFacility(){
        HealthFacility newRecord = new HealthFacility();
        newRecord.setName("healthFacilityName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode(1L);
        newRecord.setHealthBlockCode(1L);
        newRecord.setHealthFacilityCode(1L);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        HealthBlock healthBlockData = healthBlockRecordsDataService.findHealthBlockByParentCode(
                newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode(), newRecord.getHealthBlockCode());
        healthBlockData.getHealthFacility().add(newRecord);
        healthBlockRecordsDataService.update(healthBlockData);
        logger.info("HealthFacility data is successfully inserted.");
    }
    
    private void createHealthBlock(){
        HealthBlock newRecord = new HealthBlock();
        newRecord.setName("healthBlockName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode(1L);
        newRecord.setHealthBlockCode(1L);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");
        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());
        talukaRecord.getHealthBlock().add(newRecord);
        talukaRecordsDataService.update(talukaRecord);
        logger.info("HealthBlock data is successfully inserted.");
    }
    
    private void createHealthSubFacility(){
        HealthSubFacility newRecord = new HealthSubFacility();
        newRecord.setName("healthSubFacilityName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode(1L);
        newRecord.setHealthBlockCode(1L);
        newRecord.setHealthFacilityCode(1L);
        newRecord.setHealthSubFacilityCode(1L);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        HealthFacility healthFacilityData = healthFacilityRecordsDataService.findHealthFacilityByParentCode(
                newRecord.getStateCode(), newRecord.getDistrictCode(),
                newRecord.getTalukaCode(), newRecord.getHealthBlockCode(),
                newRecord.getHealthFacilityCode());

        healthFacilityData.getHealthSubFacility().add(newRecord);
        healthFacilityRecordsDataService.update(healthFacilityData);
        logger.info("HealthSubFacility Permanent data is successfully inserted.");
    }
    
    private void createVillage(){
        Village newRecord = new Village();
        newRecord.setName("villageName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode(1L);
        newRecord.setVillageCode(1L);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());
        talukaRecord.getVillage().add(newRecord);
        talukaRecordsDataService.update(talukaRecord);
        logger.info("Village data is successfully inserted.");
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
        logger.info("Inside  callMotherMctsCsvHandlerSuccessEvent");
        motherMctsCsvService.processingUploadedIds("MotherMctsCsv.csv", uploadedIds);
    }
    
    protected void callChildMctsCsvHandlerSuccessEvent(List<Long> uploadedIds){
        logger.info("Inside  callChildMctsCsvHandlerSuccessEvent");
        childMctsCsvService.processChildMctsCsv("ChildMctsCsv.csv", uploadedIds);
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


    @After
    public void tearDown() {
                    deleteAll();

        setUpIsDone = false;
    }
    
    @Test
    public void testUploadedIdNotInDatabase() throws Exception {
        logger.info("Inside  createDeleteOperation");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        Long uploadedId = new Random().nextLong();
        uploadedIds.add(uploadedId);
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds);
        assertNull(motherMctsCsvDataService.findById(uploadedId));
        
    }
    
}
