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
 * This class handles the csv upload for success and failure events for DistrictCsv.
 */

@Component
public class DistrictCsvUploadHandler {

    private DistrictCsvRecordsDataService districtCsvRecordsDataService;

    private DistrictRecordsDataService districtRecordsDataService;

    private StateRecordsDataService stateRecordsDataService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(DistrictCsvUploadHandler.class);

    @Autowired
    public DistrictCsvUploadHandler(DistrictCsvRecordsDataService districtCsvRecordsDataService, DistrictRecordsDataService districtRecordsDataService, StateRecordsDataService stateRecordsDataService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.districtCsvRecordsDataService = districtCsvRecordsDataService;
        this.districtRecordsDataService = districtRecordsDataService;
        this.stateRecordsDataService = stateRecordsDataService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in District table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {MasterDataConstants.DISTRICT_CSV_SUCCESS})
    public void districtCsvSuccess(MotechEvent motechEvent) {

        int failedRecordCount = 0;
        int successRecordCount = 0;

        logger.info("DISTRICT_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String userName = null;
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

                if (districtCsvRecord != null) {
                    District record = mapDistrictCsv(districtCsvRecord);
                    userName = districtCsvRecord.getOwner();
                    logger.info("Id exist in District Temporary Entity");
                    processDistrictData(record);
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
            } finally {
                if (null != districtCsvRecord) {
                    districtCsvRecordsDataService.delete(districtCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, logFileName, result);
    }

    private District mapDistrictCsv(DistrictCsv record) throws DataValidationException {

        String districtName = ParseDataHelper.parseString("District Name", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);

        State state = stateRecordsDataService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", "StateCode");
        }

        District newRecord = new District();

        newRecord.setName(districtName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    private void processDistrictData(District districtData) throws DataValidationException {

        logger.debug("District data contains district code : {}", districtData.getDistrictCode());
        District existDistrictData = districtRecordsDataService.findDistrictByParentCode(districtData.getDistrictCode(), districtData.getStateCode());
        if (null != existDistrictData) {
            updateDistrict(existDistrictData, districtData);
            logger.info("District data is successfully updated.");
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