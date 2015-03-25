package org.motechproject.nms.kilkari.domain;

import org.motechproject.nms.util.helper.DataValidationException;

public enum EntryType {
    ACTIVE("1"), 
    DEATH("9"),
    MIGRATED_IN("2"),
    MIGRATED_OUT("3");
    
    private String value;

    private EntryType(String value) {
        this.value = value;
    }
    
    public String toString(){
        return value;
    }

    /** This Static method checks if a string is of EntryType
     *
     * @param entry string that is to be checked
     * @return true if string is a valid value for entry type else false
     */
    public static boolean checkValidEntryType (String entry) throws DataValidationException{
        EntryType[] entryTypes = EntryType.values();
        boolean foundEntryType = false;
        if(entry == null) {
            foundEntryType = true;
            return foundEntryType;
        }
        for (EntryType entryType : entryTypes) {
            if(entryType.toString().equals(entry)){
                foundEntryType = true;
                break;
            }
        }
        return foundEntryType;
    }
}
