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
        apiRequest.validateMandatoryParameters();
        subscriptionService.createNewSubscriberAndSubscription(apiRequest.toSubscriber(), Channel.IVR, apiRequest.getOperator());
        return null;
    }

    /**
     * Maps request for deactivate subscription controller
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.DELETE)
    public String deactivateSubscription(@RequestBody DeactivateApiRequest apiRequest) throws DataValidationException{
        apiRequest.validateMandatoryParameter();
        subscriptionService.deactivateSubscription(apiRequest.getSubscriptionId());
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
