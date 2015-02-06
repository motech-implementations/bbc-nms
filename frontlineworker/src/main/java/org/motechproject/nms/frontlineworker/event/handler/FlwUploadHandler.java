package org.motechproject.nms.frontlineworker.event.handler;

/**
 * Created by abhishek on 2/2/15.
 */


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.FrontLineWorkerConstants;
import org.motechproject.nms.frontlineworker.FrontLineWorkerErrorConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.frontlineworker.repository.FlwRecordDataService;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.service.LocationService;
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

    @Autowired
    FrontLineWorker frontLineWorker;

    private static Logger logger = LoggerFactory.getLogger(FlwUploadHandler.class);

    @MotechListener(subjects = {FrontLineWorkerConstants.FLW_UPLOAD_SUCCESS})
    public void flwDataHandler(MotechEvent motechEvent) {
        logger.error("entered frontLineWorkerSuccess");
            try {
            Map<String, Object> params = motechEvent.getParameters();
                String logFile = BulkUploadError.createBulkUploadErrLogFileName((String) params.get(""));
                BulkUploadError errorDetails = new BulkUploadError();
                CsvProcessingSummary result = new CsvProcessingSummary(successCount, failCount);
                List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

                try {

                    for (Long id : createdIds) {
                        FrontLineWorkerCsv record = flwCsvRecordsDataService.findById(id);
                        if(record != null)
                        {
                            frontLineWorker = flwCsvtoFlwMapper(record);
                            BulkUploadError error = FlwConsistencyCheck(record);
                            if (error == null) {
                                FrontLineWorker dbRecord = flwRecordDataService.getFlwByFlwIdAndStateId(frontLineWorker.getFlwId(),
                                        frontLineWorker.getStateCode());
                                if (dbRecord == null)
                                {
                                    dbRecord = flwRecordDataService.getFlwByContactNo(frontLineWorker.getContactNo());
                                    {
                                        if(dbRecord == null)
                                        {

                                            flwRecordDataService.create(frontLineWorker);
                                            flwCsvRecordsDataService.delete(record);
                                            result.incrementSuccessCount();
                                        }

                                        else {
                                            flwRecordDataService.update(frontLineWorker);
                                            flwCsvRecordsDataService.delete(record);
                                            result.incrementSuccessCount();
                                        }
                                    }
                                }

                                else {
                                    flwRecordDataService.update(frontLineWorker);
                                    flwCsvRecordsDataService.delete(record);
                                    result.incrementSuccessCount();
                                }

                            } else {
                                result.incrementFailureCount();

                            }
                        }
                    } catch (DataValidationException ex) {
                        errorDetail.setErrorCategory(ex.getErrorCode());
                        errorDetail.setRecordDetails(frontLineWorker.toString());
                        errorDetail.setErrorDescription(String.format(
                                ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, ex.getErroneousField()));
                        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);

                    }catch (Exception ex) {
                        errorDetail.setErrorDescription("Record not found in the database");
                        errorDetail.setErrorCategory("");
                        errorDetail.setRecordDetails("Record is null");
                        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);

                    }
                    return result;
                        }






                CsvProcessingSummary result = processCsvRecords(params, errorFileName);
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


    private CsvProcessingSummary processCsvRecordsFailure(Map<String, Object> params) {
        CsvProcessingSummary result = new CsvProcessingSummary(successCount,failCount);
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        for(Long id : createdIds) {
            FrontLineWorkerCsv record =  flwCsvRecordsDataService.findById(id);
            flwCsvRecordsDataService.delete(record);
            result.incrementFailureCount();
        }

        return result;
    }

    private FrontLineWorker flwCsvtoFlwMapper(FrontLineWorkerCsv record)  throws DataValidationException{
        BulkUploadError errorRecord = new BulkUploadError();
        LocationService locationService = new LocationService();


        Long stateCode;
        Long districtCode;
        String talukaCode;
        Long villageCode;
        Long healthBlockCode;
        Long healthFacilityCode;
        Long healthSubFacilityCode;


        State state;
        District district;

        stateCode = ParseDataHelper.parseLong("State", record.getStateCode(), true);
        districtCode = ParseDataHelper.parseLong("District", record.getDistrictCode(), true);
        talukaCode = ParseDataHelper.parseString("Taluka", record.getTalukaCode(), false);
        villageCode = ParseDataHelper.parseLong("Village ", record.getVillageCode(), false);
        healthBlockCode = ParseDataHelper.parseLong("Health Block", record.getHealthBlockCode(), false);
        healthFacilityCode = ParseDataHelper.parseLong("Health Facility", record.getPhcCode(), false);
        healthSubFacilityCode = ParseDataHelper.parseLong("Health Sub Facility", record.getSubCentreCode(), false);


        
        frontLineWorker.setDistrictId(ParseDataHelper.parseLong("District", record.getDistrictCode(), true));
        frontLineWorker.setContactNo(ParseDataHelper.parseString("Contact Number", record.getContactNo(), true));
        frontLineWorker.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        frontLineWorker.setDesignation(ParseDataHelper.parseString("Type",record.getType(), true));
        state = locationService.getStateByCode(frontLineWorker.getStateCode());
        district = locationService.getDistrictByCode(state.getId(), )



        frontLineWorker.setFlwId(ParseDataHelper.parseLong("Flw Id", record.getFlwId(), false));
        frontLineWorker.setTalukaCode(ParseDataHelper.parseString("Taluka Code", record.getTalukaCode(),false));
        frontLineWorker.setHealthBlock(ParseDataHelper.parseLong("Health Block", record.getHealthBlockCode(), false));
        frontLineWorker.setHealthFacility(ParseDataHelper.parseLong("Health Block Facility", record.getPhcCode(), false));
        frontLineWorker.setHealthSubFacility(ParseDataHelper.parseLong("Health Sub Facility", record.getSubCentreCode(), false));
        frontLineWorker.setVillageCode(ParseDataHelper.parseLong("Village", record.getVillageCode(), false));
        frontLineWorker.setAshaNumber(ParseDataHelper.parseString("Asha Number", record.getAshaNumber(), false));
        frontLineWorker.setValid(ParseDataHelper.parseBoolean("isValid", record.getIsValid(), false));
        frontLineWorker.setValidated(ParseDataHelper.parseBoolean("IsValidated", record.getIsValidated(), false));
        frontLineWorker.setAdhaarNumber(ParseDataHelper.parseString("Adhaar Number", record.getAdhaarNo(), false));

        return frontLineWorker;
    }

    private BulkUploadError FlwConsistencyCheck(FrontLineWorkerCsv record) throws DataValidationException{
        BulkUploadError errorRecord = new BulkUploadError();



            if(frontLineWorker.getTalukaCode() == null)
            {
                if(frontLineWorker.getVillageCode() != null) {
                    errorRecord.setErrorCategory(ErrorCategoryConstants.INVALID_DATA);
                    errorRecord.setErrorDescription((FrontLineWorkerErrorConstants.RECORD_PARENT_MISSING_ERROR_DESC.format("Taluka", "Village")));
                    return errorRecord;
                }

                if(frontLineWorker.getHealthBlock() != null) {
                    errorRecord.setErrorCategory(ErrorCategoryConstants.INVALID_DATA);
                    errorRecord.setErrorDescription((FrontLineWorkerErrorConstants.RECORD_PARENT_MISSING_ERROR_DESC.format("Taluka", "HealthBlock")));
                    return errorRecord;
                }

            }

        if(frontLineWorker.getHealthBlock()== null)
        {
            if(frontLineWorker.getHealthFacility()!= null) {
                errorRecord.setErrorCategory(ErrorCategoryConstants.INVALID_DATA);
                errorRecord.setErrorDescription((FrontLineWorkerErrorConstants.RECORD_PARENT_MISSING_ERROR_DESC.format("HealthBlock", "PHC")));
                return errorRecord;
            }
        }

        if(frontLineWorker.getHealthFacility()== null)
        {
            if(frontLineWorker.getHealthSubFacility()!= null) {
                errorRecord.setErrorCategory(ErrorCategoryConstants.INVALID_DATA);
                errorRecord.setErrorDescription((FrontLineWorkerErrorConstants.RECORD_PARENT_MISSING_ERROR_DESC.format("PHC", "SubCentre")));
                return errorRecord;
            }
        }



        return null;
    }

}

