package org.motechproject.nms.kilkariobd.domain;

/**
 * Enum to define different priority for a call
 */
public enum Priority {

    DEFAULT(0),
    MEDIUM_PRIORITY(1),
    HIGH_PRIORITY(2);
    
    private int priority;
    
    private Priority(int priority) {
        this.priority = priority;
    }
}
