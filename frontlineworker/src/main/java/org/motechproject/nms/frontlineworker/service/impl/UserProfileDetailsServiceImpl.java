package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(UserProfileDetailsServiceImpl.class);

    /**
     * This procedure implements the API processUserDetails which is used to get the User Details of FrontLine worker
     * by other modules
     *
     * @param msisdn       contact number of the front line worker whose details are to be fetched
     * @param circleCode   the circle code deduced from the call
     * @param operatorCode the operator code deduced by the call
     * @param service      the module which is invoking the API
     * @throws DataValidationException , NmsInternalServerError
     */
    @Override
    public UserProfile processUserDetails(String msisdn, String circleCode, String operatorCode,
                                          ServicesUsingFrontLineWorker service)
            throws DataValidationException, NmsInternalServerError {

        logger.debug("processUserDetails API start");
        UserProfile userProfile = null;
        FrontLineWorker frontLineWorker = null;

        msisdn = validateParams(msisdn, operatorCode, circleCode, service);
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(msisdn);

        if (frontLineWorker == null) {

            userProfile = createUserProfile(msisdn, operatorCode, circleCode, service);

        } else {
            Status status = frontLineWorker.getStatus();

            switch (status) {
                case INACTIVE:
                    userProfile = getUserDetailsForInactiveUser(msisdn, operatorCode, frontLineWorker, service);
                    return userProfile;

                case ANONYMOUS:
                    userProfile = getUserDetailsForAnonymousUser(msisdn, operatorCode, frontLineWorker, service, circleCode);
                    return userProfile;

                case ACTIVE:
                    userProfile = getUserDetailsForActiveUser(msisdn, operatorCode, frontLineWorker, service, circleCode);
                    return userProfile;

                case INVALID:
                    logger.debug("Existing user is invalid. New User profile to be created");
                    userProfile = createUserProfile(msisdn, operatorCode, circleCode, service);
                    return userProfile;
            }
        }
        logger.debug("processUserDetails API ends");
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

        logger.debug("updateLanguageLocationCodeFromMsisdn API start");
        String validatedMsisdn = null;
        LanguageLocationCode languageLocationCodeByParam = new LanguageLocationCode();
        FrontLineWorker frontLineWorker = null;
        validatedMsisdn = ParseDataHelper.validateAndTrimMsisdn("msisdn", msisdn);
        String circleCode = null;

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(validatedMsisdn);
        if (frontLineWorker == null) {
            ParseDataHelper.raiseInvalidDataException("validatedMsisdn ", msisdn);
        } else {
            circleCode = frontLineWorker.getCircleCode();
            if (circleCode.equals(ConfigurationConstants.UNKNOWN_CIRCLE)) {
                languageLocationCodeByParam = languageLocationCodeService.findLLCByCode(languageLocationCode);
                if (languageLocationCodeByParam != null) {
                    frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
                    frontLineWorker.setCircleCode(languageLocationCodeByParam.getCircleCode());
                    frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
                }
                else {
                    ParseDataHelper.raiseInvalidDataException("LanguageLocationCode ", languageLocationCode.toString());
                }
            } else {
                languageLocationCodeByParam = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, languageLocationCode);
                if (languageLocationCodeByParam == null) {
                    ParseDataHelper.raiseInvalidDataException("languageLocationCode ", languageLocationCode.toString());
                } else {
                    frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
                    frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
                }
            }

        }

        logger.debug("updateLanguageLocationCodeFromMsisdn API ends");
    }

    /**
     * This procedure implements the API validateOperator which is used to validate the Operator of the call
     *
     * @param operatorCode the operator code deduced by the call
     * @throws DataValidationException
     */
    @Override
    public void validateOperator(String operatorCode) throws DataValidationException {

        logger.debug("Operator validation start");
        Operator operator = operatorService.getRecordByCode(operatorCode);
        if (operator == null) {
            ParseDataHelper.raiseInvalidDataException("operatorCode", operatorCode);
        }
        logger.debug("Operator validation ends");
    }

    /**
     * This procedure validates the circle code sent in the API call
     *
     * @param circleCode the circle code deduced by the call
     * @throws DataValidationException
     */
    private void validateCircle(String circleCode) throws DataValidationException {

        Circle circle = circleService.getRecordByCode(circleCode);

        if (circle == null) {
            ParseDataHelper.raiseInvalidDataException("circleCode", circleCode);
        }

    }

    /**
     * This procedure validates the service sent in the API call
     *
     * @param service the service deduced by the call
     * @throws DataValidationException
     */
    private void validateService(ServicesUsingFrontLineWorker service) throws DataValidationException {

        if (service != ServicesUsingFrontLineWorker.MOBILEACADEMY && service != ServicesUsingFrontLineWorker.MOBILEKUNJI) {
            ParseDataHelper.raiseInvalidDataException("service", service.toString());
        }

    }

    /**
     * This procedure creates a new UserProfile when a call is made by a number which is not present in the Database
     *
     * @param msisdn       contact number of the front line worker whose details are to be fetched
     * @param operatorCode the operator by which the call is generated
     * @param circleCode   the circle code deduced from the call
     * @param service      the module which is invoking the API
     * @throws DataValidationException , java.lang.Exception
     */
    private UserProfile createUserProfile(String msisdn, String operatorCode, String circleCode,
                                          ServicesUsingFrontLineWorker service) throws DataValidationException {

        logger.debug("New User Profile to be created with msisdn = {}, operatorCode = {}, circleCode = {}, service " +
                "= {} ", msisdn, operatorCode, circleCode, service);

        UserProfile userProfile = getUserDetailsByCircle(msisdn, circleCode, service);
        FrontLineWorker frontLineWorker = new FrontLineWorker();
        frontLineWorker.setContactNo(msisdn);
        frontLineWorker.setOperatorCode(operatorCode);
        if(!userProfile.isDefaultLanguageLocationCode()) {
            frontLineWorker.setLanguageLocationCodeId(userProfile.getLanguageLocationCode());
        }
        frontLineWorker.setCircleCode(userProfile.getCircle());
        frontLineWorker.setStatus(Status.ANONYMOUS);
        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        userProfile.setCreated(true);

        userProfile.setNmsFlwId(frontLineWorker.getId());
        logger.debug("New user profile created");
        return userProfile;
    }


    /**
     * This procedure validates the parameters that are sent in the ProcessUserDetails API
     *
     * @param msisdn       contact number of the front line worker whose details are to be fetched
     * @param operatorCode the operator code deduced by the call
     * @param circleCode   the circle code deduced by the call
     * @param service      the module which is invoking the API
     * @throws DataValidationException
     */
    private String validateParams(String msisdn, String operatorCode, String circleCode,
                                  ServicesUsingFrontLineWorker service) throws DataValidationException {
        String validatedMsisdn = null;

        validatedMsisdn = ParseDataHelper.validateAndTrimMsisdn("msisdn", msisdn);
        validateOperator(operatorCode);

        validateCircle(circleCode);

        validateService(service);

        return validatedMsisdn;

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
    private UserProfile getUserDetailsByCircle(String msisdn, String circleCode, ServicesUsingFrontLineWorker service) {

        UserProfile userProfile = new UserProfile();
        Integer locationCode = null;
        Integer defaultLanguageLocationCode = null;
        LanguageLocationCode languageLocationCode = null;
        Long stateCode = null;

        locationCode = languageLocationCodeService.getLanguageLocationCodeByCircleCode(circleCode);
        if (locationCode != null) {
            //unique language location code is found for the provided circle
            logger.debug("language location code found by circle = {}", locationCode);
            languageLocationCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, locationCode);
            userProfile.setIsDefaultLanguageLocationCode(false);
            userProfile.setLanguageLocationCode(locationCode);
            stateCode = languageLocationCode.getStateCode();
            userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
            userProfile.setCircle(languageLocationCode.getCircleCode());


        } else {
            defaultLanguageLocationCode = languageLocationCodeService.getDefaultLanguageLocationCodeByCircleCode(circleCode);
            if (defaultLanguageLocationCode != null) {
                //no or multiple language location codes is found for the provided circle. here default language location
                // code is fetched from circle
                logger.debug("default language location code found by circle = {}", defaultLanguageLocationCode);
                languageLocationCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, defaultLanguageLocationCode);
                userProfile.setIsDefaultLanguageLocationCode(true);
                userProfile.setLanguageLocationCode(defaultLanguageLocationCode);
                stateCode = languageLocationCode.getStateCode();
                userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
                userProfile.setCircle(languageLocationCode.getCircleCode());


            } else {
                //here the default language location code for circle is also not found.
                logger.debug("both language location code and default language location code not found");
                userProfile.setIsDefaultLanguageLocationCode(true);
                userProfile.setLanguageLocationCode(null);
                userProfile.setMaxStateLevelCappingValue(ConfigurationConstants.CAPPING_NOT_FOUND_BY_STATE);
                userProfile.setCircle(circleCode);

            }
        }

        userProfile.setMsisdn(msisdn);

        return userProfile;
    }

    /**
     * This procedure generated UserDetails for an InactiveUser using its state and district details
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param operatorCode    the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @throws NmsInternalServerError
     */
    private UserProfile getUserDetailsForInactiveUser(String msisdn, String operatorCode, FrontLineWorker frontLineWorker,
                                                      ServicesUsingFrontLineWorker service)
            throws NmsInternalServerError {
        logger.debug("User details to be found for inactive user");
        Long stateCode = frontLineWorker.getStateCode();
        Long districtCode = frontLineWorker.getDistrictId().getDistrictCode();
        LanguageLocationCode languageLocationCode = null;
        UserProfile userProfile = new UserProfile();

        languageLocationCode = languageLocationCodeService.getRecordByLocationCode(stateCode, districtCode);
        if (languageLocationCode != null) {
            logger.debug("language location code found by state and district = {}", languageLocationCode);
            userProfile.setLanguageLocationCode(languageLocationCode.getLanguageLocationCode());
            userProfile.setIsDefaultLanguageLocationCode(false);
            userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
            userProfile.setNmsFlwId(frontLineWorker.getId());
            userProfile.setCreated(true);
            userProfile.setMsisdn(msisdn);
            userProfile.setCircle(languageLocationCode.getCircleCode());
        } else {
            String errMessage = String.format("Language Location code not found for state : %s and district : %s", stateCode, districtCode);
            throw new NmsInternalServerError(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);
        }

        frontLineWorker.setStatus(Status.ACTIVE);
        frontLineWorker.setLanguageLocationCodeId(languageLocationCode.getLanguageLocationCode());
        frontLineWorker.setCircleCode(languageLocationCode.getCircleCode());
        frontLineWorker.setOperatorCode(operatorCode);
        frontLineWorkerService.updateFrontLineWorker(frontLineWorker);

        return userProfile;
    }

    /**
     * This procedure generated UserDetails for an ActiveUser using its state and district details
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param operatorCode    the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn     *
     * @param service         the module which is invoking the API
     * @param circleCode      the circle code deduced from the call
     * @throws NmsInternalServerError
     */
    private UserProfile getUserDetailsForActiveUser(String msisdn, String operatorCode, FrontLineWorker frontLineWorker,
                                                    ServicesUsingFrontLineWorker service, String circleCode)
            throws NmsInternalServerError {
        logger.debug("User details to be found for active user");
        Integer locationCode = null;
        UserProfile userProfile = new UserProfile();
        String circle = null;
        LanguageLocationCode languageLocationCode = null;

        Long stateCode = frontLineWorker.getStateCode();
        Long districtCode = frontLineWorker.getDistrictId().getDistrictCode();

        locationCode = frontLineWorker.getLanguageLocationCodeId();
        circle = frontLineWorker.getCircleCode();

        if (locationCode != null && circle != ConfigurationConstants.UNKNOWN_CIRCLE) {
            logger.debug(" Language Location code = {}, Circle Code = {}", locationCode, circle);
            userProfile.setLanguageLocationCode(locationCode);
            userProfile.setCircle(circle);
        } else {
            if (locationCode == null && circle == ConfigurationConstants.UNKNOWN_CIRCLE) {
                languageLocationCode = languageLocationCodeService.getRecordByLocationCode(stateCode,
                        districtCode);
                if (languageLocationCode != null) {
                    logger.debug("Language Location code found by state and district = {}",
                            languageLocationCode.getLanguageLocationCode());
                    userProfile.setLanguageLocationCode(languageLocationCode.getLanguageLocationCode());
                    userProfile.setCircle(languageLocationCode.getCircleCode());
                    frontLineWorker.setCircleCode(languageLocationCode.getCircleCode());
                    frontLineWorker.setLanguageLocationCodeId(languageLocationCode.getLanguageLocationCode());
                } else {
                    String errMessage = String.format("Language Location code not found for state : %s and district : %s", stateCode, districtCode);
                    throw new NmsInternalServerError(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);
                }
            } else {
                languageLocationCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode,
                        locationCode);
                if (languageLocationCode == null) {
                    String errMessage = String.format("Language Location code not found for circle : %s", circleCode);
                    throw new NmsInternalServerError(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);
                } else {
                    logger.debug("Language Location code found by circle = {}", languageLocationCode.getLanguageLocationCode());
                    userProfile.setLanguageLocationCode(locationCode);
                    userProfile.setCircle(circleCode);

                    frontLineWorker.setCircleCode(languageLocationCode.getCircleCode());

                }
            }


        }
        userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
        userProfile.setNmsFlwId(frontLineWorker.getId());
        userProfile.setCreated(false);
        userProfile.setMsisdn(msisdn);
        userProfile.setIsDefaultLanguageLocationCode(false);

        frontLineWorker.setOperatorCode(operatorCode);
        frontLineWorkerService.updateFrontLineWorker(frontLineWorker);

        return userProfile;
    }


    /**
     * This procedure generated UserDetails for an Anonymous Repeat User using its details stored by earlier calls
     *
     * @param msisdn          contact number of the front line worker whose details are to be fetched
     * @param operatorCode    the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @param circleCode      the circle code deduced from the call
     * @throws NmsInternalServerError
     */
    private UserProfile getUserDetailsForAnonymousUser(String msisdn, String operatorCode, FrontLineWorker frontLineWorker,
                                                       ServicesUsingFrontLineWorker service, String circleCode)
            throws NmsInternalServerError {
        logger.debug("User details to be found for anonymous user");
        Integer languageLocationCode = null;
        LanguageLocationCode langLocCode = null;
        Long stateCode = null;
        UserProfile userProfile = new UserProfile();
        String circle = null;

        circle = frontLineWorker.getCircleCode();
        languageLocationCode = frontLineWorker.getLanguageLocationCodeId();

        if (circle != ConfigurationConstants.UNKNOWN_CIRCLE && languageLocationCode != null) {
            langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circle, languageLocationCode);
            if (langLocCode != null) {
                logger.debug(" Language Location code = {}, Circle Code = {}", languageLocationCode, circle);
                userProfile.setIsDefaultLanguageLocationCode(false);
                stateCode = langLocCode.getStateCode();
                userProfile.setLanguageLocationCode(languageLocationCode);
                userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
                userProfile.setCircle(circle);
                userProfile.setNmsFlwId(frontLineWorker.getId());
                userProfile.setCreated(false);
                userProfile.setMsisdn(msisdn);

                frontLineWorker.setOperatorCode(operatorCode);
                frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
            } else {
                String errMessage = String.format("Language Location code not found for circle : %s", circle);
                throw new NmsInternalServerError(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);

            }
        } else {
            userProfile = getUserDetailsByCircle(msisdn, circleCode, service);
            userProfile.setNmsFlwId(frontLineWorker.getId());
            userProfile.setCreated(false);

            if(!userProfile.isDefaultLanguageLocationCode()) {
                frontLineWorker.setLanguageLocationCodeId(userProfile.getLanguageLocationCode());
            }
            frontLineWorker.setCircleCode(userProfile.getCircle());
            frontLineWorker.setOperatorCode(operatorCode);
            frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
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
