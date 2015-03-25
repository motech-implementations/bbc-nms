package org.motechproject.nms.mobileacademy.web;

import org.apache.log4j.Logger;
import org.motechproject.nms.mobileacademy.dto.LlcRequest;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
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
     * @throws DataValidationException , Exception
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public User getUserDetails(
            @RequestParam(value = UserController.REQUEST_PARAM_CALLING_NUMBER) String callingNumber,
            @RequestParam(value = UserController.REQUEST_PARAM_OPERATOR) String operator,
            @RequestParam(value = UserController.REQUEST_PARAM_CIRCLE) String circle,
            @RequestParam(value = UserController.REQUEST_PARAM_CALL_ID) String callId)
            throws DataValidationException, Exception {
        LOGGER.debug("getUserDetails: Started");
        LOGGER.info("Input request-"
                + UserController.REQUEST_PARAM_CALLING_NUMBER + ":"
                + callingNumber + ", " + UserController.REQUEST_PARAM_OPERATOR
                + ":" + operator + ", " + UserController.REQUEST_PARAM_CIRCLE
                + ":" + circle + ", " + UserController.REQUEST_PARAM_CALL_ID
                + ":" + callId);
        validateInputDataForGetUserDetails(callingNumber, operator, circle,
                callId);
        User user = userDetailsService.findUserDetails(callingNumber, operator,
                circle);
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
    public void setLanguageLocationCode(@RequestBody LlcRequest llcRequest)
            throws DataValidationException,
            MissingServletRequestParameterException {
        LOGGER.debug("setLanguageLocationCode: Started");
        LOGGER.info("Input request-"
                + UserController.REQUEST_PARAM_CALLING_NUMBER + ":"
                + llcRequest.getCallingNumber() + ", "
                + UserController.REQUEST_PARAM_LLC + ":"
                + llcRequest.getLanguageLocationCode() + ", "
                + UserController.REQUEST_PARAM_CALL_ID + ":"
                + llcRequest.getCallId());
        validateInputDataForSetLlc(llcRequest);
        userDetailsService.setLanguageLocationCode(
                llcRequest.getLanguageLocationCode(),
                llcRequest.getCallingNumber());
        LOGGER.debug("setLanguageLocationCode: Ended");

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
        ParseDataHelper.validateAndParseString(
                UserController.REQUEST_PARAM_CALLING_NUMBER, callingNumber,
                true);
        ParseDataHelper.validateAndTrimMsisdn(
                UserController.REQUEST_PARAM_CALLING_NUMBER, callingNumber);
        ParseDataHelper.validateAndParseString(
                UserController.REQUEST_PARAM_OPERATOR, operator, true);
        ParseDataHelper.validateAndParseString(
                UserController.REQUEST_PARAM_CIRCLE, circle, true);
        ParseDataHelper.validateAndParseLong(
                UserController.REQUEST_PARAM_CALL_ID, callId, true);
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
        if (llcRequest.getCallingNumber() == null) {
            handleMissingJsonParamException(UserController.REQUEST_PARAM_CALLING_NUMBER);
        }
        if (llcRequest.getCallId() == null) {
            handleMissingJsonParamException(UserController.REQUEST_PARAM_CALL_ID);
        }
        if (llcRequest.getLanguageLocationCode() == null) {
            handleMissingJsonParamException(UserController.REQUEST_PARAM_LLC);
        }
        ParseDataHelper.validateAndParseString(
                UserController.REQUEST_PARAM_CALLING_NUMBER,
                llcRequest.getCallingNumber(), true);
        ParseDataHelper.validateAndTrimMsisdn(
                UserController.REQUEST_PARAM_CALLING_NUMBER,
                llcRequest.getCallingNumber());
        ParseDataHelper.validateAndParseLong(
                UserController.REQUEST_PARAM_CALL_ID, llcRequest.getCallId(),
                true);
        ParseDataHelper.validateAndParseInt(UserController.REQUEST_PARAM_LLC,
                llcRequest.getLanguageLocationCode(), true);
    }
}
