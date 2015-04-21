package org.motechproject.nms.kilkariobd.commons;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

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

    public static final String FILE_NAME = "File name";

    public static final String CDR_DETAIL_FILE = "cdr Detail File";

    public static final String CDR_DETAIL_CHECKSUM = "cdr Detail checksum";

    public static final String CDR_DETAIL_RECORDS_COUNT = "cdr Detail records Count";

    public static final String CDR_SUMMARY_FILE = "cdr Summary File";

    public static final String CDR_SUMMARY_CHECKSUM = "cdr Summary checksum";

    public static final String CDR_SUMMARY_RECORDS_COUNT = "cdr Summary records Count";

    public static final String CONTENT_FILE_NAME = "Content file name";



}
