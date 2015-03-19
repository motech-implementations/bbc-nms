package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.FlwUsuageDetail;
import org.motechproject.nms.mobileacademy.domain.ServiceConfigParam;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.repository.FlwUsuageDetailDataService;
import org.motechproject.nms.mobileacademy.repository.ServiceConfigParamDataService;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserDetailsServiceImpl class contains implementation of UserDetailsService
 *
 */
@Service("userDetailsServiceMa")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserProfileDetailsService userProfileDetailsService;

    @Autowired
    private ServiceConfigParamDataService serviceConfigParamDataService;

    @Autowired
    private FlwUsuageDetailDataService flwUsuageDetailDataService;

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
        FlwUsuageDetail flwUsuageDetail = findFlwUsageInfo(userProfile
                .getNmsId());
        user.setCurrentUsageInPulses(flwUsuageDetail.getCurrentUsageInPulses());
        user.setEndOfUsagePromptCounter(flwUsuageDetail
                .getEndOfUsagePromptCounter());
        return user;
    }

    /**
     * find Front line worker mobile academy Usage information
     * 
     * @param flwId
     * @return FlwUsuageDetail
     */
    private FlwUsuageDetail findFlwUsageInfo(Long flwId) {
        FlwUsuageDetail flwUsuageDetail = flwUsuageDetailDataService
                .findByFlwId(flwId);
        if (flwUsuageDetail == null) {
            // TODO in save call detail api
            flwUsuageDetail = new FlwUsuageDetail();
            flwUsuageDetail.setFlwId(flwId);
            flwUsuageDetail.setCurrentUsageInPulses(0);
            flwUsuageDetail.setEndOfUsagePromptCounter(0);
            flwUsuageDetail = flwUsuageDetailDataService
                    .create(flwUsuageDetail);
        }
        return flwUsuageDetail;

    }
}
