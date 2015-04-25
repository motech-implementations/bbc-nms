package org.motechproject.nms.kilkariobd.client;

import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.settings.Settings;

/**
 * This class retry logic for a Http request
 */
public class HttpRetryStrategy {

    /**
     * checks if retryNumber is small enough to retry the request
     * @param  settings is the object of system properties
     * @param retryNumber Integer
     * @return Boolean value
     */
    public static boolean shouldRetry(Settings settings, Integer retryNumber) {

        return retryNumber < Integer.parseInt(settings.getOfflineApiMaxRetries());
    }

    /**
     * This method calculate the timeout interval for a Http request
     * @param retryNumber Integer
     * @param previousInterval Long
     * @return timeout interval in milliseconds
     */
    public static Long getTimeOutInterval(Settings settings, Integer retryNumber, Long previousInterval) {
        Long timeToWaitInMillis = 0L;
        if (retryNumber.equals(Constants.FIRST_ATTEMPT)) {
            timeToWaitInMillis = Long.parseLong(settings.getOfflineApiInitalIntervalInMilliseconds());
        } else {
            timeToWaitInMillis = previousInterval * Integer.parseInt(settings.getOfflineApiRetryMultiplier());
        }
        return timeToWaitInMillis;
    }
}
