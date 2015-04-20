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
    
    /* Constants for Csv fields */
    public static final String WHOM_PHONE_NUM = "Whom Phone Num";
    
    public static final String ABORTION = "Abortion";

    public static final String ENTRY_TYPE = "Entry Type";

    public static final String OUTCOME_NOS = "OutcomeNos";

    public static final String LMP_DATE = "Lmp Date";

    public static final String NAME = "Name";

    public static final String AADHAR_NUM = "Aadhar Num";

    public static final String AGE = "Age";

    public static final String ID_NO = "idNo";
    
    public static final String MOTHER_ID = "Mother Id";
    
    public static final String BIRTH_DATE = "Birth Date";

    public static final String MOTHER_NAME = "Mother Name";
    
    public static final String CONTENT_TYPE = "Content Type";

    public static final String CONTENT_NAME = "Content Name";

    public static final String CONTENT_ID = "Content Id";

    public static final String CONTENT_FILE = "Content File";

    public static final String CONTENT_DURATION = "Content Duration";

    /* Constants for Location fields  */
    public static final String VILLAGE_CODE = "Village Code";

    public static final String SUB_CENTERED_CODE = "Sub centered Code";

    public static final String PHC_CODE = "Phc Code";

    public static final String HEALTH_BLOCK_CODE = "Health Block Code";

    public static final String TALUKA_CODE = "Taluka Code";

    public static final String DISTRICT_CODE = "District Code";

    public static final String STATE_CODE = "State Code";
    
    public static final String OPERATOR_CODE = "Operator Code";

    public static final String LANGUAGE_LOCATION_CODE = "LanguageLocation Code";

    public static final String CIRCLE_CODE = "Circle Code";

    public static final String CALL_ID = "Call Id";

    public static final String CALLED_NUMBER = "Called Number";

    public static final String SUBSCRIPTION_ID = "Subscription Id";

    public static final String SUBSCRIPTION_PACK = "Subscription Pack";

    public static final String CALLING_NUMBER = "Calling Number";

    public static final String DEFAULT_FETCH_GROUP = "true";
    
    public static final int DAYS_IN_WEEK = 7;
    
}
