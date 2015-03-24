package org.motechproject.nms.frontlineworker.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerCsvService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.helper.DataValidationException;
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
public class FrontLineWorkerUploadHandler {

    private BulkUploadErrLogService bulkUploadErrLogService;

    private LocationService locationService;

    private FrontLineWorkerService frontLineWorkerService;

    private FrontLineWorkerCsvService frontLineWorkerCsvService;

    private static final String CSV_IMPORT_PREFIX = "csv-import.";

    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";

    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private static Logger logger = LoggerFactory.getLogger(FrontLineWorkerUploadHandler.class);


    @Autowired
    public FrontLineWorkerUploadHandler(BulkUploadErrLogService bulkUploadErrLogService,
                                        LocationService locationService,
                                        FrontLineWorkerService frontLineWorkerService,
                                        FrontLineWorkerCsvService frontLineWorkerCsvService
    ) {

        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.locationService = locationService;
        this.frontLineWorkerService = frontLineWorkerService;
        this.frontLineWorkerCsvService = frontLineWorkerCsvService;
    }

    /**
     * This method provides a listener to the Front Line Worker upload scenario. Here, FrontLineWorker CSV is received
     * from which records are fetched one by one and validations are performed on the record. If all the validations
     * pass, the record is saved in Database else the record is rejected. After completion of the processing on the
     * CSV record, it is deleted from the database.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {ConfigurationConstants.FLW_UPLOAD_SUCCESS})
    public void flwDataHandlerSuccess(MotechEvent motechEvent) {

        String userName = null;

        logger.info("Success[flwDataHandlerSuccess] method start for FrontLineWorkerCsv");
        Map<String, Object> params = motechEvent.getParameters();
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);

        logger.debug("Processing Csv file");

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        List<Long> createdIds = (ArrayList<Long>) params.get(CSV_IMPORT_CREATED_IDS);
        FrontLineWorkerCsv record = null;
        Long nmsFlwId = null;

        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(new DateTime());

        //this loop processes each of the entries in the Front Line Worker Csv and performs operation(DEL/ADD/MOD)
        // on the record and also deleted each record after processing from the Csv. If some error occurs in any
        // of the records, it is reported.
        for (Long id : createdIds) {
            try {
                logger.debug("Processing uploaded id : {}", id);
                record = frontLineWorkerCsvService.findByIdInCsv(id);
                if (record != null) {
                    userName = record.getOwner();
                    logger.debug("Record found in Csv database");

                    FrontLineWorker frontLineWorker = new FrontLineWorker();
                    validateFrontLineWorker(record, frontLineWorker);
                    mapFrontLineWorkerFrom(record, frontLineWorker);

                    nmsFlwId = frontLineWorker.getId();

                    FrontLineWorker dbRecord = checkExistenceOfFlw(frontLineWorker);
                    Long flw = ParseDataHelper.validateAndParseLong("flwId", record.getFlwId(), false);

                    if (flw == null && dbRecord.getFlwId() != null) {
                        ParseDataHelper.raiseInvalidDataException("FlwId for existing frontlineworker", null);
                    }

                    if (dbRecord != null && dbRecord.getId() != nmsFlwId && nmsFlwId != null) {

                        ParseDataHelper.raiseInvalidDataException("nmsFlwId", "Incorrect");
                    }

                    if (dbRecord == null) {

                        logger.debug("New front line worker creation starts");
                        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
                        bulkUploadStatus.incrementSuccessCount();
                        logger.debug("Successful creation of new front line worker");

                    } else {
                        if (dbRecord.getStatus() == Status.INVALID) {
                            ParseDataHelper.raiseInvalidDataException("Status for existing frontlineworker", "Invalid");
                        } else {
                            Boolean valid = ParseDataHelper.validateAndParseBoolean("isValid", record.getIsValid(), false);
                            Status status = dbRecord.getStatus();
                            if (valid == null) {
                                if (Status.ANONYMOUS == dbRecord.getStatus()) {
                                    frontLineWorker.setStatus(Status.ACTIVE);
                                } else {
                                    frontLineWorker.setStatus(dbRecord.getStatus());
                                }

                                updateDbRecord(frontLineWorker, dbRecord);
                                bulkUploadStatus.incrementSuccessCount();
                                logger.debug("Record updated successfully for Flw with valid = null");
                            } else {
                                if (valid == true) {
                                    if (status == Status.INVALID) {
                                        bulkUploadStatus.incrementFailureCount();
                                        //Bug 38
                                        errorDetails = populateErrorDetails(csvFileName, record.toString(),
                                                ErrorCategoryConstants.INCONSISTENT_DATA,
                                                String.format(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, "Status"));
                                        bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                                        logger.warn("Status change try from invalid to valid for id : {}", id);
                                    } else {
                                        if (Status.ANONYMOUS == dbRecord.getStatus()) {
                                            frontLineWorker.setStatus(Status.ACTIVE);
                                        } else {
                                            frontLineWorker.setStatus(dbRecord.getStatus());
                                        }
                                        updateDbRecord(frontLineWorker, dbRecord);
                                        bulkUploadStatus.incrementSuccessCount();
                                        logger.debug("Record updated successfully for Flw with valid = true");

                                    }
                                } else {
                                    frontLineWorker.setStatus(Status.INVALID);
                                    updateDbRecord(frontLineWorker, dbRecord);
                                    bulkUploadStatus.incrementSuccessCount();
                                    logger.debug("Record updated successfully for Flw with valid = false");
                                }
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
                errorDetails = populateErrorDetails(csvFileName, record.toString(),
                        ErrorCategoryConstants.GENERAL_EXCEPTION,
                        ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
            } finally {
                if (null != record) {
                    frontLineWorkerCsvService.deleteFromCsv(record);
                }
            }
        }
        bulkUploadStatus.setUploadedBy(userName);
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
        logger.debug("Success[flwDataHandlerSuccess] method finished for FrontLineWorkerCsv");
    }

    /**
     * This method maps fields of generated front line worker object to front line worker object that
     * is to be saved in Database.
     *
     * @param dbRecord        the record which will be updated in db
     * @param frontLineWorker the frontLineWorker object which is to be mapped to the db object
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

        dbRecord.setFlwId(frontLineWorker.getFlwId());
        dbRecord.setId(frontLineWorker.getId());
        dbRecord.setAdhaarNumber(frontLineWorker.getAdhaarNumber());
        dbRecord.setAshaNumber(frontLineWorker.getAshaNumber());

        dbRecord.setModifiedBy(frontLineWorker.getModifiedBy());
        dbRecord.setOwner(frontLineWorker.getOwner());
        frontLineWorkerService.updateFrontLineWorker(dbRecord);
    }


    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param record          the Front Line Worker record from Csv that is to be validated
     * @param frontLineWorker the Front Line Worker record that is to be saved in database
     * @throws DataValidationException
     */
    private void validateFrontLineWorker(FrontLineWorkerCsv record, FrontLineWorker frontLineWorker) throws DataValidationException {

        String contactNo;
        String designation;
        State state = null;
        District district = null;
        Long districtCode = null;

        logger.debug("validateFrontLineWorker process start");
        frontLineWorker.setStateCode(ParseDataHelper.validateAndParseLong("StateCode", record.getStateCode(), true));
        districtCode = ParseDataHelper.validateAndParseLong("DistrictCode", record.getDistrictCode(), true);


        state = locationService.getStateByCode(frontLineWorker.getStateCode());
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", null);
        }
        frontLineWorker.setStateId(state);
        district = locationService.getDistrictByCode(frontLineWorker.getStateId().getId(), districtCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", null);
        }

        frontLineWorker.setDistrictId(district);
        frontLineWorker.setTalukaId(talukaConsistencyCheck(frontLineWorker.getDistrictId().getId(), record.getTalukaCode()));
        frontLineWorker.setVillageId(villageConsistencyCheck(frontLineWorker.getTalukaId(), record.getVillageCode()));
        frontLineWorker.setHealthBlockId(healthBlockConsistencyCheck(frontLineWorker.getTalukaId(), record.getHealthBlockCode()));
        frontLineWorker.setHealthFacilityId(healthFacilityConsistencyCheck(frontLineWorker.getHealthBlockId(), record.getPhcCode()));
        frontLineWorker.setHealthSubFacilityId(healthSubFacilityConsistencyCheck(frontLineWorker.getHealthFacilityId(), record.getSubCentreCode()));

        contactNo = ParseDataHelper.validateAndTrimMsisdn("Contact Number", record.getContactNo());
        frontLineWorker.setContactNo(contactNo);

        //Bug 28
        designation = ParseDataHelper.validateAndParseString("Flw Type", record.getType(), true);

        //Bug 21
        if (Designation.getEnum(designation) != Designation.ANM && Designation.getEnum(designation) != Designation.AWW &&
                Designation.getEnum(designation) != Designation.ASHA && Designation.getEnum(designation) != Designation.USHA) {
            ParseDataHelper.raiseInvalidDataException("Flw Type", "Invalid");
        } else {
            //Bug 16
            frontLineWorker.setDesignation(Designation.getEnum(designation));
        }
        logger.debug("validateFrontLineWorker process end");
    }


    /**
     * This method maps a field of FrontLineWorkerCsv type to FrontLineWorker field. It checks for null/empty values,
     * and raises exception if a mandatory field is empty/null or is invalid date format
     *
     * @param record          FrontLineWorker Csv record which is provided in the Csv
     * @param frontLineWorker the frontLineWorker object which is to be mapped from FrontLineWorkerCsv record
     * @throws DataValidationException
     */
    private void mapFrontLineWorkerFrom(FrontLineWorkerCsv record, FrontLineWorker frontLineWorker) throws DataValidationException {


        logger.debug("mapFrontLineWorkerFrom process start");

        frontLineWorker.setName(ParseDataHelper.validateAndParseString("Name", record.getName(), true));

        frontLineWorker.setFlwId(ParseDataHelper.validateAndParseLong("Flw Id", record.getFlwId(), false));
        frontLineWorker.setId(ParseDataHelper.validateAndParseLong("Nms Flw Id", record.getSystemGeneratedFlwId(), false));
        frontLineWorker.setAshaNumber(ParseDataHelper.validateAndParseString("Asha Number", record.getAshaNumber(), false));
        frontLineWorker.setAdhaarNumber(ParseDataHelper.validateAndParseString("Adhaar Number", record.getAdhaarNo(), false));

        if (record.getIsValid().equalsIgnoreCase("false")) {
            frontLineWorker.setStatus(Status.INVALID);
        } else {
            frontLineWorker.setStatus(Status.INACTIVE);
        }

        frontLineWorker.setCreator(record.getCreator());
        frontLineWorker.setModifiedBy(record.getModifiedBy());
        frontLineWorker.setOwner(record.getOwner());

        logger.debug("mapFrontLineWorkerFrom process end");
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
    private HealthSubFacility healthSubFacilityConsistencyCheck(HealthFacility healthFacility, String record)
            throws DataValidationException {
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
        errorDetails.setTimeOfUpload(new DateTime());
        errorDetails.setRecordType(RecordType.FRONT_LINE_WORKER);
        errorDetails.setRecordDetails(id);
        errorDetails.setErrorCategory(errorCategory);
        errorDetails.setErrorDescription(errorDescription);
        return errorDetails;
    }

    /**
     * This method is used to set error record details
     *
     * @param frontLineWorker front line worker whose details are to be fetched from database.
     * @return null if there is no db record for given FlwId else the record generated from db
     */

    private FrontLineWorker checkExistenceOfFlw(FrontLineWorker frontLineWorker) throws DataValidationException {

        FrontLineWorker dbRecord = null;
        FrontLineWorker dbRecordByContactNo = null;
        Long id = frontLineWorker.getId();
        if (id != null) {
            dbRecord = frontLineWorkerService.findById(frontLineWorker.getId());
            if (dbRecord == null) {
                ParseDataHelper.raiseInvalidDataException("nmsFlwId", id.toString());
            }


        } else {
            Long stateCode = frontLineWorker.getStateCode();
            logger.debug("FLW state Code : {}", stateCode);
            dbRecord = frontLineWorkerService.getFlwByFlwIdAndStateId(frontLineWorker.getFlwId(), stateCode);
        }

        String contactNo = frontLineWorker.getContactNo();
        logger.debug("FLW Contact Number : {}", contactNo);
        dbRecordByContactNo = frontLineWorkerService.getFlwBycontactNo(contactNo);

        //creation when record not found in database
        if (dbRecord == null && dbRecordByContactNo == null) {
            return dbRecord;
        } else {
            if (dbRecord == null && dbRecordByContactNo != null) {
                //creation of new record when existing record is invalid
                if (dbRecordByContactNo.getStatus() == Status.INVALID) {
                    return dbRecord;
                }
                //updation of existing record
                else {
                    return dbRecordByContactNo;
                }

            } else {
                //updation of existing record
                if (dbRecord != null && dbRecordByContactNo == null) {
                    return dbRecord;
                } else {
                    //updation of existing record. here both dbRecord and dbRecordByContactNo point to same FLW
                    if (dbRecord.getId() == dbRecordByContactNo.getId()) {
                        return dbRecord;
                    } else {
                        //creation of new record when existing record is invalid
                        if (dbRecordByContactNo.getStatus() == Status.INVALID) {
                            return dbRecord;
                        } else {
                            //when trying to update record where two different records are fetched from database
                            ParseDataHelper.raiseInvalidDataException("contactNo", dbRecord.getContactNo());
                        }
                    }
                }
            }
        }

        return dbRecord;
    }

}

