package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.dto.response.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.service.*;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement business logic for finding subscriber details and identify Language
 * location code for the subscriber.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private LanguageLocationCodeService llcService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private CommonValidatorService commonValidatorService;

    /**
     * this method determine languageLocationCode using msisdn and circleCode
     * @param msisdn Phone number of the beneficiary
     * @param circleCode circle code of the beneficiary
     * @return Subscriber detail response object
     */
    @Override
    public SubscriberDetailApiResponse getSubscriberDetails(String msisdn, String circleCode, String operatorCode)
            throws DataValidationException, NmsInternalServerError {
        commonValidatorService.validateCircle(circleCode);
        commonValidatorService.validateOperator(operatorCode);

        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();
        response.setCircle(circleCode);
        //get subscriber for given msisdn
        Subscriber subscriber = subscriberService.getSubscriberByMsisdn(msisdn);

        if (subscriber != null) {
            List<String> activePackNameList = null;
            //get list of subscription packs for the subscriber.
            List<SubscriptionPack> activePackList = subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn);
            if (activePackList != null) {
                activePackNameList = new ArrayList<String>();
                for (SubscriptionPack activePack : activePackList) {
                    activePackNameList.add(activePack.getValue());
                }
            }
            response.setSubscriptionPackList(activePackNameList);
            getLanguageLocationCodeForSubscriber(subscriber, response);
        }
        else {
            //if subscriber does not exist for msisdn then get llcCode by circle.
            getLanguageLocationCodeByCircleCode(circleCode, response);
        }
        return response;
    }

    private void getLanguageLocationCodeByCircleCode(String circleCode, SubscriberDetailApiResponse response) {
        Integer llcCode = llcService.getLanguageLocationCodeByCircleCode(circleCode);
        if (llcCode != null) {
            response.setLanguageLocationCode(llcCode);
        } else {
            Integer defaultLLCCode = llcService.getDefaultLanguageLocationCodeByCircleCode(circleCode);
            if (defaultLLCCode != null) {
                response.setDefaultLanguageLocationCode(defaultLLCCode);
            } else {
                //case when circle is unknown i,e 99
                Configuration configuration = configurationService.getConfiguration();
                response.setDefaultLanguageLocationCode(configuration.getNationalDefaultLanguageLocationCode());
            }
        }
    }

    private void getLanguageLocationCodeForSubscriber(
            Subscriber subscriber, SubscriberDetailApiResponse response) throws NmsInternalServerError {
        //if LanguageLocationCode for the subscriber record is present then set this is as LanguageLocationCode in response.
        if (subscriber.getLanguageLocationCode() != null) {
            response.setLanguageLocationCode(subscriber.getLanguageLocationCode());

        } else {
            if (subscriber.getState() != null && subscriber.getDistrict() != null) {
                //if llcCode is null then get it by state and district
                getLLCCodeByStateDistrict(subscriber.getState().getStateCode(),
                        subscriber.getDistrict().getDistrictCode(), response);
                subscriber.setLanguageLocationCode(response.getLanguageLocationCode());
            }
        }
    }

    private void getLLCCodeByStateDistrict(
            Long stateCode, Long districtCode, SubscriberDetailApiResponse response) throws NmsInternalServerError {
        Integer llcCode = llcService.getLanguageLocationCodeByLocationCode(stateCode, districtCode);
        if (llcCode != null) {
            response.setLanguageLocationCode(llcCode);
        } else {
            String errMessage = "languageLocationCode could not be determined for stateCode : "
                    + stateCode +" and districtCode " + districtCode;
            throw new NmsInternalServerError(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);

        }
    }

}
