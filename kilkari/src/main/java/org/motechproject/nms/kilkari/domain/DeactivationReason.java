package org.motechproject.nms.kilkari.domain;

public enum DeactivationReason {

    CHILD_DEATH("ChildDeath"),
    MOTHER_DEATH("MotherDeath"),
    ABORTION("Abortion"),
    STILL_BIRTH("StillBirth"),
    MSISDN_IN_DND("MsisdnInDND"),
    INVALID_MSISDN("InvalidMsisdn"), 
    USER_DEACTIVATE("UserDeactivation"), 
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
