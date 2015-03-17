package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.OperatorCsv;
import org.motechproject.nms.masterdata.service.OperatorCsvService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsUtils;
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
 * This class handles the csv upload for success and failure events for OperatorCsv.
 */
@Component
public class OperatorCsvHandler {

    private OperatorService operatorService;

    private OperatorCsvService operatorCsvService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(OperatorCsvHandler.class);

    @Autowired
    public OperatorCsvHandler(OperatorService operatorService, OperatorCsvService operatorCsvService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.operatorService = operatorService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.operatorCsvService = operatorCsvService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in Operator table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = MasterDataConstants.OPERATOR_CSV_SUCCESS)
    public void operatorCsvSuccess(MotechEvent motechEvent) {

        logger.info("OPERATOR_CSV_SUCCESS event received");
        Map<String, Object> params = motechEvent.getParameters();

        OperatorCsv record = null;
        Operator persistentRecord = null;

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);

        DateTime timeStamp = NmsUtils.getCurrentTimeStamp();
        BulkUploadError errorDetail = new BulkUploadError();
        errorDetail.setCsvName(csvFileName);
        errorDetail.setTimeOfUpload(timeStamp);
        errorDetail.setRecordType(RecordType.OPERATOR);

        BulkUploadStatus uploadStatus = new BulkUploadStatus();
        uploadStatus.setBulkUploadFileName(csvFileName);
        uploadStatus.setTimeOfUpload(timeStamp);

        for (Long id : createdIds) {
            try {
                record = operatorCsvService.getRecord(id);
                if (record != null) {
                    uploadStatus.setUploadedBy(record.getOwner());
                    Operator newRecord = mapOperatorFrom(record);

                    persistentRecord = operatorService.getRecordByCode(newRecord.getCode());
                    if (persistentRecord != null) {
                        persistentRecord = copyOperatorForUpdate(newRecord, persistentRecord);
                        operatorService.update(persistentRecord);
                        logger.info("Record updated successfully for operatorcode {}", newRecord.getCode());
                    } else {
                        operatorService.create(newRecord);
                        logger.info("Record created successfully for operatorcode {}", newRecord.getCode());
                    }
                    uploadStatus.incrementSuccessCount();

                } else {
                    logger.error("Record not found in the OperatorCsv table with id {}", id);
                    errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetail.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetail);
                    uploadStatus.incrementFailureCount();
                }
            } catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(ex.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetail);
                uploadStatus.incrementFailureCount();
            } catch (Exception e) {
                logger.error("OPERATOR_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                errorDetail.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetail.setRecordDetails("Some Error Occurred");
                errorDetail.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetail);
                uploadStatus.incrementFailureCount();
            } finally {
                if (null != record) {
                    operatorCsvService.delete(record);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(uploadStatus);
        logger.info("Finished processing OperatorCsv-import success");
    }

    /**
     * This method is used to validate csv uploaded record
     * and map OperatorCsv to Operator
     *
     * @param record of OperatorCsv type
     * @return Operator record after the mapping
     * @throws DataValidationException
     */
    private Operator mapOperatorFrom(OperatorCsv record) throws DataValidationException {
        Operator newRecord = new Operator();

        newRecord.setName(ParseDataHelper.validateAndParseString("Name", record.getName(), true));
        newRecord.setCode(ParseDataHelper.validateAndParseString("Code", record.getCode(), true));
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
    private Operator copyOperatorForUpdate(Operator newRecord,
                                           Operator persistentRecord) {

        persistentRecord.setName(newRecord.getName());
        persistentRecord.setCode(newRecord.getCode());
        persistentRecord.setModifiedBy(newRecord.getModifiedBy());

        return persistentRecord;
    }
}

