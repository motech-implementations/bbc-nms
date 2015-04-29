package org.motechproject.nms.frontlineworker.exception;

/**
 *   This class models a user-defined exception for FrontLineWorker
 *   that is not present in WhiteList.
 */
public class FlwNotInWhiteListException extends Exception {

    private String errorCode;

    private String errorDesc;

    public FlwNotInWhiteListException(String message) {
        super(message);
    }

    public FlwNotInWhiteListException(String message, String errorCode,
            String errorDesc) {
        super(message);
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
