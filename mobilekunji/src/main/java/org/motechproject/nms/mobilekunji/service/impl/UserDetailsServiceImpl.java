package org.motechproject.nms.mobilekunji.service.impl;

import org.apache.commons.httpclient.HttpStatus;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFlw;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of UserDetailsService
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

        userDetailApiResponse = fillUserDetailApiResponse(userProfileData);
        return userDetailApiResponse;
    }

    @Override
    public int updateLanguageLocationCode(String msisdn, Integer languageLocationCode) throws DataValidationException {

         userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(languageLocationCode,msisdn);

         return HttpStatus.SC_OK;
    }


    private UserDetailApiResponse fillUserDetailApiResponse(UserProfile userProfile) {

        UserDetailApiResponse userDetailApiResponse = new UserDetailApiResponse();

        ServiceConsumptionFlw serviceConsumptionFlw = serviceConsumptionFlwService.findServiceConsumptionByNmsFlwId(userProfile.getNmsId());

        userDetailApiResponse.setCircle(userProfile.getCircle());
        userDetailApiResponse.setLanguageLocationCode(userProfile.getLanguageLocationCode());
        userDetailApiResponse.setDefaultLanguageLocationCode(userProfile.getDefaultLanguageLocationCode());

        //method for capping
        setNmsCappingValue(userDetailApiResponse, userProfile);
        userDetailApiResponse.setMaxAllowedEndOfUsagePrompt(configurationService.getConfiguration().getMaxEndofusageMessage());

        if(null != serviceConsumptionFlw) {
            userDetailApiResponse.setCurrentUsageInPulses(serviceConsumptionFlw.getCurrentUsageInPulses());
            userDetailApiResponse.setEndOfUsagePromptCounter(serviceConsumptionFlw.getEndOfUsagePrompt());
            userDetailApiResponse.setWelcomePromptFlag(serviceConsumptionFlw.getWelcomePromptFlag());
        } else {
            userDetailApiResponse.setCurrentUsageInPulses(ConfigurationConstants.CURRENT_USAGE_IN_PULSES);
            userDetailApiResponse.setEndOfUsagePromptCounter(ConfigurationConstants.DEFAULT_END_OF_USAGE_MESSAGE);
            userDetailApiResponse.setWelcomePromptFlag(ConfigurationConstants.DEFAULT_WELCOME_PROMPT);
        }
        return userDetailApiResponse;
    }

    private void setNmsCappingValue(UserDetailApiResponse userDetailApiResponse,UserProfile userProfile) {

        switch(configurationService.getConfiguration().getCappingType()){

            case ConfigurationConstants.DEFAULT_CAPPING_TYPE:
                userDetailApiResponse.setMaxAllowedUsageInPulses(-1);
                break;

            case ConfigurationConstants.DEFAULT_NATIONAL_CAPPING_TYPE:
                userDetailApiResponse.setMaxAllowedUsageInPulses(configurationService.getConfiguration().getNationalCapValue());

            case ConfigurationConstants.DEFAULT_STATE_CAPPING_TYPE:
                userDetailApiResponse.setMaxAllowedUsageInPulses(userProfile.getMaxStateLevelCappingValue());
        }
    }

}
