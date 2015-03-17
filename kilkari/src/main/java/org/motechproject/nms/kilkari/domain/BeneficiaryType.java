package org.motechproject.nms.kilkari.domain;

public enum BeneficiaryType {
    MOTHER,
    CHILD;
    
    public String getPack(BeneficiaryType type){
        if (BeneficiaryType.MOTHER==type){
            return "PACK_72";
        } else {
            return "PACK_48";
        }
    }
}
