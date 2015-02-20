package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mds.ex.MdsException;
import org.motechproject.nms.masterdata.constants.ErrorDescriptionConstant;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeServiceCsv;
import org.motechproject.nms.util.BulkUploadErrRecordDetails;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LanguageLocationCodeCsvHandler {

    private static Integer successCount = 0;
    private static Integer failCount = 0;

    BulkUploadErrRecordDetails errorDetail = new BulkUploadErrRecordDetails();

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
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, successCount, failCount);
        } catch (Exception ex) {
        }
    }


    @MotechListener(subjects = "mds.crud.masterdatamodule.LanguageLocationCodeCsv.csv-import.failure")
    public void languageLocationCodeCsvFailure(MotechEvent motechEvent) {
        String errorFileName = "LanguageLocationCodeCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();
            List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

            languageLocationCodeServiceCsv.deleteAll();
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(errorFileName, 0, createdIds.size());

        } catch (Exception ex) {
        }

    }

    private void processLanguageLocationCodeCsvRecords(Map<String, Object> params, String errorFileName) {
        List<Long> updatedIds = (ArrayList<Long>) params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        for (Long id : updatedIds) {
            LanguageLocationCodeCsv record = languageLocationCodeServiceCsv.findById(id);
                if (record != null) {
                    LanguageLocationCode newRecord = mapLanguageLocationCodeFrom(record);
                    languageLocationCodeService.update(newRecord);
                    languageLocationCodeServiceCsv.delete(record);
                    successCount++;
                } else {
                    logErrorRecord(errorFileName, record.getIndex());
                }
        }

        for (Long id : createdIds) {
            LanguageLocationCodeCsv record = languageLocationCodeServiceCsv.findById(id);
                if (record != null) {
                    LanguageLocationCode newRecord = mapLanguageLocationCodeFrom(record);
                    languageLocationCodeService.create(newRecord);
                    languageLocationCodeServiceCsv.delete(record);
                    successCount++;
                } else {
                    logErrorRecord(errorFileName, record.getIndex());
                }
        }
    }

    private void logErrorRecord(String errorFileName, Long index) {
        errorDetail.setErrorCode("Record not found in database");
        errorDetail.setRecordDetails(ErrorDescriptionConstant.RECORD_UPLOAD_ERROR_DETAIL.format(index.toString()));
        errorDetail.setErrorDescription("Record not found in database");
        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
        failCount++;
    }

    private LanguageLocationCode mapLanguageLocationCodeFrom(LanguageLocationCodeCsv record) {
        LanguageLocationCode newRecord = new LanguageLocationCode();
        try {
            newRecord.setCircleId(ParseDataHelper.parseString("circleId", record.getCircleId(), true));
            newRecord.setDistrictId(ParseDataHelper.parseInt("districtId", record.getDistrictId(), true));
            newRecord.setStateId(ParseDataHelper.parseInt("stateId", record.getStateId(), true));

            if (record.getIsDeployedKK().equals("true")) {
                newRecord.setDefaultLanguageLocationCodeKK(ParseDataHelper.parseBoolean("DefaultLanguageLocationCodeKK", record.getIsDefaultLanguageLocationCodeKK(), true));
                newRecord.setLanguageLocationCodeKK(ParseDataHelper.parseInt("LanguageLocationCodeKK", record.getLanguageLocationCodeKK(), true));
                newRecord.setLanguageKK(ParseDataHelper.parseString("LanguageKK", record.getLanguageKK(), true));
            }

            if (record.getIsDeployedMK().equals("true")) {
                newRecord.setDefaultLanguageLocationCodeMK(ParseDataHelper.parseBoolean("DefaultLanguageLocationCodeMK", record.getIsDefaultLanguageLocationCodeMK(), true));
                newRecord.setLanguageLocationCodeMK(ParseDataHelper.parseInt("LanguageLocationCodeMK", record.getLanguageLocationCodeMK(), true));
                newRecord.setLanguageMK(ParseDataHelper.parseString("LanguageMK", record.getLanguageMK(), true));
            }

            if (record.getIsDeployedMA().equals("true")) {
                newRecord.setDefaultLanguageLocationCodeKK(ParseDataHelper.parseBoolean("DefaultLanguageLocationCodeMA", record.getIsDefaultLanguageLocationCodeMA(), true));
                newRecord.setLanguageLocationCodeMA(ParseDataHelper.parseInt("LanguageLocationCodeMA", record.getLanguageLocationCodeMA(), true));
                newRecord.setLanguageMA(ParseDataHelper.parseString("LanguageMA", record.getLanguageMA(), true));
            }
        }catch (DataValidationException ex) {

        }
            return newRecord;
    }
}

