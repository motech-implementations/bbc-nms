package org.motechproject.nms.frontlineworker.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.constants.FrontLineWorkerConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerCsvService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsUtils;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class provides Motech Listeners for Front Line Worker upload for both success and failure scenarios.
 * This class also provides methods used to validate csv data and save the data in Motech database in case of success
 * and raise exceptions in case of failure
 */

@Component
public class FlwUploadHandler {

    private BulkUploadErrLogService bulkUploadErrLogService;

    private LocationService locationService;

    private LanguageLocationCodeService languageLocationCodeService;

    private FrontLineWorkerService frontLineWorkerService;

    private FrontLineWorkerCsvService frontLineWorkerCsvService;

    private static final String CSV_IMPORT_PREFIX = "csv-import.";

    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";

    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private static Logger logger = LoggerFactory.getLogger(FlwUploadHandler.class);


    @Autowired
    public FlwUploadHandler(BulkUploadErrLogService bulkUploadErrLogService,
                            LocationService locationService,
                            LanguageLocationCodeService languageLocationCodeService,
                            FrontLineWorkerService frontLineWorkerService,
                            FrontLineWorkerCsvService frontLineWorkerCsvService
                            ) {

        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.locationService = locationService;
        this.languageLocationCodeService = languageLocationCodeService;
        this.frontLineWorkerService = frontLineWorkerService;
        this.frontLineWorkerCsvService = frontLineWorkerCsvService;
    }


    /**
     * This method provides a listener to the Front Line Worker upload success scenario.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {FrontLineWorkerConstants.FLW_UPLOAD_SUCCESS})
    public void flwDataHandlerSuccess(MotechEvent motechEvent) {

        String userName = null;

        logger.info("Success[flwDataHandlerSuccess] method start for FrontLineWorkerCsv");
        Map<String, Object> params = motechEvent.getParameters();
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);

        logger.info("Processing Csv file");

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        List<Long> createdIds = (ArrayList<Long>) params.get(CSV_IMPORT_CREATED_IDS);
        FrontLineWorkerCsv record = null;

        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(NmsUtils.getCurrentTimeStamp());

        //this loop processes each of the entries in the Front Line Worker Csv and performs operation(DEL/ADD/MOD)
        // on the record and also deleted each record after processing from the Csv. If some error occurs in any
        // of the records, it is reported.
        for (Long id : createdIds) {
            try {
                logger.debug("Processing uploaded id : {}", id);
                record = frontLineWorkerCsvService.findByIdInCsv(id);
                if (record != null) {
                    userName = record.getOwner();
                    logger.info("Record found in Csv database");


                    FrontLineWorkerContent frontLineWorkerContent = validateFrontLineWorker(record);
                    FrontLineWorker frontLineWorker = mapFrontLineWorkerFrom(record, frontLineWorkerContent);

                    FrontLineWorker dbRecord = checkExistenceOfFlw(frontLineWorker.getFlwId(), frontLineWorker.getStateCode(), frontLineWorker.getContactNo());
                    Long flw = ParseDataHelper.validateAndParseLong("flwId", record.getFlwId(), false);

                    if (flw == null && dbRecord.getFlwId() != null) {
                        ParseDataHelper.raiseInvalidDataException("FlwId for existing frontlineworker", null);
                    }

                    if (dbRecord == null) {

                        logger.info("New front line worker creation starts");
                        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
                        bulkUploadStatus.incrementSuccessCount();
                        logger.info("Successful creation of new front line worker");

                    } else {
                        Boolean valid = ParseDataHelper.validateAndParseBoolean("isValid", record.getIsValid(), false);
                        Status status = dbRecord.getStatus();
                        if (valid == null) {
                            frontLineWorker.setStatus(dbRecord.getStatus());
                            updateDbRecord(frontLineWorker, dbRecord);
                            bulkUploadStatus.incrementSuccessCount();
                             logger.info("Record updated successfully for Flw with valid = null");
                        } else {
                            if (valid == true) {
                                if (status == Status.INVALID) {
                                    bulkUploadStatus.incrementFailureCount();
                                    //Bug 38
                                    errorDetails = populateErrorDetails(csvFileName, record.toString(), ErrorCategoryConstants.INCONSISTENT_DATA,
                                            String.format(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, "Status"));
                                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                                    logger.warn("Status change try from invalid to valid for id : {}", id);
                                } else {
                                    frontLineWorker.setStatus(dbRecord.getStatus());
                                    updateDbRecord(frontLineWorker, dbRecord);
                                    bulkUploadStatus.incrementSuccessCount();
                                    logger.info("Record updated successfully for Flw with valid = true");

                                }
                            } else {
                                frontLineWorker.setStatus(Status.INVALID);
                                updateDbRecord(frontLineWorker, dbRecord);
                                bulkUploadStatus.incrementSuccessCount();
                                logger.info("Record updated successfully for Flw with valid = false");
                            }
                        }
                    }

                }
            } catch (DataValidationException dve) {
                errorDetails = populateErrorDetails(csvFileName, record.toString(), dve.getErrorCode(), dve.getErrorDesc());
                bulkUploadStatus.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                if (record.getFlwId() != null) {
                    logger.warn("Record not found for uploaded ID: {}", record.getFlwId());
                } else {
                    if (record.getContactNo() != null) {
                        logger.warn("Record not found for uploaded Contact Number: {}", record.getContactNo());
                    } else {
                        logger.warn("Record not found for uploaded record(both Flw Id and Contact No are not present");
                    }
                }

            } catch (Exception e) {
                bulkUploadStatus.incrementFailureCount();
                logger.error("Exception occur : {}", e.getStackTrace());
                errorDetails = populateErrorDetails(csvFileName, record.toString(), ErrorCategoryConstants.INCONSISTENT_DATA, ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
            } finally {
                if (null != record) {
                    frontLineWorkerCsvService.deleteFromCsv(record);
                }
            }
        }
        bulkUploadStatus.setUploadedBy(userName);
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
        logger.info("Success[flwDataHandlerSuccess] method finished for FrontLineWorkerCsv");
    }

    /**
     * This method maps fields of generated front line worker to the db record
     *
     * @param frontLineWorker the Front Line Worker record genrated
     * @param dbRecord        the record which will be updated in db
     */
    private void updateDbRecord(FrontLineWorker frontLineWorker, FrontLineWorker dbRecord) {

        dbRecord.setName(frontLineWorker.getName());
        dbRecord.setStatus(frontLineWorker.getStatus());
        dbRecord.setContactNo(frontLineWorker.getContactNo());
        dbRecord.setDesignation(frontLineWorker.getDesignation());


        dbRecord.setStateCode(frontLineWorker.getStateCode());
        dbRecord.setStateId(frontLineWorker.getStateId());
        dbRecord.setDistrictId(frontLineWorker.getDistrictId());
        dbRecord.setTalukaId(frontLineWorker.getTalukaId());
        dbRecord.setVillageId(frontLineWorker.getVillageId());
        dbRecord.setHealthBlockId(frontLineWorker.getHealthBlockId());
        dbRecord.setHealthFacilityId(frontLineWorker.getHealthFacilityId());
        dbRecord.setHealthSubFacilityId(frontLineWorker.getHealthSubFacilityId());
        dbRecord.setLanguageLocationCodeId(frontLineWorker.getLanguageLocationCodeId());

        dbRecord.setFlwId(frontLineWorker.getFlwId());
        dbRecord.setAdhaarNumber(frontLineWorker.getAdhaarNumber());
        dbRecord.setAshaNumber(frontLineWorker.getAshaNumber());
        dbRecord.setIsValidated(frontLineWorker.getIsValidated());

        dbRecord.setCreator(frontLineWorker.getCreator());
        dbRecord.setModificationDate(frontLineWorker.getModificationDate());
        dbRecord.setModifiedBy(frontLineWorker.getModifiedBy());
        dbRecord.setCreationDate(frontLineWorker.getCreationDate());
        dbRecord.setOwner(frontLineWorker.getOwner());
        frontLineWorkerService.updateFrontLineWorker(dbRecord);
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
        State state = null;
        District district = null;


        FrontLineWorkerContent frontLineWorkerContent = new FrontLineWorkerContent();

        logger.info("validateFrontLineWorker process start");
        frontLineWorkerContent.setStateCode(ParseDataHelper.validateAndParseLong("StateCode", record.getStateCode(), true));
        frontLineWorkerContent.setDistrictCode(ParseDataHelper.validateAndParseLong("DistrictCode", record.getDistrictCode(), true));


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
        frontLineWorkerContent.setVillage(villageConsistencyCheck(frontLineWorkerContent.getTaluka(), record.getVillageCode()));
        frontLineWorkerContent.setHealthBlock(healthBlockConsistencyCheck(frontLineWorkerContent.getTaluka(), record.getHealthBlockCode()));
        frontLineWorkerContent.setHealthFacility(healthFacilityConsistencyCheck(frontLineWorkerContent.getHealthBlock(), record.getPhcCode()));
        frontLineWorkerContent.setHealthSubFacility(healthSubFacilityConsistencyCheck(frontLineWorkerContent.getHealthFacility(), record.getSubCentreCode()));

        contactNo = ParseDataHelper.validateAndParseString("Contact Number", record.getContactNo(), true);
        contactNoLength = contactNo.length();
        finalContactNo = (contactNoLength > FrontLineWorkerConstants.FLW_CONTACT_NUMBER_LENGTH ? contactNo.substring(contactNoLength - FrontLineWorkerConstants.FLW_CONTACT_NUMBER_LENGTH) : contactNo);
        frontLineWorkerContent.setContactNo(finalContactNo);

        //Bug 28
        designation = ParseDataHelper.validateAndParseString("Flw Type", record.getType(), true);

        //Bug 21
        if (Designation.getEnum(designation) != Designation.ANM && Designation.getEnum(designation) != Designation.AWW &&
                Designation.getEnum(designation) != Designation.ASHA && Designation.getEnum(designation) != Designation.USHA) {
            ParseDataHelper.raiseInvalidDataException("Flw Type", "Invalid");
        } else {
            //Bug 16
            frontLineWorkerContent.setDesignation(Designation.getEnum(designation));
        }
        logger.info("validateFrontLineWorker process end");
        return frontLineWorkerContent;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param record                 the Front Line Worker record from Csv
     * @param frontLineWorkerContent temporary flw record that is to be stored to database
     * @return the Front Line Worker generated.
     * @throws DataValidationException
     */
    private FrontLineWorker mapFrontLineWorkerFrom(FrontLineWorkerCsv record, FrontLineWorkerContent frontLineWorkerContent) throws DataValidationException {


        logger.info("mapFrontLineWorkerFrom process start");

        FrontLineWorker frontLineWorker = new FrontLineWorker();
        frontLineWorker.setContactNo(frontLineWorkerContent.getContactNo());

        frontLineWorker.setName(ParseDataHelper.validateAndParseString("Name", record.getName(), true));
        frontLineWorker.setDesignation(frontLineWorkerContent.getDesignation());

        frontLineWorker.setStateCode(frontLineWorkerContent.getStateCode());
        frontLineWorker.setStateId(frontLineWorkerContent.getState());
        frontLineWorker.setDistrictId(frontLineWorkerContent.getDistrict());
        frontLineWorker.setTalukaId(frontLineWorkerContent.getTaluka());
        frontLineWorker.setVillageId(frontLineWorkerContent.getVillage());
        frontLineWorker.setHealthBlockId(frontLineWorkerContent.getHealthBlock());
        frontLineWorker.setHealthFacilityId(frontLineWorkerContent.getHealthFacility());
        frontLineWorker.setHealthSubFacilityId(frontLineWorkerContent.getHealthSubFacility());

        frontLineWorker.setFlwId(ParseDataHelper.validateAndParseLong("Flw Id", record.getFlwId(), false));
        frontLineWorker.setAshaNumber(ParseDataHelper.validateAndParseString("Asha Number", record.getAshaNumber(), false));
        frontLineWorker.setAdhaarNumber(ParseDataHelper.validateAndParseString("Adhaar Number", record.getAdhaarNo(), false));

        LanguageLocationCode locationCode = languageLocationCodeService.getRecordByLocationCode(frontLineWorker.getStateCode(),
                frontLineWorker.getDistrictId().getDistrictCode());
        if (null != locationCode) {
            frontLineWorker.setLanguageLocationCodeId(locationCode.getId());
        }

        if (record.getIsValid().equalsIgnoreCase("false")) {
            frontLineWorker.setStatus(Status.INVALID);
        } else {
            frontLineWorker.setStatus(Status.INACTIVE);
        }

        frontLineWorker.setCreator(record.getCreator());
        frontLineWorker.setModifiedBy(record.getModifiedBy());
        frontLineWorker.setOwner(record.getOwner());
        frontLineWorker.setModificationDate(record.getModificationDate());
        frontLineWorker.setCreationDate(record.getCreationDate());

        //Bug 15
        frontLineWorker.setIsValidated(ParseDataHelper.validateAndParseBoolean("Is Validated", record.getIsValidated(), false));
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
        Long talukaCode;
        Taluka taluka = null;
        talukaCode = ParseDataHelper.validateAndParseLong("TalukaCode", record, false);

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
     * @param taluka parent taluka
     * @param record value of Village code
     * @return null if optional Village is not provided and its value is null/empty, else Village which is generated
     * from the parameters
     * @throws DataValidationException
     */
    private Village villageConsistencyCheck(Taluka taluka, String record) throws DataValidationException {

        Long villageCode;
        Village village = null;
        villageCode = ParseDataHelper.validateAndParseLong("VillageCode", record, false);
        if (villageCode != null) {
            if (taluka != null) {
                village = locationService.getVillageByCode(taluka.getId(), villageCode);
                if (village == null) {
                    logger.warn("Record not found for Village ID[{}]", villageCode);
                    ParseDataHelper.raiseInvalidDataException("Village", record);
                }
            } else {
                logger.warn("Village ID[{}] present without Taluka", villageCode);
                ParseDataHelper.raiseInvalidDataException("Village", record);
            }
        }
        return village;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param taluka parent taluka
     * @param record value of HealthBlock code
     * @return null if optional HealthBlock is not provided and its value is null/empty, else HealthBlock which is
     * generated from the parameters
     * @throws DataValidationException
     */
    private HealthBlock healthBlockConsistencyCheck(Taluka taluka, String record) throws DataValidationException {

        Long healthclockCode;
        HealthBlock healthBlock = null;

        healthclockCode = ParseDataHelper.validateAndParseLong("HealthBlockCode", record, false);
        if (healthclockCode != null) {
            if (taluka != null) {
                healthBlock = locationService.getHealthBlockByCode(taluka.getId(), healthclockCode);
                if (healthBlock == null) {
                    logger.warn("Record not found for HealthBlock ID[{}]", healthclockCode);
                    ParseDataHelper.raiseInvalidDataException("HealthBlock", record);
                }
            } else {
                logger.warn("HealthBlock ID[{}] present without Taluka", healthclockCode);
                ParseDataHelper.raiseInvalidDataException("HealthBlock", record);
            }
        }

        return healthBlock;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param healthBlock parent HelathBlock
     * @param record      value of HealthFacility code
     * @return null if optional HealthFacility is not provided and its value is null/empty, else HealthFacility which is
     * generated from the parameters
     * @throws DataValidationException
     */
    private HealthFacility healthFacilityConsistencyCheck(HealthBlock healthBlock, String record) throws DataValidationException {

        Long healthFacilityCode;
        HealthFacility healthFacility = null;

        healthFacilityCode = ParseDataHelper.validateAndParseLong("HealthFacilityCode", record, false);
        if (healthFacilityCode != null) {
            if (healthBlock != null) {
                healthFacility = locationService.getHealthFacilityByCode(healthBlock.getId(), healthFacilityCode);
                if (healthFacility == null) {
                    logger.warn("Record not found for HealthFacility ID[{}]", healthFacilityCode);
                    ParseDataHelper.raiseInvalidDataException("HealthFacility", record);
                }
            } else {
                logger.warn("HealthFacility ID[{}] present without HealthBlock", healthFacilityCode);
                ParseDataHelper.raiseInvalidDataException("HealthFacility", record);
            }
        }

        return healthFacility;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param healthFacility parent HelathBlock
     * @param record         value of HealthSubFacility code
     * @return null if optional HealthSubFacility is not provided and its value is null/empty, else HealthSubFacility
     * which is generated from the parameters
     * @throws DataValidationException
     */
    private HealthSubFacility healthSubFacilityConsistencyCheck(HealthFacility healthFacility, String record) throws DataValidationException {
        Long healthSubFacilityCode;
        HealthSubFacility healthSubFacility = null;

        healthSubFacilityCode = ParseDataHelper.validateAndParseLong("HealthSubFacilityCode", record, false);
        if (healthSubFacilityCode != null) {
            if (healthFacility != null) {
                healthSubFacility = locationService.getHealthSubFacilityByCode(healthFacility.getId(), healthSubFacilityCode);
                if (healthSubFacility == null) {
                    logger.warn("Record not found for HealthSubFacility ID[{}]", healthSubFacilityCode);
                    ParseDataHelper.raiseInvalidDataException("HealthSubFacility", record);
                }
            } else {
                logger.warn("HealthSubFacility ID[{}] present without HealthFacility", healthSubFacilityCode);
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
     * @param errorDescription specifies error description
     */
    private BulkUploadError populateErrorDetails(String csvFileName, String id, String errorCategory, String errorDescription) {

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(NmsUtils.getCurrentTimeStamp());
        errorDetails.setRecordType(RecordType.FRONT_LINE_WORKER);
        errorDetails.setRecordDetails(id);
        errorDetails.setErrorCategory(errorCategory);
        errorDetails.setErrorDescription(errorDescription);
        return errorDetails;
    }

    /**
     * This method is used to set error record details
     *
     * @param flwId     FlwId for which db record is to be found
     * @param stateCode specifies the state code from the Csv record
     * @param contactNo specifies the contact no from the Csv record
     * @return null if there is no db record for given FlwId else the record generated from db
     * @throws DataValidationException
     */
    private FrontLineWorker checkExistenceOfFlw(Long flwId, Long stateCode, String contactNo) throws DataValidationException {
        logger.debug("FLW state Code : {}", stateCode);

        FrontLineWorker dbRecord = frontLineWorkerService.getFlwByFlwIdAndStateId(flwId,stateCode);
        if (dbRecord == null) {
            logger.debug("FLW Contact Number : {}", contactNo);
            dbRecord = frontLineWorkerService.getFlwBycontactNo(contactNo);
        }

        return dbRecord;
    }

}

