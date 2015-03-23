package org.motechproject.nms.mobileacademy.dto;

/**
 * LlcRequest object contain details required in set language location code API
 * request.
 *
 */
public class LlcRequest {

    private String callingNumber;

    private String callId;

    private String languageLocationCode;

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

    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

}
