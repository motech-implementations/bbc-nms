package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.StateCsv;
import org.motechproject.nms.masterdata.service.StateCsvService;
import org.motechproject.nms.masterdata.service.StateService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class handles the csv upload for success and failure events for State Csv.
 */
@Component
public class StateCsvUploadHandler {

    private StateService stateService;

    private StateCsvService stateCsvService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(StateCsvUploadHandler.class);

    @Autowired
    public StateCsvUploadHandler(StateService stateService, StateCsvService stateCsvService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.stateService = stateService;
        this.stateCsvService = stateCsvService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in State table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {LocationConstants.STATE_CSV_SUCCESS})
    public void stateCsvSuccess(MotechEvent motechEvent) {

        logger.info("STATE_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);

        DateTime timeStamp = new DateTime();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();

        BulkUploadError errorDetails = new BulkUploadError();

        ErrorLog.setErrorDetails(errorDetails, bulkUploadStatus, csvFileName, timeStamp, RecordType.STATE);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        StateCsv stateCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("STATE_CSV_SUCCESS event processing start for ID: {}", id);
                stateCsvRecord = stateCsvService.findById(id);

                if (stateCsvRecord != null) {
                    bulkUploadStatus.setUploadedBy(stateCsvRecord.getOwner());
                    logger.info("Id exist in State Temporary Entity");
                    State newRecord = mapStateCsv(stateCsvRecord);
                    processStateData(newRecord);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the CircleCsv table with id {}", id);
                    ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION, ErrorCategoryConstants.CSV_RECORD_MISSING, "Record is null");

                }
            } catch (DataValidationException dataException) {

                logger.error("STATE_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataException.getErroneousField());

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, dataException.getErroneousField(), dataException.getErrorCode(), stateCsvRecord.toString());

            } catch (Exception stateException) {

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION, ErrorCategoryConstants.GENERAL_EXCEPTION, "Exception occurred");

                logger.error("STATE_CSV_SUCCESS processing receive Exception exception, message: {}", stateException);
            } finally {
                if (null != stateCsvRecord) {
                    stateCsvService.delete(stateCsvRecord);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }


    private State mapStateCsv(StateCsv record) throws DataValidationException {

        State newRecord = new State();

        String stateName = ParseDataHelper.validateAndParseString("StateName", record.getName(), true);
        Long stateCode = ParseDataHelper.validateAndParseLong("StateCode", record.getStateCode(), true);
        Integer maCapping = ParseDataHelper.validateAndParseInt("maCapping", record.getMaCapping(), false);
        Integer mkCapping = ParseDataHelper.validateAndParseInt("mkCapping", record.getMkCapping(), false);

        newRecord.setName(stateName);
        newRecord.setStateCode(stateCode);
        newRecord.setMaCapping(maCapping);
        newRecord.setMkCapping(mkCapping);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void processStateData(State stateData) throws DataValidationException {

        logger.debug("State data contains state code : {}", stateData.getStateCode());
        State stateExistData = stateService.findRecordByStateCode(stateData.getStateCode());

        if (null != stateExistData) {
            updateState(stateExistData, stateData);
            logger.info("State data is successfully updated.");
        } else {
            stateService.create(stateData);
            logger.info("State data is successfully inserted.");
        }
    }

    private void updateState(State stateExistData, State stateData) {

        stateExistData.setName(stateData.getName());
        stateExistData.setMaCapping(stateData.getMaCapping());
        stateExistData.setMkCapping(stateData.getMkCapping());
        stateExistData.setModifiedBy(stateData.getModifiedBy());

        stateService.update(stateExistData);
    }

}
