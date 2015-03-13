package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 13/3/15.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {



    /**
     * this method determine languageLocationCode using msisdn and circleCode
     *
     * @param msisdn     Phone number of the user
     * @param circleCode circle code of the user
     * @param operatorCode   operator code of the user
     * @param callId     callId of the calling user
     * @return User detail response object
     */
    @Override
    public UserDetailApiResponse getUserDetails(String msisdn, String circleCode, String operatorCode, Long callId) {

        UserDetailApiResponse userDetailApiResponse = new UserDetailApiResponse();

        return userDetailApiResponse;
    }

}
