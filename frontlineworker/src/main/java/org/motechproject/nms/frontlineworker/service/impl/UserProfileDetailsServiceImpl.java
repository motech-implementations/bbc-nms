package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.enums.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.enums.Status;
import org.motechproject.nms.frontlineworker.exception.FlwNotInWhiteListException;
import org.motechproject.nms.frontlineworker.exception.ServiceNotDeployedException;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.frontlineworker.service.WhiteListUsersService;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LocationService;
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

    private static Logger logger = LoggerFactory.getLogger(UserProfileDetailsServiceImpl.class);
    private FrontLineWorkerService frontLineWorkerService;
    private OperatorService operatorService;
    private CircleService circleService;
    private LanguageLocationCodeService languageLocationCodeService;
    private StateService stateService;
    private WhiteListUsersService whiteListUsersService;
    private LocationService locationService;

    @Autowired
    public UserProfileDetailsServiceImpl(FrontLineWorkerService frontLineWorkerService, OperatorService operatorService,
                                         CircleService circleService, LanguageLocationCodeService languageLocationCodeService,
                                         StateService stateService, WhiteListUsersService whiteListUsersService,
                                         LocationService locationService) {
        this.frontLineWorkerService = frontLineWorkerService;
        this.operatorService = operatorService;
        this.circleService = circleService;
        this.languageLocationCodeService = languageLocationCodeService;
        this.stateService = stateService;
        this.whiteListUsersService = whiteListUsersService;
        this.locationService = locationService;

    }

    @Override
    public UserProfile processUserDetails(String msisdn, String circleCode, String operatorCode,
                                          ServicesUsingFrontLineWorker service)
            throws DataValidationException, NmsInternalServerError, FlwNotInWhiteListException, ServiceNotDeployedException {

        logger.trace("processUserDetails API start processing");
        UserProfile userProfile = null;
        FrontLineWorker frontLineWorker = null;

        msisdn = validateProcessUserDetailsParams(msisdn, operatorCode, circleCode, service);
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(msisdn);

        if (null == frontLineWorker) {

            userProfile = createUserProfile(msisdn, operatorCode, circleCode, service);

        } else {
            Status status = frontLineWorker.getStatus();

            switch (status) {
                case INACTIVE:
                    userProfile = getUserDetailsForInactiveUser(operatorCode, frontLineWorker, service);
                    break;

                case ANONYMOUS:
                    userProfile = getUserDetailsForAnonymousUser(operatorCode, frontLineWorker, service, circleCode);
                    break;

                case ACTIVE:
                    userProfile = getUserDetailsForActiveUser(operatorCode, frontLineWorker, service, circleCode);
                    break;

                case INVALID:
                    logger.trace("Existing user is invalid. New User profile to be created");
                    userProfile = createUserProfile(msisdn, operatorCode, circleCode, service);
                    break;

                default:
                    logger.trace("Invalid status exist in Db");
                    ParseDataHelper.raiseInvalidDataException("Status ", status.toString());
                    break;
            }
        }
        logger.trace("processUserDetails API ends");
        return userProfile;
    }

    @Override
    public void updateLanguageLocationCodeFromMsisdn(String languageLocationCode, String msisdn,
                                                     ServicesUsingFrontLineWorker service)
            throws DataValidationException, FlwNotInWhiteListException, ServiceNotDeployedException {

        Long stateCode = null;
        logger.debug("updateLanguageLocationCodeFromMsisdn API start");
        String validatedMsisdn = null;
        LanguageLocationCode languageLocationCodeByParam = null;
        FrontLineWorker frontLineWorker = null;
        validatedMsisdn = ParseDataHelper.validateAndTrimMsisdn("CallingNumber", msisdn);
        String circleCode = null;

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(validatedMsisdn);
        if (frontLineWorker == null) {
            ParseDataHelper.raiseInvalidDataException("CallingNumber ", msisdn);
        } else {
            circleCode = frontLineWorker.getCircleCode();
            if (ConfigurationConstants.UNKNOWN_CIRCLE.equals(circleCode)) {
                languageLocationCodeByParam = languageLocationCodeService.findLLCByCode(languageLocationCode);
                if (null != languageLocationCodeByParam) {
                    frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
                    frontLineWorker.setCircleCode(languageLocationCodeByParam.getCircleCode());
                    frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
                } else {
                    ParseDataHelper.raiseInvalidDataException("LanguageLocationCode ", languageLocationCode);
                }
            } else {
                languageLocationCodeByParam = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, languageLocationCode);
                if (null == languageLocationCodeByParam) {
                    ParseDataHelper.raiseInvalidDataException("LanguageLocationCode ", languageLocationCode);
                } else {
                    stateCode = languageLocationCodeByParam.getStateCode();
                    checkIsDeployedAndIsInWhiteList(msisdn, service, stateCode);
                    frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
                    frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
                }
            }

        }

        logger.debug("updateLanguageLocationCodeFromMsisdn API ends");
    }

    @Override
    public void validateOperator(String operatorCode) throws DataValidationException {

        logger.trace("Validating operator");
        Operator operator = operatorService.getRecordByCode(operatorCode);
        if (operator == null) {
            ParseDataHelper.raiseInvalidDataException("operator", operatorCode);
        }

    }


    @Override
    public void validateCircle(String circleCode) throws DataValidationException {

        logger.trace("Validating circle");

        Circle circle = circleService.getRecordByCode(circleCode);

        if (circle == null) {
            ParseDataHelper.raiseInvalidDataException("circle", circleCode);
        }

    }

    /**
     * This procedure validates the service sent in the API call
     *
     * @param service the service deduced by the call
     * @throws DataValidationException
     */
    private void validateService(ServicesUsingFrontLineWorker service) throws DataValidationException {

        logger.trace("Validating serviceType");

        if (null == service) {
            ParseDataHelper.raiseInvalidDataException("service", null);
        }

        if (service != ServicesUsingFrontLineWorker.MOBILEACADEMY && service != ServicesUsingFrontLineWorker.MOBILEKUNJI) {
            ParseDataHelper.raiseInvalidDataException("service", service.toString());
        }

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
    private String validateProcessUserDetailsParams(String msisdn, String operatorCode, String circleCode,
                                                    ServicesUsingFrontLineWorker service) throws DataValidationException {

        validateOperator(operatorCode);
        validateCircle(circleCode);
        validateService(service);

        return ParseDataHelper.validateAndTrimMsisdn("callingNumber", msisdn);

    }

    /**
     * This procedure creates a new UserProfile when a call is made by a number which is not present in the Database
     *
     * @param msisdn       contact number of the front line worker whose details are to be fetched
     * @param operatorCode the operator by which the call is generated
     * @param circleCode   the circle code deduced from the call
     * @param service      the module which is invoking the API
     * @throws DataValidationException , java.lang.exception
     */
    private UserProfile createUserProfile(String msisdn, String operatorCode, String circleCode,
                                          ServicesUsingFrontLineWorker service) throws DataValidationException,
            ServiceNotDeployedException, FlwNotInWhiteListException {

        logger.debug("New User Profile to be created with msisdn = {}, operatorCode = {}, circleCode = {}, service " +
                "= {} ", msisdn, operatorCode, circleCode, service);

        UserProfile userProfile = getUserDetailsByCircle(msisdn, circleCode, service);
        FrontLineWorker frontLineWorker = new FrontLineWorker();
        frontLineWorker.setContactNo(msisdn);
        frontLineWorker.setOperatorCode(operatorCode);
        if (!userProfile.isDefaultLanguageLocationCode()) {
            frontLineWorker.setLanguageLocationCodeId(userProfile.getLanguageLocationCode());
        }
        frontLineWorker.setCircleCode(userProfile.getCircle());
        frontLineWorker.setStatus(Status.ANONYMOUS);
        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        userProfile.setNmsFlwId(frontLineWorker.getId());
        logger.trace("New user profile created");
        return userProfile;
    }


    /**
     * This procedure generates the UserDetails of a record with the circle code given in the API call
     *
     * @param msisdn     contact number of the front line worker whose details are to be fetched
     * @param circleCode the circle code deduced from the call
     * @param service    the module which is invoking the API
     * @param circleCode the circle code deduced from the call
     * @throws FlwNotInWhiteListException,ServiceNotDeployedException
     */
    private UserProfile getUserDetailsByCircle(String msisdn, String circleCode, ServicesUsingFrontLineWorker service)
            throws FlwNotInWhiteListException, ServiceNotDeployedException {

        UserProfile userProfile = null;
        String locationCode = null;
        LanguageLocationCode languageLocationCode = null;

        locationCode = languageLocationCodeService.getLanguageLocationCodeByCircleCode(circleCode);

        if (null != locationCode) {
            languageLocationCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, locationCode);

            logger.trace("Unique LLC exist for the provided circle");
            logger.debug("language location code found by circle = {}", locationCode);
            checkIsDeployedAndIsInWhiteList(msisdn, service, languageLocationCode.getStateCode());
            userProfile = uniqueLLCForAnonymousUser(languageLocationCode, service);

        } else {
            logger.trace("Multiple LLC exist for the provided circle");
            userProfile = multipleLLCForAnonymousUser(service, circleCode);
        }
        userProfile.setMsisdn(msisdn);

        return userProfile;
    }


    /**
     * This procedure generates UserDetails for an Anonymous USer if unique language location code is found
     * for the provided circle
     *
     * @param languageLocationCode LanguageLocationCode
     * @param service              the module which is invoking the API
     * @return userProfile Details
     */
    private UserProfile uniqueLLCForAnonymousUser(LanguageLocationCode languageLocationCode,
                                                  ServicesUsingFrontLineWorker service) {

        UserProfile userProfile = new UserProfile();

        userProfile.setIsDefaultLanguageLocationCode(false);
        userProfile.setLanguageLocationCode(languageLocationCode.getLanguageLocationCode());
        userProfile.setMaxStateLevelCappingValue(findMaxCapping(languageLocationCode.getStateCode(), service));
        userProfile.setCircle(languageLocationCode.getCircleCode());

        return userProfile;

    }

    /**
     * This procedure generates UserDetails for an Anonymous User if multiple language location code is found
     * for the provided circle
     *
     * @param service the module which is invoking the API
     * @return userProfile Details
     */

    private UserProfile multipleLLCForAnonymousUser(ServicesUsingFrontLineWorker service, String circleCode) {
        UserProfile userProfile = new UserProfile();
        String defaultLanguageLocationCode = null;
        LanguageLocationCode languageLocationCode = null;

        defaultLanguageLocationCode = languageLocationCodeService.getDefaultLanguageLocationCodeByCircleCode(circleCode);
        if (null != defaultLanguageLocationCode) {
            //no or multiple language location codes is found for the provided circle. here default language location
            // code is fetched from circle
            logger.debug("default language location code found by circle = {}", defaultLanguageLocationCode);
            languageLocationCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, defaultLanguageLocationCode);
            userProfile.setIsDefaultLanguageLocationCode(true);
            userProfile.setLanguageLocationCode(defaultLanguageLocationCode);
            userProfile.setMaxStateLevelCappingValue(findMaxCapping(languageLocationCode.getStateCode(), service));
            userProfile.setCircle(languageLocationCode.getCircleCode());

        } else {
            //here the default language location code for circle is also not found.
            logger.debug("both language location code and default language location code not found");
            userProfile.setIsDefaultLanguageLocationCode(true);
            userProfile.setLanguageLocationCode(null);
            userProfile.setMaxStateLevelCappingValue(ConfigurationConstants.CAPPING_NOT_FOUND_BY_STATE);
            userProfile.setCircle(circleCode);

        }
        return userProfile;
    }

    /**
     * This procedure generates UserDetails for an InactiveUser using its state and district details
     *
     * @param operatorCode    the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @throws NmsInternalServerError, FlwNotInWhiteListException, ServiceNotDeployedException
     */
    private UserProfile getUserDetailsForInactiveUser(String operatorCode,
                                                      FrontLineWorker frontLineWorker, ServicesUsingFrontLineWorker service)
            throws ServiceNotDeployedException, FlwNotInWhiteListException, NmsInternalServerError {

        checkIsDeployedAndIsInWhiteList(frontLineWorker.getContactNo(), service, frontLineWorker.getStateCode());
        return inactiveUserDetails(operatorCode, frontLineWorker, service);
    }

    /**
     * This procedure generated UserDetails for an InactiveUser using its state and district details after checking the
     * isDeployed and isInWhiteList status
     *
     * @param operatorCode    the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @throws NmsInternalServerError
     */
    private UserProfile inactiveUserDetails(String operatorCode, FrontLineWorker frontLineWorker,
                                            ServicesUsingFrontLineWorker service)
            throws NmsInternalServerError {
        logger.debug("User details to be found for inactive user");

        UserProfile userProfile = new UserProfile();
        LanguageLocationCode languageLocationCode = null;

        Long stateCode = frontLineWorker.getStateCode();
        Long districtCode = frontLineWorker.getDistrictId().getDistrictCode();

        languageLocationCode = languageLocationCodeService.getRecordByLocationCode(stateCode, districtCode);

        if (languageLocationCode != null) {
            logger.debug("language location code found by state and district = {}", languageLocationCode);
            userProfile.setLanguageLocationCode(languageLocationCode.getLanguageLocationCode());
            userProfile.setIsDefaultLanguageLocationCode(false);
            userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
            userProfile.setNmsFlwId(frontLineWorker.getId());
            userProfile.setMsisdn(frontLineWorker.getContactNo());
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
     * @param operatorCode    the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @param circleCode      the circle code deduced from the call
     * @throws NmsInternalServerError, ServiceNotDeployedException, FlwNotInWhiteListException
     */
    private UserProfile getUserDetailsForActiveUser(String operatorCode,
                                                    FrontLineWorker frontLineWorker, ServicesUsingFrontLineWorker service, String circleCode)
            throws ServiceNotDeployedException, FlwNotInWhiteListException, NmsInternalServerError {

        checkIsDeployedAndIsInWhiteList(frontLineWorker.getContactNo(), service, frontLineWorker.getStateCode());
        return activeUserDetails(operatorCode, frontLineWorker, service, circleCode);
    }


    /**
     * This procedure generated UserDetails for an ActiveUser using its state and district detailsafter checking the
     * isDeployed and isInWhiteList status
     *
     * @param operatorCode    the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn     *
     * @param service         the module which is invoking the API
     * @param circleCode      the circle code deduced from the call
     * @throws NmsInternalServerError
     */
    private UserProfile activeUserDetails(String operatorCode, FrontLineWorker frontLineWorker,
                                          ServicesUsingFrontLineWorker service, String circleCode)
            throws NmsInternalServerError {
        logger.debug("User details to be found for active user");
        String locationCode = null;
        UserProfile userProfile = new UserProfile();
        String circle = null;
        LanguageLocationCode languageLocationCode = null;

        Long stateCode = frontLineWorker.getStateCode();
        Long districtCode = frontLineWorker.getDistrictId().getDistrictCode();

        locationCode = frontLineWorker.getLanguageLocationCodeId();
        circle = frontLineWorker.getCircleCode();

        if (null != locationCode && !(ConfigurationConstants.UNKNOWN_CIRCLE.equals(circle))) {
            logger.debug(" Language Location code = {}, Circle Code = {}", locationCode, circle);
            userProfile.setLanguageLocationCode(locationCode);
            userProfile.setCircle(circle);
        } else {
            if (null == locationCode && ConfigurationConstants.UNKNOWN_CIRCLE.equals(circle)) {
                languageLocationCode = languageLocationCodeService.getRecordByLocationCode(stateCode,
                        districtCode);
                if (null != languageLocationCode) {
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
                if (null == languageLocationCode) {
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
        userProfile.setMsisdn(frontLineWorker.getContactNo());
        userProfile.setIsDefaultLanguageLocationCode(false);

        frontLineWorker.setOperatorCode(operatorCode);
        frontLineWorkerService.updateFrontLineWorker(frontLineWorker);

        return userProfile;
    }


    /**
     * This procedure generated UserDetails for an AnonymousUser using its Circle and LanguageLocationCode details after
     * checking the isDeployed and isInWhiteList status
     *
     * @param operatorCode    the operator by which the call is generated
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @param circleCode      the circle code deduced from the call
     * @throws NmsInternalServerError
     */

    private UserProfile getUserDetailsForAnonymousUser(String operatorCode, FrontLineWorker frontLineWorker,
                                                       ServicesUsingFrontLineWorker service, String circleCode)
            throws NmsInternalServerError, FlwNotInWhiteListException, ServiceNotDeployedException {
        logger.trace("User details to be found for anonymous user");

        String languageLocationCode = null;
        LanguageLocationCode langLocCode = null;
        UserProfile userProfile = null;
        String circle = null;
        Long stateCode = null;

        circle = frontLineWorker.getCircleCode();
        languageLocationCode = frontLineWorker.getLanguageLocationCodeId();

        if (!circle.equals(ConfigurationConstants.UNKNOWN_CIRCLE) && null != languageLocationCode) {
            langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circle, languageLocationCode);
            if (null != langLocCode) {
                logger.debug(" Language Location code = {}, Circle Code = {}", languageLocationCode, circle);
                stateCode = langLocCode.getStateCode();

                checkIsDeployedAndIsInWhiteList(frontLineWorker.getContactNo(), service, stateCode);

                userProfile = anonymousUserDetails(langLocCode.getStateCode(), frontLineWorker, service);

            } else {
                String errMessage = String.format("Language Location code not found for circle : %s", circle);
                throw new NmsInternalServerError(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);

            }
        } else {
            userProfile = getUserDetailsByCircle(frontLineWorker.getContactNo(), circleCode, service);
            userProfile.setNmsFlwId(frontLineWorker.getId());
            if (!userProfile.isDefaultLanguageLocationCode()) {
                frontLineWorker.setLanguageLocationCodeId(userProfile.getLanguageLocationCode());
            }
            frontLineWorker.setCircleCode(userProfile.getCircle());
        }
        frontLineWorker.setOperatorCode(operatorCode);
        frontLineWorkerService.updateFrontLineWorker(frontLineWorker);

        return userProfile;

    }

    /**
     * This procedure generated UserDetails for an AnonymousUser using its circle and languageLocationCode details
     *
     * @param stateCode       the stateCode
     * @param frontLineWorker the frontLineWorker found using the msisdn
     * @param service         the module which is invoking the API
     * @return userProfile Details
     */
    private UserProfile anonymousUserDetails(Long stateCode, FrontLineWorker frontLineWorker, ServicesUsingFrontLineWorker service) {

        UserProfile userProfile = new UserProfile();
        userProfile.setIsDefaultLanguageLocationCode(false);
        userProfile.setLanguageLocationCode(frontLineWorker.getLanguageLocationCodeId());
        userProfile.setMaxStateLevelCappingValue(findMaxCapping(stateCode, service));
        userProfile.setCircle(frontLineWorker.getCircleCode());
        userProfile.setNmsFlwId(frontLineWorker.getId());
        userProfile.setMsisdn(frontLineWorker.getContactNo());
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


    /**
     * This procedure checks whether the calling service is deployed in the state or not.
     *
     * @param service   the module which is invoking the API
     * @param stateCode stateCode fetched from frontLineWorker record
     */
    private boolean checkIsDeployedInState(ServicesUsingFrontLineWorker service, Long stateCode) {

        boolean isDeployed = false;

        if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
            if (locationService.getMaServiceDeployedByCode(stateCode)) {
                logger.debug("Mobile Academy Service deployed in state : %s", stateCode);
                isDeployed = true;
            }
        } else {
            if (locationService.getMkServiceDeployedByCode(stateCode)) {
                logger.debug("Mobile Kunji Service deployed in state : %s", stateCode);
                isDeployed = true;
            }
        }
        return isDeployed;
    }

    /**
     * This procedure checks whether the calling msisdn is present in whiteList or not
     *
     * @param msisdn    contact number which is to be checked from whiteList
     * @param stateCode state code fetched from front line worker record
     * @return isInWhiteList status
     */
    private boolean checkIsInWhiteListStatus(String msisdn, Long stateCode) {

        boolean isInWhiteList = true;
        Boolean whiteListStatus;

        whiteListStatus = locationService.getWhiteListingEnableStatusByCode(stateCode);
        if (null != whiteListStatus) {

            if (whiteListStatus.booleanValue()) {

                if (null == whiteListUsersService.findContactNo(msisdn)) {
                    isInWhiteList = false;
                }
            }
        } else {
            logger.debug("WhiteListStatus is null");
        }
        return isInWhiteList;
    }

    /**
     * @param msisdn    contact number of the front line worker whose details are to be fetched
     * @param service   the module which is invoking the API
     * @param stateCode StateCode
     * @throws ServiceNotDeployedException, FlwNotInWhiteListException
     */
    private void checkIsDeployedAndIsInWhiteList(String msisdn, ServicesUsingFrontLineWorker service, Long stateCode)
            throws ServiceNotDeployedException, FlwNotInWhiteListException {

        boolean isDeployed = false;
        boolean isInWhiteList = false;

        isDeployed = checkIsDeployedInState(service, stateCode);
        if (!isDeployed) {
            String errMessage = String.format(" %s is not Deployed for state : %s :", service.toString(), stateCode);
            throw new ServiceNotDeployedException(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);

        }
        isInWhiteList = checkIsInWhiteListStatus(msisdn, stateCode);
        if (!isInWhiteList) {

            String errMessage = String.format("ContactNo not present in WhiteListUsers: %s :", stateCode);
            throw new FlwNotInWhiteListException(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);
        }
    }
}
