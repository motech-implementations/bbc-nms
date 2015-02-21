package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.ContentType;
import org.motechproject.nms.kilkari.domain.ContentUploadKK;
import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;
import org.motechproject.nms.kilkari.service.ContentUploadKKCsvService;
import org.motechproject.nms.kilkari.service.ContentUploadKKService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContentUploadKKCsvHandler {
    BulkUploadError errorDetail = new BulkUploadError();

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private ContentUploadKKService contentUploadKKService;

    @Autowired
    private ContentUploadKKCsvService contentUploadKKCsvService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.success")
    public void ContentUploadKKCsvSuccess(MotechEvent motechEvent) {
        //todo datetime format
        Map<String, Object> params = motechEvent.getParameters();
        String errorFileName = params.get("entity_name_") + new Date().toString();
        try {
            processContentUploadKKCsvRecords(params, errorFileName);
        }catch (Exception ex) {
            //todo: errorDetail for this error. can we replace it with a common helper method
            errorDetail.setErrorCategory("");
            errorDetail.setErrorDescription(ex.getMessage());
            errorDetail.setRecordDetails("");
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.failure")
    public void ContentUploadKKCsvFailure(MotechEvent motechEvent) {
        CsvProcessingSummary summary = new CsvProcessingSummary(0,0);
        Map<String, Object> params = motechEvent.getParameters();
        String errorFileName = params.get("entity_name_") + new Date().toString();
        try {
            int createdCount = (int)params.get("csv-import.created_count");
            int updatedCount = (int)params.get("csv-import.updated_count");
            String csvImportFileName = (String)params.get("csv-import.filename");

            contentUploadKKCsvService.deleteAll();
            summary.setFailureCount(createdCount + updatedCount);
            summary.setSuccessCount(0);
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

    private void processContentUploadKKCsvRecords(Map<String, Object> params, String errorFileName) {
    	CsvProcessingSummary summary = new CsvProcessingSummary(0,0);
        String csvImportFileName = (String)params.get("csv-import.filename");
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        int  successCount = 0;
        int failureCount = 0;
        for(Long id : updatedIds) {
            ContentUploadKKCsv record = contentUploadKKCsvService.findById(id);
            if (record != null) {
                ContentUploadKK newRecord = mapContentUploadKKFrom(record, errorFileName);
                contentUploadKKService.update(newRecord);
                contentUploadKKCsvService.delete(record);

                ++successCount;
            } else {
                failureCount++;
                logErrorRecord(errorFileName);
            }
        }

        for(Long id : createdIds) {
            ContentUploadKKCsv record = contentUploadKKCsvService.findById(id);

            if (record != null) {
                ContentUploadKK newRecord = mapContentUploadKKFrom(record, errorFileName);
                contentUploadKKService.create(newRecord);
                contentUploadKKCsvService.delete(record);
                successCount++;
            } else {
                failureCount++;
                logErrorRecord(errorFileName);
            }
        }
        summary.setSuccessCount(successCount);
        summary.setFailureCount(failureCount);
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

    private ContentUploadKK mapContentUploadKKFrom(ContentUploadKKCsv record, String errorFile) {
        ContentUploadKK newRecord = new ContentUploadKK();
        try {
            newRecord.setCircleCode(ParseDataHelper.parseString("", record.getCircleCode(), true));
            newRecord.setContentDuration(ParseDataHelper.parseInt("", record.getContentDuration(), true));
            newRecord.setContentFile(ParseDataHelper.parseString("", record.getContentFile(), true));
            newRecord.setContentId(ParseDataHelper.parseInt("", record.getContentId(), true));
            newRecord.setContentName(ParseDataHelper.parseString("", record.getContentName(), true));

            String contentType = ParseDataHelper.parseString("", record.getContentType(), true);
            if (contentType.equals(ContentType.CONTENT.toString())) {
                newRecord.setContentType(ContentType.CONTENT);
            } else {
                newRecord.setContentType(ContentType.PROMPT);
            }

            newRecord.setLanguageLocationCode(ParseDataHelper.parseInt("", record.getLanguageLocationCode(), true));
        }catch (DataValidationException ex) {
            errorDetail.setErrorCategory(ex.getErrorCode());
            errorDetail.setRecordDetails(record.toString());
            errorDetail.setErrorDescription(String.format(
                    ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFile, errorDetail);

        }
        return newRecord;
    }
}
