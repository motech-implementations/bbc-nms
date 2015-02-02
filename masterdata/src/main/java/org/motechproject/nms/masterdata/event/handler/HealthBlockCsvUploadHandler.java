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
public class HealthBlockCsvUploadHandler {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    private TalukaRecordsDataService talukaRecordsDataService;

    @Autowired
    private HealthBlockCsvRecordsDataService healthBlockCsvRecordsDataService;

    @Autowired
    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(HealthBlockCsvUploadHandler.class);

    @MotechListener(subjects = {MasterDataConstants.HEALTH_BLOCK_CSV_SUCCESS})
    public void healthBlockCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        HealthBlockCsv healthBlockCsvRecord = null;

        for (Long id : createdIds) {
            try {
                healthBlockCsvRecord = healthBlockCsvRecordsDataService.findById(id);

                if (healthBlockCsvRecord != null) {
                        HealthBlock newRecord = mapHealthBlockCsv(healthBlockCsvRecord);
                        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());
                        insertHealthBlockData(talukaRecord,newRecord,healthBlockCsvRecord.getOperation());
                        result.incrementSuccessCount();
                        healthBlockCsvRecordsDataService.delete(healthBlockCsvRecord);
                } else {
                    result.incrementFailureCount();
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                }
            } catch (DataValidationException dataValidationException) {
                errorDetails.setRecordDetails(healthBlockCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                healthBlockCsvRecordsDataService.delete(healthBlockCsvRecord);
            } catch (Exception e) {
                failedRecordCount++;
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("userName", csvFileName, logFileName, result);
    }

    @MotechListener(subjects = {MasterDataConstants.HEALTH_BLOCK_CSV_FAILED})
    public void healthBlockCsvFailed(MotechEvent motechEvent) {

        Map<String, Object> params = motechEvent.getParameters();
        logger.info(String.format("Start processing HealthBlockCsv-import failure for upload %s", params.toString()));
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            logger.info(String.format("Record deleted successfully from HealthBlockCsv table for id %s", id.toString()));
            HealthBlockCsv healthBlockCsv = healthBlockCsvRecordsDataService.findById(id);
            healthBlockCsvRecordsDataService.delete(healthBlockCsv);
        }
        logger.info("Failure method finished for HealthBlockCsv");
    }

    private HealthBlock mapHealthBlockCsv(HealthBlockCsv record) throws DataValidationException {
        HealthBlock newRecord = new HealthBlock();

        String healthBlockName = ParseDataHelper.parseString("TalukaName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        String talukaCode = ParseDataHelper.parseString("TalukaCode", record.getTalukaCode(), true);
        Long healthBlockCode = ParseDataHelper.parseLong("HealthBlockCode", record.getHealthBlockCode(), true);

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
        newRecord.setName(healthBlockName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setHealthBlockCode(healthBlockCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }

    private void insertHealthBlockData(Taluka talukaData, HealthBlock healthBlockData, String operation) {

        HealthBlock existHealthBlockData = healthBlockRecordsDataService.findHealthBlockByParentCode(
                healthBlockData.getStateCode(),
                healthBlockData.getDistrictCode(),
                healthBlockData.getTalukaCode(),
                healthBlockData.getHealthBlockCode());

        if (existHealthBlockData != null) {
            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)) {
                healthBlockRecordsDataService.delete(existHealthBlockData);
                logger.info("HealthBlock data is successfully deleted.");
            } else {
                updateHealthBlock(existHealthBlockData, healthBlockData);
                logger.info("HealthBlock data is successfully updated.");
            }
        } else {
            talukaData.getHealthBlock().add(healthBlockData);
            talukaRecordsDataService.create(talukaData);
            logger.info("HealthBlock data is successfully inserted.");
        }
    }

    private void updateHealthBlock(HealthBlock existHealthBlockData, HealthBlock healthBlockData) {
        existHealthBlockData.setName(healthBlockData.getName());
        healthBlockRecordsDataService.update(existHealthBlockData);
    }
}
