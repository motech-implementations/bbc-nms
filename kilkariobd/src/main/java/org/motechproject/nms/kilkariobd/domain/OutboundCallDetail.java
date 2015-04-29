package org.motechproject.nms.kilkariobd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * This entity shall contains the call details of the OBD messages delivery calls requested in obd target file
 */
@Entity
public class OutboundCallDetail {

    @UIDisplayable(position = 0L)
    @Field(required=true)
    private String requestId;

    @UIDisplayable(position = 1L)
    @Unique
    @Field(required=true)
    private String callId;

    @UIDisplayable(position = 2L)
    @Field(required=true)
    private String msisdn;

    @UIDisplayable(position = 3L)
    @Field(required=true)
    private String circleCode;

    @UIDisplayable(position = 4L)
    @Field(required=true)
    private String operatorCode;

    @UIDisplayable(position = 5L)
    @Min(value=1)
    @Max(value=99)
    @Field(required = true)
    private Integer languageLocationCode;

    @UIDisplayable(position = 6L)
    @Field(required=true)
    private String weekId;

    @UIDisplayable(position = 7L)
    @Field(required=true)
    private Integer callStatus;

    @UIDisplayable(position = 8L)
    @Field(required=true)
    private CallDisconnectReason callDisconnectReason;

    @UIDisplayable(position = 9L)
    @Field(required=true)
    private Integer attemptNo;

    @UIDisplayable(position = 10L)
    @Field(required=true)
    private Long callStartTime;

    @UIDisplayable(position = 11L)
    @Field
    private Long callAnswerTime;

    @UIDisplayable(position = 12L)
    @Field(required=true)
    private Long callEndTime;

    @UIDisplayable(position = 13L)
    @Field(required=true)
    private Long callDurationInPulse;

    @UIDisplayable(position = 14L)
    @Field(required=true)
    private String contentFile;

    @UIDisplayable(position = 15L)
    @Field(required=true)
    private Integer msgPlayStartTime;

    @UIDisplayable(position = 16L)
    @Field(required=true)
    private Integer msgPlayEndTime;

    @UIDisplayable(position = 17L)
    @Field(required=true)
    private Integer priority;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Integer getAttemptNo() {
        return attemptNo;
    }

    public void setAttemptNo(Integer attemptNo) {
        this.attemptNo = attemptNo;
    }

    public Long getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Long callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Long getCallAnswerTime() {
        return callAnswerTime;
    }

    public void setCallAnswerTime(Long callAnswerTime) {
        this.callAnswerTime = callAnswerTime;
    }

    public Long getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(Long callEndTime) {
        this.callEndTime = callEndTime;
    }

    public Long getCallDurationInPulse() {
        return callDurationInPulse;
    }

    public void setCallDurationInPulse(Long callDurationInPulse) {
        this.callDurationInPulse = callDurationInPulse;
    }

    public Integer getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Integer callStatus) {
        this.callStatus = callStatus;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public Integer getMsgPlayStartTime() {
        return msgPlayStartTime;
    }

    public void setMsgPlayStartTime(Integer msgPlayStartTime) {
        this.msgPlayStartTime = msgPlayStartTime;
    }

    public Integer getMsgPlayEndTime() {
        return msgPlayEndTime;
    }

    public void setMsgPlayEndTime(Integer msgPlayEndTime) {
        this.msgPlayEndTime = msgPlayEndTime;
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public CallDisconnectReason getCallDisconnectReason() {
        return callDisconnectReason;
    }

    public void setCallDisconnectReason(CallDisconnectReason callDisconnectReason) {
        this.callDisconnectReason = callDisconnectReason;
    }

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }
}
