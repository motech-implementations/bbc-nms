package org.motechproject.nms.kilkariobd.domain;

public enum Priority {

    Default(0),
    MEDIUM_PRIORITY(1),
    HIGH_PRIORITY(2);
    
    private int priority;
    
    private Priority(int priority) {
        this.priority = priority;
    }
}
