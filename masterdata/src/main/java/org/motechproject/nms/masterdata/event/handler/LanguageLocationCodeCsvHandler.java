package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeServiceCsv;
import org.motechproject.nms.masterdata.service.LocationService;
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
 * This class handles the csv upload for success and failure events for LanguageLocationCodeCsv.
 */
@Component
public class LanguageLocationCodeCsvHandler {

    private LanguageLocationCodeService languageLocationCodeService;

    private LanguageLocationCodeServiceCsv languageLocationCodeServiceCsv;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private CircleService circleService;

    private LocationService locationService;

    private static Logger logger = LoggerFactory.getLogger(LanguageLocationCodeCsvHandler.class);

    @Autowired
    public LanguageLocationCodeCsvHandler(LanguageLocationCodeService languageLocationCodeService,
                                          LanguageLocationCodeServiceCsv languageLocationCodeServiceCsv,
                                          BulkUploadErrLogService bulkUploadErrLogService,
                                          CircleService circleService,
                                          LocationService locationService) {
        this.languageLocationCodeService = languageLocationCodeService;
        this.languageLocationCodeServiceCsv = languageLocationCodeServiceCsv;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.circleService = circleService;
        this.locationService = locationService;
    }

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

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);

        DateTime timeStamp = NmsUtils.getCurrentTimeStamp();
        BulkUploadError errorDetail = new BulkUploadError();
        errorDetail.setCsvName(csvFileName);
        errorDetail.setTimeOfUpload(timeStamp);
        errorDetail.setRecordType(RecordType.LANGUAGE_LOCATION_CODE);

        BulkUploadStatus uploadStatus = new BulkUploadStatus();
        uploadStatus.setBulkUploadFileName(csvFileName);
        uploadStatus.setTimeOfUpload(timeStamp);

        for (Long id : createdIds) {
            try {
                record = languageLocationCodeServiceCsv.getRecord(id);
                if (record != null) {
                    uploadStatus.setUploadedBy(record.getOwner());
                    LanguageLocationCode newRecord = mapLanguageLocationCodeFrom(record);

                    LanguageLocationCode oldRecord = languageLocationCodeService.getRecordByLocationCode(
                            newRecord.getStateCode(), newRecord.getDistrictCode());
                    if (oldRecord != null) {
                        oldRecord = copyLanguageLocationCodeForUpdate(newRecord, oldRecord);
                        languageLocationCodeService.update(oldRecord);
                        logger.info("Record updated successfully for statecode : {} and districtcode : {}", newRecord.getStateCode(), newRecord.getDistrictCode());

                    } else {
                        languageLocationCodeService.create(newRecord);
                        logger.info("Record created successfully for statecode : {} and districtcode : {}", newRecord.getStateCode(), newRecord.getDistrictCode());

                    }
                    uploadStatus.incrementSuccessCount();
                } else {
                    logger.error("Record not found in the LanguageLocationCodeCsv table with id {}", id);
                    errorDetail.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetail.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetail.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetail);
                    uploadStatus.incrementFailureCount();
                }
            } catch (DataValidationException ex) {
                errorDetail.setErrorCategory(ex.getErrorCode());
                errorDetail.setRecordDetails(record.toString());
                errorDetail.setErrorDescription(ex.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetail);
                uploadStatus.incrementFailureCount();
            } catch (Exception e) {
                logger.error("LANGUAGE_LOCATION_CSV_SUCCESS processing receive Exception exception, message: {}", e);
                errorDetail.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetail.setRecordDetails("Some Error Occurred");
                errorDetail.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetail);
                uploadStatus.incrementFailureCount();
            } finally {
                if (null != record) {
                    languageLocationCodeServiceCsv.delete(record);
                }
            }
        }

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(uploadStatus);
        logger.info("Finished processing LanguageLocationCodeCsv-import success");
    }

    /**
     * This method is used to validate csv uploaded record
     * and map LanguageLocationCodeCsv to LanguageLocationCode
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
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setCircleCode(circleCode);

        newRecord.setCircle(circle);
        newRecord.setState(state);
        newRecord.setDistrict(district);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        newRecord.setLanguageLocationCode(ParseDataHelper.parseInt("LanguageLocationCode",
                record.getLanguageLocationCode(), true));
        circle.setDefaultLanguageLocationCode(newRecord.getLanguageLocationCode());
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
     *
     * @param newRecord mapped from CSV values
     * @param oldRecord to be updated in DB
     * @return oldRecord after copied values
     */
    private LanguageLocationCode copyLanguageLocationCodeForUpdate(LanguageLocationCode newRecord,
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

