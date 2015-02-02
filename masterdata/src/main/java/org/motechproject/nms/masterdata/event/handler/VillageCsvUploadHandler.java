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
public class VillageCsvUploadHandler {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    private TalukaRecordsDataService talukaRecordsDataService;

    @Autowired
    private VillageCsvRecordsDataService villageCsvRecordsDataService;

    @Autowired
    private VillageRecordsDataService villageRecordsDataService;


    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(VillageCsvUploadHandler.class);

    public VillageCsvUploadHandler() {
    }

    @MotechListener(subjects = {MasterDataConstants.VILLAGE_CSV_SUCCESS})
    public void villageCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        VillageCsv villageCsvRecord = null;

        for (Long id : createdIds) {
            try {
                villageCsvRecord = villageCsvRecordsDataService.findById(id);

                if (villageCsvRecord != null) {
                    Village newRecord = mapVillageCsv(villageCsvRecord);
                    Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());
                    insertVillageData(talukaRecord, newRecord,villageCsvRecord.getOperation());
                    result.incrementSuccessCount();
                    villageCsvRecordsDataService.delete(villageCsvRecord);
                } else {
                    result.incrementFailureCount();
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                }
            } catch (DataValidationException dataValidationException) {
                errorDetails.setRecordDetails(villageCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                villageCsvRecordsDataService.delete(villageCsvRecord);
            } catch (Exception e) {
                failedRecordCount++;
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("userName", csvFileName, logFileName, result);
    }

    @MotechListener(subjects = {MasterDataConstants.VILLAGE_CSV_FAILED})
    public void villageCsvFailed(MotechEvent motechEvent) {

        Map<String, Object> params = motechEvent.getParameters();
        logger.info(String.format("Start processing VillageCsv-import failure for upload %s", params.toString()));
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            logger.info(String.format("Record deleted successfully from VillageCsv table for id %s", id.toString()));
            VillageCsv villageCsv = villageCsvRecordsDataService.findById(id);
            villageCsvRecordsDataService.delete(villageCsv);
        }
        logger.info("Failure method finished for VillageCsv");
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

        newRecord.setName(villageName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setVillageCode(villageCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }

    private void insertVillageData(Taluka talukaData, Village villageData, String operation) {

        Village existVillageData = villageRecordsDataService.findVillageByParentCode(
                villageData.getStateCode(),
                villageData.getDistrictCode(),
                villageData.getTalukaCode(),
                villageData.getVillageCode());

        if (existVillageData != null) {
            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)) {
                villageRecordsDataService.delete(existVillageData);
                logger.info("Village data is successfully deleted.");
            } else {
                updateVillage(existVillageData, villageData);
                logger.info("Village data is successfully updated.");
            }
        } else {
            talukaData.getVillage().add(villageData);
            talukaRecordsDataService.create(talukaData);
            logger.info("Village data is successfully inserted.");
        }
    }

    private void updateVillage(Village existVillageData, Village villageData) {
        existVillageData.setName(villageData.getName());
        villageRecordsDataService.update(existVillageData);
    }
}
