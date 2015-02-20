package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.ContentType;
import org.motechproject.nms.kilkari.domain.ContentUploadKK;
import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;
import org.motechproject.nms.kilkari.service.ContentUploadKKCsvService;
import org.motechproject.nms.kilkari.service.ContentUploadKKService;
import org.motechproject.nms.util.BulkUploadErrRecordDetails;
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

    private static Integer successCount = 0;
    private static Integer failCount = 0;

    BulkUploadErrRecordDetails errorDetail = new BulkUploadErrRecordDetails();

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private ContentUploadKKService contentUploadKKService;

    @Autowired
    private ContentUploadKKCsvService contentUploadKKCsvService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.success")
    public void ContentUploadKKCsvSuccess(MotechEvent motechEvent) {
        String errorFileName = "ContentUploadKKCsv_" + new Date().toString();

        try {
            Map<String, Object> params = motechEvent.getParameters();;
            processContentUploadKKCsvRecords(params, errorFileName);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, successCount, failCount);
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.failure")
    public void ContentUploadKKCsvFailure(MotechEvent motechEvent) {
        String errorFileName = "ContentUploadKKCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            contentUploadKKCsvService.deleteAll();
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, 0, createdIds.size());
        }catch (Exception ex) {
        }
    }

    private void processContentUploadKKCsvRecords(Map<String, Object> params, String errorFileName) {
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

        for(Long id : updatedIds) {
            ContentUploadKKCsv record = contentUploadKKCsvService.findById(id);
            if (record != null) {
                ContentUploadKK newRecord = mapContentUploadKKFrom(record);
                contentUploadKKService.update(newRecord);
                contentUploadKKCsvService.delete(record);
                successCount++;
            } else {
                logErrorRecord(errorFileName, record.getIndex());
            }
        }

        for(Long id : createdIds) {
            ContentUploadKKCsv record = contentUploadKKCsvService.findById(id);

            if (record != null) {
                ContentUploadKK newRecord = mapContentUploadKKFrom(record);
                contentUploadKKService.create(newRecord);
                contentUploadKKCsvService.delete(record);
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

    private static ContentUploadKK mapContentUploadKKFrom(ContentUploadKKCsv record) {
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


        }
        return newRecord;
    }
}
