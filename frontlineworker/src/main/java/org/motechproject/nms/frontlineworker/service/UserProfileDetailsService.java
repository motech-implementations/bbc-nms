package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.masterdata.domain.Circle;

/**
 * This interface declares the User Profile Details.
 * It declares checkAndUpdateUserDetails using msisdn, circle, and operatorId
 * and also declares updateLanguageLocationCode using LanguageLocationCode and msisdn.
 */


public interface UserProfileDetailsService {

    public UserProfile checkAndUpdateUserDetails(String msisdn, Circle circle, Long operatorId);

    public boolean updateLanguageLocationCodeFromMsisdn(Long languageLocationCode, String msisdn );

}
