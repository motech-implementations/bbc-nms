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
    
    private Integer statusCode;
    
    private ObdStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Method to get ObdStatusCode by integer
     * @param statusCode Integer
     * @return ObdStatusCode
     */
    public static ObdStatusCode getByInteger(Integer statusCode) {
        for (ObdStatusCode code : ObdStatusCode.values()) {
            if (code.ordinal() == statusCode) {
                return code;
            }
        }
        return null;
    }

}
