package org.motechproject.nms.mobileacademy.web;

import java.util.Arrays;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.motechproject.nms.mobileacademy.commons.CallDisconnectReason;
import org.motechproject.nms.mobileacademy.commons.CallStatus;
import org.motechproject.nms.mobileacademy.dto.CallDetailRequest;
import org.motechproject.nms.mobileacademy.dto.ContentLogRequest;
import org.motechproject.nms.mobileacademy.dto.LlcRequest;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * UserController handles following requests i.e Get User, Set language location
 * code, Save Call details.
 *
 */
@Controller
public class UserController extends BaseController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    private static final String REQUEST_PARAM_CALLING_NUMBER = "callingNumber";

    private static final String REQUEST_PARAM_OPERATOR = "operator";

    private static final String REQUEST_PARAM_CIRCLE = "circle";

    private static final String REQUEST_PARAM_CALL_ID = "callId";

    private static final String REQUEST_PARAM_LLC = "languageLocationCode";

    private static final String REQUEST_PARAM_CALL_START_TIME = "callStartTime";

    private static final String REQUEST_PARAM_CALL_END_TIME = "callEndTime";

    private static final String REQUEST_PARAM_CALL_DURATION_IN_PULSES = "callDurationInPulses";

    private static final String REQUEST_PARAM_END_USAGE_PROMPT_COUNTER = "endOfUsagePromptCounter";

    private static final String REQUEST_PARAM_CALL_STATUS = "callStatus";

    private static final String REQUEST_PARAM_CALL_DISCONNECT_REASON = "callDisconnectReason";

    private static final String REQUEST_PARAM_TYPE = "type";

    private static final String REQUEST_PARAM_CONTENT_NAME = "contentName";

    private static final String REQUEST_PARAM_CONTENT_FILE = "contentFile";

    private static final String REQUEST_PARAM_START_TIME = "startTime";

    private static final String REQUEST_PARAM_END_TIME = "endTime";

    private static final String REQUEST_PARAM_COMPLETION_FLAG = "completionFlag";

    private static final String REQUEST_PARAM_CORRECT_ANSWER_RECEIVED = "correctAnswerReceived";

    private static final String[] CALL_DATA_TYPE = { "lesson", "chapter",
            "question" };

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Get User Details API
     * 
     * @param callingNumber mobile number of the caller
     * @param operator operator of caller
     * @param circle Circle from where the call is originating.
     * @param callId unique call id assigned by IVR
     * @return User response object containing user details
     * @throws DataValidationException , NmsInternalServerError
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUserDetails(
            @RequestParam(value = REQUEST_PARAM_CALLING_NUMBER) String callingNumber,
            @RequestParam(value = REQUEST_PARAM_OPERATOR) String operator,
            @RequestParam(value = REQUEST_PARAM_CIRCLE) String circle,
            @RequestParam(value = REQUEST_PARAM_CALL_ID) String callId)
            throws DataValidationException, NmsInternalServerError {
        LOGGER.debug("getUserDetails: Started");
        LOGGER.debug("Input request-" + REQUEST_PARAM_CALLING_NUMBER + ":"
                + callingNumber + ", " + REQUEST_PARAM_OPERATOR + ":"
                + operator + ", " + REQUEST_PARAM_CIRCLE + ":" + circle + ", "
                + REQUEST_PARAM_CALL_ID + ":" + callId);
        validateInputDataForGetUserDetails(callingNumber, operator, circle,
                callId);
        User user = userDetailsService.findUserDetails(callingNumber, operator,
                circle, callId);
        LOGGER.debug("Output Response: " + user);
        LOGGER.debug("getUserDetails: Ended");
        return user;

    }

    /**
     * Set language location code API
     * 
     * @param llcRequest object contain input request
     * @throws DataValidationException
     * @throws MissingServletRequestParameterException
     */
    @RequestMapping(value = "/languageLocationCode", method = RequestMethod.POST)
    @ResponseBody
    public void setLanguageLocationCode(@RequestBody LlcRequest llcRequest)
            throws DataValidationException,
            MissingServletRequestParameterException {
        LOGGER.debug("setLanguageLocationCode: Started");
        LOGGER.debug("Input request: " + llcRequest);
        validateInputDataForSetLlc(llcRequest);
        userDetailsService.setLanguageLocationCode(
                llcRequest.getLanguageLocationCode(),
                llcRequest.getCallingNumber(), llcRequest.getCallId());
        LOGGER.debug("setLanguageLocationCode: Ended");

    }

    /**
     * Save Call Details API
     * 
     * @param callDetails object contain input request
     * @throws MissingServletRequestParameterException
     * @throws DataValidationException
     */
    @RequestMapping(value = "/callDetails", method = RequestMethod.POST)
    @ResponseBody
    public void saveCallDetails(@RequestBody CallDetailRequest callDetailRequest)
            throws MissingServletRequestParameterException,
            DataValidationException {
        LOGGER.debug("saveCallDetails: Started");
        LOGGER.debug("Input request: " + callDetailRequest);
        validateInputDataForSaveCallDetails(callDetailRequest);
        userDetailsService.saveCallDetails(callDetailRequest);
        LOGGER.debug("saveCallDetails: Ended");
    }

    /**
     * validate Input Data For Get User Details API
     * 
     * @param callingNumber
     * @param operator
     * @param circle
     * @param callId
     * @throws DataValidationException
     */
    private void validateInputDataForGetUserDetails(String callingNumber,
            String operator, String circle, String callId)
            throws DataValidationException {
        ParseDataHelper.validateAndParseString(REQUEST_PARAM_CALLING_NUMBER,
                callingNumber, true);
        ParseDataHelper.validateAndTrimMsisdn(REQUEST_PARAM_CALLING_NUMBER,
                callingNumber);
        ParseDataHelper.validateAndParseString(REQUEST_PARAM_OPERATOR,
                operator, true);
        ParseDataHelper.validateAndParseString(REQUEST_PARAM_CIRCLE, circle,
                true);
        ParseDataHelper.validateLengthOfCallId(REQUEST_PARAM_CALL_ID,
                ParseDataHelper.validateAndParseString(REQUEST_PARAM_CALL_ID,
                        callId, true));
    }

    /**
     * validate Input Data For Set Language location code
     * 
     * @param llcRequest object contain input request
     * @throws DataValidationException
     * @throws MissingServletRequestParameterException
     */
    private void validateInputDataForSetLlc(LlcRequest llcRequest)
            throws DataValidationException,
            MissingServletRequestParameterException {

        checkParameterMissing(REQUEST_PARAM_CALLING_NUMBER,
                llcRequest.getCallingNumber());
        checkParameterMissing(REQUEST_PARAM_CALL_ID, llcRequest.getCallId());
        checkParameterMissing(REQUEST_PARAM_LLC,
                llcRequest.getLanguageLocationCode());

        ParseDataHelper.validateAndParseString(REQUEST_PARAM_CALLING_NUMBER,
                llcRequest.getCallingNumber(), true);
        ParseDataHelper.validateAndTrimMsisdn(REQUEST_PARAM_CALLING_NUMBER,
                llcRequest.getCallingNumber());
        ParseDataHelper.validateLengthOfCallId(REQUEST_PARAM_CALL_ID,
                ParseDataHelper.validateAndParseString(REQUEST_PARAM_CALL_ID,
                        llcRequest.getCallId(), true));
        ParseDataHelper.validateAndParseInt(REQUEST_PARAM_LLC,
                llcRequest.getLanguageLocationCode(), true);
    }

    /**
     * validate Input Data For Save Call Details for missing and invalid values
     * 
     * @param callDetailRequest
     * @throws MissingServletRequestParameterException
     * @throws DataValidationException
     */
    private void validateInputDataForSaveCallDetails(
            CallDetailRequest callDetailRequest)
            throws MissingServletRequestParameterException,
            DataValidationException {
        // check whether parameter is missing or not
        checkParameterMissing(REQUEST_PARAM_CALLING_NUMBER,
                callDetailRequest.getCallingNumber());
        checkParameterMissing(REQUEST_PARAM_OPERATOR,
                callDetailRequest.getOperator());
        checkParameterMissing(REQUEST_PARAM_CIRCLE,
                callDetailRequest.getCircle());
        checkParameterMissing(REQUEST_PARAM_CALL_ID,
                callDetailRequest.getCallId());
        checkParameterMissing(REQUEST_PARAM_CALL_START_TIME,
                callDetailRequest.getCallStartTime());
        checkParameterMissing(REQUEST_PARAM_CALL_END_TIME,
                callDetailRequest.getCallEndTime());
        checkParameterMissing(REQUEST_PARAM_CALL_DURATION_IN_PULSES,
                callDetailRequest.getCallDurationInPulses());
        checkParameterMissing(REQUEST_PARAM_END_USAGE_PROMPT_COUNTER,
                callDetailRequest.getEndOfUsagePromptCounter());
        checkParameterMissing(REQUEST_PARAM_CALL_STATUS,
                callDetailRequest.getCallStatus());
        checkParameterMissing(REQUEST_PARAM_CALL_DISCONNECT_REASON,
                callDetailRequest.getCallDisconnectReason());

        // check whether parameter value is invalid or not
        ParseDataHelper.validateAndTrimMsisdn(REQUEST_PARAM_CALLING_NUMBER,
                ParseDataHelper.validateAndParseString(
                        REQUEST_PARAM_CALLING_NUMBER,
                        callDetailRequest.getCallingNumber(), true));
        ParseDataHelper.validateAndParseString(REQUEST_PARAM_OPERATOR,
                callDetailRequest.getOperator(), true);
        ParseDataHelper.validateAndParseString(REQUEST_PARAM_CIRCLE,
                callDetailRequest.getCircle(), true);
        ParseDataHelper.validateLengthOfCallId(REQUEST_PARAM_CALL_ID,
                ParseDataHelper.validateAndParseString(REQUEST_PARAM_CALL_ID,
                        callDetailRequest.getCallId(), true));
        ParseDataHelper.validateAndParseLong(REQUEST_PARAM_CALL_START_TIME,
                callDetailRequest.getCallStartTime(), true);
        ParseDataHelper.validateAndParseLong(REQUEST_PARAM_CALL_END_TIME,
                callDetailRequest.getCallEndTime(), true);
        ParseDataHelper.validateAndParseInt(
                REQUEST_PARAM_CALL_DURATION_IN_PULSES,
                callDetailRequest.getCallDurationInPulses(), true);
        ParseDataHelper.validateAndParseInt(
                REQUEST_PARAM_END_USAGE_PROMPT_COUNTER,
                callDetailRequest.getEndOfUsagePromptCounter(), true);
        ParseDataHelper.validateAndParseInt(REQUEST_PARAM_CALL_STATUS,
                callDetailRequest.getCallStatus(), true);
        if (CallStatus.findByValue(Integer.valueOf(callDetailRequest
                .getCallStatus())) == null) {
            ParseDataHelper.raiseInvalidDataException(
                    REQUEST_PARAM_CALL_STATUS,
                    callDetailRequest.getCallStatus());
        }
        ParseDataHelper.validateAndParseInt(
                REQUEST_PARAM_CALL_DISCONNECT_REASON,
                callDetailRequest.getCallDisconnectReason(), true);
        if (CallDisconnectReason.findByValue(Integer.valueOf(callDetailRequest
                .getCallDisconnectReason())) == null) {
            ParseDataHelper.raiseInvalidDataException(
                    REQUEST_PARAM_CALL_DISCONNECT_REASON,
                    callDetailRequest.getCallDisconnectReason());
        }
        if (CollectionUtils.isNotEmpty(callDetailRequest.getContent())) {
            for (ContentLogRequest contentLogRequest : callDetailRequest
                    .getContent()) {
                validateCallContentLogData(contentLogRequest);
            }
        }
    }

    /**
     * validate Call Content Log Data for missing and invalid values
     * 
     * @param contentLogRequest
     * @throws MissingServletRequestParameterException
     * @throws DataValidationException
     */
    private void validateCallContentLogData(ContentLogRequest contentLogRequest)
            throws MissingServletRequestParameterException,
            DataValidationException {
        // check whether parameter is missing or not
        checkParameterMissing(REQUEST_PARAM_TYPE, contentLogRequest.getType());
        checkParameterMissing(REQUEST_PARAM_CONTENT_NAME,
                contentLogRequest.getContentName());
        checkParameterMissing(REQUEST_PARAM_CONTENT_FILE,
                contentLogRequest.getContentFile());
        checkParameterMissing(REQUEST_PARAM_START_TIME,
                contentLogRequest.getStartTime());
        checkParameterMissing(REQUEST_PARAM_END_TIME,
                contentLogRequest.getEndTime());
        checkParameterMissing(REQUEST_PARAM_COMPLETION_FLAG,
                contentLogRequest.getCompletionFlag());

        // check whether parameter value is invalid or not
        ParseDataHelper.validateAndParseString(REQUEST_PARAM_TYPE,
                contentLogRequest.getType(), true);
        if (!Arrays.asList(CALL_DATA_TYPE).contains(
                contentLogRequest.getType().toLowerCase())) {
            ParseDataHelper.raiseInvalidDataException(REQUEST_PARAM_TYPE,
                    contentLogRequest.getType());
        }
        ParseDataHelper.validateAndParseString(REQUEST_PARAM_CONTENT_NAME,
                contentLogRequest.getContentName(), true);
        ParseDataHelper.validateAndParseString(REQUEST_PARAM_CONTENT_FILE,
                contentLogRequest.getContentFile(), true);
        ParseDataHelper.validateAndParseLong(REQUEST_PARAM_START_TIME,
                contentLogRequest.getStartTime(), true);
        ParseDataHelper.validateAndParseLong(REQUEST_PARAM_END_TIME,
                contentLogRequest.getEndTime(), true);
        ParseDataHelper.validateAndParseBoolean(REQUEST_PARAM_COMPLETION_FLAG,
                contentLogRequest.getCompletionFlag(), true);
        ParseDataHelper.validateAndParseBoolean(
                REQUEST_PARAM_CORRECT_ANSWER_RECEIVED,
                contentLogRequest.getCorrectAnswerReceived(), false);

    }
}
