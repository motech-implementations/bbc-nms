package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthFacilityCsv;
import org.motechproject.nms.masterdata.service.HealthBlockService;
import org.motechproject.nms.masterdata.service.HealthFacilityCsvService;
import org.motechproject.nms.masterdata.service.HealthFacilityService;
import org.motechproject.nms.masterdata.service.ValidatorService;
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
 * This class handles the csv upload for success and failure events for Health Facility Csv.
 */

@Component
public class HealthFacilityCsvUploadHandler {

    private ValidatorService validatorService;

    private HealthFacilityCsvService healthFacilityCsvService;

    private HealthFacilityService healthFacilityService;

    private HealthBlockService healthBlockService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(HealthFacilityCsvUploadHandler.class);

    @Autowired
    public HealthFacilityCsvUploadHandler(HealthBlockService healthBlockService,HealthFacilityCsvService healthFacilityCsvService, HealthFacilityService healthFacilityService, BulkUploadErrLogService bulkUploadErrLogService,ValidatorService validatorService) {
        this.healthBlockService = healthBlockService;
        this.healthFacilityCsvService = healthFacilityCsvService;
        this.healthFacilityService = healthFacilityService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.validatorService = validatorService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in HealthFacility table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {LocationConstants.HEALTH_FACILITY_CSV_SUCCESS})
    public void healthFacilityCsvSuccess(MotechEvent motechEvent) {

        logger.info("HEALTH_FACILITY_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        DateTime timeStamp = new DateTime();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();

        BulkUploadError errorDetails = new BulkUploadError();

        ErrorLog.setErrorDetails(errorDetails, bulkUploadStatus, csvFileName, timeStamp, RecordType.HEALTH_FACILITY);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        HealthFacilityCsv healthFacilityCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("HEALTH_FACILITY_CSV_SUCCESS event processing start for ID: {}", id);
                healthFacilityCsvRecord = healthFacilityCsvService.findById(id);

                if (null != healthFacilityCsvRecord) {
                    logger.info("Id exist in HealthFacility Temporary Entity");
                    bulkUploadStatus.setUploadedBy(healthFacilityCsvRecord.getOwner());
                    HealthFacility record = mapHealthFacilityCsv(healthFacilityCsvRecord);
                    processHealthFacilityData(record);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in HealthFacility Temporary Entity");
                    ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION, ErrorCategoryConstants.CSV_RECORD_MISSING, "Record is null");

                }
            } catch (DataValidationException healthFacilityDataException) {
                logger.error("HEALTH_BLOCK_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", healthFacilityDataException.getErroneousField());

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, healthFacilityDataException.getErroneousField(), healthFacilityDataException.getErrorCode(), healthFacilityCsvRecord.toString());

            } catch (Exception healthFacilityException) {

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION, ErrorCategoryConstants.GENERAL_EXCEPTION, "Exception occurred");

                logger.error("HEALTH_BLOCK_CSV_SUCCESS processing receive Exception exception, message: {}", healthFacilityException);
            } finally {
                if (null != healthFacilityCsvRecord) {
                    healthFacilityCsvService.delete(healthFacilityCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }


    private HealthFacility mapHealthFacilityCsv(HealthFacilityCsv record) throws DataValidationException {
        HealthFacility newRecord = new HealthFacility();

        String healthFacilityName = ParseDataHelper.validateAndParseString("HealthFacilityName", record.getName(), true);
        Long stateCode = ParseDataHelper.validateAndParseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.validateAndParseLong("DistrictCode", record.getDistrictCode(), true);
        Long talukaCode = ParseDataHelper.validateAndParseLong("TalukaCode", record.getTalukaCode(), true);
        Long healthBlockCode = ParseDataHelper.validateAndParseLong("HealthBlockCode", record.getHealthBlockCode(), true);
        Long facilityCode = ParseDataHelper.validateAndParseLong("FacilityCode", record.getHealthFacilityCode(), true);
        Integer facilityType = ParseDataHelper.validateAndParseInt("FacilityType", record.getHealthFacilityType(), true);

        validatorService.validateHealthFacilityParent(stateCode,districtCode,talukaCode,healthBlockCode);

        newRecord.setName(healthFacilityName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setHealthBlockCode(healthBlockCode);
        newRecord.setHealthFacilityCode(facilityCode);
        newRecord.setHealthFacilityType(facilityType);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void processHealthFacilityData(HealthFacility healthFacilityData) throws DataValidationException {

        logger.debug("Health Facility data contains facility code : {}", healthFacilityData.getHealthFacilityCode());
        HealthFacility existHealthFacilityData = healthFacilityService.findHealthFacilityByParentCode(
                healthFacilityData.getStateCode(),
                healthFacilityData.getDistrictCode(),
                healthFacilityData.getTalukaCode(),
                healthFacilityData.getHealthBlockCode(),
                healthFacilityData.getHealthFacilityCode());

        if (existHealthFacilityData != null) {
            updateHealthFacilityData(existHealthFacilityData, healthFacilityData);
            logger.info("HealthFacility data is successfully updated.");
        } else {

            HealthBlock healthBlockData = healthBlockService.findHealthBlockByParentCode(
                    healthFacilityData.getStateCode(), healthFacilityData.getDistrictCode(),
                    healthFacilityData.getTalukaCode(), healthFacilityData.getHealthBlockCode());

            healthBlockData.getHealthFacility().add(healthFacilityData);
            healthBlockService.update(healthBlockData);
            logger.info("HealthFacility data is successfully inserted.");
        }
    }

    private void updateHealthFacilityData(HealthFacility existHealthFacilityData, HealthFacility healthFacilityData) {
        existHealthFacilityData.setName(healthFacilityData.getName());
        existHealthFacilityData.setHealthFacilityType(healthFacilityData.getHealthFacilityType());
        existHealthFacilityData.setModifiedBy(healthFacilityData.getModifiedBy());
        healthFacilityService.update(existHealthFacilityData);
    }
}
