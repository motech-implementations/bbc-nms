package org.motechproject.nms.masterdata.it;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.OperatorCsv;
import org.motechproject.nms.masterdata.event.handler.OperatorCsvHandler;
import org.motechproject.nms.masterdata.repository.OperatorCsvDataService;
import org.motechproject.nms.masterdata.repository.OperatorDataService;
import org.motechproject.nms.masterdata.service.OperatorCsvService;
import org.motechproject.nms.masterdata.service.OperatorService;
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
public class OperatorCsvHandlerIT extends BasePaxIT {

    @Inject
    private OperatorService operatorService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private OperatorCsvService operatorCsvService;

    @Inject
    private OperatorCsvDataService operatorCsvDataService;

    @Inject
    private OperatorDataService operatorDataService;

    List<Long> createdIds = new ArrayList<Long>();

    @Test
    public void shouldCreateCircleRecordsAfterCsvUpload() throws Exception {
        OperatorCsv csv = new OperatorCsv();
        csv.setName("MotechEventCreateTest");
        csv.setCode("12345");
        csv.setOperation("ADD");
        OperatorCsv dbCsv = operatorCsvDataService.create(csv);
        createdIds.add(dbCsv.getId());

        OperatorCsvHandler operatorCsvHandler = new OperatorCsvHandler(operatorService, operatorCsvService,bulkUploadErrLogService);
        operatorCsvHandler.operatorCsvSuccess(createMotechEvent(createdIds));
        Assert.assertNull(operatorCsvService.getRecord(createdIds.get(0)));
        Operator record = operatorService.getRecordByCode("12345");
        Assert.assertEquals(record.getName(), "MotechEventCreateTest");
    }

    @Test
    public void shouldDeleteOperatorRecordsAfterCsvUpload() throws Exception {
        OperatorCsv csv = new OperatorCsv();
        csv.setName("MotechEventCreateTest");
        csv.setCode("12345");
        csv.setOperation("ADD");
        OperatorCsv dbCsv = operatorCsvDataService.create(csv);
        createdIds.add(dbCsv.getId());

        OperatorCsv csv2 = new OperatorCsv();
        csv2.setName("MotechEventCreateTest");
        csv2.setCode("12345");
        csv2.setOperation("DEL");
        dbCsv = operatorCsvDataService.create(csv2);
        createdIds.add(dbCsv.getId());

        OperatorCsvHandler operatorCsvHandler = new OperatorCsvHandler(operatorService, operatorCsvService,bulkUploadErrLogService);
        operatorCsvHandler.operatorCsvSuccess(createMotechEvent(createdIds));
        Assert.assertNull(operatorCsvService.getRecord(createdIds.get(0)));
        Assert.assertNull(operatorCsvService.getRecord(createdIds.get(1)));
        Assert.assertNull(operatorService.getRecordByCode("12345"));
    }

    @Test
    public void shouldUpdateOperatorRecordsAfterCsvUpload() throws Exception {
        OperatorCsv csv = new OperatorCsv();
        csv.setName("MotechEventCreateTest");
        csv.setCode("12345");
        csv.setOperation("ADD");
        OperatorCsv dbCsv = operatorCsvDataService.create(csv);
        createdIds.add(dbCsv.getId());
        OperatorCsv csv2 = new OperatorCsv();
        csv2.setName("MotechEventChanged");
        csv2.setCode("12345");
        dbCsv = operatorCsvDataService.create(csv2);
        createdIds.add(dbCsv.getId());

        OperatorCsvHandler operatorCsvHandler = new OperatorCsvHandler(operatorService, operatorCsvService,bulkUploadErrLogService);
        operatorCsvHandler.operatorCsvSuccess(createMotechEvent(createdIds));
        Assert.assertNull(operatorCsvService.getRecord(createdIds.get(0)));
        Assert.assertNull(operatorCsvService.getRecord(createdIds.get(1)));
        Operator record = operatorService.getRecordByCode("12345");
        Assert.assertEquals(record.getName(), "MotechEventChanged");
    }

    public MotechEvent createMotechEvent(List<Long> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "operator");
        return new MotechEvent(MasterDataConstants.OPERATOR_CSV_SUCCESS, params);
    }

    @After
    public void tearDown() {
        for(Long id : createdIds) {
            Operator operator = operatorDataService.findById(id);
            if(operator !=null) {
                operatorService.delete(operator);
            }
        }
        createdIds.clear();
    }
}