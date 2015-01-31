package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.DistrictCsv;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.DistrictCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
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
public class DistrictCsvUploadHandler {

    @Autowired
    private DistrictCsvRecordsDataService districtCsvRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(DistrictCsvUploadHandler.class);

    @MotechListener(subjects = {MasterDataConstants.DISTRICT_CSV_SUCCESS})
    public void districtCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        DistrictCsv districtCsvRecord = null;

        for (Long id : createdIds) {
            try {
                districtCsvRecord = districtCsvRecordsDataService.findById(id);

                if (districtCsvRecord != null) {
                    District newRecord = mapDistrictCsv(districtCsvRecord);
                    State stateRecord = stateRecordsDataService.findRecordByStateCode(newRecord.getStateCode());

                    if (stateRecord != null) {
                        insertDistrictData(stateRecord,newRecord);
                        result.incrementSuccessCount();
                        districtCsvRecordsDataService.delete(districtCsvRecord);
                    } else {
                        result.incrementFailureCount();
                        errorDetails.setRecordDetails(id.toString());
                        errorDetails.setErrorCategory("Record_Not_Found");
                        errorDetails.setErrorDescription("Record not in database");
                        bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                    }
                } else {
                    result.incrementFailureCount();
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                }
            } catch (DataValidationException dataValidationException) {
                errorDetails.setRecordDetails(districtCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                districtCsvRecordsDataService.delete(districtCsvRecord);
            } catch (Exception e) {
                failedRecordCount++;
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("userName", csvFileName, logFileName, result);
    }

    @MotechListener(subjects = {MasterDataConstants.DISTRICT_CSV_FAILED})
    public void districtCsvFailed() {

        districtCsvRecordsDataService.deleteAll();
        logger.info("District successfully deleted from temporary tables");
    }

    private District mapDistrictCsv(DistrictCsv record) throws DataValidationException {

        District newRecord =new District();
        String districtName = ParseDataHelper.parseString("District Name", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);

        newRecord.setName(districtName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }

    private void insertDistrictData(State stateData,District districtData) {

        District dist = districtRecordsDataService.findDistrictByParentCode(districtData.getDistrictCode(), districtData.getStateCode());

        if (dist != null) {
            districtRecordsDataService.update(districtData);
            logger.info("District permanent data is successfully updated.");
        } else {
            stateData.getDistrict().add(districtData);
            stateRecordsDataService.update(stateData);
            logger.info("District permanent data is successfully inserted.");
        }
    }

    private State isStateExist(Long stateCode) {

        State stateRecord = stateRecordsDataService.findRecordByStateCode(stateCode);
        if (stateRecord != null) {
            return stateRecord;
        }
        return null;
    }

}