package org.motechproject.nms.kilkariobd.domain;

/**
 * Enum to define different states of a call
 */
public enum CallStatus {
    SUCCESS(1),
    FAILED(2),
    REJECTED(3);
    
    private Integer callStatus;
    
    private CallStatus(Integer callStatus){
        this.callStatus = callStatus;
    }

    public static CallStatus getByInteger(Integer callStatus) {
        for (CallStatus status : CallStatus.values()) {
            if (status.ordinal() == callStatus) {
                return status;
            }
        }
        return null;
    }

}
