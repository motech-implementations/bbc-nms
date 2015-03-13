package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.LocationValidatorService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
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
import org.springframework.stereotype.Service;

@Service("childMctsCsvService")
public class ChildMctsCsvServiceImpl implements ChildMctsCsvService {

    @Autowired
    private ChildMctsCsvDataService childMctsCsvDataService;
    
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private LocationValidatorService locationValidator;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Autowired
    private SubscriberService subscriberService;
    
    @Autowired
    private ConfigurationService configurationService;
    
    private static Logger logger = LoggerFactory.getLogger(ChildMctsCsvServiceImpl.class);
    
    public static final String CHILD_DEATH_NINE = "9";
    
    @Override
    public void processChildMctsCsv(String csvFileName, List<Long> uploadedIDs){
        logger.info("Processing Csv file[{}]", csvFileName);
        BulkUploadStatus uploadedStatus = new BulkUploadStatus();
        BulkUploadError errorDetails = new BulkUploadError();
        DateTime timeOfUpload = NmsUtils.getCurrentTimeStamp();
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
                    uploadedStatus.incrementSuccessCount();
                } else {
                    errorDetails.setErrorDescription(ErrorDescriptionConstants.CSV_RECORD_MISSING_DESCRIPTION);
                    errorDetails.setErrorCategory(ErrorCategoryConstants.CSV_RECORD_MISSING);
                    errorDetails.setRecordDetails("Record is null");
                    bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                    logger.error("Record not found for uploaded id [{}]", id);
                    uploadedStatus.incrementFailureCount();
                }
                logger.info("Processed record id[{}]", id);
            } catch (DataValidationException dve) {
                logger.warn("DataValidationException ::::", dve.getMessage());
                errorDetails.setRecordDetails(childMctsCsv.toString());
                errorDetails.setErrorCategory(dve.getErrorCode());
                errorDetails.setErrorDescription(dve.getErrorDesc());
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                uploadedStatus.incrementFailureCount();

            } catch (Exception e) {
                errorDetails.setRecordDetails("Some Error Occurred");
                errorDetails.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
                errorDetails.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
                bulkUploadErrLogService.writeBulkUploadErrLog(errorDetails);
                uploadedStatus.incrementFailureCount();
                
            } finally {
                if (childMctsCsv != null) {
                    childMctsCsvDataService.delete(childMctsCsv);
                }
            }
        }
        uploadedStatus.setUploadedBy(userName);
        uploadedStatus.setBulkUploadFileName(csvFileName);
        uploadedStatus.setTimeOfUpload(timeOfUpload);
        
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(uploadedStatus);
        logger.info("Processed Csv file[{}]", csvFileName);
    }
    
    /**
     *  This method is used to validate csv uploaded record 
     *  and map Child mcts to subscriber
     * 
     *  @param childMctsCsv csv uploaded record
     */
    private Subscriber mapChildMctsToSubscriber(ChildMctsCsv childMctsCsv) throws DataValidationException {

        Subscriber childSubscriber = new Subscriber();
        
        logger.trace("mapChildMctsToSubscriber method start");
        childSubscriber = locationValidator.validateAndMapMctsLocationToSubscriber(childMctsCsv, childSubscriber);

        String msisdn = ParseDataHelper.validateAndParseString("Whom Phone Num", childMctsCsv.getWhomPhoneNo(), true);
        childSubscriber.setMsisdn(ParseDataHelper.validateAndTrimMsisdn("Whom Phone Num", msisdn));
        
        childSubscriber.setChildMctsId(ParseDataHelper.validateAndParseString("idNo", childMctsCsv.getIdNo(), true));
        childSubscriber.setMotherMctsId(ParseDataHelper.validateAndParseString("Mother Id", childMctsCsv.getMotherId(), false));
        childSubscriber.setName(ParseDataHelper.validateAndParseString("Mother Name", childMctsCsv.getMotherName(), false));
        childSubscriber.setDob(ParseDataHelper.validateAndParseDate("Birth Date", childMctsCsv.getBirthdate(), true));

        /* Set the appropriate Deactivation Reason */
        if(CHILD_DEATH_NINE.equalsIgnoreCase(ParseDataHelper.validateAndParseString("Entry Type", childMctsCsv.getEntryType(), false))){
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


}
