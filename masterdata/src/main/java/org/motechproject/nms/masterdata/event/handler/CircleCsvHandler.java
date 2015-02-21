package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
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
        //todo datetime format
        Map<String, Object> params = motechEvent.getParameters();
        String errorFileName = params.get("entity_name_") + new Date().toString();
        try {
            processCircleCsvRecords(params, errorFileName);
        }catch (Exception ex) {
            //todo: errorDetail for this error. can we replace it with a common helper method
            errorDetail.setErrorCategory("");
            errorDetail.setErrorDescription(ex.getMessage());
            errorDetail.setRecordDetails("");
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.failure")
    public void circleCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        String errorFileName = params.get("entity_name_") + new Date().toString();
        try {
            int createdCount = (int)params.get("csv-import.created_count");
            int updatedCount = (int)params.get("csv-import.updated_count");
            String csvImportFileName = (String)params.get("csv-import.filename");

            circleCsvService.deleteAll();
            summary.setSuccessCount(0);;
            summary.setFailureCount(createdCount + updatedCount);
            //todo : username
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", csvImportFileName, errorFileName, summary);
        }catch (Exception ex) {
            //todo: errorDetail for this error. can we replace it with a common helper method
            errorDetail.setErrorCategory("");
            errorDetail.setErrorDescription(ex.getMessage());
            errorDetail.setRecordDetails("");
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        }
    }

    private void processCircleCsvRecords(Map<String, Object> params, String errorFileName) {
        String csvImportFileName = (String)params.get("csv-import.filename");
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        int successCount = 0;
        int failureCount = 0;
        for(Long id : updatedIds) {
            CircleCsv record = circleCsvService.findById(id);
            if (record != null) {
                Circle newRecord = mapCircleFrom(record, errorFileName);
                circleService.update(newRecord);
                circleCsvService.delete(record);
                ++successCount;
            } else {
                failureCount++;
                logErrorRecord(errorFileName);
            }
        }

        for(Long id : createdIds) {
            CircleCsv record = circleCsvService.findById(id);

            if (record != null) {
                Circle newRecord = mapCircleFrom(record, errorFileName);
                circleService.create(newRecord);
                circleCsvService.delete(record);
                successCount++;
            } else {
                failureCount++;
                logErrorRecord(errorFileName);
            }
        }
        summary.setFailureCount(failureCount);
        summary.setSuccessCount(successCount);
        //todo : username
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("", csvImportFileName, errorFileName, summary);
    }

    private void logErrorRecord(String errorFileName) {
        //todo: errorDetail for this error. can we replace it with a common helper method
        errorDetail.setErrorDescription("Record not found in the database");
        errorDetail.setErrorCategory("");
        errorDetail.setRecordDetails("Record is null");
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
    }

    private Circle mapCircleFrom(CircleCsv record, String errorFileName) {
        Circle newRecord = new Circle();
        try {
            newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
            newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        }catch (DataValidationException ex) {
            errorDetail.setErrorCategory(ex.getErrorCode());
            errorDetail.setRecordDetails(record.toString());
            errorDetail.setErrorDescription(String.format(
                    ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        }
        return newRecord;
    }
}
