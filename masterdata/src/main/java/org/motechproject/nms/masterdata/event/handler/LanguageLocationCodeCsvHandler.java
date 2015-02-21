package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LanguageLocationCodeCsvHandler {
    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    private LanguageLocationCodeServiceCsv languageLocationCodeServiceCsv;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private LocationService locationService;

    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.success")
    public void languageLocationCodeCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        String csvImportFileName = (String)params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);
        processLanguageLocationCodeCsvRecords(params, errorFileName);
    }

    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.failure")
    public void languageLocationCodeCsvFailure(MotechEvent motechEvent) {
        CsvProcessingSummary summary = new CsvProcessingSummary(0,0);
        Map<String, Object> params = motechEvent.getParameters();

        String csvImportFileName = (String)params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);

        try {
            int createdCount = (int)params.get("csv-import.created_count");
            int updatedCount = (int)params.get("csv-import.updated_count");

            languageLocationCodeServiceCsv.deleteAll();
            summary.setFailureCount(createdCount + updatedCount);
            summary.setSuccessCount(0);
            //todo : username
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("", csvImportFileName, errorFileName, summary);
        } catch (Exception ex) {
            //todo: errorDetail for this error. Can we replace it with a common helper method
            BulkUploadError errorDetail = new BulkUploadError();
            errorDetail.setErrorCategory("General Exception");
            errorDetail.setErrorDescription(ex.getMessage());
            errorDetail.setRecordDetails("");
            bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        }

    }

    private void processLanguageLocationCodeCsvRecords(Map<String, Object> params, String errorFileName) {
        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary(0,0);

        String csvImportFileName = (String)params.get("csv-import.filename");

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        int successCount = 0;
        int failureCount = 0;
        LanguageLocationCodeCsv record = null;
            for (Long id : createdIds) {
                try {
                    record = languageLocationCodeServiceCsv.findById(id);
                    if (record != null) {
                        LanguageLocationCode newRecord = mapLanguageLocationCodeFrom(record);
                        languageLocationCodeService.create(newRecord);
                        languageLocationCodeServiceCsv.delete(record);
                        successCount++;
                    }
                } catch (DataValidationException ex) {
                    failureCount++;
                    errorDetail.setErrorCategory(ex.getErrorCode());
                    errorDetail.setErrorDescription(String.format(
                            ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
                    errorDetail.setRecordDetails(record.toString());
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);

                } catch (Exception ex) {
                    failureCount++;
                    errorDetail.setRecordDetails("");
                    errorDetail.setErrorCategory("General Exception");
                    errorDetail.setErrorDescription(ex.getMessage());
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                }
            }
        summary.setFailureCount(failureCount);
        summary.setSuccessCount(successCount);
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

    private LanguageLocationCode mapLanguageLocationCodeFrom(LanguageLocationCodeCsv record) throws DataValidationException{
        LanguageLocationCode newRecord = new LanguageLocationCode();

        Long stateId = ParseDataHelper.parseLong("stateId", record.getStateId(), true);
        Circle circle = circleService.getCircleByCode(ParseDataHelper.parseString("circleId", record.getCircleId(), true));
        State state = locationService.getStateByCode(stateId);
        District district = locationService.getDistrictByCode(stateId,ParseDataHelper.parseLong("districtId", record.getDistrictId(), true));

        if(circle == null) {
            invalidDataException("circleId", record.getCircleId());
        }
        if(state == null) {
            invalidDataException("stateId", stateId.toString());
        }

        if(district == null) {
            invalidDataException("districtId", record.getDistrictId());
        }
            newRecord.setCircleId(circle);
            newRecord.setStateId(state);
            newRecord.setDistrictId(district);

                newRecord.setDefaultLanguageLocationCodeKK(ParseDataHelper.parseInt("DefaultLanguageLocationCodeKK", record.getDefaultLanguageLocationCodeKK(), true));
                newRecord.setLanguageLocationCodeKK(ParseDataHelper.parseInt("LanguageLocationCodeKK", record.getLanguageLocationCodeKK(), true));
                newRecord.setLanguageKK(ParseDataHelper.parseString("LanguageKK", record.getLanguageKK(), true));

                newRecord.setDefaultLanguageLocationCodeMK(ParseDataHelper.parseInt("DefaultLanguageLocationCodeMK", record.getDefaultLanguageLocationCodeMK(), true));
                newRecord.setLanguageLocationCodeMK(ParseDataHelper.parseInt("LanguageLocationCodeMK", record.getLanguageLocationCodeMK(), true));
                newRecord.setLanguageMK(ParseDataHelper.parseString("LanguageMK", record.getLanguageMK(), true));

                newRecord.setDefaultLanguageLocationCodeKK(ParseDataHelper.parseInt("DefaultLanguageLocationCodeMA", record.getDefaultLanguageLocationCodeMA(), true));
                newRecord.setLanguageLocationCodeMA(ParseDataHelper.parseInt("LanguageLocationCodeMA", record.getLanguageLocationCodeMA(), true));
                newRecord.setLanguageMA(ParseDataHelper.parseString("LanguageMA", record.getLanguageMA(), true));
            return newRecord;
    }

    private void invalidDataException(String fieldName, String fieldValue) throws DataValidationException {
        throw new DataValidationException(
            String.format(DataValidationException.INVALID_FORMAT_MESSAGE, fieldValue),
            ErrorCategoryConstants.INVALID_DATA, fieldName);
    }
}

