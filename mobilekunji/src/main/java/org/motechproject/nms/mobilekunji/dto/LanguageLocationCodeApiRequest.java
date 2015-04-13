package org.motechproject.nms.mobilekunji.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * API request for Language Location Code
 */
public class LanguageLocationCodeApiRequest {

    @JsonProperty
    private String callingNumber;

    @JsonProperty
    private Integer languageLocationCode;

    @JsonProperty
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


    /**
     * Validates mandatory value parameter and non null values
     * @throws org.motechproject.nms.util.helper.DataValidationException if parameter value is blank or null
     */
    public void validateMandatoryParameters() throws DataValidationException {

        ParseDataHelper.validateAndTrimMsisdn(ConfigurationConstants.CALLING_NUMBER,callingNumber);
        ParseDataHelper.validateAndParseString(ConfigurationConstants.CALLING_NUMBER, callingNumber, true);
        ParseDataHelper.validateLengthOfCallId(ConfigurationConstants.CALL_ID, callId);
        ParseDataHelper.validateAndParseString(ConfigurationConstants.CALL_ID,callId,true);
    }
}
