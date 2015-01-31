package org.motechproject.nms.util.helper;

public class DataValidationException extends Exception {

    public static final String MANDATORY_MISSING_MESSAGE = "Missing mandatory data for [ {} ]";
    public static final String INVALID_FORMAT_MESSAGE = "Invalid Format for [ {} ]";

    private String errorCode;
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

    public String getErroneousField() {
        return erroneousField;
    }

}
