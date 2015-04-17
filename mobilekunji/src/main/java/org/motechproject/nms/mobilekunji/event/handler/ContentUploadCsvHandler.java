package org.motechproject.nms.mobilekunji.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.ContentType;
import org.motechproject.nms.mobilekunji.domain.ContentUpload;
import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;
import org.motechproject.nms.mobilekunji.dto.CommonValidator;
import org.motechproject.nms.mobilekunji.service.ContentUploadCsvService;
import org.motechproject.nms.mobilekunji.service.ContentUploadService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
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

    private BulkUploadErrLogService bulkUploadErrLogService;

    private ContentUploadService contentUploadService;

    private ContentUploadCsvService contentUploadCsvService;

    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private static Logger logger = LoggerFactory.getLogger(ContentUploadCsvHandler.class);

    @Autowired
    public ContentUploadCsvHandler(BulkUploadErrLogService bulkUploadErrLogService, ContentUploadService contentUploadService, ContentUploadCsvService contentUploadCsvService) {
        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.contentUploadService = contentUploadService;
        this.contentUploadCsvService = contentUploadCsvService;
    }

    /**
     * This method provides a listener to the Content upload success scenario.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {ConfigurationConstants.CONTENT_UPLOAD_CSV_SUCCESS})
    public void mobileKunjiContentUploadSuccess(MotechEvent motechEvent) {
        logger.info("Success[mobileKunjiContentUploadSuccess] method start for mobileKunjiContentUploadCsv");
        ContentUploadCsv record = null;
        Map<String, Object> params = motechEvent.getParameters();
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);

        logger.info("Processing Csv file");

        List<Long> createdIds = (ArrayList<Long>) params.get(CSV_IMPORT_CREATED_IDS);
        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        String userName = null;
        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(new DateTime());

        //this loop processes each of the entries in the Content Upload Csv and performs operation(DEL/ADD/MOD)
        // on the record and also deleted each record after processing from the Csv. If some error occurs in any
        // of the records, it is reported.
        for (Long id : createdIds) {
            try {
                logger.debug("Processing uploaded id : {}", id);
                record = contentUploadCsvService.findByIdInCsv(id);
                if (record != null) {
                    logger.info("Record found in Csv database");
                    userName = record.getOwner();
                    ContentUpload newRecord = mapContentUploadFrom(record);
                    ContentUpload dbRecord = contentUploadService.findRecordByContentId(newRecord.getContentId());

                    if (dbRecord == null) {
                        contentUploadService.createContentUpload(newRecord);
                        bulkUploadStatus.incrementSuccessCount();
                    } else {

                        mappDbRecordWithCsvrecord(newRecord, dbRecord);
                        contentUploadService.updateContentUpload(dbRecord);
                        bulkUploadStatus.incrementSuccessCount();
                    }
                }

            } catch (DataValidationException dve) {
                errorDetails = setErrorDetails(csvFileName, record.toString(), dve.getErrorCode(), dve.getErrorDesc());
                bulkUploadStatus.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                logger.warn("Record not found for uploaded ID: {}", id);
            } catch (Exception ex) {

                logger.error("Exception Occur : {}", ex);
                errorDetails = setErrorDetails(csvFileName, record.toString(), ErrorCategoryConstants.INVALID_DATA, ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION);
                bulkUploadStatus.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
            } finally {
                if (null != record) {
                    contentUploadCsvService.deleteFromCsv(record);
                }
            }
        }
        bulkUploadStatus.setUploadedBy(userName);
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
        logger.info("Success[mobileKunjiContentUploadSuccess] method finished for mobileKunjiContentUploadCsv");
    }


    private void mappDbRecordWithCsvrecord(ContentUpload newRecord, ContentUpload dbRecord) {

        dbRecord.setCircleCode(newRecord.getCircleCode());
        dbRecord.setCardNumber(newRecord.getCardNumber());
        dbRecord.setContentDuration(newRecord.getContentDuration());
        dbRecord.setContentFile(newRecord.getContentFile());
        dbRecord.setCircleCode(newRecord.getCircleCode());
        dbRecord.setContentId(newRecord.getContentId());
        dbRecord.setContentName(newRecord.getContentName());
        dbRecord.setContentType(newRecord.getContentType());
        dbRecord.setLanguageLocationCode(newRecord.getLanguageLocationCode());
        dbRecord.setCreator(newRecord.getCreator());
        dbRecord.setOwner(newRecord.getOwner());
        dbRecord.setModifiedBy(newRecord.getModifiedBy());
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
        String languageLocationCode;
        String contentName = null;
        String content = null;
        String contentFile = null;
        String cardNumber = null;
        Integer contentDuration = null;

        contentId = ParseDataHelper.validateAndParseInt("Content Id", record.getContentId(), true);
        circleCode = ParseDataHelper.validateAndParseString("Circle Code", record.getCircleCode(), true);
        languageLocationCode = ParseDataHelper.validateAndParseString("Language Location Code", record.getLanguageLocationCode(), true);
        contentName = ParseDataHelper.validateAndParseString("Content name", record.getContentName(), true);
        contentFile = ParseDataHelper.validateAndParseString("Content File", record.getContentFile(), true);

        cardNumber = record.getCardNumber();
        ParseDataHelper.validateAndParseInt("Card number", cardNumber, true);
        CommonValidator.validateCardNumber(cardNumber.toString());

        contentDuration = ParseDataHelper.validateAndParseInt("Content Duration", record.getContentDuration(), true);
        content = ParseDataHelper.validateAndParseString("Content Type", record.getContentType(), true);

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
    private BulkUploadError setErrorDetails(String csvFileName, String id, String errorCategory, String errorDescription) {

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(new DateTime());
        errorDetails.setRecordType(RecordType.CONTENT_UPLOAD_MK);
        errorDetails.setRecordDetails(id);
        errorDetails.setErrorCategory(errorCategory);
        errorDetails.setErrorDescription(errorDescription);

        return errorDetails;
    }
}
