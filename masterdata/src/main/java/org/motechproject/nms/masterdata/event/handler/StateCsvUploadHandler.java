package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
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

    @MotechListener(subjects = {"mds.crud.masterdata.StateCsv.csv-import.success"})
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
                    insertStateData(newRecord);
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

    @MotechListener(subjects = {"mds.crud.masterdata.StateCsv.csv-import.failed"})
    public void stateCsvFailed(MotechEvent event) {

        stateCsvRecordsDataService.deleteAll();
        logger.info("State successfully deleted from Temporary table");
    }

    private State mapStateCsv(StateCsv record) throws DataValidationException {
        State newRecord = new State();

        String stateName = ParseDataHelper.parseString("StateName", record.getName(), true);

        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);

        newRecord.setName(stateName);
        newRecord.setStateCode(stateCode);
        
        return newRecord;
    }

    private void insertStateData(State stateData) {

        State stateExistData = stateRecordsDataService.findRecordByStateCode(stateData.getStateCode());

        if (null != stateExistData) {
            stateExistData.setName(stateData.getName());
            stateRecordsDataService.update(stateExistData);
            logger.info("State permanent data is successfully updated.");
        } else {
            stateRecordsDataService.create(stateData);
            logger.info("State permanent data is successfully inserted.");
        }
    }

}
