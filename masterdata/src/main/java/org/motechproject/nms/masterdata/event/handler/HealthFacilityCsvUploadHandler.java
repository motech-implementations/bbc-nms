package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.*;
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
import java.util.List;
import java.util.Map;

/**
 * This class handles the csv upload for success and failure events for HealthFacilityCsv.
 */

@Component
public class HealthFacilityCsvUploadHandler {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    private TalukaRecordsDataService talukaRecordsDataService;

    @Autowired
    private HealthFacilityCsvRecordsDataService healthFacilityCsvRecordsDataService;

    @Autowired
    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;

    @Autowired
    private HealthBlockCsvRecordsDataService healthBlockCsvRecordsDataService;

    @Autowired
    private HealthBlockRecordsDataService healthBlockRecordsDataService;


    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(HealthFacilityCsvUploadHandler.class);

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in HealthFacility table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.HEALTH_FACILITY_CSV_SUCCESS})
    public void healthFacilityCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        logger.info("HEALTH_FACILITY_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();
        String userName = null;

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        HealthFacilityCsv healthFacilityCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("HEALTH_FACILITY_CSV_SUCCESS event processing start for ID: {}", id);
                healthFacilityCsvRecord = healthFacilityCsvRecordsDataService.findById(id);

                if (null != healthFacilityCsvRecord) {
                    logger.info("Id exist in HealthFacility Temporary Entity");
                    userName = healthFacilityCsvRecord.getOwner();
                    HealthFacility record = mapHealthFacilityCsv(healthFacilityCsvRecord);
                    insertHealthFacilityData(record, healthFacilityCsvRecord.getOperation());
                    result.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in HealthFacility Temporary Entity");
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                    result.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("HEALTH_BLOCK_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(healthFacilityCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                result.incrementFailureCount();
            } catch (Exception e) {
                logger.error("HEALTH_BLOCK_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                result.incrementFailureCount();
            }
            finally {
                if(null != healthFacilityCsvRecord){
                    healthFacilityCsvRecordsDataService.delete(healthFacilityCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFileName, result);
    }

    /**
     * This method handle the event which is raised after csv upload is failed.
     * This method also deletes all the csv records which get inserted in this upload..
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.HEALTH_FACILITY_CSV_FAILED})
    public void healthFacilityCsvFailed(MotechEvent motechEvent) {

        Map<String, Object> params = motechEvent.getParameters();
        logger.info("HEALTH_FACILITY_CSV_FAILED event received");
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            logger.debug("HEALTH_FACILITY_CSV_FAILED event processing start for ID: {}", id);
            HealthFacilityCsv healthFacilityCsv = healthFacilityCsvRecordsDataService.findById(id);
            healthFacilityCsvRecordsDataService.delete(healthFacilityCsv);
        }
        logger.info("HEALTH_FACILITY_CSV_FAILED event processing finished");
    }

    private HealthFacility mapHealthFacilityCsv(HealthFacilityCsv record) throws DataValidationException {
        HealthFacility newRecord = new HealthFacility();

        String healthFacilityName = ParseDataHelper.parseString("HealthFacilityName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        String talukaCode = ParseDataHelper.parseString("TalukaCode", record.getTalukaCode(), true);
        Long healthBlockCode = ParseDataHelper.parseLong("HealthBlockCode", record.getHealthBlockCode(), true);
        Long facilityCode = ParseDataHelper.parseLong("FacilityCode", record.getHealthFacilityCode(), true);

        State state = stateRecordsDataService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", "StateCode");
        }

        District district = districtRecordsDataService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", "DistrictCode");
        }

        Taluka taluka = talukaRecordsDataService.findTalukaByParentCode(stateCode, districtCode, talukaCode);

        if (taluka == null) {
            ParseDataHelper.raiseInvalidDataException("Taluka", "TalukaCode");
        }

        HealthBlock healthBlock = healthBlockRecordsDataService.findHealthBlockByParentCode(
                stateCode, districtCode, talukaCode, healthBlockCode);
        if (healthBlock == null) {
            ParseDataHelper.raiseInvalidDataException("HealthBlock", "HealthBlockCode");
        }
        newRecord.setName(healthFacilityName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setHealthBlockCode(healthBlockCode);
        newRecord.setHealthFacilityCode(facilityCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void insertHealthFacilityData(HealthFacility healthFacilityData, String operation) {

        logger.debug("Health Facility data contains facility code : {}",healthFacilityData.getHealthFacilityCode());
        HealthFacility existHealthFacilityData = healthFacilityRecordsDataService.findHealthFacilityByParentCode(
                healthFacilityData.getStateCode(),
                healthFacilityData.getDistrictCode(),
                healthFacilityData.getTalukaCode(),
                healthFacilityData.getHealthBlockCode(),
                healthFacilityData.getHealthFacilityCode());

        if (existHealthFacilityData != null) {
            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)) {
                healthFacilityRecordsDataService.delete(existHealthFacilityData);
                logger.info("HealthFacility data is successfully deleted.");
            } else {
                updateHealthFacilityDAta(existHealthFacilityData, healthFacilityData);
                logger.info("HealthFacility data is successfully updated.");
            }
        } else {
            HealthBlock healthBlockData = healthBlockRecordsDataService.findHealthBlockByParentCode(
                    healthFacilityData.getStateCode(), healthFacilityData.getDistrictCode(), healthFacilityData.getTalukaCode(), healthFacilityData.getHealthBlockCode());
            healthBlockData.getHealthBlock().add(healthFacilityData);
            healthBlockRecordsDataService.update(healthBlockData);
            logger.info("HealthFacility data is successfully inserted.");
        }
    }

    private void updateHealthFacilityDAta(HealthFacility existHealthFacilityData, HealthFacility healthFacilityData) {
        existHealthFacilityData.setName(healthFacilityData.getName());
        healthFacilityRecordsDataService.update(existHealthFacilityData);
    }
}
