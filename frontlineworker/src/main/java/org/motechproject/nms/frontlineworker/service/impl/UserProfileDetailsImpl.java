package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of User Profile Details interface.
 */
@Service("userProfileDetailsService")
public class UserProfileDetailsImpl implements UserProfileDetailsService {

    @Autowired
    FrontLineWorkerService frontLineWorkerService;

    @Autowired
    OperatorService operatorService;

    @Autowired
    CircleService circleService;

    @Autowired
    LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    StateService stateService;


    /**
     * This procedure implements the API processUserDetails which is used to get the User Details of FrontLine worker
     * by other modules
     *
     * @param msisdn       contact number of the front line worker whose details are to be fetched
     * @param circleCode   the circle code deduced from the call
     * @param operatorCode the operator code deduced by the call
     * @param service      the module which is invoking the API
     * @throws DataValidationException , java.lang.Exception
     */
    @Override
    public UserProfile processUserDetails(String msisdn, String circleCode, String operatorCode,
                                          ServicesUsingFrontLineWorker service)
            throws DataValidationException, Exception {

        UserProfile userProfile = new UserProfile();
        FrontLineWorker frontLineWorker = new FrontLineWorker();
        Operator operator = new Operator();

        operator = validateParams(msisdn, operatorCode, service);
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(msisdn);

        if (frontLineWorker == null) {

            createUserProfile(msisdn, operator, userProfile, circleCode, service);

        } else {
            Status status = frontLineWorker.getStatus();

            switch (status) {
                case INACTIVE:
                    getUserDetailsForInactiveUser(msisdn, frontLineWorker, userProfile, service, circleCode);
                    break;

                case ANONYMOUS:
                    getUserDetailsForAnonymousUser(msisdn, frontLineWorker, userProfile, service, circleCode);
                    break;

                case ACTIVE:
                    getUserDetailsForActiveUser(msisdn, frontLineWorker, userProfile, service, circleCode);
                    break;

                case INVALID:
                    getUserDetailsByCircle(msisdn, frontLineWorker, userProfile, circleCode, service);
                    frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
                    break;
            }
        }
        return userProfile;
    }

    /**
     * This procedure implements the API updateLanguageLocationCodeFromMsisdn which is used to set the Language Location
     * code as given in the API call
     *
     * @param languageLocationCode the languageLocationCode that is to be set for the msisdn
     * @param msisdn               contact number of the front line worker whose details are to be fetched
     * @throws DataValidationException
     */
    @Override
    public void updateLanguageLocationCodeFromMsisdn(Integer languageLocationCode, String msisdn)
            throws DataValidationException {
        String validatedMsisdn = null;
        LanguageLocationCode languageLocationCodeByParam = null;
        FrontLineWorker frontLineWorker = null;
        validatedMsisdn = ParseDataHelper.validateAndTrimMsisdn("msisdn", msisdn);

        languageLocationCodeByParam = languageLocationCodeService.findLLCByCode(languageLocationCode);
        if (languageLocationCodeByParam == null) {
            ParseDataHelper.raiseInvalidDataException("languageLocationCode ", languageLocationCode.toString());
        } else {
            frontLineWorker = frontLineWorkerService.getFlwBycontactNo(msisdn);
            if (frontLineWorker == null) {
                ParseDataHelper.raiseInvalidDataException("validatedMsisdn ", msisdn);
            } else {
                frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
            }
        }
    }


    /**
     * This procedure creates a new UserProfile when a call is made by a number which is not present in the Database
     *
     * @param msisdn     contact number of the front line worker whose details are to be fetched
     * @param operator   the operator by which the call is generated
     * @param circleCode the circle code deduced from the call
     * @param service    the module which is invoking the API
     * @throws DataValidationException , java.lang.Exception
     */
    private void createUserProfile(String msisdn, Operator operator, UserProfile userProfile, String circleCode,
                                   ServicesUsingFrontLineWorker service) throws DataValidationException, Exception {

        FrontLineWorker frontLineWorker = new FrontLineWorker();
        frontLineWorker.setContactNo(msisdn);
        frontLineWorker.setOperatorId(operator.getId());

        getUserDetailsByCircle(msisdn, frontLineWorker, userProfile, circleCode, service);
        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
    }


    /**
     * This procedure validates the parameters that are sent in the ProcessUserDetails API
     *
     * @param msisdn       contact number of the front line worker whose details are to be fetched
     * @param operatorCode the operator code deduced by the call
     * @param service      the module which is invoking the API
     * @throws DataValidationException
     */
    private Operator validateParams(String msisdn, String operatorCode,
                                    ServicesUsingFrontLineWorker service) throws DataValidationException {

        msisdn = ParseDataHelper.validateAndTrimMsisdn("msisdn", msisdn);

        Operator operator = operatorService.getRecordByCode(operatorCode);

        if (operator == null) {
            ParseDataHelper.raiseInvalidDataException("operatorCode", operatorCode);
        }

        if (service != ServicesUsingFrontLineWorker.MOBILEACADEMY && service != ServicesUsingFrontLineWorker.MOBILEKUNJI) {
            ParseDataHelper.raiseInvalidDataException("service", service.toString());
        }

        return operator;
    }

    /**
     * This procedure generates the UserDetails of a record with the circle code given in the API call
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param userProfile     the User details that are to be returned by the API call
     * @param service         the module which is invoking the API
     * @throws DataValidationException,java.lang.Exception
     * @circleCode the circle code deduced from the call
     */
    private void getUserDetailsByCircle(String msisdn, FrontLineWorker frontLineWorker, UserProfile userProfile,
                                        String circleCode, ServicesUsingFrontLineWorker service) throws
            DataValidationException, Exception {
        Integer languageLocationCode = null;
        Integer defaultLanguageLocationCode = null;
        LanguageLocationCode langLocCode = null;
        Long stateCode = null;
        State state = null;

        languageLocationCode = languageLocationCodeService.getLanguageLocationCodeByCircleCode(circleCode);
        if (languageLocationCode != null) {
            userProfile.setDefaultLanguageLocationCode(null);
            userProfile.setIsDefaultLanguageLocationCode(false);
            userProfile.setLanguageLocationCode(languageLocationCode);

            langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, languageLocationCode);
            if (langLocCode != null) {
                stateCode = langLocCode.getStateCode();
                state = stateService.findRecordByStateCode(stateCode);
                if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
                    userProfile.setMaxStateLevelCappingValue(state.getMaCapping());
                } else {
                    userProfile.setMaxStateLevelCappingValue(state.getMkCapping());
                }
                frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
                frontLineWorker.setDefaultLanguageLocationCodeId(null);
            } else {
                throw new Exception("Language Location could not be found using state and district");
            }


        } else {
            defaultLanguageLocationCode = languageLocationCodeService.getDefaultLanguageLocationCodeByCircleCode(circleCode);
            if (defaultLanguageLocationCode != null) {
                userProfile.setLanguageLocationCode(null);
                userProfile.setDefaultLanguageLocationCode(defaultLanguageLocationCode);
                userProfile.setIsDefaultLanguageLocationCode(true);

                langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, defaultLanguageLocationCode);
                if (langLocCode != null) {
                    stateCode = langLocCode.getStateCode();
                    state = stateService.findRecordByStateCode(stateCode);
                    if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
                        userProfile.setMaxStateLevelCappingValue(state.getMaCapping());
                    } else {
                        userProfile.setMaxStateLevelCappingValue(state.getMkCapping());
                    }
                    frontLineWorker.setDefaultLanguageLocationCodeId(defaultLanguageLocationCode);
                    frontLineWorker.setLanguageLocationCodeId(null);
                } else {
                    throw new Exception("Language Location could not be found using state and district");
                }


            } else {
                userProfile.setDefaultLanguageLocationCode(null);
                userProfile.setIsDefaultLanguageLocationCode(true);
                userProfile.setLanguageLocationCode(null);
                userProfile.setMaxStateLevelCappingValue(ConfigurationConstants.CAPPING_NOT_FOUND_BY_STATE);
                frontLineWorker.setDefaultLanguageLocationCodeId(null);
                frontLineWorker.setLanguageLocationCodeId(null);
            }
        }

        userProfile.setCircle(circleCode);
        userProfile.setNmsId(frontLineWorker.getId());
        userProfile.setCreated(true);
        userProfile.setMsisdn(msisdn);

        frontLineWorker.setStatus(Status.ANONYMOUS);
    }

    /**
     * This procedure generated UserDetails for an InactiveUser using its state and district details
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param userProfile     the User details that are to be returned by the API call
     * @param service         the module which is invoking the API
     * @throws DataValidationException,java.lang.Exception
     * @circleCode the circle code deduced from the call
     */
    private void getUserDetailsForInactiveUser(String msisdn, FrontLineWorker frontLineWorker, UserProfile userProfile,
                                               ServicesUsingFrontLineWorker service, String circleCode)
            throws DataValidationException, Exception {

        State state = null;
        Long stateCode = frontLineWorker.getStateCode();
        Long districtCode = frontLineWorker.getDistrictId().getDistrictCode();
        Integer languageLocationCode = null;

        languageLocationCode = languageLocationCodeService.getLanguageLocationCodeByLocationCode(stateCode, districtCode);
        if (languageLocationCode != null) {
            userProfile.setLanguageLocationCode(languageLocationCode);
            userProfile.setDefaultLanguageLocationCode(null);
            userProfile.setIsDefaultLanguageLocationCode(false);
            state = stateService.findRecordByStateCode(stateCode);
            if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
                userProfile.setMaxStateLevelCappingValue(state.getMaCapping());
            } else {
                userProfile.setMaxStateLevelCappingValue(state.getMkCapping());
            }
            userProfile.setCircle(circleCode);
            userProfile.setNmsId(frontLineWorker.getId());
            userProfile.setCreated(false);
            userProfile.setMsisdn(msisdn);
        } else {
            throw new Exception("Language Location could not be found using state and district");
        }

        frontLineWorker.setStatus(Status.ACTIVE);
        frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
        frontLineWorker.setDefaultLanguageLocationCodeId(null);
        frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
    }

    /**
     * This procedure generated UserDetails for an ActiveUser using its state and district details
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param userProfile     the User details that are to be returned by the API call
     * @param service         the module which is invoking the API
     * @circleCode the circle code deduced from the call
     */
    private void getUserDetailsForActiveUser(String msisdn, FrontLineWorker frontLineWorker, UserProfile userProfile,
                                             ServicesUsingFrontLineWorker service, String circleCode) {

        State state = null;
        Integer languageLocationCode = null;
        Integer defaultLanguageLocationCode = null;

        Long stateCode = frontLineWorker.getStateCode();
        languageLocationCode = frontLineWorker.getLanguageLocationCodeId();
        defaultLanguageLocationCode = frontLineWorker.getDefaultLanguageLocationCodeId();

        userProfile.setLanguageLocationCode(languageLocationCode);
        userProfile.setDefaultLanguageLocationCode(defaultLanguageLocationCode);

        state = stateService.findRecordByStateCode(stateCode);
        if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
            userProfile.setMaxStateLevelCappingValue(state.getMaCapping());
        } else {
            userProfile.setMaxStateLevelCappingValue(state.getMkCapping());
        }
        userProfile.setCircle(circleCode);
        userProfile.setNmsId(frontLineWorker.getId());
        userProfile.setCreated(false);
        userProfile.setMsisdn(msisdn);

        if (languageLocationCode == null) {
            userProfile.setIsDefaultLanguageLocationCode(true);
        } else {
            userProfile.setIsDefaultLanguageLocationCode(false);
        }
    }


    /**
     * This procedure generated UserDetails for an Anonymous Repeat User using its details stored by earlier calls
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param userProfile     the User details that are to be returned by the API call
     * @param service         the module which is invoking the API
     * @throws DataValidationException,java.lang.Exception
     * @circleCode the circle code deduced from the call
     */
    private void getUserDetailsForAnonymousUser(String msisdn, FrontLineWorker frontLineWorker, UserProfile userProfile,
                                                ServicesUsingFrontLineWorker service, String circleCode)
            throws DataValidationException, Exception {
        Integer languageLocationCode = null;
        Integer defaultLanguageLocationCode = null;
        LanguageLocationCode langLocCode = null;
        Long stateCode = null;
        State state = null;

        languageLocationCode = frontLineWorker.getLanguageLocationCodeId();
        defaultLanguageLocationCode = frontLineWorker.getDefaultLanguageLocationCodeId();

        userProfile.setLanguageLocationCode(languageLocationCode);
        userProfile.setDefaultLanguageLocationCode(defaultLanguageLocationCode);
        if (languageLocationCode == null) {
            userProfile.setIsDefaultLanguageLocationCode(true);
        } else {
            userProfile.setIsDefaultLanguageLocationCode(false);
        }

        langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, languageLocationCode);
        if (langLocCode != null) {
            stateCode = langLocCode.getStateCode();
            state = stateService.findRecordByStateCode(stateCode);
            if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
                userProfile.setMaxStateLevelCappingValue(state.getMaCapping());
            } else {
                userProfile.setMaxStateLevelCappingValue(state.getMkCapping());
            }

            userProfile.setCircle(circleCode);
            userProfile.setNmsId(frontLineWorker.getId());
            userProfile.setCreated(false);
            userProfile.setMsisdn(msisdn);
        } else {
            throw new Exception("Language Location could not be found using circle and Language Location code");
        }
    }

}
