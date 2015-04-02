package org.motechproject.nms.frontlineworker.it.event.handler;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.event.handler.FrontLineWorkerUploadHandler;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerCsvService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
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
 * This Class models the integration testing of FrontLineWorkerUploadHandler.
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class FrontlineWorkerHandlerIT extends BasePaxIT {
    
    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;
    
    @Inject
    private LocationService locationService;
    
    @Inject
    private FrontLineWorkerService frontLineWorkerService;
    
    @Inject
    private FrontLineWorkerCsvService frontLineWorkerCsvService;
    
    @Inject
    private StateService stateService;
    
    @Inject
    private DistrictService districtService;
    
    @Inject
    private VillageService villageService;
    
    @Inject
    private TalukaService talukaService;
    
    @Inject
    private HealthBlockService healthBlockService;
    
    @Inject
    private HealthFacilityService healthFacilityService;
    
    @Inject
    private HealthSubFacilityService healthSubFacilityService;
    
    
    private FrontLineWorkerUploadHandler frontLineWorkerUploadHandler;
    
    private State stateData;
    
    private District districtData;
    
    private Taluka talukaData;
    
    private Village villageData;
    
    private HealthBlock healthBlockData;
    
    private HealthFacility healthFacilityData;
    
    private HealthSubFacility healthSubFacilityData;
    
    private static boolean setUpIsDone = false;
    
    private State state = null;
    
    private District district = null;
    
    private Circle circle = null;
    
    private Taluka taluka = null;
    
    private HealthBlock healthBlock = null;
    
    private HealthFacility healthFacility = null;
    
    private HealthSubFacility healthSubFacility = null;
    
    private Village village = null;
    
    
    private TestHelper testHelper = new TestHelper();
    
    Map<String, Object> parameters = new HashMap<>();
    List<Long> uploadedIds = new ArrayList<Long>();
    
    @Before
    public void setUp() {
        
        
        frontLineWorkerUploadHandler = new FrontLineWorkerUploadHandler(bulkUploadErrLogService,
                locationService,
                frontLineWorkerService, frontLineWorkerCsvService
        );
        
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(locationService);
        assertNotNull(frontLineWorkerService);
        assertNotNull(frontLineWorkerCsvService);
        
        
        if (!setUpIsDone) {
            state = testHelper.createState();
            stateService.create(state);
            assertNotNull(state);
            
            district = testHelper.createDistrict();
            State stateData = stateService.findRecordByStateCode(district.getStateCode());
            stateData.getDistrict().add(district);
            stateService.update(stateData);
            assertNotNull(district);
            
            
            taluka = testHelper.createTaluka();
            District districtData = districtService.findDistrictByParentCode(taluka.getDistrictCode(), taluka.getStateCode());
            districtData.getTaluka().add(taluka);
            districtService.update(districtData);
            assertNotNull(taluka);
            
            
            healthBlock = testHelper.createHealthBlock();
            Taluka talukaRecord = talukaService.findTalukaByParentCode(healthBlock.getStateCode(),
                    healthBlock.getDistrictCode(), healthBlock.getTalukaCode());
            talukaRecord.getHealthBlock().add(healthBlock);
            talukaService.update(talukaRecord);
            assertNotNull(healthBlock);
            
            healthFacility = testHelper.createHealthFacility();
            HealthBlock healthBlockData = healthBlockService.findHealthBlockByParentCode(
                    healthFacility.getStateCode(), healthFacility.getDistrictCode(), healthFacility.getTalukaCode(),
                    healthFacility.getHealthBlockCode());
            healthBlockData.getHealthFacility().add(healthFacility);
            healthBlockService.update(healthBlockData);
            assertNotNull(healthFacility);
            
            
            healthSubFacility = testHelper.createHealthSubFacility();
            HealthFacility healthFacilityData = healthFacilityService.findHealthFacilityByParentCode(
                    healthSubFacility.getStateCode(), healthSubFacility.getDistrictCode(),
                    healthSubFacility.getTalukaCode(), healthSubFacility.getHealthBlockCode(),
                    healthSubFacility.getHealthFacilityCode());
            
            healthFacilityData.getHealthSubFacility().add(healthSubFacility);
            healthFacilityService.update(healthFacilityData);
            assertNotNull(healthSubFacility);
            
            village = testHelper.createVillage();
            Taluka talukaRecordVillage = talukaService.findTalukaByParentCode(village.getStateCode(),
                    village.getDistrictCode(), village.getTalukaCode());
            talukaRecordVillage.getVillage().add(village);
            talukaService.update(talukaRecordVillage);
            assertNotNull(village);

            // do the setup
            setUpIsDone = true;
        }

        FrontLineWorkerCsv frontLineWorkerCsv;
        FrontLineWorkerCsv frontLineWorkerCsvdb;

        // testFrontLineWorkerValidDataGetByPhnNo

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545494", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");
        frontLineWorkerCsv.setOwner("Etasha");


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerValidDataGetById

        frontLineWorkerCsv = new FrontLineWorkerCsv("2", "12", "9990545495", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerValidDataLargerphnNo

        frontLineWorkerCsv = new FrontLineWorkerCsv("3", "12", "99905454950", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerValidDatasmallPhnNo

        frontLineWorkerCsv = new FrontLineWorkerCsv("4", "12", "99905", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());


        // testFrontLineWorkerNoState
        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "11", "9990545496", "etasha",
                "USHA", "123", null, null, null, null, null,
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerNoDistrict

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "USHA", "122", null, null, null, null, null,
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerInvalidTaluka

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "USHA", "123", "1233", null, null, null, null,
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerInvalidVillage

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "USHA", "123", "1", null, null, null, "1233",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerInvalidHealthBlock

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "USHA", "123", "1", "1233", null, null, "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerInvalidHealthFacility
        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "USHA", "123", "1", "1234", "12344", null, "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerInvalidHealthSubFacility

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "USHA", "123", "1", "1234", "12345", "123455", "1234",
                null, null, "true", null);


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerInvalidDesignation

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "ABC", "123", "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerContactNoAbsent

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", null, "etasha",
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerStateCodeAbsent

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", null, "9990545496", null,
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerDistrictCodeAbsent

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "ASHA", null, "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerDesignationAbsent

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                null, "123", "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerTalukaAbsentVillagePresent

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "ASHA", "123", null, null, null, null, "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerTalukaAbsentHealthBlockPresent

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "ASHA", "123", null, "1234", null, null, null,
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerHBAbsentPHCPresent

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "ASHA", "123", "1", null, "12345", null, "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerPHCAbsentSSCPresent

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545496", "etasha",
                "ASHA", "123", "1", "1234", null, "123456", "1234",
                null, null, "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerUpdationNoFlwId Part 1

        frontLineWorkerCsv = new FrontLineWorkerCsv("10", "12", "1234567890", "Jyoti",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerUpdationNoFlwId Part 2

        frontLineWorkerCsv = new FrontLineWorkerCsv("", "12", "1234567890", "Jyoti",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerUpdation Part 1

        frontLineWorkerCsv = new FrontLineWorkerCsv("10", "12", "1234567890", "Jyoti",
                "ANM", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerUpdation Part 2

        frontLineWorkerCsv = new FrontLineWorkerCsv("10", "12", "1234567890", "Jyoti2",
                "ANM", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerStatusInvalidToValid Part 1

        frontLineWorkerCsv = new FrontLineWorkerCsv("11", "12", "5555555555", "Jaya",
                "AWW", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "False", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerStatusInvalidToValid Part 2

        frontLineWorkerCsv = new FrontLineWorkerCsv("11", "12", "5555555555", "Jaya2",
                "AWW", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerStatusValidToInvalid Part 1

        frontLineWorkerCsv = new FrontLineWorkerCsv("12", "12", "3333333333", "Sushma",
                "AWW", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerStatusValidToInvalid Part 2

        frontLineWorkerCsv = new FrontLineWorkerCsv("12", "12", "3333333333", "Sushma",
                "AWW", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "False", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerUpdationWithIsValidNull Part 1

        frontLineWorkerCsv = new FrontLineWorkerCsv("13", "12", "4444444444", "Rekha",
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerUpdationWithIsValidNull Part 2

        frontLineWorkerCsv = new FrontLineWorkerCsv("13", "12", "4444444444", "Rekha",
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerUpdationWithNoFlwId Part 1

        frontLineWorkerCsv = new FrontLineWorkerCsv("14", "12", "1234500000", "Jyoti",
                "ANM", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "True", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerUpdationWithNoFlwId Part 2

        frontLineWorkerCsv = new FrontLineWorkerCsv(null, "12", "1234500000", "Jyoti2",
                "ANM", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerVillageWithoutTaluka

        frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "9990000000", "Anjali",
                "USHA", "123", null, "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerHealthBlockWithoutTaluka

        frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "9990000000", "Etasha",
                "USHA", "123", null, "1234", "12345", "123456", null,
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerPhcWithoutHealthBlock

        frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", null, "12345", "123456", "1234",
                "9876", "1234", "true", null);


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerSubCentreWithoutPhc

        frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "1234", null, "123456", "1234",
                "9876", "1234", "true", null);


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        //nms generated id is null in update record

        frontLineWorkerCsv = new FrontLineWorkerCsv("100", "12", "8888888888", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // test FrontLineWorker ContactNo doesnt exist, Flw and State already present
/*
            frontLineWorkerCsv = new FrontLineWorkerCsv("12", "12", "8826575961", "Etasha",
                    "USHA", "123", "1", "1234", null, "123456", "1234",
                    "9876", "1234", "True", null);*/


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // test Front Line Worker flw id doesnt exist, Contact No already present. hence failure case

        frontLineWorkerCsv = new FrontLineWorkerCsv("25", "12", "9990545494", "Etasha",
                "USHA", "123", "1", "1234", null, "123456", "1234",
                "9876", "1234", "true", null);


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

           /* // testFrontLineWorkerFlwIdAndContactNoIsDifferentAndStateIsInvalid

           frontLineWorkerCsv = new FrontLineWorkerCsv("2", "12", "5555555555", "Etasha",
                                "USHA", "123", "1", "1234", null, "123456", "1234",
                                "9876", "1234", "True", "48");


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());*/

        // testFrontLineWorkerFlwIdAndContactNoIsDifferentAndStateIsInactive...diff records

        frontLineWorkerCsv = new FrontLineWorkerCsv("3", "12", "4444444444", "Etasha",
                "USHA", "123", "1", "1234", null, "123456", "1234",
                "9876", "1234", "true", null);


        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testvalid  is null contactNo  and FlwId exist

        frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545494", "Etasha",
                "USHA", "123", "1", "1234", null, "123456", "1234",
                "9876", "1234", null, null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorkerCreation flwId not present in CSV and new record insertion

        frontLineWorkerCsv = new FrontLineWorkerCsv("", "12", "8484848484", "Rashi",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());


        // testFrontLineWorker Update Flw ID provided in record where fleID was originally null

        frontLineWorkerCsv = new FrontLineWorkerCsv("103", "12", "8484848484", "Rashi",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());

        // testFrontLineWorker update FLW ID

        frontLineWorkerCsv = new FrontLineWorkerCsv("104", "12", "8484848484", "Rashi",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvdb);
        uploadedIds.add(frontLineWorkerCsvdb.getId());


        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        }


    
    
    @Test
    public void testFrontLineWorkerAll() {
        
        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        
        FrontLineWorker flw;
        FrontLineWorker frontLineWorker;
        
        // testFrontLineWorkerValidDataGetByPhnNo
        
        flw = frontLineWorkerService.getFlwBycontactNo("9990545494");
        
        assertNotNull(flw);
        assertTrue(1L == flw.getFlwId());
        assertEquals("9990545494", flw.getContactNo());
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals("Etasha", flw.getName());

        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertTrue(1L == flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());
        
        assertEquals("1234", flw.getAdhaarNumber());
        assertEquals("9876", flw.getAshaNumber());
        assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());
        
        
        // testFrontLineWorkerValidDataGetById
        
        flw = frontLineWorkerService.getFlwByFlwIdAndStateId(2L, 12L);
        
        assertNotNull(flw);
        
        assertTrue(2L == flw.getFlwId());
        assertEquals("9990545495", flw.getContactNo());
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals("Etasha", flw.getName());
        
        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertTrue(1L == flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());
        
        assertEquals("1234", flw.getAdhaarNumber());
        assertEquals("9876", flw.getAshaNumber());
        assertEquals(Status.INACTIVE, flw.getStatus());
        
        // testFrontLineWorkerValidDataLargerphnNo
        
        flw = frontLineWorkerService.getFlwByFlwIdAndStateId(3L, 12L);
        
        assertNotNull(flw);
        
        assertTrue(3L == flw.getFlwId());
        assertEquals("9905454950", flw.getContactNo());
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals("Etasha", flw.getName());
        
        assertTrue(12L == flw.getStateCode());
        assertTrue(123L == flw.getDistrictId().getDistrictCode());
        assertTrue(1L == flw.getTalukaId().getTalukaCode());
        assertTrue(1234L == flw.getVillageId().getVillageCode());
        assertTrue(1234L == flw.getHealthBlockId().getHealthBlockCode());
        assertTrue(12345L == flw.getHealthFacilityId().getHealthFacilityCode());
        assertTrue(123456L == flw.getHealthSubFacilityId().getHealthSubFacilityCode());
        
        assertEquals("1234", flw.getAdhaarNumber());
        assertEquals("9876", flw.getAshaNumber());
        /*assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());*/
        assertEquals(Status.INACTIVE, flw.getStatus());
        
        // testFrontLineWorkerValidDatasmallPhnNo
        
        flw = frontLineWorkerService.getFlwByFlwIdAndStateId(4L, 12L);
        
        assertNull(flw);
        
        // testFrontLineWorkerNoState
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        // testFrontLineWorkerNoDistrict
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerInvalidTaluka
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerInvalidVillage
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerInvalidHealthBlock
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerInvalidHealthFacility
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerInvalidHealthSubFacility
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerInvalidDesignation
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerContactNoAbsent
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerStateCodeAbsent
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerDistrictCodeAbsent
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerDesignationAbsent
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerTalukaAbsentVillagePresent
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerTalukaAbsentHealthBlockPresent
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerHBAbsentPHCPresent
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerPHCAbsentSSCPresent
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        
        // testFrontLineWorkerUpdationNoFlwId Part 1
        
        flw = frontLineWorkerService.getFlwBycontactNo("1234567890");
        assertNotNull(flw);
        
        // testFrontLineWorkerUpdationNoFlwId Part 2
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234567890");
        assertNotNull(frontLineWorker);
        assertTrue(10L == frontLineWorker.getFlwId());
        
        // testFrontLineWorkerUpdation Part 1
        
        flw = frontLineWorkerService.getFlwBycontactNo("1234567890");
        assertNotNull(flw);
        
        // testFrontLineWorkerUpdation Part 2
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234567890");
        assertNotNull(frontLineWorker);
        assertEquals("1234", frontLineWorker.getAshaNumber());
        assertEquals("Jyoti2", frontLineWorker.getName());
        
        // testFrontLineWorkerStatusInvalidToValid Part 1
        
        flw = frontLineWorkerService.getFlwBycontactNo("5555555555");
        assertNotNull(flw);
        assertEquals(Status.INVALID, flw.getStatus());
        
        // testFrontLineWorkerStatusInvalidToValid Part 2
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("5555555555");
        assertNotNull(frontLineWorker);
        assertEquals("9876", frontLineWorker.getAshaNumber());
        assertEquals("Jaya", frontLineWorker.getName());
        assertEquals(Status.INVALID, flw.getStatus());
        
        // testFrontLineWorkerStatusValidToInvalid Part 1
        
        flw = frontLineWorkerService.getFlwBycontactNo("3333333333");
        assertNotNull(flw);
        //assertEquals(Status.INACTIVE, flw.getStatus());
        
        // testFrontLineWorkerStatusValidToInvalid Part 2
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("3333333333");
        assertNotNull(frontLineWorker);
        assertEquals("1234", frontLineWorker.getAshaNumber());
        assertEquals(Status.INVALID, frontLineWorker.getStatus());
        
        // testFrontLineWorkerUpdationWithIsValidNull Part 1
        
        flw = frontLineWorkerService.getFlwBycontactNo("4444444444");
        
        assertNotNull(flw);
        assertEquals(Status.INACTIVE, flw.getStatus());
        
        // testFrontLineWorkerUpdationWithIsValidNull Part 2
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("4444444444");
        assertNotNull(frontLineWorker);
        assertEquals("1234", frontLineWorker.getAshaNumber());
        assertEquals(Status.INACTIVE, frontLineWorker.getStatus());
        
        // testFrontLineWorkerUpdationWithNoFlwId Part 1
        
        flw = frontLineWorkerService.getFlwBycontactNo("1234500000");
        
        assertNotNull(flw);
        assertTrue(14L == flw.getFlwId());
        
        // testFrontLineWorkerUpdationWithNoFlwId Part 2
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234500000");
        assertNotNull(frontLineWorker);
        assertEquals("9876", frontLineWorker.getAshaNumber());
        assertEquals("Jyoti", frontLineWorker.getName());
        assertTrue(14L == frontLineWorker.getFlwId());
        
        // testFrontLineWorkerVillageWithoutTaluka
        
        flw = frontLineWorkerService.getFlwBycontactNo("9990000000");
        assertNull(flw);
        
        // testFrontLineWorkerHealthBlockWithoutTaluka
        
        flw = frontLineWorkerService.getFlwBycontactNo("9990000000");
        assertNull(flw);
        
        // testFrontLineWorkerPhcWithoutHealthBlock
        
        flw = frontLineWorkerService.getFlwBycontactNo("9990000000");
        assertNull(flw);
        
        // testFrontLineWorkerSubCentreWithoutPhc
        
        flw = frontLineWorkerService.getFlwBycontactNo("9990000000");
        
        assertNull(flw);
        
        //nms generated id is null in update record
        
        flw = frontLineWorkerService.getFlwBycontactNo("8888888888");
        assertNotNull(flw);
        assertTrue(null != flw.getId());
        assertTrue(0001L != flw.getId());


/*        // test FrontLineWorker ContactNo doesnt exist, Flw and State already present, Hence Updation case
        flw = frontLineWorkerService.getFlwBycontactNo("8826575961");
        assertNotNull(flw);
        assertTrue(12L == flw.getFlwId());*/
        
        
        // test Front Line Worker flw id doesnt exist, Contact No already present. hence failure case
        flw = frontLineWorkerService.getFlwBycontactNo("9990545494");
        assertNotNull(flw);
        assertTrue(25L != flw.getFlwId());

        /*// testFrontLineWorkerFlwIdAndContactNoIsDifferentAndStateIsInvalid

        flw = frontLineWorkerService.getFlwByFlwIdAndStateId(2L,12L);
        assertNotNull(flw);
        assertEquals("9990545495", flw.getContactNo());

        // testFrontLineWorkerFlwIdAndContactNoIsDifferentAndStateIsInactive*/
        
        flw = frontLineWorkerService.getFlwBycontactNo("4444444444");
        assertNotNull(flw);
        FrontLineWorker flw2 = frontLineWorkerService.getFlwByFlwIdAndStateId(3L, 12L);
        assertNotNull(flw2);
        assertNotEquals(flw, flw2);


        // testvalid  is null contactNo and FlwId exist

        flw = frontLineWorkerService.getFlwByFlwIdAndStateId(1L,12L);
        assertNotNull(flw);
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals(Status.INACTIVE, flw.getStatus());
        assertEquals("9990545494",flw.getContactNo());

        // testFrontLineWorkerCreation flwId not present in CSV and new record insertion
        // testFrontLineWorker Update Flw ID provided in record where fleID was originally null
        // testFrontLineWorker update of FLW ID


        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("8484848484");
        assertNotNull(frontLineWorker);
        assertTrue(104L == frontLineWorker.getFlwId());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        
        
    }

}

