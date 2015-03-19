package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of User Profile Details interface.
 */
@Service("userProfileDetailsService")
public class UserProfileDetailsImpl implements UserProfileDetailsService {

    @Override
    public UserProfile processUserDetails(String msisdn, String circle, String operatorCode)
            throws DataValidationException {

        UserProfile userProfile = null;

        return userProfile;

    }

    @Override
    public void updateLanguageLocationCodeFromMsisdn(Integer languageLocationCode, String msisdn)
            throws DataValidationException{

    }

}
