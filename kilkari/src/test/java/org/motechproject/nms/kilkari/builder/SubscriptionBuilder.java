package org.motechproject.nms.kilkari.builder;

import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.dto.request.SubscriptionCreateApiRequest;
import org.motechproject.nms.kilkari.dto.request.SubscriptionDeactivateApiRequest;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;

public class SubscriptionBuilder {
    Subscriber subscriber = new Subscriber();

    public Subscriber buildSubscriber(String msisdn, Integer llcCode, State state, District district, BeneficiaryType type) {
        subscriber.setName("motechUser");
        subscriber.setMsisdn(msisdn);
        subscriber.setBeneficiaryType(type);
        subscriber.setLanguageLocationCode(llcCode);
        subscriber.setDistrict(district);
        subscriber.setState(state);
        if (state != null) {
            subscriber.setStateCode(state.getStateCode());
        }
        return subscriber;
    }

    public Subscription buildSubscription(String msisdn, Channel channel, Status status) {
        Subscription subscription = new Subscription();
        subscription.setMsisdn(msisdn);
        subscription.setChannel(channel);
        subscription.setStatus(status);
        return subscription;
    }

    public SubscriptionCreateApiRequest buildSubscriptionApiRequest(String callingNumber,String operator,String circle,String callId,Integer languageLocationCode,String subscriptionPack) {
        SubscriptionCreateApiRequest apiRequest = new SubscriptionCreateApiRequest();
        apiRequest.setCallingNumber(callingNumber);
        apiRequest.setOperator(operator);
        apiRequest.setCircle(circle);
        apiRequest.setCallId(callId);
        apiRequest.setLanguageLocationCode(languageLocationCode);
        apiRequest.setSubscriptionPack(subscriptionPack);
        return apiRequest;
    }

    public SubscriptionDeactivateApiRequest buildSubscriptionDeactivateApiRequest(String msisdn, String operator,
                                                                                  String circle, String subscriptionId, String callId) {
        SubscriptionDeactivateApiRequest request = new SubscriptionDeactivateApiRequest();
        request.setCalledNumber(msisdn);
        request.setCircle(circle);
        request.setOperator(operator);
        request.setSubscriptionId(subscriptionId);
        request.setCallId(callId);
        return request;
    }

    public Subscriber buildSubscriber(String msisdn, Integer llcCode, BeneficiaryType type) {
        subscriber.setName("motechUser");
        subscriber.setMsisdn(msisdn);
        subscriber.setBeneficiaryType(type);
        subscriber.setLanguageLocationCode(llcCode);
        return subscriber;
    }

}
