package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.CsvTaluka;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.TalukaCsvService;
import org.motechproject.nms.masterdata.service.TalukaService;
import org.motechproject.nms.masterdata.service.ValidatorService;
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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class handles the csv upload for success and failure events for Taluka Csv.
 */
@Component
public class TalukaCsvUploadHandler {

    private ValidatorService validatorService;

    private DistrictService districtService;

    private TalukaCsvService talukaCsvService;

    private TalukaService talukaService;

    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(TalukaCsvUploadHandler.class);

    @Autowired
    public TalukaCsvUploadHandler(ValidatorService validatorService,DistrictService districtService, TalukaCsvService talukaCsvService, TalukaService talukaService, BulkUploadErrLogService bulkUploadErrLogService) {
        this.validatorService = validatorService;
        this.districtService = districtService;
        this.talukaCsvService = talukaCsvService;
        this.talukaService = talukaService;
        this.bulkUploadErrLogService = bulkUploadErrLogService;
    }

    /**
     * This method handle the event which is raised after csv is uploaded successfully.
     * this method also populates the records in Taluka table after checking its validity.
     *
     * @param motechEvent This is the object from which required parameters are fetched.
     */
    @MotechListener(subjects = {LocationConstants.TALUKA_CSV_SUCCESS})
    public void talukaCsvSuccess(MotechEvent motechEvent) {

        logger.info("TALUKA_CSV_SUCCESS event received");

        Map<String, Object> params = motechEvent.getParameters();

        String csvFileName = (String) params.get("csv-import.filename");
        logger.debug("Csv file name received in event : {}", csvFileName);

        List<Long> createdIds = (ArrayList<Long>) params.get("csv-import.created_ids");
        processRecords(createdIds, csvFileName);

    }

    /**
     * This method processes the Csv data Records.
     * @param CreatedId
     * @param csvFileName
     */
    private void processRecords(List<Long> CreatedId,
                               String csvFileName) {
        logger.info("Record Processing Started for csv file: {}", csvFileName);

        talukaService.getTalukaRecordsDataService()
                .doInTransaction(new TransactionCallback<Taluka>() {

                    List<Long> talukaCsvId;

                    String csvFileName;

                    private TransactionCallback<Taluka> init(
                            List<Long> createdId,
                            String csvFileName) {
                        this.talukaCsvId = createdId;
                        this.csvFileName = csvFileName;
                        return this;
                    }

                    @Override
                    public Taluka doInTransaction(
                            TransactionStatus status) {
                        Taluka transactionObject = null;
                        processTalukaRecords(csvFileName, talukaCsvId);
                        return transactionObject;
                    }
                }.init(CreatedId, csvFileName));
        logger.info("Record Processing complete for csv file: {}", csvFileName);
    }


    /**
     * This method is used to process TalukaCsv records and upload it into the database.
     * @param csvFileName
     * @param createdIds
     */
    private void processTalukaRecords(String csvFileName, List<Long> createdIds) {

        DateTime timeStamp = new DateTime();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();

        BulkUploadError errorDetails = new BulkUploadError();

        ErrorLog.setErrorDetails(errorDetails, bulkUploadStatus, csvFileName, timeStamp, RecordType.TALUKA);


        CsvTaluka csvTalukaRecord = null;

        for (Long id : createdIds) {
            try {
                logger.debug("TALUKA_CSV_SUCCESS event processing start for ID: {}", id);
                csvTalukaRecord = talukaCsvService.findById(id);

                if (csvTalukaRecord != null) {
                    logger.info("Id exist in Taluka Temporary Entity");
                    bulkUploadStatus.setUploadedBy(csvTalukaRecord.getOwner());
                    Taluka record = mapTalukaCsv(csvTalukaRecord);
                    processTalukaData(record);
                    bulkUploadStatus.incrementSuccessCount();
                } else {
                    logger.info("Id do not exist in Taluka Temporary Entity");
                    ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION, ErrorCategoryConstants.CSV_RECORD_MISSING, "Record is null");
                }
            } catch (DataValidationException talukaDataException) {
                logger.error("TALUKA_CSV_SUCCESS processing receive DataValidationException exception due to error field: {}", talukaDataException.getErroneousField());

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, talukaDataException.getErroneousField(), talukaDataException.getErrorCode(), csvTalukaRecord.toString());

            } catch (Exception talukaException) {

                ErrorLog.errorLog(errorDetails, bulkUploadStatus, bulkUploadErrLogService, ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION, ErrorCategoryConstants.GENERAL_EXCEPTION, "Exception occurred");

                logger.error("TALUKA_CSV_SUCCESS processing receive Exception exception, message: {}", talukaException);
            } finally {
                if (null != csvTalukaRecord) {
                    talukaCsvService.delete(csvTalukaRecord);
                }
            }
        }
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
    }

    /**
     * This method maps CSV data to the the Taluka object.
     * @param record
     * @return
     * @throws DataValidationException
     */
    private Taluka mapTalukaCsv(CsvTaluka record) throws DataValidationException {
        Taluka newRecord = new Taluka();

        String talukaName = ParseDataHelper.validateAndParseString("TalukaName", record.getName(), true);
        Long stateCode = ParseDataHelper.validateAndParseLong("StateCode", record.getStateCode(), true);
        Long districtCode = ParseDataHelper.validateAndParseLong("DistrictCode", record.getDistrictCode(), true);
        Long talukaCode = ParseDataHelper.validateAndParseLong("TalukaCode", record.getTalukaCode(), true);

        validateTalukaParent(stateCode, districtCode);

        newRecord.setName(talukaName);
        newRecord.setStateCode(stateCode);
        newRecord.setDistrictCode(districtCode);
        newRecord.setTalukaCode(talukaCode);
        newRecord.setCreator(record.getCreator());
        newRecord.setOwner(record.getOwner());
        newRecord.setModifiedBy(record.getModifiedBy());

        return newRecord;
    }

    /**
     * This method validates whether the Taluka has its parent or not.
     * @param stateCode
     * @param districtCode
     * @throws DataValidationException
     */
    private void validateTalukaParent(Long stateCode, Long districtCode) throws DataValidationException {
        validatorService.validateTalukaParent(stateCode, districtCode);
    }

    /**
     * This method is used to process the Taluka data according to the operation
     * @param talukaData
     * @throws DataValidationException
     */
    private void processTalukaData(Taluka talukaData) throws DataValidationException {

        Taluka existTalukaData = talukaService.findTalukaByParentCode(
                talukaData.getStateCode(),
                talukaData.getDistrictCode(),
                talukaData.getTalukaCode());

        if (existTalukaData != null) {
            updateTaluka(existTalukaData, talukaData);
        } else {
            insertTaluka(talukaData);
        }
    }

    /**
     * This method is used to insert a new Taluka record to the database.
     * @param talukaData
     */
    private void insertTaluka(Taluka talukaData) {

        District districtData = districtService.findDistrictByParentCode(talukaData.getDistrictCode(), talukaData.getStateCode());
        districtData.getTaluka().add(talukaData);
        districtService.update(districtData);
        logger.info("Taluka data is successfully inserted.");
    }


    /**
     * This method is used to update an existing Taluka record.
     * @param existTalukaData
     * @param talukaData
     */
    private void updateTaluka(Taluka existTalukaData, Taluka talukaData) {
        existTalukaData.setName(talukaData.getName());
        existTalukaData.setModifiedBy(talukaData.getModifiedBy());
        talukaService.update(existTalukaData);
        logger.info("Taluka data is successfully updated.");
    }
}
