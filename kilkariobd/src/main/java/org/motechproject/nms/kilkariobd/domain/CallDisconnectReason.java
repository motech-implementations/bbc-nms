package org.motechproject.nms.kilkariobd.domain;

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

}
