package org.motechproject.nms.mobilekunji.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.mobilekunji.constants.KunjiConstants;
import org.motechproject.nms.mobilekunji.domain.ContentType;
import org.motechproject.nms.mobilekunji.domain.ContentUpload;
import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;
import org.motechproject.nms.mobilekunji.repository.ContentUploadCsvRecordDataService;
import org.motechproject.nms.mobilekunji.repository.ContentUploadRecordDataService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class provides Motech Listeners for Content Upload for both success and failure scenarios.
 * This class saves the data in Motech database in case of success
 * and raise exceptions in case of any failure.
 */
@Component
public class ContentUploadCsvHandler {


    //@Autowired
    private ContentUploadCsvRecordDataService contentUploadCsvRecordDataService;

    //@Autowired
    private ContentUploadRecordDataService contentUploadRecordDataService;

    //@Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;


    private Integer successCount = 0;
    private Integer failCount = 0;
    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private static Logger logger = LoggerFactory.getLogger(ContentUploadCsvHandler.class);


    @Autowired
    public ContentUploadCsvHandler(ContentUploadCsvRecordDataService contentUploadCsvRecordDataService,
                                   ContentUploadRecordDataService contentUploadRecordDataService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.contentUploadCsvRecordDataService = contentUploadCsvRecordDataService;
        this.contentUploadRecordDataService = contentUploadRecordDataService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }


    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }



    /**
     * This method provides a listener to the Content upload success scenario.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {KunjiConstants.CONTENT_UPLOAD_CSV_SUCCESS})
    public void mobileKunjiContentUploadSuccess(MotechEvent motechEvent) {
        logger.info("Success[mobileKunjiContentUploadSuccess] method start for mobileKunjiContentUploadCsv");
        ContentUploadCsv record = null;

        Map<String, Object> params = motechEvent.getParameters();
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);

        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        logger.info("Processing Csv file");
        CsvProcessingSummary summary = new CsvProcessingSummary(successCount, failCount);

        List<Long> createdIds = (ArrayList<Long>) params.get(CSV_IMPORT_CREATED_IDS);
        BulkUploadError errorDetails = null;

        //this loop processes each of the entries in the Content Upload Csv and performs operation(DEL/ADD/MOD)
        // on the record and also deleted each record after processing from the Csv. If some error occurs in any
        // of the records, it is reported.
        for (Long id : createdIds) {
            try {
                logger.debug("Processing uploaded id : {}", id);
                record = contentUploadCsvRecordDataService.findById(id);
                if (record != null) {
                    logger.info("Record found in Csv database");

                    ContentUpload newRecord = mapContentUploadFrom(record);

                    ContentUpload dbRecord = contentUploadRecordDataService.findRecordByContentId(newRecord.getContentId());

                    if (dbRecord == null) {

                            contentUploadRecordDataService.create(newRecord);
                            summary.incrementSuccessCount();


                    } else {

                            mappDbRecordWithCsvrecord(newRecord, dbRecord);
                            contentUploadRecordDataService.update(dbRecord);
                            summary.incrementSuccessCount();


                    }
                }

            } catch (DataValidationException dve) {
                errorDetails = setErrorDetails(record.toString(), dve.getErrorCode(), dve.getErrorDesc());
                summary.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                logger.warn("Record not found for uploaded ID: {}", id);
            } catch (Exception ex) {

                logger.error("Exception Occur : {}", ex);
                errorDetails = setErrorDetails(record.toString(), ErrorCategoryConstants.INVALID_DATA, ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION);
                summary.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
            } finally {
                if (null != record) {
                    contentUploadCsvRecordDataService.delete(record);
                }
            }
        }

        logger.info("Success[mobileKunjiContentUploadSuccess] method finished for mobileKunjiContentUploadCsv");
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(record.getOwner(), csvFileName, logFile, summary);


    }


    private void mappDbRecordWithCsvrecord(ContentUpload newRecord,ContentUpload dbRecord){

        dbRecord.setCircleCode(newRecord.getCircleCode());
        dbRecord.setCardNumber(newRecord.getCardNumber());
        dbRecord.setContentDuration(newRecord.getContentDuration());
        dbRecord.setContentFile(newRecord.getContentFile());
        dbRecord.setCircleCode(newRecord.getCircleCode());
        dbRecord.setContentId(newRecord.getContentId());
        dbRecord.setContentName(newRecord.getContentName());
        dbRecord.setContentType(newRecord.getContentType());
        dbRecord.setLanguageLocationCode(newRecord.getLanguageLocationCode());
    }


    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param record the Content Upload record from Csv that is to be validated
     * @return the Conetnt Upload generated after applying validations.
     * @throws DataValidationException
     */
    private ContentUpload mapContentUploadFrom(ContentUploadCsv record) throws DataValidationException {
        logger.info("mapContentUploadFrom process start");

        ContentUpload newRecord = new ContentUpload();
        Integer contentId = null;
        String circleCode = null;
        Integer languageLocationCode;
        String contentName = null;
        String content = null;
        String contentFile = null;
        Integer cardNumber = null;
        Integer contentDuration = null;

        contentId = ParseDataHelper.parseInt("Content Id", record.getContentId(), true);
        circleCode = ParseDataHelper.parseString("Circle Code", record.getCircleCode(), true);
        languageLocationCode = ParseDataHelper.parseInt("Language Location Code", record.getLanguageLocationCode(), true);
        contentName = ParseDataHelper.parseString("Conteny name", record.getContentName(), true);
        contentFile = ParseDataHelper.parseString("Content File", record.getContentFile(), true);
        cardNumber = ParseDataHelper.parseInt("Card number", record.getCardNumber(), true);
        contentDuration = ParseDataHelper.parseInt("Content Duration", record.getContentDuration(), true);
        content = ParseDataHelper.parseString("Content Type", record.getContentType(), true);

        //Bug24
        if (ContentType.of(content) != ContentType.CONTENT && ContentType.of(content) != ContentType.PROMPT) {
            ParseDataHelper.raiseInvalidDataException("Content Type", "Invalid");
        }

        newRecord.setContentId(contentId);
        newRecord.setCircleCode(circleCode);
        newRecord.setLanguageLocationCode(languageLocationCode);
        newRecord.setContentName(contentName);
        newRecord.setContentFile(contentFile);
        newRecord.setCardNumber(cardNumber);
        newRecord.setContentDuration(contentDuration);
        newRecord.setContentType(ContentType.of(content));
        newRecord.setCreator(record.getCreator());
        newRecord.setModifiedBy(record.getModifiedBy());
        newRecord.setOwner(record.getOwner());
        logger.info("mapContentUploadFrom process end");
        return newRecord;

    }

    /**
     * This method is used to set error record details
     *
     * @param id               record for which error is generated
     * @param errorCategory    specifies error category
     * @param errorDescription specifies error descriotion
     */
    private BulkUploadError setErrorDetails(String id, String errorCategory, String errorDescription) {

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setRecordDetails(id);
        errorDetails.setErrorCategory(errorCategory);
        errorDetails.setErrorDescription(errorDescription);

        return errorDetails;
    }



}
