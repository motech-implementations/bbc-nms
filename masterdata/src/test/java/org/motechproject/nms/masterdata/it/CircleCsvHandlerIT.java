package org.motechproject.nms.masterdata.it;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.event.handler.CircleCsvHandler;
import org.motechproject.nms.masterdata.repository.CircleCsvDataService;
import org.motechproject.nms.masterdata.repository.CircleDataService;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CircleCsvHandlerIT extends BasePaxIT {

    @Inject
    private CircleService circleService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private CircleCsvService circleCsvService;

    @Inject
    private CircleCsvDataService circleCsvDataService;

    @Inject
    private CircleDataService circleDataService;

    List<Long> createdIds = new ArrayList<Long>();

    @Test
    public void shouldCreateCircleRecordsAfterCsvUpload() throws Exception {
        preSetup();
        CircleCsvHandler circleCsvHandler = new CircleCsvHandler(bulkUploadErrLogService, circleService, circleCsvService);
        circleCsvHandler.circleCsvSuccess(createMotechEvent(createdIds));
        Assert.assertNull(circleCsvService.getRecord(createdIds.get(0)));
        Circle record = circleService.getRecordByCode("12345");
        Assert.assertEquals(record.getName(), "MotechEventCreateTest");
    }

    @Test
    public void shouldUpdateCircleRecordsAfterCsvUpload() throws Exception {
        preSetup();
        CircleCsv csv2 = new CircleCsv();
        csv2.setName("MotechEventChanged");
        csv2.setCode("12345");
        CircleCsv dbCsv = circleCsvDataService.create(csv2);
        createdIds.add(dbCsv.getId());

        CircleCsvHandler circleCsvHandler = new CircleCsvHandler(bulkUploadErrLogService, circleService, circleCsvService);
        circleCsvHandler.circleCsvSuccess(createMotechEvent(createdIds));
        Assert.assertNull(circleCsvService.getRecord(createdIds.get(0)));
        Assert.assertNull(circleCsvService.getRecord(createdIds.get(1)));
        Circle record = circleService.getRecordByCode("12345");
        Assert.assertEquals(record.getName(), "MotechEventChanged");
    }

    @Test
    public void shouldWriteErrorLogIfCsvRecordIsNotFound() throws Exception {
        createdIds.add(1L);

        CircleCsvHandler circleCsvHandler = new CircleCsvHandler(bulkUploadErrLogService, circleService, circleCsvService);
        circleCsvHandler.circleCsvSuccess(createMotechEvent(createdIds));
    }

    @Test
    public void shouldRaiseDataValidationException() throws Exception {
        CircleCsv csv = new CircleCsv();
        csv.setName("MotechEventCreateTest");
        csv.setCode("");
        csv.setOperation("ADD");
        CircleCsv dbCsv = circleCsvDataService.create(csv);
        createdIds.add(dbCsv.getId());

        CircleCsvHandler circleCsvHandler = new CircleCsvHandler(bulkUploadErrLogService, circleService, circleCsvService);
        circleCsvHandler.circleCsvSuccess(createMotechEvent(createdIds));
    }



    public void preSetup() {
        CircleCsv csv = new CircleCsv();
        csv.setName("MotechEventCreateTest");
        csv.setCode("12345");
        csv.setOperation("ADD");
        CircleCsv dbCsv = circleCsvDataService.create(csv);
        createdIds.add(dbCsv.getId());

    }


    public MotechEvent createMotechEvent(List<Long> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "circle");
        return new MotechEvent(MasterDataConstants.CIRCLE_CSV_SUCCESS, params);
    }

    @After
    public void tearDown() {
        for(Long id : createdIds) {
            Circle circle = circleDataService.findById(id);
            if(circle !=null) {
                circleService.delete(circle);
            }
        }
        createdIds.clear();
    }
}