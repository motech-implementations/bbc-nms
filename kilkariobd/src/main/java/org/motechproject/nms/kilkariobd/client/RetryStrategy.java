package org.motechproject.nms.kilkariobd.client;

public class RetryStrategy {
    static final int DEFAULT_RETRY = 3;
    static final int RETRY_MULTIPLIER = 2;
    static final Long INITIAL_INTERVAL = 5L;


    public static boolean shouldRetry(Integer retryNumber) {
        return retryNumber <= DEFAULT_RETRY;
    }

    public Long getTimeOutInterval(int retryNumber, Long previousInterval) {
        Long timeToWaitInMillis = 0L;
        if (retryNumber < 1) {
            timeToWaitInMillis = INITIAL_INTERVAL;
        } else if (retryNumber <= DEFAULT_RETRY) {
            timeToWaitInMillis = previousInterval * RETRY_MULTIPLIER;
        }
        return timeToWaitInMillis;
    }

}
