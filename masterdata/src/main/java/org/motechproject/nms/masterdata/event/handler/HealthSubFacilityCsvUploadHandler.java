package org.motechproject.nms.masterdata.event.handler;

/**
 * This class handles the csv upload for success and failure events for HealthSubFacilityCsv.
 */

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.*;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.masterdata.service.TalukaService;
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

@Component
public class HealthSubFacilityCsvUploadHandler {

    private StateService stateService;

    private DistrictService districtService;

    private TalukaService talukaService;

    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;

    private HealthSubFacilityCsvRecordsDataService healthSubFacilityCsvRecordsDataService;

    private HealthSubFacilityRecordsDataService healthSubFacilityRecordsDataService;

    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(HealthSubFacilityCsvUploadHandler.class);

    @Autowired
    public HealthSubFacilityCsvUploadHandler(StateService stateService, DistrictService districtService, TalukaService talukaService, HealthFacilityRecordsDataService healthFacilityRecordsDataService, HealthSubFacilityCsvRecordsDataService healthSubFacilityCsvRecordsDataService, HealthSubFacilityRecordsDataService healthSubFacilityRecordsDataService, HealthBlockRecordsDataService healthBlockRecordsDataService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.stateService = stateService;
        this.districtService = districtService;
        this.talukaService = talukaService;
        this.healthFacilityRecordsDataService = healthFacilityRecordsDataService;
        this.healthSubFacilityCsvRecordsDataService = healthSubFacilityCsvRecordsDataService;
        this.healthSubFacilityRecordsDataService = healthSubFacilityRecordsDataService;
        this.healthBlockRecordsDataService = healthBlockRecordsDataService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in HealthSubFacility table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.HEALTH_SUB_FACILITY_CSV_SUCCESS})
    public void healthSubFacilityCsvSuccess(MotechEvent motechEvent) {

        logger.info("HEALTH_SUB_FACILITY_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);

        DateTime timeStamp = NmsUtils.getCurrentTimeStamp();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(timeStamp);

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setRecordType(RecordType.HEALTH_SUB_FACILITY);
        errorDetails.setTimeOfUpload(timeStamp);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        HealthSubFacilityCsv healthSubFacilityCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("HEALTH_SUB_FACILITY_CSV_SUCCESS event processing start for ID: {}", id);
                healthSubFacilityCsvRecord = healthSubFacilityCsvRecordsDataService.findById(id);

                if (healthSubFacilityCsvRecord != null) {
                    logger.info("Id exist in Health Sub Facility Temporary Entity");
                    bulkUploadStatus.setUploadedBy(healthSubFacilityCsvRecord.getOwner());
                    HealthSubFacility record = mapHealthSubFacilityCsv(healthSubFacilityCsvRecord);
                    processHealthSubFacilityData(record);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in Health Sub Facility Temporary Entity");
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    bulkUploadStatus.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("HEALTH_SUB_FACILITY_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(healthSubFacilityCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                bulkUploadStatus.incrementFailureCount();
            } catch (Exception e) {
                errorDetails.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetails.setRecordDetails("Exception occurred");
                errorDetails.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                bulkUploadStatus.incrementFailureCount();
                logger.error("HEALTH_SUB_FACILITY_CSV_SUCCESS processing receive Exception exception, message: {}", e);
            } finally {
                if (null != healthSubFacilityCsvRecord) {
                    healthSubFacilityCsvRecordsDataService.delete(healthSubFacilityCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }

    private HealthSubFacility mapHealthSubFacilityCsv(HealthSubFacilityCsv record) throws DataValidationException {
        HealthSubFacility newRecord = new HealthSubFacility();

        String healthSubFacilityName = ParseDataHelper.validateAndParseString("HealthSubFacilityName", record.getName(), true);
        Long stateCode = ParseDataHelper.validateAndParseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.validateAndParseLong("DistrictCode", record.getDistrictCode(), true);
        Long talukaCode = ParseDataHelper.validateAndParseLong("TalukaCode", record.getTalukaCode(), true);
        Long healthBlockCode = ParseDataHelper.validateAndParseLong("HealthBlockCode", record.getHealthBlockCode(), true);
        Long healthfacilityCode = ParseDataHelper.validateAndParseLong("HealthFacilityCode", record.getHealthFacilityCode(), true);
        Long healthSubFacilityCode = ParseDataHelper.validateAndParseLong("HealthSubFacilityCode", record.getHealthSubFacilityCode(), true);

        State state = stateService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", "StateCode");
        }

        District district = districtService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", "DistrictCode");
        }

        Taluka taluka = talukaService.findTalukaByParentCode(stateCode, districtCode, talukaCode);

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

    private void processHealthSubFacilityData(HealthSubFacility healthSubFacilityData) throws DataValidationException {

        logger.debug("Health Sub Facility data contains Sub Facility code : {}", healthSubFacilityData.getHealthSubFacilityCode());

        HealthSubFacility existHealthSubFacilityData = healthSubFacilityRecordsDataService.findHealthSubFacilityByParentCode(
                healthSubFacilityData.getStateCode(),
                healthSubFacilityData.getDistrictCode(),
                healthSubFacilityData.getTalukaCode(),
                healthSubFacilityData.getHealthBlockCode(),
                healthSubFacilityData.getHealthFacilityCode(),
                healthSubFacilityData.getHealthSubFacilityCode()
        );

        if (existHealthSubFacilityData != null) {
            updateHealthSubFacilityDAta(existHealthSubFacilityData, healthSubFacilityData);
            logger.info("HealthSubFacility Permanent data is successfully updated.");
        } else {

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
