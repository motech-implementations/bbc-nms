package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.OperationType;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.OperatorCsv;
import org.motechproject.nms.masterdata.service.OperatorCsvService;
import org.motechproject.nms.masterdata.service.OperatorService;
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
 * This class handles the csv upload for success and failure events for OperatorCsv.
 */
public class OperatorCsvHandler {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorCsvService operatorCsvService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(DistrictCsvUploadHandler.class);

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in Operator table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.success")
    public void operatorCsvSuccess(MotechEvent motechEvent) {

        OperatorCsv record = null;
        Operator persistentRecord = null;
        String userName = null;

        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary();

        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvImportFileName = (String) params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);

        for (Long id : createdIds) {
            try {
                record = operatorCsvService.getRecord(id);
                if (record != null) {
                    userName = record.getOwner();
                    Operator newRecord = mapOperatorFrom(record);

                    persistentRecord = operatorService.getRecordByCode(newRecord.getCode());

                    if (persistentRecord != null) {
                        if (OperationType.DEL.toString().equals(record.getOperation())) {
                            operatorService.delete(persistentRecord);
                            logger.info(String.format("Record deleted successfully for operatorcode %s", newRecord.getCode()));
                        }else {
                            newRecord.setId(persistentRecord.getId());
                            newRecord.setModifiedBy(userName);
                            operatorService.update(newRecord);
                            logger.info(String.format("Record updated successfully for operatorcode %s", newRecord.getCode()));
                        }
                    } else {
                        newRecord.setOwner(userName);
                        newRecord.setModifiedBy(userName);
                        operatorService.create(newRecord);
                        logger.info(String.format("Record created successfully for operatorcode %s", newRecord.getCode()));
                    }
                    summary.incrementSuccessCount();
                } else {
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
    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.failure")
    public void operatorCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            OperatorCsv oldRecord = operatorCsvService.getRecord(id);
            if (oldRecord != null) {
                operatorCsvService.delete(oldRecord);
                logger.info(String.format("Record deleted successfully for id %s", id.toString()));
            }
        }
    }

    private Operator mapOperatorFrom(OperatorCsv record) throws DataValidationException {
        Operator newRecord = new Operator();
        newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
        return newRecord;
    }
}

