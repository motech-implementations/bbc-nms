package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.dto.LanguageLocationCodeApiRequest;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;

/**
 * The purpose of this class is to provide methods to create, delete and update the service level User Details.
 */
public interface UserDetailsService {

    /**
     * this method determine languageLocationCode using msisdn and circleCode
     *
     * @param msisdn     Phone number of the user
     * @param circleCode circle code of the user
     * @param operator   operator code of the user
     * @param callId     callId of the calling user
     * @return User detail response object
     */
    public UserDetailApiResponse getUserDetails(String msisdn, String circleCode, String operator, String callId) throws DataValidationException, NmsInternalServerError;

    /**
     * this method update LanguageLocationCode using msisdn, callId and languageLocationCode in LanguageLocationCodeApiRequest
     */
    public void setLanguageLocationCode(LanguageLocationCodeApiRequest request) throws DataValidationException;

}
