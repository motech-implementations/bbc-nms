package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;

public interface UserDetailsService {

    /**
     * this method determine languageLocationCode using msisdn and circleCode
     * @param msisdn Phone number of the user
     * @param circleCode circle code of the user
     * @param operator operator code of the user
     * @param callId callId of the calling user
     * @return User detail response object
     */
    public UserDetailApiResponse getUserDetails(String msisdn, String circleCode,String operator, String callId);
}
