package org.motechproject.nms.mobilekunji.web;


import org.apache.commons.httpclient.HttpStatus;
import org.motechproject.nms.mobilekunji.dto.LanguageLocationCodeApiRequest;
import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiRequest;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * This class register the controller methods for creation of user and update its details.
 */
@Controller
public class CallerDataController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CallerDataController.class);

    private UserDetailsService userDetailsService;

    private SaveCallDetailsService saveCallDetailsService;

    @Autowired
    public CallerDataController(UserDetailsService userDetailsService, SaveCallDetailsService saveCallDetailsService) {
        this.saveCallDetailsService = saveCallDetailsService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Get User Details
     *
     * @param callingNumber mobile number of the caller
     * @param operator      operator of caller
     * @param circle        Circle from where the call is originating.
     * @param callId        unique call id assigned by IVR
     * @return User
     * @throws DataValidationException
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public
    @ResponseBody
    UserDetailApiResponse getUserDetails(
            @RequestParam(value = "callingNumber") String callingNumber,
            @RequestParam(value = "operator") String operator,
            @RequestParam(value = "circle") String circle,
            @RequestParam(value = "callId") String callId, final HttpServletRequest request)
            throws DataValidationException {

        long startTime = System.currentTimeMillis();

        logger.info("getUserDetails: Started");

        logger.debug("Input request-callingNumber: {}, operator:{}, circle: {}, callId: {} " + callingNumber, operator, circle, callId);

        validateCallId(callId);

        validateInputDataForGetUserDetails(operator, circle, callId);

        UserDetailApiResponse userDetailApiResponse = userDetailsService.getUserDetails(
                ParseDataHelper.validateAndTrimMsisdn(callingNumber, callingNumber), circle, operator, callId);

        logger.trace("getUserDetails:Ended");
        long endTime = System.currentTimeMillis();

        IvrInteractionLogger.logIvrInteraction(startTime, endTime, request.getRequestURI(), HttpStatus.SC_OK);

        return userDetailApiResponse;
    }

    /**
     * SaveCallDetails
     *
     * @param saveCallDetailApiRequest
     * @throws DataValidationException
     */
    @RequestMapping(value = "/callDetails", method = RequestMethod.POST)
    @ResponseBody
    public void saveCallDetails(@RequestBody SaveCallDetailApiRequest saveCallDetailApiRequest, final HttpServletRequest request) throws DataValidationException {

        logger.debug("SaveCallDetails: started");
        logger.debug("SaveCallDetails Request Parameters : {} ", saveCallDetailApiRequest.toString());
        long startTime = System.currentTimeMillis();

        validateCallId(saveCallDetailApiRequest.getCallId());

        saveCallDetailsService.saveCallDetails(saveCallDetailApiRequest);

        long endTime = System.currentTimeMillis();

        IvrInteractionLogger.logIvrInteraction(startTime, endTime, request.getRequestURI(), HttpStatus.SC_OK);
        logger.debug("Save CallDetails:Ended");
    }

    /**
     * setLanguageLocationCode
     *
     * @param languageLocationCodeApiRequest
     * @throws DataValidationException
     */
    @RequestMapping(value = "/languageLocationCode", method = RequestMethod.POST)
    public
    @ResponseBody
    void setLanguageLocationCode(@RequestBody LanguageLocationCodeApiRequest languageLocationCodeApiRequest, final HttpServletRequest request) throws DataValidationException {

        logger.debug("SetLanguageLocationCode: started");
        logger.debug("LanguageLocationCode Request Parameters : {} ", languageLocationCodeApiRequest.toString());

        long startTime = System.currentTimeMillis();

        validateCallId(languageLocationCodeApiRequest.getCallId());

        userDetailsService.setLanguageLocationCode(languageLocationCodeApiRequest);

        long endTime = System.currentTimeMillis();

        IvrInteractionLogger.logIvrInteraction(startTime, endTime, request.getRequestURI(), HttpStatus.SC_OK);

        logger.debug("Save CallDetails:Ended");
    }

    /**
     * validate Input Data For Get User Details API
     *
     * @param operator
     * @param circle
     * @param callId
     * @throws DataValidationException
     */
    private void validateInputDataForGetUserDetails(String operator, String circle, String callId)
            throws DataValidationException {
        ParseDataHelper.validateAndParseString(operator, operator, true);
        ParseDataHelper.validateAndParseString(circle, circle, true);
        ParseDataHelper.validateAndParseLong(callId, callId, true);
    }

    /**
     * validate 15 Digit CallId
     *
     * @param callId
     * @throws DataValidationException
     */
    private void validateCallId(String callId) throws DataValidationException {

        ParseDataHelper.validateLengthOfCallId("CallId", callId);
    }
}
