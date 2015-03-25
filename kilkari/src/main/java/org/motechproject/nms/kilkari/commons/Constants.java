package org.motechproject.nms.kilkari.commons;

/**
 * Defines constants for kilkari module
 */
public final class Constants {

    /* Constants for Subscription Pack Details */
    public static final Integer DURATION_OF_72_WEEK_PACK = 72;

    public static final Integer START_WEEK_OF_72_WEEK_PACK = 1;

    public static final Integer DURATION_OF_48_WEEK_PACK = 48;

    public static final Integer START_WEEK_OF_48_WEEK_PACK = 27;


    /* Constants for Error Description */
    public static final String SUBSCRIPTION_EXIST_ERR_DESC =
            "Upload Unsuccessful as Subscription to MSISDN already Exist";
    
    public static final String SUBSCRIPTION_EXIST_EXCEPTION_MSG =
            "Subscription to MSISDN already Exist";


    /* constants for Integer constants */
    public static final Long ACTIVE_SUBSCRIPTION_COUNT_ZERO = 0L;

    public static final Integer STILL_BIRTH_ZERO = 0;


    /* Constants for MDS Bulk Upload Events */
    public static final String MOTHER_MCTS_CSV_UPLOAD_SUCCESS_EVENT = "mds.crud.kilkari.MotherMctsCsv.csv-import.success";

    public static final String CHILD_MCTS_CSV_UPLOAD_SUCCESS_EVENT = "mds.crud.kilkari.ChildMctsCsv.csv-import.success";
}
