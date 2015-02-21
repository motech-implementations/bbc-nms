package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.OperatorCsv;
import org.motechproject.nms.masterdata.service.OperatorCsvService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OperatorCsvHandler {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorCsvService operatorCsvService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.success")
    public void operatorCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        String csvImportFileName = (String)params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);
        processOperatorCsvRecords(params, errorFileName);
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.failure")
    public void operatorCsvFailure(MotechEvent motechEvent) {
        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary(0,0);

        Map<String, Object> params = motechEvent.getParameters();
        String errorFileName = params.get("entity_name_") + new Date().toString();

        try {
            int createdCount = (int)params.get("csv-import.created_count");
            int updatedCount = (int)params.get("csv-import.updated_count");
            String csvImportFileName = (String)params.get("csv-import.filename");

            operatorCsvService.deleteAll();
            summary.setFailureCount(createdCount + updatedCount);
            summary.setSuccessCount(0);
            //todo : username
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", csvImportFileName, errorFileName, summary);
        }catch (Exception ex) {
            //todo: errorDetail for this error. can we replace it with a common helper method
            errorDetail.setErrorCategory("");
            errorDetail.setErrorDescription(ex.getMessage());
            errorDetail.setRecordDetails("");
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        }
    }

    private void processOperatorCsvRecords(Map<String, Object> params, String errorFileName) {
        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary(0,0);
        String csvImportFileName = (String)params.get("csv-import.filename");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        OperatorCsv record = null;
        int successCount = 0;
        int failurCount = 0;

        for(Long id : createdIds) {
            try {
                record = operatorCsvService.findById(id);
                if (record != null) {
                    Operator newRecord = mapOperatorFrom(record);
                    operatorService.create(newRecord);
                    operatorCsvService.delete(record);
                    successCount++;
                }
            }catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(String.format(
                        ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
            }catch (Exception ex) {
                errorDetail.setErrorDescription("Record not found in the database");
                errorDetail.setErrorCategory("");
                errorDetail.setRecordDetails("Record is null");
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
            }
        }
        summary.setSuccessCount(successCount);
        summary.setFailureCount(failurCount);
        //todo : username
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("", csvImportFileName, errorFileName, summary);
    }

    private void logErrorRecord(String errorFileName, BulkUploadError errorDetail) {
        //todo: errorDetail for this error. can we replace it with a common helper method
        errorDetail.setErrorDescription("Record not found in the database");
        errorDetail.setErrorCategory("");
        errorDetail.setRecordDetails("Record is null");
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
    }

    private Operator mapOperatorFrom(OperatorCsv record) throws DataValidationException{
        Operator newRecord = new Operator();
            newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
            newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
        return newRecord;
    }
}
