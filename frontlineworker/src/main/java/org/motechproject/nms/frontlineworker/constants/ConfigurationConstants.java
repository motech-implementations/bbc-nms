package org.motechproject.nms.frontlineworker.constants;

/**
 * This class models the constants to be used in the module Front Line Worker
 */
public final class ConfigurationConstants {

    public static final int FLW_CONTACT_NUMBER_LENGTH = 10;
    
    public static final String FLW_UPLOAD_SUCCESS = "mds.crud.frontlineworker.CsvFrontLineWorker.csv-import.success";

    public static final int CAPPING_NOT_FOUND_BY_STATE = -1;

    public static final String UNKNOWN_CIRCLE = "99";

    public static final String BASE_SUBJECT = "org.motechproject.frontlineworker.";

    public static final String DELETION_EVENT_SUBJECT_SCHEDULER = BASE_SUBJECT + "deletion";


    private ConfigurationConstants() {
    }
}
