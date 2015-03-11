package org.motechproject.nms.masterdata.event.handler;

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
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();
        String userName = null;
        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        TalukaCsv talukaCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("TALUKA_CSV_SUCCESS event processing start for ID: {}", id);
                talukaCsvRecord = talukaCsvRecordsDataService.findById(id);

                if (talukaCsvRecord != null) {
                    logger.info("Id exist in Taluka Temporary Entity");
                    userName = talukaCsvRecord.getOwner();
                    Taluka record = mapTalukaCsv(talukaCsvRecord);
                    processTalukaData(record, talukaCsvRecord.getOperation());
                    result.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in Taluka Temporary Entity");
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                    result.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("TALUKA_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(talukaCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                result.incrementFailureCount();
            } catch (Exception e) {
                logger.error("TALUKA_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                result.incrementFailureCount();
            } finally {
                if (null != talukaCsvRecord) {
                    talukaCsvRecordsDataService.delete(talukaCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFileName, result);
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

    private void processTalukaData(Taluka talukaData, String operation) throws DataValidationException {

        Taluka existTalukaData = talukaRecordsDataService.findTalukaByParentCode(
                talukaData.getStateCode(),
                talukaData.getDistrictCode(),
                talukaData.getTalukaCode());

        if (existTalukaData != null) {
            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)) {
                District districtData = districtRecordsDataService.findDistrictByParentCode(talukaData.getDistrictCode(), talukaData.getStateCode());
                districtData.getTaluka().remove(talukaData);
                districtRecordsDataService.update(districtData);
                logger.info("Taluka data is successfully deleted.");
            } else {
                updateTaluka(existTalukaData, talukaData);
                logger.info("Taluka data is successfully updated.");
            }
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
