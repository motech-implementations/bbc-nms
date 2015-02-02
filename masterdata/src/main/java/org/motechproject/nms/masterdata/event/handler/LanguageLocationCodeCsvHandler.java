package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.OperationType;
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
        logger.info("Start processing LanguageLocationCodeCsv-import success for upload {}", params.toString());
        LanguageLocationCodeCsv record = null;
        String userName = null;

        BulkUploadError errorDetail = new BulkUploadError();
        CsvProcessingSummary summary = new CsvProcessingSummary();


        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvImportFileName = (String) params.get("csv-import.filename");
        String errorFileName = BulkUploadError.createBulkUploadErrLogFileName(csvImportFileName);

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
                            newRecord.setOwner(oldRecord.getOwner());
                            newRecord.setModifiedBy(userName);
                            languageLocationCodeService.update(newRecord);
                            logger.info("Record updated successfully for statecode : {} and districtcode : {}", newRecord.getStateCode(), newRecord.getDistrictCode());
                        }
                    } else {
                        newRecord.setOwner(userName);
                        newRecord.setModifiedBy(userName);
                        languageLocationCodeService.create(newRecord);
                        logger.info("Record created successfully for statecode : {} and districtcode : {}", newRecord.getStateCode(), newRecord.getDistrictCode());
                    }
                    languageLocationCodeServiceCsv.delete(record);
                    summary.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the LanguageLocationCodeCsv table with id {}", id);
                    errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetail.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                    summary.incrementFailureCount();
                }
            } catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(ex.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorFileName, errorDetail);
                summary.incrementFailureCount();
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName, csvImportFileName, errorFileName, summary);
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
        logger.info("Start processing LanguageLocationCodeCsv-import failure for upload {}", params.toString());

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");

        for (Long id : createdIds) {
            LanguageLocationCodeCsv oldRecord = languageLocationCodeServiceCsv.getRecord(id);
            if (oldRecord != null) {
                languageLocationCodeServiceCsv.delete(oldRecord);
                logger.info("Record deleted successfully from LanguageLocationCodeCsv table for id {}", id.toString());
            }
        }
        logger.info("Finished processing LanguageLocationCodeCsv-import failure");
    }


    private LanguageLocationCode mapLanguageLocationCodeFrom(LanguageLocationCodeCsv record) throws DataValidationException {
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


        /* Update the Default Language Location Codes in Circle entity */
        circle.setDefaultLanguageLocationCodeMK(ParseDataHelper.parseInt("DefaultLanguageLocationCodeMK",
                record.getDefaultLanguageLocationCodeMK(), true));
        circle.setDefaultLanguageLocationCodeKK(ParseDataHelper.parseInt("DefaultLanguageLocationCodeMA",
                record.getDefaultLanguageLocationCodeMA(), true));
        circle.setDefaultLanguageLocationCodeKK(ParseDataHelper.parseInt("defaultLanguageLocationCodeKK",
                record.getDefaultLanguageLocationCodeKK(), true));

        circleService.update(circle);

        /* Create a new object of LanguageLocationCode and fill it with values from CSV */
        newRecord = new LanguageLocationCode();

        newRecord.setStateCode(ParseDataHelper.parseLong("StateCode", record.getStateCode(), true));
        newRecord.setDistrictCode(ParseDataHelper.parseLong("DistrictCode", record.getDistrictCode(), true));
        newRecord.setCircleCode(ParseDataHelper.parseString("CircleCode", record.getCircleCode(), true));

        newRecord.setCircle(circle);
        newRecord.setState(state);
        newRecord.setDistrict(district);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());

        newRecord.setLanguageLocationCodeKK(ParseDataHelper.parseInt("LanguageLocationCodeKK", record.getLanguageLocationCodeKK(), true));
        newRecord.setLanguageKK(ParseDataHelper.parseString("LanguageKK", record.getLanguageKK(), true));

        newRecord.setLanguageLocationCodeMK(ParseDataHelper.parseInt("LanguageLocationCodeMK", record.getLanguageLocationCodeMK(), true));
        newRecord.setLanguageMK(ParseDataHelper.parseString("LanguageMK", record.getLanguageMK(), true));

        newRecord.setLanguageLocationCodeMA(ParseDataHelper.parseInt("LanguageLocationCodeMA", record.getLanguageLocationCodeMA(), true));
        newRecord.setLanguageMA(ParseDataHelper.parseString("LanguageMA", record.getLanguageMA(), true));

        return newRecord;
    }

}

