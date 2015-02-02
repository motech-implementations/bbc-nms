package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.StateCsv;
import org.motechproject.nms.masterdata.repository.StateCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
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
public class StateCsvUploadHandler {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private StateCsvRecordsDataService stateCsvRecordsDataService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(StateCsvUploadHandler.class);

    @MotechListener(subjects = {MasterDataConstants.STATE_CSV_SUCCESS})
    public void stateCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        StateCsv stateCsvRecord = null;

        for (Long id : createdIds) {
            try {
                stateCsvRecord = stateCsvRecordsDataService.findById(id);

                if (stateCsvRecord != null) {
                    State newRecord = mapStateCsv(stateCsvRecord);
                    insertStateData(newRecord,stateCsvRecord.getOperation());
                    result.incrementSuccessCount();
                    stateCsvRecordsDataService.delete(stateCsvRecord);
                } else {
                    result.incrementFailureCount();
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                }
            } catch (DataValidationException dataValidationException) {
                errorDetails.setRecordDetails(stateCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                stateCsvRecordsDataService.delete(stateCsvRecord);
            } catch (Exception e) {
                failedRecordCount++;
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary("userName", csvFileName, logFileName, result);
    }

    @MotechListener(subjects = {MasterDataConstants.STATE_CSV_FAILED})
    public void stateCsvFailed(MotechEvent motechEvent) {

        Map<String, Object> params = motechEvent.getParameters();
        logger.info(String.format("Start processing StateCsv-import failure for upload %s", params.toString()));
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            logger.info(String.format("Record deleted successfully from StateCsv table for id %s", id.toString()));
            StateCsv stateCsv = stateCsvRecordsDataService.findById(id);
            stateCsvRecordsDataService.delete(stateCsv);
        }
        logger.info("Failure method finished for StateCsv");
    }

    private State mapStateCsv(StateCsv record) throws DataValidationException {
        State newRecord = new State();

        String stateName = ParseDataHelper.parseString("StateName", record.getName(), true);

        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);

        newRecord.setName(stateName);
        newRecord.setStateCode(stateCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }

    private void insertStateData(State stateData, String operation) {

        State stateExistData = stateRecordsDataService.findRecordByStateCode(stateData.getStateCode());

        if (null != stateExistData) {

            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)) {
                stateRecordsDataService.delete(stateExistData);
                logger.info("State data is successfully deleted.");
            } else {
                updateState(stateExistData, stateData);
                logger.info("State data is successfully updated.");
            }
        } else {
            stateRecordsDataService.create(stateData);
            logger.info("State data is successfully inserted.");
        }
    }

    private void updateState(State stateExistData,State stateData){

        stateExistData.setName(stateData.getName());
        stateRecordsDataService.update(stateExistData);
    }

}
