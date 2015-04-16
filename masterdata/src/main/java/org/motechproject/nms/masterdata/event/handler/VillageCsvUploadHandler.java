package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.VillageRecordsDataService;
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
 * This class handles the csv upload for success and failure events for VillageCsv.
 */
@Component
public class VillageCsvUploadHandler {

    private StateService stateService;

    private DistrictService districtService;

    private TalukaService talukaService;

    private VillageCsvService villageCsvService;

    private VillageService villageService;

    private VillageRecordsDataService villageRecordsDataService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(VillageCsvUploadHandler.class);

    @Autowired
    public VillageCsvUploadHandler(StateService stateService, DistrictService districtService, TalukaService talukaService, VillageCsvService villageCsvService, VillageService villageService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.stateService = stateService;
        this.districtService = districtService;
        this.talukaService = talukaService;
        this.villageCsvService = villageCsvService;
        this.villageService = villageService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in Village table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {LocationConstants.VILLAGE_CSV_SUCCESS})
    public void villageCsvSuccess(MotechEvent motechEvent) {

        logger.info("VILLAGE_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        processRecords(createdIds, csvFileName);

    }


    public void processRecords(List<Long> CreatedId,
                               String csvFileName) {
        logger.info("Record Processing Started for csv file: {}", csvFileName);

        villageRecordsDataService
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

        ErrorLog.setErrorDetails(errorDetails, bulkUploadStatus, csvFileName, timeStamp, RecordType.VILLAGE);


        CsvVillage csvVillageRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("VILLAGE_CSV_SUCCESS event processing start for ID: {}", id);
                csvVillageRecord = villageCsvService.findById(id);

                if (csvVillageRecord != null) {
                    logger.info("Id exist in Village Temporary Entity");
                    bulkUploadStatus.setUploadedBy(csvVillageRecord.getOwner());
                    Village record = mapVillageCsv(csvVillageRecord);
                    processVillageData(record);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in Village Temporary Entity");
                    ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION, ErrorCategoryConstants.CSV_RECORD_MISSING, "Record is null");

                }
            } catch (DataValidationException dataValidationException) {
                logger.error("VILLAGE_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, dataValidationException.getErroneousField(), dataValidationException.getErrorCode(), csvVillageRecord.toString());

            } catch (Exception e) {

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION, ErrorCategoryConstants.GENERAL_EXCEPTION, "Exception occurred");

                logger.error("VILLAGE_CSV_SUCCESS processing receive Exception exception, message: {}", e);
            } finally {
                if (null != csvVillageRecord) {
                    villageCsvService.delete(csvVillageRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }

    private Village mapVillageCsv(CsvVillage record) throws DataValidationException {
        Village newRecord = new Village();

        String villageName = ParseDataHelper.validateAndParseString("VillageName", record.getName(), true);
        Long stateCode = ParseDataHelper.validateAndParseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.validateAndParseLong("DistrictCode", record.getDistrictCode(), true);
        Long talukaCode = ParseDataHelper.validateAndParseLong("TalukaCode", record.getTalukaCode(), true);
        Long villageCode = ParseDataHelper.validateAndParseLong("VillageCode", record.getVillageCode(), true);

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

        newRecord.setName(villageName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setVillageCode(villageCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void processVillageData(Village villageData) throws DataValidationException {

        logger.debug("Village data contains village code : {}", villageData.getVillageCode());
        Village existVillageData = villageService.findVillageByParentCode(
                villageData.getStateCode(),
                villageData.getDistrictCode(),
                villageData.getTalukaCode(),
                villageData.getVillageCode());

        if (existVillageData != null) {
            updateVillage(existVillageData, villageData);
            logger.info("Village data is successfully updated.");
        } else {

            Taluka talukaRecord = talukaService.findTalukaByParentCode(villageData.getStateCode(),
                    villageData.getDistrictCode(), villageData.getTalukaCode());
            talukaRecord.getVillage().add(villageData);
            talukaService.update(talukaRecord);
            logger.info("Village data is successfully inserted.");
        }
    }

    private void updateVillage(Village existVillageData, Village villageData) {
        existVillageData.setName(villageData.getName());
        existVillageData.setModifiedBy(villageData.getModifiedBy());
        villageService.update(existVillageData);
    }
}
