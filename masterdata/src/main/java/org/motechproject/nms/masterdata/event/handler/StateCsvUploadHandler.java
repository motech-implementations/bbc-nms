package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.StateCsv;
import org.motechproject.nms.masterdata.repository.StateCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
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
 * This class handles the csv upload for success and failure events for StateCsv.
 */
@Component
public class StateCsvUploadHandler {

    private StateRecordsDataService stateRecordsDataService;

    private StateCsvRecordsDataService stateCsvRecordsDataService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(StateCsvUploadHandler.class);

    @Autowired
    public StateCsvUploadHandler(StateRecordsDataService stateRecordsDataService, StateCsvRecordsDataService stateCsvRecordsDataService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.stateRecordsDataService = stateRecordsDataService;
        this.stateCsvRecordsDataService = stateCsvRecordsDataService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in State table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.STATE_CSV_SUCCESS})
    public void stateCsvSuccess(MotechEvent motechEvent) {

        logger.info("STATE_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);

        DateTime timeStamp = NmsUtils.getCurrentTimeStamp();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(timeStamp);

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setRecordType(RecordType.STATE);
        errorDetails.setTimeOfUpload(timeStamp);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        StateCsv stateCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("STATE_CSV_SUCCESS event processing start for ID: {}", id);
                stateCsvRecord = stateCsvRecordsDataService.findById(id);

                if (stateCsvRecord != null) {
                    bulkUploadStatus.setUploadedBy(stateCsvRecord.getOwner());
                    logger.info("Id exist in State Temporary Entity");
                    State newRecord = mapStateCsv(stateCsvRecord);
                    processStateData(newRecord);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the CircleCsv table with id {}", id);
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    bulkUploadStatus.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {

                logger.error("STATE_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(stateCsvRecord.toString());
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
                logger.error("STATE_CSV_SUCCESS processing receive Exception exception, message: {}", e);
            } finally {
                if (null != stateCsvRecord) {
                    stateCsvRecordsDataService.delete(stateCsvRecord);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }

    private State mapStateCsv(StateCsv record) throws DataValidationException {

        State newRecord = new State();

        String stateName = ParseDataHelper.parseString("StateName", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Integer maCapping = ParseDataHelper.parseInt("maCapping", record.getMaCapping(), false);
        Integer mkCapping = ParseDataHelper.parseInt("mkCapping", record.getMkCapping(), false);

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
        State stateExistData = stateRecordsDataService.findRecordByStateCode(stateData.getStateCode());

        if (null != stateExistData) {
            updateState(stateExistData, stateData);
            logger.info("State data is successfully updated.");
        } else {
            stateRecordsDataService.create(stateData);
            logger.info("State data is successfully inserted.");
        }
    }

    private void updateState(State stateExistData, State stateData) {

        stateExistData.setName(stateData.getName());
        stateExistData.setMaCapping(stateData.getMaCapping());
        stateExistData.setMkCapping(stateData.getMkCapping());

        stateRecordsDataService.update(stateExistData);
    }

}
