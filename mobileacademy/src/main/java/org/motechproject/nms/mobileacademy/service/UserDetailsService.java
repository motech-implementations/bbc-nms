package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.dto.User;

/**
 * UserDetailsService interface contains methods to retrieve data required by
 * user related api
 */
public interface UserDetailsService {

    /**
     * find User Details for get user API
     * 
     * @param callingNumber mobile number of the caller
     * @param operator operator of caller
     * @param circle Circle from where the call is originating.
     * @return User
     */
    User findUserDetails(String callingNumber, String operator, String circle);
}
