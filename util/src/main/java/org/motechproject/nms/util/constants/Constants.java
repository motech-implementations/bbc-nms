package org.motechproject.nms.util.constants;

/**
 * This class defines all the commonly used constants
 * as well as constants required in Util module
 */
public final class Constants {

    public static final String NEXT_LINE = "\n";
    public static final String TAB = "\t";
    public static final String DELIMITER = ", ";
    public static final String ERROR_LOG_TITLE = "Record upload failed for : ";
    public static final String BULK_UPLOAD_SUMMARY_TITLE = "Bulk Upload Summary : ";
    public static final String UNDERLINE = "------------------------------------------------------------------------------------------------------------------";
    public static final String USER_NAME_TITLE = "Uploaded by : ";
    public static final String SUCCESSFUL_RECORDS_TITLE = "Number Of Records Successful : ";
    public static final String FAILED_RECORDS_TITLE = "Number Of Records Failed : ";
    public static final String EMPTY_STRING = "";
    public static final int MSISDN_LENGTH = 10;
    
    /* Csv event related constants */
    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_UPDATED_IDS = CSV_IMPORT_PREFIX + "updated_ids";
    public static final String CSV_IMPORT_CREATED_COUNT = CSV_IMPORT_PREFIX + "created_count";
    public static final String CSV_IMPORT_UPDATED_COUNT = CSV_IMPORT_PREFIX + "updated_count";
    public static final String CSV_IMPORT_TOTAL_COUNT = CSV_IMPORT_PREFIX + "total_count";
    public static final String CSV_IMPORT_FAILURE_MSG = CSV_IMPORT_PREFIX + "failure_message";
    public static final String CSV_IMPORT_FAILURE_STACKTRACE = CSV_IMPORT_PREFIX + "failure_stacktrace";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private Constants() {

    }

}
