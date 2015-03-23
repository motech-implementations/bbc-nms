package org.motechproject.nms.mobilekunji.web;


import org.motechproject.nms.mobilekunji.domain.CardContent;
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
            @RequestParam(value = "callId") String callId)
            throws DataValidationException {
        log.info("getUserDetails: Started");
        log.debug("Input request-callingNumber: {}, operator:{}, circle: {}, callId: {} " + callingNumber, operator, circle, callId);

        validateInputDataForGetUserDetails(callingNumber, operator, circle,
                callId);
        UserDetailApiResponse userDetailApiResponse = userDetailsService.getUserDetails(callingNumber, circle, operator,
                Long.parseLong(callId));
        log.trace("getUserDetails:Ended");
        return userDetailApiResponse;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public void saveCallDetails(@RequestBody SaveCallDetailApiRequest request) throws DataValidationException {

        log.info("SaveCallDetails: Started");

        validateInputDataForSaveCallDetails(request);

        saveCallDetailsService.saveCallDetails();

        log.trace("Save CallDetails:Ended");
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public @ResponseBody void updateLanguageLocationCode(@RequestBody LanguageLocationCodeApiRequest request) throws DataValidationException {

        log.info("Update Language Location Code: Started");

        userDetailsService.updateLanguageLocationCode(request.getCallingNumber(), request.getLanguageLocationCode());

        log.trace("Update LanguageLocationCode:Ended");
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
        ParseDataHelper.validateAndParseLong(callId, callId, true);
    }

    private void validateInputDataForSaveCallDetails(SaveCallDetailApiRequest request)
            throws DataValidationException {

        ParseDataHelper.validateAndParseInt("CallingNumber", request.getCallingNumber(), true);
        ParseDataHelper.validateAndParseString("Operator", request.getCircle(), true);
        ParseDataHelper.validateAndParseString("Circle", request.getCircle(), true);
        ParseDataHelper.validateAndParseInt("CallStartTime", request.getStartTime().toString(), true);
        ParseDataHelper.validateAndParseInt("CallEndTime",request.getEndTime().toString() , true);
        ParseDataHelper.validateAndParseInt("CurrentUsageInPulses",request.getCurrentUsageInPulses().toString(),true);
        ParseDataHelper.validateAndParseInt("",request.getEndOfUsagePromptCounter().toString(),true);
        ParseDataHelper.validateAndParseBoolean("WelcomeMessageFlag",request.getWelcomeMessageFlag().toString(),true);

        CardContent cardContent = request.getCardContentList().get(0);

        ParseDataHelper.validateAndParseInt("CardNumber", cardContent.getCardNumber().toString(), true);
        ParseDataHelper.validateAndParseString("ContentName",cardContent.getContentName(),true);
        ParseDataHelper.validateAndParseString("AudioFileName",cardContent.getAudioFileName(),true);
        ParseDataHelper.validateAndParseInt("StartTime",cardContent.getStartTime().toString(),true);
        ParseDataHelper.validateAndParseInt("EndTime",cardContent.getEndTime().toString(),true);
    }
}
