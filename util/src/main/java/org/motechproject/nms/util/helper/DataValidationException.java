package org.motechproject.nms.util.helper;

/**
 * This class models a user-defined exception for data
 * validation failure while parsing bulk upload data.
 */
public class DataValidationException extends Exception {

    public static final String MANDATORY_MISSING_MESSAGE = "Missing mandatory data [ %s ], value is [ %s ]";
    public static final String INVALID_FORMAT_MESSAGE = "Invalid Format data [ %s ], value is [%s]";

    private String errorCode;
    private String errorDesc;
    private String erroneousField;

    public DataValidationException() {
        super();
    }

    public DataValidationException(String message, String errorCode, String erroneousField) {
        super(message);
        this.errorCode = errorCode;
        this.erroneousField = erroneousField;
    }

    public DataValidationException(String message, String errorCode, String erroneousField, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.erroneousField = erroneousField;
    }

    public DataValidationException(String message, String errorCode, String errorDesc, String erroneousField,
                                   Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.erroneousField = erroneousField;
    }

    public DataValidationException(String message, String errorCode, String errorDesc, String erroneousField) {
        super(message);
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.erroneousField = erroneousField;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " for errorCode :" + errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }


    public String getErrorDesc() {
        return errorDesc;
    }

    public String getErroneousField() {
        return erroneousField;
    }

}

