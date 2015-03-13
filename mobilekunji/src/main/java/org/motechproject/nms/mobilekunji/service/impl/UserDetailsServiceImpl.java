package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFlw;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 13/3/15.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private ServiceConsumptionFlwService serviceConsumptionFlwService;

    private UserProfileDetailsService userProfileDetailsService;

    @Autowired
    public UserDetailsServiceImpl(ServiceConsumptionFlwService serviceConsumptionFlwService, UserProfileDetailsService userProfileDetailsService) {
        this.serviceConsumptionFlwService = serviceConsumptionFlwService;
        this.userProfileDetailsService = userProfileDetailsService;
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
    public UserDetailApiResponse getUserDetails(String msisdn, String circleCode, String operatorCode, Long callId) {

        UserDetailApiResponse userDetailApiResponse = new UserDetailApiResponse();

        UserProfile userProfileData = userProfileDetailsService.handleUserDetail(msisdn,circleCode,operatorCode);

        if(userProfileData.isCreated()) {
            setFlwData(userProfileData);
        }

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

    private UserDetailApiResponse getUserDetailApiResponse() {
            return null;
    }

}
