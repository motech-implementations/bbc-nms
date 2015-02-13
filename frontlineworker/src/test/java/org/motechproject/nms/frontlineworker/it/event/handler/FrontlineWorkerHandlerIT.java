package org.motechproject.nms.frontlineworker.it.event.handler;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.event.handler.FlwUploadHandler;
import org.motechproject.nms.frontlineworker.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.frontlineworker.repository.FlwRecordDataService;
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
import org.motechproject.nms.masterdata.repository.HealthSubFacilityRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.nms.masterdata.repository.VillageRecordsDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by abhishek on 27/2/15.
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class FrontlineWorkerHandlerIT extends BasePaxIT {

    @Inject
    private FlwRecordDataService flwRecordDataService;

    @Inject
    private FlwCsvRecordsDataService flwCsvRecordsDataService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private LocationService locationService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private StateRecordsDataService stateRecordsDataService;

    @Inject
    private DistrictRecordsDataService districtRecordsDataService;

    @Inject
    private VillageRecordsDataService villageRecordsDataService;

    @Inject
    private TalukaRecordsDataService talukaRecordsDataService;

    @Inject
    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    @Inject
    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;

    @Inject
    private HealthSubFacilityRecordsDataService healthSubFacilityRecordsDataService;

    private FlwUploadHandler flwUploadHandler;

    private State stateData;

    private District districtData;

    private Taluka talukaData;

    private Village villageData;

    private HealthBlock healthBlockData;

    private HealthFacility healthFacilityData;

    private HealthSubFacility healthSubFacilityData;

    private static boolean setUpIsDone = false;

    @Before
    public void setUp() {



        flwUploadHandler = new FlwUploadHandler(flwRecordDataService,
                flwCsvRecordsDataService,
                bulkUploadErrLogService,
                locationService,
                languageLocationCodeService);

        assertNotNull(flwRecordDataService);
        assertNotNull(flwCsvRecordsDataService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(locationService);
        assertNotNull(languageLocationCodeService);

        if (!setUpIsDone) {
            System.out.println("");
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

    private void createState() {
        State state = new State();
        state.setName("Delhi");
        state.setStateCode(12L);
        state.setCreator("Etasha");
        state.setOwner("Etasha");
        state.setModifiedBy("Etasha");
        stateRecordsDataService.create(state);
        assertNotNull(state);
        System.out.println("State data is successfully inserted.");

    }

    private void createDistrict() {
        District district = new District();
        district.setName("East Delhi");
        district.setStateCode(12L);
        district.setDistrictCode(123L);
        district.setCreator("Etasha");
        district.setOwner("Etasha");
        district.setModifiedBy("Etasha");
        State stateData = stateRecordsDataService.findRecordByStateCode(district.getStateCode());
        stateData.getDistrict().add(district);
        stateRecordsDataService.update(stateData);
        assertNotNull(district);
        System.out.println("District data is successfully inserted.");
    }

    private void createTaluka() {
        Taluka taluka = new Taluka();
        taluka.setName("taluka");
        taluka.setStateCode(12L);
        taluka.setDistrictCode(123L);
        taluka.setTalukaCode("taluka");
        taluka.setCreator("Etasha");
        taluka.setOwner("Etasha");
        taluka.setModifiedBy("Etasha");
        District districtData = districtRecordsDataService.findDistrictByParentCode(taluka.getDistrictCode(), taluka.getStateCode());
        districtData.getTaluka().add(taluka);
        districtRecordsDataService.update(districtData);
        assertNotNull(taluka);
        System.out.println("Taluka data is successfully inserted.");
    }

    private void createVillage(){
        Village village = new Village();
        village.setName("villageName");
        village.setStateCode(12L);
        village.setDistrictCode(123L);
        village.setTalukaCode("taluka");
        village.setVillageCode(1234L);
        village.setCreator("Etasha");
        village.setOwner("Etasha");
        village.setModifiedBy("Etasha");

        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(village.getStateCode(),
                village.getDistrictCode(), village.getTalukaCode());
        talukaRecord.getVillage().add(village);
        talukaRecordsDataService.update(talukaRecord);
        assertNotNull(village);
        System.out.println("Village data is successfully inserted.");

    }

    private void createHealthBlock(){
        HealthBlock healthBlock = new HealthBlock();
        healthBlock.setName("healthBlockName");
        healthBlock.setStateCode(12L);
        healthBlock.setDistrictCode(123L);
        healthBlock.setTalukaCode("taluka");
        healthBlock.setHealthBlockCode(1234L);
        healthBlock.setCreator("Etasha");
        healthBlock.setOwner("Etasha");
        healthBlock.setModifiedBy("Etasha");
        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(healthBlock.getStateCode(),
                healthBlock.getDistrictCode(), healthBlock.getTalukaCode());
        talukaRecord.getHealthBlock().add(healthBlock);
        talukaRecordsDataService.update(talukaRecord);

        assertNotNull(healthBlock);
        System.out.println("HealthBlock data is successfully inserted.");
    }


    private void createHealthFacility(){
        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setName("healthFacilityName");
        healthFacility.setStateCode(12L);
        healthFacility.setDistrictCode(123L);
        healthFacility.setTalukaCode("taluka");
        healthFacility.setHealthBlockCode(1234L);
        healthFacility.setHealthFacilityCode(12345L);
        healthFacility.setCreator("Etasha");
        healthFacility.setOwner("Etasha");
        healthFacility.setModifiedBy("Etasha");

        HealthBlock healthBlockData = healthBlockRecordsDataService.findHealthBlockByParentCode(
                healthFacility.getStateCode(), healthFacility.getDistrictCode(), healthFacility.getTalukaCode(),
                healthFacility.getHealthBlockCode());
        healthBlockData.getHealthBlock().add(healthFacility);
        healthBlockRecordsDataService.update(healthBlockData);
        assertNotNull(healthFacility);
        System.out.println("HealthFacility data is successfully inserted.");


    }


    private void createHealthSubFacility(){
        HealthSubFacility healthSubFacility = new HealthSubFacility();
        healthSubFacility.setName("healthSubFacilityName");
        healthSubFacility.setStateCode(12L);
        healthSubFacility.setDistrictCode(123L);
        healthSubFacility.setTalukaCode("taluka");
        healthSubFacility.setHealthBlockCode(1234L);
        healthSubFacility.setHealthFacilityCode(12345L);
        healthSubFacility.setHealthSubFacilityCode(123456L);
        healthSubFacility.setCreator("Etasha");
        healthSubFacility.setOwner("Etasha");
        healthSubFacility.setModifiedBy("Etasha");

        HealthFacility healthFacilityData = healthFacilityRecordsDataService.findHealthFacilityByParentCode(
                healthSubFacility.getStateCode(), healthSubFacility.getDistrictCode(),
                healthSubFacility.getTalukaCode(), healthSubFacility.getHealthBlockCode(),
                healthSubFacility.getHealthFacilityCode());

        healthFacilityData.getHealthSubFacility().add(healthSubFacility);
        healthFacilityRecordsDataService.update(healthFacilityData);
        assertNotNull(healthSubFacility);
        System.out.println("HealthSubFacility Permanent data is successfully inserted.");

    }




    @Rule
    public ExpectedException thrown = ExpectedException.none();

 /*   @Before
    public void testFrontLineWorkerServicepresent() throws Exception {

        System.out.println("Front Line Worker Setup start");

        flwUploadHandler = new FlwUploadHandler(flwRecordDataService,
                flwCsvRecordsDataService,
                bulkUploadErrLogService,
                locationService,
                languageLocationCodeService);

        assertNotNull(flwRecordDataService);
        assertNotNull(flwCsvRecordsDataService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(locationService);
        assertNotNull(languageLocationCodeService);

        State state = new State();
        state.setStateCode(12L);

        District district = new District();
        district.setStateCode(12L);
        district.setDistrictCode(123L);
        Set<District> districtSet = new HashSet<District>();
        districtSet.add(district);

*//*
        Taluka taluka = new Taluka();
        taluka.setStateCode(12L);
        taluka.setDistrictCode(123L);
        taluka.setTalukaCode(1234L);
        Set<Taluka> talukaSet = new HashSet<Taluka>();
        talukaSet.add(taluka);


        Village village*//*

        state = stateRecordsDataService.create(state);

        stateData = stateRecordsDataService.findRecordByStateCode(district.getStateCode());
        stateData.getDistrict().add(district);
        stateRecordsDataService.update(stateData);

        System.out.println("Front Line Worker Setup end");
    }*/


    @Test
    public void testFrontLineWorkerValidDataGetByPhnNo() {

        System.out.println("case 1 testFrontLineWorkerValidDataGetByPhnNo start");
        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setContactNo("9990545494");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");
        frontLineWorkerCsv.setOperation("ADD");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = flwRecordDataService.getFlwByContactNo("9990545494");

        assertNotNull(flw);
        assertTrue(1L == flw.getFlwId());
        assertEquals("9990545494", flw.getContactNo());
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals("Etasha", flw.getName());

        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertEquals("taluka", flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());

        assertEquals("1234", flw.getAdhaarNumber());
        assertEquals("9876", flw.getAshaNumber());
        assertEquals(true, flw.isValidated());
        assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerValidDataGetByPhnNo end");
    }



    @Test
    public void testFrontLineWorkerValidDataGetById() {

        System.out.println("case 2 testFrontLineWorkerValidDataGetById start");
        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("2");
        frontLineWorkerCsv.setContactNo("9990545495");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");
        frontLineWorkerCsv.setOperation("ADD");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = flwRecordDataService.getFlwByFlwIdAndStateId(2L, 12L);

        assertNotNull(flw);

        assertTrue(2L == flw.getFlwId());
        assertEquals("9990545495", flw.getContactNo());
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals("Etasha", flw.getName());

        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertEquals("taluka", flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());

        assertEquals("1234", flw.getAdhaarNumber());
        assertEquals("9876", flw.getAshaNumber());
        assertEquals(true, flw.isValidated());
        assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerValidDataGetById end");
    }

    @Test
    public void testFrontLineWorkerValidDataLargerphnNo() {

        System.out.println("case 3 testFrontLineWorkerValidDataLargerphnNo start");
        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("3");
        frontLineWorkerCsv.setContactNo("99905454950");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");
        frontLineWorkerCsv.setOperation("ADD");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = flwRecordDataService.getFlwByFlwIdAndStateId(3L, 12L);

        assertNotNull(flw);

        assertTrue(3L == flw.getFlwId());
        assertEquals("9905454950", flw.getContactNo());
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals("Etasha", flw.getName());

        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertEquals("taluka", flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());

        assertEquals("1234", flw.getAdhaarNumber());
        assertEquals("9876", flw.getAshaNumber());
        assertEquals(true, flw.isValidated());
        assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerValidDataLargerphnNo end");
    }

    @Test
    public void testFrontLineWorkerValidDatasmallPhnNo() {

        System.out.println("case 4 testFrontLineWorkerValidDatasmallPhnNo start");
        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("4");
        frontLineWorkerCsv.setContactNo("99905");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");
        frontLineWorkerCsv.setOperation("ADD");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = flwRecordDataService.getFlwByFlwIdAndStateId(4L, 12L);

        assertNotNull(flw);

        assertTrue(4L == flw.getFlwId());
        assertEquals("99905", flw.getContactNo());
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals("Etasha", flw.getName());

        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertEquals("taluka", flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());

        assertEquals("1234", flw.getAdhaarNumber());
        assertEquals("9876", flw.getAshaNumber());
        assertEquals(true, flw.isValidated());
        assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerValidDatasmallPhnNo end");
    }

    @Test
    public void testFrontLineWorkerNoState()  throws DataValidationException{
        System.out.println("case 5 testFrontLineWorkerNoState starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();


        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("11");//Invalid
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);
        System.out.println("testFrontLineWorkerNoState ends");
        throw new DataValidationException();

    }


    @Test
    public void testFrontLineWorkerNoDistrict() throws DataValidationException {
        System.out.println("case 6 testFrontLineWorkerNoDistrict starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("122");//Invalid
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerNoDistrict ends");
        throw new DataValidationException();

    }


    @Test
    public void testFrontLineWorkerInvalidTaluka() throws  DataValidationException {


        System.out.println("case 7 testFrontLineWorkerInvalidTaluka starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1233");//Invalid
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerInvalidTaluka ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerInvalidVillage() throws  DataValidationException {


        System.out.println("case 8 testFrontLineWorkerInvalidVillage starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1233");//invalid
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerInvalidVillage ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerInvalidHealthBlock() throws  DataValidationException {


        System.out.println("case 9 testFrontLineWorkerInvalidHealthBlock starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1233");//invalid
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerInvalidHealthBlock ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerInvalidHealthFacility() throws  DataValidationException {


        System.out.println("case 10 testFrontLineWorkerInvalidHealthFacility starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12344");//Invalid
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerInvalidHealthFacility ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerInvalidHealthSubFacility() throws  DataValidationException {


        System.out.println("case 11 testFrontLineWorkerInvalidHealthSubFacility starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123455");//Invalid
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerInvalidHealthSubFacility ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerInvalidDesignation() throws  DataValidationException {


        System.out.println("case 12 testFrontLineWorkerInvalidDesignation starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ABC");//Invalid
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerInvalidDesignation ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerContactNoAbsent() throws  DataValidationException {


        System.out.println("case 13 testFrontLineWorkerContactNoAbsent starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerContactNoAbsent ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerStateCodeAbsent() throws  DataValidationException {


        System.out.println("case 14 testFrontLineWorkerStateCodeAbsent starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerStateCodeAbsent ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerDistrictCodeAbsent() throws  DataValidationException {


        System.out.println("case 15 testFrontLineWorkerDistrictCodeAbsent starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerDistrictCodeAbsent ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerDesignationAbsent() throws  DataValidationException {


        System.out.println("case 16 testFrontLineWorkerDesignationAbsent starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerDesignationAbsent ends");
        throw new DataValidationException();

    }


    @Test
    public void testFrontLineWorkerTalukaAbsentVillagePresent() throws  DataValidationException {


        System.out.println("case 17 testFrontLineWorkerTalukaAbsentVillagePresent starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerTalukaAbsentVillagePresent ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerTalukaAbsentHealthBlockPresent() throws  DataValidationException {


        System.out.println("case 18 testFrontLineWorkerTalukaAbsentHealthBlockPresent starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerTalukaAbsentHealthBlockPresent ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerHBAbsentPHCPresent() throws  DataValidationException {


        System.out.println("case 19 testFrontLineWorkerHBAbsentPHCPresent starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerHBAbsentPHCPresent ends");
        throw new DataValidationException();

    }

    @Test
    public void testFrontLineWorkerPHCAbsentSSCPresent() throws  DataValidationException {


        System.out.println("case 20 testFrontLineWorkerPHCAbsentSSCPresent starts");

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOperation("ADD");


        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);
        assertNotNull(flwCsvRecordsDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = flwRecordDataService.getFlwByContactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerPHCAbsentSSCPresent ends");
        throw new DataValidationException();

    }


    @Ignore
    public void testFrontLineWorkerUpdation() {

        System.out.println("case 21 testFrontLineWorkerUpdation start");
        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("10");
        frontLineWorkerCsv.setContactNo("9990545494");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");
        frontLineWorkerCsv.setOperation("ADD");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = flwRecordDataService.getFlwByContactNo("9990545494");

        assertNotNull(flw);

        //Updation
        frontLineWorkerCsv.setContactNo("1234");
        frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);


        Map<String, Object> parameters_new = new HashMap<>();
        List<Long> uploadedIds_new = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters_new.put("csv-import.created_ids", uploadedIds_new);
        parameters_new.put("csv-import.filename", "FrontLineWorker.csv");

        /*MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters_new);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = flwRecordDataService.getFlwByContactNo("9990545494");*/

        assertNotNull(flw);

        assertTrue(1L == flw.getFlwId());
        assertEquals("1234", flw.getContactNo());
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals("Etasha", flw.getName());

        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertEquals("taluka", flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());

        assertEquals("1234", flw.getAdhaarNumber());
        assertEquals("9876", flw.getAshaNumber());
        assertEquals(true, flw.isValidated());
        assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerUpdation end");
    }


    @Ignore
    public void testFrontLineWorkerStatusCheck() {

        System.out.println("case 1 testFrontLineWorkerValidDataGetByPhnNo start");
        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setContactNo("1234567890");//from 9990545494 to 1234567890
        frontLineWorkerCsv.setType("ASHA");//From USHA to ASHA
        frontLineWorkerCsv.setName("Etasha_new");//from Etasha to Etasha_new
        //frontLineWorkerCsv.setOperation("ADD");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("taluka");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("5678");//from1234 to 5678
        frontLineWorkerCsv.setAshaNumber("1234");//from 9876 to 1234
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setIsValidated("true");
        frontLineWorkerCsv.setOwner("Etasha_new");//Etasha to Etasha_New
        frontLineWorkerCsv.setCreator("Etasha_new");//Etasha to Etasha_New
        frontLineWorkerCsv.setModifiedBy("Etasha_new");//Etasha to Etasha_New

        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = flwRecordDataService.getFlwByContactNo("1234567890");

        assertNotNull(flw);
        assertTrue(1L == flw.getFlwId());
        assertEquals("1234567890", flw.getContactNo());
        assertEquals(Designation.ASHA, flw.getDesignation());
        assertEquals("Etasha_new", flw.getName());

        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertEquals("taluka", flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());

        assertEquals("5678", flw.getAdhaarNumber());
        assertEquals("1234", flw.getAshaNumber());
        assertEquals(true, flw.isValidated());
        assertEquals("Etasha_new", flw.getCreator());
        assertEquals("Etasha_new", flw.getModifiedBy());
        assertEquals("Etasha_new", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = flwCsvRecordsDataService.retrieveAll();
        assertTrue(listFlwCsv.size() == 0);

        System.out.println("testFrontLineWorkerValidDataGetByPhnNo end");
    }
}

