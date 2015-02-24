package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
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

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private CircleCsvService circleCsvService;

    private static Logger logger = LoggerFactory.getLogger(CircleCsvHandler.class);

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in Circle table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.success")
    public void circleCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("Start processing CircleCsv-import success for upload {}", params.toString());

        CircleCsv record = null;
        Circle persistentRecord = null;
        String userName = null;

        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary();


        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvImportFileName = (String) params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);

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
                            newRecord.setId(persistentRecord.getId());
                            newRecord.setModifiedBy(userName);
                            circleService.update(newRecord);
                            logger.info("Record updated successfully for circlecode {}", newRecord.getCode());
                        }

                    } else {
                        newRecord.setOwner(userName);
                        newRecord.setModifiedBy(userName);
                        circleService.create(newRecord);
                        logger.info("Record created successfully for circlecode {}", newRecord.getCode());
                    }
                    summary.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the CircleCsv table with id {}", id);
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
                System.out.println("reached");
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
    @MotechListener(subjects = "mds.crud.masterdatamodule.CircleCsv.csv-import.failure")
    public void circleCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("Start processing CircleCsv-import failure for upload {}", params.toString());


        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            CircleCsv oldRecord = circleCsvService.getRecord(id);
            if (oldRecord != null) {
                circleCsvService.delete(oldRecord);
                logger.info("Record deleted successfully from CircleCsv table for id {}", id.toString());
            }
        }
        logger.info("Finished processing CircleCsv-import failure");
    }

    private Circle mapCircleFrom(CircleCsv record) throws DataValidationException {
        Circle newRecord = new Circle();
        newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
        newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }
}
