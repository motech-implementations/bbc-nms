package org.motechproject.nms.masterdata.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.DistrictCsv;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.event.handler.DistrictCsvUploadHandler;
import org.motechproject.nms.masterdata.repository.DistrictCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
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

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by abhishek on 8/2/15.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class DistrictCsvHandlerIT extends BasePaxIT {

    private DistrictCsvUploadHandler districtCsvUploadHandler;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private StateRecordsDataService stateRecordsDataService;

    @Inject
    private DistrictRecordsDataService districtRecordsDataService;

    @Inject
    private DistrictCsvRecordsDataService districtCsvRecordsDataService;

    List<Long> createdIds = new ArrayList<Long>();

    @Before
    public void setUp() {
        districtCsvUploadHandler = new DistrictCsvUploadHandler(districtCsvRecordsDataService,
                districtRecordsDataService, stateRecordsDataService, bulkUploadErrLogService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(districtCsvRecordsDataService);
        assertNotNull(districtRecordsDataService);
        assertNotNull(stateRecordsDataService);
        assertNotNull(bulkUploadErrLogService);
    }

    @Test
    public void testDistrictCsvHandler() {

        State stateData = TestHelper.getStateData();
        stateRecordsDataService.create(stateData);

        DistrictCsv csvData = TestHelper.getDistrictCsvData();
        createDistrictCsvData(csvData);

        createdIds.add(csvData.getId());
        createdIds.add(csvData.getId()+1);
        districtCsvUploadHandler.districtCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.DISTRICT_CSV_SUCCESS));


        District districtData = districtRecordsDataService.findDistrictByParentCode(456L, 123L);

        assertNotNull(districtData);
        assertTrue(123L == districtData.getStateCode());
        assertTrue(456L == districtData.getDistrictCode());
        assertTrue("Agra".equals(districtData.getName()));

        csvData = TestHelper.getUpdatedDistrictCsvData();
        createDistrictCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        districtCsvUploadHandler.districtCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.DISTRICT_CSV_SUCCESS));
        District districtUpdateData = districtRecordsDataService.findDistrictByParentCode(456L, 123L);

        assertNotNull(districtUpdateData);
        assertTrue(123L == districtUpdateData.getStateCode());
        assertTrue(456L == districtUpdateData.getDistrictCode());
        assertTrue("Aligarh".equals(districtUpdateData.getName()));

        csvData = TestHelper.getDeleteDistrictCsvData();
        createDistrictCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        districtCsvUploadHandler.districtCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.DISTRICT_CSV_SUCCESS));
        District districtDeletedData = districtRecordsDataService.findDistrictByParentCode(456L, 123L);

        assertNull(districtDeletedData);
    }

    private void clearId() {
        createdIds.clear();
    }

    private void createDistrictCsvData(DistrictCsv csvData) {

        districtCsvRecordsDataService.create(csvData);
    }
}
