package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.HealthBlockRecordsDataService;
import org.motechproject.nms.masterdata.service.*;
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
 * This class handles the csv upload for success and failure events for Health Block Csv.
 */
@Component
public class HealthBlockCsvUploadHandler {

    private StateService stateService;

    private DistrictService districtService;

    private TalukaService talukaService;

    private HealthBlockCsvService healthBlockCsvService;

    private HealthBlockService healthBlockService;

    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(HealthBlockCsvUploadHandler.class);

    @Autowired
    public HealthBlockCsvUploadHandler(StateService stateService, DistrictService districtService, TalukaService talukaService, HealthBlockCsvService healthBlockCsvService, HealthBlockService healthBlockService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.stateService = stateService;
        this.districtService = districtService;
        this.talukaService = talukaService;
        this.healthBlockCsvService = healthBlockCsvService;
        this.healthBlockService = healthBlockService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in HealthBlock table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {LocationConstants.HEALTH_BLOCK_CSV_SUCCESS})
    public void healthBlockCsvSuccess(MotechEvent motechEvent) {

        logger.info("HEALTH_BLOCK_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");

        logger.debug("Csv file name received in event : {}", csvFileName);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        processRecords(createdIds, csvFileName);

    }


    public void processRecords(List<Long> CreatedId,
                               String csvFileName) {
        logger.info("Record Processing Started for csv file: {}", csvFileName);

        healthBlockRecordsDataService
                .doInTransaction(new TransactionCallback<State>() {

                    List<Long> CreatedId;

                    String csvFileName;

                    private TransactionCallback<State> init(
                            List<Long> CreatedId,
                            String csvFileName) {
                        this.CreatedId = CreatedId;
                        this.csvFileName = csvFileName;
                        return this;
                    }

                    @Override
                    public State doInTransaction(
                            TransactionStatus status) {
                        State transactionObject = null;
                        processRecordsInTransaction(csvFileName, CreatedId);
                        return transactionObject;
                    }
                }.init(CreatedId, csvFileName));
        logger.info("Record Processing complete for csv file: {}", csvFileName);
    }


    private void processRecordsInTransaction(String csvFileName, List<Long> createdIds) {
        DateTime timeStamp = new DateTime();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();

        BulkUploadError errorDetails = new BulkUploadError();

        ErrorLog.setErrorDetails(errorDetails, bulkUploadStatus, csvFileName, timeStamp, RecordType.HEALTH_BLOCK);


        CsvHealthBlock csvHealthBlockRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("HEALTH_BLOCK_CSV_SUCCESS event processing start for ID: {}", id);
                csvHealthBlockRecord = healthBlockCsvService.findById(id);

                if (csvHealthBlockRecord != null) {
                    logger.info("Id exist in HealthBlock Temporary Entity");
                    bulkUploadStatus.setUploadedBy(csvHealthBlockRecord.getOwner());
                    HealthBlock record = mapHealthBlockCsv(csvHealthBlockRecord);
                    processHealthBlockData(record);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in HealthBlock Temporary Entity");
                    ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION, ErrorCategoryConstants.CSV_RECORD_MISSING, "Record is null");

                }
            } catch (DataValidationException dataValidationException) {
                logger.error("HEALTH_BLOCK_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, dataValidationException.getErroneousField(), dataValidationException.getErrorCode(), csvHealthBlockRecord.toString());
            } catch (Exception e) {

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION, ErrorCategoryConstants.GENERAL_EXCEPTION, "Exception occurred");

                logger.error("HEALTH_BLOCK_CSV_SUCCESS processing receive Exception exception, message: {}", e);
            } finally {
                if (null != csvHealthBlockRecord) {
                    healthBlockCsvService.delete(csvHealthBlockRecord);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }


    private HealthBlock mapHealthBlockCsv(CsvHealthBlock record) throws DataValidationException {
        HealthBlock newRecord = new HealthBlock();

        String healthBlockName = ParseDataHelper.validateAndParseString("HealthBlockName", record.getName(), true);
        Long stateCode = ParseDataHelper.validateAndParseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.validateAndParseLong("DistrictCode", record.getDistrictCode(), true);
        Long talukaCode = ParseDataHelper.validateAndParseLong("TalukaCode", record.getTalukaCode(), true);
        Long healthBlockCode = ParseDataHelper.validateAndParseLong("HealthBlockCode", record.getHealthBlockCode(), true);

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
        newRecord.setName(healthBlockName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setHealthBlockCode(healthBlockCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void processHealthBlockData(HealthBlock healthBlockData) throws DataValidationException {

        logger.debug("Health Block data contains Health Block code : {}", healthBlockData.getHealthBlockCode());
        HealthBlock existHealthBlockData = healthBlockService.findHealthBlockByParentCode(
                healthBlockData.getStateCode(),
                healthBlockData.getDistrictCode(),
                healthBlockData.getTalukaCode(),
                healthBlockData.getHealthBlockCode());

        if (existHealthBlockData != null) {
            updateHealthBlock(existHealthBlockData, healthBlockData);
            logger.info("HealthBlock data is successfully updated.");

        } else {

            Taluka talukaRecord = talukaService.findTalukaByParentCode(healthBlockData.getStateCode(), healthBlockData.getDistrictCode(), healthBlockData.getTalukaCode());
            talukaRecord.getHealthBlock().add(healthBlockData);
            talukaService.update(talukaRecord);
            logger.info("HealthBlock data is successfully inserted.");
        }
    }

    private void updateHealthBlock(HealthBlock existHealthBlockData, HealthBlock healthBlockData) {
        existHealthBlockData.setName(healthBlockData.getName());
        existHealthBlockData.setModifiedBy(healthBlockData.getModifiedBy());
        healthBlockService.update(existHealthBlockData);
    }
}
