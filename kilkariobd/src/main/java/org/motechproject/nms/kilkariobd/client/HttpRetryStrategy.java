package org.motechproject.nms.kilkariobd.client;

import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class retry logic for a Http request
 */
public class HttpRetryStrategy {
    @Autowired
    private static SettingsFacade kilkariObdSettings;



    /**
     * checks if retryNumber is small enough to retry the request
     * @param retryNumber Integer
     * @return Boolean value
     */
    public static boolean shouldRetry(Integer retryNumber) {
        Settings settings = new Settings(kilkariObdSettings);
        return retryNumber <= Integer.parseInt(settings.getOfflineApiMaxRetries());
    }

    /**
     * This method calculate the timeout interval for a Http request
     * @param retryNumber Integer
     * @param previousInterval Long
     * @return timeout interval in milliseconds
     */
    public static Long getTimeOutInterval(Integer retryNumber, Long previousInterval) {
        Settings settings = new Settings(kilkariObdSettings);
        Long timeToWaitInMillis = 0L;
        if (retryNumber.equals(Constants.FIRST_ATTEMPT)) {
            timeToWaitInMillis = Long.parseLong(settings.getOfflineApiInitalIntervalInMilliseconds());
        } else if (retryNumber <= Integer.parseInt(settings.getOfflineApiMaxRetries())) {
            timeToWaitInMillis = previousInterval * Integer.parseInt(settings.getOfflineApiRetryMultiplier());
        }
        return timeToWaitInMillis;
    }
}
