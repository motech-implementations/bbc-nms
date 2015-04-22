package org.motechproject.nms.kilkariobd.domain;

/**
 * Enum to define different outbound call statuses.
 */
public enum ObdStatusCode {

    OBD_SUCCESS_CALL_CONNECTED(1001),
    OBD_FAILED_NOATTEMPT(2000),
    OBD_FAILED_BUSY(2001),
    OBD_FAILED_NOANSWER(2002),
    OBD_FAILED_SWITCHEDOFF(2003),
    OBD_FAILED_INVALIDNUMBER(2004),
    OBD_FAILED_OTHERS(2005),
    OBD_DNIS_IN_DND(3001);
    
    private int statusCode;
    
    private ObdStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
