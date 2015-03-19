package org.motechproject.nms.kilkari.dto;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * This class represents the Subscription Api request object
 */
public class SubscriptionApiRequest {
    String callingNumber;
    String operator;
    String circle;
    String callId;
    String languageLocationCode;
    String subscriptionPack;

    public void setSubscriptionPack(String subscriptionPack) {
        this.subscriptionPack = subscriptionPack;
    }

    public String getCallingNumber() {
        return callingNumber;
    }

    public void setCallingNumber(String callingNumber) {
        this.callingNumber = callingNumber;
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
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(this.callingNumber);

        subscriber.setLanguageLocationCode(Integer.parseInt(this.languageLocationCode));
        if (this.subscriptionPack.equals(SubscriptionPack.PACK_48_WEEKS.toString())) {
            subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
            subscriber.setDob(new DateTime().plusDays(1));
        } else {
            subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
            subscriber.setLmp(new DateTime().minusMonths(3));
        }
        return subscriber;
    }

    public void validateMandatoryParameters() throws DataValidationException{
        ParseDataHelper.validateAndParseString("callingNumber", callingNumber, true);
        ParseDataHelper.validateAndParseString("operator", operator, true);
        ParseDataHelper.validateAndParseString("circle", circle, true);
        ParseDataHelper.validateAndParseString("callId", callId, true);
        ParseDataHelper.validateAndParseString("languageLocationCode", languageLocationCode, true);
        ParseDataHelper.validateAndParseString("subscriptionPack", subscriptionPack, true);
    }
}
