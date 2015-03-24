package org.motechproject.nms.mobilekunji.service.impl;


import org.joda.time.DateTime;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.FlwDetail;
import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiRequest;
import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionCallService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public SaveCallDetailApiResponse saveCallDetails(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException {

            setFlwDetail(saveCallDetailApiRequest);
            return null;
    }

    private void setFlwDetail(SaveCallDetailApiRequest saveCallDetailApiRequest) throws DataValidationException{

        UserProfile userProfileData = userProfileDetailsService.processUserDetails(saveCallDetailApiRequest.getCallingNumber(),
                saveCallDetailApiRequest.getCircle(),saveCallDetailApiRequest.getOperator(), ServicesUsingFrontLineWorker.MOBILEKUNJI);

        FlwDetail flwDetail = serviceConsumptionFlwService.findServiceConsumptionByMsisdn(userProfileData.getMsisdn());

        if(null != flwDetail) {
            updateFlwDetail(flwDetail,saveCallDetailApiRequest);
        } else {
            populateFlwDetail(userProfileData,saveCallDetailApiRequest);
        }
    }


    private void populateFlwDetail(UserProfile userProfileData,SaveCallDetailApiRequest saveCallDetailApiRequest) {

        FlwDetail flwDetail = new FlwDetail();
        flwDetail.setNmsFlwId(userProfileData.getNmsId());
        flwDetail.setMsisdn(userProfileData.getMsisdn());

        if(saveCallDetailApiRequest.getWelcomeMessageFlag()) {
        flwDetail.setWelcomePromptFlagCounter(ConfigurationConstants.DEFAULT_MAX_WELCOME_MESSAGE);
        } else {
        flwDetail.setWelcomePromptFlagCounter(ConfigurationConstants.ZERO);
        }

        flwDetail.setEndOfUsagePrompt(saveCallDetailApiRequest.getEndOfUsagePromptCounter());
        flwDetail.setCurrentUsageInPulses(saveCallDetailApiRequest.getCurrentUsageInPulses());
        flwDetail.setLastAccessDate(DateTime.now());

        serviceConsumptionFlwService.create(flwDetail);
    }

    private void updateFlwDetail(FlwDetail updateFlwDetail,SaveCallDetailApiRequest saveCallDetailApiRequest) {

        updateFlwDetail.setEndOfUsagePrompt(saveCallDetailApiRequest.getEndOfUsagePromptCounter()+updateFlwDetail.getEndOfUsagePrompt());
        updateFlwDetail.setCurrentUsageInPulses(saveCallDetailApiRequest.getCurrentUsageInPulses() + updateFlwDetail.getCurrentUsageInPulses());
        updateFlwDetail.setLastAccessDate(DateTime.now());

        serviceConsumptionFlwService.update(updateFlwDetail);
    }
}
