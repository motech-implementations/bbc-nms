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
public class UserProfileDetailsServiceImpl implements UserProfileDetailsService {

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
            throws DataValidationException {

        UserProfile userProfile = null;
        FrontLineWorker frontLineWorker = null;
        Operator operator = null;

        operator = validateParams(msisdn, operatorCode, service);
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(msisdn);

        if (frontLineWorker == null) {

            userProfile = createUserProfile(msisdn, operator, circleCode, service);

        } else {
            Status status = frontLineWorker.getStatus();

            switch (status) {
                case INACTIVE:
                    userProfile = getUserDetailsForInactiveUser(msisdn, operator, frontLineWorker, service, circleCode);
                    return userProfile;

                case ANONYMOUS:
                    userProfile = getUserDetailsForAnonymousUser(msisdn, operator, frontLineWorker, service, circleCode);
                    return userProfile;

                case ACTIVE:
                    userProfile = getUserDetailsForActiveUser(msisdn, operator, frontLineWorker, service, circleCode);
                    return userProfile;

                case INVALID:
                    userProfile = createUserProfile(msisdn, operator, circleCode, service);
                    return userProfile;
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

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(validatedMsisdn);
        if (frontLineWorker == null) {
            ParseDataHelper.raiseInvalidDataException("validatedMsisdn ", msisdn);
        } else {
            languageLocationCodeByParam = languageLocationCodeService.findLLCByCode(languageLocationCode);
            if (languageLocationCodeByParam == null) {
                ParseDataHelper.raiseInvalidDataException("languageLocationCode ", languageLocationCode.toString());
            } else {
                frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
                frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
            }
        }
    }

    /**
     * This procedure implements the API validateOperator which is used to validate the Operator of the call
     *
     * @param operatorCode the operator code deduced by the call
     * @throws DataValidationException
     */
    @Override
    public void validateOperator(String operatorCode) throws DataValidationException {
        Operator operator = operatorService.getRecordByCode(operatorCode);

        if (operator == null) {
            ParseDataHelper.raiseInvalidDataException("operatorCode", operatorCode);
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
    private UserProfile createUserProfile(String msisdn, Operator operator, String circleCode,
                                          ServicesUsingFrontLineWorker service) throws DataValidationException {

        UserProfile userProfile = getUserDetailsByCircle(msisdn, circleCode, service);
        FrontLineWorker frontLineWorker = new FrontLineWorker();
        frontLineWorker.setContactNo(msisdn);
        frontLineWorker.setOperatorCode(operator.getCode());
        if (userProfile.isDefaultLanguageLocationCode() != true) {
            frontLineWorker.setLanguageLocationCodeId(userProfile.getLanguageLocationCode());
            frontLineWorker.setCircleCode(userProfile.getCircle());
        }

        frontLineWorker.setStatus(Status.ANONYMOUS);
        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        userProfile.setNmsFlwId(frontLineWorker.getId());
        return userProfile;
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
     * @param msisdn     contact number of the front line worker whose details are to be fetched
     * @param circleCode the circle code deduced from the call
     * @param service    the module which is invoking the API
     * @param circleCode the circle code deduced from the call
     * @throws DataValidationException,java.lang.Exception
     */
    private UserProfile getUserDetailsByCircle(String msisdn, String circleCode, ServicesUsingFrontLineWorker service)
            throws DataValidationException {

        UserProfile userProfile = new UserProfile();
        Integer locationCode = null;
        Integer defaultLanguageLocationCode = null;
        LanguageLocationCode languageLocationCode = null;
        Long stateCode = null;


        locationCode = languageLocationCodeService.getLanguageLocationCodeByCircleCode(circleCode);
        if (locationCode != null) {
            //unique language location code is found for the provided circle
            userProfile.setIsDefaultLanguageLocationCode(false);
            userProfile.setLanguageLocationCode(locationCode);

            languageLocationCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, locationCode);
            if (languageLocationCode != null) {
                stateCode = languageLocationCode.getStateCode();
                userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
                userProfile.setCircle(languageLocationCode.getCircleCode());
            } else {
                ParseDataHelper.raiseInvalidDataException("Circle Code", circleCode);
            }

        } else {
            defaultLanguageLocationCode = languageLocationCodeService.getDefaultLanguageLocationCodeByCircleCode(circleCode);
            if (defaultLanguageLocationCode != null) {
                //no or multiple language location codes is found for the provided circle. here default language location
                // code is fetched from circle
                userProfile.setLanguageLocationCode(defaultLanguageLocationCode);
                userProfile.setIsDefaultLanguageLocationCode(true);


                languageLocationCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, defaultLanguageLocationCode);
                if (languageLocationCode != null) {
                    stateCode = languageLocationCode.getStateCode();
                    userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
                    userProfile.setCircle(null);
                } else {
                    ParseDataHelper.raiseInvalidDataException("Circle Code", circleCode);
                }


            } else {
                //here the default language location code for circle is also not found.
                userProfile.setIsDefaultLanguageLocationCode(true);
                userProfile.setLanguageLocationCode(null);
                userProfile.setMaxStateLevelCappingValue(ConfigurationConstants.CAPPING_NOT_FOUND_BY_STATE);
                userProfile.setCircle(null);

            }
        }

        userProfile.setCreated(true);
        userProfile.setMsisdn(msisdn);

        return userProfile;
    }

    /**
     * This procedure generated UserDetails for an InactiveUser using its state and district details
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param operator        the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @param circleCode      the circle code deduced from the call
     * @throws DataValidationException
     */
    private UserProfile getUserDetailsForInactiveUser(String msisdn, Operator operator, FrontLineWorker frontLineWorker,
                                                      ServicesUsingFrontLineWorker service, String circleCode)
            throws DataValidationException {

        Long stateCode = frontLineWorker.getStateCode();
        Long districtCode = frontLineWorker.getDistrictId().getDistrictCode();
        LanguageLocationCode languageLocationCode = null;
        UserProfile userProfile = new UserProfile();

        languageLocationCode = languageLocationCodeService.getRecordByLocationCode(stateCode, districtCode);
        if (languageLocationCode != null) {
            userProfile.setLanguageLocationCode(languageLocationCode.getLanguageLocationCode());
            userProfile.setIsDefaultLanguageLocationCode(false);
            userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
            userProfile.setNmsFlwId(frontLineWorker.getId());
            userProfile.setCreated(true);
            userProfile.setMsisdn(msisdn);
            userProfile.setCircle(languageLocationCode.getCircleCode());
        } else {
            ParseDataHelper.raiseInvalidDataException("State Code", stateCode.toString());

        }

        frontLineWorker.setStatus(Status.ACTIVE);
        frontLineWorker.setLanguageLocationCodeId(languageLocationCode.getLanguageLocationCode());
        frontLineWorker.setCircleCode(languageLocationCode.getCircle().getCode());
        frontLineWorker.setOperatorCode(operator.getCode());
        frontLineWorkerService.updateFrontLineWorker(frontLineWorker);

        return userProfile;
    }

    /**
     * This procedure generated UserDetails for an ActiveUser using its state and district details
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param operator        the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn     *
     * @param service         the module which is invoking the API
     * @param circleCode      the circle code deduced from the call
     * @throws DataValidationException
     */
    private UserProfile getUserDetailsForActiveUser(String msisdn, Operator operator, FrontLineWorker frontLineWorker,
                                                    ServicesUsingFrontLineWorker service, String circleCode)
            throws DataValidationException {

        Integer locationCode = null;
        UserProfile userProfile = new UserProfile();
        String circle = null;
        LanguageLocationCode languageLocationCode = null;

        Long stateCode = frontLineWorker.getStateCode();
        Long districtCode = frontLineWorker.getDistrictId().getDistrictCode();

        locationCode = frontLineWorker.getLanguageLocationCodeId();
        circle = frontLineWorker.getCircleCode();

        if (locationCode != null && circle != null) {
            userProfile.setLanguageLocationCode(locationCode);
            userProfile.setCircle(circle);
        } else {
            if (locationCode != null && circle == null) {
                languageLocationCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, locationCode);
                if (languageLocationCode != null) {
                    userProfile.setLanguageLocationCode(languageLocationCode.getLanguageLocationCode());
                    userProfile.setCircle(circleCode);
                    frontLineWorker.setCircleCode(circleCode);

                } else {
                    ParseDataHelper.raiseInvalidDataException("Circle Code", circleCode);

                }
            } else {
                languageLocationCode = languageLocationCodeService.getRecordByLocationCode(stateCode,
                        districtCode);
                if (languageLocationCode != null) {
                    userProfile.setLanguageLocationCode(languageLocationCode.getLanguageLocationCode());
                    userProfile.setCircle(languageLocationCode.getCircleCode());
                    frontLineWorker.setCircleCode(languageLocationCode.getCircleCode());
                    frontLineWorker.setLanguageLocationCodeId(languageLocationCode.getLanguageLocationCode());
                } else {
                    ParseDataHelper.raiseInvalidDataException("State Code", stateCode.toString());

                }
            }

        }

        userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
        userProfile.setNmsFlwId(frontLineWorker.getId());
        userProfile.setCreated(false);
        userProfile.setMsisdn(msisdn);
        userProfile.setIsDefaultLanguageLocationCode(false);

        frontLineWorker.setOperatorCode(operator.getCode());
        frontLineWorkerService.updateFrontLineWorker(frontLineWorker);

        return userProfile;
    }


    /**
     * This procedure generated UserDetails for an Anonymous Repeat User using its details stored by earlier calls
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param operator        the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @param circleCode      the circle code deduced from the call
     * @throws DataValidationException
     */
    private UserProfile getUserDetailsForAnonymousUser(String msisdn, Operator operator, FrontLineWorker frontLineWorker,
                                                       ServicesUsingFrontLineWorker service, String circleCode)
            throws DataValidationException {
        Integer languageLocationCode = null;
        LanguageLocationCode langLocCode = null;
        Long stateCode = null;
        UserProfile userProfile = new UserProfile();
        String circle = null;

        circle = frontLineWorker.getCircleCode();
        languageLocationCode = frontLineWorker.getLanguageLocationCodeId();

        if (circle != null && languageLocationCode != null) {
            langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, languageLocationCode);
            if (langLocCode != null) {
                userProfile.setIsDefaultLanguageLocationCode(false);
                stateCode = langLocCode.getStateCode();
                userProfile.setLanguageLocationCode(languageLocationCode);
                userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
                userProfile.setCircle(circle);
                userProfile.setNmsFlwId(frontLineWorker.getId());
                userProfile.setCreated(false);
                userProfile.setMsisdn(msisdn);

                frontLineWorker.setOperatorCode(operator.getCode());
                frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
            } else {
                ParseDataHelper.raiseInvalidDataException("Circle Code", circleCode);

            }
        } else {
            if (languageLocationCode != null) {
                userProfile = generateUserProfileUsingLLC(msisdn, circleCode, service, languageLocationCode);
                userProfile.setNmsFlwId(frontLineWorker.getId());
                frontLineWorker.setCircleCode(userProfile.getCircle());
                frontLineWorker.setOperatorCode(operator.getCode());
                frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
            } else {

                userProfile = getUserDetailsByCircle(msisdn, circleCode, service);
                userProfile.setNmsFlwId(frontLineWorker.getId());
                userProfile.setCreated(false);

                frontLineWorker.setOperatorCode(operator.getCode());
                if (userProfile.isDefaultLanguageLocationCode() != true) {
                    frontLineWorker.setLanguageLocationCodeId(userProfile.getLanguageLocationCode());
                    frontLineWorker.setCircleCode(userProfile.getCircle());
                }

                frontLineWorkerService.updateFrontLineWorker(frontLineWorker);

            }


        }

        return userProfile;
    }

    /**
     * This procedure generated UserDetails for an Anonymous Repeat User using its details stored by earlier calls
     *
     * @param msisdn               contact number of the front line worker whose details are to be fetched
     * @param circleCode           the circle code deduced from the call
     * @param service              the module which is invoking the API
     * @param languageLocationCode the language location code saved in front line worker record
     * @throws DataValidationException
     */
    private UserProfile generateUserProfileUsingLLC(String msisdn, String circleCode,
                                                    ServicesUsingFrontLineWorker service, Integer languageLocationCode)
            throws DataValidationException {

        LanguageLocationCode langLocCode = null;
        UserProfile userProfile = new UserProfile();
        Long stateCode = null;
        langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, languageLocationCode);

        if (langLocCode == null) {
            ParseDataHelper.raiseInvalidDataException("circleCode", circleCode);
        } else {
            userProfile.setIsDefaultLanguageLocationCode(false);
            userProfile.setLanguageLocationCode(langLocCode.getLanguageLocationCode());
            stateCode = langLocCode.getStateCode();
            userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
            userProfile.setCircle(langLocCode.getCircleCode());
            userProfile.setCreated(false);
            userProfile.setMsisdn(msisdn);
        }

        return userProfile;
    }

    /**
     * This procedure generated UserDetails for an Anonymous Repeat User using its details stored by earlier calls
     *
     * @param stateCode the state code that will be used to find the Max state level capping
     * @param service   the module which is invoking the API
     */
    private Integer findMaxCapping(Long stateCode, ServicesUsingFrontLineWorker service) {
        State state = null;
        Integer maxStateLevelCapping = null;
        state = stateService.findRecordByStateCode(stateCode);
        if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
            maxStateLevelCapping = state.getMaCapping();
        } else {
            maxStateLevelCapping = state.getMkCapping();
        }
        return maxStateLevelCapping;
    }

}
