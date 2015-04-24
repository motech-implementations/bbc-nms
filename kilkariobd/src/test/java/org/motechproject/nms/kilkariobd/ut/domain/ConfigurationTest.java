package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.Configuration;

public class ConfigurationTest {

    @Test
    public void shouldSetValuesInConfiguration() {

        Configuration configuration = new Configuration();

        configuration.setObdFileServerSshUsername("SshUsername");
        Assert.assertEquals("SshUsername", configuration.getObdFileServerSshUsername());

        configuration.setObdNotificationEventCronExpression("*****");
        Assert.assertEquals("*****", configuration.getObdNotificationEventCronExpression());

        configuration.setObdCreationEventCronExpression("*****");
        Assert.assertEquals("*****", configuration.getObdCreationEventCronExpression());

        configuration.setObdIvrUrl("OBDIvrUrl");
        Assert.assertEquals("OBDIvrUrl", configuration.getObdIvrUrl());

        configuration.setFreshObdPriority(1);
        Assert.assertTrue(1 == configuration.getFreshObdPriority());

        configuration.setIndex(1L);
        Assert.assertTrue(1L == configuration.getIndex());

        configuration.setFreshObdServiceId("serviceId");
        Assert.assertEquals("serviceId", configuration.getFreshObdServiceId());

        configuration.setObdFilePathOnServer("filePath");
        Assert.assertEquals("filePath", configuration.getObdFilePathOnServer());

        configuration.setObdFileServerIp("serverIp");
        Assert.assertEquals("serverIp", configuration.getObdFileServerIp());

        configuration.setRetryDay1ObdPriority(1);
        Assert.assertTrue(1 == configuration.getRetryDay1ObdPriority());

        configuration.setRetryDay2ObdPriority(1);
        Assert.assertTrue(1 == configuration.getRetryDay2ObdPriority());

        configuration.setRetryDay3ObdPriority(1);
        Assert.assertTrue(1 == configuration.getRetryDay3ObdPriority());

        configuration.setRetryDay1ObdServiceId("day10");
        Assert.assertEquals("day10", configuration.getRetryDay1ObdServiceId());

        configuration.setRetryDay2ObdServiceId("day20");
        Assert.assertEquals("day20", configuration.getRetryDay2ObdServiceId());

        configuration.setRetryDay3ObdServiceId("day30");
        Assert.assertEquals("day30", configuration.getRetryDay3ObdServiceId());
    }
}
