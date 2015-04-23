package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.EntryType;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.CommonValidatorService;
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

/**
 * This class implements the logic in ChildMctsCsvService.
 */
@Service("childMctsCsvService")
public class ChildMctsCsvServiceImpl implements ChildMctsCsvService {

    @Autowired
    private ChildMctsCsvDataService childMctsCsvDataService;
    
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CommonValidatorService commonValidatorService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static Logger logger = LoggerFactory.getLogger(ChildMctsCsvServiceImpl.class);
    
    /**
     * This method process the child csv records under transaction means
     * if single records fails whole transaction is rolled back 
     * if all records process successfully data is committed
     * 
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    @Override
    public void processChildMctsCsv(String csvFileName, List<Long> uploadedIDs){

        childMctsCsvDataService.doInTransaction(new TransactionCallback<ChildMctsCsv>() {

            private String csvFileName;
            private List<Long> uploadedIDs;

            private TransactionCallback<ChildMctsCsv> init(String csvFileName, List<Long> uploadedIDs) {
                this.csvFileName = csvFileName;
                this.uploadedIDs = uploadedIDs;
                return this;
            }

            @Override
            public ChildMctsCsv doInTransaction(TransactionStatus arg0) {
                processChildMctsCsvInTransaction(csvFileName, uploadedIDs);
                return null;
            }
        }.init(csvFileName, uploadedIDs));

    }
    
    
    
    /**
     * This method process ChildMctsCsv
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    public void processChildMctsCsvInTransaction(String csvFileName, List<Long> uploadedIDs){
        logger.info("Processing Csv file[{}]", csvFileName);
        BulkUploadStatus childCsvUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        DateTime timeOfUpload = new DateTime();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(timeOfUpload);
        errorDetails.setRecordType(RecordType.CHILD_MCTS);
        
        ChildMctsCsv childMctsCsv = null;
        String userName = null;
        
        for (Long id : uploadedIDs) {
            try {
                logger.debug("Processing record id[{}]", id);
                childMctsCsv = childMctsCsvDataService.findById(id);
                
                if (childMctsCsv != null) {
                    logger.debug("Record found in database for record id[{}]", id);
                    userName = childMctsCsv.getOwner();
                    Subscriber subscriber = mapChildMctsToSubscriber(childMctsCsv);
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
                errorDetails.setRecordDetails(childMctsCsv.toString());
                errorDetails.setErrorCategory(nise.getErrorCode());
                errorDetails.setErrorDescription(nise.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);

                childCsvUploadStatus.incrementFailureCount();

            } catch (DataValidationException dve) {
                logger.warn("DataValidationException :::: [{}]", dve.getMessage());
                        errorDetails.setRecordDetails(childMctsCsv.toString());
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
                
            } finally {
                if (childMctsCsv != null) {
                    childMctsCsvDataService.delete(childMctsCsv);
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
     *  @param childMctsCsv csv uploaded record
     */
    private Subscriber mapChildMctsToSubscriber(ChildMctsCsv childMctsCsv) throws DataValidationException, NmsInternalServerError {

        Subscriber childSubscriber = new Subscriber();
        
        logger.trace("mapChildMctsToSubscriber method start");
        childSubscriber = commonValidatorService.validateAndMapMctsLocationToSubscriber(childMctsCsv, childSubscriber);

        String msisdn = ParseDataHelper.validateAndParseString(Constants.WHOM_PHONE_NUM, childMctsCsv.getWhomPhoneNo(), true);
        childSubscriber.setMsisdn(ParseDataHelper.validateAndTrimMsisdn(Constants.WHOM_PHONE_NUM, msisdn));
        
        childSubscriber.setChildMctsId(ParseDataHelper.validateAndParseString(Constants.ID_NO, childMctsCsv.getIdNo(), true));
        childSubscriber.setMotherMctsId(ParseDataHelper.validateAndParseString(Constants.MOTHER_ID, childMctsCsv.getMotherId(), false));
        childSubscriber.setName(ParseDataHelper.validateAndParseString(Constants.MOTHER_NAME, childMctsCsv.getMotherName(), false));
        
        /* handling of appropriate dob */
        DateTime dob = ParseDataHelper.validateAndParseDate(Constants.BIRTH_DATE, childMctsCsv.getBirthdate(), true);
        validateDob(dob);
        childSubscriber.setDob(dob);

        /* Set the appropriate Deactivation Reason */
        String entryType = ParseDataHelper.validateAndParseString(Constants.ENTRY_TYPE, childMctsCsv.getEntryType(), false);
        commonValidatorService.checkValidEntryType(entryType);

        if(EntryType.DEATH.toString().equalsIgnoreCase(entryType)) {
            childSubscriber.setDeactivationReason(DeactivationReason.CHILD_DEATH);
        } else {
            childSubscriber.setDeactivationReason(DeactivationReason.NONE);
        }
        
        childSubscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        childSubscriber.setModifiedBy(childMctsCsv.getModifiedBy());
        childSubscriber.setCreator(childMctsCsv.getCreator());
        childSubscriber.setOwner(childMctsCsv.getOwner());

        logger.trace("mapChildMctsToSubscriber method finished");
        return childSubscriber;
    }


    /**
     * This method is used to check dob. Dob should not be of future date and more than 48 week before
     * @param dob
     * @throws DataValidationException
     */
    private void validateDob(DateTime dob) throws DataValidationException {
        
        boolean isValid = false;
        DateTime currDate = DateTime.now();

        if (dob.isAfter(currDate)) {
            ParseDataHelper.raiseInvalidDataException(Constants.BIRTH_DATE, dob.toString());
        }

        commonValidatorService.validateWeeksFromDate(dob, Constants.DURATION_OF_48_WEEK_PACK);
    }

}
