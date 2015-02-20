package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.OperatorCsv;
import org.motechproject.nms.masterdata.service.OperatorCsvService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.util.BulkUploadErrRecordDetails;
import org.motechproject.nms.util.constants.ErrorCodeConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OperatorCsvHandler {

    private static Integer successCount = 0;
    private static Integer failCount = 0;

    private BulkUploadErrRecordDetails errorDetails = new BulkUploadErrRecordDetails();

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
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, successCount, failCount);
        }catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.OperatorCsv.csv-import.failure")
    public void operatorCsvFailure(MotechEvent motechEvent) {
        String errorFileName = "OperatorCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();;
            List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

            operatorCsvService.deleteAll();
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, 0, createdIds.size());
        }catch (Exception ex) {
        }
    }

    private void logErrorRecord(String errorFileName, Long index) {
        errorDetails.setErrorCode("");
        errorDetails.setErrorDescription("");
        errorDetails.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_ERROR_DETAIL.format(index.toString()));
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetails);
        failCount++;
    }

    private void processOperatorCsvRecords(Map<String, Object> params, String errorFileName) {
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");

        for(Long id : updatedIds) {
            OperatorCsv record = operatorCsvService.findById(id);
            if (record != null) {
                Operator newRecord = mapOperatorFrom(record);
                operatorService.update(newRecord);
                operatorCsvService.delete(record);
                successCount++;
            } else {
                logErrorRecord(errorFileName, record.getIndex());
            }
        }

        for(Long id : createdIds) {
            OperatorCsv record = operatorCsvService.findById(id);
            if (record != null) {
                Operator newRecord = mapOperatorFrom(record);
                operatorService.create(newRecord);
                operatorCsvService.delete(record);
                successCount++;
            } else {
                logErrorRecord(errorFileName, record.getIndex());
            }
        }
    }

    private static Operator mapOperatorFrom(OperatorCsv record) {
        Operator newRecord = new Operator();
        try {
            newRecord.setName(ParseDataHelper.parseString("Name", record.getName(), true));
            newRecord.setCode(ParseDataHelper.parseString("Code", record.getCode(), true));
        }catch (DataValidationException ex) {

        }
        return newRecord;
    }
}
