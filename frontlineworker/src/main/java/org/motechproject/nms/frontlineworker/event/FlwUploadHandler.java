package org.motechproject.nms.frontlineworker.event;

/**
 * Created by abhishek on 2/2/15.
 */


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.FrontLineWorkerConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.frontlineworker.repository.FlwRecordDataService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class FlwUploadHandler {

    public static Integer successCount = 0;
    public static Integer failCount = 0;

    @Autowired
    private FlwRecordDataService flwRecordDataService;

    @Autowired
    private FlwCsvRecordsDataService flwCsvRecordsDataService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(FlwUploadHandler.class);


    @MotechListener(subjects = {FrontLineWorkerConstants.FLW_UPLOAD_SUCCESS})
    public void flwDataHandler(MotechEvent motechEvent) {
        logger.error("entered frontLineWorkerSuccess");
        String errorFileName = "FrontLineWorkerCsv_" + new Date().toString();
           try {
            Map<String, Object> params = motechEvent.getParameters();
               CsvProcessingSummary result = processCsvRecords(params);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("etasha","FrontLineWorkerCsv",errorFileName, result);
        } catch (Exception ex) {
        }
    }

    @MotechListener(subjects = {FrontLineWorkerConstants.FLW_UPLOAD_FAILED})
    public void frontLineWorkerFailure(MotechEvent motechEvent) {
        logger.error("entered frontLineWorkerFailed");

        String errorFileName = "FrontLineWorkerCsv_" + new Date().toString();
        try {
            Map<String, Object> params = motechEvent.getParameters();
            CsvProcessingSummary result = processCsvRecordsFailure(params);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary("etasha","FrontLineWorkerCsv", errorFileName, result);
        } catch (Exception ex) {
        }
    }

    private CsvProcessingSummary processCsvRecords(Map<String, Object> params) {
        String entityName = (String) params.get("entityName");

        CsvProcessingSummary result = new CsvProcessingSummary(successCount,failCount);
        List<Long> updatedIds = (ArrayList<Long>) params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            FrontLineWorkerCsv record = flwCsvRecordsDataService.findById(id);
            BulkUploadError error = validateFrontLineWorkerCsv(record);
            if (error == null) {
                FrontLineWorker newRecord = mapFrontLineWorker(record);
                flwRecordDataService.create(newRecord);
                flwCsvRecordsDataService.delete(record);
                result.incrementSuccessCount();
            } else {
                result.incrementFailureCount();

            }
        }
        return result;
    }

    private CsvProcessingSummary processCsvRecordsFailure(Map<String, Object> params) {
        CsvProcessingSummary result = new CsvProcessingSummary(successCount,failCount);
        List<Long> updatedIds = (ArrayList<Long>) params.get("csv-import.updated_ids");
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        for(Long id : createdIds) {
            FrontLineWorkerCsv record =  flwCsvRecordsDataService.findById(id);
            flwCsvRecordsDataService.delete(record);
            result.incrementFailureCount();
        }

        for(Long id : updatedIds) {
            FrontLineWorkerCsv record =  flwCsvRecordsDataService.findById(id);
            flwCsvRecordsDataService.delete(record);
            result.incrementFailureCount();
        }
        return result;
    }

    private FrontLineWorker mapFrontLineWorker(FrontLineWorkerCsv record) {
        FrontLineWorker newRecord = new FrontLineWorker();
        newRecord.setState(null);
        newRecord.setDistrict(null);
        newRecord.setLanguageLocationCode(null);
        newRecord.setOperatorId(1);
        newRecord.setStatus("Active");
        newRecord.setContactNo(record.getContactNo());
        newRecord.setName(record.getName());
        newRecord.setDesignation(record.getType());

        return newRecord;
    }

    private BulkUploadError validateFrontLineWorkerCsv(FrontLineWorkerCsv record) {
        BulkUploadError errorRecord = new BulkUploadError();

/*        if (ParseDataHelper.isNullOrEmpty(record.getStateId())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(FrontLineWorkerErrorConstants.RECORD_UPLOAD_ERROR_DESC.format("State"));
            return errorRecord;
        }*/

/*        if (ParseDataHelper.isNullOrEmpty(record.getDistrictCode())) {
            errorRecord.setErrorCode(ErrorCodeConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(FrontLineWorkerErrorConstants.RECORD_UPLOAD_ERROR_DESC.format("District"));
            return errorRecord;
        }*/

        if (ParseDataHelper.isNullOrEmpty((record.getContactNo()))) {
            errorRecord.setErrorCategory(ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION.format("Conatct No"));
        }

        if (ParseDataHelper.isNullOrEmpty((record.getType()))) {
            errorRecord.setErrorCategory(ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION.format("Designation"));
        }

        if (ParseDataHelper.isNullOrEmpty(record.getName())) {
            errorRecord.setErrorCategory(ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
            errorRecord.setErrorDescription(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION.format("name"));
            return errorRecord;
        }

/*        if (!(ParseDataHelper.isNullOrEmpty(record.getVillageCode())) && ParseDataHelper.isNullOrEmpty(record.getTalukaCode())){
            errorRecord.setErrorCode(ErrorCodeConstants.INVALID_DATA);
            errorRecord.setErrorDescription((FrontLineWorkerErrorConstants.RECORD_PARENT_MISSING_ERROR_DESC.format("Taluka", "Village")));

        }*/

/*        if (!ParseDataHelper.isNullOrEmpty(record.getVillageCode()) && ParseDataHelper.isNullOrEmpty(record.getTalukaCode())){
            errorRecord.setErrorCode(ErrorCodeConstants.INVALID_DATA);
            errorRecord.setErrorDescription((FrontLineWorkerErrorConstants.RECORD_PARENT_MISSING_ERROR_DESC.format("Taluka", "HealthBlock")));

        }*/

/*        if (!ParseDataHelper.isNullOrEmpty(record.getVillageCode()) && ParseDataHelper.isNullOrEmpty(record.getTalukaCode())) {
            errorRecord.setErrorCode(ErrorCodeConstants.INVALID_DATA);
            errorRecord.setErrorDescription((FrontLineWorkerErrorConstants.RECORD_PARENT_MISSING_ERROR_DESC.format("HealthBlock", "PHC")));

        }*/
/*
        if (!ParseDataHelper.isNullOrEmpty(record.getVillageCode()) && ParseDataHelper.isNullOrEmpty(record.getTalukaCode())) {
            errorRecord.setErrorCode(ErrorCodeConstants.INVALID_DATA);
            errorRecord.setErrorDescription((FrontLineWorkerErrorConstants.RECORD_PARENT_MISSING_ERROR_DESC.format("PHC", "SubCentre")));

        }*/

        return null;
    }

}

