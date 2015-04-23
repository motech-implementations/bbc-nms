package org.motechproject.nms.mobileacademy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.exception.FlwNotInWhiteListException;
import org.motechproject.nms.frontlineworker.exception.ServiceNotDeployedException;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.mobileacademy.commons.CappingType;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.CallDetail;
import org.motechproject.nms.mobileacademy.domain.Configuration;
import org.motechproject.nms.mobileacademy.domain.ContentLog;
import org.motechproject.nms.mobileacademy.domain.FlwUsageDetail;
import org.motechproject.nms.mobileacademy.dto.CallDetailsRequest;
import org.motechproject.nms.mobileacademy.dto.ContentLogRequest;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.repository.CallDetailDataService;
import org.motechproject.nms.mobileacademy.service.CallDetailService;
import org.motechproject.nms.mobileacademy.service.ConfigurationService;
import org.motechproject.nms.mobileacademy.service.FlwUsageDetailService;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * UserDetailsServiceImpl class contains implementation of UserDetailsService
 *
 */
@Service("UserDetailsServiceMa")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserProfileDetailsService userProfileDetailsService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FlwUsageDetailService flwUsageDetailService;

    @Autowired
    private CallDetailService callDetailService;

    @Autowired
    private FrontLineWorkerService frontLineWorkerService;

    @Autowired
    private CallDetailDataService callDetailDataService;

    private static final Logger LOGGER = Logger
            .getLogger(UserDetailsServiceImpl.class);

    private static final String COURSE_START_TIME_KEY = "courseStartTime";

    private static final String COURSE_END_TIME_KEY = "courseEndTime";

    private static final String REQUEST_PARAM_CALLING_NUMBER = "callingNumber";

    @Override
    public User findUserDetails(String callingNumber, String operator,
            String circle, String callId) throws DataValidationException,
            NmsInternalServerError, ServiceNotDeployedException,
            FlwNotInWhiteListException {
        // TODO service deployed and white list validation
        User user = new User();// user detail response DTO
        String msisdn = ParseDataHelper.validateAndTrimMsisdn(
                REQUEST_PARAM_CALLING_NUMBER, callingNumber);
        // Get User Information using FLW Service
        UserProfile userProfile = userProfileDetailsService.processUserDetails(
                msisdn, circle, operator,
                ServicesUsingFrontLineWorker.MOBILEACADEMY);
        LOGGER.trace("User Information Returned From FLW: " + userProfile);

        Configuration configuration = configurationService.getConfiguration();
        FlwUsageDetail flwUsageDetail = findFlwUsageInfo(userProfile
                .getNmsFlwId());
        determineLanguageLocationCodeForUser(userProfile, configuration, user);
        Integer maxAllowedUsageInPulses = findMaxAllowedUsageInPulses(
                userProfile, configuration);

        user.setCircle(userProfile.getCircle());
        user.setMaxAllowedEndOfUsagePrompt(configuration
                .getMaxAllowedEndOfUsagePrompt());
        user.setMaxAllowedUsageInPulses(maxAllowedUsageInPulses);
        user.setCurrentUsageInPulses(flwUsageDetail.getCurrentUsageInPulses());
        user.setEndOfUsagePromptCounter(flwUsageDetail
                .getEndOfUsagePromptCounter());
        return user;
    }

    /**
     * find mobile academy Usage information for Front line worker.
     * 
     * @param flwId flwId return from userProfileDetailsService of flw module
     * @return FlwUsageDetail
     */
    private FlwUsageDetail findFlwUsageInfo(Long flwId) {
        FlwUsageDetail flwUsageDetailReturn = new FlwUsageDetail();
        FlwUsageDetail flwUsageDetail = flwUsageDetailService
                .findByFlwId(flwId);
        if (flwUsageDetail == null) {
            flwUsageDetailReturn
                    .setCurrentUsageInPulses(MobileAcademyConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
            flwUsageDetailReturn
                    .setEndOfUsagePromptCounter(MobileAcademyConstants.DEFAULT_END_OF_USAGE_PROMPT_COUNTER);
        } else {
            // Compare FLW last access month with current month (year
            // also considered). If both not
            // equals then reset current usage and end of usage prompt count to
            // default value.
            DateTime lastAccessTime = flwUsageDetail.getLastAccessTime();
            String lastAccessMonth = new java.text.SimpleDateFormat("MMMYYYY")
                    .format(new Date(lastAccessTime.getMillis()));
            String currentMonth = new java.text.SimpleDateFormat("MMMYYYY")
                    .format(new Date(System.currentTimeMillis()));
            if (!lastAccessMonth.equals(currentMonth)) {
                flwUsageDetail
                        .setCurrentUsageInPulses(MobileAcademyConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
                flwUsageDetail
                        .setEndOfUsagePromptCounter(MobileAcademyConstants.DEFAULT_END_OF_USAGE_PROMPT_COUNTER);
                flwUsageDetail = flwUsageDetailService
                        .updateFlwUsageRecord(flwUsageDetail);
            }
            flwUsageDetailReturn.setCurrentUsageInPulses(flwUsageDetail
                    .getCurrentUsageInPulses());
            flwUsageDetailReturn.setEndOfUsagePromptCounter(flwUsageDetail
                    .getEndOfUsagePromptCounter());
        }
        return flwUsageDetailReturn;

    }

    /**
     * Determine Max Allowed Usage In Pulses as per capping type in
     * configuration.
     * 
     * @param userProfile UserProfile object
     * @param configuration Configuration object
     * @return Integer MaxAllowedUsageInPulses
     */
    private Integer findMaxAllowedUsageInPulses(UserProfile userProfile,
            Configuration configuration) {
        Integer maxAllowedUsageInPulses = null;
        if (CappingType.NO_CAPPING.getValue().equals(
                configuration.getCappingType())) {
            maxAllowedUsageInPulses = MobileAcademyConstants.MAX_ALLOWED_USAGE_PULSE_FOR_UNCAPPED;
        } else if (CappingType.NATIONAL_CAPPING.getValue().equals(
                configuration.getCappingType())) {
            maxAllowedUsageInPulses = configuration.getNationalCapValue();
        } else if (CappingType.STATE_CAPPING.getValue().equals(
                configuration.getCappingType())) {
            maxAllowedUsageInPulses = userProfile
                    .getMaxStateLevelCappingValue();
        }
        // maxAllowedUsageInPulses to no capped i.e -1 in case no value found
        if (maxAllowedUsageInPulses == null) {
            maxAllowedUsageInPulses = MobileAcademyConstants.MAX_ALLOWED_USAGE_PULSE_FOR_UNCAPPED;
        }
        return maxAllowedUsageInPulses;

    }

    /**
     * determine Language Location Code for User using
     * isDefaultLanguageLocationCode field returned by FLW. If it is true then
     * set defaultLanguageLocationCode or false then set languageLocationCode
     * using field languageLocationCode. If it is not found then set national
     * default using configuration.
     * 
     * @param userProfile UserProfile object
     * @param configuration Configuration object
     * @param user User object for updating LLC or default LLC
     */
    private void determineLanguageLocationCodeForUser(UserProfile userProfile,
            Configuration configuration, User user) {
        boolean nationalDefaultLlc = false;
        if (userProfile.isDefaultLanguageLocationCode()) {
            if (userProfile.getLanguageLocationCode() != null) {
                user.setDefaultLanguageLocationCode(userProfile
                        .getLanguageLocationCode());
            } else {
                nationalDefaultLlc = true;
            }
        } else {
            if (userProfile.getLanguageLocationCode() != null) {
                user.setLanguageLocationCode(userProfile
                        .getLanguageLocationCode());
            } else {
                nationalDefaultLlc = true;
            }
        }
        if (nationalDefaultLlc) {
            // set national default language location code
            user.setDefaultLanguageLocationCode(configuration
                    .getDefaultLanguageLocationCode());
        }
    }

    @Override
    public void setLanguageLocationCode(String languageLocationCode,
            String callingNumber, String callId)
            throws DataValidationException, ServiceNotDeployedException,
            FlwNotInWhiteListException {
        // TODO service deployed and white list validation
        String msisdn = ParseDataHelper.validateAndTrimMsisdn(
                REQUEST_PARAM_CALLING_NUMBER, callingNumber);
        userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(
                Integer.parseInt(languageLocationCode), msisdn);
        LOGGER.debug("Llc updated  for callingNumber:" + callingNumber
                + ", callId:" + callId + ", llc:" + languageLocationCode);

    }

    @Override
    public void saveCallDetails(CallDetailsRequest callDetailsRequest)
            throws DataValidationException {
        String msisdn = ParseDataHelper.validateAndTrimMsisdn(
                REQUEST_PARAM_CALLING_NUMBER,
                callDetailsRequest.getCallingNumber());
        FrontLineWorker frontLineWorker = frontLineWorkerService
                .getFlwBycontactNo(msisdn);
        if (frontLineWorker == null) {
            ParseDataHelper.raiseInvalidDataException(
                    REQUEST_PARAM_CALLING_NUMBER,
                    callDetailsRequest.getCallingNumber());
        } else {
            callDetailDataService
                    .doInTransaction(new TransactionCallback<CallDetail>() {

                        CallDetailsRequest callDetailsRequest;

                        FrontLineWorker frontLineWorker;

                        private TransactionCallback<CallDetail> init(
                                CallDetailsRequest callDetailsRequest,
                                FrontLineWorker frontLineWorker) {
                            this.callDetailsRequest = callDetailsRequest;
                            this.frontLineWorker = frontLineWorker;
                            return this;
                        }

                        @Override
                        public CallDetail doInTransaction(
                                TransactionStatus status) {
                            CallDetail transactionObject = null;
                            LOGGER.trace("Data from FLW- Id:"
                                    + frontLineWorker.getId()
                                    + ", circle:"
                                    + frontLineWorker.getCircleCode()
                                    + ", opeartor:"
                                    + frontLineWorker.getOperatorCode()
                                    + ", llc:"
                                    + frontLineWorker
                                            .getLanguageLocationCodeId()
                                    + ", contactNo:"
                                    + frontLineWorker.getContactNo());
                            FlwUsageDetail flwUsageDetail = flwUsageDetailService
                                    .findByFlwId(frontLineWorker.getId());
                            Map<String, DateTime> courseStartEndTime = findCourseStartAndEndTime(callDetailsRequest
                                    .getContent());
                            if (flwUsageDetail == null) {
                                // add new FLW usage record
                                flwUsageDetail = addFlwUsageRecord(
                                        courseStartEndTime, callDetailsRequest,
                                        frontLineWorker);
                            } else {
                                // update existing FLW usage record
                                flwUsageDetail = updateFlwUsageRecord(
                                        flwUsageDetail, courseStartEndTime,
                                        callDetailsRequest);
                            }
                            saveCallDetailRecord(callDetailsRequest,
                                    frontLineWorker, flwUsageDetail);
                            return transactionObject;
                        }

                    }.init(callDetailsRequest, frontLineWorker));
        }

    }

    /**
     * find Course Start And End Time using call content log records data
     * 
     * @param contentLogRequests
     * @return Map<String, DateTime> having keys:courseStartTime and
     *         courseEndTime
     */
    private Map<String, DateTime> findCourseStartAndEndTime(
            List<ContentLogRequest> contentLogRequests) {
        Map<String, DateTime> courseStartEndTime = new HashMap<String, DateTime>();
        courseStartEndTime.put(COURSE_START_TIME_KEY, null);
        courseStartEndTime.put(COURSE_END_TIME_KEY, null);
        List<Long> courseStartTimeValues = new ArrayList<Long>();
        List<Long> courseEndTimeValues = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(contentLogRequests)) {
            for (ContentLogRequest contentLogRequest : contentLogRequests) {
                if (("lesson".equalsIgnoreCase(contentLogRequest.getType()))
                        && (MobileAcademyConstants.COURSE_START_CONTENT
                                .equalsIgnoreCase(contentLogRequest
                                        .getContentName()))) {
                    courseStartTimeValues.add(Long.valueOf(contentLogRequest
                            .getStartTime()));
                } else if (("question".equalsIgnoreCase(contentLogRequest
                        .getType()))
                        && (MobileAcademyConstants.COURSE_END_CONTENT
                                .equalsIgnoreCase(contentLogRequest
                                        .getContentName()))
                        && (Boolean.parseBoolean(contentLogRequest
                                .getCompletionFlag()))) {
                    courseEndTimeValues.add(Long.valueOf(contentLogRequest
                            .getEndTime()));

                }
            }
        }
        // if more than one record present select first for start date
        if (!courseStartTimeValues.isEmpty()) {
            Collections.sort(courseStartTimeValues);
            DateTime startTime = new DateTime(
                    Long.valueOf(courseStartTimeValues.get(0))
                            * MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER);
            courseStartEndTime.put(COURSE_START_TIME_KEY, startTime);
        }
        // if more than one record present select last for end date
        if (!courseEndTimeValues.isEmpty()) {
            Collections.sort(courseEndTimeValues);
            DateTime endTime = new DateTime(Long.valueOf(courseEndTimeValues
                    .get(courseEndTimeValues.size() - 1))
                    * MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER);
            courseStartEndTime.put(COURSE_END_TIME_KEY, endTime);
        }
        return courseStartEndTime;
    }

    /**
     * add FLW Usage Record
     * 
     * @param courseStartEndTime
     * @param callDetailRequest
     * @param frontLineWorker
     * @return FlwUsageDetail
     */
    private FlwUsageDetail addFlwUsageRecord(
            Map<String, DateTime> courseStartEndTime,
            CallDetailsRequest callDetailsRequest,
            FrontLineWorker frontLineWorker) {
        FlwUsageDetail flwUsageDetail = new FlwUsageDetail();
        flwUsageDetail.setFlwId(frontLineWorker.getId());
        flwUsageDetail.setMsisdn(Long.valueOf(frontLineWorker.getContactNo()));
        flwUsageDetail.setCurrentUsageInPulses(Integer
                .valueOf(callDetailsRequest.getCallDurationInPulses()));
        flwUsageDetail.setEndOfUsagePromptCounter(Integer
                .valueOf(callDetailsRequest.getEndOfUsagePromptCounter()));
        flwUsageDetail.setCourseStartDate(courseStartEndTime
                .get(COURSE_START_TIME_KEY));
        flwUsageDetail.setCourseEndDate(courseStartEndTime
                .get(COURSE_END_TIME_KEY));
        flwUsageDetail.setLastAccessTime(new DateTime(Long
                .valueOf(callDetailsRequest.getCallStartTime())
                * MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER));
        return flwUsageDetailService.createFlwUsageRecord(flwUsageDetail);
    }

    /**
     * update FLW Usage Record
     * 
     * @param flwUsageDetail
     * @param courseStartEndTime
     * @param callDetailsRequest
     * @return FlwUsageDetail
     */
    private FlwUsageDetail updateFlwUsageRecord(FlwUsageDetail flwUsageDetail,
            Map<String, DateTime> courseStartEndTime,
            CallDetailsRequest callDetailsRequest) {
        Integer currentUsageInPulses = Integer.valueOf(callDetailsRequest
                .getCallDurationInPulses())
                + flwUsageDetail.getCurrentUsageInPulses();
        flwUsageDetail.setCurrentUsageInPulses(currentUsageInPulses);
        flwUsageDetail.setEndOfUsagePromptCounter(Integer
                .valueOf(callDetailsRequest.getEndOfUsagePromptCounter()));
        if (flwUsageDetail.getCourseStartDate() != null
                && flwUsageDetail.getCourseEndDate() == null) {
            flwUsageDetail.setCourseEndDate(courseStartEndTime
                    .get(COURSE_END_TIME_KEY));
        } else {
            flwUsageDetail.setCourseStartDate(courseStartEndTime
                    .get(COURSE_START_TIME_KEY));
            flwUsageDetail.setCourseEndDate(courseStartEndTime
                    .get(COURSE_END_TIME_KEY));
        }
        flwUsageDetail.setLastAccessTime(new DateTime(Long
                .valueOf(callDetailsRequest.getCallStartTime())
                * MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER));
        return flwUsageDetailService.updateFlwUsageRecord(flwUsageDetail);
    }

    /**
     * save Call Detail and Content Log Records
     * 
     * @param callDetailsRequest
     * @param frontLineWorker
     * @param flwUsageDetail
     */
    private void saveCallDetailRecord(CallDetailsRequest callDetailsRequest,
            FrontLineWorker frontLineWorker, FlwUsageDetail flwUsageDetail) {
        CallDetail callDetail = new CallDetail();
        callDetail.setCallId(Long.valueOf(callDetailsRequest.getCallId()));
        callDetail.setFlwId(frontLineWorker.getId());
        callDetail.setMsisdn(Long.valueOf(frontLineWorker.getContactNo()));
        callDetail.setCircle(callDetailsRequest.getCircle());
        callDetail.setOperator(callDetailsRequest.getOperator());
        callDetail.setCallStartTime(new DateTime(Long
                .valueOf(callDetailsRequest.getCallStartTime())
                * MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER));
        callDetail.setCallEndTime(new DateTime(Long.valueOf(callDetailsRequest
                .getCallEndTime())
                * MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER));
        callDetail.setCallStatus(Integer.valueOf(callDetailsRequest
                .getCallStatus()));
        callDetail.setCallDisconnectReason(Integer.valueOf(callDetailsRequest
                .getCallDisconnectReason()));
        callDetailService.saveCallDetailRecord(callDetail);

        if (CollectionUtils.isNotEmpty(callDetailsRequest.getContent())) {
            for (ContentLogRequest contentLogRequest : callDetailsRequest
                    .getContent()) {
                ContentLog contentLog = new ContentLog();
                contentLog.setCallId(Long.valueOf(callDetailsRequest
                        .getCallId()));
                contentLog.setLanguageLocationCode(frontLineWorker
                        .getLanguageLocationCodeId());
                contentLog.setType(contentLogRequest.getType().toUpperCase());
                contentLog.setContentName(contentLogRequest.getContentName()
                        .toUpperCase());
                contentLog.setContentFile(contentLogRequest.getContentFile());
                contentLog.setStartTime(new DateTime(Long
                        .valueOf(contentLogRequest.getStartTime())
                        * MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER));
                contentLog.setEndTime(new DateTime(Long
                        .valueOf(contentLogRequest.getEndTime())
                        * MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER));
                contentLog.setCompletionFlag(Boolean
                        .parseBoolean(contentLogRequest.getCompletionFlag()));
                if (StringUtils.isNotBlank(contentLogRequest
                        .getCorrectAnswerReceived())) {
                    contentLog.setCorrectAnswerReceived(Boolean
                            .parseBoolean(contentLogRequest
                                    .getCorrectAnswerReceived()));
                }
                contentLog.setCourseStartDate(flwUsageDetail
                        .getCourseStartDate());
                // save course end date only for chapter-11question-04 record
                if (("question".equalsIgnoreCase(contentLogRequest.getType()))
                        && (MobileAcademyConstants.COURSE_END_CONTENT
                                .equalsIgnoreCase(contentLogRequest
                                        .getContentName()))
                        && (Boolean.parseBoolean(contentLogRequest
                                .getCompletionFlag()))) {
                    contentLog.setCourseEndDate(flwUsageDetail
                            .getCourseEndDate());
                }
                callDetailService.saveContentLogRecord(contentLog);
            }
        }
    }

    @Override
    public boolean doesMsisdnExist(String callingNo) {
        return frontLineWorkerService.getFlwBycontactNo(callingNo) != null;
    }
}