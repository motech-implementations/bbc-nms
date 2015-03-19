package org.motechproject.nms.mobileacademy.web;

import org.apache.log4j.Logger;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @Autowired
    private UserDetailsService userDetailsService;

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
    public @ResponseBody User getUserDetails(
            @RequestParam(value = "callingNumber") String callingNumber,
            @RequestParam(value = "operator") String operator,
            @RequestParam(value = "circle") String circle,
            @RequestParam(value = "callId") String callId)
            throws DataValidationException {
        LOGGER.debug("MA Get User Detail Controller Start");
        LOGGER.debug("Input request-callingNumber:" + callingNumber
                + ", operator:" + operator + ", circle:" + circle + ", callId:"
                + callId);
        validateInputDataForGetUserDetails(callingNumber, operator, circle,
                callId);
        User user = userDetailsService.findUserDetails(callingNumber, operator,
                circle);
        LOGGER.debug("MA Get User Detail Controller End");
        return user;

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
