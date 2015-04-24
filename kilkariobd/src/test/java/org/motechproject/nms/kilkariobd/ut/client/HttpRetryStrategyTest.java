package org.motechproject.nms.kilkariobd.ut.client;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.client.HttpRetryStrategy;
import org.motechproject.nms.kilkariobd.settings.Settings;

public class HttpRetryStrategyTest {

    @Test
    public void shouldReturnFalseForRetrynumGreaterThanMaxRetries() {

        Settings settings = new Settings(null, "3", null, null, null, null);

        Assert.assertFalse(HttpRetryStrategy.shouldRetry(settings,4));

    }

    @Test
    public void shouldReturnTruForRetrynumlessThanMaxRetries() {

        Settings settings = new Settings(null, "3", null, null, null, null);

        Assert.assertTrue(HttpRetryStrategy.shouldRetry(settings,2));

    }

    @Test
    public void shouldGetTimeOutIntervalWithFirstAttempt() {

        Settings settings = new Settings("5000", "3", "2", null, null, null);
        Assert.assertTrue(5000L == HttpRetryStrategy.getTimeOutInterval(settings,0,0L));
    }

    @Test
    public void shouldGetTimeOutIntervalWhenRetryNumLessThanMaxRetries() {

        Settings settings = new Settings("5000", "3", "2", null, null, null);
        Assert.assertTrue(10000L == HttpRetryStrategy.getTimeOutInterval(settings,2, 5000L));
    }

    @Test
    public void shouldGetTimeOutIntervalWhenRetryNumGreaterThanMaxRetries() {

        Settings settings = new Settings("5000", "3", "2", null, null, null);
        Assert.assertTrue(0L == HttpRetryStrategy.getTimeOutInterval(settings,4, 5000L));
    }



}
