package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobileacademy.commons.CappingType;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.Configuration;
import org.motechproject.nms.mobileacademy.domain.FlwUsageDetail;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.repository.FlwUsageDetailDataService;
import org.motechproject.nms.mobileacademy.service.ConfigurationService;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserDetailsServiceImpl class contains implementation of UserDetailsService
 *
 */
@Service("UserDetailsServiceMa")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserProfileDetailsService userProfileDetailsService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FlwUsageDetailDataService flwUsageDetailDataService;

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.UserDetailsService#
     * findUserDetails(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public User findUserDetails(String callingNumber, String operator,
            String circle) throws DataValidationException {
        User user = new User();// response DTO
        String msisdn = ParseDataHelper.validateAndTrimMsisdn("callingNumber",
                callingNumber);
        UserProfile userProfile = userProfileDetailsService.processUserDetails(
                msisdn, circle, operator,
                ServicesUsingFrontLineWorker.MOBILEACADEMY);
        Configuration configuration = configurationService.getConfiguration();
        findLanguageLocationCodeForUser(userProfile, configuration, user);
        user.setCircle(userProfile.getCircle());
        user.setMaxAllowedEndOfUsagePrompt(configuration
                .getMaxAllowedEndOfUsagePrompt());
        Integer maxAllowedUsageInPulses = findMaxAllowedUsageInPulses(
                userProfile, configuration);
        user.setMaxAllowedUsageInPulses(maxAllowedUsageInPulses);
        FlwUsageDetail flwUsageDetail = findFlwUsageInfo(userProfile.getNmsId());
        user.setCurrentUsageInPulses(flwUsageDetail.getCurrentUsageInPulses());
        user.setEndOfUsagePromptCounter(flwUsageDetail
                .getEndOfUsagePromptCounter());
        return user;
    }

    /**
     * find mobile academy Usage information for Front line worker.
     * 
     * @param flwId flwId return from userProfileDetailsService of flw module
     * @return FlwUsageDetail
     */
    private FlwUsageDetail findFlwUsageInfo(Long flwId) {
        FlwUsageDetail flwUsageDetail = flwUsageDetailDataService
                .findByFlwId(flwId);
        if (flwUsageDetail == null) {
            // TODO in save call detail api
            flwUsageDetail = new FlwUsageDetail();
            flwUsageDetail.setFlwId(flwId);
            flwUsageDetail.setCurrentUsageInPulses(0);
            flwUsageDetail.setEndOfUsagePromptCounter(0);
            flwUsageDetail = flwUsageDetailDataService.create(flwUsageDetail);
        }
        return flwUsageDetail;

    }

    /**
     * Determine Max Allowed Usage In Pulses as per capping type in
     * configuration.
     * 
     * @param userProfile UserProfile object
     * @param configuration Configuration object
     * @return Integer MaxAllowedUsageInPulses
     */
    private Integer findMaxAllowedUsageInPulses(UserProfile userProfile,
            Configuration configuration) {
        Integer maxAllowedUsageInPulses = null;
        if (CappingType.NO_CAPPING.getValue().equals(
                configuration.getCappingType())) {
            maxAllowedUsageInPulses = MobileAcademyConstants.MAX_ALLOWED_USAGE_PULSE_FOR_UNCAPPED;
        } else if (CappingType.STATE_CAPPING.equals(configuration
                .getCappingType())) {
            maxAllowedUsageInPulses = userProfile
                    .getMaxStateLevelCappingValue();
        } else if (CappingType.NATIONAL_CAPPING.equals(configuration
                .getCappingType())) {
            maxAllowedUsageInPulses = configuration.getNationalCapValue();
        }
        return maxAllowedUsageInPulses;

    }

    /**
     * determine and set Language Location Code for User as per circle and LLC
     * fields returned by FLW.
     * 
     * @param userProfile UserProfile object
     * @param configuration Configuration object
     * @param User User object
     */
    private void findLanguageLocationCodeForUser(UserProfile userProfile,
            Configuration configuration, User user) {
        if (MobileAcademyConstants.UNKNOWN_CIRCLE_CODE.equals(userProfile
                .getCircle())) {
            // set national default using configuration for unknown circle
            user.setDefaultLanguageLocationCode(configuration
                    .getDefaultLanguageLocationCode());
        } else {
            if (userProfile.isDefaultLanguageLocationCode()) {
                if (userProfile.getDefaultLanguageLocationCode() != null) {
                    user.setDefaultLanguageLocationCode(userProfile
                            .getDefaultLanguageLocationCode());
                } else {
                    user.setDefaultLanguageLocationCode(configuration
                            .getDefaultLanguageLocationCode());
                }
            } else {
                user.setLanguageLocationCode(userProfile
                        .getLanguageLocationCode());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.UserDetailsService#
     * setLanguageLocationCode(java.lang.String, java.lang.String)
     */
    @Override
    public void setLanguageLocationCode(String languageLocationCode,
            String callingNumber) throws DataValidationException {
        String msisdn = ParseDataHelper.validateAndTrimMsisdn("callingNumber",
                callingNumber);
        userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(
                Integer.parseInt(languageLocationCode), msisdn);

    }
}
