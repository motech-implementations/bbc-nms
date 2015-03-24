package org.motechproject.nms.kilkari.builder;

import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.dto.request.SubscriptionCreateApiRequest;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;

public class SubscriptionBuilder {
    Subscriber subscriber = new Subscriber();

    public Subscriber buildSubscriber(String msisdn, Integer llcCode, State state, District district) {
        subscriber.setName("motechUser");
        subscriber.setMsisdn(msisdn);
        subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        subscriber.setLanguageLocationCode(llcCode);
        subscriber.setState(state);
        subscriber.setDistrict(district);
        return subscriber;
    }

    public Subscription buildSubscription(String msisdn) {
        Subscription subscription = new Subscription();
        subscription.setMsisdn(msisdn);
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

    //public SubscriptionDeactivateApiRequest buildSubscriptionDeactivateApiRequest()

}
