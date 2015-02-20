package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeServiceCsv;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorCodeConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LanguageLocationCodeCsvHandler {

    BulkUploadError errorDetail = new BulkUploadError();

    CsvProcessingSummary summary = new CsvProcessingSummary(0,0);

    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    private LanguageLocationCodeServiceCsv languageLocationCodeServiceCsv;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.success")
    public void languageLocationCodeCsvSuccess(MotechEvent motechEvent) {
        SimpleDateFormat tt = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss");
        String errorFileName = "LanguageLocationCodeCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();
            processLanguageLocationCodeCsvRecords(params, errorFileName);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", errorFileName, summary);
        } catch (Exception ex) {
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.failure")
    public void languageLocationCodeCsvFailure(MotechEvent motechEvent) {
        String errorFileName = "LanguageLocationCodeCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();
            List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
            List<Long> updatedIds = (ArrayList<Long>) params.get("csv-import.updated_ids");

            languageLocationCodeServiceCsv.deleteAll();
            summary.setFailureCount(createdIds.size() + updatedIds.size());
            summary.setSuccessCount(0);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", errorFileName, summary);

        } catch (Exception ex) {
        }

    }

    private void processLanguageLocationCodeCsvRecords(Map<String, Object> params, String errorFileName) {
        List<Long> updatedIds = (ArrayList<Long>) params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        int successCount = 0;
        int failureCount = 0;

        for (Long id : updatedIds) {
            LanguageLocationCodeCsv record = languageLocationCodeServiceCsv.findById(id);
                if (record != null) {
                    LanguageLocationCode newRecord = mapLanguageLocationCodeFrom(record, errorFileName);
                    languageLocationCodeService.update(newRecord);
                    languageLocationCodeServiceCsv.delete(record);
                    successCount++;
                } else {
                    failureCount++;
                    logErrorRecord(errorFileName, record.toString());
                }
        }

        for (Long id : createdIds) {
            LanguageLocationCodeCsv record = languageLocationCodeServiceCsv.findById(id);
                if (record != null) {
                    LanguageLocationCode newRecord = mapLanguageLocationCodeFrom(record, errorFileName);
                    languageLocationCodeService.create(newRecord);
                    languageLocationCodeServiceCsv.delete(record);
                    successCount++;
                } else {
                    failureCount++;
                    logErrorRecord(errorFileName, record.toString());
                }
        }
        summary.setFailureCount(failureCount);
        summary.setSuccessCount(successCount);
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("", errorFileName, summary);
    }

    private void logErrorRecord(String errorFileName, String recordStr) {
        errorDetail.setErrorDescription("Record not found in database");
        errorDetail.setErrorCategory("");
        errorDetail.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_ERROR_DETAIL.format(recordStr));
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
    }

    private LanguageLocationCode mapLanguageLocationCodeFrom(LanguageLocationCodeCsv record, String errorFile) {
        LanguageLocationCode newRecord = new LanguageLocationCode();
        try {
            newRecord.setCircleId(ParseDataHelper.parseString("circleId", record.getCircleId(), true));
            newRecord.setDistrictId(ParseDataHelper.parseInt("districtId", record.getDistrictId(), true));
            newRecord.setStateId(ParseDataHelper.parseInt("stateId", record.getStateId(), true));

            if (record.getIsDeployedKK().equals("true")) {
                newRecord.setDefaultLanguageLocationCodeKK(ParseDataHelper.parseInt("DefaultLanguageLocationCodeKK", record.getIsDefaultLanguageLocationCodeKK(), true));
                newRecord.setLanguageLocationCodeKK(ParseDataHelper.parseInt("LanguageLocationCodeKK", record.getLanguageLocationCodeKK(), true));
                newRecord.setLanguageKK(ParseDataHelper.parseString("LanguageKK", record.getLanguageKK(), true));
            }

            if (record.getIsDeployedMK().equals("true")) {
                newRecord.setDefaultLanguageLocationCodeMK(ParseDataHelper.parseInt("DefaultLanguageLocationCodeMK", record.getIsDefaultLanguageLocationCodeMK(), true));
                newRecord.setLanguageLocationCodeMK(ParseDataHelper.parseInt("LanguageLocationCodeMK", record.getLanguageLocationCodeMK(), true));
                newRecord.setLanguageMK(ParseDataHelper.parseString("LanguageMK", record.getLanguageMK(), true));
            }

            if (record.getIsDeployedMA().equals("true")) {
                newRecord.setDefaultLanguageLocationCodeKK(ParseDataHelper.parseInt("DefaultLanguageLocationCodeMA", record.getIsDefaultLanguageLocationCodeMA(), true));
                newRecord.setLanguageLocationCodeMA(ParseDataHelper.parseInt("LanguageLocationCodeMA", record.getLanguageLocationCodeMA(), true));
                newRecord.setLanguageMA(ParseDataHelper.parseString("LanguageMA", record.getLanguageMA(), true));
            }
        }catch (DataValidationException ex) {
            errorDetail.setErrorCategory(ex.getErrorCode());
            errorDetail.setErrorDescription(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION.format(ex.getErroneousField()));
            errorDetail.setRecordDetails(record.toString());
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFile, errorDetail);
        }
            return newRecord;
    }
}

