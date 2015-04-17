package org.motechproject.nms.frontlineworker.event.handler;


import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.CsvWhiteListUsers;
import org.motechproject.nms.frontlineworker.domain.WhiteListUsers;
import org.motechproject.nms.frontlineworker.repository.WhiteListUsersRecordDataService;
import org.motechproject.nms.frontlineworker.service.CsvWhiteListUsersService;
import org.motechproject.nms.frontlineworker.service.WhiteListUsersService;
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
 * This class provides Motech Listeners for WhiteListUsers upload.
 * This class also provides methods used to validate csv data and save the data in Motech database in case of success.
 */
@Component
public class WhiteListUsersUploadHandler {

    private BulkUploadErrLogService bulkUploadErrLogService;

    private WhiteListUsersService whiteListUsersService;

    private WhiteListUsersRecordDataService whiteListUsersRecordDataService;

    private CsvWhiteListUsersService csvWhiteListUsersService;

    private static final String CSV_IMPORT_PREFIX = "csv-import.";

    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";

    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private static Logger logger = LoggerFactory.getLogger(WhiteListUsersUploadHandler.class);

    @Autowired
    public WhiteListUsersUploadHandler(BulkUploadErrLogService bulkUploadErrLogService,
                                        WhiteListUsersService whiteListUsersService,
                                        CsvWhiteListUsersService csvWhiteListUsersService,
                                        WhiteListUsersRecordDataService whiteListUsersRecordDataService
    ) {

        this.bulkUploadErrLogService = bulkUploadErrLogService;
        this.whiteListUsersService = whiteListUsersService;
        this.csvWhiteListUsersService = csvWhiteListUsersService;
        this.whiteListUsersRecordDataService = whiteListUsersRecordDataService;
    }

    /**
     * This method provides a listener to the FWhiteListUsers upload scenario. Here, WhiteListUsers CSV is received
     * from which records are fetched one by one and validations are performed on the record. If all the validations
     * pass, the record is saved in Database else the record is rejected. After completion of the processing on the
     * CSV record, it is deleted from the database.
     *
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = {ConfigurationConstants.WLU_UPLOAD_SUCCESS})
    public void wluDataHandlerSuccess(MotechEvent motechEvent) {
        logger.info("Success[wluDataHandlerSuccess] method start for CsvWhiteListUsers");
        Map<String, Object> params = motechEvent.getParameters();
        String csvFileName = (String) params.get(CSV_IMPORT_FILE_NAME);
        List<Long> createdIds = (ArrayList<Long>) params.get(CSV_IMPORT_CREATED_IDS);

        logger.debug("Processing Csv file");

        processRecords(findListOfRecords(createdIds), csvFileName);
        logger.info("Finished processing WhiteListUsers_CSV_UPLOAD_SUCCESS_EVENT Handler");


    }

    /**
     * find List Of CsvWhiteListUsers on the basis of received Id
     *
     * @param createdIds List of created id on csv upload
     * @return List<CsvWhiteListUsers> List Of CsvWhiteListUsers
     */

    private List<CsvWhiteListUsers> findListOfRecords(
            List<Long> createdIds) {
        List<CsvWhiteListUsers> listOfRecords = new ArrayList<>();
        for (Long id : createdIds) {
            listOfRecords.add(csvWhiteListUsersService.findByIdInCsv(id));
        }
        return listOfRecords;
    }


    public void processRecords(List<CsvWhiteListUsers> record,
                               String csvFileName) {
        logger.info("Record Processing Started for csv file: {}", csvFileName);

        whiteListUsersRecordDataService
                .doInTransaction(new TransactionCallback<WhiteListUsers>() {

                    List<CsvWhiteListUsers> record;

                    String csvFileName;
                    private TransactionCallback<WhiteListUsers> init(
                            List<CsvWhiteListUsers> record,
                            String csvFileName) {
                        this.record = record;
                        this.csvFileName = csvFileName;
                        return this;
                    }

                    @Override
                public WhiteListUsers doInTransaction(
                            TransactionStatus status) {
                        WhiteListUsers transactionObject = null;
                        processRecordsInTransaction(record,
                                csvFileName);
                        return transactionObject;
                    }
                }.init(record,csvFileName));
    }


     /*
     * This function processes all the CSV upload records. This function is
     * called in a transaction call so in case of any error, the changes are
     * reverted back.
     */

    private void processRecordsInTransaction(
            List<CsvWhiteListUsers> record, String csvFileName) {


        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails;

        String userName = null;

        bulkUploadStatus.setBulkUploadFileName(csvFileName);
        bulkUploadStatus.setTimeOfUpload(new DateTime());

        //this loop processes each of the entries in the White list users Csv and performs addition operation
        // of new records and also deletes each record after processing from the Csv. If some error occurs in any
        // of the records, it is reported.

        for (CsvWhiteListUsers csvsWhiteListUsers : record) {
            try {
                if (csvsWhiteListUsers != null) {
                    //Record is found in Csv
                    userName = csvsWhiteListUsers.getOwner();
                    logger.debug("Record found in Csv database");

                    WhiteListUsers whiteListUsers = new WhiteListUsers();
                    //Apply validations on the values entered in the CSV and
                    //Map values entered for the record in CSV to WhiteListUsers.
                    validateAndMapWhiteListUsers(csvsWhiteListUsers, whiteListUsers);

                    //to verify whether it is a creation case or update case
                    WhiteListUsers dbRecordByContactNo = checkExistenceOfWlu(whiteListUsers);

                    if (dbRecordByContactNo == null) {
                        //creation scenario
                        logger.debug("New White List User creation starts");
                        successfulCreate(whiteListUsers, bulkUploadStatus, "Successful creation of new White List User");
                    }
                    else {
                        logger.debug("Record already exist for the given ContactNo");
                    }
                }
            } catch (DataValidationException dve) {

                errorDetails = populateErrorDetails(csvFileName, csvsWhiteListUsers.toString(), dve.getErrorCode(), dve.getErrorDesc());
                bulkUploadStatus.incrementFailureCount();
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);

                if (csvsWhiteListUsers.getContactNo() != null) {
                    logger.warn("Record not found for uploaded Contact Number: {}", csvsWhiteListUsers.getContactNo());
                } else {
                    logger.warn("Record not found for uploaded record(Contact No is not present");
                }
            }catch (Exception e) {
                bulkUploadStatus.incrementFailureCount();
                logger.error("exception occur : {}", e.getStackTrace());
                errorDetails = populateErrorDetails(csvFileName, csvsWhiteListUsers.toString(),
                        ErrorCategoryConstants.GENERAL_EXCEPTION,
                        ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
            } finally {
                if (csvsWhiteListUsers != null) {
                    csvWhiteListUsersService.deleteFromCsv(csvsWhiteListUsers);
                }
            }
            bulkUploadStatus.setUploadedBy(userName);
            bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);
            logger.debug("Success[wluDataHandlerSuccess] method finished for CsvWhiteListUser");
        }
    }

    /**
     * This method validates a field of whiteListUsers
     *
     * @param record          the whiteListUsers record from Csv that is to be validated
     * @param whiteListUsers the FwhiteListUsers record that is to be saved in database
     * @throws DataValidationException
     */

    private void validateAndMapWhiteListUsers(CsvWhiteListUsers record, WhiteListUsers whiteListUsers) throws DataValidationException {

        String contactNo;
        logger.debug("validateAndMapWhiteListUsers process start");

        contactNo = ParseDataHelper.validateAndTrimMsisdn(" White List User Contact Number", record.getContactNo());
        whiteListUsers.setContactNo(contactNo);

        whiteListUsers.setCreator(record.getCreator());
        whiteListUsers.setModifiedBy(record.getModifiedBy());
        whiteListUsers.setOwner(record.getOwner());

        logger.debug("validateAndMapWhiteListUsers process end");
    }

    /**
     * This method is used to set error record details
     *
     * @param id               record for which error is generated
     * @param errorCategory    specifies error category
     * @param errorDescription specifies error description
     */

    private BulkUploadError populateErrorDetails(String csvFileName, String id, String errorCategory, String errorDescription) {

        BulkUploadError errorDetails = new BulkUploadError();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(new DateTime());
        errorDetails.setRecordType(RecordType.WHITE_LIST_USERS);
        errorDetails.setRecordDetails(id);
        errorDetails.setErrorCategory(errorCategory);
        errorDetails.setErrorDescription(errorDescription);
        return errorDetails;
    }

    /**
     * This method is used to check existence of WhiteListUsers
     *
     * @param whiteListUsers  whiteListUsers whose details are to be fetched from database.
     * @return null if there is no dbrecord for that ContactNo.
     */

    private WhiteListUsers checkExistenceOfWlu(WhiteListUsers whiteListUsers) throws DataValidationException {

        WhiteListUsers dbRecordByContactNo = null;

        String contactNo = whiteListUsers.getContactNo();
        logger.debug("WLU Contact Number : {}", contactNo);
        dbRecordByContactNo = whiteListUsersService.findContactNo(contactNo);
        return dbRecordByContactNo;
    }


    /**
     * This method maps fields of generated white list users object to white list users  object that
     * is to be saved in Database.
     *
     * @param whiteListUsers  the whiteListUsers object which is to be stored in database
     * @param bulkUploadStatus object to provide the status of create scenario
     * @param log              log to be generated for create scenario
     */

    private void successfulCreate(WhiteListUsers whiteListUsers, BulkUploadStatus bulkUploadStatus, String log) {

        whiteListUsersService.createWhiteListUsers(whiteListUsers);
        bulkUploadStatus.incrementSuccessCount();
        logger.debug(log);
    }

}
