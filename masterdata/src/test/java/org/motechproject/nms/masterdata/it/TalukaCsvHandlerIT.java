package org.motechproject.nms.masterdata.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.CsvTaluka;
import org.motechproject.nms.masterdata.event.handler.TalukaCsvUploadHandler;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.masterdata.service.TalukaCsvService;
import org.motechproject.nms.masterdata.service.TalukaService;
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
 * This class is used to test(IT) the operations of Taluka Csv
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class TalukaCsvHandlerIT extends BasePaxIT {

    private TalukaCsvUploadHandler talukaCsvUploadHandler;

    List<Long> createdIds = new ArrayList<Long>();

    @Inject
    private StateService stateService;

    @Inject
    private DistrictService districtService;

    @Inject
    private TalukaCsvService talukaCsvService;

    @Inject
    private TalukaService talukaService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Before
    public void setUp() {
        talukaCsvUploadHandler = new TalukaCsvUploadHandler(stateService,
                districtService, talukaCsvService, talukaService, bulkUploadErrLogService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(talukaCsvService);
        assertNotNull(talukaService);
        assertNotNull(districtService);
        assertNotNull(stateService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(talukaCsvUploadHandler);
    }

    @Test
    public void testTalukaCsvHandler() {

        State stateData = TestHelper.getStateData();
        District districtData = TestHelper.getDistrictData();
        stateData.getDistrict().add(districtData);
        stateService.create(stateData);

        CsvTaluka csvData = TestHelper.getTalukaCsvData();
        CsvTaluka invalidCsvData = TestHelper.getInvalidTalukaCsvData();

        createTalukaCsvData(csvData);
        createTalukaCsvData(invalidCsvData);

        createdIds.add(csvData.getId());
        createdIds.add(invalidCsvData.getId());
        createdIds.add(csvData.getId() + 1);
        talukaCsvUploadHandler.talukaCsvSuccess(TestHelper.createMotechEvent(createdIds, LocationConstants.TALUKA_CSV_SUCCESS));


        Taluka talukaData = talukaService.findTalukaByParentCode(123L, 456L, 8L);

        assertNotNull(talukaData);
        assertTrue(123L == talukaData.getStateCode());
        assertTrue(456L == talukaData.getDistrictCode());
        assertTrue("Gabhana".equals(talukaData.getName()));

        csvData = TestHelper.getUpdatedTalukaCsvData();
        createTalukaCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        talukaCsvUploadHandler.talukaCsvSuccess(TestHelper.createMotechEvent(createdIds, LocationConstants.TALUKA_CSV_SUCCESS));
        Taluka talukaUpdateData = talukaService.findTalukaByParentCode(123L, 456L, 8L);

        assertNotNull(talukaUpdateData);
        assertTrue(123L == talukaUpdateData.getStateCode());
        assertTrue(456L == talukaUpdateData.getDistrictCode());
        assertTrue("Ghabhana".equals(talukaUpdateData.getName()));
    }

    private void clearId() {
        createdIds.clear();
    }

    @After
    public void tearDown() {
        stateService.deleteAll();
    }

    private void createTalukaCsvData(CsvTaluka csvData) {

        talukaCsvService.create(csvData);
    }
}
