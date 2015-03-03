package org.motechproject.nms.masterdata.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.StateCsv;
import org.motechproject.nms.masterdata.event.handler.StateCsvUploadHandler;
import org.motechproject.nms.masterdata.repository.StateCsvRecordsDataService;
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

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by abhishek on 3/3/15.
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class StateCsvHandlerIT extends BasePaxIT {

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private StateCsvRecordsDataService stateCsvRecordsService;

    @Inject
    private StateRecordsDataService stateRecordsDataService;

    private StateCsvUploadHandler stateCsvUploadHandler;

    @Before
    public void setUp() {
       stateCsvUploadHandler = new StateCsvUploadHandler(stateRecordsDataService,
               stateCsvRecordsService,bulkUploadErrLogService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(stateCsvRecordsService);
        assertNotNull(stateRecordsDataService);
        assertNotNull(bulkUploadErrLogService);
    }

    @Test
    public void StateCsvSuccess() {

        StateCsv csvData = TestHelper.getStateCsvData();
        stateCsvRecordsService.create(csvData);

        List<Long> createdIds = new ArrayList<Long>();
        createdIds.add(csvData.getId());

        stateCsvUploadHandler.stateCsvSuccess(TestHelper.createMotechEvent(createdIds,MasterDataConstants.STATE_CSV_SUCCESS));

        State stateData = stateRecordsDataService.findRecordByStateCode(123L);

        assertNotNull(stateData);
        assertTrue(123L == stateData.getStateCode());
        assertTrue("MotechEventCreateTest".equals(stateData.getName()));
    }
}
