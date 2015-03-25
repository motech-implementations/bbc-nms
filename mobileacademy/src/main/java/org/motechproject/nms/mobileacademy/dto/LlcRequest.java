package org.motechproject.nms.mobileacademy.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * LlcRequest object contain details required in set language location code API
 * request.
 *
 */
public class LlcRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String callingNumber;

    @JsonProperty
    private String callId;

    @JsonProperty
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

    @Override
    public String toString() {
        return "LlcRequest{callingNumber=" + callingNumber + ", callId="
                + callId + ", languageLocationCode=" + languageLocationCode
                + "}";
    }

}
