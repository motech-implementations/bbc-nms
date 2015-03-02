package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.domain.OperationType;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.motechproject.nms.masterdata.service.CircleService;
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
 * This class handles the csv upload for success and failure events for CircleCsv.
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
    @MotechListener(subjects = MasterDataConstants.CIRCLE_CSV_SUCCESS)
    public void circleCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("CIRCLE_CSV_SUCCESS event received");

        CircleCsv record = null;
        Circle persistentRecord = null;
        String userName = null;

        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary result = new CsvProcessingSummary();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);

        for (Long id : createdIds) {
            try {
                record = circleCsvService.getRecord(id);

                if (record != null) {
                    userName = record.getOwner();
                    Circle newRecord = mapCircleFrom(record);

                    persistentRecord = circleService.getRecordByCode(newRecord.getCode());
                    if (persistentRecord != null) {
                        if (OperationType.DEL.toString().equals(record.getOperation())) {
                            circleService.delete(persistentRecord);
                            logger.info("Record deleted successfully for circlecode {}", newRecord.getCode());
                        } else {
                            persistentRecord = copyCircleForUpdate(newRecord, persistentRecord);
                            circleService.update(persistentRecord);
                            logger.info("Record updated successfully for circlecode {}", newRecord.getCode());
                        }

                    } else if (OperationType.DEL.toString().equals(record.getOperation())) {
                        logger.error("Record for deletion not found in the Circle table with code {}",
                                newRecord.getCode());
                        ParseDataHelper.raiseInvalidDataException("Circle Code", newRecord.getCode());
                    } else {
                        circleService.create(newRecord);
                        logger.info("Record created successfully for circlecode {}", newRecord.getCode());

                    }
                    result.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the CircleCsv table with id {}", id);
                    errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetail.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                    result.incrementFailureCount();
                }
            } catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(ex.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                result.incrementFailureCount();
            } catch (Exception e) {
                logger.error("CIRCLE_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                errorDetail.setErrorCategory("");
                errorDetail.setRecordDetails("");
                errorDetail.setErrorDescription("");
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                result.incrementFailureCount();
                throw e;
            }
            finally {
                if(null != record){
                    circleCsvService.delete(record);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, errorFileName, result);
        logger.info("Finished processing CircleCsv-import success");
    }

    /**
     * This method handle the event which is raised after csv upload is failed.
     * This method also deletes all the csv records which get inserted in this upload..
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = MasterDataConstants.CIRCLE_CSV_FAILED)
    public void circleCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("CIRCLE_CSV_FAILED event received");

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            CircleCsv oldRecord = circleCsvService.getRecord(id);
            if (oldRecord != null) {
                logger.debug("CIRCLE_CSV_FAILED event processing start for ID: {}", id);
                circleCsvService.delete(oldRecord);
            }
        }
        logger.info("CIRCLE_CSV_FAILED event processing finished");
    }

    /**
     *  This method is used to validate csv uploaded record
     *  and map CircleCsv to Circle
     *
     * @param record of CircleCsv type
     * @return Circle record after the mapping
     * @throws DataValidationException
     */
    private Circle mapCircleFrom(CircleCsv record) throws DataValidationException {

        Circle newRecord = new Circle();

        newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
        newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
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
    private  Circle copyCircleForUpdate(Circle newRecord,
                                        Circle persistentRecord) {

        persistentRecord.setName(newRecord.getName());
        persistentRecord.setCode(newRecord.getCode());
        persistentRecord.setModifiedBy(newRecord.getModifiedBy());

        return persistentRecord;
    }
}
