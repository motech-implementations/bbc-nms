package org.motechproject.nms.kilkari.dto.request;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * This class represents the Subscription Api request object
 */
public class SubscriptionCreateApiRequest {
    String callingNumber;
    String operator;
    String circle;
    String callId;
    Integer languageLocationCode;
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
     * This method creates a subscriber object by subscrition api request
     * @return subscriber type object
     */
    public Subscriber toSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setMsisdn(this.callingNumber);
        subscriber.setDeactivationReason(DeactivationReason.NONE);

        subscriber.setLanguageLocationCode(this.languageLocationCode);
        if (this.subscriptionPack.equals(SubscriptionPack.PACK_48_WEEKS.toString())) {
            subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
            subscriber.setDob(new DateTime());
        } else {
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
        ParseDataHelper.validateAndTrimMsisdn("callingNumber",
                ParseDataHelper.validateAndParseString("callingNumber", callingNumber, true));
        ParseDataHelper.validateAndParseString("operator", operator, true);
        ParseDataHelper.validateAndParseString("circle", circle, true);
        ParseDataHelper.validateAndParseString("callId", callId, true);
        ParseDataHelper.validateAndParseString("subscriptionPack", subscriptionPack, true);
        if (languageLocationCode == null) {
            String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, "languageLocationCode", languageLocationCode);
            String errDesc = String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, "languageLocationCode");
            throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, "languageLocationCode");
        }
    }
}