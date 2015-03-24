package org.motechproject.nms.util.constants;

/**
 * This class defines all the commonly used constants
 * as well as constants required in Util module
 */
public final class Constants {

    public static final String EMPTY_STRING = "";
    public static final int MSISDN_LENGTH = 10;
    public static final int CALL_ID_LENGTH = 15;

    /* Csv event related constants */
    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    /* Logging related constants*/
    public static final String NMS_PACKAGE_NAME = "org.motechproject.nms";
    public static final String NMS_DEFAULT_LOG_LEVEL = "INFO";

    private Constants() {

    }

}
