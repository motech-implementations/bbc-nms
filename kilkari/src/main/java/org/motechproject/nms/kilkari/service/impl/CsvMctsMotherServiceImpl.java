package org.motechproject.nms.kilkari.service.impl;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.repository.CsvMctsMotherDataService;
import org.motechproject.nms.kilkari.service.CommonValidatorService;
import org.motechproject.nms.kilkari.service.CsvMctsMotherService;
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
 * This class implements the logic in CsvMctsMotherService.
 */
@Service("csvMctsMotherService")
public class CsvMctsMotherServiceImpl implements CsvMctsMotherService {

    @Autowired
    private CsvMctsMotherDataService csvMctsMotherDataService;
    
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CommonValidatorService commonValidatorService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;
    
    private static Logger logger = LoggerFactory.getLogger(CsvMctsMotherServiceImpl.class);
    
    /**
     * This method process the mother csv records under transaction means
     * if single records fails whole transaction is rolled back 
     * if all records process successfully data is committed
     * 
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    @Override
    public void processCsvMctsMother(String csvFileName, List<Long> uploadedIDs){
        
        csvMctsMotherDataService.doInTransaction(new TransactionCallback<CsvMctsMother>() {
            
            private String csvFileName;
            private List<Long> uploadedIDs;
            
            private TransactionCallback<CsvMctsMother> init(String csvFileName, List<Long> uploadedIDs) {
                this.csvFileName = csvFileName;
                this.uploadedIDs = uploadedIDs;
                return this;
            }
            
            @Override
            public CsvMctsMother doInTransaction(TransactionStatus arg0) {
                processCsvMctsMotherInTransaction(csvFileName, uploadedIDs);
                return null;
            }
        }.init(csvFileName, uploadedIDs));
        
    }
    
    /**
     * This method process the mother record
     * 
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    public void processCsvMctsMotherInTransaction(String csvFileName, List<Long> uploadedIDs){
        logger.info("Processing Csv file[{}]", csvFileName);
        BulkUploadStatus motherCsvUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        DateTime timeOfUpload = new DateTime();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(timeOfUpload);
        errorDetails.setRecordType(RecordType.MOTHER_MCTS);

        CsvMctsMother csvMctsMother = null;
        String userName = null;

        for (Long id : uploadedIDs) {
            try {
                logger.debug("Processing record id[{}]", id);
                csvMctsMother = csvMctsMotherDataService.findById(id);
                if (csvMctsMother != null) {
                    logger.debug("Record found in database for uploaded id[{}]", id);
                    userName = csvMctsMother.getOwner();
                    Subscriber subscriber = mapMotherMctsToSubscriber(csvMctsMother);
                    subscriptionService.handleMctsSubscriptionRequestForMother(subscriber, Channel.MCTS);
                    motherCsvUploadStatus.incrementSuccessCount();
                } else {
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    logger.error("Record not found for uploaded id [{}]", id);
                    motherCsvUploadStatus.incrementFailureCount();
                }
                logger.info("Processing finished for record id[{}]", id);
            } catch (DataValidationException dve) {
                logger.warn("DataValidationException :::: [{}]", dve.getMessage());
                errorDetails.setRecordDetails(csvMctsMother.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);

                motherCsvUploadStatus.incrementFailureCount();

            } catch (NmsInternalServerError nise) {
                logger.warn("NmsInternalServerError :::: [{}]", nise.getMessage());
                        errorDetails.setRecordDetails(csvMctsMother.toString());
                errorDetails.setErrorCategory(nise.getErrorCode());
                errorDetails.setErrorDescription(nise.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);

                motherCsvUploadStatus.incrementFailureCount();

            } catch (Exception e) {
                errorDetails.setRecordDetails("Some Error Occurred");
                errorDetails.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetails.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                logger.error("**** Generic Exception Raised *****:", e);
                motherCsvUploadStatus.incrementFailureCount();
                throw e;
            }finally {
                logger.debug("Inside finally");
                if (csvMctsMother != null) {
                    logger.info("Deleting csvMctsMother record id[{}]", csvMctsMother.getId());
                    csvMctsMotherDataService.delete(csvMctsMother);
                }
            }
        }
        motherCsvUploadStatus.setUploadedBy(userName);
        motherCsvUploadStatus.setBulkUploadFileName(csvFileName);
        motherCsvUploadStatus.setTimeOfUpload(timeOfUpload);
        
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(motherCsvUploadStatus);
        logger.info("Processed Csv file[{}]", csvFileName);
    }
    
    /**
     *  This method is used to validate csv uploaded record 
     *  and map Mother mcts to subscriber
     * 
     *  @param csvMctsMother csv uploaded subscriber
     * @throws NmsInternalServerError 
     */
    private Subscriber mapMotherMctsToSubscriber(CsvMctsMother csvMctsMother) throws DataValidationException, NmsInternalServerError {

        Subscriber motherSubscriber = new Subscriber();

        logger.trace("mapMotherMctsToSubscriber method start");
        motherSubscriber = commonValidatorService.validateAndMapMctsLocationToSubscriber(csvMctsMother, motherSubscriber);
        
        String msisdn = ParseDataHelper.validateAndParseString(Constants.WHOM_PHONE_NUM, csvMctsMother.getWhomPhoneNo(), true);
        motherSubscriber.setMsisdn(ParseDataHelper.validateAndTrimMsisdn(Constants.WHOM_PHONE_NUM, msisdn));
        
        motherSubscriber.setMotherMctsId(ParseDataHelper.validateAndParseString(Constants.ID_NO, csvMctsMother.getIdNo(), true));
        motherSubscriber.setAge(ParseDataHelper.validateAndParseInt(Constants.AGE, csvMctsMother.getAge(), false));
        motherSubscriber.setAadharNumber(ParseDataHelper.validateAndParseString(Constants.AADHAR_NUM, csvMctsMother.getAadharNo(), false));
        motherSubscriber.setName(ParseDataHelper.validateAndParseString(Constants.NAME, csvMctsMother.getName(),false));
        
        /* handling of appropriate lmp */
        DateTime lmp = ParseDataHelper.validateAndParseDate(Constants.LMP_DATE, csvMctsMother.getLmpDate(), true);
        validateLmp(lmp);
        motherSubscriber.setLmp(lmp);

        /* Check appropriate value of entryType and abortion*/
        Integer outcomeNos = ParseDataHelper.validateAndParseInt(Constants.OUTCOME_NOS, csvMctsMother.getOutcomeNos(), false);
        String entryType = ParseDataHelper.validateAndParseString(Constants.ENTRY_TYPE, csvMctsMother.getEntryType(), false);
        commonValidatorService.checkValidEntryType(entryType);
        String abortion = ParseDataHelper.validateAndParseString(Constants.ABORTION, csvMctsMother.getAbortion(), false);
        commonValidatorService.checkValidAbortionType(abortion);
        
        /* Set the appropriate Deactivation Reason */
        if (Constants.STILL_BIRTH_ZERO.equals(outcomeNos)) {
            motherSubscriber.setDeactivationReason(DeactivationReason.STILL_BIRTH);
        } else if (!(abortion == null || AbortionType.NONE.toString().equalsIgnoreCase(abortion))) {
            motherSubscriber.setDeactivationReason(DeactivationReason.ABORTION);
        } else if (EntryType.DEATH.toString().equalsIgnoreCase(entryType)) {
            motherSubscriber.setDeactivationReason(DeactivationReason.MOTHER_DEATH);
        } else {
            motherSubscriber.setDeactivationReason(DeactivationReason.NONE);
        }
        motherSubscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        motherSubscriber.setModifiedBy(csvMctsMother.getModifiedBy());
        motherSubscriber.setCreator(csvMctsMother.getCreator());
        motherSubscriber.setOwner(csvMctsMother.getOwner());

        logger.trace("mapMotherMctsToSubscriber method finished");
        return motherSubscriber;
    }

    /**
     * This method is used to check lmp. lmp should not be of future date and more than 3 months + 72 week before
     * @param lmp
     * @throws DataValidationException
     */
    private void validateLmp(DateTime lmp) throws DataValidationException {

        logger.debug(String.format("checking the date %s", lmp.toString()));
        if (lmp.isAfter(DateTime.now())) {
            throw new DataValidationException("Date in Future", ErrorCategoryConstants.INCONSISTENT_DATA,
                    "LMP Date in Future", Constants.LMP_DATE);
        }
        commonValidatorService.validateWeeksFromDate(lmp.plusMonths(Constants.LMP_MSG_DELIVERY_START_MONTH), 
                Constants.DURATION_OF_72_WEEK_PACK, Constants.LMP_DATE);
        logger.debug(String.format("After Invoking the validateWeeksFromDate with lmp : %s", lmp.toString()));
    }

}
