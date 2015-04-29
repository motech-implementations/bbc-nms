package org.motechproject.nms.kilkari.it.event.handler;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.motechproject.nms.kilkari.domain.CsvMctsChild;
import org.motechproject.nms.kilkari.domain.CsvMctsMother;
import org.motechproject.nms.kilkari.repository.*;
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
import java.util.List;

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
    private CircleDataService circleDataService;
    
    @Inject
    private LanguageLocationCodeDataService llcDataService;
    
    @Inject
    protected SubscriberService subscriberService;
    
    @Inject
    protected CsvMctsMotherDataService csvMctsMotherDataService;
    
    @Inject
    protected CsvMctsMotherService csvMctsMotherService;
    
    @Inject
    protected CsvMctsChildDataService csvMctsChildDataService;
    
    @Inject
    protected CsvMctsChildService csvMctsChildService;

    @Inject
    protected SubscriptionService subscriptionService;

    @Inject
    protected LanguageLocationCodeService languageLocationCodeService;

    @Inject
    protected BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    protected ConfigurationService configurationService;
    
    @Inject
    protected CommonValidatorService commonValidatorService;
    
    @Inject
    protected ActiveSubscriptionCountService activeSubscriptionCountService;
    
    @Inject
    protected ActiveSubscriptionCountDataService activeSubscriptionCountDataService;
    
    @Inject
    protected SubscriptionMeasureDataService subscriptionMeasureDataService;
    
    @Inject
    protected SubscriptionDataService subscriptionDataService;
    
    @Inject
    protected ConfigurationDataService configurationDateService;

    private static boolean setUpIsDone = false;
    
    private static Logger logger = LoggerFactory.getLogger(CommonStructure.class);
    
    @Before
    public void setUp() {
        if (!setUpIsDone) {
            deleteAll();
            createState();
            createDistrict();
            createTaluka();
            createHealthBlock();
            createHealthFacility();
            createHealthSubFacility();
            createVillage();
            createCircle();
            createLLC();
        }
        // do the setup
        setUpIsDone = true;
    }

    private void deleteAll() {
        try {
            subscriptionMeasureDataService.deleteAll();
        } catch(JDOObjectNotFoundException | NucleusObjectNotFoundException n){}
        try {
            subscriptionService.deleteAll();
        } catch(JDOObjectNotFoundException | NucleusObjectNotFoundException n){}
        try {
            subscriberService.deleteAll();
        } catch(JDOObjectNotFoundException | NucleusObjectNotFoundException n){}

        try {
            llcDataService.deleteAll();
        } catch(JDOObjectNotFoundException | NucleusObjectNotFoundException n){}
        
        try {
            circleDataService.deleteAll();
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
    
    private void createCircle(){
        Circle newRecord = new Circle();
        newRecord.setName("circleName");
        newRecord.setCode("1");
        newRecord.setDefaultLanguageLocationCode("1");
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");
        circleDataService.create(newRecord);
        
        logger.info("Circle data is successfully inserted.");
    }
    
    private void createLLC(){
        LanguageLocationCode newRecord = new LanguageLocationCode();
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setCircleCode("1");
        newRecord.setLanguageKK("LanguageKK");
        newRecord.setLanguageMA("LanguageMA");
        newRecord.setLanguageMK("LanguageMK");
        newRecord.setLanguageLocationCode("1");
        newRecord.setState(stateRecordsDataService.findRecordByStateCode(newRecord.getStateCode()));
        newRecord.setDistrict(districtRecordsDataService.findDistrictByParentCode(newRecord.getDistrictCode(), newRecord.getStateCode()));
        newRecord.setCircle(circleDataService.findByCode(newRecord.getCircleCode()));
        llcDataService.create(newRecord);

        logger.info("LLC data is successfully inserted.");
    }
    
    
    protected CsvMctsMother createMotherMcts(CsvMctsMother csv) {
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setTalukaCode("1");
        csv.setHealthBlockCode("1");
        csv.setPhcCode("1");
        csv.setSubCentreCode("1");
        csv.setVillageCode("1");
        csv.setName("test");
        DateTime date = DateTime.now().minusDays(1);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        csv.setLmpDate(dtf.print(date));
        csv.setAbortion("NONE");
        csv.setOutcomeNos("1");
        csv.setAge("30");
        csv.setEntryType("1");
        csv.setAadharNo("123456789876");
        csv.setCreator("Deepak");
        csv.setOwner("Deepak");
        csv.setModifiedBy("Deepak");
        return csv;
    }
    
    protected void callCsvMctsMotherHandlerSuccessEvent(List<Long> uploadedIds){
        logger.info("Inside  callCsvMctsMotherHandlerSuccessEvent");
        csvMctsMotherService.processCsvMctsMother("CsvMctsMother.csv", uploadedIds);
    }
    
    protected void callCsvMctsChildHandlerSuccessEvent(List<Long> uploadedIds){
        logger.info("Inside  callCsvMctsChildHandlerSuccessEvent");
        csvMctsChildService.processCsvMctsChild("CsvMctsChild.csv", uploadedIds);
    }
    
    protected CsvMctsChild createChildMcts(CsvMctsChild csv) {
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setTalukaCode("1");
        csv.setHealthBlockCode("1");
        csv.setPhcCode("1");
        csv.setSubCentreCode("1");
        csv.setVillageCode("1");
        csv.setMotherName("motherName");
        DateTime date = DateTime.now().minusDays(1);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        csv.setBirthdate(dtf.print(date));
        csv.setEntryType("2");
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
    
}
