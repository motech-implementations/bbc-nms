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

    /** This Static method checks if a string is of AbortionType
     *
     * @param abortion string that is to be checked
     * @return true if string is a valid value for abortion type else false
     */
    public static boolean checkValidAbortionType (String abortion) {
        AbortionType[] abortionTypes = AbortionType.values();
        boolean foundAbortionType = false;
        for (AbortionType abortionType : abortionTypes) {
            if(abortionType.toString().equals(abortion)){
                foundAbortionType = true;
                break;
            }
        }
        return foundAbortionType;
    }

}
