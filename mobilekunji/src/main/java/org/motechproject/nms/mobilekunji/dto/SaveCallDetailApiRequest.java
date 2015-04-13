package org.motechproject.nms.mobilekunji.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.nms.mobilekunji.CommonValidator;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.CardDetail;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

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

    @Override
    public String toString() {
        return "SaveCallDetailApiRequest{" +
                "callingNumber='" + callingNumber + '\'' +
                ", callId='" + callId + '\'' +
                ", operator='" + operator + '\'' +
                ", circle='" + circle + '\'' +
                ", callStartTime=" + callStartTime +
                ", callEndTime=" + callEndTime +
                ", callDurationInPulses=" + callDurationInPulses +
                ", endOfUsagePromptCounter=" + endOfUsagePromptCounter +
                ", callStatus=" + callStatus +
                ", welcomeMessagePromptFlag=" + welcomeMessagePromptFlag +
                ", callDisconnectReason='" + callDisconnectReason + '\'' +
                ", content=" + content +
                '}';
    }

    /**
     * Validates mandatory value parameter and non null values
     * @throws org.motechproject.nms.util.helper.DataValidationException if parameter value is blank or null
     */
    public void validateMandatoryParameters() throws DataValidationException {

        ParseDataHelper.validateAndTrimMsisdn(ConfigurationConstants.CALLING_NUMBER, callingNumber);
        ParseDataHelper.validateAndParseString(ConfigurationConstants.CALLING_NUMBER, callingNumber, true);
        ParseDataHelper.validateLengthOfCallId(ConfigurationConstants.CALL_ID, callId);
        ParseDataHelper.validateAndParseString(ConfigurationConstants.CALL_ID,callId,true);

        ParseDataHelper.validateAndParseString(ConfigurationConstants.OPERATOR_CODE,operator,true);
        ParseDataHelper.validateAndParseString(ConfigurationConstants.CIRCLE_CODE,operator,true);
        ParseDataHelper.validateAndParseLong(ConfigurationConstants.CALL_START_TIME, callStartTime.toString(), true);
        ParseDataHelper.validateAndParseLong(ConfigurationConstants.CALL_END_TIME,callEndTime.toString(),true);
        ParseDataHelper.validateAndParseInt(ConfigurationConstants.CALL_DURATION_PULSES, callDurationInPulses.toString(), true);
        ParseDataHelper.validateAndParseInt(ConfigurationConstants.END_OF_USAGE_PROMPT,endOfUsagePromptCounter.toString(),true);
        ParseDataHelper.validateAndParseBoolean(ConfigurationConstants.WELCOME_MESSAGE_FLAG, welcomeMessagePromptFlag.toString(), true);
        ParseDataHelper.validateAndParseInt(ConfigurationConstants.CALL_STATUS, callStatus.toString(), true);
        ParseDataHelper.validateAndParseString(ConfigurationConstants.CALL_DISCONNECTED_REASON, callDisconnectReason, true);
    }

    /**
     * Validates mandatory value parameter and non null values
     * @throws org.motechproject.nms.util.helper.DataValidationException if parameter value is blank or null
     */
    public void validateCardDetailParameters() throws DataValidationException {

        if(null != content && !content.isEmpty()) {

            for(CardDetail carDetail : content) {

                ParseDataHelper.validateAndParseString(ConfigurationConstants.CARD_NUMBER, carDetail.getMkCardNumber().toString(), true);
                CommonValidator.validateCardNumber(carDetail.getMkCardNumber().toString());
                ParseDataHelper.validateAndParseString(ConfigurationConstants.CONTENT_NAME, carDetail.getContentName(), true);
                ParseDataHelper.validateAndParseString(ConfigurationConstants.AUDIO_FILE_NAME, carDetail.getAudioFileName(), true);
                ParseDataHelper.validateAndParseInt(ConfigurationConstants.START_TIME, carDetail.getStartTime().toString(),true);
                ParseDataHelper.validateAndParseInt(ConfigurationConstants.END_TIME, carDetail.getEndTime().toString(), true);
            }
        }
    }

}


