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

    public static final String FILE_NAME = "File name";
    public static final String CDR_DETAIL_FILE = "cdr Detail File";
    public static final String CDR_DETAIL_CHECKSUM = "cdr Detail checksum";
    public static final String CDR_DETAIL_RECORDS_COUNT = "cdr Detail records Count";
    public static final String CDR_SUMMARY_FILE = "cdr Summary File";
    public static final String CDR_SUMMARY_CHECKSUM = "cdr Summary checksum";
    public static final String CDR_SUMMARY_RECORDS_COUNT = "cdr Summary records Count";
    public static final String PREPARE_OBD_TARGET_EVENT_SUBJECT = "kilkariobd.prepare.obdtarget";
    public static final String NOTIFY_OBD_TARGET_EVENT_SUBJECT = "kilkariobd.notify.obdtarget";
    public static final String PREPARE_OBD_TARGET_EVENT_Job = "OBDPreparationEvent";
    public static final String NOTIFY_OBD_TARGET_EVENT_Job = "NotifyOBDEvent";

    //constants for cdrSummary
    public static final String RETRY_DAY_NUMBER = "retryDayNumber";
    public static final String FINAL_STATUS = "finalStatus";
    public static final String STATUS_CODE = "statusCode";
    public static final String REQUEST_ID = "requestId";
    public static final String SERVICE_ID = "serviceId";
    public static final String MSISDN = "msisdn";
    public static final String PRIORITY = "priority";
    public static final String LANGUAGE_LOCATION_CODE = "languageLocationCode";
    public static final String CIRCLE = "circle";
    public static final String WEEK_ID = "weekId";
    public static final String CONTENT_FILE_NAME = "contentFileName";
    public static final String CLI = "cli";
    public static final String CALL_FLOW_URL = "callFlowUrl";

    //constants for cdrDetail
    public static final String CALL_ID = "callId";
    public static final String ATTEMPT_NO = "attemptNo";
    public static final String CALL_START_TIME = "callStartTime";
    public static final String CALL_ANSWER_TIME = "callAnswerTime";
    public static final String CALL_END_TIME = "callEndTime";
    public static final String CALL_DURATION_IN_PULSE = "callDurationInPulse";
    public static final String CALL_STATUS = "callStatus";
    public static final String LANGUAGE_LOCATION_ID = "languageLocationId";
    public static final String CONTENT_FILE = "contentFile";
    public static final String MSG_PLAY_END_TIME = "msgPlayEndTime";
    public static final String MSG_PLAY_START_TIME = "msgPlayStartTime";
    public static final String CIRCLE_ID = "circleId";
    public static final String OPERATOR_ID = "operatorId";
    public static final String CALL_DISCONNECT_REASON = "callDisconnectReason";

    //constant for httpRetryStrategy
    public static final Integer FIRST_ATTEMPT = 0;

}
