package org.motechproject.nms.kilkari.domain;

public enum AbortionType {
    
    MTP_G12_WEEKS("MTP>12 Weeks"), 
    MTP_L12_WEEKS("MTP<12 Weeks"), 
    SPONTANEOUS("Spontaneous"), 
    NONE("None");
    
    private String type;
    private AbortionType(String type){
        this.type = type;
    }
    
    @Override
    public String toString(){
        return type;
    }

    public static boolean checkAbortionType (String abortion) {
        AbortionType[] abortionTypes = AbortionType.values();
        boolean foundAbortionType = false;
        for (AbortionType abortionType : abortionTypes) {
            if(abortionType.toString().equalsIgnoreCase(abortion)){
                foundAbortionType = true;
                break;
            }
        }
        return foundAbortionType;
    }

}
