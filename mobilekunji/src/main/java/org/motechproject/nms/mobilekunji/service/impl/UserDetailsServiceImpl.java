package org.motechproject.nms.mobilekunji.service.impl;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.exception.FlwNotInWhiteListException;
import org.motechproject.nms.frontlineworker.exception.ServiceNotDeployedException;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.FlwDetail;
import org.motechproject.nms.mobilekunji.dto.LanguageLocationCodeApiRequest;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.motechproject.nms.mobilekunji.service.FlwDetailService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.nms.util.helper.ParseDataHelper;
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
    public UserDetailApiResponse getUserDetails(String msisdn, String circleCode, String operatorCode, String callId) throws
            DataValidationException, NmsInternalServerError,FlwNotInWhiteListException,ServiceNotDeployedException {

        logger.info("Get UserDetails Entered successfully.");

        UserDetailApiResponse userDetailApiResponse = null;

        UserProfile userProfileData = userProfileDetailsService.processUserDetails(msisdn, circleCode, operatorCode, ServicesUsingFrontLineWorker.MOBILEKUNJI);

        populateFlwDetail(userProfileData);

        userDetailApiResponse = fillUserDetailApiResponse(userProfileData);

        logger.info("Get UserDetails executed successfully.");

        return userDetailApiResponse;
    }

    /**
     * this method update LanguageLocationCode using msisdn, callId and languageLocationCode in LanguageLocationCodeApiRequest
     */
    @Override
    public void setLanguageLocationCode(LanguageLocationCodeApiRequest request) throws DataValidationException, ServiceNotDeployedException, FlwNotInWhiteListException {

        logger.info("Update LanguageLocationCode Entered successfully.");

        userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(request.getLanguageLocationCode(),
                ParseDataHelper.validateAndTrimMsisdn(
                        "CallingNumber", request.getCallingNumber()),ServicesUsingFrontLineWorker.MOBILEKUNJI);

        logger.info("LanguageLocationCode executed successfully.");
    }

    /**
     * Saves Call details of the user
     *
     * @param flwDetail
     * @param userProfileData
     */
    private void fillDefaultFlwWithUserProfile(FlwDetail flwDetail, UserProfile userProfileData) {

        flwDetail.setNmsFlwId(userProfileData.getNmsFlwId());
        flwDetail.setMsisdn(userProfileData.getMsisdn());
        flwDetail.setLastAccessDate(DateTime.now());
        flwDetail.setWelcomePromptFlag(ConfigurationConstants.DEFAULT_WELCOME_PROMPT);
        flwDetail.setEndOfUsagePrompt(ConfigurationConstants.DEFAULT_END_OF_USAGE_PROMPT);
        flwDetail.setCurrentUsageInPulses(ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
    }

    /**
     * Populate FlwDetail
     *
     * @param userProfileData
     */
    private void populateFlwDetail(UserProfile userProfileData) {

        if (userProfileData.isCreated()) {
            FlwDetail flwDetail = flwDetailService.findFlwDetailByMsisdn(userProfileData.getMsisdn());
            if (null == flwDetail) {

                flwDetail = new FlwDetail();
                fillDefaultFlwWithUserProfile(flwDetail, userProfileData);
                flwDetailService.create(flwDetail);
                logger.info("FlwDetail created successfully.");
            } else {

                fillDefaultFlwWithUserProfile(flwDetail, userProfileData);
                flwDetailService.update(flwDetail);
                logger.info("FlwDetail updated successfully.");

            }
        }
    }

    /**
     * fill User Detail Response
     *
     * @param userProfile throws DataValidationException
     */
    private UserDetailApiResponse fillUserDetailApiResponse(UserProfile userProfile) throws DataValidationException {

        UserDetailApiResponse userDetailApiResponse = new UserDetailApiResponse();

        FlwDetail flwDetail = flwDetailService.findFlwDetailByNmsFlwId(userProfile.getNmsFlwId());

        if (null != flwDetail) {
            userDetailApiResponse.setWelcomePromptFlag(flwDetail.getWelcomePromptFlag());
            userDetailApiResponse.setCircle(userProfile.getCircle());
            userDetailApiResponse.setMaxAllowedEndOfUsagePrompt(configurationService.getConfiguration().getMaxEndofusageMessage());
            userDetailApiResponse.setEndOfUsagePromptCounter(flwDetail.getEndOfUsagePrompt());
            setLanguageLocationCode(userProfile.isDefaultLanguageLocationCode(), userDetailApiResponse, userProfile);
            setNmsCappingValue(userDetailApiResponse, userProfile.getMaxStateLevelCappingValue());
            fillCurrentUsageInPulses(userDetailApiResponse, flwDetail);
        } else {
            ParseDataHelper.raiseInvalidDataException("flwNmsId", userProfile.getNmsFlwId().toString());
        }
        return userDetailApiResponse;
    }

    /**
     * fill National Default LLC in UserDetailResponse if Llc is null else
     * fill Default Llc if defaultLlc boolean is true else fill Llc
     *
     * @param defaultLlc
     * @param userProfile
     * @param userDetailApiResponse
     */
    private void setLanguageLocationCode(Boolean defaultLlc, UserDetailApiResponse userDetailApiResponse, UserProfile userProfile) {

        if (userProfile.getLanguageLocationCode() == null) {
            setNationalDefaultLlc(userDetailApiResponse);
        } else {
            setLlcWithBoolean(defaultLlc, userDetailApiResponse, userProfile);
        }
    }

    /**
     * fill Default LLC in UserDetailResponse if boolean is true else fill Llc
     *
     * @param defaultLlc
     * @param userProfile
     * @param userDetailApiResponse
     */
    private void setLlcWithBoolean(Boolean defaultLlc, UserDetailApiResponse userDetailApiResponse, UserProfile userProfile) {

        if (defaultLlc) {
            userDetailApiResponse.setDefaultLanguageLocationCode(userProfile.getLanguageLocationCode());
        } else {
            userDetailApiResponse.setLanguageLocationCode(userProfile.getLanguageLocationCode());
        }
    }

    /**
     * fill National Default LLC
     *
     * @param userDetailApiResponse
     */
    private void setNationalDefaultLlc(UserDetailApiResponse userDetailApiResponse) {
        userDetailApiResponse.setDefaultLanguageLocationCode(configurationService.getConfiguration().
                getNationalDefaultLanguageLocationCode());
    }

    /**
     * fill Current Usage In Pulses
     *
     * @param userDetailApiResponse
     * @param flwDetail
     */
    private void fillCurrentUsageInPulses(UserDetailApiResponse userDetailApiResponse, FlwDetail flwDetail) {
        if (checkNextTime(flwDetail.getLastAccessDate())) {
            userDetailApiResponse.setCurrentUsageInPulses(ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
            resetCurrentUsage(flwDetail);
        } else {
            userDetailApiResponse.setCurrentUsageInPulses(flwDetail.getCurrentUsageInPulses());
        }
    }

    /**
     * Set Maximum Allowed Usage in Pulses on the basis of Capping Type
     *
     * @param userDetailApiResponse
     * @param stateLevelCappingValue
     */
    private void setNmsCappingValue(UserDetailApiResponse userDetailApiResponse, Integer stateLevelCappingValue) {

        switch (configurationService.getConfiguration().getCappingType()) {

            case ConfigurationConstants.DEFAULT_CAPPING_TYPE:
                userDetailApiResponse.setMaxAllowedUsageInPulses(ConfigurationConstants.DEFAULT_CAPPING_VALUE);
                break;

            case ConfigurationConstants.DEFAULT_NATIONAL_CAPPING_TYPE:
                userDetailApiResponse.setMaxAllowedUsageInPulses(configurationService.getConfiguration().getNationalCapValue());
                break;

            case ConfigurationConstants.DEFAULT_STATE_CAPPING_TYPE:
                setStateCappingValue(userDetailApiResponse, stateLevelCappingValue);
                break;
        }
    }

    /**
     * set State Capping Value
     *
     * @param stateLevelCappingValue
     * @param userDetailApiResponse
     */
    private void setStateCappingValue(UserDetailApiResponse userDetailApiResponse, Integer stateLevelCappingValue) {

        if (stateLevelCappingValue == null) {
            userDetailApiResponse.setMaxAllowedUsageInPulses(ConfigurationConstants.DEFAULT_CAPPING_VALUE);
        } else {
            userDetailApiResponse.setMaxAllowedUsageInPulses(stateLevelCappingValue);
        }
    }

    /**
     * Check For Next Month and Year and return true if lastAccessTime
     * is greater than current date else return false.
     *
     * @param lastAccessTime
     */
    public boolean checkNextTime(DateTime lastAccessTime) {
        DateTime now = DateTime.now();
        boolean flag = false;
        logger.debug("LastAccessTime: {} " + lastAccessTime);
        if (lastAccessTime != null) {
            lastAccessTime = lastAccessTime.withZone(DateTimeZone.getDefault());
            flag = lastAccessTime.getMonthOfYear() != now.getMonthOfYear() ||
                    lastAccessTime.getYear() != now.getYear();
        }
        return flag;
    }

    /**
     * set the Current Usage in Pulses to its Default Value
     *
     * @param flwDetail
     */
    private void resetCurrentUsage(FlwDetail flwDetail) {

        flwDetail.setCurrentUsageInPulses(ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
        flwDetailService.update(flwDetail);
        logger.info("FlwDetail: Current Usage is updated successfully.");
    }

}
