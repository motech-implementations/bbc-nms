package org.motechproject.nms.mobilekunji.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.nms.mobilekunji.domain.CardContent;

import java.util.List;

/**
 * Created by abhishek on 23/3/15.
 */
public class SaveCallDetailApiRequest {

    @JsonProperty
    private String callingNumber;

    @JsonProperty
    private String callId;

    @JsonProperty
    private String operator;

    @JsonProperty
    private String circle;

    @JsonProperty
    private Integer startTime;

    @JsonProperty
    private Integer endTime;

    @JsonProperty
    private Integer currentUsageInPulses;

    @JsonProperty
    private Integer endOfUsagePromptCounter;

    @JsonProperty
    private Boolean welcomeMessageFlag;

    @JsonProperty
    private String failureReason;

    @JsonProperty
    private List<CardContent> cardContentList;

    public List<CardContent> getCardContentList() {
        return cardContentList;
    }

    public void setCardContentList(List<CardContent> cardContentList) {
        this.cardContentList = cardContentList;
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

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getCurrentUsageInPulses() {
        return currentUsageInPulses;
    }

    public void setCurrentUsageInPulses(Integer currentUsageInPulses) {
        this.currentUsageInPulses = currentUsageInPulses;
    }

    public Integer getEndOfUsagePromptCounter() {
        return endOfUsagePromptCounter;
    }

    public void setEndOfUsagePromptCounter(Integer endOfUsagePromptCounter) {
        this.endOfUsagePromptCounter = endOfUsagePromptCounter;
    }

    public Boolean getWelcomeMessageFlag() {
        return welcomeMessageFlag;
    }

    public void setWelcomeMessageFlag(Boolean welcomeMessageFlag) {
        this.welcomeMessageFlag = welcomeMessageFlag;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "SaveCallDetailApiRequest{" +
                "callingNumber='" + callingNumber + '\'' +
                ", callId='" + callId + '\'' +
                ", operator='" + operator + '\'' +
                ", circle='" + circle + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", currentUsageInPulses=" + currentUsageInPulses +
                ", endOfUsagePromptCounter=" + endOfUsagePromptCounter +
                ", welcomeMessageFlag=" + welcomeMessageFlag +
                ", failureReason='" + failureReason + '\'' +
                ", cardContentList=" + cardContentList +
                '}';
    }
}


