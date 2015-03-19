package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.FlwUsageDetail;
import org.motechproject.nms.mobileacademy.domain.ServiceConfigParam;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.repository.FlwUsageDetailDataService;
import org.motechproject.nms.mobileacademy.repository.ServiceConfigParamDataService;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
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
    private ServiceConfigParamDataService serviceConfigParamDataService;

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
            String circle) {
        User user = new User();// response DTO
        UserProfile userProfile = userProfileDetailsService.handleUserDetail(
                callingNumber, circle, operator);
        user.setCircle(userProfile.getCircle());
        user.setMaxAllowedUsageInPulses(userProfile.getMaxCappingValue());// TODO
        if (userProfile.isDefaultLanguageLocationCode()) {
            user.setDefaultLanguageLocationCode(userProfile
                    .getDefaultLanguageLocationCode());
        } else {
            user.setLanguageLocationCode(userProfile.getLanguageLocationCode());
        }
        ServiceConfigParam serviceConfigParam = serviceConfigParamDataService
                .findByIndex(MobileAcademyConstants.SERVICE_CONFIG_DEFAULT_RECORD_INDEX);
        user.setMaxAllowedEndOfUsagePrompt(serviceConfigParam
                .getMaxEndOfUsuageMessage());
        FlwUsageDetail flwUsageDetail = findFlwUsageInfo(userProfile.getNmsId());
        user.setCurrentUsageInPulses(flwUsageDetail.getCurrentUsageInPulses());
        user.setEndOfUsagePromptCounter(flwUsageDetail
                .getEndOfUsagePromptCounter());
        return user;
    }

    /**
     * find Front line worker mobile academy Usage information
     * 
     * @param flwId
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
}
