package org.motechproject.nms.frontlineworker.event.handler;

/**
 * Created by abhishek on 2/2/15.
 * This class provides Motech Listeners for Front Line Worker upload for both success and failure scenarios.
 * This class also provides methods used to validate csv data and save the data in Motech database in case of success
 * and raise exceptions in case of failure
 */

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.FrontLineWorkerConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.frontlineworker.repository.FlwRecordDataService;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
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

    @Autowired
    LocationService locationService;

    @Autowired
    BulkUploadError errorDetails;

    private static Logger logger = LoggerFactory.getLogger(FlwUploadHandler.class);


    /**
     * This method provides a listerner to the Front Line Worker upload success scenario.
     * @param motechEvent   name of the event raised during upload
     */
    @MotechListener(subjects = {FrontLineWorkerConstants.FLW_UPLOAD_SUCCESS})
    public void flwDataHandler(MotechEvent motechEvent) {
        logger.error("entered frontLineWorkerSuccess");
            try {
            Map<String, Object> params = motechEvent.getParameters();
                String logFile = BulkUploadError.createBulkUploadErrLogFileName((String) params.get(""));

                CsvProcessingSummary result = new CsvProcessingSummary(successCount, failCount);
                List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

                    for (Long id : createdIds) {
                        FrontLineWorkerCsv record = flwCsvRecordsDataService.findById(id);
                        if (record != null) {
                            frontLineWorker = null;
                            frontLineWorker = flwCsvtoFlwMapper(record);
                            if (frontLineWorker != null) {
                                FrontLineWorker dbRecord = flwRecordDataService.getFlwByFlwIdAndStateId(frontLineWorker.getFlwId(),
                                        frontLineWorker.getStateCode());
                                if (dbRecord == null) {
                                    dbRecord = flwRecordDataService.getFlwByContactNo(frontLineWorker.getContactNo());
                                    {
                                        if (dbRecord == null) {
                                            frontLineWorker.setStatus("Inactive");
                                            flwRecordDataService.create(frontLineWorker);
                                            flwCsvRecordsDataService.delete(record);
                                            result.incrementSuccessCount();
                                        } else {

                                            frontLineWorker.setStatus("Active");
                                            flwRecordDataService.update(frontLineWorker);
                                            flwCsvRecordsDataService.delete(record);
                                            result.incrementSuccessCount();
                                        }
                                    }
                                } else {

                                    frontLineWorker.setStatus("Active");
                                    flwRecordDataService.update(frontLineWorker);
                                    flwCsvRecordsDataService.delete(record);
                                    result.incrementSuccessCount();
                                }

                            } else {
                                result.incrementFailureCount();
                                errorDetails.setRecordDetails(id.toString());
                                errorDetails.setErrorCategory("Record_Not_Found");
                                errorDetails.setErrorDescription("Record not found in Csv database");

                            }
                        }
                    }
                }catch (DataValidationException dve) {
                    errorDetails.setErrorCategory("Record_Not_Found");
                }catch (Exception ex) {
                    errorDetails.setErrorCategory("Record_Not_Found");
                }
            }



    /**
     * This method provides a listerner to the Front Line Worker upload failure scenario.
     *
     * @param motechEvent   name of the event raised during upload
     */
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



    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param params
     * @return the summary of the failure operation i.e the number of failed records.
     */
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

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param record the Front Line Worker record fron Csv that is to be validated
     * @return the Front Line Worker generated after applying validations.
     * @throws DataValidationException
     */
    private FrontLineWorker flwCsvtoFlwMapper(FrontLineWorkerCsv record)  throws DataValidationException{
        BulkUploadError errorRecord = new BulkUploadError();

        Long stateCode;
        Long districtCode;
        String contactNo;
        String finalContactNo;
        int contactNoLength;
        State state;
        District district;
        Taluka taluka;
        Village village;
        HealthBlock healthBlock;
        HealthFacility healthFacility;
        HealthSubFacility healthSubFacility;

        stateCode = ParseDataHelper.parseLong("State", record.getStateCode(), true);
        districtCode = ParseDataHelper.parseLong("District", record.getDistrictCode(), true);

        state = locationService.getStateByCode(stateCode);
        if(state == null){
            ParseDataHelper.raiseInvalidDataException("State", null);
        }

        district =locationService.getDistrictByCode(state.getId(), districtCode);
        if(district == null) {
            ParseDataHelper.raiseInvalidDataException("District", null);
        }

        taluka = talukaConsistencyCheck(district.getId(), record.getTalukaCode());
        village = villageConsistencyCheck(taluka.getId(), record.getVillageCode());
        healthBlock = healthBlockConsistencyCheck(taluka.getId(), record.getHealthBlockCode());
        healthFacility = healthFacilityConsistencyCheck(healthBlock.getId(), record.getPhcCode());
        healthSubFacility = healthSubFacilityConsistencyCheck(healthFacility.getId(), record.getSubCentreCode());

        contactNo = ParseDataHelper.parseString("Contact Number", record.getContactNo(), true);
        contactNoLength = contactNo.length();
        finalContactNo = (contactNoLength > 10 ? contactNo.substring(contactNoLength -10) : contactNo);
        frontLineWorker.setContactNo(finalContactNo);

        frontLineWorker.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        frontLineWorker.setDesignation(ParseDataHelper.parseString("Type",record.getType(), true));

        frontLineWorker.setStateCode(stateCode);
        frontLineWorker.setStateId(state);
        frontLineWorker.setDistrictId(district);
        frontLineWorker.setTalukaId(taluka);
        frontLineWorker.setVillageId(village);
        frontLineWorker.setHealthBlockId(healthBlock);
        frontLineWorker.setHealthFacilityId(healthFacility);
        frontLineWorker.setHealthSubFacilityId(healthSubFacility);

        frontLineWorker.setFlwId(ParseDataHelper.parseLong("Flw Id", record.getFlwId(), false));
        frontLineWorker.setAshaNumber(ParseDataHelper.parseString("Asha Number", record.getAshaNumber(), false));
        frontLineWorker.setValid(ParseDataHelper.parseBoolean("isValid", record.getIsValid(), false));
        frontLineWorker.setValidated(ParseDataHelper.parseBoolean("IsValidated", record.getIsValidated(), false));
        frontLineWorker.setAdhaarNumber(ParseDataHelper.parseString("Adhaar Number", record.getAdhaarNo(), false));

        return frontLineWorker;
    }



    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param districtId Id of parent district
     * @param record  value of taluka code
     * @return null if optional Taluka is not provided and its value is null/empty, else Taluka which is generated
     * from the parameters
     * @throws DataValidationException
     */
    private Taluka talukaConsistencyCheck(Long districtId, String record) throws DataValidationException {
        String talukaCode;
        Taluka taluka = null;
        talukaCode = ParseDataHelper.parseString("Taluka", record, false);
        if (talukaCode!=null) {
            taluka = locationService.getTalukaByCode(districtId,talukaCode);
            if(taluka == null){
                ParseDataHelper.raiseInvalidDataException("Taluka", record);

            }
        }
        return taluka;
    }


    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param talukaId Id of parent taluka
     * @param record  value of Village code
     * @return null if optional Village is not provided and its value is null/empty, else Village which is generated
     * from the parameters
     * @throws DataValidationException
     */
    private Village villageConsistencyCheck(Long talukaId, String record) throws DataValidationException {

        Long villageCode;
        Village village = null;
        villageCode = ParseDataHelper.parseLong("Village", record, false);
        if(villageCode != null) {
            if(talukaId != null) {
                village = locationService.getVillageByCode(talukaId, villageCode);
                {
                    if (village == null)
                    {
                        ParseDataHelper.raiseInvalidDataException("Village", record);
                    }
                }
            }
            else {
                ParseDataHelper.raiseInvalidDataException("Village",record);
            }
        }
        return village;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param talukaId Id of parent taluka
     * @param record  value of HealthBlock code
     * @return null if optional HealthBlock is not provided and its value is null/empty, else HealthBlock which is
     * generated from the parameters
     * @throws DataValidationException
     */
    private HealthBlock healthBlockConsistencyCheck(Long talukaId, String record) throws DataValidationException {

        Long healthclockCode;
        HealthBlock healthBlock = null;

        healthclockCode = ParseDataHelper.parseLong("HealthBlock",record, false);
        if(healthclockCode != null) {
            if(talukaId != null) {
                healthBlock = locationService.getHealthBlockByCode(talukaId, healthclockCode);
                {
                    if (healthBlock == null)
                    {
                        ParseDataHelper.raiseInvalidDataException("HealthBlock", record);
                    }
                }
            }
            else {
                ParseDataHelper.raiseInvalidDataException("HealthBlock",record);
            }
        }

        return healthBlock;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param healthBlockId Id of parent HelathBlock
     * @param record  value of HealthFacility code
     * @return null if optional HealthFacility is not provided and its value is null/empty, else HealthFacility which is
     * generated from the parameters
     * @throws DataValidationException
     */
    private HealthFacility healthFacilityConsistencyCheck(Long healthBlockId, String record) throws DataValidationException {

        Long healthFacilityCode;
        HealthFacility healthFacility = null;

        healthFacilityCode = ParseDataHelper.parseLong("HealthFacility",record, false);
        if(healthFacilityCode != null) {
            if(healthBlockId != null) {
                healthFacility = locationService.getHealthFacilityByCode(healthBlockId, healthFacilityCode);
                {
                    if (healthFacility == null)
                    {
                        ParseDataHelper.raiseInvalidDataException("HealthFacility", record);
                    }
                }
            }
            else {
                ParseDataHelper.raiseInvalidDataException("HealthFacility",record);
            }
        }

        return healthFacility;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param healthFacilityId Id of parent HelathBlock
     * @param record  value of HealthSubFacility code
     * @return null if optional HealthSubFacility is not provided and its value is null/empty, else HealthSubFacility
     * which is generated from the parameters
     * @throws DataValidationException
     */
    private HealthSubFacility healthSubFacilityConsistencyCheck(Long healthFacilityId, String record) throws DataValidationException {
        Long healthSubFacilityCode;
        HealthSubFacility healthSubFacility = null;

        healthSubFacilityCode = ParseDataHelper.parseLong("HealthFacility",record, false);
        if(healthSubFacilityCode != null) {
            if(healthFacilityId != null) {
                healthSubFacility = locationService.getHealthSubFacilityByCode(healthFacilityId, healthSubFacilityCode);
                {
                    if (healthSubFacility == null)
                    {
                        ParseDataHelper.raiseInvalidDataException("HealthSubFacility", record);
                    }
                }
            }
            else {
                ParseDataHelper.raiseInvalidDataException("HealthSubFacility",record);
            }
        }

        return healthSubFacility;
    }

}

