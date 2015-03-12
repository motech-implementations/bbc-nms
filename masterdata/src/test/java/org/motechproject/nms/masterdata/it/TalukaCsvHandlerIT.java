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
import org.motechproject.nms.masterdata.repository.TalukaCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.nms.masterdata.service.StateService;
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
 * Created by abhishek on 14/2/15.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class TalukaCsvHandlerIT extends BasePaxIT {

    private TalukaCsvUploadHandler  talukaCsvUploadHandler;

    List<Long> createdIds = new ArrayList<Long>();

    @Inject
    private StateService stateService;

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
        talukaCsvUploadHandler = new TalukaCsvUploadHandler(stateService,
                districtRecordsDataService, talukaCsvRecordsDataService,talukaRecordsDataService, bulkUploadErrLogService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(talukaCsvRecordsDataService);
        assertNotNull(talukaRecordsDataService);
        assertNotNull(districtRecordsDataService);
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

        TalukaCsv csvData = TestHelper.getTalukaCsvData();
        TalukaCsv invalidCsvData = TestHelper.getInvalidTalukaCsvData();

        createTalukaCsvData(csvData);
        createTalukaCsvData(invalidCsvData);

        createdIds.add(csvData.getId());
        createdIds.add(invalidCsvData.getId());
        createdIds.add(csvData.getId()+1);
        talukaCsvUploadHandler.talukaCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.TALUKA_CSV_SUCCESS));


        Taluka talukaData = talukaRecordsDataService.findTalukaByParentCode(123L, 456L, 8L);

        assertNotNull(talukaData);
        assertTrue(123L == talukaData.getStateCode());
        assertTrue(456L == talukaData.getDistrictCode());
        assertTrue("Gabhana".equals(talukaData.getName()));

        csvData = TestHelper.getUpdatedTalukaCsvData();
        createTalukaCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        talukaCsvUploadHandler.talukaCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.TALUKA_CSV_SUCCESS));
        Taluka talukaUpdateData = talukaRecordsDataService.findTalukaByParentCode(123L, 456L, 8L);

        assertNotNull(talukaUpdateData);
        assertTrue(123L == talukaUpdateData.getStateCode());
        assertTrue(456L == talukaUpdateData.getDistrictCode());
        assertTrue("Ghabhana".equals(talukaUpdateData.getName()));
    }

    private void clearId() {
        createdIds.clear();
    }

    private void createTalukaCsvData(TalukaCsv csvData) {

        talukaCsvRecordsDataService.create(csvData);
    }
}
