package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.ContentType;
import org.motechproject.nms.kilkari.domain.ContentUploadKK;
import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;
import org.motechproject.nms.kilkari.service.ContentUploadKKCsvService;
import org.motechproject.nms.kilkari.service.ContentUploadKKService;
import org.motechproject.nms.masterdata.domain.OperationType;
import org.motechproject.nms.masterdata.event.handler.DistrictCsvUploadHandler;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class handles the csv upload for success and failure events for ContentUploadKKCsv.
 */
public class ContentUploadKKCsvHandler {

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private ContentUploadKKService contentUploadKKService;

    @Autowired
    private ContentUploadKKCsvService contentUploadKKCsvService;

    private static Logger logger = LoggerFactory.getLogger(DistrictCsvUploadHandler.class);

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in ContentUploadKK table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
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
                        if (OperationType.DEL.toString().equals(record.getOperation())) {
                            contentUploadKKService.delete(persistentRecord);
                            logger.info(String.format("Record deleted successfully for contentid : %s", newRecord.getContentId()));
                        } else {
                            newRecord.setId(persistentRecord.getId());
                            newRecord.setModifiedBy(userName);
                            contentUploadKKService.update(newRecord);
                            logger.info(String.format("Record updated successfully for contentid : %s", newRecord.getContentId()));
                        }
                    }else {
                        newRecord.setOwner(userName);
                        newRecord.setModifiedBy(userName);
                        contentUploadKKService.create(newRecord);
                        logger.info(String.format("Record created successfully for contentid : %s", newRecord.getContentId()));
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

    /**
     * This method handle the event which is raised after csv upload is failed.
     * This method also deletes all the csv records which get inserted in this upload..
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = "mds.crud.masterdatamodule.ContentUploadKKCsv.csv-import.failure")
    public void ContentUploadKKCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

        for(Long id : createdIds) {
            ContentUploadKKCsv oldRecord = contentUploadKKCsvService.getRecord(id);
            if (oldRecord != null) {
                contentUploadKKCsvService.delete(oldRecord);
                logger.info(String.format("Record deleted successfully for id %s", id.toString()));
            }
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
