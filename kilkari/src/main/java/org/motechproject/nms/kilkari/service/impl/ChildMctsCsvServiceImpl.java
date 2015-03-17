package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
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
    
    public static final String CHILD_DEATH_NINE = "9";
    
    private static Logger logger = LoggerFactory.getLogger(ChildMctsCsvServiceImpl.class);
    
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
                logger.info("Processing record id[{}]", id);
                childMctsCsv = childMctsCsvDataService.findById(id);
                
                if (childMctsCsv != null) {
                    logger.info("Record found in database for record id[{}]", id);
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
        
        logger.info("Validation and map to entity process start");
        childSubscriber = locationValidator.mapMctsLocationToSubscriber(childMctsCsv, childSubscriber);

        String msisdn = ParseDataHelper.parseString("Whom Phone Num", childMctsCsv.getWhomPhoneNo(), true);
        int msisdnCsvLength = msisdn.length();
        if(msisdnCsvLength > 10){
            msisdn = msisdn.substring(msisdnCsvLength-10, msisdnCsvLength);
        }
        childSubscriber.setMsisdn(msisdn);
        childSubscriber.setChildMctsId(ParseDataHelper.parseString("idNo", childMctsCsv.getIdNo(), true));
        childSubscriber.setMotherMctsId(ParseDataHelper.parseString("Mother Id", childMctsCsv.getMotherId(), false));
        childSubscriber.setChildDeath(CHILD_DEATH_NINE.equalsIgnoreCase(ParseDataHelper.parseString("Entry Type", childMctsCsv.getEntryType(), false)));
        childSubscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        childSubscriber.setName(ParseDataHelper.parseString("Mother Name", childMctsCsv.getMotherName(), false));
        childSubscriber.setDob(ParseDataHelper.parseDate("Birth Date", childMctsCsv.getBirthdate(), true));

        childSubscriber.setModifiedBy(childMctsCsv.getModifiedBy());
        childSubscriber.setCreator(childMctsCsv.getCreator());
        childSubscriber.setOwner(childMctsCsv.getOwner());

        logger.info("Validation and map to entity process finished");
        return childSubscriber;
    }


}
