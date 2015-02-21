package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
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

    @Autowired
    private CircleCsvService circleCsvService;

    @Autowired
    private LocationService locationService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.success")
    public void languageLocationCodeCsvSuccess(MotechEvent motechEvent) {
        //todo datetime format
        SimpleDateFormat tt = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss");
        Map<String, Object> params = motechEvent.getParameters();
        String errorFileName = params.get("entity_name_") + new Date().toString();
        try {
            processLanguageLocationCodeCsvRecords(params, errorFileName);
        } catch (Exception ex) {
            //todo: errorDetail for this error. Can we replace it with a common helper method
            errorDetail.setErrorCategory("");
            errorDetail.setErrorDescription(ex.getMessage());
            errorDetail.setRecordDetails("");
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        }
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.failure")
    public void languageLocationCodeCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        String errorFileName = params.get("entity_name_") + new Date().toString();
        try {
            int createdCount = (int)params.get("csv-import.created_count");
            int updatedCount = (int)params.get("csv-import.updated_count");
            String csvImportFileName = (String)params.get("csv-import.filename");

            languageLocationCodeServiceCsv.deleteAll();
            summary.setFailureCount(createdCount + updatedCount);
            summary.setSuccessCount(0);
            //todo : username
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", csvImportFileName, errorFileName, summary);
        } catch (Exception ex) {
            //todo: errorDetail for this error. Can we replace it with a common helper method
            errorDetail.setErrorCategory("");
            errorDetail.setErrorDescription(ex.getMessage());
            errorDetail.setRecordDetails("");
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        }

    }

    private void processLanguageLocationCodeCsvRecords(Map<String, Object> params, String errorFileName) {
        String csvImportFileName = (String)params.get("csv-import.filename");
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
                    logErrorRecord(errorFileName);
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
                    logErrorRecord(errorFileName);
                }
        }
        summary.setFailureCount(failureCount);
        summary.setSuccessCount(successCount);
        //todo : username
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("", csvImportFileName, errorFileName, summary);
    }

    private void logErrorRecord(String errorFileName) {
        //todo: errorDetail for this error. can we replace it with a common helper method
        errorDetail.setErrorDescription("Record not found in the database");
        errorDetail.setErrorCategory("");
        errorDetail.setRecordDetails("Record is null");
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
    }

    private LanguageLocationCode mapLanguageLocationCodeFrom(LanguageLocationCodeCsv record, String errorFile) {
        LanguageLocationCode newRecord = new LanguageLocationCode();
        try {
            Long stateId = ParseDataHelper.parseLong("stateId", record.getStateId(), true);
            Circle circle = circleCsvService.getCircleByCode(ParseDataHelper.parseString("circleId", record.getCircleId(), true));
            State state = locationService.getStateByCode(stateId);
            District district = locationService.getDistrictByCode(stateId,ParseDataHelper.parseLong("districtId", record.getDistrictId(), true));

            newRecord.setCircleId(circle);
            newRecord.setStateId(state);
            newRecord.setDistrictId(district);

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
            errorDetail.setErrorDescription(String.format(
                    ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
            errorDetail.setRecordDetails(record.toString());
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFile, errorDetail);
        }
            return newRecord;
    }
}

