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
 * Created by abhishek on 29/1/15.
 */
@Component
public class TalukaCsvUploadHandler {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;


    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    private TalukaCsvRecordsDataService talukaCsvRecordsDataService;

    @Autowired
    private TalukaRecordsDataService talukaRecordsDataService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(TalukaCsvUploadHandler.class);

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

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        TalukaCsv talukaCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("TALUKA_CSV_SUCCESS event processing start for ID: {}", id);
                talukaCsvRecord = talukaCsvRecordsDataService.findById(id);

                if (talukaCsvRecord != null) {
                    logger.info("Id exist in Taluka Temporary Entity");
                    Taluka newRecord = mapTalukaCsv(talukaCsvRecord);
                    District districtRecord = districtRecordsDataService.findDistrictByParentCode(newRecord.getDistrictCode(), newRecord.getStateCode());
                    insertTalukaData(districtRecord, newRecord, talukaCsvRecord.getOperation());
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
            }
            finally {
                if(null != talukaCsvRecord){
                    talukaCsvRecordsDataService.delete(talukaCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("userName", csvFileName, logFileName, result);
    }

    @MotechListener(subjects = {MasterDataConstants.TALUKA_CSV_FAILED})
    public void talukaCsvFailed(MotechEvent motechEvent) {

        Map<String, Object> params = motechEvent.getParameters();
        logger.info("TALUKA_CSV_FAILED event received");
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            logger.debug("TALUKA_CSV_FAILED event processing start for ID: {}", id);
            TalukaCsv talukaCsv = talukaCsvRecordsDataService.findById(id);
            talukaCsvRecordsDataService.delete(talukaCsv);
        }
        logger.info("TALUKA_CSV_FAILED event processing finished");
    }

    private Taluka mapTalukaCsv(TalukaCsv record) throws DataValidationException {
        Taluka newRecord = new Taluka();

        String talukaName = ParseDataHelper.parseString("TalukaName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        String talukaCode = ParseDataHelper.parseString("TalukaCode", record.getTalukaCode(), true);

        State state = stateRecordsDataService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", null);
        }

        District district = districtRecordsDataService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", null);
        }

        newRecord.setName(talukaName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }

    private void insertTalukaData(District districtData, Taluka talukaData, String operation) {

        Taluka existTalukaData = talukaRecordsDataService.findTalukaByParentCode(
                talukaData.getStateCode(),
                talukaData.getDistrictCode(),
                talukaData.getTalukaCode());

        if (existTalukaData != null) {
            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)) {
                talukaRecordsDataService.delete(existTalukaData);
                logger.info("Taluka data is successfully deleted.");
            } else {
                updateTaluka(existTalukaData, talukaData);
                logger.info("Taluka data is successfully updated.");
            }
        } else {
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
