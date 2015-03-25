package org.motechproject.nms.mobileacademy.service.impl;

import org.apache.log4j.Logger;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobileacademy.commons.CappingType;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.Configuration;
import org.motechproject.nms.mobileacademy.domain.FlwUsageDetail;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.service.ConfigurationService;
import org.motechproject.nms.mobileacademy.service.FlwUsageDetailService;
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
    private FlwUsageDetailService flwUsageDetailService;

    private static final Logger LOGGER = Logger
            .getLogger(UserDetailsServiceImpl.class);

    @Override
    public User findUserDetails(String callingNumber, String operator,
            String circle, String callId) throws DataValidationException {
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
        LOGGER.debug("user Details retrieved for callingNumber("
                + callingNumber + ") and callId(" + callId + ")are - llc:"
                + user.getLanguageLocationCode() + ", defaultllc:"
                + user.getDefaultLanguageLocationCode() + ", Currentusage:"
                + user.getCurrentUsageInPulses());
        return user;
    }

    /**
     * find mobile academy Usage information for Front line worker.
     * 
     * @param flwId flwId return from userProfileDetailsService of flw module
     * @return FlwUsageDetail
     */
    private FlwUsageDetail findFlwUsageInfo(Long flwId) {
        FlwUsageDetail flwUsageDetail = flwUsageDetailService
                .findByFlwId(flwId);
        if (flwUsageDetail == null) {
            // TODO in save call detail api
            flwUsageDetail = new FlwUsageDetail();
            flwUsageDetail.setFlwId(flwId);
            flwUsageDetail.setCurrentUsageInPulses(0);
            flwUsageDetail.setEndOfUsagePromptCounter(0);
            flwUsageDetail = flwUsageDetailService
                    .createFlwUsageRecord(flwUsageDetail);
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
        } else if (CappingType.NATIONAL_CAPPING.getValue().equals(
                configuration.getCappingType())) {
            maxAllowedUsageInPulses = configuration.getNationalCapValue();
        } else if (CappingType.STATE_CAPPING.getValue().equals(
                configuration.getCappingType())) {
            maxAllowedUsageInPulses = userProfile
                    .getMaxStateLevelCappingValue();
        }
        return maxAllowedUsageInPulses;

    }

    /**
     * determine and set Language Location Code for User using
     * isDefaultLanguageLocationCode field returned by FLW.
     * 
     * @param userProfile UserProfile object
     * @param configuration Configuration object
     * @param User User object
     */
    private void findLanguageLocationCodeForUser(UserProfile userProfile,
            Configuration configuration, User user) {
        boolean nationalDefaultLlc = false;
        if (userProfile.isDefaultLanguageLocationCode()) {
            if (userProfile.getDefaultLanguageLocationCode() != null) {
                user.setDefaultLanguageLocationCode(userProfile
                        .getDefaultLanguageLocationCode());
            } else {
                nationalDefaultLlc = true;
            }
        } else {
            if (userProfile.getLanguageLocationCode() != null) {
                user.setLanguageLocationCode(userProfile
                        .getLanguageLocationCode());
            } else {
                nationalDefaultLlc = true;
            }
        }
        if (nationalDefaultLlc) {
            // set national default language location code
            user.setDefaultLanguageLocationCode(configuration
                    .getDefaultLanguageLocationCode());
        }
    }

    @Override
    public void setLanguageLocationCode(String languageLocationCode,
            String callingNumber, String callId) throws DataValidationException {
        String msisdn = ParseDataHelper.validateAndTrimMsisdn("callingNumber",
                callingNumber);
        userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(
                Integer.parseInt(languageLocationCode), msisdn);
        LOGGER.debug("Llc updated  for callingNumber:" + callingNumber
                + ", callId:" + callId + ", llc:" + languageLocationCode);

    }
}
