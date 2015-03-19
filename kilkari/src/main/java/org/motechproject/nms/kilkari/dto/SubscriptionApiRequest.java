package org.motechproject.nms.kilkari.dto;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;

/**
 * This class represents the Subscription Api request object
 */
public class SubscriptionApiRequest {
    String msisdn;
    String operator;
    String circle;
    String callId;
    String languageLocationCode;
    String subscriptionPack;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getSubscriptionPack() {
        return subscriptionPack;
    }

    public Subscriber toSubscriber() {
        DateTime dt = new DateTime();
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(this.msisdn);

        subscriber.setLanguageLocationCode(Integer.parseInt(this.languageLocationCode));
        if (this.subscriptionPack.equals(SubscriptionPack.PACK_48_WEEKS.toString())) {
            subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        } else {
            subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        }
        return subscriber;
    }


}
