package org.motechproject.nms.kilkari.domain;

public enum BeneficiaryType {
    MOTHER,
    CHILD;
    
    public String getPackForBeneficiaryType(){
        if (BeneficiaryType.MOTHER.equals(this)) {
            return "PACK_72";
        } else {
            return "PACK_48";
        }
    }
}
