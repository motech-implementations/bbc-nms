package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.exception.FlwNotInWhiteListException;
import org.motechproject.nms.frontlineworker.exception.ServiceNotDeployedException;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;

/**
 * This interface provides a service which will be used by other modules to get or update certain details of
 * front line worker.
 */


public interface UserProfileDetailsService {

    public UserProfile processUserDetails(String msisdn, String circle, String operatorCode,
                                          ServicesUsingFrontLineWorker service)
            throws DataValidationException, NmsInternalServerError,
            FlwNotInWhiteListException, ServiceNotDeployedException;

    public void updateLanguageLocationCodeFromMsisdn(String languageLocationCode, String msisdn,
                                                     ServicesUsingFrontLineWorker service)
            throws DataValidationException, FlwNotInWhiteListException, ServiceNotDeployedException;

    public void validateOperator(String operatorCode) throws DataValidationException;

    public void validateCircle(String circleCode) throws DataValidationException;

}
