package org.motechproject.nms.kilkariobd.domain;

public enum CallStatus {
    Success(1),
    Failed(2),
    Rejected(3);
    
    private int callStatus;
    
    private CallStatus(int callStatus){
        this.callStatus = callStatus;
    }

}
