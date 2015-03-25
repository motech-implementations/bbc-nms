package org.motechproject.nms.mobilekunji.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.nms.mobilekunji.domain.CardDetail;

import java.util.List;

/**
 * API request for save call details
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
    private Long callStartTime;

    @JsonProperty
    private Long callEndTime;

    @JsonProperty
    private Integer callDurationInPulses;

    @JsonProperty
    private Integer endOfUsagePromptCounter;

    @JsonProperty
    private Integer callStatus;

    @JsonProperty
    private Boolean welcomeMessagePromptFlag;

    @JsonProperty
    private String callDisconnectReason;

    @JsonProperty
    private List<CardDetail> content;

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

    public Long getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Long callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Long getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(Long callEndTime) {
        this.callEndTime = callEndTime;
    }

    public Integer getCallDurationInPulses() {
        return callDurationInPulses;
    }

    public void setCallDurationInPulses(Integer callDurationInPulses) {
        this.callDurationInPulses = callDurationInPulses;
    }

    public Integer getEndOfUsagePromptCounter() {
        return endOfUsagePromptCounter;
    }

    public void setEndOfUsagePromptCounter(Integer endOfUsagePromptCounter) {
        this.endOfUsagePromptCounter = endOfUsagePromptCounter;
    }

    public Integer getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Integer callStatus) {
        this.callStatus = callStatus;
    }

    public Boolean getWelcomeMessagePromptFlag() {
        return welcomeMessagePromptFlag;
    }

    public void setWelcomeMessagePromptFlag(Boolean welcomeMessagePromptFlag) {
        this.welcomeMessagePromptFlag = welcomeMessagePromptFlag;
    }

    public String getCallDisconnectReason() {
        return callDisconnectReason;
    }

    public void setCallDisconnectReason(String callDisconnectReason) {
        this.callDisconnectReason = callDisconnectReason;
    }

    public List<CardDetail> getContent() {
        return content;
    }

    public void setContent(List<CardDetail> content) {
        this.content = content;
    }

}


