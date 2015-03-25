package org.motechproject.nms.kilkari.dto.request;

import org.codehaus.jackson.annotate.JsonProperty;
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
        ParseDataHelper.validateAndTrimMsisdn("calledNumber",
                ParseDataHelper.validateAndParseString("calledNumber", calledNumber, true));
        ParseDataHelper.validateAndParseString("operator", operator, true);
        ParseDataHelper.validateAndParseString("circle", circle, true);
        ParseDataHelper.validateAndParseString("callId", callId, true);
        if (subscriptionId == null) {
            String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, "subscriptionId", subscriptionId);
            String errDesc = String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, "subscriptionId");
            throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, "subscriptionId");
        }
    }
}
