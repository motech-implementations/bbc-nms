package org.motechproject.nms.mobileacademy.web;

import org.apache.log4j.Logger;
import org.motechproject.nms.mobileacademy.dto.CallDetails;
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
     */
    // This is just a placeholder for save call details API.
    // actual implementation would be done in sprint 1504
    @RequestMapping(value = "/callDetails", method = RequestMethod.POST)
    @ResponseBody
    public void saveCallDetails(@RequestBody CallDetails callDetails)
            throws MissingServletRequestParameterException {
        LOGGER.debug("saveCallDetails: Started");
        LOGGER.debug("Input request: " + callDetails);
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
}
