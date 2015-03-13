package org.motechproject.nms.kilkari.web;

import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.dto.request.DeactivateApiRequest;
import org.motechproject.nms.kilkari.dto.response.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.dto.request.SubscriptionApiRequest;
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
public class SubscriptionController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SubscriptionService subscriptionService;

    Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    /**
     * Maps request subscription controller
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.POST)
    @ResponseBody
    public String createSubscription(@RequestBody SubscriptionApiRequest apiRequest) throws DataValidationException{
        logger.info("*****createSubscription is invoked******");
        logger.debug("***************Deactivate Subscription Request Parameter*****************");
        logger.debug("callingNumber : [" + apiRequest.getCallingNumber() + "]");
        logger.debug("operator : [" + apiRequest.getOperator() + "]");
        logger.debug("circle : [" + apiRequest.getCircle() + "]");
        logger.debug("callId : [" + apiRequest.getCallId() + "]");
        logger.debug("languageLocationCode : [" + apiRequest.getLanguageLocationCode().toString() + "]");
        logger.debug("subscriptionPack : [" + apiRequest.getSubscriptionPack() + "]");

        apiRequest.validateMandatoryParameters();
        subscriptionService.createNewSubscriberAndSubscription(apiRequest.toSubscriber(), Channel.IVR, apiRequest.getOperator());
        logger.trace("Finished processing createSubscription");
        return null;
    }

    /**
     * Maps request for deactivate subscription controller
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.DELETE)
    public String deactivateSubscription(@RequestBody DeactivateApiRequest apiRequest) throws DataValidationException{
        logger.info("*****deactivateSubscription is invoked******");
        logger.debug("***************Deactivate Subscription Request Parameter*****************");
        logger.debug("calledNumber : [" + apiRequest.getCalledNumber() + "]");
        logger.debug("operator : [" + apiRequest.getOperator() + "]");
        logger.debug("circle : [" + apiRequest.getCircle() + "]");
        logger.debug("callId : [" + apiRequest.getCallId() + "]");
        logger.debug("subscriptionId : [" + apiRequest.getSubscriptionId().toString() + "]");

        apiRequest.validateMandatoryParameter();
        subscriptionService.deactivateSubscription(apiRequest.getSubscriptionId());
        logger.trace("Finished processing deactivateSubscription");
        return null;
    }

    /**
     * Maps request for subscriber detail controller
     * @return Json object for subscriber detail.
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public SubscriberDetailApiResponse getSubscriberDetails(@RequestParam String callingNumber, @RequestParam String operator,
                                       @RequestParam String circle, @RequestParam String callId) throws DataValidationException{
        logger.info("*****getSubscriberDetails is invoked******");
        logger.debug("***************Deactivate Subscription Request Parameter*****************");
        logger.debug("callingNumber : [" + callingNumber + "]");
        logger.debug("operator : [" + operator + "]");
        logger.debug("circle : [" + circle + "]");
        logger.debug("callId : [" + callId + "]");
        SubscriberDetailApiResponse response;
            validateSubscriberDetailsRequestParams(callingNumber, operator, circle, callId);
            response = userDetailsService.getSubscriberDetails(callingNumber, circle);

        logger.trace("Finished processing getUserDetails");
        return response;
    }

    public void validateSubscriberDetailsRequestParams(
            String msisdn, String operator, String circle, String callId) throws DataValidationException {
        ParseDataHelper.validateAndTrimMsisdn("callingNumber",
                ParseDataHelper.validateAndParseString("callingNumber", msisdn, true));
        ParseDataHelper.validateAndParseString("operator", operator, true);
        ParseDataHelper.validateAndParseString("circle", circle, true);
        ParseDataHelper.validateAndParseString("callId", callId, true);
    }
}
