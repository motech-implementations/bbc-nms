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
 * This class handles the csv upload for success and failure events for VillageCsv.
 */
@Component
public class VillageCsvUploadHandler {

    private StateRecordsDataService stateRecordsDataService;

    private DistrictRecordsDataService districtRecordsDataService;

    private TalukaRecordsDataService talukaRecordsDataService;

    private VillageCsvRecordsDataService villageCsvRecordsDataService;

    private VillageRecordsDataService villageRecordsDataService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(VillageCsvUploadHandler.class);

    @Autowired
    public VillageCsvUploadHandler(StateRecordsDataService stateRecordsDataService, DistrictRecordsDataService districtRecordsDataService, TalukaRecordsDataService talukaRecordsDataService, VillageCsvRecordsDataService villageCsvRecordsDataService, VillageRecordsDataService villageRecordsDataService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.stateRecordsDataService = stateRecordsDataService;
        this.districtRecordsDataService = districtRecordsDataService;
        this.talukaRecordsDataService = talukaRecordsDataService;
        this.villageCsvRecordsDataService = villageCsvRecordsDataService;
        this.villageRecordsDataService = villageRecordsDataService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in Village table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.VILLAGE_CSV_SUCCESS})
    public void villageCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        logger.info("VILLAGE_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();
        String userName = null;

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        VillageCsv villageCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("VILLAGE_CSV_SUCCESS event processing start for ID: {}", id);
                villageCsvRecord = villageCsvRecordsDataService.findById(id);

                if (villageCsvRecord != null) {
                    logger.info("Id exist in Village Temporary Entity");
                    userName = villageCsvRecord.getOwner();
                    Village record = mapVillageCsv(villageCsvRecord);
                    processVillageData(record, villageCsvRecord.getOperation());
                    result.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in Village Temporary Entity");
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                    result.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("VILLAGE_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(villageCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                result.incrementFailureCount();
            } catch (Exception e) {
                logger.error("VILLAGE_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                result.incrementFailureCount();
            } finally {
                if (null != villageCsvRecord) {
                    villageCsvRecordsDataService.delete(villageCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFileName, result);
    }

    private Village mapVillageCsv(VillageCsv record) throws DataValidationException {
        Village newRecord = new Village();

        String villageName = ParseDataHelper.parseString("VillageName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        String talukaCode = ParseDataHelper.parseString("TalukaCode", record.getTalukaCode(), true);
        Long villageCode = ParseDataHelper.parseLong("VillageCode", record.getVillageCode(), true);

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

    private void processVillageData(Village villageData, String operation) throws DataValidationException {

        logger.debug("Village data contains village code : {}", villageData.getVillageCode());
        Village existVillageData = villageRecordsDataService.findVillageByParentCode(
                villageData.getStateCode(),
                villageData.getDistrictCode(),
                villageData.getTalukaCode(),
                villageData.getVillageCode());

        if (existVillageData != null) {
            updateVillage(existVillageData, villageData);
            logger.info("Village data is successfully updated.");
        } else {

            Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(villageData.getStateCode(),
                    villageData.getDistrictCode(), villageData.getTalukaCode());
            talukaRecord.getVillage().add(villageData);
            talukaRecordsDataService.update(talukaRecord);
            logger.info("Village data is successfully inserted.");
        }
    }

    private void updateVillage(Village existVillageData, Village villageData) {
        existVillageData.setName(villageData.getName());
        villageRecordsDataService.update(existVillageData);
    }
}
