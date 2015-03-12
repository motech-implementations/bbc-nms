package org.motechproject.nms.masterdata.ut.event.handler;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.event.handler.CircleCsvHandler;
import org.motechproject.nms.masterdata.repository.CircleCsvDataService;
import org.motechproject.nms.masterdata.repository.CircleDataService;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.service.BulkUploadErrLogService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.MockitoAnnotations.initMocks;

public class CircleCsvHandlerTest extends TestCase {
    @Mock
    private CircleService circleService;

    @Mock
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Mock
    private CircleCsvService circleCsvService;

    @Mock
    private CircleCsvDataService circleCsvDataService;

    @Mock
    private CircleDataService circleDataService;

    private CircleCsvHandler handler;

    List<Long> createdIds = new ArrayList<Long>();

    @Before
    public void init() {
        initMocks(this);
        this.handler = new CircleCsvHandler(bulkUploadErrLogService, circleService, circleCsvService);
    }

    @Test
    public void testDataValidationExceptionShouldRaised() {
        init();
        Circle circle = new Circle();
        createdIds.add(1L);

        CircleCsv csv = new CircleCsv();
        csv.setName("@#$");
        csv.setCode("code");

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapCircleFrom", CircleCsv.class);
            method.setAccessible(true);
            circle = (Circle) method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }

    }

    public CircleCsv createCircleCsvRecord(String name, String code) {
        CircleCsv csv = new CircleCsv();
        csv.setName(name);
        csv.setCode(code);
        return circleCsvDataService.create(csv);
    }

    public MotechEvent createMotechEvent(List<Long> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "circle");
        return new MotechEvent(MasterDataConstants.CIRCLE_CSV_SUCCESS, params);
    }

    public void preSetup() {
        CircleCsv csv = new CircleCsv();
        csv.setName("MotechEventCreateTest");
        csv.setCode("12345");
        CircleCsv dbCsv = circleCsvDataService.create(csv);
        createdIds.add(dbCsv.getId());
    }

    @After
    public void tearDown() {
        createdIds.clear();
    }
}
