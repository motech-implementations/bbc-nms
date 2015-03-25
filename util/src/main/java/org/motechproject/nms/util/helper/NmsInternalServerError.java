package org.motechproject.nms.util.helper;

/**
 * This Exception indicates and InternalServerError situation in NMS.
 */
public class NmsInternalServerError extends Exception {



    private String errorCode;
    private String errorDesc;

    public NmsInternalServerError(String message) {
        super(message);
    }

    public NmsInternalServerError(String message, String errorCode, String errorDesc ) {
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
