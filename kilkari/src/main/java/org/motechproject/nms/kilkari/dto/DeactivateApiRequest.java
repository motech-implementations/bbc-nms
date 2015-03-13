package org.motechproject.nms.kilkari.dto;

import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

public class DeactivateApiRequest {

    private String calledNumber;
    private String operator;
    private String circle;
    private String callId;
    private String subscriptionId;

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

    public void validateMandatoryParameter() throws DataValidationException{
        ParseDataHelper.validateAndParseString("calledNumber", calledNumber, true);
        ParseDataHelper.validateAndParseString("operator", operator, true);
        ParseDataHelper.validateAndParseString("circle", circle, true);
        ParseDataHelper.validateAndParseString("callId", callId, true);
        ParseDataHelper.validateAndParseLong("subscriptionId", subscriptionId, true);
    }
}
