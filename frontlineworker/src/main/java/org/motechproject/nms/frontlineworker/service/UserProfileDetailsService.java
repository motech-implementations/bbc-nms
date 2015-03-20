package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.UserProfile;

/**
 * This interface provides a service which will be used by other modules to get or update certain details of
 * front line worker.
 */


public interface UserProfileDetailsService {

    public UserProfile handleUserDetail(String msisdn, String circle, String operatorCode);

    public boolean updateLanguageLocationCodeFromMsisdn(Integer languageLocationCode, String msisdn);

}
