package org.motechproject.nms.kilkari.web;

import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.dto.FailureResponse;
import org.motechproject.nms.kilkari.dto.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.dto.SubscriptionApiRequest;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkari.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
     * @param request Http request object from the client
     * @param errors Binding error object
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.POST)
    @ResponseBody
    public String createSubscription(HttpServletRequest request, BindingResult errors,
                                     SubscriptionApiRequest apiRequest) {
        if (errors.hasErrors()) {
            return null;
        }
        try {
            subscriptionService.createNewSubscriberAndSubscription(buildSubscriber(apiRequest.getMsisdn()), Channel.IVR, null);
        } catch (DataValidationException ex) {
            return null;
        }
        return null;
    }

    /**
     * Maps request for deactivate subscription controller
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.DELETE)
    public String deactivateSubscription() {

        return null;
    }

    /**
     * Maps request for subscriber detail controller
     * @return Json object for subscriber detail.
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public String getSubscriberDetails(@RequestParam String callingNumber, @RequestParam String operator,
                                       @RequestParam String circle, @RequestParam String callId) throws DataValidationException{
        logger.info("*****getSubscriberDetails is invoked******");
        SubscriberDetailApiResponse response;
            validateSubscriberDetailsRequestParams(callingNumber, operator, circle, callId);
            response = userDetailsService.getSubscriberDetails(callingNumber, circle);

        //TODO :validate msisdn, operator, circle, callId are not null.
        //TODO :SAVE operatorCode info in subscription table
        
        logger.info("Finished processing getUserDetails");
        return response.toString();

    }

    private Subscriber buildSubscriber(String msisdn) {
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(msisdn);
        subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        return subscriber;
    }

    private void validateSubscriberDetailsRequestParams(
            String msisdn, String operator, String circle, String callId) throws DataValidationException{
        ParseDataHelper.validateAndParseString("callingNumber", msisdn, true);
        ParseDataHelper.validateAndParseString("operator", operator, true);
        ParseDataHelper.validateAndParseString("circle", circle, true);
    }
}
