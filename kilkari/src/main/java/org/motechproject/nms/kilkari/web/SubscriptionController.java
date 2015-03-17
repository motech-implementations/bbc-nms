package org.motechproject.nms.kilkari.web;


import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.dto.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.dto.SubscriptionApiRequest;
import org.motechproject.nms.kilkari.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * This class register the controller methods for create subscription, deactivate subscription
 * and get subscriber details
 */
@Controller
public class SubscriptionController {

    @Autowired
    private UserDetailsService registrationService;

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

        return null;
    }

    /**
     * Maps request for deactivate subscription controller
     * @param request Http request object from the client
     * @param errors Binding error object
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.DELETE)
    public String deactivateSubscription(HttpServletRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            return null;
        }
        return null;
    }

    /**
     * Maps request for subscriber detail controller
     * @param request Http request object from the client
     * @param errors Binding error object
     * @return Json object for subscriber detail.
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public String getSubscriberDetails(HttpServletRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            return null;
        }
        String msisdn = request.getParameter("callingNumber");
        String operator = request.getParameter("operator");
        String circle = request.getParameter("circle");
        String callId = request.getParameter("callId");

        //TODO :validate msisdn, operator, circle, callId are not null.
        //TODO :SAVE operatorCode info in subscription table
        
        SubscriberDetailApiResponse response = registrationService.getSubscriberDetails(msisdn, circle);
        return response.toString();

    }

    private Subscriber buildSubscriber(String msisdn) {
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(msisdn);
        subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        return subscriber;
    }

    private Subscription buildSubscription(SubscriptionApiRequest apiRequest) {
        String msisdn = apiRequest.getMsisdn();
        Subscription subscription = new Subscription();
        subscription.setMsisdn(msisdn);
        subscription.setSubscriber(buildSubscriber(msisdn));
        return subscription;
    }
}
