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

    private static Logger logger = LoggerFactory.getLogger(TalukaCsvUploadHandler.class);

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

                    State stateRecord = stateRecordsDataService.findRecordByStateCode(newRecord.getStateCode());
                    District districtRecord = districtRecordsDataService.findDistrictByParentCode(newRecord.getDistrictCode(), newRecord.getStateCode());
                    Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());

                    if (stateRecord != null && districtRecord != null && talukaRecord != null) {
                        insertHealthBlockData(talukaRecord,newRecord);
                        result.incrementSuccessCount();
                        healthBlockCsvRecordsDataService.delete(healthBlockCsvRecord);
                    } else {
                        result.incrementFailureCount();
                        errorDetails.setRecordDetails(id.toString());
                        errorDetails.setErrorCategory("Record_Not_Found");
                        errorDetails.setErrorDescription("Record not in database");
                        bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                    }
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
    public void healthBlockCsvFailed(MotechEvent event) {

        healthBlockCsvRecordsDataService.deleteAll();

        logger.info("HealthBlock successfully deleted from temporary tables");
    }

    private HealthBlock mapHealthBlockCsv(HealthBlockCsv record) throws DataValidationException {
        HealthBlock newRecord = new HealthBlock();

        String healthBlockName = ParseDataHelper.parseString("TalukaName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        String talukaCode = ParseDataHelper.parseString("TalukaCode", record.getTalukaCode(), true);
        Long healthBlockCode = ParseDataHelper.parseLong("HealthBlockCode", record.getHealthBlockCode(), true);

        newRecord.setName(healthBlockName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setHealthBlockCode(healthBlockCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }

    private void insertHealthBlockData(Taluka talukaData,HealthBlock healthBlockData) {

        HealthBlock existHealthBlockData = healthBlockRecordsDataService.findHealthBlockByParentCode(
                healthBlockData.getStateCode(),
                healthBlockData.getDistrictCode(),
                healthBlockData.getTalukaCode(),
                healthBlockData.getHealthBlockCode());

        if (existHealthBlockData != null) {

            healthBlockRecordsDataService.update(healthBlockData);
            logger.info("HealthBlock permanent data is successfully updated.");
        } else {
            talukaData.getHealthBlock().add(healthBlockData);
            talukaRecordsDataService.create(talukaData);
            logger.info("HealthBlock permanent data is successfully updated.");
        }
    }
}
