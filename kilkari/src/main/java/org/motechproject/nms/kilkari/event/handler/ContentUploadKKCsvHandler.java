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

        ContentUploadKKCsv record = null;
        ContentUploadKK persistentRecord = null;
        String userName = null;

        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary();

        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        String csvImportFileName = (String)params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);


        for(Long id : createdIds) {
            try {
                record = contentUploadKKCsvService.getRecord(id);

                if (record != null) {
                    ContentUploadKK newRecord = mapContentUploadKKFrom(record);
                    userName = record.getOwner();

                    persistentRecord = contentUploadKKService.getRecordByContentId(newRecord.getContentId());
                    if (persistentRecord != null) {
                        newRecord.setId(persistentRecord.getId());
                        newRecord.setModifiedBy(userName);
                        contentUploadKKService.update(newRecord);
                    }else {
                        newRecord.setOwner(userName);
                        newRecord.setModifiedBy(userName);
                        contentUploadKKService.create(newRecord);
                    }

                    contentUploadKKCsvService.delete(record);
                    summary.incrementSuccessCount();
                } else {
                    errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetail.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                    summary.incrementFailureCount();
                }
            }catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(ex.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                summary.incrementFailureCount();
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvImportFileName, errorFileName, summary);
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.failure")
    public void ContentUploadKKCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

        for(Long id : createdIds) {
            contentUploadKKCsvService.delete(contentUploadKKCsvService.getRecord(id));
        }

    }

    private ContentUploadKK mapContentUploadKKFrom(ContentUploadKKCsv record) throws DataValidationException {
        ContentUploadKK newRecord = new ContentUploadKK();

        newRecord.setCircleCode(ParseDataHelper.parseString("circleCode", record.getCircleCode(), true));
        newRecord.setContentDuration(ParseDataHelper.parseInt("contentDuration", record.getContentDuration(), true));
        newRecord.setContentFile(ParseDataHelper.parseString("contentFile", record.getContentFile(), true));
        newRecord.setContentId(ParseDataHelper.parseLong("contentId", record.getContentId(), true));
        newRecord.setContentName(ParseDataHelper.parseString("contentName", record.getContentName(), true));

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
