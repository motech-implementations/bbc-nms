package org.motechproject.nms.mobilekunji.service.impl;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.FlwDetail;
import org.motechproject.nms.mobilekunji.dto.LanguageLocationCodeApiRequest;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.motechproject.nms.mobilekunji.service.FlwDetailService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of UserDetailsService
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private FlwDetailService flwDetailService;

    private UserProfileDetailsService userProfileDetailsService;

    private ConfigurationService configurationService;

    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    @Autowired
    public UserDetailsServiceImpl(FlwDetailService flwDetailService, UserProfileDetailsService userProfileDetailsService, ConfigurationService configurationService) {
        this.flwDetailService = flwDetailService;
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
    public UserDetailApiResponse getUserDetails(String msisdn, String circleCode, String operatorCode, String callId) throws DataValidationException {

        UserDetailApiResponse userDetailApiResponse = null;

        UserProfile userProfileData = userProfileDetailsService.processUserDetails(msisdn, circleCode, operatorCode, ServicesUsingFrontLineWorker.MOBILEACADEMY.MOBILEKUNJI);

        populateFlwDetail(userProfileData);

        userDetailApiResponse = fillUserDetailApiResponse(userProfileData);

        return userDetailApiResponse;
    }

    @Override
    public void setLanguageLocationCode(LanguageLocationCodeApiRequest request) throws DataValidationException {

        userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(request.getLanguageLocationCode(), request.getCallingNumber());
    }

    private void populateFlwDetail(UserProfile userProfileData) {

        FlwDetail flwDetail  =  flwDetailService.findServiceConsumptionByMsisdn(userProfileData.getMsisdn());
        if ( null == flwDetail){

            flwDetail = new FlwDetail();

            flwDetail.setNmsFlwId(userProfileData.getNmsId());
            flwDetail.setMsisdn(userProfileData.getMsisdn());
            flwDetail.setLastAccessDate(DateTime.now());
            flwDetail.setEndOfUsagePrompt(ConfigurationConstants.DEFAULT_END_OF_USAGE_MESSAGE);
            flwDetail.setCurrentUsageInPulses(ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
            flwDetail.setWelcomePromptFlagCounter(ConfigurationConstants.ZERO);

            flwDetailService.create(flwDetail);
            logger.info("FlwDetail created successfully.");
        }
    }

    private UserDetailApiResponse fillUserDetailApiResponse(UserProfile userProfile) {

        UserDetailApiResponse userDetailApiResponse = new UserDetailApiResponse();

        FlwDetail flwDetail = flwDetailService.findServiceConsumptionByNmsFlwId(userProfile.getNmsId());

        userDetailApiResponse.setCircle(userProfile.getCircle());

        if(userProfile.isDefaultLanguageLocationCode()){
            userDetailApiResponse.setDefaultLanguageLocationCode(userProfile.getLanguageLocationCode());
        } else {
            userDetailApiResponse.setLanguageLocationCode(userProfile.getLanguageLocationCode());
        }

        userDetailApiResponse.setLanguageLocationCode(userProfile.getLanguageLocationCode());
        userDetailApiResponse.setDefaultLanguageLocationCode(userProfile.getDefaultLanguageLocationCode());
        userDetailApiResponse.setMaxAllowedEndOfUsagePrompt(configurationService.getConfiguration().getMaxEndofusageMessage());

        setNmsCappingValue(userDetailApiResponse, userProfile.getMaxStateLevelCappingValue());

        if (null != flwDetail) {

            userDetailApiResponse.setEndOfUsagePromptCounter(flwDetail.getEndOfUsagePrompt());

            if (flwDetail.getWelcomePromptFlagCounter() <= configurationService.getConfiguration().getMaxWelcomeMessage()) {
                userDetailApiResponse.setWelcomePromptFlag(ConfigurationConstants.DEFAULT_WELCOME_PROMPT);
            } else {
                userDetailApiResponse.setWelcomePromptFlag(ConfigurationConstants.FALSE);
            }

            if (checkNextTime(flwDetail.getLastAccessDate())) {
                userDetailApiResponse.setCurrentUsageInPulses(ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
            } else {
                userDetailApiResponse.setCurrentUsageInPulses(flwDetail.getCurrentUsageInPulses());
            }
        }
        return userDetailApiResponse;
    }

    private void setNmsCappingValue(UserDetailApiResponse userDetailApiResponse, Integer stateLevelCappingValue) {

        switch (configurationService.getConfiguration().getCappingType()) {

            case ConfigurationConstants.DEFAULT_CAPPING_TYPE:
                userDetailApiResponse.setMaxAllowedUsageInPulses(-1);
                break;

            case ConfigurationConstants.DEFAULT_NATIONAL_CAPPING_TYPE:
                userDetailApiResponse.setMaxAllowedUsageInPulses(configurationService.getConfiguration().getNationalCapValue());

            case ConfigurationConstants.DEFAULT_STATE_CAPPING_TYPE:
                userDetailApiResponse.setMaxAllowedUsageInPulses(stateLevelCappingValue);
        }
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
