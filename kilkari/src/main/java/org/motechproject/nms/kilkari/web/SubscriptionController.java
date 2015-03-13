package org.motechproject.nms.kilkari.web;


import org.motechproject.nms.kilkari.domain.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.service.RegistrationService;
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
    private RegistrationService registrationService;

    @RequestMapping(value = "/subscription", method = RequestMethod.POST)
    public String createSubscription(HttpServletRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            return null;
        }
        return null;
    }

    @RequestMapping(value = "/subscription", method = RequestMethod.DELETE)
    public String deactivateSubscription(HttpServletRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            return null;
        }
        return null;
    }

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
        
        SubscriberDetailApiResponse response = registrationService.getSubscriberDetails(msisdn, circle);
        return response.toString();

    }
}
