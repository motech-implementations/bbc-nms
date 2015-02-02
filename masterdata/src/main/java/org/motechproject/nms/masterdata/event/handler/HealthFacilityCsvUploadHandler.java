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
 * Created by abhishek on 31/1/15.
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


    @MotechListener(subjects = {MasterDataConstants.HEALTH_FACILITY_CSV_SUCCESS})
    public void healthFacilityCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        HealthFacilityCsv healthFacilityCsvRecord = null;

        for (Long id : createdIds) {
            try {
                healthFacilityCsvRecord = healthFacilityCsvRecordsDataService.findById(id);

                if (healthFacilityCsvRecord != null) {
                    HealthFacility newRecord = mapHealthFacilityCsv(healthFacilityCsvRecord);
                    HealthBlock healthBlockRecord = healthBlockRecordsDataService.findHealthBlockByParentCode(
                            newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode(), newRecord.getHealthBlockCode());
                    insertHealthFacilityData(healthBlockRecord, newRecord);
                    result.incrementSuccessCount();
                    healthFacilityCsvRecordsDataService.delete(healthFacilityCsvRecord);
                } else {
                    result.incrementFailureCount();
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                }
            } catch (DataValidationException dataValidationException) {
                errorDetails.setRecordDetails(healthFacilityCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                healthFacilityCsvRecordsDataService.delete(healthFacilityCsvRecord);
            } catch (Exception e) {
                failedRecordCount++;
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("userName", csvFileName, logFileName, result);
    }

    @MotechListener(subjects = {MasterDataConstants.HEALTH_FACILITY_CSV_FAILED})
    public void healthFacilityCsvFailed(MotechEvent motechEvent) {

        Map<String, Object> params = motechEvent.getParameters();
        logger.info(String.format("Start processing HealthFacilityCsv-import failure for upload %s", params.toString()));
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            logger.info(String.format("Record deleted successfully from HealthFacilityCsv table for id %s", id.toString()));
            HealthFacilityCsv healthFacilityCsv = healthFacilityCsvRecordsDataService.findById(id);
            healthFacilityCsvRecordsDataService.delete(healthFacilityCsv);
        }
        logger.info("Failure method finished for HealthFacilityCsv");
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
            ParseDataHelper.raiseInvalidDataException("State", null);
        }

        District district = districtRecordsDataService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", null);
        }

        Taluka taluka = talukaRecordsDataService.findTalukaByParentCode(stateCode, districtCode, talukaCode);

        if (taluka == null) {
            ParseDataHelper.raiseInvalidDataException("Taluka", null);
        }

        HealthBlock healthBlock = healthBlockRecordsDataService.findHealthBlockByParentCode(
                stateCode, districtCode, talukaCode, healthBlockCode);
        if (healthBlock == null) {
            ParseDataHelper.raiseInvalidDataException("HealthBlock", null);
        }
        newRecord.setName(healthFacilityName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setHealthBlockCode(healthBlockCode);
        newRecord.setHealthFacilityCode(facilityCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }

    private void insertHealthFacilityData(HealthBlock healthBlockData, HealthFacility healthFacilityData) {

        HealthFacility existHealthFacilityData = healthFacilityRecordsDataService.findHealthFacilityByParentCode(
                healthFacilityData.getStateCode(),
                healthFacilityData.getDistrictCode(),
                healthFacilityData.getTalukaCode(),
                healthFacilityData.getHealthBlockCode(),
                healthFacilityData.getHealthFacilityCode());

        if (existHealthFacilityData != null) {

            updateHealthFacilityDAta(existHealthFacilityData, healthFacilityData);
            logger.info("HealthFacility data is successfully updated.");
        } else {
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
