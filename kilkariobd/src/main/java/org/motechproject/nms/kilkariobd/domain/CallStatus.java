package org.motechproject.nms.kilkariobd.domain;

public enum CallStatus {
    SUCCESS(1),
    FAILED(2),
    REJECTED(3);
    
    private int callStatus;
    
    private CallStatus(int callStatus){
        this.callStatus = callStatus;
    }

}
