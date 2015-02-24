package org.motechproject.nms.frontlineworker.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.constants.FrontLineWorkerConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.frontlineworker.repository.FlwRecordDataService;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.OperationType;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by abhishek on 2/2/15.
 * This class provides Motech Listeners for Front Line Worker upload for both success and failure scenarios.
 * This class also provides methods used to validate csv data and save the data in Motech database in case of success
 * and raise exceptions in case of failure
 */

@Component
public class FlwUploadHandler {
    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private Integer successCount = 0;
    private Integer failCount = 0;

    @Autowired
    private FlwRecordDataService flwRecordDataService;

    @Autowired
    private FlwCsvRecordsDataService flwCsvRecordsDataService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private FrontLineWorker frontLineWorker;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    private BulkUploadError errorDetails;

    private static Logger logger = LoggerFactory.getLogger(FlwUploadHandler.class);


    /**
     * This method provides a listener to the Front Line Worker upload success scenario.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {FrontLineWorkerConstants.FLW_UPLOAD_SUCCESS})
    public void flwDataHandlerSuccess(MotechEvent motechEvent) {
        logger.info("Success[flwDataHandlerSuccess] method start for FrontLineWorkerCsv");
        Map<String, Object> params = motechEvent.getParameters();
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);

        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);

        logger.info("Processing Csv file");
        CsvProcessingSummary summary = new CsvProcessingSummary(successCount, failCount);
        List<Long> createdIds = (ArrayList<Long>) params.get(CSV_IMPORT_CREATED_IDS);
        FrontLineWorkerContent frontLineWorkerContent;
        FrontLineWorkerCsv record = null;

        for (Long id : createdIds) {
            try {
                logger.debug("Processing uploaded id : {}", id);
                record = flwCsvRecordsDataService.findById(id);
                if (record != null) {
                    logger.info("Record found in Csv database");
                    frontLineWorker = null;
                    frontLineWorkerContent = null;
                    frontLineWorkerContent = validateFrontLineWorker(record);
                    frontLineWorker = mapFrontLineWorkerFrom(record, frontLineWorkerContent);

                    FrontLineWorker dbRecord = checkExistenceOfFlw(frontLineWorker.getFlwId(), frontLineWorker.getStateCode(), frontLineWorker.getContactNo());

                    if (dbRecord == null) {
                        if (OperationType.DEL.toString().equals(record.getOperation())) {
                            summary.incrementFailureCount();
                            setErrorDetails(record.toString(), ErrorCategoryConstants.INVALID_DATA, ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION);
                            bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                            logger.warn("Record to be deleted with ID : {} not present", id);
                        } else {
                            flwRecordDataService.create(frontLineWorker);
                            flwCsvRecordsDataService.delete(record);
                            summary.incrementSuccessCount();
                            logger.info("Successful creation of new front line worker");
                        }

                    } else {
                        boolean valid = ParseDataHelper.parseBoolean("isValid", record.getIsValid(), false);
                        if (dbRecord.getStatus() == Status.INVALID && valid) {
                            summary.incrementFailureCount();
                            setErrorDetails(record.toString(), ErrorCategoryConstants.INCONSISTENT_DATA, ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION);
                            bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                            logger.warn("Status change try from invalid to valid for id : {}", id);
                        } else {
                            flwRecordDataService.update(frontLineWorker);
                            flwCsvRecordsDataService.delete(record);
                            summary.incrementSuccessCount();
                            logger.info("Record updated successfully for Flw. Searched By: Contact number");
                        }
                    }
                }
            }catch (DataValidationException dve) {
                setErrorDetails(record.toString(), dve.getErrorCode(), dve.getErrorDesc());
                summary.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
                logger.warn("Record not found for uploaded ID: {}", id);
            } catch (Exception e) {
                summary.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
            }
        }
        logger.info("Success[flwDataHandlerSuccess] method finished for FrontLineWorkerCsv");
    }


    /**
     * This method provides a listener to the Front Line Worker upload failure scenario.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {FrontLineWorkerConstants.FLW_UPLOAD_FAILED})
    public void flwDataHandlerFailure(MotechEvent motechEvent) {
        logger.info("Failure[flwDataHandlerFailure] method start for FrontLineWorkerCsv");
        Map<String, Object> params = motechEvent.getParameters();
        CsvProcessingSummary summary = new CsvProcessingSummary(successCount, failCount);
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);

        String logFile = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        for (Long id : createdIds) {
            try {
                logger.info("Processing uploaded ID : {}", id);
                FrontLineWorkerCsv record = flwCsvRecordsDataService.findById(id);
                flwCsvRecordsDataService.delete(record);
                summary.incrementFailureCount();
                setErrorDetails(record.toString(), "Upload failure", "Front Line Worker Upload Failure");
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
            } catch (Exception ex) {
                summary.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(logFile, errorDetails);
            }
        }
        logger.info("Failure[flwDataHandlerFailure] method finished for FrontLineWorkerCsv");
    }


    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param record the Front Line Worker record from Csv that is to be validated
     * @return temporaryFrontLineWorker generated after applying validations.
     * @throws DataValidationException
     */
    private FrontLineWorkerContent validateFrontLineWorker(FrontLineWorkerCsv record) throws DataValidationException {

        String contactNo;
        String finalContactNo;
        String designation;
        int contactNoLength;
        State state;
        District district;

        FrontLineWorkerContent frontLineWorkerContent = null;

        logger.info("validateFrontLineWorker process start");
        frontLineWorkerContent.setStateCode(ParseDataHelper.parseLong("StateCode", record.getStateCode(), true));
        frontLineWorkerContent.setDistrictCode(ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true));


        state = locationService.getStateByCode(frontLineWorkerContent.getStateCode());
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", null);
        }
        frontLineWorkerContent.setState(state);
        district = locationService.getDistrictByCode(frontLineWorkerContent.getState().getId(), frontLineWorkerContent.getDistrictCode());
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", null);
        }

        frontLineWorkerContent.setDistrict(district);
        frontLineWorkerContent.setTaluka(talukaConsistencyCheck(frontLineWorkerContent.getDistrict().getId(), record.getTalukaCode()));
        frontLineWorkerContent.setVillage(villageConsistencyCheck(frontLineWorkerContent.getTaluka().getId(), record.getVillageCode()));
        frontLineWorkerContent.setHealthBlock(healthBlockConsistencyCheck(frontLineWorkerContent.getTaluka().getId(), record.getHealthBlockCode()));
        frontLineWorkerContent.setHealthFacility(healthFacilityConsistencyCheck(frontLineWorkerContent.getHealthBlock().getId(), record.getPhcCode()));
        frontLineWorkerContent.setHealthSubFacility(healthSubFacilityConsistencyCheck(frontLineWorkerContent.getHealthBlock().getId(), record.getSubCentreCode()));

        contactNo = ParseDataHelper.parseString("Contact Number", record.getContactNo(), true);
        contactNoLength = contactNo.length();
        finalContactNo = (contactNoLength > FrontLineWorkerConstants.FLW_CONTACT_NUMBER_LENGTH ? contactNo.substring(contactNoLength - FrontLineWorkerConstants.FLW_CONTACT_NUMBER_LENGTH) : contactNo);
        frontLineWorkerContent.setContactNo(finalContactNo);

        designation = ParseDataHelper.parseString("Type", record.getType(), true);

        if (Designation.of(designation) != Designation.ANM || Designation.of(designation) != Designation.AWW ||
                Designation.of(designation) != Designation.ASHA || Designation.of(designation) != Designation.USHA) {
            ParseDataHelper.raiseInvalidDataException("Content Type", "Invalid");
        }
        logger.info("validateFrontLineWorker process end");
        return frontLineWorkerContent;

    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param record                   the Front Line Worker record from Csv
     * @param frontLineWorkerContent temporary flw record that is to be stored to database
     * @return the Front Line Worker generated.
     * @throws DataValidationException
     */
    private FrontLineWorker mapFrontLineWorkerFrom(FrontLineWorkerCsv record, FrontLineWorkerContent frontLineWorkerContent) throws DataValidationException {

        logger.info("mapFrontLineWorkerFrom process start");
        frontLineWorker.setContactNo(frontLineWorkerContent.getContactNo());

        frontLineWorker.setName(ParseDataHelper.parseString("Name", record.getName(), true));
        frontLineWorker.setDesignation(ParseDataHelper.parseString("Type", record.getType(), true));

        frontLineWorker.setStateCode(frontLineWorkerContent.getStateCode());
        frontLineWorker.setStateId(frontLineWorkerContent.getState());
        frontLineWorker.setDistrictId(frontLineWorkerContent.getDistrict());
        frontLineWorker.setTalukaId(frontLineWorkerContent.getTaluka());
        frontLineWorker.setVillageId(frontLineWorkerContent.getVillage());
        frontLineWorker.setHealthBlockId(frontLineWorkerContent.getHealthBlock());
        frontLineWorker.setHealthFacilityId(frontLineWorkerContent.getHealthFacility());
        frontLineWorker.setHealthSubFacilityId(frontLineWorkerContent.getHealthSubFacility());

        frontLineWorker.setFlwId(ParseDataHelper.parseLong("Flw Id", record.getFlwId(), false));
        frontLineWorker.setAshaNumber(ParseDataHelper.parseString("Asha Number", record.getAshaNumber(), false));
        frontLineWorker.setAdhaarNumber(ParseDataHelper.parseString("Adhaar Number", record.getAdhaarNo(), false));

        frontLineWorker.setLanguageLocationCodeId(languageLocationCodeService.getRecordByLocationCode(frontLineWorker.getStateCode(),
                frontLineWorker.getDistrictId().getDistrictCode()).getId());

        frontLineWorker.setStatus(Status.INACTIVE);
        frontLineWorker.setOperatorId(null);
        frontLineWorker.setCreator(record.getCreator());
        frontLineWorker.setModifiedBy(record.getModifiedBy());
        frontLineWorker.setOwner(record.getOwner());

        logger.info("mapFrontLineWorkerFrom process end");
        return frontLineWorker;
    }


    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param districtId Id of parent district
     * @param record     value of taluka code
     * @return null if optional Taluka is not provided and its value is null/empty, else Taluka which is generated
     * from the parameters
     * @throws DataValidationException
     */
    private Taluka talukaConsistencyCheck(Long districtId, String record) throws DataValidationException {
        String talukaCode;
        Taluka taluka = null;
        talukaCode = ParseDataHelper.parseString("TalukaCode", record, false);
        if (talukaCode != null) {
            taluka = locationService.getTalukaByCode(districtId, talukaCode);
            if (taluka == null) {
                logger.warn("Record not found for Taluka ID[{}]", talukaCode);
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
     * @param record   value of Village code
     * @return null if optional Village is not provided and its value is null/empty, else Village which is generated
     * from the parameters
     * @throws DataValidationException
     */
    private Village villageConsistencyCheck(Long talukaId, String record) throws DataValidationException {

        Long villageCode;
        Village village = null;
        villageCode = ParseDataHelper.parseLong("VillageCode", record, false);
        if (villageCode != null) {
            if (talukaId != null) {
                village = locationService.getVillageByCode(talukaId, villageCode);
                if (village == null) {
                    logger.warn("Record not found for Village ID[{}]", villageCode);
                    ParseDataHelper.raiseInvalidDataException("Village", record);
                }
            } else {
                logger.warn("Village ID[{}] present withour Taluka", villageCode);
                ParseDataHelper.raiseInvalidDataException("Village", record);
            }
        }
        return village;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param talukaId Id of parent taluka
     * @param record   value of HealthBlock code
     * @return null if optional HealthBlock is not provided and its value is null/empty, else HealthBlock which is
     * generated from the parameters
     * @throws DataValidationException
     */
    private HealthBlock healthBlockConsistencyCheck(Long talukaId, String record) throws DataValidationException {

        Long healthclockCode;
        HealthBlock healthBlock = null;

        healthclockCode = ParseDataHelper.parseLong("HealthBlockCode", record, false);
        if (healthclockCode != null) {
            if (talukaId != null) {
                healthBlock = locationService.getHealthBlockByCode(talukaId, healthclockCode);
                if (healthBlock == null) {
                    logger.warn("Record not found for HealthBlock ID[{}]", healthclockCode);
                    ParseDataHelper.raiseInvalidDataException("HealthBlock", record);
                }
            } else {
                logger.warn("HealthBlock ID[{}] present withour Taluka", healthclockCode);
                ParseDataHelper.raiseInvalidDataException("HealthBlock", record);
            }
        }

        return healthBlock;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param healthBlockId Id of parent HelathBlock
     * @param record        value of HealthFacility code
     * @return null if optional HealthFacility is not provided and its value is null/empty, else HealthFacility which is
     * generated from the parameters
     * @throws DataValidationException
     */
    private HealthFacility healthFacilityConsistencyCheck(Long healthBlockId, String record) throws DataValidationException {

        Long healthFacilityCode;
        HealthFacility healthFacility = null;

        healthFacilityCode = ParseDataHelper.parseLong("HealthFacilityCode", record, false);
        if (healthFacilityCode != null) {
            if (healthBlockId != null) {
                healthFacility = locationService.getHealthFacilityByCode(healthBlockId, healthFacilityCode);
                if (healthFacility == null) {
                    logger.warn("Record not found for HealthFacility ID[{}]", healthFacilityCode);
                    ParseDataHelper.raiseInvalidDataException("HealthFacility", record);
                }
            } else {
                logger.warn("HealthFacility ID[{}] present withour HealthBlock", healthFacilityCode);
                ParseDataHelper.raiseInvalidDataException("HealthFacility", record);
            }
        }

        return healthFacility;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param healthFacilityId Id of parent HelathBlock
     * @param record           value of HealthSubFacility code
     * @return null if optional HealthSubFacility is not provided and its value is null/empty, else HealthSubFacility
     * which is generated from the parameters
     * @throws DataValidationException
     */
    private HealthSubFacility healthSubFacilityConsistencyCheck(Long healthFacilityId, String record) throws DataValidationException {
        Long healthSubFacilityCode;
        HealthSubFacility healthSubFacility = null;

        healthSubFacilityCode = ParseDataHelper.parseLong("HealthSubFacilityCode", record, false);
        if (healthSubFacilityCode != null) {
            if (healthFacilityId != null) {
                healthSubFacility = locationService.getHealthSubFacilityByCode(healthFacilityId, healthSubFacilityCode);
                if (healthSubFacility == null) {
                    logger.warn("Record not found for HealthSubFacility ID[{}]", healthSubFacilityCode);
                    ParseDataHelper.raiseInvalidDataException("HealthSubFacility", record);
                }
            } else {
                logger.warn("HealthSubFacility ID[{}] present withour HealthFacility", healthSubFacilityCode);
                ParseDataHelper.raiseInvalidDataException("HealthSubFacility", record);
            }
        }

        return healthSubFacility;
    }

    /**
     * This method is used to set error record details
     *
     * @param id               record for which error is generated
     * @param errorCategory    specifies error category
     * @param errorDescription specifies error descriotion
     */
    private void setErrorDetails(String id, String errorCategory, String errorDescription) {
        errorDetails.setRecordDetails(id);
        errorDetails.setErrorCategory(errorCategory);
        errorDetails.setErrorDescription(errorDescription);
    }

    /**
     * This method is used to set error record details
     *
     * @param flwId FlwId for which db record is to be found
     * @param stateCode specifies the state code from the Csv record
     * @param contactNo specifies the contact no from the Csv record
     * @return null if thier is no db record for given FlwId else the record generated from db
     * @throws DataValidationException
     */
    private FrontLineWorker checkExistenceOfFlw( Long flwId, Long stateCode, String contactNo) throws DataValidationException {
        logger.debug("FLW state Code : {}", stateCode);
        FrontLineWorker dbRecord = flwRecordDataService.getFlwByFlwIdAndStateId(flwId, stateCode);
        if(dbRecord == null) {
            logger.debug("FLW Contact Number : {}", contactNo);
            dbRecord = flwRecordDataService.getFlwByContactNo(frontLineWorker.getContactNo());
        }

        return dbRecord;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }
}

