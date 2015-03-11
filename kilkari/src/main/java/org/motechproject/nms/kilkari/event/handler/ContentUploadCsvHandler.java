package org.motechproject.nms.kilkari.event.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.domain.ContentType;
import org.motechproject.nms.kilkari.domain.ContentUpload;
import org.motechproject.nms.kilkari.domain.ContentUploadCsv;
import org.motechproject.nms.kilkari.service.ContentUploadCsvService;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
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

    private CircleService circleService;

    private  LanguageLocationCodeService languageLocationCodeService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private ContentUploadService contentUploadService;

    private ContentUploadCsvService contentUploadCsvService;

    private static Logger logger = LoggerFactory.getLogger(ContentUploadCsvHandler.class);

    @Autowired
    public ContentUploadCsvHandler(BulkUploadErrLogService bulkUploadErrLogService,
                                   ContentUploadService contentUploadService,
                                   ContentUploadCsvService contentUploadCsvService, CircleService circleService,
                                   LanguageLocationCodeService languageLocationCodeService) {

        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.circleService = circleService;
        this.contentUploadService = contentUploadService;
        this.contentUploadCsvService = contentUploadCsvService;
        this.languageLocationCodeService = languageLocationCodeService;
    }


    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in ContentUploadtable after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = "mds.crud.kilkari.ContentUploadCsv.csv-import.success")
    public void contentUploadCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("Start processing ContentUploadCsv-import success for upload {}", params.toString());

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
                record = contentUploadCsvService.getRecord(id);

                if (record != null) {
                    userName = record.getOwner();
                    ContentUpload newRecord = mapContentUploadFrom(record);

                    persistentRecord = contentUploadService.getRecordByContentId(newRecord.getContentId());
                    if (persistentRecord != null) {
                        persistentRecord = copyContentUploadForUpdate(newRecord, persistentRecord);
                        contentUploadService.update(persistentRecord);
                        logger.info("Record updated successfully for contentid : {}", newRecord.getContentId());
                    } else {
                        contentUploadService.create(newRecord);
                        logger.info("Record created successfully for contentid : {}", newRecord.getContentId());
                    }
                    summary.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the ContentUploadCsv table with id {}", id);
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
                errorDetail.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetail.setRecordDetails("Some Error Occurred");
                errorDetail.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                summary.incrementFailureCount();
            } finally {
                if (record != null) {
                    contentUploadCsvService.delete(record);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvImportFileName, errorFileName, summary);
        logger.info("Finished processing CircleCsv-import success");
    }

    /**
     *  This method is used to validate csv uploaded record
     *  and map ContentUploadCsv to ContentUpload
     *
     * @param record of ContentUploadCsv type
     * @return ContentUpload record after the mapping
     * @throws DataValidationException
     */
    private ContentUpload mapContentUploadFrom(ContentUploadCsv record) throws DataValidationException {
        ContentUpload newRecord = new ContentUpload();

        String circleCode = ParseDataHelper.parseString("circleCode", record.getCircleCode(), true);
        if (circleService.getRecordByCode(circleCode)!= null) {
            newRecord.setCircleCode(circleCode);
        } else {
            ParseDataHelper.raiseInvalidDataException("Circle Code", circleCode);
        }

        Integer llc = ParseDataHelper.parseInt("LanguageLocationCode", record.getLanguageLocationCode(), true);
        if (languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, llc) != null) {
            newRecord.setLanguageLocationCode(llc);
        } else {
            ParseDataHelper.raiseInvalidDataException("languageLocationCode and/or circleCode", record.getLanguageLocationCode());
        }

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
