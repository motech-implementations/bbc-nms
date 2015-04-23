package org.motechproject.nms.frontlineworker.exception;

public class ServiceNotDeployedException extends Exception {

    private String errorCode;
    private String errorDesc;

    public ServiceNotDeployedException(String message) {
        super(message);
    }

    public ServiceNotDeployedException(String message, String errorCode, String errorDesc) {
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

