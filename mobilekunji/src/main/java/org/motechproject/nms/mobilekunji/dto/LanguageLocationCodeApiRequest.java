package org.motechproject.nms.mobilekunji.dto;

/**
 * Created by abhishek on 23/2/15.
 */
public class LanguageLocationCodeApiRequest {

    private String callingNumber;

    private Integer languageLocationCode;

    private String callId;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCallingNumber() {
        return callingNumber;
    }

    public void setCallingNumber(String callingNumber) {
        this.callingNumber = callingNumber;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    @Override
    public String toString() {
        return "LanguageLocationCodeApiRequest{" +
                "callingNumber='" + callingNumber + '\'' +
                ", languageLocationCode=" + languageLocationCode +
                ", callId='" + callId + '\'' +
                '}';
    }
}
