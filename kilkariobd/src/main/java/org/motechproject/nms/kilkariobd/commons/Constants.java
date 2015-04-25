package org.motechproject.nms.kilkariobd.commons;

/**
 * Defines constants for kilkariobd module
 */
public final class Constants {

    /* constants for Integer constants */
    public static final Long ACTIVE_SUBSCRIPTION_COUNT_ZERO = 0L;
    public static final String CONFIGURATION_UPDATE_EVENT = "mds.crud.kilkariobd.Configuation.UPDATE";
    public static final String CONFIGURATION_ID = "object_id";


    /* Constants for Kilkari-Obd system settings parameters */
    public static final String KILKARI_OBD_PROPERTY_FILE_NAME = "kilkariobd.properties";
    public static final String OFFLINE_API_INIT_INTERVAL = "offlineApiInitalIntervalInMilliseconds";
    public static final String OFFLINE_API_MAX_RETRIES = "offlineApiMaxRetries";
    public static final String OFFLINE_API_RETRY_MULTIPLIER = "offlineApiRetryMultiplier";
    public static final String OBD_FILE_LOCAL_PATH = "obdFileLocalPath";
    public static final String SSH_PRIVATE_KEY_FILE = "sshPrivateKeyFile";
    public static final String SSH_LOCAL_USERNAME = "sshLocalUsername";

    /* Strings for request parameters to report validation error */
    public static final String FILE_NAME = "File name";
    public static final String CDR_DETAIL_INFO = "CDR Detail Info";
    public static final String CDR_DETAIL_FILE = "CDR Detail File";
    public static final String CDR_DETAIL_CHECKSUM = "CDR Detail checksum";
    public static final String CDR_DETAIL_RECORDS_COUNT = "CDR Detail records Count";
    public static final String CDR_SUMMARY_INFO = "CDR Summary Info";
    public static final String CDR_SUMMARY_FILE = "CDR Summary File";
    public static final String CDR_SUMMARY_CHECKSUM = "CDR Summary checksum";
    public static final String CDR_SUMMARY_RECORDS_COUNT = "CDR Summary records Count";

    /*Constants for cron jobs*/
    public static final String PREPARE_OBD_TARGET_EVENT_SUBJECT = "kilkariobd.prepare.obdtarget";
    public static final String NOTIFY_OBD_TARGET_EVENT_SUBJECT = "kilkariobd.notify.obdtarget";
    public static final String PREPARE_OBD_TARGET_EVENT_JOB = "OBDPreparationJob";
    public static final String NOTIFY_OBD_TARGET_EVENT_JOB = "ODBNotifyJob";
    public static final String RETRY_PREPARE_OBD_TARGET_EVENT_SUBJECT = "kilkariobd.retry.prepare.obdtarget";
    public static final String OBD_PREPARATION_RETRY_JOB = "obdPreparationRetryJob";
    public static final String CURRENT_OBD_FILE = "currentObdFile";
    public static final String CURRENT_CDR_OBD_FILE = "currentCdrObdFile";
    public static final String OBD_PREPARATION_RETRY_NUMBER = "retryNumber";


    //constants for cdrSummary
    public static final String FINAL_STATUS = "FinalStatus";
    public static final String STATUS_CODE = "StatusCode";
    public static final String REQUEST_ID = "RequestId";
    public static final String SERVICE_ID = "ServiceId";
    public static final String MSISDN = "Msisdn";
    public static final String PRIORITY = "Priority";
    public static final String LANGUAGE_LOCATION_CODE = "LanguageLocationCode";
    public static final String CIRCLE = "Circle";
    public static final String WEEK_ID = "WeekId";
    public static final String CONTENT_FILE_NAME = "ContentFileName";
    public static final String CLI = "Cli";
    public static final String CALL_FLOW_URL = "CallFlowUrl";
    public static final Integer RETRY_DAY_ONE = 1;
    public static final Integer RETRY_DAY_TWO = 2;
    public static final Integer RETRY_DAY_THREE = 3;
    public static final Integer MAX_WEEK_NUMBER = 72;


    //constants for cdrDetail
    public static final String CALL_ID = "CallId";
    public static final String ATTEMPT_NO = "AttemptNo";
    public static final String CALL_START_TIME = "CallStartTime";
    public static final String CALL_ANSWER_TIME = "CallAnswerTime";
    public static final String CALL_END_TIME = "CallEndTime";
    public static final String CALL_DURATION_IN_PULSE = "CallDurationInPulse";
    public static final String CALL_STATUS = "CallStatus";
    public static final String LANGUAGE_LOCATION_ID = "LanguageLocationId";
    public static final String CONTENT_FILE = "ContentFile";
    public static final String MSG_PLAY_END_TIME = "MsgPlayEndTime";
    public static final String MSG_PLAY_START_TIME = "MsgPlayStartTime";
    public static final String CIRCLE_ID = "CircleId";
    public static final String OPERATOR_ID = "OperatorId";
    public static final String CALL_DISCONNECT_REASON = "CallDisconnectReason";

    //constant for httpRetryStrategy
    public static final Integer FIRST_ATTEMPT = 0;

}
