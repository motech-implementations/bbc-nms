package org.motechproject.nms.mobilekunji.web;


import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * This class register the controller methods for creation of user and update its details.
 */
@Controller
public class CallerDataController {


    private static Logger log = LoggerFactory.getLogger(CallerDataController.class);

    private UserDetailsService userDetailsService;
    private SaveCallDetailsService saveCallDetailsService;

    @Autowired
    public CallerDataController(UserDetailsService jobAidService, SaveCallDetailsService saveCallDetailsService) {
        this.saveCallDetailsService = saveCallDetailsService;
        this.userDetailsService = userDetailsService;
    }


    /**
     * Maps request for caller data detail controller
     * @param request Http request object from the client
     * @param errors Binding error object
     * @return Json object for user detail.
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public String getUserDetails(HttpServletRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            return null;
        }
        String msisdn = request.getParameter("callingNumber");
        String operator = request.getParameter("operator");
        String circle = request.getParameter("circle");
        String callId = request.getParameter("callId");

        //TODO :validate msisdn, operator, circle, callId are not null.
        //TODO :SAVE operatorCode info in subscription table
        
        UserDetailApiResponse response = userDetailsService.getUserDetails(msisdn, circle,operator,callId);
        return response.toString();

    }


}
