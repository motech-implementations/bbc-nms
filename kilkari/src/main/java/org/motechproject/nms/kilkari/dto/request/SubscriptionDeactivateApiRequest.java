package org.motechproject.nms.kilkari.dto.request;

import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * Represents deactivateApiRequest object
 */
public class SubscriptionDeactivateApiRequest {

    @JsonProperty
    private String calledNumber;

    @JsonProperty
    private String operator;

    @JsonProperty
    private String circle;

    @JsonProperty
    private String callId;

    @JsonProperty
    private String subscriptionId;

    private Long subscriptionIdLongValue;

    public String getCalledNumber() {
        return calledNumber;
    }

    public void setCalledNumber(String calledNumber) {
        this.calledNumber = calledNumber;
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

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getSubscriptionIdLongValue() {
        return subscriptionIdLongValue;
    }

    public void setSubscriptionIdLongValue(Long subscriptionIdLongValue) {
        this.subscriptionIdLongValue = subscriptionIdLongValue;
    }

    /**
     * Validates mandatory value parameter and non null values
     * @throws DataValidationException if parameter value is blank or null
     */
    public void validateMandatoryParameter() throws DataValidationException{
        calledNumber = ParseDataHelper.validateAndTrimMsisdn(Constants.CALLED_NUMBER,
                ParseDataHelper.validateAndParseString(Constants.CALLED_NUMBER, calledNumber, true));
        ParseDataHelper.validateAndParseString(Constants.OPERATOR_CODE, operator, true);
        ParseDataHelper.validateAndParseString(Constants.CIRCLE_CODE, circle, true);
        ParseDataHelper.validateAndParseString(Constants.CALL_ID, callId, true);
        subscriptionIdLongValue = ParseDataHelper.validateAndParseLong(Constants.SUBSCRIPTION_ID, subscriptionId, true);
    }
}
