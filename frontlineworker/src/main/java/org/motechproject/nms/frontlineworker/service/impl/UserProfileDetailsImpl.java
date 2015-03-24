package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.Status;
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

    @Override
    public UserProfile processUserDetails(String msisdn, String circleCode, String operatorCode,
                                          ServicesUsingFrontLineWorker service)
            throws DataValidationException {

        UserProfile userProfile = new UserProfile();
        FrontLineWorker frontLineWorker = new FrontLineWorker();
        Operator operator = new Operator();

        operator = validateParams(msisdn, circleCode, operatorCode, service);
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(msisdn);

        if (frontLineWorker == null) {

            createUserProfile(msisdn, operator, userProfile, circleCode, service);
        } else {
            Status status = frontLineWorker.getStatus();

            switch (status) {
                case INACTIVE:
                    getUserDetailsForInactiveUser(frontLineWorker, userProfile, service, circleCode);
                    break;

                case ANONYMOUS:
                    getUserDetailsForAnonymousUser(frontLineWorker, userProfile, service, circleCode);
                    break;

                case ACTIVE:
                    getUserDetailsForActiveUser(frontLineWorker, userProfile, service, circleCode);
                    break;

                case INVALID:
                    getUserDetailsByCircle(frontLineWorker, userProfile, circleCode, service);
                    frontLineWorker.setStatus(Status.ANONYMOUS);
                    frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
                    break;
            }
        }
        return userProfile;
    }


    @Override
    public void updateLanguageLocationCodeFromMsisdn(Integer languageLocationCode, String msisdn)
            throws DataValidationException {

    }



    private void createUserProfile(String msisdn, Operator operator, UserProfile userProfile, String circleCode,
                                   ServicesUsingFrontLineWorker service) {

        FrontLineWorker frontLineWorker = new FrontLineWorker();
        frontLineWorker.setContactNo(msisdn);
        frontLineWorker.setOperatorId(operator.getId());
        frontLineWorker.setStatus(Status.ANONYMOUS);

        getUserDetailsByCircle(frontLineWorker, userProfile, circleCode, service);
        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
    }



    private Operator validateParams(String msisdn, String circleCode, String operatorCode,
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


    private void getUserDetailsByCircle(FrontLineWorker frontLineWorker, UserProfile userProfile,
                                        String circleCode, ServicesUsingFrontLineWorker service) {
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
            if(langLocCode != null) {
                stateCode = langLocCode.getStateCode();
                state = stateService.findRecordByStateCode(stateCode);
                if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
                    userProfile.setMaxStateLevelCappingValue(state.getMaCapping());
                } else {
                    userProfile.setMaxStateLevelCappingValue(state.getMkCapping());
                }
                frontLineWorker.setLanguageLocationCodeId(languageLocationCode);
                frontLineWorker.setDefaultLanguageLocationCodeId(null);
            }
            else {
                //throw error
            }


        } else {
            defaultLanguageLocationCode = languageLocationCodeService.getDefaultLanguageLocationCodeByCircleCode(circleCode);
            if (defaultLanguageLocationCode != null) {
                userProfile.setLanguageLocationCode(null);
                userProfile.setDefaultLanguageLocationCode(defaultLanguageLocationCode);
                userProfile.setIsDefaultLanguageLocationCode(true);

                langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, defaultLanguageLocationCode);
                if(langLocCode != null) {
                    stateCode = langLocCode.getStateCode();
                    state = stateService.findRecordByStateCode(stateCode);
                    if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
                        userProfile.setMaxStateLevelCappingValue(state.getMaCapping());
                    } else {
                        userProfile.setMaxStateLevelCappingValue(state.getMkCapping());
                    }
                    frontLineWorker.setDefaultLanguageLocationCodeId(defaultLanguageLocationCode);
                    frontLineWorker.setLanguageLocationCodeId(null);
                }
                else {
                    //throw error
                }


            } else {
                userProfile.setDefaultLanguageLocationCode(null);
                userProfile.setIsDefaultLanguageLocationCode(true);
                userProfile.setLanguageLocationCode(null);
                userProfile.setMaxStateLevelCappingValue(-1);//to be changed
                frontLineWorker.setDefaultLanguageLocationCodeId(null);
                frontLineWorker.setLanguageLocationCodeId(null);
            }
        }

        userProfile.setCircle(circleCode);
        userProfile.setNmsId(frontLineWorker.getId());
        userProfile.setCreated(true);
    }


    private void getUserDetailsForInactiveUser(FrontLineWorker frontLineWorker, UserProfile userProfile,
                                                      ServicesUsingFrontLineWorker service, String circleCode)
            throws DataValidationException {

        State state = null;
        Long stateCode = frontLineWorker.getStateCode();
        Long districtCode = frontLineWorker.getDistrictId().getDistrictCode();
        Integer languageLocationCode = null;
        Integer defaultLanguageLocationCode = null;


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
        } else {
            ParseDataHelper.raiseMissingDataException("language location code", null);//throw exception
        }

        frontLineWorker.setStatus(Status.ACTIVE);
        frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
    }


    private void getUserDetailsForActiveUser(FrontLineWorker frontLineWorker, UserProfile userProfile,
                                                    ServicesUsingFrontLineWorker service, String circleCode) {

        State state = null;
        Long stateCode = frontLineWorker.getStateCode();
        userProfile.setDefaultLanguageLocationCode(frontLineWorker.getDefaultLanguageLocationCodeId());
        userProfile.setLanguageLocationCode(frontLineWorker.getLanguageLocationCodeId());
        state = stateService.findRecordByStateCode(stateCode);
        if (service == ServicesUsingFrontLineWorker.MOBILEACADEMY) {
            userProfile.setMaxStateLevelCappingValue(state.getMaCapping());
        } else {
            userProfile.setMaxStateLevelCappingValue(state.getMkCapping());
        }
        userProfile.setCircle(circleCode);
        userProfile.setNmsId(frontLineWorker.getId());
        userProfile.setCreated(false);

        if(frontLineWorker.getLanguageLocationCodeId() == null) {
            userProfile.setIsDefaultLanguageLocationCode(true);
        }
        else {
            userProfile.setIsDefaultLanguageLocationCode(false);
        }
    }



    private void getUserDetailsForAnonymousUser(FrontLineWorker frontLineWorker, UserProfile userProfile,
                                                       ServicesUsingFrontLineWorker service, String circleCode) {
        Integer languageLocationCode = null;
        Integer defaultLanguageLocationCode = null;
        LanguageLocationCode langLocCode = null;
        Long stateCode = null;
        State state = null;

        languageLocationCode = frontLineWorker.getLanguageLocationCodeId();
        defaultLanguageLocationCode = frontLineWorker.getDefaultLanguageLocationCodeId();

        userProfile.setDefaultLanguageLocationCode(defaultLanguageLocationCode);
        userProfile.setLanguageLocationCode(languageLocationCode);
        if(frontLineWorker.getLanguageLocationCodeId() == null) {
            userProfile.setIsDefaultLanguageLocationCode(true);
        }
        else {
            userProfile.setIsDefaultLanguageLocationCode(false);
        }

        langLocCode = languageLocationCodeService.getRecordByCircleCodeAndLangLocCode(circleCode, languageLocationCode);
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
    }

}
