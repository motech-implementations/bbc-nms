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
    private String languageLocationCode;

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

    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
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
     *
     * @throws org.motechproject.nms.util.helper.DataValidationException if parameter value is blank or null
     */
    public void validateMandatoryParameters() throws DataValidationException {

        ParseDataHelper.validateAndParseString(ConfigurationConstants.LANGUAGE_LOCATION_CODE,languageLocationCode, true);

        callingNumber = ParseDataHelper.validateAndTrimMsisdn(ConfigurationConstants.CALLING_NUMBER,
                ParseDataHelper.validateAndParseString(ConfigurationConstants.CALLING_NUMBER, callingNumber, true));

        ParseDataHelper.validateLengthOfCallId(ConfigurationConstants.CALL_ID,
                ParseDataHelper.validateAndParseString(ConfigurationConstants.CALL_ID, callId, true));

        if (null == languageLocationCode) {
            ParseDataHelper.raiseInvalidDataException(ConfigurationConstants.LANGUAGE_LOCATION_CODE, null);
        }
    }
}
