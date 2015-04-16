package org.motechproject.nms.masterdata.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.event.handler.VillageCsvUploadHandler;
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
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This class is used to test(IT) the operations of Village Csv
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class VillageCsvHandlerIT extends BasePaxIT {

    private VillageCsvUploadHandler villageCsvUploadHandler;

    private List<Long> createdIds = new ArrayList<Long>();

    @Inject
    private StateService stateService;

    @Inject
    private DistrictService districtService;

    @Inject
    private TalukaService talukaService;

    @Inject
    private VillageService villageService;

    @Inject
    private VillageCsvService villageCsvService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private ValidatorService validatorService;

    @Before
    public void setUp() {
        villageCsvUploadHandler = new VillageCsvUploadHandler(validatorService,
                 talukaService, villageCsvService,
                villageService, bulkUploadErrLogService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(villageCsvService);
        assertNotNull(villageService);
        assertNotNull(talukaService);
        assertNotNull(districtService);
        assertNotNull(stateService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(validatorService);
        assertNotNull(villageCsvUploadHandler);
    }

    @Test
    public void testVillageCsvHandler() {

        State stateData = TestHelper.getStateData();
        District districtData = TestHelper.getDistrictData();
        Taluka talukaData = TestHelper.getTalukaData();

        stateData.getDistrict().add(districtData);
        districtData.getTaluka().add(talukaData);

        stateService.create(stateData);

        CsvVillage csvData = TestHelper.getVillageCsvData();
        CsvVillage invalidCsvData = TestHelper.getInvalidVillageCsvData();

        createVillageCsvData(csvData);
        createVillageCsvData(invalidCsvData);

        createdIds.add(csvData.getId());
        createdIds.add(invalidCsvData.getId());
        createdIds.add(csvData.getId() + 1);

        villageCsvUploadHandler.villageCsvSuccess(TestHelper.createMotechEvent(createdIds, LocationConstants.VILLAGE_CSV_SUCCESS));
        Village villageData = villageService.findVillageByParentCode(123L, 456L, 8L, 122656L);

        assertNotNull(villageData);
        assertTrue(123L == villageData.getStateCode());
        assertTrue(456L == villageData.getDistrictCode());
        assertTrue(8L == villageData.getTalukaCode());
        assertTrue(122656L == villageData.getVillageCode());
        assertTrue("Alampur".equals(villageData.getName()));

        csvData = TestHelper.getUpdateVillageCsvData();
        createVillageCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        villageCsvUploadHandler.villageCsvSuccess(TestHelper.createMotechEvent(createdIds, LocationConstants.VILLAGE_CSV_SUCCESS));
        Village villageUpdateData = villageService.findVillageByParentCode(123L, 456L, 8L, 122656L);

        assertNotNull(villageUpdateData);
        assertTrue(123L == villageUpdateData.getStateCode());
        assertTrue(456L == villageUpdateData.getDistrictCode());
        assertTrue(8L == villageData.getTalukaCode());
        assertTrue(122656L == villageUpdateData.getVillageCode());
        assertTrue("Ahamadabad".equals(villageUpdateData.getName()));
    }

    private void clearId() {
        createdIds.clear();
    }

    @After
    public void tearDown() {
        stateService.deleteAll();
    }

    private void createVillageCsvData(CsvVillage csvData) {

        villageCsvService.create(csvData);
    }
}
