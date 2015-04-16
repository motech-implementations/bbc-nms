package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CsvCircle;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.motechproject.nms.masterdata.service.CircleService;
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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class handles the csv upload for success and failure events for Circle Csv.
 */
@Component
public class CircleCsvHandler {

    private BulkUploadErrLogService bulkUploadErrLogService;

    private CircleService circleService;

    private CircleCsvService circleCsvService;

    private static Logger logger = LoggerFactory.getLogger(CircleCsvHandler.class);

    @Autowired
    public CircleCsvHandler(BulkUploadErrLogService bulkUploadErrLogService, CircleService circleService, CircleCsvService circleCsvService) {
        this.circleCsvService = circleCsvService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.circleService = circleService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in Circle table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = LocationConstants.CIRCLE_CSV_SUCCESS)
    public void circleCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("CIRCLE_CSV_SUCCESS event received");
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        processRecords(createdIds, csvFileName);

    }


    public void processRecords(List<Long> createdId,
                               String csvFileName) {
        logger.info("Record Processing Started for csv file: {}", csvFileName);

        circleService.getCircleDataService()
                .doInTransaction(new TransactionCallback<Circle>() {

                    List<Long> csvCircleId;

                    String csvFileName;

                    private TransactionCallback<Circle> init(
                            List<Long> createdId,
                            String csvFileName) {
                        this.csvCircleId = createdId;
                        this.csvFileName = csvFileName;
                        return this;
                    }

                    @Override
                    public Circle doInTransaction(
                            TransactionStatus status) {
                        Circle transactionObject = null;
                        processCircleRecords(csvFileName, csvCircleId);
                        return transactionObject;
                    }
                }.init(createdId, csvFileName));
        logger.info("Record Processing complete for csv file: {}", csvFileName);
    }


    private void processCircleRecords(String csvFileName, List<Long> createdIds) {
        CsvCircle record = null;
        Circle persistentRecord = null;

        DateTime timeStamp = new DateTime();
        BulkUploadError errorDetail = new BulkUploadError();

        BulkUploadStatus uploadStatus = new BulkUploadStatus();

        ErrorLog.setErrorDetails(errorDetail, uploadStatus, csvFileName, timeStamp, RecordType.CIRCLE);
        for (Long id : createdIds) {
            try {
                record = circleCsvService.getRecord(id);

                if (record != null) {
                    uploadStatus.setUploadedBy(record.getOwner());
                    Circle newRecord = mapCircleFrom(record);
                    persistentRecord = circleService.getRecordByCode(newRecord.getCode());

                    if (persistentRecord != null) {
                        persistentRecord = copyCircleForUpdate(newRecord, persistentRecord);
                        circleService.update(persistentRecord);
                        logger.info("Record updated successfully for circlecode {}", newRecord.getCode());

                    } else {
                        circleService.create(newRecord);
                        logger.info("Record created successfully for circlecode {}", newRecord.getCode());
                    }
                    uploadStatus.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the CircleCsv table with id {}", id);

                    ErrorLog.errorLog(errorDetail, uploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION, ErrorCategoryConstants.CSV_RECORD_MISSING, "Record is null");

                }
            } catch (DataValidationException ex) {

                ErrorLog.errorLog(errorDetail, uploadStatus, bulkUploadErrLogService, ex.getErrorDesc(), ex.getErrorCode(), record.toString());

            } catch (Exception e) {
                logger.error("CIRCLE_CSV_SUCCESS processing receive Exception exception, message: {}", e);

                ErrorLog.errorLog(errorDetail, uploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION, ErrorCategoryConstants.GENERAL_EXCEPTION, "Exception occurred");


            } finally {
                if (null != record) {
                    circleCsvService.delete(record);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(uploadStatus);
        logger.info("Finished processing CircleCsv-import success");

    }


    /**
     * This method is used to validate csv uploaded record
     * and map CircleCsv to Circle
     *
     * @param record of CircleCsv type
     * @return Circle record after the mapping
     * @throws DataValidationException
     */
    private Circle mapCircleFrom(CsvCircle record) throws DataValidationException {

        Circle newRecord = new Circle();

        newRecord.setCode(ParseDataHelper.validateAndParseString("Code", record.getCode(), true));
        newRecord.setName(ParseDataHelper.validateAndParseString("Name", record.getName(), true));
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    /**
     * Copies the field values from new Record to oldRecord for update in DB
     *
     * @param newRecord        mapped from CSV values
     * @param persistentRecord to be updated in DB
     * @return oldRecord after copied values
     */
    private Circle copyCircleForUpdate(Circle newRecord, Circle persistentRecord) {

        persistentRecord.setName(newRecord.getName());
        persistentRecord.setCode(newRecord.getCode());
        persistentRecord.setModifiedBy(newRecord.getModifiedBy());

        return persistentRecord;
    }
}
