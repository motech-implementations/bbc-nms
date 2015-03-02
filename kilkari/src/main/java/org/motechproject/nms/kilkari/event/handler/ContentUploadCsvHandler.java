package org.motechproject.nms.kilkari.event.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.ContentType;
import org.motechproject.nms.kilkari.domain.ContentUpload;
import org.motechproject.nms.kilkari.domain.ContentUploadCsv;
import org.motechproject.nms.kilkari.domain.Operation;
import org.motechproject.nms.kilkari.service.ContentUploadCsvService;
import org.motechproject.nms.kilkari.service.ContentUploadService;
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
import org.springframework.stereotype.Component;

/**
 * This class handles the csv upload for success and failure events for ContentUploadKKCsv.
 */
@Component
public class ContentUploadCsvHandler {

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private ContentUploadService contentUploadKKService;

    @Autowired
    private ContentUploadCsvService contentUploadKKCsvService;

    private static Logger logger = LoggerFactory.getLogger(ContentUploadCsvHandler.class);

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in ContentUploadKK table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = "mds.crud.kilkari.ContentUploadKKCsv.csv-import.success")
    public void contentUploadCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("Start processing ContentUploadKKCsv-import success for upload {}", params.toString());

        ContentUploadCsv record = null;
        ContentUpload persistentRecord = null;
        String userName = null;
        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary();
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvImportFileName = (String) params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);


        for (Long id : createdIds) {
            try {
                record = contentUploadKKCsvService.getRecord(id);

                if (record != null) {
                    ContentUpload newRecord = mapContentUploadKKFrom(record);
                    userName = record.getOwner();

                    persistentRecord = contentUploadKKService.getRecordByContentId(newRecord.getContentId());
                    if (persistentRecord != null) {
                        
                        if (Operation.DEL.toString().equals(record.getOperation())) {
                            contentUploadKKService.delete(persistentRecord);
                            logger.info("Record deleted successfully for contentid : {}", newRecord.getContentId());
                        } else {
                            persistentRecord = copyContentUploadForUpdate(newRecord, persistentRecord);
                            contentUploadKKService.update(newRecord);
                            logger.info("Record updated successfully for contentid : {}", newRecord.getContentId());
                        }
                    } else if (Operation.DEL.toString().equals(record.getOperation())) {
                        logger.error("Record for deletion not found in the Content table with code {}",
                                newRecord.getContentId());
                        ParseDataHelper.raiseInvalidDataException("Content Id", newRecord.getContentId().toString());
                    }else {
                        contentUploadKKService.create(newRecord);
                        logger.info("Record created successfully for contentid : {}", newRecord.getContentId());
                    }
                    summary.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the ContentUploadKKCsv table with id {}", id);
                    errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetail.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                    summary.incrementFailureCount();
                }
            } catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(ex.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                summary.incrementFailureCount();
            } catch (Exception e) {
                logger.error("CONTENT_UPLOAD_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                errorDetail.setErrorCategory("");
                errorDetail.setRecordDetails("");
                errorDetail.setErrorDescription("");
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                summary.incrementFailureCount();
                throw e;
            } finally {
                if (record != null) {
                    contentUploadKKCsvService.delete(record);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvImportFileName, errorFileName, summary);
        logger.info("Finished processing CircleCsv-import success");
    }

    /**
     * This method handle the event which is raised after csv upload is failed.
     * This method also deletes all the csv records which get inserted in this upload..
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = "mds.crud.kilkari.ContentUploadKKCsv.csv-import.failure")
    public void contentUploadKKCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("Start processing ContentUploadKKCsv-import failure for upload {}", params.toString());
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            ContentUploadCsv oldRecord = contentUploadKKCsvService.getRecord(id);
            if (oldRecord != null) {
                contentUploadKKCsvService.delete(oldRecord);
                logger.info("Record deleted successfully from ContentUploadKKCsv table for id {}", id.toString());
            }
        }
        logger.info("Finished processing ContentUploadKKCsv-import failure");
    }

    /**
     *  This method is used to validate csv uploaded record
     *  and map ContentUploadCsv to ContentUpload
     *
     * @param record of ContentUploadCsv type
     * @return ContentUpload record after the mapping
     * @throws DataValidationException
     */
    private ContentUpload mapContentUploadKKFrom(ContentUploadCsv record) throws DataValidationException {
        ContentUpload newRecord = new ContentUpload();

        newRecord.setCircleCode(ParseDataHelper.parseString("circleCode", record.getCircleCode(), true));
        newRecord.setContentDuration(ParseDataHelper.parseInt("contentDuration", record.getContentDuration(), true));
        newRecord.setContentFile(ParseDataHelper.parseString("contentFile", record.getContentFile(), true));
        newRecord.setContentId(ParseDataHelper.parseLong("contentId", record.getContentId(), true));
        newRecord.setContentName(ParseDataHelper.parseString("contentName", record.getContentName(), true));
        newRecord.setLanguageLocationCode(ParseDataHelper.parseInt("LanguageLocationCode",
                record.getLanguageLocationCode(), true));

        String contentType = ParseDataHelper.parseString("contentType", record.getContentType(), true);
        if (contentType.equals(ContentType.CONTENT.toString())) {
            newRecord.setContentType(ContentType.CONTENT);
        } else {
            newRecord.setContentType(ContentType.PROMPT);
        }
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());


        return newRecord;
    }

    /**
     * Copies the field values from new Record to oldRecord for update in DB
     * @param newRecord mapped from CSV values
     * @param persistentRecord to be updated in DB
     * @return oldRecord after copied values
     */
    private  ContentUpload copyContentUploadForUpdate(ContentUpload newRecord,
                                                             ContentUpload persistentRecord) {

        persistentRecord.setCircleCode(newRecord.getCircleCode());
        persistentRecord.setContentDuration(newRecord.getContentDuration());
        persistentRecord.setContentFile(newRecord.getContentFile());
        persistentRecord.setContentId(newRecord.getContentId());
        persistentRecord.setContentName(newRecord.getContentName());
        persistentRecord.setContentType(newRecord.getContentType());
        persistentRecord.setLanguageLocationCode(newRecord.getLanguageLocationCode());

        persistentRecord.setModifiedBy(newRecord.getModifiedBy());

        return persistentRecord;
    }

}
