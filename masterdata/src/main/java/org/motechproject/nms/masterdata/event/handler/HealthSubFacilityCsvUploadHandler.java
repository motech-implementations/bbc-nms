package org.motechproject.nms.masterdata.event.handler;

/**
 * This class handles the csv upload for success and failure events for HealthSubFacilityCsv.
 */

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

@Component
public class HealthSubFacilityCsvUploadHandler {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    private TalukaRecordsDataService talukaRecordsDataService;

    @Autowired
    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;

    @Autowired
    private HealthSubFacilityCsvRecordsDataService healthSubFacilityCsvRecordsDataService;

    @Autowired
    private HealthSubFacilityRecordsDataService healthSubFacilityRecordsDataService;

    @Autowired
    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(HealthSubFacilityCsvUploadHandler.class);

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in HealthSubFacility table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.HEALTH_SUB_FACILITY_CSV_SUCCESS})
    public void healthSubFacilityCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        logger.info("HEALTH_SUB_FACILITY_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();
        String userName = null;
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        HealthSubFacilityCsv healthSubFacilityCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("HEALTH_SUB_FACILITY_CSV_SUCCESS event processing start for ID: {}", id);
                healthSubFacilityCsvRecord = healthSubFacilityCsvRecordsDataService.findById(id);

                if (healthSubFacilityCsvRecord != null) {
                    logger.info("Id exist in Health Sub Facility Temporary Entity");
                    userName = healthSubFacilityCsvRecord.getOwner();
                    HealthSubFacility record = mapHealthSubFacilityCsv(healthSubFacilityCsvRecord);
                    processHealthSubFacilityData(record,healthSubFacilityCsvRecord.getOperation());
                    result.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in Health Sub Facility Temporary Entity");
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                    result.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("HEALTH_SUB_FACILITY_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(healthSubFacilityCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                result.incrementFailureCount();
            } catch (Exception e) {
                logger.error("HEALTH_SUB_FACILITY_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                result.incrementFailureCount();
            }
            finally {
                if(null != healthSubFacilityCsvRecord){
                    healthSubFacilityCsvRecordsDataService.delete(healthSubFacilityCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFileName, result);
    }

    private HealthSubFacility mapHealthSubFacilityCsv(HealthSubFacilityCsv record) throws DataValidationException {
        HealthSubFacility newRecord = new HealthSubFacility();

        String healthSubFacilityName = ParseDataHelper.parseString("HealthSubFacilityName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        String talukaCode = ParseDataHelper.parseString("TalukaCode", record.getTalukaCode(), true);
        Long healthBlockCode = ParseDataHelper.parseLong("HealthBlockCode", record.getHealthBlockCode(), true);
        Long healthfacilityCode = ParseDataHelper.parseLong("HealthFacilityCode", record.getHealthFacilityCode(), true);
        Long healthSubFacilityCode = ParseDataHelper.parseLong("HealthSubFacilityCode", record.getHealthSubFacilityCode(), true);

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

        HealthFacility healthFacility = healthFacilityRecordsDataService.findHealthFacilityByParentCode(stateCode, districtCode, talukaCode, healthBlockCode, healthfacilityCode);
        if (healthFacility == null) {
            ParseDataHelper.raiseInvalidDataException("HealthFacility", "HealthFacilityCode");
        }
        newRecord.setName(healthSubFacilityName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setHealthBlockCode(healthBlockCode);
        newRecord.setHealthFacilityCode(healthfacilityCode);
        newRecord.setHealthSubFacilityCode(healthSubFacilityCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void processHealthSubFacilityData(HealthSubFacility healthSubFacilityData, String operation) throws DataValidationException {

        logger.debug("Health Sub Facility data contains Sub Facility code : {}",healthSubFacilityData.getHealthSubFacilityCode());

        HealthSubFacility existHealthSubFacilityData = healthSubFacilityRecordsDataService.findHealthSubFacilityByParentCode(
                healthSubFacilityData.getStateCode(),
                healthSubFacilityData.getDistrictCode(),
                healthSubFacilityData.getTalukaCode(),
                healthSubFacilityData.getHealthBlockCode(),
                healthSubFacilityData.getHealthFacilityCode(),
                healthSubFacilityData.getHealthSubFacilityCode()
        );

        if (existHealthSubFacilityData != null) {
            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)) {

                HealthFacility healthFacilityDeleteRecord = healthFacilityRecordsDataService.findHealthFacilityByParentCode(
                        healthSubFacilityData.getStateCode(), healthSubFacilityData.getDistrictCode(),
                        healthSubFacilityData.getTalukaCode(), healthSubFacilityData.getHealthBlockCode(),
                        healthSubFacilityData.getHealthFacilityCode());

                healthFacilityDeleteRecord.getHealthSubFacility().remove(existHealthSubFacilityData);
                healthFacilityRecordsDataService.update(healthFacilityDeleteRecord);
                logger.info("HealthSubFacility data is successfully deleted.");
            } else {
                updateHealthSubFacilityDAta(existHealthSubFacilityData, healthSubFacilityData);
                logger.info("HealthSubFacility Permanent data is successfully updated.");
            }
        } else {

            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)){
                ParseDataHelper.raiseInvalidDataException("operation",MasterDataConstants.DELETE_OPERATION);
            }

            HealthFacility healthFacilityData = healthFacilityRecordsDataService.findHealthFacilityByParentCode(
                    healthSubFacilityData.getStateCode(), healthSubFacilityData.getDistrictCode(),
                    healthSubFacilityData.getTalukaCode(), healthSubFacilityData.getHealthBlockCode(),
                    healthSubFacilityData.getHealthFacilityCode());

            healthFacilityData.getHealthSubFacility().add(healthSubFacilityData);
            healthFacilityRecordsDataService.update(healthFacilityData);
            logger.info("HealthSubFacility Permanent data is successfully inserted.");
        }
    }

    private void updateHealthSubFacilityDAta(HealthSubFacility existHealthSubFacilityData, HealthSubFacility healthSubFacilityData) {
        existHealthSubFacilityData.setName(healthSubFacilityData.getName());
        healthSubFacilityRecordsDataService.update(existHealthSubFacilityData);
    }
}
