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
import org.motechproject.nms.masterdata.domain.Circle;
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
import org.motechproject.nms.masterdata.service.LocationService;
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

    @Inject
    private FrontLineWorkerService frontLineWorkerService;

    @Inject
    private FrontLineWorkerCsvService frontLineWorkerCsvService;

    private FrontLineWorkerUploadHandler frontLineWorkerUploadHandler;

    private State stateData;

    private District districtData;

    private Taluka talukaData;

    private Village villageData;

    private HealthBlock healthBlockData;

    private HealthFacility healthFacilityData;

    private HealthSubFacility healthSubFacilityData;

    private static boolean setUpIsDone = false;

    private State state;

    private District district;

    private Circle circle;

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
            System.out.println("");
            state = new State();
            district = new District();
            circle = new Circle();
            state = createState();
            district = createDistrict();
            createTaluka();
            createHealthBlock();
            createHealthFacility();
            createHealthSubFacility();
            createVillage();

            FrontLineWorkerCsv frontLineWorkerCsv;
            FrontLineWorkerCsv frontLineWorkerCsvdb;

            // testFrontLineWorkerValidDataGetByPhnNo

            frontLineWorkerCsv = new FrontLineWorkerCsv("1", "12", "9990545494", "Etasha",
                    "USHA", "123", "1", "1234", "12345", "123456", "1234",
                    "9876", "1234", "True", null);

            frontLineWorkerCsv.setCreator("Etasha");
            frontLineWorkerCsv.setModifiedBy("Etasha");
            frontLineWorkerCsv.setOwner("Etasha");


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerValidDataGetById

            frontLineWorkerCsv = new FrontLineWorkerCsv("2", "12", "9990545495", "Etasha",
                    "USHA", "123", "1", "1234", "12345", "123456", "1234",
                    "9876", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerValidDataLargerphnNo

            frontLineWorkerCsv = new FrontLineWorkerCsv("3", "12", "99905454950", "Etasha",
                    "USHA", "123", "1", "1234", "12345", "123456", "1234",
                    "9876", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerValidDatasmallPhnNo

            frontLineWorkerCsv = new FrontLineWorkerCsv("4", "12", "99905", "Etasha",
                    "USHA", "123", "1", "1234", "12345", "123456", "1234",
                    "9876", "1234", "True", null);

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
                    "9876", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerUpdationNoFlwId Part 2

            frontLineWorkerCsv = new FrontLineWorkerCsv("", "12", "1234567890", "Jyoti",
                    "USHA", "123", "1", "1234", "12345", "123456", "1234",
                    "9876", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerUpdation Part 1

            frontLineWorkerCsv = new FrontLineWorkerCsv("10", "12", "1234567890", "Jyoti",
                    "ANM", "123", "1", "1234", "12345", "123456", "1234",
                    "9876", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerUpdation Part 2

            frontLineWorkerCsv = new FrontLineWorkerCsv("10", "12", "1234567890", "Jyoti2",
                    "ANM", "123", "1", "1234", "12345", "123456", "1234",
                    "1234", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerStatusInvalidToValid Part 1

            frontLineWorkerCsv = new FrontLineWorkerCsv("11", "12", "2222222222", "Jaya",
                    "AWW", "123", "1", "1234", "12345", "123456", "1234",
                    "9876", "1234", "False", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerStatusInvalidToValid Part 2

            frontLineWorkerCsv = new FrontLineWorkerCsv("11", "12", "2222222222", "Jaya2",
                    "AWW", "123", "1", "1234", "12345", "123456", "1234",
                    "1234", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerStatusValidToInvalid Part 1

            frontLineWorkerCsv = new FrontLineWorkerCsv("12", "12", "3333333333", "Sushma",
                    "AWW", "123", "1", "1234", "12345", "123456", "1234",
                    "9876", "1234", "True", null);

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
                    "9876", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerUpdationWithIsValidNull Part 2

            frontLineWorkerCsv = new FrontLineWorkerCsv("13", "12", "4444444444", "Rekha",
                    "ASHA", "123", "1", "1234", "12345", "123456", "1234",
                    "1234", "1234", "True", null);

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
                    "1234", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerVillageWithoutTaluka

            frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "9990000000", "Anjali",
                    "USHA", "123", null, "1234", "12345", "123456", "1234",
                    "9876", "1234", "True", null);


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerHealthBlockWithoutTaluka

            frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "9990000000", "Etasha",
                    "USHA", "123", null, "1234", "12345", "123456", null,
                    "9876", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerPhcWithoutHealthBlock

            frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "9990000000", "Etasha",
                    "USHA", "123", "1", null, "12345", "123456", "1234",
                    "9876", "1234", "True", null);


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerSubCentreWithoutPhc

            frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "9990000000", "Etasha",
                    "USHA", "123", "1", "1234", null, "123456", "1234",
                    "9876", "1234", "True", null);


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            //nms generated id is null
 /*           frontLineWorkerCsv = new FrontLineWorkerCsv("10", "12", "000", "Rashi",
                    "USHA", "123", "1", "1234", "12345", "123456","1234",
                    "9876", "1234", "True", null);

            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            FrontLineWorker frontLineWorker = new FrontLineWorker(10L,"8888888888", "Rashi", Designation.USHA, null,
                    12L, state, district, null, null, null, null, null,"9876", "1234", Status.INACTIVE, null, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);

            public FrontLineWorker(Long flwId, String contactNo, String name, Designation designation, Long operatorId,
                    Long stateCode, State stateId, District districtId, Taluka talukaId,
                    HealthBlock healthBlockId, HealthFacility healthFacilityId, HealthSubFacility
                    healthSubFacilityId, Village villageId, String ashaNumber, String adhaarNumber,
                    Status status, Integer languageLocationCodeId, Integer defaultLanguageLocationCodeId) {*/


            // testFrontLineWorkerContactNoIsDifferent

            frontLineWorkerCsv = new FrontLineWorkerCsv("20", "12", "8826575961", "Etasha",
                    "USHA", "123", "1", "1234", null, "123456", "1234",
                    "9876", "1234", "True", null);


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

            // testFrontLineWorkerFlwIdIsDifferent

            frontLineWorkerCsv = new FrontLineWorkerCsv("25", "12", "9990545494", "Etasha",
                    "USHA", "123", "1", "1234", null, "123456", "1234",
                    "9876", "1234", "True", null);


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());

           /* // testFrontLineWorkerFlwIdAndContactNoIsDifferentAndStateIsInvalid

           frontLineWorkerCsv = new FrontLineWorkerCsv("2", "12", "2222222222", "Etasha",
                                "USHA", "123", "1", "1234", null, "123456", "1234",
                                "9876", "1234", "True", "48");


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());*/

            // testFrontLineWorkerFlwIdAndContactNoIsDifferentAndStateIsInactive

            frontLineWorkerCsv = new FrontLineWorkerCsv("3", "12", "4444444444", "Etasha",
                    "USHA", "123", "1", "1234", null, "123456", "1234",
                    "9876", "1234", "True", null);


            frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
            assertNotNull(frontLineWorkerCsvdb);
            uploadedIds.add(frontLineWorkerCsvdb.getId());


            parameters.put("csv-import.created_ids", uploadedIds);
            parameters.put("csv-import.filename", "FrontLineWorker.csv");


        }
        // do the setup
        setUpIsDone = true;

    }


    private State createState() {
        State state = new State();
        state.setName("Delhi");
        state.setStateCode(12L);
        state.setCreator("Etasha");
        state.setOwner("Etasha");
        state.setModifiedBy("Etasha");
        stateRecordsDataService.create(state);
        assertNotNull(state);
        return state;
    }

    private District createDistrict() {
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
        return district;
    }

    private void createTaluka() {
        Taluka taluka = new Taluka();
        taluka.setName("taluka");
        taluka.setStateCode(12L);
        taluka.setDistrictCode(123L);
        taluka.setTalukaCode(1L);
        taluka.setCreator("Etasha");
        taluka.setOwner("Etasha");
        taluka.setModifiedBy("Etasha");
        District districtData = districtRecordsDataService.findDistrictByParentCode(taluka.getDistrictCode(), taluka.getStateCode());
        districtData.getTaluka().add(taluka);
        districtRecordsDataService.update(districtData);
        assertNotNull(taluka);
    }

    private void createVillage() {
        Village village = new Village();
        village.setName("villageName");
        village.setStateCode(12L);
        village.setDistrictCode(123L);
        village.setTalukaCode(1L);
        village.setVillageCode(1234L);
        village.setCreator("Etasha");
        village.setOwner("Etasha");
        village.setModifiedBy("Etasha");

        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(village.getStateCode(),
                village.getDistrictCode(), village.getTalukaCode());
        talukaRecord.getVillage().add(village);
        talukaRecordsDataService.update(talukaRecord);
        assertNotNull(village);
    }

    private void createHealthBlock() {
        HealthBlock healthBlock = new HealthBlock();
        healthBlock.setName("healthBlockName");
        healthBlock.setStateCode(12L);
        healthBlock.setDistrictCode(123L);
        healthBlock.setTalukaCode(1L);
        healthBlock.setHealthBlockCode(1234L);
        healthBlock.setCreator("Etasha");
        healthBlock.setOwner("Etasha");
        healthBlock.setModifiedBy("Etasha");
        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(healthBlock.getStateCode(),
                healthBlock.getDistrictCode(), healthBlock.getTalukaCode());
        talukaRecord.getHealthBlock().add(healthBlock);
        talukaRecordsDataService.update(talukaRecord);

        assertNotNull(healthBlock);
    }


    private void createHealthFacility() {
        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setName("healthFacilityName");
        healthFacility.setStateCode(12L);
        healthFacility.setDistrictCode(123L);
        healthFacility.setTalukaCode(1L);
        healthFacility.setHealthBlockCode(1234L);
        healthFacility.setHealthFacilityCode(12345L);
        healthFacility.setCreator("Etasha");
        healthFacility.setOwner("Etasha");
        healthFacility.setModifiedBy("Etasha");

        HealthBlock healthBlockData = healthBlockRecordsDataService.findHealthBlockByParentCode(
                healthFacility.getStateCode(), healthFacility.getDistrictCode(), healthFacility.getTalukaCode(),
                healthFacility.getHealthBlockCode());
        healthBlockData.getHealthFacility().add(healthFacility);
        healthBlockRecordsDataService.update(healthBlockData);
        assertNotNull(healthFacility);
    }


    private void createHealthSubFacility() {
        HealthSubFacility healthSubFacility = new HealthSubFacility();
        healthSubFacility.setName("healthSubFacilityName");
        healthSubFacility.setStateCode(12L);
        healthSubFacility.setDistrictCode(123L);
        healthSubFacility.setTalukaCode(1L);
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
        /*assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());*/
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
/*
        assertTrue(4L == flw.getFlwId());
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
        *//*assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());*//*
        assertEquals(Status.INACTIVE, flw.getStatus());*/

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

        flw = frontLineWorkerService.getFlwBycontactNo("2222222222");
        assertNotNull(flw);
        assertEquals(Status.INVALID, flw.getStatus());

        // testFrontLineWorkerStatusInvalidToValid Part 2

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("2222222222");
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
        //assertEquals("9876", flw.getAshaNumber());

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

        /*//nms generated id is null

        flw = frontLineWorkerService.getFlwBycontactNo("8888888888");
        assertNotNull(flw);
        assertTrue(null == flw.getId());
*/
        // testFrontLineWorkerPhoneNoIsDifferent

        flw = frontLineWorkerService.getFlwBycontactNo("8826575961");
        assertNull(flw);

        // testFrontLineWorkerFlwIdIsDifferent

        flw = frontLineWorkerService.getFlwBycontactNo("9990545494");
        assertNotNull(flw);

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

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);


    }


   /* @Test
    public void testFrontLineWorkerValidDataGetByPhnNo() {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setContactNo("9990545494");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");


        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);

        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("9990545494");

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

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
    }
*/

    /*@Test
    public void testFrontLineWorkerValidDataGetById() {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("2");
        frontLineWorkerCsv.setContactNo("9990545495");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");


        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwByFlwIdAndStateId(2L, 12L);

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
        assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
    }*/

   /* @Test
    public void testFrontLineWorkerValidDataLargerphnNo() {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("3");
        frontLineWorkerCsv.setContactNo("99905454950");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwByFlwIdAndStateId(3L, 12L);

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
        assertEquals("Etasha", flw.getCreator());
        assertEquals("Etasha", flw.getModifiedBy());
        assertEquals("Etasha", flw.getOwner());
        assertEquals(Status.INACTIVE, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
    }*/

    /*@Test
    public void testFrontLineWorkerValidDatasmallPhnNo() {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("4");
        frontLineWorkerCsv.setContactNo("99905");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwByFlwIdAndStateId(4L, 12L);

        assertNotNull(flw);

        assertTrue(4L == flw.getFlwId());
        assertEquals("99905", flw.getContactNo());
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

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

    }*/

   /* @Test
    public void testFrontLineWorkerNoState() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();


        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("11");//Invalid
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        throw new DataValidationException();

    }*/


    /*@Test
    public void testFrontLineWorkerNoDistrict() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("122");//Invalid
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }*/


    /*@Test
    public void testFrontLineWorkerInvalidTaluka() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1233");//Invalid
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/
   /* @Test
    public void testFrontLineWorkerInvalidVillage() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1233");//invalid
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }*/

    /*@Test
    public void testFrontLineWorkerInvalidHealthBlock() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1233");//invalid
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }*/

    /*@Test
    public void testFrontLineWorkerInvalidHealthFacility() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12344");//Invalid
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);
        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }*/

   /* @Test
    public void testFrontLineWorkerInvalidHealthSubFacility() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123455");//Invalid
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/
    /*@Test
    public void testFrontLineWorkerInvalidDesignation() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ABC");//Invalid
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }*/

    /*@Test
    public void testFrontLineWorkerContactNoAbsent() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/
    /*@Test
    public void testFrontLineWorkerStateCodeAbsent() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }*/

    /*@Test
    public void testFrontLineWorkerDistrictCodeAbsent() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/
    /*@Test
    public void testFrontLineWorkerDesignationAbsent() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/

   /* @Test
    public void testFrontLineWorkerTalukaAbsentVillagePresent() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/
   /* @Test
    public void testFrontLineWorkerTalukaAbsentHealthBlockPresent() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/
    /*@Test
    public void testFrontLineWorkerHBAbsentPHCPresent() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/
    /*@Test
    public void testFrontLineWorkerPHCAbsentSSCPresent() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("1");
        frontLineWorkerCsv.setName("etasha");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setContactNo("9990545496");
        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setSubCentreCode("123456");
        frontLineWorkerCsv.setIsValid("true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9990545496");
        assertNull(frontLineWorker);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);

        throw new DataValidationException();

    }
*/

   /* @Test
    public void testFrontLineWorkerUpdationNoFlwId() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("10");
        frontLineWorkerCsv.setContactNo("1234567890");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Jyoti");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");

        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("1234567890");

        assertNotNull(flw);

        //Updation
        FrontLineWorkerCsv frontLineWorkerCsvNew = new FrontLineWorkerCsv();
        frontLineWorkerCsvNew.setFlwId("");
        frontLineWorkerCsvNew.setContactNo("1234567890");
        frontLineWorkerCsvNew.setType("USHA");
        frontLineWorkerCsvNew.setName("Jyoti");

        frontLineWorkerCsvNew.setStateCode("12");
        frontLineWorkerCsvNew.setDistrictCode("123");
        frontLineWorkerCsvNew.setTalukaCode("1");
        frontLineWorkerCsvNew.setVillageCode("1234");
        frontLineWorkerCsvNew.setHealthBlockCode("1234");
        frontLineWorkerCsvNew.setPhcCode("12345");
        frontLineWorkerCsvNew.setSubCentreCode("123456");

        frontLineWorkerCsvNew.setAdhaarNo("1234");
        frontLineWorkerCsvNew.setAshaNumber("9876");
        frontLineWorkerCsvNew.setIsValid("True");
        frontLineWorkerCsvNew.setOwner("Etasha");
        frontLineWorkerCsvNew.setCreator("Etasha");
        frontLineWorkerCsvNew.setModifiedBy("Etasha");
        assertNotNull(frontLineWorkerCsvService);

        FrontLineWorkerCsv frontLineWorkerCsvdb2 = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsvNew);

        Map<String, Object> parameters_new = new HashMap<>();
        List<Long> uploadedIds_new = new ArrayList<Long>();

        uploadedIds_new.add(frontLineWorkerCsvdb2.getId());
        parameters_new.put("csv-import.created_ids", uploadedIds_new);
        parameters_new.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEventNew = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters_new);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEventNew);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234567890");
        assertNotNull(frontLineWorker);
        assertTrue(10L == frontLineWorker.getFlwId());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        throw new DataValidationException();

    }*/

    /*@Test
    public void testFrontLineWorkerUpdation() {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("10");
        frontLineWorkerCsv.setContactNo("1234567890");
        frontLineWorkerCsv.setType("ANM");
        frontLineWorkerCsv.setName("Jyoti");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);
        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("1234567890");

        assertNotNull(flw);

        //Updation
        FrontLineWorkerCsv frontLineWorkerCsvNew = new FrontLineWorkerCsv();
        frontLineWorkerCsvNew.setFlwId("10");
        frontLineWorkerCsvNew.setContactNo("1234567890");
        frontLineWorkerCsvNew.setType("ANM");
        frontLineWorkerCsvNew.setName("Jyoti2");//changed from Jyoti to Jyoti2

        frontLineWorkerCsvNew.setStateCode("12");
        frontLineWorkerCsvNew.setDistrictCode("123");
        frontLineWorkerCsvNew.setTalukaCode("1");
        frontLineWorkerCsvNew.setVillageCode("1234");
        frontLineWorkerCsvNew.setHealthBlockCode("1234");
        frontLineWorkerCsvNew.setPhcCode("12345");
        frontLineWorkerCsvNew.setSubCentreCode("123456");

        frontLineWorkerCsvNew.setAdhaarNo("1234");
        frontLineWorkerCsvNew.setAshaNumber("1234");//Changed from 9876 to 1234
        frontLineWorkerCsvNew.setIsValid("True");
        frontLineWorkerCsvNew.setOwner("Etasha");
        frontLineWorkerCsvNew.setCreator("Etasha");
        frontLineWorkerCsvNew.setModifiedBy("Etasha");

        assertNotNull(frontLineWorkerCsvService);

        FrontLineWorkerCsv frontLineWorkerCsvdb2 = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsvNew);

        Map<String, Object> parameters_new = new HashMap<>();
        List<Long> uploadedIds_new = new ArrayList<Long>();

        uploadedIds_new.add(frontLineWorkerCsvdb2.getId());
        parameters_new.put("csv-import.created_ids", uploadedIds_new);
        parameters_new.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEventNew = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters_new);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEventNew);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234567890");
        assertNotNull(frontLineWorker);
        assertEquals("1234", frontLineWorker.getAshaNumber());
        assertEquals("Jyoti2", frontLineWorker.getName());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
    }
*/
    /*@Test
    public void testFrontLineWorkerStatusInvalidToValid() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("11");
        frontLineWorkerCsv.setContactNo("2222222222");
        frontLineWorkerCsv.setType("AWW");
        frontLineWorkerCsv.setName("Jaya");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("False");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("2222222222");

        assertNotNull(flw);
        assertEquals(Status.INVALID, flw.getStatus());

        //Updation
        FrontLineWorkerCsv frontLineWorkerCsvNew = new FrontLineWorkerCsv();
        frontLineWorkerCsvNew.setFlwId("11");
        frontLineWorkerCsvNew.setContactNo("2222222222");
        frontLineWorkerCsvNew.setType("AWW");
        frontLineWorkerCsvNew.setName("Jaya2");//changed from Jaya to Jaya2

        frontLineWorkerCsvNew.setStateCode("12");
        frontLineWorkerCsvNew.setDistrictCode("123");
        frontLineWorkerCsvNew.setTalukaCode("1");
        frontLineWorkerCsvNew.setVillageCode("1234");
        frontLineWorkerCsvNew.setHealthBlockCode("1234");
        frontLineWorkerCsvNew.setPhcCode("12345");
        frontLineWorkerCsvNew.setSubCentreCode("123456");

        frontLineWorkerCsvNew.setAdhaarNo("1234");
        frontLineWorkerCsvNew.setAshaNumber("1234");//Changed from 9876 to 1234
        frontLineWorkerCsvNew.setIsValid("True");//Changed from false to true
        frontLineWorkerCsvNew.setOwner("Etasha");
        frontLineWorkerCsvNew.setCreator("Etasha");
        frontLineWorkerCsvNew.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb2 = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsvNew);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters_new = new HashMap<>();
        List<Long> uploadedIds_new = new ArrayList<Long>();

        uploadedIds_new.add(frontLineWorkerCsvdb2.getId());
        parameters_new.put("csv-import.created_ids", uploadedIds_new);
        parameters_new.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEventNew = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters_new);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEventNew);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("2222222222");
        assertNotNull(frontLineWorker);
        assertEquals("9876", frontLineWorker.getAshaNumber());
        assertEquals("Jaya", frontLineWorker.getName());
        assertEquals(Status.INVALID, flw.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        throw new DataValidationException();

    }
*/

    /*@Test
    public void testFrontLineWorkerStatusValidToInvalid() {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("12");
        frontLineWorkerCsv.setContactNo("3333333333");
        frontLineWorkerCsv.setType("AWW");
        frontLineWorkerCsv.setName("Sushma");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("3333333333");

        assertNotNull(flw);
        assertEquals(Status.INACTIVE, flw.getStatus());

        //Updation
        FrontLineWorkerCsv frontLineWorkerCsvNew = new FrontLineWorkerCsv();
        frontLineWorkerCsvNew.setFlwId("12");
        frontLineWorkerCsvNew.setContactNo("3333333333");
        frontLineWorkerCsvNew.setType("AWW");
        frontLineWorkerCsvNew.setName("Sushma");

        frontLineWorkerCsvNew.setStateCode("12");
        frontLineWorkerCsvNew.setDistrictCode("123");
        frontLineWorkerCsvNew.setTalukaCode("1");
        frontLineWorkerCsvNew.setVillageCode("1234");
        frontLineWorkerCsvNew.setHealthBlockCode("1234");
        frontLineWorkerCsvNew.setPhcCode("12345");
        frontLineWorkerCsvNew.setSubCentreCode("123456");

        frontLineWorkerCsvNew.setAdhaarNo("1234");
        frontLineWorkerCsvNew.setAshaNumber("1234");//Changed from 9876 to 1234
        frontLineWorkerCsvNew.setIsValid("False");//Changed from true to false
        frontLineWorkerCsvNew.setOwner("Etasha");
        frontLineWorkerCsvNew.setCreator("Etasha");
        frontLineWorkerCsvNew.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb2 = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsvNew);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters_new = new HashMap<>();
        List<Long> uploadedIds_new = new ArrayList<Long>();

        uploadedIds_new.add(frontLineWorkerCsvdb2.getId());
        parameters_new.put("csv-import.created_ids", uploadedIds_new);
        parameters_new.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEventNew = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters_new);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEventNew);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("3333333333");
        assertNotNull(frontLineWorker);
        assertEquals("1234", frontLineWorker.getAshaNumber());
        assertEquals(Status.INVALID, frontLineWorker.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
    }*/

    /*@Test
    public void testFrontLineWorkerUpdationWithIsValidNull() {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("13");
        frontLineWorkerCsv.setContactNo("4444444444");
        frontLineWorkerCsv.setType("ASHA");
        frontLineWorkerCsv.setName("Rekha");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");


        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("4444444444");

        assertNotNull(flw);
        assertEquals(Status.INACTIVE, flw.getStatus());
        assertEquals("9876", flw.getAshaNumber());

        //Updation
        FrontLineWorkerCsv frontLineWorkerCsvNew = new FrontLineWorkerCsv();

        frontLineWorkerCsvNew.setFlwId("13");
        frontLineWorkerCsvNew.setContactNo("4444444444");
        frontLineWorkerCsvNew.setType("ASHA");
        frontLineWorkerCsvNew.setName("Rekha");

        frontLineWorkerCsvNew.setStateCode("12");
        frontLineWorkerCsvNew.setDistrictCode("123");
        frontLineWorkerCsvNew.setTalukaCode("1");
        frontLineWorkerCsvNew.setVillageCode("1234");
        frontLineWorkerCsvNew.setHealthBlockCode("1234");
        frontLineWorkerCsvNew.setPhcCode("12345");
        frontLineWorkerCsvNew.setSubCentreCode("123456");

        frontLineWorkerCsvNew.setAdhaarNo("1234");
        frontLineWorkerCsvNew.setAshaNumber("1234");
        frontLineWorkerCsvNew.setIsValid("True");
        frontLineWorkerCsvNew.setOwner("Etasha");
        frontLineWorkerCsvNew.setCreator("Etasha");
        frontLineWorkerCsvNew.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb2 = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsvNew);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters_new = new HashMap<>();
        List<Long> uploadedIds_new = new ArrayList<Long>();

        uploadedIds_new.add(frontLineWorkerCsvdb2.getId());
        parameters_new.put("csv-import.created_ids", uploadedIds_new);
        parameters_new.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEventNew = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters_new);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEventNew);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("4444444444");
        assertNotNull(frontLineWorker);
        assertEquals("1234", frontLineWorker.getAshaNumber());
        assertEquals(Status.INACTIVE, frontLineWorker.getStatus());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
    }*/

    /*@Test
    public void testFrontLineWorkerUpdationWithNoFlwId() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("14");
        frontLineWorkerCsv.setContactNo("12345");
        frontLineWorkerCsv.setType("ANM");
        frontLineWorkerCsv.setName("Jyoti");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("12345");

        assertNotNull(flw);
        assertTrue(14L == flw.getFlwId());

        //Updation
        FrontLineWorkerCsv frontLineWorkerCsvNew = new FrontLineWorkerCsv();
        frontLineWorkerCsvNew.setContactNo("12345");
        frontLineWorkerCsvNew.setType("ANM");
        frontLineWorkerCsvNew.setName("Jyoti2");//changed from Jyoti to Jyoti2

        frontLineWorkerCsvNew.setStateCode("12");
        frontLineWorkerCsvNew.setDistrictCode("123");
        frontLineWorkerCsvNew.setTalukaCode("1");
        frontLineWorkerCsvNew.setVillageCode("1234");
        frontLineWorkerCsvNew.setHealthBlockCode("1234");
        frontLineWorkerCsvNew.setPhcCode("12345");
        frontLineWorkerCsvNew.setSubCentreCode("123456");

        frontLineWorkerCsvNew.setAdhaarNo("1234");
        frontLineWorkerCsvNew.setAshaNumber("1234");//Changed from 9876 to 1234
        frontLineWorkerCsvNew.setIsValid("True");
        frontLineWorkerCsvNew.setOwner("Etasha");
        frontLineWorkerCsvNew.setCreator("Etasha");
        frontLineWorkerCsvNew.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb2 = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsvNew);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters_new = new HashMap<>();
        List<Long> uploadedIds_new = new ArrayList<Long>();

        uploadedIds_new.add(frontLineWorkerCsvdb2.getId());
        parameters_new.put("csv-import.created_ids", uploadedIds_new);
        parameters_new.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEventNew = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters_new);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEventNew);
        thrown.expect(DataValidationException.class);

        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("12345");
        assertNotNull(frontLineWorker);
        assertEquals("9876", frontLineWorker.getAshaNumber());
        assertEquals("Jyoti", frontLineWorker.getName());
        assertTrue(14L == frontLineWorker.getFlwId());

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        throw new DataValidationException();
    }
*/
    /*@Test
    public void testFrontLineWorkerVillageWithoutTaluka() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("20");
        frontLineWorkerCsv.setContactNo("9990");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Anjali");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);


        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("9990");

        assertNull(flw);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        throw new DataValidationException();
    }

*/
    /*@Test
    public void testFrontLineWorkerHealthBlockWithoutTaluka() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("20");
        frontLineWorkerCsv.setContactNo("9990");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);
        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("9990");

        assertNull(flw);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        throw new DataValidationException();
    }*/

    /*@Test
    public void testFrontLineWorkerPhcWithoutHealthBlock() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("20");
        frontLineWorkerCsv.setContactNo("9990");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setPhcCode("12345");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("9990");
        assertNull(flw);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        throw new DataValidationException();
    }*/

   /* @Test
    public void testFrontLineWorkerSubCentreWithoutPhc() throws DataValidationException {

        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();

        frontLineWorkerCsv.setFlwId("20");
        frontLineWorkerCsv.setContactNo("9990");
        frontLineWorkerCsv.setType("USHA");
        frontLineWorkerCsv.setName("Etasha");

        frontLineWorkerCsv.setStateCode("12");
        frontLineWorkerCsv.setDistrictCode("123");
        frontLineWorkerCsv.setTalukaCode("1");
        frontLineWorkerCsv.setVillageCode("1234");
        frontLineWorkerCsv.setHealthBlockCode("1234");
        frontLineWorkerCsv.setSubCentreCode("123456");

        frontLineWorkerCsv.setAdhaarNo("1234");
        frontLineWorkerCsv.setAshaNumber("9876");
        frontLineWorkerCsv.setIsValid("True");
        frontLineWorkerCsv.setOwner("Etasha");
        frontLineWorkerCsv.setCreator("Etasha");
        frontLineWorkerCsv.setModifiedBy("Etasha");

        FrontLineWorkerCsv frontLineWorkerCsvdb = frontLineWorkerCsvService.createFrontLineWorkerCsv(frontLineWorkerCsv);

        assertNotNull(frontLineWorkerCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");

        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        frontLineWorkerUploadHandler.flwDataHandlerSuccess(motechEvent);
        thrown.expect(DataValidationException.class);
        FrontLineWorker flw = frontLineWorkerService.getFlwBycontactNo("9990");

        assertNull(flw);

        List<FrontLineWorkerCsv> listFlwCsv = frontLineWorkerCsvService.retrieveAllFromCsv();
        assertTrue(listFlwCsv.size() == 0);
        throw new DataValidationException();
    }*/
}

