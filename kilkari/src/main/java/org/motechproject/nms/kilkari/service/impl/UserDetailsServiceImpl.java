package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.dto.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.service.UserDetailsService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implement business logic for finding subscriber details and identify Language
 * location code for the subscriber.
 */
@Service("registrationServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private LanguageLocationCodeService llcService;

    /**
     * this method determine languageLocationCode using msisdn and circleCode
     * @param msisdn Phone number of the beneficiary
     * @param circleCode circle code of the beneficiary
     * @return Subscriber detail response object
     */
    @Override
    public SubscriberDetailApiResponse getSubscriberDetails(String msisdn, String circleCode) {
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();
        response.setCircle(circleCode);
        //get subscriber for given msisdn
        Subscriber subscriber = subscriberService.getSubscriberByMsisdn(msisdn);

        if (subscriber != null) {
            //get list of subscription packs for the subscriber.
            List<SubscriptionPack> activePackList = subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn);
            response.setSubscriptionPackList(activePackList);

            getLanguageLocationCodeForSubscriber(circleCode, subscriber, response);
        }
        else {
            //if subscriber does not exist for msisdn then get llcCode by circle.
            getLanguageLocationCodeByCircleCode(circleCode, response);
        }
        return response;
    }

    private void getLanguageLocationCodeByCircleCode(String circleCode, SubscriberDetailApiResponse response) {
        //Todo : validate circle by circleCode
        Integer llcCode = llcService.getLanguageLocationCodeByCircleCode(circleCode);
        if (llcCode != null) {
            response.setLanguageLocationCode(llcCode.toString());
        } else {
            Integer defaultLLCCode = llcService.getDefaultLanguageLocationCodeByCircleCode(circleCode);
            if (defaultLLCCode != null) {
                response.setDefaultLanguageLocationCode(defaultLLCCode.toString());
            } else {
                //todo : set national default llcCode
                response.setDefaultLanguageLocationCode("");
            }
        }
    }

    private void getLanguageLocationCodeForSubscriber(String circleCode, Subscriber subscriber, SubscriberDetailApiResponse response) {
        //if LanguageLocationCode for the subscriber record is present then set this is as LanguageLocationCode in response.
        if (subscriber.getLanguageLocationCode() != null) {
            response.setLanguageLocationCode(subscriber.getLanguageLocationCode().toString());

        } else {
            if (subscriber.getState() != null && subscriber.getDistrict() != null) {
                //if llcCode is null then get it by state and district
                getLLCCodeByStateDistrict(subscriber.getState().getStateCode(), subscriber.getDistrict().getDistrictCode(), circleCode, response);

            } else {
                //if either state or district is null then get llcCode by circleCode.
                getLanguageLocationCodeByCircleCode(circleCode, response);
            }
        }
    }

    private void getLLCCodeByStateDistrict(Long stateCode, Long districtCode, String circleCode, SubscriberDetailApiResponse response) {
        Integer llcCode = llcService.getLanguageLocationCodeByLocationCode(stateCode, districtCode);
        if (llcCode != null) {
            response.setLanguageLocationCode(llcCode.toString());
        } else {
            //get llcCode by circleCode if llcCode by state and district is null
            getLanguageLocationCodeByCircleCode(circleCode, response);
        }
    }
}
