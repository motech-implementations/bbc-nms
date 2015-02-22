package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.masterdata.domain.Circle;

/**
 *  This class provides the implementation of User Profile Details.
 */
public class UserProfileDetailsImpl implements UserProfileDetailsService {

    @Override
    public UserProfile checkAndUpdateUserDetails(String msisdn, Circle circle, Long operatorId){

        UserProfile userProfile = null;

       return userProfile;

    }

    @Override
    public boolean updateLanguageLocationCodeFromMsisdn(Long languageLocationCode, String msisdn ){

        boolean status = true;

        return status;

    }
}
