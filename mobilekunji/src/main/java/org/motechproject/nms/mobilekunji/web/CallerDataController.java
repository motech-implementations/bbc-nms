package org.motechproject.nms.mobilekunji.web;


import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * This class register the controller methods for creation of user and update its details.
 */
@Controller
public class CallerDataController extends BaseController {


    private static Logger log = LoggerFactory.getLogger(CallerDataController.class);

    private UserDetailsService userDetailsService;
    private SaveCallDetailsService saveCallDetailsService;

    @Autowired
    public CallerDataController(UserDetailsService jobAidService, SaveCallDetailsService saveCallDetailsService) {
        this.saveCallDetailsService = saveCallDetailsService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Get User Details
     *
     * @param callingNumber mobile number of the caller
     * @param operator operator of caller
     * @param circle Circle from where the call is originating.
     * @param callId unique call id assigned by IVR
     * @return User
     * @throws DataValidationException
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public @ResponseBody UserDetailApiResponse getUserDetails(
            @RequestParam(value = "callingNumber") String callingNumber,
            @RequestParam(value = "operator") String operator,
            @RequestParam(value = "circle") String circle,
            @RequestParam(value = "callId") String callId)
            throws DataValidationException {
        log.debug("getUserDetails: Started");
        log.debug("Input request-callingNumber:" + callingNumber
                + ", operator:" + operator + ", circle:" + circle + ", callId:"
                + callId);
        validateInputDataForGetUserDetails(callingNumber, operator, circle,
                callId);
        UserDetailApiResponse userDetailApiResponse = userDetailsService.getUserDetails(callingNumber, circle, operator,
                callId);
        log.debug("getUserDetails:Ended");
        return userDetailApiResponse;

    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public void saveCallDetails(HttpServletRequest request) {
        saveCallDetailsService.saveCallDetails();
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
        ParseDataHelper.validateAndParseInt(callingNumber, callingNumber, true);
        ParseDataHelper.validateAndParseString(operator, operator, true);
        ParseDataHelper.validateAndParseString(circle, circle, true);
        ParseDataHelper.validateAndParseInt(callId, callId, true);
    }
}
