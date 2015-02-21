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

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private CircleCsvService circleCsvService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.success")
    public void circleCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        String csvImportFileName = (String)params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);
        processCircleCsvRecords(params, errorFileName);
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.failure")
    public void circleCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();

        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");

        for(Long id : createdIds) {
            circleCsvService.delete(circleCsvService.findById(id));
        }

        for(Long id : updatedIds) {
            circleCsvService.delete(circleCsvService.findById(id));
        }

    }

    private void processCircleCsvRecords(Map<String, Object> params, String errorFileName) {
        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary(0,0);

        String csvImportFileName = (String)params.get("csv-import.filename");

        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        int successCount = 0;
        int failureCount = 0;
        CircleCsv record = null;
        String userName = null;

        for(Long id : createdIds) {
            try {
                record = circleCsvService.findById(id);

                if (record != null) {
                    userName = record.getOwner();
                    Circle newRecord = mapCircleFrom(record);
                    circleService.create(newRecord);
                    circleCsvService.delete(record);
                    successCount++;
                } else {
                    failureCount++;
                    logErrorRecord(errorFileName, errorDetail);
                }
            } catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(String.format(
                        ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
            }
        }
        summary.setFailureCount(failureCount);
        summary.setSuccessCount(successCount);
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvImportFileName, errorFileName, summary);
    }

    private void logErrorRecord(String errorFileName, BulkUploadError errorDetail) {
        //todo: errorDetail for this error. can we replace it with a common helper method
        errorDetail.setErrorDescription("Record not found in the database");
        errorDetail.setErrorCategory("");
        errorDetail.setRecordDetails("Record is null");
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
    }

    private Circle mapCircleFrom(CircleCsv record) throws DataValidationException{
        Circle newRecord = new Circle();
            newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
            newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        return newRecord;
    }
}
