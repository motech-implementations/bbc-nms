package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.dto.CallDetailsRequest;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;

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
     * @throws DataValidationException,NmsInternalServerError
     */
    User findUserDetails(String callingNumber, String operator, String circle,
            String callId) throws DataValidationException,
            NmsInternalServerError;

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

    /**
     * Check whether a MSISDN exists with FLW for MA service or not
     * 
     * @param callingNo : 10 digit MSISDN
     * @return true if MSISDN exists
     */
    boolean doesMsisdnExist(String callingNo);

    /**
     * save Call Details when callDetails API is invoked.
     * 
     * @param callDetailsRequest
     * @throws DataValidationException
     */
    void saveCallDetails(CallDetailsRequest callDetailsRequest)
            throws DataValidationException;
}
