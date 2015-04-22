package org.motechproject.nms.kilkariobd.domain;

/**
 * Enum to define different reason for which call may disconnect
 */
public enum CallDisconnectReason {

    NORMAL_DROP("Normal Drop", 1),
    VXML_RUNTIME_EXCEPTION("VXML Runtime exception", 2),
    CONTENT_NOT_FOUND("Content Not found", 3),
    USAGE_CAP_EXCEEDED("Usage Cap exceeded", 4),
    ERROR_IN_API("Error in the API", 5),
    SYSTEM_ERROR("System Error", 6);
    
    private String reason;
    private int value;
    
    private CallDisconnectReason(String reason, int value){
        this.reason = reason;
        this.value = value;
    }

    /**
     * method to return enum for given string value otherwise null
     * @param reason String for disconnect reason
     * @return CallDisconnectReason enum
     */
    public static CallDisconnectReason getByString(String reason) {
        for (CallDisconnectReason disconnectReason : CallDisconnectReason.values()) {
            if (reason.equals(disconnectReason.name())) {
                return disconnectReason;
            }
        }
        return null;
    }

}
