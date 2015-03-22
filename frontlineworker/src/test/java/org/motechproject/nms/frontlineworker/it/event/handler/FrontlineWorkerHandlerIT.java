package org.motechproject.nms.frontlineworker.it.event.handler;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.domain.CsvFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.event.handler.FrontLineWorkerUploadHandler;
import org.motechproject.nms.frontlineworker.service.CsvFrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.HealthBlockService;
import org.motechproject.nms.masterdata.service.HealthFacilityService;
import org.motechproject.nms.masterdata.service.HealthSubFacilityService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.masterdata.service.TalukaService;
import org.motechproject.nms.masterdata.service.VillageService;
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
    private CsvFrontLineWorkerService csvFrontLineWorkerService;
    
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
                frontLineWorkerService, csvFrontLineWorkerService
        );
        
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(locationService);
        assertNotNull(frontLineWorkerService);
        assertNotNull(csvFrontLineWorkerService);
        
        
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

        CsvFrontLineWorker csvFrontLineWorker;
        CsvFrontLineWorker csvdbFrontLineWorker;

        // testFrontLineWorkerValidDataGetByPhnNo

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545494", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvFrontLineWorker.setCreator("Etasha");
        csvFrontLineWorker.setModifiedBy("Etasha");
        csvFrontLineWorker.setOwner("Etasha");


        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerValidDataGetById

        csvFrontLineWorker = new CsvFrontLineWorker("2", "12", "9990545495", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerValidDataLargerphnNo

        csvFrontLineWorker = new CsvFrontLineWorker("3", "12", "99905454950", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerValidDatasmallPhnNo

        csvFrontLineWorker = new CsvFrontLineWorker("4", "12", "99905", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        // testFrontLineWorkerNoState

        csvFrontLineWorker = new CsvFrontLineWorker("1", "11", "9990545496", "etasha",
                "USHA", "123", null, null, null, null, null,
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerNoDistrict

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "USHA", "122", null, null, null, null, null,
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerInvalidTaluka

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "USHA", "123", "1233", null, null, null, null,
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerInvalidVillage

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "USHA", "123", "1", null, null, null, "1233",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerInvalidHealthBlock

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "USHA", "123", "1", "1233", null, null, "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerInvalidHealthFacility

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "USHA", "123", "1", "1234", "12344", null, "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerInvalidHealthSubFacility

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "USHA", "123", "1", "1234", "12345", "123455", "1234",
                null, null, "true", null);


        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerInvalidDesignation

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "ABC", "123", "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerContactNoAbsent

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", null, "etasha",
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerStateCodeAbsent

        csvFrontLineWorker = new CsvFrontLineWorker("1", null, "9990545496", null,
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerDistrictCodeAbsent

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "ASHA", null, "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerDesignationAbsent

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                null, "123", "1", "1234", "12345", "123456", "1234",
                null, null, "true", null);


        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerTalukaAbsentVillagePresent

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "ASHA", "123", null, null, null, null, "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerTalukaAbsentHealthBlockPresent

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "ASHA", "123", null, "1234", null, null, null,
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerHBAbsentPHCPresent

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "ASHA", "123", "1", null, "12345", null, "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerPHCAbsentSSCPresent

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545496", "etasha",
                "ASHA", "123", "1", "1234", null, "123456", "1234",
                null, null, "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdationNoFlwId Part 1

        csvFrontLineWorker = new CsvFrontLineWorker("10", "12", "1234567890", "Jyoti",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdationNoFlwId Part 2

        csvFrontLineWorker = new CsvFrontLineWorker("", "12", "1234567890", "Jyoti",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdation Part 1

        csvFrontLineWorker = new CsvFrontLineWorker("10", "12", "1234567890", "Jyoti",
                "ANM", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdation Part 2

        csvFrontLineWorker = new CsvFrontLineWorker("10", "12", "1234567890", "Jyoti2",
                "ANM", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerStatusInvalidToValid Part 1

        csvFrontLineWorker = new CsvFrontLineWorker("11", "12", "5555555555", "Jaya",
                "AWW", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "False", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerStatusInvalidToValid Part 2

        csvFrontLineWorker = new CsvFrontLineWorker("11", "12", "5555555555", "Jaya2",
                "AWW", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerStatusValidToInvalid Part 1

        csvFrontLineWorker = new CsvFrontLineWorker("12", "12", "3333333333", "Sushma",
                "AWW", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerStatusValidToInvalid Part 2

        csvFrontLineWorker = new CsvFrontLineWorker("12", "12", "3333333333", "Sushma",
                "AWW", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "False", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdationWithIsValidNull Part 1

        csvFrontLineWorker = new CsvFrontLineWorker("13", "12", "4444444444", "Rekha",
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdationWithIsValidNull Part 2

        csvFrontLineWorker = new CsvFrontLineWorker("13", "12", "4444444444", "Rekha",
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdationWithNoFlwId Part 1

        csvFrontLineWorker = new CsvFrontLineWorker("14", "12", "1234500000", "Jyoti",
                "ANM", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "True", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdationWithNoFlwId Part 2

        csvFrontLineWorker = new CsvFrontLineWorker(null, "12", "1234500000", "Jyoti2",
                "ANM", "123", "1", "1234", "12345", "123456", "1234",
                "1234", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerVillageWithoutTaluka

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Anjali",
                "USHA", "123", null, "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);


        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerHealthBlockWithoutTaluka

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", null, "1234", "12345", "123456", null,
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerPhcWithoutHealthBlock

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", null, "12345", "123456", "1234",
                "9876", "1234", "true", null);


        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerSubCentreWithoutPhc

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "1234", null, "123456", "1234",
                "9876", "1234", "true", null);


        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        //nms generated id is null in update record

        csvFrontLineWorker = new CsvFrontLineWorker("100", "12", "8888888888", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // test Front Line Worker flw id doesnt exist, Contact No already present. hence failure case

        csvFrontLineWorker = new CsvFrontLineWorker("25", "12", "9990545494", "Etasha",
                "USHA", "123", "1", "1234", null, "123456", "1234",
                "9876", "1234", "true", null);


        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerFlwIdAndContactNoIsDifferentAndStateIsInactive...diff records

        csvFrontLineWorker = new CsvFrontLineWorker("3", "12", "4444444444", "Etasha",
                "USHA", "123", "1", "1234", null, "123456", "1234",
                "9876", "1234", "true", null);


        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // test valid  is null contactNo  and FlwId exist

        csvFrontLineWorker = new CsvFrontLineWorker("1", "12", "9990545494", "Etasha",
                "USHA", "123", "1", "1234", null, "123456", "1234",
                "9876", "1234", null, null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerCreation flwId not present in CSV and new record insertion

        csvFrontLineWorker = new CsvFrontLineWorker("", "12", "8484848484", "Rashi",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        // testFrontLineWorker Update Flw ID provided in record where fleID was originally null

        csvFrontLineWorker = new CsvFrontLineWorker("103", "12", "8484848484", "Rashi",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker update FLW ID

        csvFrontLineWorker = new CsvFrontLineWorker("104", "12", "8484848484", "Rashi",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerTaluka not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "2", "1234", "12345", "123456", null,
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerVillage not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "123456",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker Taluka-Village combination not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "2", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker Taluka is null, Village is non null.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        // testFrontLineWorkerHealthBlock not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "123456", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker Taluka-HealthBlock combination not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "3", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker Taluka is null, HealthBlock is non null.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerHealthFacility not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "1234", "123", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker HealthBlock-HealthFacility combination not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "123", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker HealthBlock is null, HealthFacility is non null.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerHealthSubFacility not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "1234", "12345", "1234", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker HealthFacility-HealthSubFacility combination not present in CSV.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "1234", "123", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker HealthFacility is null, HealthSubFacility is non null.

        csvFrontLineWorker = new CsvFrontLineWorker("20", "12", "9990000000", "Etasha",
                "USHA", "123", "1", "1234", "", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdation flwId and stateId combination is null, ContactNo not null(status invalid).

        csvFrontLineWorker = new CsvFrontLineWorker("15", "12", "3737373737", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "false", null);// status is invalid.

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        csvFrontLineWorker = new CsvFrontLineWorker("16", "12", "3737373737", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        // testFrontLineWorkerUpdation flwId and stateId combination is not null, ContactNo not null, present
        // in different records and record fetched by contact number is invalid

        csvFrontLineWorker = new CsvFrontLineWorker("17", "12", "4747474747", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        csvFrontLineWorker = new CsvFrontLineWorker("18", "12", "5757575757", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "false", null);//status is invalid.

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        csvFrontLineWorker = new CsvFrontLineWorker("17", "12", "5757575757", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdation flwId and stateId combination is not null, ContactNo is null

        csvFrontLineWorker = new CsvFrontLineWorker("19", "12", "6767676767", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        csvFrontLineWorker = new CsvFrontLineWorker("19", "12", "7777777777", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorkerUpdation flwId and stateId combination is not null, ContactNo not null,
        // present in different records.

        csvFrontLineWorker = new CsvFrontLineWorker("21", "12", "9797979797", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        csvFrontLineWorker = new CsvFrontLineWorker("22", "12", "8989898987", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        csvFrontLineWorker = new CsvFrontLineWorker("22", "12", "9797979797", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker flwId and stateId combination non null, ContactNo non null, present in same Record

        csvFrontLineWorker = new CsvFrontLineWorker("6", "12", "1717171717", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        csvFrontLineWorker = new CsvFrontLineWorker("6", "12", "1717171717", "Etasha",
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        // testFrontLineWorker flwId and stateId combination non null, ContactNo non null, present in different Record

        csvFrontLineWorker = new CsvFrontLineWorker("2", "12", "1717171717", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());


        // testFrontLineWorker flwId not present in both create and update

        csvFrontLineWorker = new CsvFrontLineWorker("", "12", "1818181818", "Etasha",
                "USHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        csvFrontLineWorker = new CsvFrontLineWorker("", "12", "1818181818", "Etasha",
                "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                "9876", "1234", "true", null);

        csvdbFrontLineWorker = csvFrontLineWorkerService.createFrontLineWorkerCsv(csvFrontLineWorker);
        assertNotNull(csvdbFrontLineWorker);
        uploadedIds.add(csvdbFrontLineWorker.getId());

        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

    }

    
    @Test
    public void testFrontLineWorkerAll() {
        
        MotechEvent motechEvent = new MotechEvent("CsvFrontLineWorker.csv_success", parameters);
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

        // test Front Line Worker flw id doesnt exist, Contact No already present. hence failure case
        flw = frontLineWorkerService.getFlwBycontactNo("9990545494");
        assertNotNull(flw);
        assertTrue(25L != flw.getFlwId());

        // testFrontLineWorkerFlwIdAndContactNoIsDifferentAndStateIsInactive*/
        
        flw = frontLineWorkerService.getFlwBycontactNo("4444444444");
        assertNotNull(flw);
        FrontLineWorker flw2 = frontLineWorkerService.getFlwByFlwIdAndStateId(3L, 12L);
        assertNotNull(flw2);
        assertNotEquals(flw, flw2);


        // testvalid  is null contactNo and FlwId exist

        flw = frontLineWorkerService.getFlwByFlwIdAndStateId(1L, 12L);
        assertNotNull(flw);
        assertEquals(Designation.USHA, flw.getDesignation());
        assertEquals(Status.INACTIVE, flw.getStatus());
        assertEquals("9990545494", flw.getContactNo());

        // testFrontLineWorkerCreation flwId not present in CSV and new record insertion
        // testFrontLineWorker Update Flw ID provided in record where fleID was originally null
        // testFrontLineWorker update of FLW ID


        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("8484848484");
        assertNotNull(frontLineWorker);
        assertTrue(104L == frontLineWorker.getFlwId());

        // testFrontLineWorkerTaluka not present in CSV.
        // testFrontLineWorkerVillage not present in CSV.
        // testFrontLineWorker Taluka-Village combination not present in CSV.

        // testFrontLineWorkerHealthBlock not present in CSV.
        // testFrontLineWorker Taluka-HealthBlock combination not present in CSV.
        // testFrontLineWorker Taluka is null, HealthBlock is non null.

        // testFrontLineWorkerHealthFacility not present in CSV.
        // testFrontLineWorker HealthBlock-HealthFacility combination not present in CSV.
        // testFrontLineWorker HealthBlock is null, HealthFacility is non null.

        // testFrontLineWorkerHealthSubFacility not present in CSV.
        // testFrontLineWorker HealthFacility-HealthSubFacility combination not present in CSV.
        // testFrontLineWorker HealthFacility is null, HealthSubFacility is non null.

        flw = frontLineWorkerService.getFlwBycontactNo("9990000000");
        assertNull(flw);

        // testFrontLineWorkerUpdation flwId and stateId combination is null, ContactNo not null(status invalid).

        flw = frontLineWorkerService.getFlwBycontactNo("3737373737");
        assertNotNull(flw);
        assertTrue(16L == flw.getFlwId());

        // testFrontLineWorkerUpdation flwId and stateId combination is not null, ContactNo not null, present
        // in different records and record fetched by contact number is invalid

        flw = frontLineWorkerService.getFlwBycontactNo("5757575757");
        assertNotNull(flw);
        assertTrue(17L == flw.getFlwId());

        // testFrontLineWorkerUpdation flwId and stateId combination is not null, ContactNo is null

        flw = frontLineWorkerService.getFlwBycontactNo("7777777777");
        assertNotNull(flw);
        assertTrue(19L == flw.getFlwId());

        // testFrontLineWorkerUpdation flwId and stateId combination is not null, ContactNo not null, present
        // in different records.

        flw = frontLineWorkerService.getFlwBycontactNo("9797979797");
        assertNotNull(flw);
        assertTrue(21L == flw.getFlwId());

        flw = frontLineWorkerService.getFlwBycontactNo("8989898987");
        assertNotNull(flw);
        assertTrue(22L == flw.getFlwId());

        // testFrontLineWorker flwId and stateId combination non null, ContactNo non null, present in same Record
        // testFrontLineWorker flwId and stateId combination non null, ContactNo non null, present in different Record

        flw = frontLineWorkerService.getFlwBycontactNo("1717171717");
        assertNotNull(flw);
        assertTrue(6L == flw.getFlwId());
        assertEquals(Designation.ASHA, flw.getDesignation());

        // testFrontLineWorker flwId not present in both create and update

        flw = frontLineWorkerService.getFlwBycontactNo("1818181818");
        assertNotNull(flw);
        assertEquals(Designation.ASHA, flw.getDesignation());
        assertNull(flw.getFlwId());

        List<CsvFrontLineWorker> listFlwCsv = csvFrontLineWorkerService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        
        
    }

}

