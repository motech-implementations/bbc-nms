package org.motechproject.nms.masterdata.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.TalukaCsv;
import org.motechproject.nms.masterdata.event.handler.TalukaCsvUploadHandler;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
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
 * Created by abhishek on 14/2/15.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class TalukaCsvHandlerIT extends BasePaxIT {

    private TalukaCsvUploadHandler  talukaCsvUploadHandler;

    List<Long> createdIds = new ArrayList<Long>();

    @Inject
    private StateRecordsDataService stateRecordsDataService;

    @Inject
    private DistrictRecordsDataService districtRecordsDataService;

    @Inject
    private TalukaCsvRecordsDataService talukaCsvRecordsDataService;

    @Inject
    private TalukaRecordsDataService talukaRecordsDataService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Before
    public void setUp() {
        talukaCsvUploadHandler = new TalukaCsvUploadHandler(stateRecordsDataService,
                districtRecordsDataService, talukaCsvRecordsDataService,talukaRecordsDataService, bulkUploadErrLogService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(talukaCsvRecordsDataService);
        assertNotNull(talukaRecordsDataService);
        assertNotNull(districtRecordsDataService);
        assertNotNull(stateRecordsDataService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(talukaCsvUploadHandler);
    }

    @Test
    public void testTalukaCsvHandler() {

        State stateData = TestHelper.getStateData();
        District districtData = TestHelper.getDistrictData();
        stateData.getDistrict().add(districtData);
        stateRecordsDataService.create(stateData);

        TalukaCsv csvData = TestHelper.getTalukaCsvData();
        createTalukaCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());
        talukaCsvUploadHandler.talukaCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.TALUKA_CSV_SUCCESS));


        Taluka talukaData = talukaRecordsDataService.findTalukaByParentCode(123L, 456L,"8");

        assertNotNull(talukaData);
        assertTrue(123L == talukaData.getStateCode());
        assertTrue(456L == talukaData.getDistrictCode());
        assertTrue("Gabhana".equals(talukaData.getName()));

        csvData = TestHelper.getUpdatedTalukaCsvData();
        createTalukaCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        talukaCsvUploadHandler.talukaCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.TALUKA_CSV_SUCCESS));
        Taluka talukaUpdateData = talukaRecordsDataService.findTalukaByParentCode(123L, 456L, "8");

        assertNotNull(talukaUpdateData);
        assertTrue(123L == talukaUpdateData.getStateCode());
        assertTrue(456L == talukaUpdateData.getDistrictCode());
        assertTrue("Ghabhana".equals(talukaUpdateData.getName()));

        csvData = TestHelper.getDeleteTalukaCsvData();
        createTalukaCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        talukaCsvUploadHandler.talukaCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.TALUKA_CSV_SUCCESS));
        Taluka talukaDeletedData = talukaRecordsDataService.findTalukaByParentCode(123L, 456L, "8");

        assertNull(talukaDeletedData);
    }

    private void clearId() {
        createdIds.clear();
    }

    private void createTalukaCsvData(TalukaCsv csvData) {

        talukaCsvRecordsDataService.create(csvData);
    }
}
