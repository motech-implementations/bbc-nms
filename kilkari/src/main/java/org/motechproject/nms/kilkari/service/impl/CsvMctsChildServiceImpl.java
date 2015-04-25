package org.motechproject.nms.kilkari.service.impl;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.repository.CsvMctsChildDataService;
import org.motechproject.nms.kilkari.service.CommonValidatorService;
import org.motechproject.nms.kilkari.service.CsvMctsChildService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.List;

/**
 * This class implements the logic in CsvMctsChildService.
 */
@Service("csvMctsChildService")
public class CsvMctsChildServiceImpl implements CsvMctsChildService {

    @Autowired
    private CsvMctsChildDataService csvMctsChildDataService;
    
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CommonValidatorService commonValidatorService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(CsvMctsChildServiceImpl.class);
    
    /**
     * This method process the child csv records under transaction means
     * if single records fails whole transaction is rolled back 
     * if all records process successfully data is committed
     * 
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    @Override
    public void processCsvMctsChild(String csvFileName, List<Long> uploadedIDs){

        csvMctsChildDataService.doInTransaction(new TransactionCallback<CsvMctsChild>() {

            private String csvFileName;
            private List<Long> uploadedIDs;

            private TransactionCallback<CsvMctsChild> init(String csvFileName, List<Long> uploadedIDs) {
                this.csvFileName = csvFileName;
                this.uploadedIDs = uploadedIDs;
                return this;
            }

            @Override
            public CsvMctsChild doInTransaction(TransactionStatus arg0) {
                processCsvMctsChildInTransaction(csvFileName, uploadedIDs);
                return null;
            }
        }.init(csvFileName, uploadedIDs));

    }
    
    
    
    /**
     * This method process CsvMctsChild
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    public void processCsvMctsChildInTransaction(String csvFileName, List<Long> uploadedIDs){
        logger.info("Processing Csv file[{}]", csvFileName);
        BulkUploadStatus childCsvUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        DateTime timeOfUpload = new DateTime();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(timeOfUpload);
        errorDetails.setRecordType(RecordType.CHILD_MCTS);
        
        CsvMctsChild csvMctsChild = null;
        String userName = null;
        
        for (Long id : uploadedIDs) {
            try {
                logger.debug("Processing record id[{}]", id);
                csvMctsChild = csvMctsChildDataService.findById(id);
                
                if (csvMctsChild != null) {
                    logger.debug("Record found in database for record id[{}]", id);
                    userName = csvMctsChild.getOwner();
                    Subscriber subscriber = mapChildMctsToSubscriber(csvMctsChild);
                    subscriptionService.handleMctsSubscriptionRequestForChild(subscriber, Channel.MCTS);
                    childCsvUploadStatus.incrementSuccessCount();
                } else {
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    logger.error("Record not found for uploaded id [{}]", id);
                    childCsvUploadStatus.incrementFailureCount();
                }
                logger.info("Processed record id[{}]", id);
            } catch (NmsInternalServerError nise) {
                logger.warn("NmsInternalServerError :::: [{}]", nise.getMessage());
                errorDetails.setRecordDetails(csvMctsChild.toString());
                errorDetails.setErrorCategory(nise.getErrorCode());
                errorDetails.setErrorDescription(nise.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);

                childCsvUploadStatus.incrementFailureCount();

            } catch (DataValidationException dve) {
                logger.warn("DataValidationException :::: [{}]", dve.getMessage());
                        errorDetails.setRecordDetails(csvMctsChild.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                childCsvUploadStatus.incrementFailureCount();

            } catch (Exception e) {
                errorDetails.setRecordDetails("Some Error Occurred");
                errorDetails.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetails.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                logger.error("**** Generic Exception Raised *****:", e);
                childCsvUploadStatus.incrementFailureCount();
                throw e;
                
            } finally {
                if (csvMctsChild != null) {
                    csvMctsChildDataService.delete(csvMctsChild);
                }
            }
        }
        childCsvUploadStatus.setUploadedBy(userName);
        childCsvUploadStatus.setBulkUploadFileName(csvFileName);
        childCsvUploadStatus.setTimeOfUpload(timeOfUpload);
        
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(childCsvUploadStatus);
        logger.info("Processed Csv file[{}]", csvFileName);
    }
    
    /**
     *  This method is used to validate csv uploaded record 
     *  and map Child mcts to subscriber
     * 
     *  @param csvMctsChild csv uploaded record
     */
    private Subscriber mapChildMctsToSubscriber(CsvMctsChild csvMctsChild) throws DataValidationException, NmsInternalServerError {

        Subscriber childSubscriber = new Subscriber();
        
        logger.trace("mapChildMctsToSubscriber method start");
        childSubscriber = commonValidatorService.validateAndMapMctsLocationToSubscriber(csvMctsChild, childSubscriber);

        String msisdn = ParseDataHelper.validateAndParseString(Constants.WHOM_PHONE_NUM, csvMctsChild.getWhomPhoneNo(), true);
        childSubscriber.setMsisdn(ParseDataHelper.validateAndTrimMsisdn(Constants.WHOM_PHONE_NUM, msisdn));
        
        childSubscriber.setChildMctsId(ParseDataHelper.validateAndParseString(Constants.ID_NO, csvMctsChild.getIdNo(), true));
        childSubscriber.setMotherMctsId(ParseDataHelper.validateAndParseString(Constants.MOTHER_ID, csvMctsChild.getMotherId(), false));
        childSubscriber.setName(ParseDataHelper.validateAndParseString(Constants.MOTHER_NAME, csvMctsChild.getMotherName(), false));
        
        /* handling of appropriate dob */
        DateTime dob = ParseDataHelper.validateAndParseDate(Constants.BIRTH_DATE, csvMctsChild.getBirthdate(), true);
        validateDob(dob);
        childSubscriber.setDob(dob);

        /* Set the appropriate Deactivation Reason */
        String entryType = ParseDataHelper.validateAndParseString(Constants.ENTRY_TYPE, csvMctsChild.getEntryType(), false);
        commonValidatorService.checkValidEntryType(entryType);

        if(EntryType.DEATH.toString().equalsIgnoreCase(entryType)) {
            childSubscriber.setDeactivationReason(DeactivationReason.CHILD_DEATH);
        } else {
            childSubscriber.setDeactivationReason(DeactivationReason.NONE);
        }
        
        childSubscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        childSubscriber.setModifiedBy(csvMctsChild.getModifiedBy());
        childSubscriber.setCreator(csvMctsChild.getCreator());
        childSubscriber.setOwner(csvMctsChild.getOwner());

        logger.trace("mapChildMctsToSubscriber method finished");
        return childSubscriber;
    }


    /**
     * This method is used to check dob. Dob should not be of future date and more than 48 week before
     * @param dob
     * @throws DataValidationException
     */
    private void validateDob(DateTime dob) throws DataValidationException {
        
        if (dob.isAfter(DateTime.now())) {
            throw new DataValidationException("Date in Future", ErrorCategoryConstants.INCONSISTENT_DATA,
                    "DOB Date in Future", Constants.BIRTH_DATE);
        }

        commonValidatorService.validateWeeksFromDate(dob, Constants.DURATION_OF_48_WEEK_PACK, Constants.BIRTH_DATE);
    }

}
