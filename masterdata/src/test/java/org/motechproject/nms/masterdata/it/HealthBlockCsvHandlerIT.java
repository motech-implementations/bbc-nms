package org.motechproject.nms.masterdata.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.event.handler.HealthBlockCsvUploadHandler;
import org.motechproject.nms.masterdata.repository.*;
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
public class HealthBlockCsvHandlerIT extends BasePaxIT {

    private HealthBlockCsvUploadHandler healthBlockCsvUploadHandler;

    List<Long> createdIds = new ArrayList<Long>();

    @Inject
    private StateRecordsDataService stateRecordsDataService;

    @Inject
    private DistrictRecordsDataService districtRecordsDataService;

    @Inject
    private TalukaRecordsDataService talukaRecordsDataService;

    @Inject
    private HealthBlockCsvRecordsDataService healthBlockCsvRecordsDataService;

    @Inject
    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Before
    public void setUp() {
        healthBlockCsvUploadHandler = new HealthBlockCsvUploadHandler(stateRecordsDataService,
                districtRecordsDataService, talukaRecordsDataService,healthBlockCsvRecordsDataService,
                healthBlockRecordsDataService, bulkUploadErrLogService);
    }
    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(healthBlockCsvRecordsDataService);
        assertNotNull(healthBlockRecordsDataService);
        assertNotNull(talukaRecordsDataService);
        assertNotNull(talukaRecordsDataService);
        assertNotNull(districtRecordsDataService);
        assertNotNull(stateRecordsDataService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(healthBlockCsvUploadHandler);
    }

    @Test
    public void testHealthBlockCsvHandler() {

        State stateData = TestHelper.getStateData();
        District districtData = TestHelper.getDistrictData();
        Taluka talukaData = TestHelper.getTalukaData();

        stateData.getDistrict().add(districtData);
        districtData.getTaluka().add(talukaData);

        stateRecordsDataService.create(stateData);

        HealthBlockCsv csvData = TestHelper.getHealthBlockCsvData();
        HealthBlockCsv invalidCsvData = TestHelper.getInvalidHealthBlockCsvData();
        createHealthBlockCsvData(csvData);
        createHealthBlockCsvData(invalidCsvData);

        createdIds.add(csvData.getId());
        createdIds.add(invalidCsvData.getId());
        createdIds.add(csvData.getId()+1);

        healthBlockCsvUploadHandler.healthBlockCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.HEALTH_BLOCK_CSV_SUCCESS));
        HealthBlock healthBlockeData = healthBlockRecordsDataService.findHealthBlockByParentCode(123L, 456L, 8L, 1002L);

        assertNotNull(healthBlockeData);
        assertTrue(123L == healthBlockeData.getStateCode());
        assertTrue(456L == healthBlockeData.getDistrictCode());
        assertTrue(8L ==  healthBlockeData.getTalukaCode());
        assertTrue(1002L == healthBlockeData.getHealthBlockCode());
        assertTrue("Gangiri".equals(healthBlockeData.getName()));

        csvData = TestHelper.getUpdateHealthBlockCsvData();
        createHealthBlockCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        healthBlockCsvUploadHandler.healthBlockCsvSuccess(TestHelper.createMotechEvent(createdIds, MasterDataConstants.HEALTH_BLOCK_CSV_SUCCESS));
        HealthBlock healthBlockUpdateData = healthBlockRecordsDataService.findHealthBlockByParentCode(123L, 456L, 8L, 1002L);

        assertNotNull(healthBlockUpdateData);
        assertTrue(123L == healthBlockUpdateData.getStateCode());
        assertTrue(456L == healthBlockUpdateData.getDistrictCode());
        assertTrue(8L == healthBlockUpdateData.getTalukaCode());
        assertTrue(1002L == healthBlockUpdateData.getHealthBlockCode());
        assertTrue("Ganiri".equals(healthBlockUpdateData.getName()));
    }

    private void clearId() {
        createdIds.clear();
    }

    private void createHealthBlockCsvData(HealthBlockCsv csvData) {

        healthBlockCsvRecordsDataService.create(csvData);
    }
}
