package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of User Profile Details interface.
 */
@Service("userProfileDetailsService")
public class UserProfileDetailsImpl implements UserProfileDetailsService {

    @Override
    public UserProfile handleUserDetail(String msisdn, String circle, String operatorCode) {

        UserProfile userProfile = null;

        return userProfile;

    }

    @Override
    public boolean updateLanguageLocationCodeFromMsisdn(Integer languageLocationCode, String msisdn) {

        boolean status = true;

        return status;

    }


}
