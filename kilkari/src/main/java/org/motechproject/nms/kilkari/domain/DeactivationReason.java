package org.motechproject.nms.kilkari.domain;

/**
 * This enum is used for reason for deactivation of subscription.
 */
public enum DeactivationReason {

    CHILD_DEATH("ChildDeath"),
    MOTHER_DEATH("MotherDeath"),
    ABORTION("Abortion"),
    STILL_BIRTH("StillBirth"),
    MSISDN_IN_DND("MsisdnInDND"),
    INVALID_MSISDN("InvalidMsisdn"), 
    USER_DEACTIVATED("UserDeactivation"),
    PACK_CHANGED("PackChanged"),
    PACK_SCHEDULE_CHANGED("PackLmpOrDobChanged"),
    NONE("None");
    
    private String name;
    
    private DeactivationReason(String name){
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
