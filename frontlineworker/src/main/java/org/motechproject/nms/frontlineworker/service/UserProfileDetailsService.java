package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.util.helper.DataValidationException;

/**
 * This interface provides a service which will be used by other modules to get or update certain details of
 * front line worker.
 */


public interface UserProfileDetailsService {

    public UserProfile processUserDetails(String msisdn, String circle, String operatorCode)
            throws DataValidationException;

    public void updateLanguageLocationCodeFromMsisdn(Integer languageLocationCode, String msisdn)
            throws DataValidationException;

}
