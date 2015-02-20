package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.util.BulkUploadErrRecordDetails;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CircleCsvHandler {

    private static Integer successCount = 0;
    private static Integer failCount = 0;

    BulkUploadErrRecordDetails errorDetail = new BulkUploadErrRecordDetails();

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private CircleCsvService circleCsvService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.success")
    public void circleCsvSuccess(MotechEvent motechEvent) {
        String errorFileName = "CircleCsv_" + new Date().toString();

        try {
            Map<String, Object> params = motechEvent.getParameters();;
            processCircleCsvRecords(params, errorFileName);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, successCount, failCount);
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.failure")
    public void circleCsvFailure(MotechEvent motechEvent) {
        String errorFileName = "CircleCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            circleCsvService.deleteAll();
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, 0, createdIds.size());
        }catch (Exception ex) {
        }
    }

    private void processCircleCsvRecords(Map<String, Object> params, String errorFileName) {
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

        for(Long id : updatedIds) {
            CircleCsv record = circleCsvService.findById(id);
            if (record != null) {
                Circle newRecord = mapCircleFrom(record);
                circleService.update(newRecord);
                circleCsvService.delete(record);
                successCount++;
            } else {
                logErrorRecord(errorFileName, record.getIndex());
            }
        }

        for(Long id : createdIds) {
            CircleCsv record = circleCsvService.findById(id);

            if (record != null) {
                Circle newRecord = mapCircleFrom(record);
                circleService.create(newRecord);
                circleCsvService.delete(record);
                successCount++;
            } else {
                logErrorRecord(errorFileName, record.getIndex());
            }
        }
    }

    private void logErrorRecord(String errorFileName, Long index) {
        errorDetail.setErrorCode("");
        errorDetail.setErrorDescription("");
        errorDetail.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_ERROR_DETAIL.format(index.toString()));
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        failCount++;
    }

    private static Circle mapCircleFrom(CircleCsv record) {
        Circle newRecord = new Circle();
        try {
            newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
            newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        }catch (DataValidationException ex) {


        }
        return newRecord;
    }
}
