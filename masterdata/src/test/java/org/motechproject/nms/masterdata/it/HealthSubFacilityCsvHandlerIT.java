package org.motechproject.nms.masterdata.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.event.handler.HealthSubFacilityCsvUploadHandler;
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
 * Created by abhishek on 11/3/15.
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HealthSubFacilityCsvHandlerIT extends BasePaxIT {

    private HealthSubFacilityCsvUploadHandler healthSubFacilityCsvHandler;

    List<Long> createdIds = new ArrayList<Long>();

    @Inject
    private StateService stateService;

    @Inject
    private DistrictService districtService;

    @Inject
    private TalukaService talukaService;

    @Inject
    private HealthFacilityService healthFacilityService;

    @Inject
    private HealthSubFacilityCsvService healthSubFacilityCsvService;

    @Inject
    private HealthSubFacilityService healthSubFacilityService;

    @Inject
    private HealthBlockService healthBlockService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Before
    public void setUp() {
        healthSubFacilityCsvHandler = new HealthSubFacilityCsvUploadHandler(stateService,
                districtService, talukaService, healthFacilityService, healthSubFacilityCsvService,
                healthSubFacilityService, healthBlockService, bulkUploadErrLogService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(healthFacilityService);
        assertNotNull(healthSubFacilityCsvService);
        assertNotNull(healthSubFacilityService);
        assertNotNull(talukaService);
        assertNotNull(districtService);
        assertNotNull(stateService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(healthSubFacilityCsvHandler);
    }

    @Test
    public void testHealthSubFacilityCsvHandler() {

        State stateData = TestHelper.getStateData();
        District districtData = TestHelper.getDistrictData();
        Taluka talukaData = TestHelper.getTalukaData();
        HealthBlock healthBlockData = TestHelper.getHealthBlockData();
        HealthFacility healthFacilityData = TestHelper.getHealthFacilityData();

        stateData.getDistrict().add(districtData);
        districtData.getTaluka().add(talukaData);
        talukaData.getHealthBlock().add(healthBlockData);
        healthBlockData.getHealthFacility().add(healthFacilityData);

        stateService.create(stateData);

        HealthSubFacilityCsv csvData = TestHelper.getHealthSubFacilityCsvData();
        HealthSubFacilityCsv invalidCsvData = TestHelper.getInvalidHealthSubFacilityCsvData();
        createHealthSubFacilityCsvData(csvData);
        createHealthSubFacilityCsvData(invalidCsvData);

        createdIds.add(csvData.getId());
        createdIds.add(invalidCsvData.getId());
        createdIds.add(csvData.getId() + 1);

        healthSubFacilityCsvHandler.healthSubFacilityCsvSuccess(TestHelper.createMotechEvent(createdIds, LocationConstants.HEALTH_SUB_FACILITY_CSV_SUCCESS));
        HealthSubFacility healthSubFacilityData = healthSubFacilityService.findHealthSubFacilityByParentCode(123L, 456L, 8L, 1002L, 1111L, 9001L);

        assertNotNull(healthSubFacilityData);
        assertTrue(123L == healthSubFacilityData.getStateCode());
        assertTrue(456L == healthSubFacilityData.getDistrictCode());
        assertTrue(8L == healthSubFacilityData.getTalukaCode());
        assertTrue(1002L == healthSubFacilityData.getHealthBlockCode());
        assertTrue(1111L == healthSubFacilityData.getHealthFacilityCode());
        assertTrue(9001L == healthSubFacilityData.getHealthSubFacilityCode());
        assertTrue("HSF1".equals(healthSubFacilityData.getName()));

        csvData = TestHelper.getUpdateHealthSubFacilityCsvData();
        createHealthSubFacilityCsvData(csvData);

        clearId();
        createdIds.add(csvData.getId());

        healthSubFacilityCsvHandler.healthSubFacilityCsvSuccess(TestHelper.createMotechEvent(createdIds, LocationConstants.HEALTH_SUB_FACILITY_CSV_SUCCESS));
        HealthSubFacility healthSubFacilityUpdateData = healthSubFacilityService.findHealthSubFacilityByParentCode(123L, 456L, 8L, 1002L, 1111L, 9001L);

        assertNotNull(healthSubFacilityUpdateData);
        assertTrue(123L == healthSubFacilityUpdateData.getStateCode());
        assertTrue(456L == healthSubFacilityUpdateData.getDistrictCode());
        assertTrue(8L == healthSubFacilityUpdateData.getTalukaCode());
        assertTrue(1002L == healthSubFacilityUpdateData.getHealthBlockCode());
        assertTrue(1111L == healthSubFacilityUpdateData.getHealthFacilityCode());
        assertTrue(9001L == healthSubFacilityUpdateData.getHealthSubFacilityCode());
        assertTrue("HSF2".equals(healthSubFacilityUpdateData.getName()));
    }

    private void clearId() {
        createdIds.clear();
    }

    private void createHealthSubFacilityCsvData(HealthSubFacilityCsv csvData) {
        healthSubFacilityCsvService.create(csvData);
    }
}
