package org.motechproject.nms.kilkari.domain;

import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

public enum EntryType {
    ACTIVE("1"), 
    DEATH("9"),
    MigratedIn("2"),
    MigratedOut("3");
    
    private String value;

    private EntryType(String value) {
        this.value = value;
    }
    
    public String toString(){
        return value;
    }
    
    public static boolean checkValidEntryType (String entry) throws DataValidationException{
        EntryType[] entryTypes = EntryType.values();
        boolean foundEntryType = false;
        for (EntryType entryType : entryTypes) {
            if(entryType.toString().equalsIgnoreCase(entry)){
                foundEntryType = true;
                break;
            }
        }
        return foundEntryType;
    }
}
