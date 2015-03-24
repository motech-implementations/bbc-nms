package org.motechproject.nms.mobilekunji.service.impl;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.CallDetail;
import org.motechproject.nms.mobilekunji.domain.CardContent;
import org.motechproject.nms.mobilekunji.domain.FlwDetail;
import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiRequest;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionCallService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * This class provides the implementation of SaveCallDetailsService
 */

@Service("saveCallDetailsService")
public class SaveCallDetailsServiceImpl implements SaveCallDetailsService {

    private ServiceConsumptionCallService serviceConsumptionCallService;

    private ServiceConsumptionFlwService serviceConsumptionFlwService;

    private UserProfileDetailsService userProfileDetailsService;

    private Logger logger = LoggerFactory.getLogger(SaveCallDetailsServiceImpl.class);

    @Autowired
    public SaveCallDetailsServiceImpl(ServiceConsumptionCallService serviceConsumptionCallService, ServiceConsumptionFlwService serviceConsumptionFlwService) {
        this.serviceConsumptionCallService = serviceConsumptionCallService;
        this.serviceConsumptionFlwService = serviceConsumptionFlwService;
    }

    @Override
    public void saveCallDetails(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException {

        UserProfile userProfileData = userProfileDetailsService.processUserDetails(saveCallDetailApiRequest.getCallingNumber(),
                saveCallDetailApiRequest.getCircle(), saveCallDetailApiRequest.getOperator(), ServicesUsingFrontLineWorker.MOBILEKUNJI);

        setCallDetail(userProfileData, saveCallDetailApiRequest);

        setFlwDetail(userProfileData, saveCallDetailApiRequest);
    }

    private void setCallDetail(UserProfile userProfileData, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        CallDetail callDetail = new CallDetail();

        callDetail.setCallId(saveCallDetailApiRequest.getCallId());
        callDetail.setCallStartTime(saveCallDetailApiRequest.getStartTime());
        callDetail.setCallEndTime(saveCallDetailApiRequest.getEndTime());
        callDetail.setCircle(saveCallDetailApiRequest.getCircle());
        callDetail.setNmsFlwId(userProfileData.getNmsId());

        setCardContent(callDetail, saveCallDetailApiRequest);
        serviceConsumptionCallService.create(callDetail);

        logger.info("CallDetail created successfully.");
    }

    private void setCardContent(CallDetail callDetail, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        for (Iterator<CardContent> itr = saveCallDetailApiRequest.getCardContentList().iterator(); itr.hasNext(); ) {
            CardContent element = itr.next();
            callDetail.getCardContent().add(element);
        }
    }

    private void setFlwDetail(UserProfile userProfileData, SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException {

        FlwDetail flwDetail = serviceConsumptionFlwService.findServiceConsumptionByMsisdn(userProfileData.getMsisdn());

        if (null != flwDetail) {
            updateFlwDetail(flwDetail, saveCallDetailApiRequest);
        } else {
            populateFlwDetail(userProfileData, saveCallDetailApiRequest);
        }
    }


    private void populateFlwDetail(UserProfile userProfileData, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        FlwDetail flwDetail = new FlwDetail();

        flwDetail.setNmsFlwId(userProfileData.getNmsId());
        flwDetail.setMsisdn(userProfileData.getMsisdn());
        flwDetail.setEndOfUsagePrompt(saveCallDetailApiRequest.getEndOfUsagePromptCounter());
        flwDetail.setCurrentUsageInPulses(saveCallDetailApiRequest.getCurrentUsageInPulses());
        flwDetail.setLastAccessDate(new DateTime(saveCallDetailApiRequest.getStartTime()));

        if (saveCallDetailApiRequest.getWelcomeMessageFlag()) {
            flwDetail.setWelcomePromptFlagCounter(ConfigurationConstants.ONE);
        } else {
            flwDetail.setWelcomePromptFlagCounter(ConfigurationConstants.ZERO);
        }

        serviceConsumptionFlwService.create(flwDetail);

        logger.info("FlwDetail created successfully.");
    }

    private void updateFlwDetail(FlwDetail updateFlwDetail, SaveCallDetailApiRequest saveCallDetailApiRequest) {

        if (checkNextTime(updateFlwDetail.getLastAccessDate())) {

            updateFlwDetail.setEndOfUsagePrompt(ConfigurationConstants.DEFAULT_END_OF_USAGE_MESSAGE);
            updateFlwDetail.setCurrentUsageInPulses(ConfigurationConstants.CURRENT_USAGE_IN_PULSES);
        } else {
            updateFlwDetail.setEndOfUsagePrompt(saveCallDetailApiRequest.getEndOfUsagePromptCounter() + updateFlwDetail.getEndOfUsagePrompt());
            updateFlwDetail.setCurrentUsageInPulses(saveCallDetailApiRequest.getCurrentUsageInPulses() + updateFlwDetail.getCurrentUsageInPulses());
        }

        if (saveCallDetailApiRequest.getWelcomeMessageFlag()) {
            updateFlwDetail.setWelcomePromptFlagCounter(updateFlwDetail.getWelcomePromptFlagCounter() + ConfigurationConstants.ONE);
        }

        serviceConsumptionFlwService.update(updateFlwDetail);
        logger.info("FlwDetail updated successfully.");
    }

    public boolean checkNextTime(DateTime lastAccessTime) {
        DateTime now = DateTime.now();

        if (lastAccessTime != null) {
            lastAccessTime = lastAccessTime.withZone(DateTimeZone.getDefault());
            return lastAccessTime.getMonthOfYear() != now.getMonthOfYear() ||
                    lastAccessTime.getYear() != now.getYear();
        }
        return false;
    }

}
