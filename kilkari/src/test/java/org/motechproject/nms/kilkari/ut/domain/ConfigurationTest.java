package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.Configuration;

public class ConfigurationTest {

    @Test
    public void shouldSetValuesInConfiguration() {

        Configuration configuration = new Configuration();

        configuration.setIndex(1L);
        Assert.assertTrue(1L == configuration.getIndex());

        configuration.setFreshObdPriority("freshOdbPriority");
        Assert.assertEquals("freshOdbPriority",configuration.getFreshObdPriority());

        configuration.setFreshObdServiceId("freshObdServiceId");
        Assert.assertEquals("freshObdServiceId",configuration.getFreshObdServiceId());

        configuration.setMaxAllowedActiveBeneficiaryCount(20);
        Assert.assertTrue(20 == configuration.getMaxAllowedActiveBeneficiaryCount());

        configuration.setNationalDefaultLanguageLocationCode(123);
        Assert.assertTrue(123 == configuration.getNationalDefaultLanguageLocationCode());

        configuration.setNumMsgPerWeek(10);
        Assert.assertTrue(10 == configuration.getNumMsgPerWeek());

        configuration.setRetryDay1ObdPriority("retryDay1ObdPriority");
        Assert.assertEquals("retryDay1ObdPriority",configuration.getRetryDay1ObdPriority());

        configuration.setRetryDay1ObdServiceId("retryDay1ObdServiceId");
        Assert.assertEquals("retryDay1ObdServiceId",configuration.getRetryDay1ObdServiceId());

        configuration.setRetryDay2ObdPriority("retryDay2ObdPriority");
        Assert.assertEquals("retryDay2ObdPriority",configuration.getRetryDay2ObdPriority());

        configuration.setRetryDay2ObdServiceId("retryDay2ObdServiceId");
        Assert.assertEquals("retryDay2ObdServiceId",configuration.getRetryDay2ObdServiceId());

        configuration.setRetryDay3ObdPriority("retryDay3ObdPriority");
        Assert.assertEquals("retryDay3ObdPriority",configuration.getRetryDay3ObdPriority());

        configuration.setRetryDay3ObdServiceId("retryDay3ObdServiceId");
        Assert.assertEquals("retryDay3ObdServiceId",configuration.getRetryDay3ObdServiceId());
    }

}
