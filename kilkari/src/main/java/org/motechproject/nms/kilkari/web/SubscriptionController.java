package org.motechproject.nms.kilkari.web;

import java.util.List;

import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.dto.request.SubscriptionCreateApiRequest;
import org.motechproject.nms.kilkari.dto.request.SubscriptionDeactivateApiRequest;
import org.motechproject.nms.kilkari.dto.response.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkari.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
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
public class SubscriptionController extends BaseController {

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
    public void createSubscription(@RequestBody SubscriptionCreateApiRequest apiRequest)
            throws DataValidationException, NmsInternalServerError {
        logger.debug("createSubscription: started");
        logger.debug("createSubscription Request Parameters");
        logger.debug("callingNumber : [" + apiRequest.getCallingNumber() + "]");
        logger.debug("operator : [" + apiRequest.getOperator() + "]");
        logger.debug("circle : [" + apiRequest.getCircle() + "]");
        logger.debug("callId : [" + apiRequest.getCallId() + "]");
        logger.debug(String.format("languageLocationCode : [%s]", apiRequest.getLanguageLocationCode()));
        logger.debug("subscriptionPack : [" + apiRequest.getSubscriptionPack() + "]");
        apiRequest.validateMandatoryParameters();
        subscriptionService.handleIVRSubscriptionRequest(apiRequest.toSubscriber(), apiRequest.getOperator(), apiRequest.getCircle(), apiRequest.getLanguageLocationCode());
        logger.trace("createSubscription: End");
    }

    /**
     * Maps request for deactivate subscription controller
     * @return
     */
    @RequestMapping(value = "/subscription", method = RequestMethod.DELETE)
    @ResponseBody
    public void deactivateSubscription(@RequestBody SubscriptionDeactivateApiRequest apiRequest) throws DataValidationException{
        logger.debug("deactivateSubscription: started");
        logger.debug("DeactivateSubscription Request Parameters");
        logger.debug("calledNumber : [" + apiRequest.getCalledNumber() + "]");
        logger.debug("operator : [" + apiRequest.getOperator() + "]");
        logger.debug("circle : [" + apiRequest.getCircle() + "]");
        logger.debug("callId : [" + apiRequest.getCallId() + "]");
        logger.debug("subscriptionId : [" + apiRequest.getSubscriptionId() + "]");
        apiRequest.validateMandatoryParameter();
        subscriptionService.deactivateSubscription(apiRequest.getSubscriptionIdLongValue(),
                apiRequest.getOperator(), apiRequest.getCircle());
        logger.trace("deactivateSubscription: End");
    }

    /**
     * Maps request for subscriber detail controller
     * @return Json object for subscriber detail.
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public SubscriberDetailApiResponse getSubscriberDetails(@RequestParam String callingNumber, @RequestParam String operator,
                                       @RequestParam String circle, @RequestParam String callId)
            throws DataValidationException, NmsInternalServerError {
        logger.debug("getSubscriberDetails: started");
        logger.debug("getSubscriberDetails Request Parameters");
        logger.debug("callingNumber : [" + callingNumber + "]");
        logger.debug("operator : [" + operator + "]");
        logger.debug("circle : [" + circle + "]");
        logger.debug("callId : [" + callId + "]");
        SubscriberDetailApiResponse response;
        validateSubscriberDetailsRequestParams(callingNumber, operator, circle, callId);
        response = userDetailsService.getSubscriberDetails(ParseDataHelper.validateAndTrimMsisdn(Constants.CALLING_NUMBER,callingNumber), circle, operator);
        logger.trace("getUserDetails: End");
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
    public void validateSubscriberDetailsRequestParams(String msisdn, String operator, String circle,
                                                       String callId) throws DataValidationException {
        ParseDataHelper.validateString(Constants.CALLING_NUMBER, msisdn, true);
        ParseDataHelper.validateString(Constants.OPERATOR_CODE, operator, true);
        ParseDataHelper.validateString(Constants.CIRCLE_CODE, circle, true);
        ParseDataHelper.validateLengthOfCallId(Constants.CALL_ID, ParseDataHelper.validateAndParseString(Constants.CALL_ID, callId, true));
    }
    
    @RequestMapping(value = "/scheduledSubscription", method = RequestMethod.GET)
    @ResponseBody
    public void scheduledSubscription() throws DataValidationException{
        logger.info("scheduledSubscription: started");
        List<Subscription> subscriptions = subscriptionService.getScheduledSubscriptions();
        logger.info("scheduledSubscription: End ListSize[{}]", subscriptions.size());
    }
    
    @RequestMapping(value = "/purgeOldSubscription", method = RequestMethod.DELETE)
    @ResponseBody
    public void purgeOldSubscription() throws DataValidationException{
        logger.info("purgeOldSubscription: started");
        subscriptionService.purgeOldSubscriptionSubscriberRecords();
        logger.info("purgeOldSubscription: End ListSize[{}]");
    }
}
