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
 * This class handles the csv upload for success and failure events for StateCsv.
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

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in State table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.STATE_CSV_SUCCESS})
    public void stateCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        logger.info("STATE_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();
        String userName = null;

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        StateCsv stateCsvRecord = null;

        for (Long id : createdIds) {
            try {

                logger.debug("STATE_CSV_SUCCESS event processing start for ID: {}", id);
                stateCsvRecord = stateCsvRecordsDataService.findById(id);

                if (stateCsvRecord != null) {
                    userName = stateCsvRecord.getOwner();
                    logger.info("Id exist in State Temporary Entity");
                    State newRecord = mapStateCsv(stateCsvRecord);
                    insertStateData(newRecord,stateCsvRecord.getOperation());
                    result.incrementSuccessCount();

                } else {
                    logger.info("Id do not exist in State Temporary Entity");
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                    result.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {

                logger.error("STATE_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(stateCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                result.incrementFailureCount();
            } catch (Exception e) {

                logger.error("STATE_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                result.incrementFailureCount();
            }
              finally {
                if(null != stateCsvRecord){
                    stateCsvRecordsDataService.delete(stateCsvRecord);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFileName, result);
    }

    /**
     * This method handle the event which is raised after csv upload is failed.
     * This method also deletes all the csv records which get inserted in this upload..
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.STATE_CSV_FAILED})
    public void stateCsvFailed(MotechEvent motechEvent) {

        Map<String, Object> params = motechEvent.getParameters();
        logger.info("STATE_CSV_FAILED event received");
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            logger.debug("STATE_CSV_FAILED event processing start for ID: {}", id);
            StateCsv stateCsv = stateCsvRecordsDataService.findById(id);
            stateCsvRecordsDataService.delete(stateCsv);
        }
        logger.info("STATE_CSV_FAILED event processing finished");
    }

    private State mapStateCsv(StateCsv record) throws DataValidationException {
        State newRecord = new State();

        String stateName = ParseDataHelper.parseString("StateName", record.getName(), true);

        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);

        newRecord.setName(stateName);
        newRecord.setStateCode(stateCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void insertStateData(State stateData, String operation) {


        logger.debug("State data contains state code : {}",stateData.getStateCode() );
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
