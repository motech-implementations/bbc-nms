package org.motechproject.nms.kilkariobd.settings;


import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this class is to read kilkari obd properties from the kilkariobd.properties file and make
 * the setting parameters available for the application to use *
 */
public class Settings {

    private Logger logger = LoggerFactory.getLogger(Settings.class);

    private String offlineApiInitialIntervalInMilliseconds;

    private String offlineApiMaxRetries;

    private String offlineApiRetryMultiplier;

    private String obdFileLocalPath;

    private String sshPrivateKeyFile;


    public Settings() {
        this(null, null, null, null, null);
    }

    public Settings(SettingsFacade settingsFacade) {
        this(
                settingsFacade.getProperty(Constants.OFFLINE_API_INIT_INTERVAL, Constants.KILKARI_OBD_PROPERTY_FILE_NAME),
                settingsFacade.getProperty(Constants.OFFLINE_API_MAX_RETRIES, Constants.KILKARI_OBD_PROPERTY_FILE_NAME),
                settingsFacade.getProperty(Constants.OFFLINE_API_RETRY_MULTIPLIER, Constants.KILKARI_OBD_PROPERTY_FILE_NAME),
                settingsFacade.getProperty(Constants.OBD_FILE_LOCAL_PATH, Constants.KILKARI_OBD_PROPERTY_FILE_NAME),
                settingsFacade.getProperty(Constants.SSH_PRIVATE_KEY_FILE, Constants.KILKARI_OBD_PROPERTY_FILE_NAME)
        );
    }

    public Settings(String offlineApiInitalIntervalInMilliseconds, String offlineApiMaxRetries, String offlineApiRetryMultiplier,
                    String obdFileLocalPath, String sshPrivateKeyFile) {
        this.offlineApiInitialIntervalInMilliseconds = offlineApiInitalIntervalInMilliseconds;
        this.offlineApiMaxRetries = offlineApiMaxRetries;
        this.offlineApiRetryMultiplier = offlineApiRetryMultiplier;
        this.obdFileLocalPath = obdFileLocalPath;
        this.sshPrivateKeyFile = sshPrivateKeyFile;
    }

    public String getOfflineApiInitalIntervalInMilliseconds() {
        return offlineApiInitialIntervalInMilliseconds;
    }

    public void setOfflineApiInitalIntervalInMilliseconds(String offlineApiInitalIntervalInMilliseconds) {
        this.offlineApiInitialIntervalInMilliseconds = offlineApiInitalIntervalInMilliseconds;
    }

    public String getOfflineApiMaxRetries() {
        return offlineApiMaxRetries;
    }

    public void setOfflineApiMaxRetries(String offlineApiMaxRetries) {
        this.offlineApiMaxRetries = offlineApiMaxRetries;
    }

    public String getOfflineApiRetryMultiplier() {
        return offlineApiRetryMultiplier;
    }

    public void setOfflineApiRetryMultiplier(String offlineApiRetryMultiplier) {
        this.offlineApiRetryMultiplier = offlineApiRetryMultiplier;
    }

    public String getObdFileLocalPath() {
        return obdFileLocalPath;
    }

    public void setObdFileLocalPath(String obdFileLocalPath) {
        this.obdFileLocalPath = obdFileLocalPath;
    }

    public String getSshPrivateKeyFile() {
        return sshPrivateKeyFile;
    }

    public void setSshPrivateKeyFile(String sshPrivateKeyFile) {
        this.sshPrivateKeyFile = sshPrivateKeyFile;
    }
}