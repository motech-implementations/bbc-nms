package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.util.helper.DataValidationException;

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
     * @param callId unique call id assigned by IVR
     * @return User user response object
     * @throws DataValidationException
     */
    User findUserDetails(String callingNumber, String operator, String circle,
            String callId) throws DataValidationException;

    /**
     * set Language Location Code for user
     * 
     * @param languageLocationCode Language location preference provided by
     *            caller
     * @param callingNumber calling number i.e msisdn
     * @param callId unique call id assigned by IVR
     * @throws DataValidationException
     */
    void setLanguageLocationCode(String languageLocationCode,
            String callingNumber, String callId) throws DataValidationException;
}
