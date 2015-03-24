package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.dto.response.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.service.*;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ActiveUserService activeUserService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private CircleService circleService;

    /**
     * this method determine languageLocationCode using msisdn and circleCode
     * @param msisdn Phone number of the beneficiary
     * @param circleCode circle code of the beneficiary
     * @return Subscriber detail response object
     */
    @Override
    public SubscriberDetailApiResponse getSubscriberDetails(String msisdn, String circleCode, String operatorCode)
            throws DataValidationException{
        validateCircleAndOperator(circleCode, operatorCode);

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
                response.setDefaultLanguageLocationCode(configuration.getNationalLanguageLocationCode());
            }
        }
    }

    private void getLanguageLocationCodeForSubscriber(String circleCode, Subscriber subscriber, SubscriberDetailApiResponse response) {
        //if LanguageLocationCode for the subscriber record is present then set this is as LanguageLocationCode in response.
        if (subscriber.getLanguageLocationCode() != null) {
            response.setLanguageLocationCode(subscriber.getLanguageLocationCode());

        } else {
            if (subscriber.getState() != null && subscriber.getDistrict() != null) {
                //if llcCode is null then get it by state and district
                getLLCCodeByStateDistrict(subscriber.getState().getStateCode(), subscriber.getDistrict().getDistrictCode(), circleCode, response);

            } else {
                //if either state or district is null then get llcCode by circleCode.
                getLanguageLocationCodeByCircleCode(circleCode, response);
            }
            if (response.getLanguageLocationCode() != null) {
                subscriber.setLanguageLocationCode(response.getLanguageLocationCode());
            }
        }
    }

    private void getLLCCodeByStateDistrict(Long stateCode, Long districtCode, String circleCode, SubscriberDetailApiResponse response) {
        Integer llcCode = llcService.getLanguageLocationCodeByLocationCode(stateCode, districtCode);
        if (llcCode != null) {
            response.setLanguageLocationCode(llcCode);
        } else {
            //get llcCode by circleCode if llcCode by state and district is null
            getLanguageLocationCodeByCircleCode(circleCode, response);
        }
    }

    private void validateCircleAndOperator(String circleCode, String operatorCode) throws DataValidationException{
        //validate operatorCode if not NUll
        if (operatorCode != null) {
            Operator operator = operatorService.getRecordByCode(operatorCode);
            if (operator == null) {
                String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, "operatorCode", operatorCode);
                String errDesc = String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, "operatorCode");
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, "operatorCode");
            }
        }

        //validate circleCode if not NUll
        if (circleCode != null) {
            Circle circle = circleService.getRecordByCode(circleCode);
            if (circle == null) {
                String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, "circleCode", operatorCode);
                String errDesc = String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, "circleCode");
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, "circleCode");
            }
        }
    }
}
