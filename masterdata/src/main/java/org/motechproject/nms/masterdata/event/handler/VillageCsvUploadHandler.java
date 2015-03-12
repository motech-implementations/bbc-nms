package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.VillageCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.masterdata.service.TalukaService;
import org.motechproject.nms.masterdata.service.VillageService;
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

/**
 * This class handles the csv upload for success and failure events for VillageCsv.
 */
@Component
public class VillageCsvUploadHandler {

    private StateService stateService;

    private DistrictService districtService;

    private TalukaService talukaService;

    private VillageCsvRecordsDataService villageCsvRecordsDataService;

    private VillageService villageService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(VillageCsvUploadHandler.class);

    @Autowired
    public VillageCsvUploadHandler(StateService stateService, DistrictService districtService, TalukaService talukaService, VillageCsvRecordsDataService villageCsvRecordsDataService, VillageService villageService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.stateService = stateService;
        this.districtService = districtService;
        this.talukaService = talukaService;
        this.villageCsvRecordsDataService = villageCsvRecordsDataService;
        this.villageService = villageService;
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

        logger.info("VILLAGE_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);

        DateTime timeStamp = NmsUtils.getCurrentTimeStamp();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(timeStamp);

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setRecordType(RecordType.VILLAGE);
        errorDetails.setTimeOfUpload(timeStamp);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        VillageCsv villageCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("VILLAGE_CSV_SUCCESS event processing start for ID: {}", id);
                villageCsvRecord = villageCsvRecordsDataService.findById(id);

                if (villageCsvRecord != null) {
                    logger.info("Id exist in Village Temporary Entity");
                    bulkUploadStatus.setUploadedBy(villageCsvRecord.getOwner());
                    Village record = mapVillageCsv(villageCsvRecord);
                    processVillageData(record);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in Village Temporary Entity");
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    bulkUploadStatus.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("VILLAGE_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(villageCsvRecord.toString());
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
                logger.error("VILLAGE_CSV_SUCCESS processing receive Exception exception, message: {}", e);
            } finally {
                if (null != villageCsvRecord) {
                    villageCsvRecordsDataService.delete(villageCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }

    private Village mapVillageCsv(VillageCsv record) throws DataValidationException {
        Village newRecord = new Village();

        String villageName = ParseDataHelper.parseString("VillageName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        Long talukaCode = ParseDataHelper.parseLong("TalukaCode", record.getTalukaCode(), true);
        Long villageCode = ParseDataHelper.parseLong("VillageCode", record.getVillageCode(), true);

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
        villageService.update(existVillageData);
    }
}
