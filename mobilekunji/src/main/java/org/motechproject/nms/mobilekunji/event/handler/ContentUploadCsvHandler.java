package org.motechproject.nms.mobilekunji.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
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
import org.motechproject.nms.masterdata.domain.OperationType;
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

    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private Integer successCount = 0;
    private Integer failCount = 0;
    @Autowired
    private ContentUploadCsvRecordDataService contentUploadCsvRecordDataService;

    @Autowired
    private ContentUploadRecordDataService contentUploadRecordDataService;

    @Autowired
    private BulkUploadError errorDetails;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;


    private static Logger logger = LoggerFactory.getLogger(ContentUploadCsvHandler.class);


    /**
     * This method provides a listener to the Content upload success scenario.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {"mds.crud.mobilekunji.ContentUploadCsv.csv-import.success"})
    public void mobileKunjiContentUploadSuccess(MotechEvent motechEvent) {
        logger.info("Success[mobileKunjiContentUploadSuccess] method start for mobileKunjiContentUploadCsv");
        ContentUploadCsv record = null;

        Map<String, Object> params = motechEvent.getParameters();
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);

        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        logger.info("Processing Csv file");
        CsvProcessingSummary summary = new CsvProcessingSummary(successCount, failCount);

        List<Long> createdIds = (ArrayList<Long>) params.get(CSV_IMPORT_CREATED_IDS);


        for (Long id : createdIds) {
            try {
                logger.debug("Processing uploaded id : {}", id);
                record = contentUploadCsvRecordDataService.findById(id);
                if (record != null) {
                    logger.info("Record found in Csv database");
                    ContentUpload newRecord = mapContentUploadFrom(record);

                    ContentUpload dbRecord = contentUploadRecordDataService.findRecordByContentId(newRecord.getContentId());

                    if (dbRecord == null) {
                        if (OperationType.DEL.toString().equals(record.getOperation())) {
                            summary.incrementFailureCount();
                            setErrorDetails(record.toString(), ErrorCategoryConstants.INVALID_DATA, ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION);
                            bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                            logger.warn("Record to be deleted with ID : {} not present", id);
                        } else {
                            contentUploadRecordDataService.create(newRecord);
                            contentUploadCsvRecordDataService.delete(record);
                            summary.incrementSuccessCount();
                        }

                    } else {
                        contentUploadRecordDataService.update(dbRecord);
                        contentUploadCsvRecordDataService.delete(record);
                        summary.incrementSuccessCount();
                    }
                }

            } catch (DataValidationException dve) {
                setErrorDetails(record.toString(), dve.getErrorCode(), dve.getErrorDesc());
                summary.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                logger.warn("Record not found for uploaded ID: {}", id);
            } catch (Exception ex) {
                summary.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
            }
        }

        logger.info("Success[mobileKunjiContentUploadSuccess] method finished for mobileKunjiContentUploadCsv");
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
        int contentId;
        String circleCode;
        Integer languageLocationCode;
        String contentName;
        ContentType contentType = null;
        String content;
        String contentFile;
        int cardNumber;
        int contentDuration;

        contentId = ParseDataHelper.parseInt("Content Id", record.getContentId(), true);
        circleCode = ParseDataHelper.parseString("Circle Code", record.getCircleCode(), true);
        languageLocationCode = ParseDataHelper.parseInt("Language Location Code", record.getLanguageLocationCode(), true);
        contentName = ParseDataHelper.parseString("Conteny name", record.getContentName(), true);
        contentFile = ParseDataHelper.parseString("Content File", record.getContentFile(), true);
        cardNumber = ParseDataHelper.parseInt("Card number", record.getCardNumber(), true);
        contentDuration = ParseDataHelper.parseInt("Content Duration", record.getContentDuration(), true);
        content = ParseDataHelper.parseString("Content Type", record.getContentType(), true);

        ContentUpload newRecord = new ContentUpload();
        if (ContentType.of(content) != ContentType.CONTENT || ContentType.of(content) != ContentType.PROMPT) {
            ParseDataHelper.raiseInvalidDataException("Content Type", "Invalid");
        }

        newRecord.setContentId(contentId);
        newRecord.setCircleCode(circleCode);
        newRecord.setLanguageLocationCode(languageLocationCode);
        newRecord.setContentName(contentName);
        newRecord.setContentFile(contentFile);
        newRecord.setCardNumber(cardNumber);
        newRecord.setContentDuration(contentDuration);
        newRecord.setContentType(contentType);
        logger.info("mapContentUploadFrom process end");
        return newRecord;

    }


    /**
     * This method provides a listener to the Content Upload failure scenario.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {"mds.crud.mobilekunji.ContentUploadCsv.csv-import.failed"})
    public void mobileKunjiContentUploadCsvFailure(MotechEvent motechEvent) {

        logger.info("Failure[mobileKunjiContentUploadFailure] method start for mobileKunjiContentUploadCsv");
        Map<String, Object> params = motechEvent.getParameters();
        CsvProcessingSummary summary = new CsvProcessingSummary(successCount, failCount);
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);

        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        for (Long id : createdIds) {
            try {
                ContentUploadCsv record = contentUploadCsvRecordDataService.findById(id);
                contentUploadCsvRecordDataService.delete(record);
                summary.incrementFailureCount();
                setErrorDetails(record.toString(), "Upload failure", "Content Upload failure");
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
            } catch (Exception ex) {
                summary.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
            }
        }
        logger.info("Failure[mobileKunjiContentUploadFailure] method finished for mobileKunjiContentUploadCsv");
    }

    /**
     * This method is used to set error record details
     *
     * @param id               record for which error is generated
     * @param errorCategory    specifies error category
     * @param errorDescription specifies error descriotion
     */
    private void setErrorDetails(String id, String errorCategory, String errorDescription) {
        errorDetails.setRecordDetails(id);
        errorDetails.setErrorCategory(errorCategory);
        errorDetails.setErrorDescription(errorDescription);
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
}
