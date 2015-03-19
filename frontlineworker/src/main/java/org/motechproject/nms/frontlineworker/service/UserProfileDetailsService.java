package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.UserProfile;

/**
 * This interface declares the User Profile Details.
 * It declares checkAndUpdateUserDetails using msisdn, circle, and operatorId
 * and also declares updateLanguageLocationCode using LanguageLocationCode and msisdn.
 */


public interface UserProfileDetailsService {

    public UserProfile handleUserDetail(String msisdn, String circle, String operatorCode);

    public boolean updateLanguageLocationCodeFromMsisdn(Integer languageLocationCode, String msisdn );

}
