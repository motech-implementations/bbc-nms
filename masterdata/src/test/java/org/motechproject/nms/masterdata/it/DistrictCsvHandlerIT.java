package org.motechproject.nms.masterdata.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.DistrictCsv;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.event.handler.DistrictCsvUploadHandler;
import org.motechproject.nms.masterdata.service.DistrictCsvService;
import org.motechproject.nms.masterdata.service.DistrictService;
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
    private StateService stateService;

    @Inject
    private DistrictService districtService;

    @Inject
    private DistrictCsvService districtCsvService;

    List<Long> createdIds = new ArrayList<Long>();

    @Before
    public void setUp() {
        districtCsvUploadHandler = new DistrictCsvUploadHandler(districtCsvService,
                districtService, stateService, bulkUploadErrLogService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(districtCsvService);
        assertNotNull(districtService);
        assertNotNull(stateService);
        assertNotNull(bulkUploadErrLogService);
    }

    @Test
    public void testDistrictCsvHandler() {

        State stateData = TestHelper.getStateData();
        stateService.create(stateData);

        DistrictCsv csvData = TestHelper.getDistrictCsvData();
        DistrictCsv invalidCsvData = TestHelper.getInvalidDistrictCsvData();

        createDistrictCsvData(csvData);
        createDistrictCsvData(invalidCsvData);
        createdIds.add(csvData.getId());
        createdIds.add(csvData.getId() + 1);
        createdIds.add(invalidCsvData.getId());

        districtCsvUploadHandler.districtCsvSuccess(TestHelper.createMotechEvent(createdIds, LocationConstants.DISTRICT_CSV_SUCCESS));
        District districtData = districtService.findDistrictByParentCode(456L, 123L);

        assertNotNull(districtData);
        assertTrue(123L == districtData.getStateCode());
        assertTrue(456L == districtData.getDistrictCode());
        assertTrue("Agra".equals(districtData.getName()));

        csvData = TestHelper.getUpdatedDistrictCsvData();
        createDistrictCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        districtCsvUploadHandler.districtCsvSuccess(TestHelper.createMotechEvent(createdIds, LocationConstants.DISTRICT_CSV_SUCCESS));
        District districtUpdateData = districtService.findDistrictByParentCode(456L, 123L);

        assertNotNull(districtUpdateData);
        assertTrue(123L == districtUpdateData.getStateCode());
        assertTrue(456L == districtUpdateData.getDistrictCode());
        assertTrue("Aligarh".equals(districtUpdateData.getName()));
    }

    private void clearId() {
        createdIds.clear();
    }

    private void createDistrictCsvData(DistrictCsv csvData) {

        districtCsvService.create(csvData);
    }
}
