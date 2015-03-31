package org.motechproject.nms.kilkari.dto.request;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * This class represents the Subscription Api request object
 */
public class SubscriptionCreateApiRequest {

    @JsonProperty
    private String callingNumber;

    @JsonProperty
    private String operator;

    @JsonProperty
    private String circle;

    @JsonProperty
    private String callId;

    @JsonProperty
    private Integer languageLocationCode;

    @JsonProperty
    private String subscriptionPack;

    public void setSubscriptionPack(String subscriptionPack) {
        this.subscriptionPack = subscriptionPack;
    }

    public String getCallingNumber() {
        return callingNumber;
    }

    public void setCallingNumber(String callingNumber) {
        this.callingNumber = callingNumber;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getSubscriptionPack() {
        return subscriptionPack;
    }

    /**
     * This method creates a subscriber object by subscription api request
     * @return subscriber type object
     */
    public Subscriber toSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(this.callingNumber);
        subscriber.setDeactivationReason(DeactivationReason.NONE);

        subscriber.setLanguageLocationCode(this.languageLocationCode);
        if (this.subscriptionPack.equals(SubscriptionPack.PACK_48_WEEKS.getValue())) {
            subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
            subscriber.setDob(new DateTime());
        }else if (this.subscriptionPack.equals(SubscriptionPack.PACK_72_WEEKS.getValue())) {
            subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
            subscriber.setLmp(new DateTime().minusMonths(3));
        }
        return subscriber;
    }

    /**
     * Validates mandatory value parameter and non null values
     * @throws DataValidationException if parameter value is blank or null
     */
    public void validateMandatoryParameters() throws DataValidationException{
        callingNumber = ParseDataHelper.validateAndTrimMsisdn(Constants.CALLING_NUMBER,
                ParseDataHelper.validateAndParseString(Constants.CALLING_NUMBER, callingNumber, true));
        ParseDataHelper.validateAndParseString(Constants.OPERATOR_CODE, operator, true);
        ParseDataHelper.validateAndParseString(Constants.CIRCLE_CODE, circle, true);
        ParseDataHelper.validateLengthOfCallId(Constants.CALL_ID,
                ParseDataHelper.validateAndParseString(Constants.CALL_ID, callId, true));

        if(SubscriptionPack.findByName(subscriptionPack) == null) {
            ParseDataHelper.raiseInvalidDataException(Constants.SUBSCRIPTION_PACK, subscriptionPack);
        }
        if (languageLocationCode == null) {
            ParseDataHelper.raiseInvalidDataException(Constants.LANGUAGE_LOCATION_CODE, null);
        }
    }
}
