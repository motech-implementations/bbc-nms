package org.motechproject.nms.kilkariobd.client;

import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;

public class RetryStrategy {
    @Autowired
    SettingsFacade kilkariObdSettings;

    Settings settings = new Settings(kilkariObdSettings);

    public boolean shouldRetry(Integer retryNumber) {
        return retryNumber <= Integer.parseInt(settings.getOfflineApiMaxRetries());
    }

    public Long getTimeOutInterval(Integer retryNumber, Long previousInterval) {
        Long timeToWaitInMillis = 0L;
        if (retryNumber < 1) {
            timeToWaitInMillis = Long.parseLong(settings.getOfflineApiInitalIntervalInMilliseconds());
        } else if (retryNumber <= Integer.parseInt(settings.getOfflineApiMaxRetries())) {
            timeToWaitInMillis = previousInterval * Integer.parseInt(settings.getOfflineApiRetryMultiplier());
        }
        return timeToWaitInMillis;
    }

}
