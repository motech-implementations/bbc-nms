package org.motechproject.nms.frontlineworker.constants;

/**
 * This class models the constants to be used in the module Front Line Worker
 */
public final class ConfigurationConstants {

    public static final String FLW_UPLOAD_SUCCESS = "mds.crud.frontlineworker.CsvFrontLineWorker.csv-import.success";

    public static final String WLU_UPLOAD_SUCCESS = "mds.crud.frontlineworker.CsvWhiteListUsers.csv-import.success";

    public static final int CAPPING_NOT_FOUND_BY_STATE = -1;

    public static final String UNKNOWN_CIRCLE = "99";

    public static final Integer PURGE_DATE = 42;

    public static final Long CONFIGURATION_INDEX = 1L;

    /*Triggers at 00:01am everyday */
    public static final String DELETION_EVENT_CRON_EXPRESSION = "0 1 0 * * ?";

    public static final String FLW_DELETE_JOB = "FLWDeleteJob";

    public static final String FLW_DELETE_EVENT_SUBJECT = "mds.crud.frontlineworker.delete";

    private ConfigurationConstants() {
    }
}
