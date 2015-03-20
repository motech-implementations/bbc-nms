package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFlw;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 13/3/15.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private ServiceConsumptionFlwService serviceConsumptionFlwService;

    private UserProfileDetailsService userProfileDetailsService;

    private ConfigurationService configurationService;


    @Autowired
    public UserDetailsServiceImpl(ServiceConsumptionFlwService serviceConsumptionFlwService, UserProfileDetailsService userProfileDetailsService, ConfigurationService configurationService) {
        this.serviceConsumptionFlwService = serviceConsumptionFlwService;
        this.userProfileDetailsService = userProfileDetailsService;
        this.configurationService = configurationService;
    }




    /**
     * this method determine languageLocationCode using msisdn and circleCode
     *
     * @param msisdn       Phone number of the user
     * @param circleCode   circle code of the user
     * @param operatorCode operator code of the user
     * @param callId       callId of the calling user
     * @return User detail response object
     */
    @Override
    public UserDetailApiResponse getUserDetails(String msisdn, String circleCode, String operatorCode, Long callId) throws DataValidationException {

        UserDetailApiResponse userDetailApiResponse = null;

        UserProfile userProfileData = userProfileDetailsService.processUserDetails(msisdn, circleCode, operatorCode);

        if(userProfileData.isCreated()) {
            setFlwData(userProfileData);
        }

        userDetailApiResponse = fillUserDetailApiResponse(userProfileData);
        return userDetailApiResponse;
    }

    private void setFlwData(UserProfile userProfile) {

        ServiceConsumptionFlw serviceConsumptionFlw = new ServiceConsumptionFlw();
        serviceConsumptionFlw.setNmsFlwId(userProfile.getNmsId());
        serviceConsumptionFlw.setWelcomePromptFlag(true);
        serviceConsumptionFlw.setCurrentUsageInPulses(0);
        serviceConsumptionFlw.setEndOfUsagePrompt(0);

        serviceConsumptionFlwService.create(serviceConsumptionFlw);
    }

    private UserDetailApiResponse fillUserDetailApiResponse(UserProfile userProfile) {

        UserDetailApiResponse userDetailApiResponse = new UserDetailApiResponse();

        ServiceConsumptionFlw serviceConsumptionFlw = serviceConsumptionFlwService.findServiceConsumptionByNmsFlwId(userProfile.getNmsId());

        userDetailApiResponse.setCircle(userProfile.getCircle());
        userDetailApiResponse.setLanguageLocationCode(userProfile.getLanguageLocationCode());
        userDetailApiResponse.setDefaultLanguageLocationCode(userProfile.getDefaultLanguageLocationCode());


        userDetailApiResponse.setCurrentUsageInPulses(serviceConsumptionFlw.getCurrentUsageInPulses());

        //method for capping
        userDetailApiResponse.setMaxAllowedUsageInPulses(configurationService.getConfiguration().getNmsMkNationalCapValue());
        userDetailApiResponse.setWelcomePromptFlag(serviceConsumptionFlw.getWelcomePromptFlag());
        userDetailApiResponse.setMaxAllowedEndOfUsagePrompt(configurationService.getConfiguration().getNmsMkMaxEndofusageMessage());
        userDetailApiResponse.setEndOfUsagePromptCounter(serviceConsumptionFlw.getEndOfUsagePrompt());

        return userDetailApiResponse;
    }

}
