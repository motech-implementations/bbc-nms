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

        UserProfile userProfile = null;
        FrontLineWorker frontLineWorker = new FrontLineWorker();

        ParseDataHelper.validateAndTrimMsisdn("msisdn", msisdn);

        if (circleCode == null) {
            ParseDataHelper.raiseMissingDataException("circle", null);
        }

        if (operatorCode == null) {
            ParseDataHelper.raiseMissingDataException("operatorCode", null);
        }

        Operator operator = new Operator();
        operator = operatorService.getRecordByCode(operatorCode);

        if (operator == null) {
            ParseDataHelper.raiseInvalidDataException("operatorCode", operatorCode);
        }

        if (service != ServicesUsingFrontLineWorker.MOBILEACADEMY && service != ServicesUsingFrontLineWorker.MOBILEKUNJI) {
            ParseDataHelper.raiseInvalidDataException("service", service.toString());
        }

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo(msisdn);

        if (frontLineWorker == null) {

            frontLineWorker.setContactNo(msisdn);
            frontLineWorker.setOperatorId(operator.getId());
            frontLineWorker.setStatus(Status.ANONYMOUS);

            userProfile = getLanguageLocationCodeByCircle(frontLineWorker, userProfile, circleCode, service);
            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            userProfile.setCreated(true);


        } else {
            Status status = frontLineWorker.getStatus();

            switch (status) {
                case INACTIVE:
                    userProfile = getLanguageLocationCodeForUser(frontLineWorker, userProfile, service, circleCode);
                    frontLineWorker.setStatus(Status.ACTIVE);

                    frontLineWorkerService.updateFrontLineWorker(frontLineWorker);
                    break;

                case ANONYMOUS:
                    userProfile = getDetailsForRepeatAnonymousUser(frontLineWorker, userProfile, service, circleCode);
                    break;

                case ACTIVE:
                    userProfile = getDetailsForRepeatActiveUser(frontLineWorker, userProfile, service, circleCode);
                    break;

                case INVALID:
                    userProfile = getLanguageLocationCodeByCircle(frontLineWorker, userProfile, circleCode, service);
                    userProfile.setCreated(true);
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


    private UserProfile getLanguageLocationCodeByCircle(FrontLineWorker frontLineWorker, UserProfile userProfile,
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
            defaultLanguageLocationCode = languageLocationCodeService.getDefaultLanguageLocationCodeByCircleCode(circleCode);
            if (defaultLanguageLocationCode != null) {
                userProfile.setDefaultLanguageLocationCode(defaultLanguageLocationCode);
                userProfile.setIsDefaultLanguageLocationCode(true);
                userProfile.setLanguageLocationCode(null);
                userProfile.setMaxStateLevelCappingValue(1);//to be changed
                frontLineWorker.setDefaultLanguageLocationCodeId(defaultLanguageLocationCode);
                frontLineWorker.setLanguageLocationCodeId(null);
            } else {
                userProfile.setDefaultLanguageLocationCode(null);
                userProfile.setIsDefaultLanguageLocationCode(true);
                userProfile.setLanguageLocationCode(null);
                userProfile.setMaxStateLevelCappingValue(null);//to be changed
                frontLineWorker.setDefaultLanguageLocationCodeId(null);
                frontLineWorker.setLanguageLocationCodeId(null);
            }
        }

        userProfile.setCircle(circleCode);
        userProfile.setNmsId(frontLineWorker.getId());
        return userProfile;
    }


    private UserProfile getLanguageLocationCodeForUser(FrontLineWorker frontLineWorker, UserProfile userProfile,
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
            ParseDataHelper.raiseMissingDataException("language location code", null);
        }


        return userProfile;

    }


    private UserProfile getDetailsForRepeatActiveUser(FrontLineWorker frontLineWorker, UserProfile userProfile,
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
        return userProfile;
    }



    private UserProfile getDetailsForRepeatAnonymousUser(FrontLineWorker frontLineWorker, UserProfile userProfile,
                                                         ServicesUsingFrontLineWorker service, String circleCode) {
        Integer languageLocationCode = null;
        Integer defaultLanguageLocationCode = null;
        LanguageLocationCode langLocCode = null;
        Long stateCode = null;
        State state = null;

        userProfile.setDefaultLanguageLocationCode(frontLineWorker.getDefaultLanguageLocationCodeId());
        userProfile.setLanguageLocationCode(frontLineWorker.getLanguageLocationCodeId());
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
        return userProfile;
    }

}
