package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.TalukaCsv;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
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
 * This class handles the csv upload for success and failure events for TalukaCsv.
 */
@Component
public class TalukaCsvUploadHandler {

    private StateRecordsDataService stateRecordsDataService;

    private DistrictRecordsDataService districtRecordsDataService;

    private TalukaCsvRecordsDataService talukaCsvRecordsDataService;

    private TalukaRecordsDataService talukaRecordsDataService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(TalukaCsvUploadHandler.class);

    @Autowired
    public TalukaCsvUploadHandler(StateRecordsDataService stateRecordsDataService, DistrictRecordsDataService districtRecordsDataService, TalukaCsvRecordsDataService talukaCsvRecordsDataService, TalukaRecordsDataService talukaRecordsDataService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.stateRecordsDataService = stateRecordsDataService;
        this.districtRecordsDataService = districtRecordsDataService;
        this.talukaCsvRecordsDataService = talukaCsvRecordsDataService;
        this.talukaRecordsDataService = talukaRecordsDataService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in Taluka table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.TALUKA_CSV_SUCCESS})
    public void talukaCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        logger.info("TALUKA_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);

        DateTime timeStamp = NmsUtils.getCurrentTimeStamp();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(timeStamp);

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setRecordType(RecordType.TALUKA);
        errorDetails.setTimeOfUpload(timeStamp);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        TalukaCsv talukaCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("TALUKA_CSV_SUCCESS event processing start for ID: {}", id);
                talukaCsvRecord = talukaCsvRecordsDataService.findById(id);

                if (talukaCsvRecord != null) {
                    logger.info("Id exist in Taluka Temporary Entity");
                    bulkUploadStatus.setUploadedBy(talukaCsvRecord.getOwner());
                    Taluka record = mapTalukaCsv(talukaCsvRecord);
                    processTalukaData(record);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in Taluka Temporary Entity");
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    bulkUploadStatus.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("TALUKA_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(talukaCsvRecord.toString());
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
                logger.error("TALUKA_CSV_SUCCESS processing receive Exception exception, message: {}", e);
            } finally {
                if (null != talukaCsvRecord) {
                    talukaCsvRecordsDataService.delete(talukaCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }

    private Taluka mapTalukaCsv(TalukaCsv record) throws DataValidationException {
        Taluka newRecord = new Taluka();

        String talukaName = ParseDataHelper.parseString("TalukaName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        String talukaCode = ParseDataHelper.parseString("TalukaCode", record.getTalukaCode(), true);

        State state = stateRecordsDataService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", "StateCode");
        }

        District district = districtRecordsDataService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", "districtCode");
        }

        newRecord.setName(talukaName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void processTalukaData(Taluka talukaData) throws DataValidationException {

        Taluka existTalukaData = talukaRecordsDataService.findTalukaByParentCode(
                talukaData.getStateCode(),
                talukaData.getDistrictCode(),
                talukaData.getTalukaCode());

        if (existTalukaData != null) {
            updateTaluka(existTalukaData, talukaData);
            logger.info("Taluka data is successfully updated.");

        } else {
            District districtData = districtRecordsDataService.findDistrictByParentCode(talukaData.getDistrictCode(), talukaData.getStateCode());
            districtData.getTaluka().add(talukaData);
            districtRecordsDataService.update(districtData);
            logger.info("Taluka data is successfully inserted.");
        }
    }

    private void updateTaluka(Taluka existTalukaData, Taluka talukaData) {
        existTalukaData.setName(talukaData.getName());
        talukaRecordsDataService.update(existTalukaData);
    }
}
