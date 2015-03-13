package org.motechproject.nms.mobilekunji.web;


import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionCall;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String getUserDetails(@RequestParam String msisdn,
                                 @RequestParam String operator,
                                 @RequestParam String circle,
                                 @RequestParam String callid
                                , BindingResult errors) throws DataValidationException{
        if (errors.hasErrors()) {
            return null;
        }
        Long msIsdn = ParseDataHelper.parseLong("",msisdn,true);
        String operatorCode = ParseDataHelper.parseString("operatorCode",operator,true);
        String circleCode = ParseDataHelper.parseString("circleCode",circle,true);
        Long callId = ParseDataHelper.parseLong("callId", callid, true);

        
        UserDetailApiResponse response = userDetailsService.getUserDetails(msisdn, circle,operator,callId);
        return response.toString();

    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public void saveCallDetails(HttpServletRequest request){

        ServiceConsumptionCall consumptionCall = getServiceConsumptionCall();

        saveCallDetailsService.saveCallDetails();
    }

    private ServiceConsumptionCall getServiceConsumptionCall() {

        ServiceConsumptionCall consumptionCall = new ServiceConsumptionCall();
        consumptionCall.setCircle("");
        consumptionCall.setCallId(0L);
        consumptionCall.setNmsFlwId(0L);
        consumptionCall.setCallStartTime(null);
        consumptionCall.setCallEndTime(null);

        return consumptionCall;
    }

}
