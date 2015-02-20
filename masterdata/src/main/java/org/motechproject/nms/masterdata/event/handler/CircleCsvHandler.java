package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorCodeConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CircleCsvHandler {

    BulkUploadError errorDetail = new BulkUploadError();

    CsvProcessingSummary summary = new CsvProcessingSummary(0,0);

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
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", errorFileName, summary);
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.failure")
    public void circleCsvFailure(MotechEvent motechEvent) {
        String errorFileName = "CircleCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
            List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");

            circleCsvService.deleteAll();
            summary.setSuccessCount(0);;
            summary.setFailureCount(createdIds.size() + updatedIds.size());
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", errorFileName, summary);
        }catch (Exception ex) {
        }
    }

    private void processCircleCsvRecords(Map<String, Object> params, String errorFileName) {
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        int successCount = 0;
        int failureCount = 0;
        for(Long id : updatedIds) {
            CircleCsv record = circleCsvService.findById(id);
            if (record != null) {
                Circle newRecord = mapCircleFrom(record);
                circleService.update(newRecord);
                circleCsvService.delete(record);
                ++successCount;
            } else {
                failureCount++;
                logErrorRecord(errorFileName, record.toString());
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
                failureCount++;
                logErrorRecord(errorFileName, record.toString());
            }
        }
        summary.setFailureCount(failureCount);
        summary.setSuccessCount(successCount);
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("", errorFileName, summary);
    }

    private void logErrorRecord(String errorFileName, String recordStr) {
        errorDetail.setErrorDescription("");
        errorDetail.setErrorCategory("");
        errorDetail.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_ERROR_DETAIL.format(recordStr));
        errorDetail.setUserName("");
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
    }

    private Circle mapCircleFrom(CircleCsv record) {
        Circle newRecord = new Circle();
        try {
            newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
            newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        }catch (DataValidationException ex) {
            errorDetail.setErrorCategory(ex.getErrorCode());
            errorDetail.setRecordDetails(record.toString());
            errorDetail.setErrorDescription(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION.format(ex.getErroneousField()));
        }
        return newRecord;
    }
}
