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
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
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


    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private ContentUploadKKService contentUploadKKService;

    @Autowired
    private ContentUploadKKCsvService contentUploadKKCsvService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.success")
    public void ContentUploadKKCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        String errorFileName = params.get("entity_name_") + new Date().toString();
        processContentUploadKKCsvRecords(params, errorFileName);
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.failure")
    public void ContentUploadKKCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");

        for(Long id : createdIds) {
            contentUploadKKCsvService.delete(contentUploadKKCsvService.findById(id));
        }

        for(Long id : updatedIds) {
            contentUploadKKCsvService.delete(contentUploadKKCsvService.findById(id));
        }
    }

    private void processContentUploadKKCsvRecords(Map<String, Object> params, String errorFileName) {
        BulkUploadError errorDetail = new BulkUploadError();
    	CsvProcessingSummary summary = new CsvProcessingSummary(0,0);
        String csvImportFileName = (String)params.get("csv-import.filename");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        int  successCount = 0;
        int failureCount = 0;
        ContentUploadKKCsv record = null;
        String userName = null;

        for(Long id : createdIds) {
            try {
                record = contentUploadKKCsvService.findById(id);

                if (record != null) {
                    ContentUploadKK newRecord = mapContentUploadKKFrom(record);
                    userName = record.getOwner();
                    contentUploadKKService.create(newRecord);
                    contentUploadKKCsvService.delete(record);
                    successCount++;
                } else {
                    failureCount++;
                    errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetail.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                }
            }catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(String.format(
                        ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
            }
        }
        summary.setSuccessCount(successCount);
        summary.setFailureCount(failureCount);
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvImportFileName, errorFileName, summary);
    }

        private ContentUploadKK mapContentUploadKKFrom(ContentUploadKKCsv record) throws DataValidationException {
        ContentUploadKK newRecord = new ContentUploadKK();
        newRecord.setCircleCode(ParseDataHelper.parseString("CircleCode", record.getCircleCode(), true));
        newRecord.setContentDuration(ParseDataHelper.parseInt("ContentDuration", record.getContentDuration(), true));
        newRecord.setContentFile(ParseDataHelper.parseString("ContentFile", record.getContentFile(), true));
        newRecord.setContentId(ParseDataHelper.parseInt("ContentId", record.getContentId(), true));
        newRecord.setContentName(ParseDataHelper.parseString("ContentName", record.getContentName(), true));

        String contentType = ParseDataHelper.parseString("contentType", record.getContentType(), true);
        if (contentType.equals(ContentType.CONTENT.toString())) {
            newRecord.setContentType(ContentType.CONTENT);
        } else {
            newRecord.setContentType(ContentType.PROMPT);
        }
        newRecord.setLanguageLocationCode(ParseDataHelper.parseInt("LanguageLocationCode", record.getLanguageLocationCode(), true));
        return newRecord;
    }
}
