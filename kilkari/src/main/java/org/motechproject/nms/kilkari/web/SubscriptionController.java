package org.motechproject.nms.kilkari.web;

import org.motechproject.nms.kilkari.dto.request.SubscriptionCreateApiRequest;
import org.motechproject.nms.kilkari.dto.request.SubscriptionDeactivateApiRequest;
import org.motechproject.nms.kilkari.dto.response.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkari.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * This class register the controller methods for create subscription, deactivate subscription
 * and get subscriber details
 */
@Controller
public class SubscriptionController extends BaseController{

    Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    private UserDetailsService userDetailsService;

    private SubscriptionService subscriptionService;

    @Autowired
    public  SubscriptionController(UserDetailsService userDetailsService, SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Maps request subscription controller
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.POST)
    @ResponseBody
    public void createSubscription(@RequestBody SubscriptionCreateApiRequest apiRequest) throws DataValidationException{
        logger.info("*****createSubscription is invoked******");
        logger.debug("***************Deactivate Subscription Request Parameter*****************");
        logger.debug("callingNumber : [" + apiRequest.getCallingNumber() + "]");
        logger.debug("operator : [" + apiRequest.getOperator() + "]");
        logger.debug("circle : [" + apiRequest.getCircle() + "]");
        logger.debug("callId : [" + apiRequest.getCallId() + "]");
        logger.debug(String.format("languageLocationCode : [%d]", apiRequest.getLanguageLocationCode()));
        logger.debug("subscriptionPack : [" + apiRequest.getSubscriptionPack() + "]");
        apiRequest.validateMandatoryParameters();
        subscriptionService.handleIVRSubscriptionRequest(apiRequest.toSubscriber(), apiRequest.getOperator(), apiRequest.getCircle(), apiRequest.getLanguageLocationCode());
        logger.trace("Finished processing createSubscription");
    }

    /**
     * Maps request for deactivate subscription controller
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.DELETE)
    public void deactivateSubscription(@RequestBody SubscriptionDeactivateApiRequest apiRequest) throws DataValidationException{
        logger.info("*****deactivateSubscription is invoked******");
        logger.debug("***************Deactivate Subscription Request Parameter*****************");
        logger.debug("calledNumber : [" + apiRequest.getCalledNumber() + "]");
        logger.debug("operator : [" + apiRequest.getOperator() + "]");
        logger.debug("circle : [" + apiRequest.getCircle() + "]");
        logger.debug("callId : [" + apiRequest.getCallId() + "]");
        logger.debug("subscriptionId : [" + apiRequest.getSubscriptionId().toString() + "]");
        apiRequest.validateMandatoryParameter();
        subscriptionService.deactivateSubscription(apiRequest.getSubscriptionId(),
                apiRequest.getOperator(), apiRequest.getCircle());
        logger.trace("Finished processing deactivateSubscription");
    }

    /**
     * Maps request for subscriber detail controller
     * @return Json object for subscriber detail.
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public SubscriberDetailApiResponse getSubscriberDetails(@RequestParam String callingNumber, @RequestParam String operator,
                                       @RequestParam String circle, @RequestParam String callId) throws DataValidationException, Exception{
        logger.info("*****getSubscriberDetails is invoked******");
        logger.debug("***************Deactivate Subscription Request Parameter*****************");
        logger.debug("callingNumber : [" + callingNumber + "]");
        logger.debug("operator : [" + operator + "]");
        logger.debug("circle : [" + circle + "]");
        logger.debug("callId : [" + callId + "]");
        SubscriberDetailApiResponse response;
            validateSubscriberDetailsRequestParams(callingNumber, operator, circle, callId);
            response = userDetailsService.getSubscriberDetails(callingNumber, circle, operator);
        logger.trace("Finished processing getUserDetails");
        return response;
    }

    /**
     * This method validates SubscriberDetailRequest parameters
     * @param msisdn String type object
     * @param operator String type object
     * @param circle String type object
     * @param callId String type object
     * @throws DataValidationException
     */
    public void validateSubscriberDetailsRequestParams(
            String msisdn, String operator, String circle, String callId) throws DataValidationException {
        ParseDataHelper.validateAndTrimMsisdn("callingNumber",
                ParseDataHelper.validateAndParseString("callingNumber", msisdn, true));
        ParseDataHelper.validateAndParseString("operator", operator, true);
        ParseDataHelper.validateAndParseString("circle", circle, true);
        ParseDataHelper.validateAndParseString("callId", callId, true);
    }
}
