package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.AbortionType;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.EntryType;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.CommonValidatorService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
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
 * This class implements the logic in MotherMctsCsvService.
 */
@Service("motherMctsCsvService")
public class MotherMctsCsvServiceImpl implements MotherMctsCsvService {

    @Autowired
    private MotherMctsCsvDataService motherMctsCsvDataService;
    
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CommonValidatorService commonValidatorService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;
    
    private static Logger logger = LoggerFactory.getLogger(MotherMctsCsvServiceImpl.class);
    
    /**
     * This method process the mother csv records under transaction means
     * if single records fails whole transaction is rolled back 
     * if all records process successfully data is committed
     * 
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    @Override
    public void processMotherMctsCsv(String csvFileName, List<Long> uploadedIDs){
        
        motherMctsCsvDataService.doInTransaction(new TransactionCallback<MotherMctsCsv>() {
            
            private String csvFileName;
            private List<Long> uploadedIDs;
            
            private TransactionCallback<MotherMctsCsv> init(String csvFileName, List<Long> uploadedIDs) {
                this.csvFileName = csvFileName;
                this.uploadedIDs = uploadedIDs;
                return this;
            }
            
            @Override
            public MotherMctsCsv doInTransaction(TransactionStatus arg0) {
                processMotherMctsCsvInTransaction(csvFileName, uploadedIDs);
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
    public void processMotherMctsCsvInTransaction(String csvFileName, List<Long> uploadedIDs){
        logger.info("Processing Csv file[{}]", csvFileName);
        BulkUploadStatus motherCsvUploadStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        DateTime timeOfUpload = new DateTime();
        errorDetails.setCsvName(csvFileName);
        errorDetails.setTimeOfUpload(timeOfUpload);
        errorDetails.setRecordType(RecordType.MOTHER_MCTS);

        MotherMctsCsv motherMctsCsv = null;
        String userName = null;

        for (Long id : uploadedIDs) {
            try {
                logger.debug("Processing record id[{}]", id);
                motherMctsCsv = motherMctsCsvDataService.findById(id);
                if (motherMctsCsv != null) {
                    logger.debug("Record found in database for uploaded id[{}]", id);
                    userName = motherMctsCsv.getOwner();
                    Subscriber subscriber = mapMotherMctsToSubscriber(motherMctsCsv);
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
                errorDetails.setRecordDetails(motherMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);

                motherCsvUploadStatus.incrementFailureCount();

            } catch (NmsInternalServerError nise) {
                logger.warn("NmsInternalServerError :::: [{}]", nise.getMessage());
                        errorDetails.setRecordDetails(motherMctsCsv.toString());
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
            }finally {
                logger.debug("Inside finally");
                if (motherMctsCsv != null) {
                    logger.info("Deleting motherMctsCsv record id[{}]", motherMctsCsv.getId());
                    motherMctsCsvDataService.delete(motherMctsCsv);
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
     *  @param motherMctsCsv csv uploaded subscriber
     */
    private Subscriber mapMotherMctsToSubscriber(MotherMctsCsv motherMctsCsv) throws DataValidationException {

        Subscriber motherSubscriber = new Subscriber();

        logger.trace("mapMotherMctsToSubscriber method start");
        motherSubscriber = commonValidatorService.validateAndMapMctsLocationToSubscriber(motherMctsCsv, motherSubscriber);
        
        String msisdn = ParseDataHelper.validateAndParseString(Constants.WHOM_PHONE_NUM, motherMctsCsv.getWhomPhoneNo(), true);
        motherSubscriber.setMsisdn(ParseDataHelper.validateAndTrimMsisdn(Constants.WHOM_PHONE_NUM, msisdn));
        
        motherSubscriber.setMotherMctsId(ParseDataHelper.validateAndParseString(Constants.ID_NO, motherMctsCsv.getIdNo(), true));
        motherSubscriber.setAge(ParseDataHelper.validateAndParseInt(Constants.AGE, motherMctsCsv.getAge(), false));
        motherSubscriber.setAadharNumber(ParseDataHelper.validateAndParseString(Constants.AADHAR_NUM, motherMctsCsv.getAadharNo(), false));
        motherSubscriber.setName(ParseDataHelper.validateAndParseString(Constants.NAME, motherMctsCsv.getName(),false));
        
        /* handling of appropriate lmp */
        DateTime lmp = ParseDataHelper.validateAndParseDate(Constants.LMP_DATE, motherMctsCsv.getLmpDate(), true);
        DateTime currDate = DateTime.now();
        if (lmp.isAfter(DateTime.now())) {
            ParseDataHelper.raiseInvalidDataException(Constants.LMP_DATE, lmp.toString());
        } else {
            int days = Days.daysBetween(lmp.plusMonths(3), currDate).getDays();
            if ((days/Constants.DAYS_IN_WEEK) < Constants.DURATION_OF_72_WEEK_PACK) {
                motherSubscriber.setLmp(lmp);
            } else {
                ParseDataHelper.raiseInvalidDataException(Constants.LMP_DATE, lmp.toString());
            }
        }

        /* Check appropriate value of entryType and abortion*/
        Integer outcomeNos = ParseDataHelper.validateAndParseInt(Constants.OUTCOME_NOS, motherMctsCsv.getOutcomeNos(), false);
        String entryType = ParseDataHelper.validateAndParseString(Constants.ENTRY_TYPE, motherMctsCsv.getEntryType(), false);
        commonValidatorService.checkValidEntryType(entryType);
        String abortion = ParseDataHelper.validateAndParseString(Constants.ABORTION, motherMctsCsv.getAbortion(), false);
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
        motherSubscriber.setModifiedBy(motherMctsCsv.getModifiedBy());
        motherSubscriber.setCreator(motherMctsCsv.getCreator());
        motherSubscriber.setOwner(motherMctsCsv.getOwner());

        logger.trace("mapMotherMctsToSubscriber method finished");
        return motherSubscriber;
    }

}
