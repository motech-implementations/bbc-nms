package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeServiceCsv;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
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
 * This class handles the csv upload for success and failure events for LanguageLocationCodeCsv.
 */
@Component
public class LanguageLocationCodeCsvHandler {
    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    private LanguageLocationCodeServiceCsv languageLocationCodeServiceCsv;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private LocationService locationService;

    private static Logger logger = LoggerFactory.getLogger(LanguageLocationCodeCsvHandler.class);

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in LanguageLocationCode table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = MasterDataConstants.LANGUAGE_LOCATION_CODE_CSV_SUCCESS)
    public void languageLocationCodeCsvSuccess(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("CIRCLE_CSV_SUCCESS event received");
        LanguageLocationCodeCsv record = null;
        String userName = null;

        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary result = new CsvProcessingSummary();

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvFileName);

        for (Long id : createdIds) {
            try {
                record = languageLocationCodeServiceCsv.getRecord(id);
                if (record != null) {
                    userName = record.getOwner();
                    LanguageLocationCode newRecord = mapLanguageLocationCodeFrom(record);

                    LanguageLocationCode oldRecord = languageLocationCodeService.getRecordByLocationCode(
                            newRecord.getStateCode(), newRecord.getDistrictCode());
                    if (oldRecord != null) {
                        if (OperationType.DEL.toString().equals(record.getOperation())) {
                            languageLocationCodeService.delete(oldRecord);
                            logger.info("Record deleted successfully for statecode : {} and districtcode : {}", newRecord.getStateCode(), newRecord.getDistrictCode());
                        } else {
                            oldRecord = copyLanguageLocationCodeForUpdate(newRecord, oldRecord);
                            languageLocationCodeService.update(oldRecord);
                            logger.info("Record updated successfully for statecode : {} and districtcode : {}", newRecord.getStateCode(), newRecord.getDistrictCode());
                        }
                        result.incrementSuccessCount();

                    } else if (OperationType.DEL.toString().equals(record.getOperation())) {
                        logger.error("Record for deletion not found in the LanguageLocation table with state code " +
                                        "{} and district code", newRecord.getStateCode(), newRecord.getDistrictCode());
                        errorDetail.setErrorDescription(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION);
                        errorDetail.setErrorCategory(ErrorCategoryConstants.INVALID_DATA);
                        errorDetail.setRecordDetails("State and District Code combination invalid");
                        bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);

                        result.incrementFailureCount();

                    } else {
                        languageLocationCodeService.create(newRecord);
                        logger.info("Record created successfully for statecode : {} and districtcode : {}", newRecord.getStateCode(), newRecord.getDistrictCode());
                        result.incrementSuccessCount();
                    }

                } else {
                    logger.error("Record not found in the LanguageLocationCodeCsv table with id {}", id);
                    errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetail.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                    result.incrementFailureCount();
                }
            } catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(ex.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                result.incrementFailureCount();
            } catch (Exception e) {
                logger.error("LANGUAGE_LOCATION_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                errorDetail.setErrorCategory("");
                errorDetail.setRecordDetails("");
                errorDetail.setErrorDescription("");
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                result.incrementFailureCount();
                throw e;
            }
            finally{
                if(null != record){
                    languageLocationCodeServiceCsv.delete(record);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvFileName, errorFileName, result);
        logger.info("Finished processing LanguageLocationCodeCsv-import success");
    }

    /**
     * This method handle the event which is raised after csv upload is failed.
     * This method also deletes all the csv records which get inserted in this upload..
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = MasterDataConstants.LANGUAGE_LOCATION_CODE_CSV_FAILED)
    public void languageLocationCodeCsvFailure(MotechEvent motechEvent) {
        Map<String, Object> params = motechEvent.getParameters();
        logger.info("LANGUAGE_LOCATION_CSV_FAILED event received");

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            LanguageLocationCodeCsv oldRecord = languageLocationCodeServiceCsv.getRecord(id);
            if (oldRecord != null) {
                logger.debug("LANGUAGE_LOCATION_CSV_FAILED event processing start for ID: {}", id);
                languageLocationCodeServiceCsv.delete(oldRecord);
            }
        }
        logger.info("CIRCLE_CSV_FAILED event processing finished");
    }


    /**
     *  This method is used to validate csv uploaded record
     *  and map LanguageLocationCodeCsv to LanguageLocationCode
     *
     * @param record of LanguageLocationCodeCsv type
     * @return Operator record after the mapping
     * @throws DataValidationException
     */
    private LanguageLocationCode mapLanguageLocationCodeFrom(LanguageLocationCodeCsv record)
            throws DataValidationException {

        LanguageLocationCode newRecord = null;

        Long stateCode = ParseDataHelper.parseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true);
        String circleCode = ParseDataHelper.parseString("CircleCode", record.getCircleCode(), true);

        State state = locationService.getStateByCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("stateCode", record.getStateCode());
        }

        District district = locationService.getDistrictByCode(state.getId(), districtCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("districtCode", record.getDistrictCode());
        }

        Circle circle = circleService.getRecordByCode(circleCode);
        if (circle == null) {
            ParseDataHelper.raiseInvalidDataException("circleCode", record.getCircleCode());
        }

        newRecord = new LanguageLocationCode();

        /* Fill newRecord with values from CSV */
        newRecord.setStateCode(ParseDataHelper.parseLong("StateCode", record.getStateCode(), true));
        newRecord.setDistrictCode(ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true));
        newRecord.setCircleCode(ParseDataHelper.parseString("CircleCode", record.getCircleCode(), true));

        newRecord.setCircle(circle);
        newRecord.setState(state);
        newRecord.setDistrict(district);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        newRecord.setLanguageLocationCode(ParseDataHelper.parseInt("LanguageLocationCode",
                record.getLanguageLocationCode(), true));
        newRecord.setLanguageKK(ParseDataHelper.parseString("LanguageKK", record.getLanguageKK(), true));
        newRecord.setLanguageMK(ParseDataHelper.parseString("LanguageMK", record.getLanguageMK(), true));
        newRecord.setLanguageMA(ParseDataHelper.parseString("LanguageMA", record.getLanguageMA(), true));


        /* Update the Default Language Location Codes in Circle entity */
        String valueOfIsDefLangLocCode = ParseDataHelper.parseString("isDefaultLanguageLocationCode",
                record.getIsDefaultLanguageLocationCode(), true);
        Boolean isDefaultLangLocCode = (
                MasterDataConstants.YES_FOR_DEFAULT_LANG_LOC_CODE.equalsIgnoreCase(valueOfIsDefLangLocCode));

        if (isDefaultLangLocCode) {
            circle.setDefaultLanguageLocationCode(newRecord.getLanguageLocationCode());
            circleService.update(circle);
        }

        return newRecord;
    }

    /**
     * Copies the field values from new Record to oldRecord for update in DB
     * @param newRecord mapped from CSV values
     * @param oldRecord to be updated in DB
     * @return oldRecord after copied values
     */
    private  LanguageLocationCode copyLanguageLocationCodeForUpdate(LanguageLocationCode newRecord,
                                                              LanguageLocationCode oldRecord) {

        oldRecord.setStateCode(newRecord.getStateCode());
        oldRecord.setDistrictCode(newRecord.getDistrictCode());
        oldRecord.setCircleCode(newRecord.getCircleCode());

        oldRecord.setCircle(newRecord.getCircle());
        oldRecord.setState(newRecord.getState());
        oldRecord.setDistrict(newRecord.getDistrict());
        oldRecord.setModifiedBy(newRecord.getModifiedBy());

        oldRecord.setLanguageLocationCode(newRecord.getLanguageLocationCode());

        oldRecord.setLanguageMA(newRecord.getLanguageMA());
        oldRecord.setLanguageMK(newRecord.getLanguageMK());
        oldRecord.setLanguageKK(newRecord.getLanguageKK());

        return oldRecord;
    }

}

