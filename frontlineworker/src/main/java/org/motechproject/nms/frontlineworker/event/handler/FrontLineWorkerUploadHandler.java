package org.motechproject.nms.frontlineworker.event.handler;


import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.CsvFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.enums.Designation;
import org.motechproject.nms.frontlineworker.enums.Status;
import org.motechproject.nms.frontlineworker.repository.FrontLineWorkerRecordDataService;
import org.motechproject.nms.frontlineworker.service.CsvFrontLineWorkerService;
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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

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

    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";
    private static Logger logger = LoggerFactory.getLogger(FrontLineWorkerUploadHandler.class);
    private BulkUploadErrLogService bulkUploadErrLogService;
    private LocationService locationService;
    private FrontLineWorkerService frontLineWorkerService;
    private FrontLineWorkerRecordDataService frontLineWorkerRecordDataService;
    private CsvFrontLineWorkerService csvFrontLineWorkerService;
    public static final String NMS_FLW_ID = "NMS Flw Id";
    public static final String CONTACT_NUMBER = "Contact Number";


    @Autowired
    public FrontLineWorkerUploadHandler(BulkUploadErrLogService bulkUploadErrLogService,
                                        LocationService locationService,
                                        FrontLineWorkerService frontLineWorkerService,
                                        CsvFrontLineWorkerService csvFrontLineWorkerService,
                                        FrontLineWorkerRecordDataService frontLineWorkerRecordDataService
    ) {

        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.locationService = locationService;
        this.frontLineWorkerService = frontLineWorkerService;
        this.csvFrontLineWorkerService = csvFrontLineWorkerService;
        this.frontLineWorkerRecordDataService = frontLineWorkerRecordDataService;
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

        logger.info("Success[flwDataHandlerSuccess] method start for CsvFrontLineWorker");
        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);
        List<Long> createdIds = (ArrayList<Long>) params.get(CSV_IMPORT_CREATED_IDS);

        logger.debug("Processing Csv file");

        processRecords(findListOfRecords(createdIds), csvFileName);
        logger.info("Finished processing Success[flwDataHandlerSuccess] method for CsvFrontLineWorker");
    }


    /**
     * find List Of CsvFrontLineWorker on the basis of received Id
     *
     * @param createdIds List of created id on csv upload
     * @return List<CsvFrontLineWorker> List Of CsvFrontLineWorker
     */

    private List<CsvFrontLineWorker> findListOfRecords(
            List<Long> createdIds) {
        List<CsvFrontLineWorker> listOfRecords = new ArrayList<>();
        for (Long id : createdIds) {
            listOfRecords.add(csvFrontLineWorkerService.findByIdInCsv(id));
        }
        return listOfRecords;
    }

    /**
     * This function processes all the CSV upload records. This function is
     * called in a transaction call so in case of any error, the changes are
     * reverted back.
     *
     * @param record      list of csvFrontLineWorker objects
     * @param csvFileName name of the upload file
     */
    private void processRecords(List<CsvFrontLineWorker> record,
                                String csvFileName) {

        logger.debug("Record Processing Started for csv file: {}", csvFileName);

        frontLineWorkerRecordDataService
                .doInTransaction(new TransactionCallback<FrontLineWorker>() {

                    List<CsvFrontLineWorker> record;

                    String csvFileName;

                    private TransactionCallback<FrontLineWorker> init(
                            List<CsvFrontLineWorker> record,
                            String csvFileName) {
                        this.record = record;
                        this.csvFileName = csvFileName;
                        return this;
                    }

                    @Override
                    public FrontLineWorker doInTransaction(
                            TransactionStatus status) {
                        FrontLineWorker transactionObject = null;
                        processRecordsInTransaction(record,
                                csvFileName);
                        return transactionObject;
                    }
                }.init(record, csvFileName));
        logger.debug("Record Processing complete for csv file: {}", csvFileName);
    }

    /**
     * This function processes all the CSV upload records. This function is
     * called from  processRecords procedure to perform transactional add/del/mod.
     *
     * @param record      list of csvFrontLineWorker objects
     * @param csvFileName name of the upload file
     */
    private void processRecordsInTransaction(
            List<CsvFrontLineWorker> record, String csvFileName) {

        logger.debug("processRecordsInTransaction method start for CsvFrontLineWorker");
        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails;

        Long nmsFlwId = null;
        String userName = null;

        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(new DateTime());

        //this loop processes each of the entries in the Front Line Worker Csv and performs operation(DEL/ADD/MOD)
        // on the record and also deleted each record after processing from the Csv. If some error occurs in any
        // of the records, it is reported.

        for (CsvFrontLineWorker csvsFrontLineWorker : record) {

            try {
                if (csvsFrontLineWorker != null) {
                    //Record is found in Csv
                    userName = csvsFrontLineWorker.getOwner();
                    logger.debug("Record found in Csv database");

                    FrontLineWorker frontLineWorker = new FrontLineWorker();

                    if ((null != csvsFrontLineWorker.getIsValid()) && (csvsFrontLineWorker.getIsValid().equalsIgnoreCase("false"))) {
                        checkForInvalidFrontLineWorker(csvsFrontLineWorker);
                    } else {

                        //Apply validations on the values entered in the CSV
                        validateFrontLineWorker(csvsFrontLineWorker, frontLineWorker);

                        //Map values entered for the record in CSV to FrontLineWorker
                        mapFrontLineWorkerFrom(csvsFrontLineWorker, frontLineWorker);

                        nmsFlwId = frontLineWorker.getId();

                        //to verify whether it is a creation case, update case of invalid case
                        FrontLineWorker dbRecord = checkExistenceOfFlw(frontLineWorker);
                        Long flw = ParseDataHelper.validateAndParseLong("flwId", csvsFrontLineWorker.getFlwId(), false);

                        //in case of update, if flwId was present earlier and absent in latest record, exception is
                        // to be thrown
                        if (flw == null && dbRecord != null && dbRecord.getFlwId() != null) {
                            ParseDataHelper.raiseInvalidDataException("FlwId for existing frontlineworker", null);
                        }

                        //if in case of updation, the CSV has system generated nmsFlwId and is not equal to the same stored
                        // in database, then exception is to be thrown
                        if ((dbRecord != null) && (nmsFlwId != null) && (dbRecord.getId().longValue() != nmsFlwId.longValue())) {
                            ParseDataHelper.raiseInvalidDataException(NMS_FLW_ID, "Incorrect");
                        }

                        if (dbRecord == null) {

                            //creation scenario
                            logger.debug("New front line worker creation starts");
                            successfulCreate(frontLineWorker, bulkUploadStatus, "Successful creation of new front line worker");


                        } else {
                            //updation scenario
                            if (dbRecord.getStatus() == Status.INVALID) {
                                //Invalid record is tried to be updated
                                ParseDataHelper.raiseInvalidDataException("Status for existing frontlineworker", "Invalid");
                            } else {
                                Boolean valid = ParseDataHelper.validateAndParseBoolean("isValid", csvsFrontLineWorker.getIsValid(), false);
                                if (valid == null) {
                                    frontLineWorker.setStatus(setStatusWhenValid(dbRecord.getStatus()));
                                    successfulUpdate(frontLineWorker, dbRecord, bulkUploadStatus, "Record updated successfully for Flw with valid = null ");

                                } else {
                                    frontLineWorker.setStatus(setStatusWhenValid(dbRecord.getStatus()));
                                    successfulUpdate(frontLineWorker, dbRecord, bulkUploadStatus, "Record updated successfully for Flw with valid = true ");
                                }
                            }
                        }
                    }
                }
            } catch (DataValidationException dve) {
                errorDetails = populateErrorDetails(csvFileName, csvsFrontLineWorker.toString(), dve.getErrorCode(), dve.getErrorDesc());
                bulkUploadStatus.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                if (csvsFrontLineWorker.getFlwId() != null) {
                    logger.warn("Record not found for uploaded ID: {}", csvsFrontLineWorker.getFlwId());
                } else {
                    if (csvsFrontLineWorker.getContactNo() != null) {
                        logger.warn("Record not found for uploaded Contact Number: {}", csvsFrontLineWorker.getContactNo());
                    } else {
                        logger.warn("Record not found for uploaded record(both Flw Id and Contact No are not present");
                    }
                }

            } catch (Exception e) {
                bulkUploadStatus.incrementFailureCount();
                logger.error("exception occur : {}", e.getStackTrace());
                errorDetails = populateErrorDetails(csvFileName, csvsFrontLineWorker.toString(),
                        ErrorCategoryConstants.GENERAL_EXCEPTION,
                        ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
            } finally {
                if (csvsFrontLineWorker != null) {
                    csvFrontLineWorkerService.deleteFromCsv(csvsFrontLineWorker);
                }
            }


            bulkUploadStatus.setUploadedBy(userName);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
            logger.debug("processRecordsInTransaction method finished for CsvFrontLineWorker");
        }
    }


    /**
     * This procedure is used to mark he frontLineWorker as invalid if the isValid field is set as false in Csv record
     * and the record is present in database.
     *
     * @param csvFrontLineWorker the csvFrontLineWorker record which is to be marked invalid
     * @throws DataValidationException
     */
    private void checkForInvalidFrontLineWorker(CsvFrontLineWorker csvFrontLineWorker) throws DataValidationException {

        logger.debug(" checking for existence of record that is to be marked invalid");
        FrontLineWorker dbRecord = null;
        FrontLineWorker dbRecordByContactNo = null;
        Long nmsFlwId = ParseDataHelper.validateAndParseLong(NMS_FLW_ID, csvFrontLineWorker.getNmsFlwId(), false);


        if (nmsFlwId == null) {
            logger.debug(" nms Flw Id is null. searching by Contact Number");

            String contactNo = ParseDataHelper.validateAndTrimMsisdn(CONTACT_NUMBER, ParseDataHelper.validateAndParseString(CONTACT_NUMBER, csvFrontLineWorker.getContactNo(), true));
            dbRecordByContactNo = frontLineWorkerService.getFlwBycontactNo(contactNo);
            if (dbRecordByContactNo == null) {
                ParseDataHelper.raiseInvalidDataException(CONTACT_NUMBER, csvFrontLineWorker.getContactNo());
            } else {
                //mark record is invalid
                logger.debug(" Record found in database. Marking recors as invalid");
                dbRecordByContactNo.setStatus(Status.INVALID);
                dbRecordByContactNo.setInvalidDate(DateTime.now());
                frontLineWorkerService.updateFrontLineWorker(dbRecordByContactNo);
            }


        } else {
            logger.debug(" nms Flw Id is not null. searching record in database by nmsFlwID");
            dbRecord = frontLineWorkerService.findById(nmsFlwId);
            if (dbRecord == null) {
                ParseDataHelper.raiseInvalidDataException(NMS_FLW_ID, nmsFlwId.toString());
            } else {
                //update the record as invalid
                logger.debug(" Record found in database. Marking recors as invalid");
                dbRecord.setStatus(Status.INVALID);
                dbRecord.setInvalidDate(DateTime.now());
                frontLineWorkerService.updateFrontLineWorker(dbRecord);
            }
        }
    }


    /**
     * This method maps fields of generated front line worker object to front line worker object that
     * is to be saved in Database.
     *
     * @param status status of frontLineWorker present in database
     * @return status       updated status that is to be saved in database
     */
    private Status setStatusWhenValid(Status status) {
        if (Status.ANONYMOUS == status) {
            return Status.ACTIVE;
        } else {
            return status;
        }
    }

    /**
     * This method maps fields of generated front line worker object to front line worker object that
     * is to be saved in Database.
     *
     * @param frontLineWorker  the frontLineWorker object which is to be stored in database
     * @param bulkUploadStatus object to provide the status of create scenario
     * @param log              log to be generated for create scenario
     */
    private void successfulCreate(FrontLineWorker frontLineWorker, BulkUploadStatus bulkUploadStatus, String log) {

        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        bulkUploadStatus.incrementSuccessCount();
        logger.debug(log);


    }

    /**
     * This method maps fields of generated front line worker object to front line worker object that
     * is to be saved in Database.
     *
     * @param frontLineWorker  the frontLineWorker object which is to be mapped to the db object
     * @param dbRecord         the record which will be updated in db
     * @param bulkUploadStatus object to provide the status of update scenario
     * @param log              log to be generated for update scenario
     */
    private void successfulUpdate(FrontLineWorker frontLineWorker, FrontLineWorker dbRecord, BulkUploadStatus bulkUploadStatus, String log) {

        updateDbRecord(frontLineWorker, dbRecord);
        bulkUploadStatus.incrementSuccessCount();
        logger.debug(log);
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
        dbRecord.setAdhaarNumber(frontLineWorker.getAdhaarNumber());

        if (frontLineWorker.getOldMobileNo() != null) {
            dbRecord.setOldMobileNo(frontLineWorker.getOldMobileNo());
        }

        if (frontLineWorker.getAlternateContactNo() != null) {
            dbRecord.setAlternateContactNo(frontLineWorker.getAlternateContactNo());
        }

        dbRecord.setModifiedBy(frontLineWorker.getModifiedBy());
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
    private void validateFrontLineWorker(CsvFrontLineWorker record, FrontLineWorker frontLineWorker) throws DataValidationException {

        String contactNo;
        String designation;
        State state = null;
        District district = null;
        Long districtCode = null;
        String alternateContactNo;
        String oldMobileNo;

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

        contactNo = ParseDataHelper.validateAndTrimMsisdn(CONTACT_NUMBER, ParseDataHelper.validateAndParseString(CONTACT_NUMBER, record.getContactNo(), true));
        frontLineWorker.setContactNo(contactNo);

        if(!ParseDataHelper.isNullOrEmpty(record.getAlternateContactNo())) {
            alternateContactNo = ParseDataHelper.validateAndTrimMsisdn("Alternate Number", record.getAlternateContactNo());
            frontLineWorker.setAlternateContactNo(alternateContactNo);
        }
        if(!ParseDataHelper.isNullOrEmpty(record.getOldMobileNo())) {
            oldMobileNo = ParseDataHelper.validateAndTrimMsisdn("Old Mobile Number", record.getOldMobileNo());
            frontLineWorker.setOldMobileNo(oldMobileNo);
        }

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
     * This method maps a field of CsvFrontLineWorker type to FrontLineWorker field. It checks for null/empty values,
     * and raises exception if a mandatory field is empty/null or is invalid date format
     *
     * @param record          FrontLineWorker Csv record which is provided in the Csv
     * @param frontLineWorker the frontLineWorker object which is to be mapped from CsvFrontLineWorker record
     * @throws DataValidationException
     */
    private void mapFrontLineWorkerFrom(CsvFrontLineWorker record, FrontLineWorker frontLineWorker) throws DataValidationException {


        logger.debug("mapFrontLineWorkerFrom process start");

        frontLineWorker.setName(ParseDataHelper.validateAndParseString("Name", record.getName(), true));

        frontLineWorker.setFlwId(ParseDataHelper.validateAndParseLong("Flw Id", record.getFlwId(), false));
        frontLineWorker.setId(ParseDataHelper.validateAndParseLong(NMS_FLW_ID, record.getNmsFlwId(), false));
        frontLineWorker.setAdhaarNumber(ParseDataHelper.validateAndParseString("Adhaar Number", record.getAdhaarNo(), false));

        frontLineWorker.setStatus(Status.INACTIVE);

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

        Long healthBlockCode;
        HealthBlock healthBlock = null;

        healthBlockCode = ParseDataHelper.validateAndParseLong("HealthBlockCode", record, false);
        if (healthBlockCode != null) {
            if (taluka != null) {
                healthBlock = locationService.getHealthBlockByCode(taluka.getId(), healthBlockCode);
                if (healthBlock == null) {
                    logger.warn("Record not found for HealthBlock ID[{}]", healthBlockCode);
                    ParseDataHelper.raiseInvalidDataException("HealthBlock", record);
                }
            } else {
                logger.warn("HealthBlock ID[{}] present without Taluka", healthBlockCode);
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
     * This method is used to check existence of frontlineworker
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
                ParseDataHelper.raiseInvalidDataException(NMS_FLW_ID, id.toString());
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
                    if (frontLineWorker.getFlwId() == null && dbRecordByContactNo.getFlwId() != null) {
                        ParseDataHelper.raiseInvalidDataException("FlwId", null);
                    } else {
                        return dbRecordByContactNo;
                    }

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


