package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.enums.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.exception.FlwNotInWhiteListException;
import org.motechproject.nms.frontlineworker.exception.ServiceNotDeployedException;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;

/**
 * This interface provides a service which will be used by other modules to get or update certain details of
 * front line worker.
 */


public interface UserProfileDetailsService {

    /**
     * This procedure implements the API processUserDetails which is used to get the User Details of FrontLine worker
     * by other modules
     *
     * @param msisdn       contact number of the front line worker whose details are to be fetched
     * @param circle       the circle code deduced from the call
     * @param operatorCode the operator code deduced by the call
     * @param service      the module which is invoking the API
     * @throws DataValidationException , NmsInternalServerError
     */
    public UserProfile processUserDetails(String msisdn, String circle, String operatorCode,
                                          ServicesUsingFrontLineWorker service)
            throws DataValidationException, NmsInternalServerError,
            FlwNotInWhiteListException, ServiceNotDeployedException;

    /**
     * This procedure implements the API updateLanguageLocationCodeFromMsisdn which is used to set the Language Location
     * code as given in the API call
     *
     * @param languageLocationCode the languageLocationCode that is to be set for the msisdn
     * @param msisdn               contact number of the front line worker whose details are to be fetched
     * @throws DataValidationException
     */
    public void updateLanguageLocationCodeFromMsisdn(String languageLocationCode, String msisdn,
                                                     ServicesUsingFrontLineWorker service)
            throws DataValidationException, FlwNotInWhiteListException, ServiceNotDeployedException;


    /**
     * This procedure implements the API validateOperator which is used to validate the Operator of the call
     *
     * @param operatorCode the operator code deduced by the call
     * @throws DataValidationException
     */
    public void validateOperator(String operatorCode) throws DataValidationException;


    /**
     * This procedure validates the circle code sent in the API call
     *
     * @param circleCode the circle code deduced by the call
     * @throws DataValidationException
     */
    public void validateCircle(String circleCode) throws DataValidationException;

}
