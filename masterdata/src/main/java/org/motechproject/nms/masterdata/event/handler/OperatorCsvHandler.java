package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
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

    BulkUploadError errorDetail = new BulkUploadError();

    CsvProcessingSummary summary = new CsvProcessingSummary(0,0);

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private OperatorCsvService operatorCsvService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.success")
    public void operatorCsvSuccess(MotechEvent motechEvent) {
        String errorFileName = "OperatorCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            processOperatorCsvRecords(params, errorFileName);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", errorFileName, summary);
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.failure")
    public void operatorCsvFailure(MotechEvent motechEvent) {
        String errorFileName = "OperatorCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
            List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");

            operatorCsvService.deleteAll();
            summary.setFailureCount(createdIds.size() + updatedIds.size());
            summary.setSuccessCount(0);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", errorFileName, summary);
        }catch (Exception ex) {
        }
    }

    private void logErrorRecord(String errorFileName, String recordStr) {
        errorDetail.setErrorDescription("");
        errorDetail.setErrorCategory("");
        errorDetail.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_ERROR_DETAIL.format(recordStr));
        errorDetail.setUserName("");
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
    }

    private void processOperatorCsvRecords(Map<String, Object> params, String errorFileName) {
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        int successCount = 0;
        int failurCount = 0;
        for(Long id : updatedIds) {
            OperatorCsv record = operatorCsvService.findById(id);
            if (record != null) {
                Operator newRecord = mapOperatorFrom(record, errorFileName);
                operatorService.update(newRecord);
                operatorCsvService.delete(record);
                successCount++;
            } else {
                failurCount++;
                logErrorRecord(errorFileName, record.toString());
            }
        }

        for(Long id : createdIds) {
            OperatorCsv record = operatorCsvService.findById(id);
            if (record != null) {
                Operator newRecord = mapOperatorFrom(record, errorFileName);
                operatorService.create(newRecord);
                operatorCsvService.delete(record);
                successCount++;
            } else {
                failurCount++;
                logErrorRecord(errorFileName, record.toString());
            }
        }
        summary.setSuccessCount(successCount);
        summary.setFailureCount(failurCount);
    }

    private Operator mapOperatorFrom(OperatorCsv record, String errorFile) {
        Operator newRecord = new Operator();
        try {
            newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
            newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
        }catch (DataValidationException ex) {
            errorDetail.setErrorCategory(ex.getErrorCode());
            errorDetail.setRecordDetails(record.toString());
            errorDetail.setErrorDescription(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION.format(ex.getErroneousField()));
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFile, errorDetail);
        }
        return newRecord;
    }
}
