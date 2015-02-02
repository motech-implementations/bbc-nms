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

        logger.info("DISTRICT_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        String logFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);
        CsvProcessingSummary result = new CsvProcessingSummary(successRecordCount, failedRecordCount);
        BulkUploadError errorDetails = new BulkUploadError();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        DistrictCsv districtCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("DISTRICT_CSV_SUCCESS event processing start for ID: {}", id);
                districtCsvRecord = districtCsvRecordsDataService.findById(id);
                District record = mapDistrictCsv(districtCsvRecord);

                if (districtCsvRecord != null) {
                    logger.info("Id exist in District Temporary Entity");
                    insertDistrictData(record,districtCsvRecord.getOperation());
                    result.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in District Temporary Entity");
                    errorDetails.setRecordDetails(id.toString());
                    errorDetails.setErrorCategory("Record_Not_Found");
                    errorDetails.setErrorDescription("Record not in database");
                    bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                    result.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("DISTRICT_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(districtCsvRecord.toString());
                errorDetails.setErrorCategory(dataValidationException.getErrorCode());
                errorDetails.setErrorDescription(dataValidationException.getErroneousField());
                bulkUploadErrLogService.writeBulkUploadErrLog(logFileName, errorDetails);
                result.incrementFailureCount();
            } catch (Exception e) {
                logger.error("DISTRICT_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                result.incrementFailureCount();
            }
            finally {
                if(null != districtCsvRecord){
                    districtCsvRecordsDataService.delete(districtCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary("userName", csvFileName, logFileName, result);
    }

    @MotechListener(subjects = {MasterDataConstants.DISTRICT_CSV_FAILED})
    public void districtCsvFailed(MotechEvent motechEvent) {

        Map<String, Object> params = motechEvent.getParameters();
        logger.info("DISTRICT_CSV_FAILED event received");
        List<Long> createdIds = (List<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            logger.debug("DISTRICT_CSV_FAILED event processing start for ID: {}", id);
            DistrictCsv districtCsv = districtCsvRecordsDataService.findById(id);
            districtCsvRecordsDataService.delete(districtCsv);
        }
        logger.info("DISTRICT_CSV_FAILED event processing finished");
    }

    private District mapDistrictCsv(DistrictCsv record) throws DataValidationException {

        String districtName = ParseDataHelper.parseString("District Name", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);

        State state = stateRecordsDataService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", null);
        }

        District newRecord = new District();

        newRecord.setName(districtName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        return newRecord;
    }

    private void insertDistrictData(District districtData, String operation) {

        logger.debug("District data contains district code : {}",districtData.getDistrictCode());
        District existDistrictData = districtRecordsDataService.findDistrictByParentCode(districtData.getDistrictCode(), districtData.getStateCode());
        if (null != existDistrictData) {
            if (null != operation && operation.toUpperCase().equals(MasterDataConstants.DELETE_OPERATION)) {
                districtRecordsDataService.delete(existDistrictData);
                logger.info("District data is successfully deleted.");
            } else {
                updateDistrict(existDistrictData, districtData);
                logger.info("District data is successfully updated.");
            }
        } else {
            State stateData = stateRecordsDataService.findRecordByStateCode(districtData.getStateCode());
            stateData.getDistrict().add(districtData);
            stateRecordsDataService.update(stateData);
            logger.info("District data is successfully inserted.");
        }
    }

    private void updateDistrict(District existDistrictData, District districtData) {
        existDistrictData.setName(districtData.getName());
        districtRecordsDataService.update(existDistrictData);
    }
}