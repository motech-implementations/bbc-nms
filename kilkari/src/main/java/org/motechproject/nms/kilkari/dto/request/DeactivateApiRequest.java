package org.motechproject.nms.kilkari.dto.request;

import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

public class DeactivateApiRequest {

    private String calledNumber;
    private String operator;
    private String circle;
    private String callId;
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

    public void validateMandatoryParameter() throws DataValidationException{
        ParseDataHelper.validateAndTrimMsisdn("calledNumber",
                ParseDataHelper.validateAndParseString("calledNumber", calledNumber, true));
        ParseDataHelper.validateAndParseString("operator", operator, true);
        ParseDataHelper.validateAndParseString("circle", circle, true);
        ParseDataHelper.validateAndParseString("callId", callId, true);
        if (subscriptionId == null) {
            String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, "subscriptionId", subscriptionId);
            String errDesc = String.format(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, "subscriptionId");
            throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, "subscriptionId");
        }
    }
}
