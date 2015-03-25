package org.motechproject.nms.kilkari.service.impl;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.*;
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

import java.util.List;

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
    
    private static Logger logger = LoggerFactory.getLogger(ChildMctsCsvServiceImpl.class);
    
    /**
     * this method process the mother record
     * @param csvFileName String type object
     * @param uploadedIDs List type object
     */
    @Override
    public void processMotherMctsCsv(String csvFileName, List<Long> uploadedIDs){
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
                logger.warn("DataValidationException ::::", dve.getMessage());
                errorDetails.setRecordDetails(motherMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);

                motherCsvUploadStatus.incrementFailureCount();

            } catch (NmsInternalServerError dve) {
                logger.warn("DataValidationException ::::", dve.getMessage());
                errorDetails.setRecordDetails(motherMctsCsv.toString());
                errorDetails.setErrorCategory(ErrorCategoryConstants.INCONSISTENT_DATA);
                errorDetails.setErrorDescription(dve.getMessage());
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
        
        String msisdn = ParseDataHelper.validateAndParseString("Whom Phone Num", motherMctsCsv.getWhomPhoneNo(), true);
        motherSubscriber.setMsisdn(ParseDataHelper.validateAndTrimMsisdn("Whom Phone Num", msisdn));
        
        motherSubscriber.setMotherMctsId(ParseDataHelper.validateAndParseString("idNo", motherMctsCsv.getIdNo(), true));
        motherSubscriber.setAge(ParseDataHelper.validateAndParseInt("Age", motherMctsCsv.getAge(), false));
        motherSubscriber.setAadharNumber(ParseDataHelper.validateAndParseString("AAdhar Num", motherMctsCsv.getAadharNo(), false));
        motherSubscriber.setName(ParseDataHelper.validateAndParseString("Name", motherMctsCsv.getName(),false));
        motherSubscriber.setLmp(ParseDataHelper.validateAndParseDate("Lmp Date", motherMctsCsv.getLmpDate(), true));

        /* Check appropriate value of entryType and abortion*/
        Integer outcomeNos = ParseDataHelper.validateAndParseInt("OutcomeNos", motherMctsCsv.getOutcomeNos(), false);
        String entryType = ParseDataHelper.validateAndParseString("Entry Type", motherMctsCsv.getEntryType(), false);
        commonValidatorService.checkValidEntryType(entryType);
        String abortion = ParseDataHelper.validateAndParseString("Abortion", motherMctsCsv.getAbortion(), false);
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
