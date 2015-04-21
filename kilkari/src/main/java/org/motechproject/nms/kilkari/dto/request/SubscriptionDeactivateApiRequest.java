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
    private Long subscriptionId;

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

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
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
        if (subscriptionId == null) {
            String errMessage = String.format(DataValidationException.MANDATORY_MISSING_MESSAGE, Constants.SUBSCRIPTION_ID, subscriptionId);
            String errDesc = String.format(ErrorDescriptionConstants.MISSING_API_PARAMETER_DESCRIPTION, Constants.SUBSCRIPTION_ID);
            throw new DataValidationException(errMessage, ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING, errDesc, Constants.SUBSCRIPTION_ID);
        }
    }
}
