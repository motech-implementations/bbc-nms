package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.DistrictCsv;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.DistrictCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.StateService;
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
 * This class handles the csv upload for success and failure events for DistrictCsv.
 */

@Component
public class DistrictCsvUploadHandler {

    private DistrictCsvRecordsDataService districtCsvRecordsDataService;

    private DistrictService districtService;

    private StateService stateService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(DistrictCsvUploadHandler.class);

    @Autowired
    public DistrictCsvUploadHandler(DistrictCsvRecordsDataService districtCsvRecordsDataService, DistrictService districtService, StateService stateService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.districtCsvRecordsDataService = districtCsvRecordsDataService;
        this.districtService = districtService;
        this.stateService = stateService;
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

        logger.info("DISTRICT_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        DateTime timeStamp = NmsUtils.getCurrentTimeStamp();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(timeStamp);

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setRecordType(RecordType.DISTRICT);
        errorDetails.setTimeOfUpload(timeStamp);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        DistrictCsv districtCsvRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("DISTRICT_CSV_SUCCESS event processing start for ID: {}", id);
                districtCsvRecord = districtCsvRecordsDataService.findById(id);

                if (districtCsvRecord != null) {
                    logger.info("Id exist in District Temporary Entity");
                    bulkUploadStatus.setUploadedBy(districtCsvRecord.getOwner());
                    District record = mapDistrictCsv(districtCsvRecord);
                    processDistrictData(record);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in District Temporary Entity");
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    bulkUploadStatus.incrementFailureCount();
                }
            } catch (DataValidationException dataValidationException) {
                logger.error("DISTRICT_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", dataValidationException.getErroneousField());
                errorDetails.setRecordDetails(districtCsvRecord.toString());
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
                logger.error("DISTRICT_CSV_SUCCESS processing receive Exception exception, message: {}", e);
            } finally {
                if (null != districtCsvRecord) {
                    districtCsvRecordsDataService.delete(districtCsvRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }

    private District mapDistrictCsv(DistrictCsv record) throws DataValidationException {

        String districtName = ParseDataHelper.parseString("District Name", record.getName(), true);
        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);

        State state = stateService.findRecordByStateCode(stateCode);
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
        District existDistrictData = districtService.findDistrictByParentCode(districtData.getDistrictCode(), districtData.getStateCode());
        if (null != existDistrictData) {
            updateDistrict(existDistrictData, districtData);
            logger.info("District data is successfully updated.");
        } else {

            State stateData = stateService.findRecordByStateCode(districtData.getStateCode());
            stateData.getDistrict().add(districtData);
            stateService.update(stateData);
            logger.info("District data is successfully inserted.");
        }
    }

    private void updateDistrict(District existDistrictData, District districtData) {
        existDistrictData.setName(districtData.getName());
        districtService.update(existDistrictData);
    }
}