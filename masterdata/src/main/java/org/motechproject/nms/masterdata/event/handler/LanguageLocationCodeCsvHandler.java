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
        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>)params.get("csv-import.created_ids");
        List<Long> updatedIds = (ArrayList<Long>)params.get("csv-import.updated_ids");

        for(Long id : createdIds) {
            languageLocationCodeServiceCsv.delete(languageLocationCodeServiceCsv.findById(id));
        }

        for(Long id : updatedIds) {
            languageLocationCodeServiceCsv.delete(languageLocationCodeServiceCsv.findById(id));
        }
    }

    private void processLanguageLocationCodeCsvRecords(Map<String, Object> params, String errorFileName) {
        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary(0,0);

        String csvImportFileName = (String)params.get("csv-import.filename");

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        int successCount = 0;
        int failureCount = 0;
        String userName = null;
        LanguageLocationCodeCsv record = null;
            for (Long id : createdIds) {
                try {
                    record = languageLocationCodeServiceCsv.findById(id);
                    if (record != null) {
                        userName = record.getOwner();
                        LanguageLocationCode newRecord = mapLanguageLocationCodeFrom(record);

                        LanguageLocationCode oldRecord = languageLocationCodeService.getLanguageLocationCodeRecord(
                                Long.parseLong(record.getStateId()), Long.parseLong(record.getDistrictId()));
                        if(oldRecord == null) {
                            languageLocationCodeService.create(newRecord);
                        }else {
                            languageLocationCodeService.update(newRecord);
                        }
                        languageLocationCodeServiceCsv.delete(record);
                        successCount++;
                    } else {
                        failureCount++;
                        errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                        errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                        errorDetail.setRecordDetails("Record is null");
                        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                    }
                } catch (DataValidationException ex) {
                    failureCount++;
                    errorDetail.setErrorCategory(ex.getErrorCode());
                    errorDetail.setErrorDescription(String.format(
                            ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
                    errorDetail.setRecordDetails(record.toString());
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                }
            }
        summary.setFailureCount(failureCount);
        summary.setSuccessCount(successCount);
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvImportFileName, errorFileName, summary);
    }

    private LanguageLocationCode mapLanguageLocationCodeFrom(LanguageLocationCodeCsv record) throws DataValidationException{
        LanguageLocationCode newRecord = new LanguageLocationCode();

        newRecord.setStateCode(ParseDataHelper.parseInt("StateCode", record.getStateId(), true));
        newRecord.setDistrictCode(ParseDataHelper.parseInt("DistrictCode", record.getDistrictId(), true));
        newRecord.setCircleCode(ParseDataHelper.parseString("CircleCode", record.getCircleId(), true));
        
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

