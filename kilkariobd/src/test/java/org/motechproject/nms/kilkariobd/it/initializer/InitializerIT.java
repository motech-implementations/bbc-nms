package org.motechproject.nms.kilkariobd.it.initializer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkariobd.domain.Configuration;
import org.motechproject.nms.kilkariobd.initializer.Initializer;
import org.motechproject.nms.kilkariobd.repository.ConfigurationDataService;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

/**
 * This class contains test case to test Initializer
 * for initializing the kilkariobd configurations
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)

public class InitializerIT extends BasePaxIT {

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private ConfigurationDataService configurationDataService;

    @Inject
    private MotechSchedulerService motechSchedulerService;

    private Initializer initializer;

    @Before
    public void setUp() {
        initializer = new Initializer(configurationService, motechSchedulerService);
    }

    @After
    public void tearDown() {
        //configurationDataService.deleteAll();
    }

    /**
     * This test case tests that Initializer is correctly
     * initializing the kilkariobd configurations with default values.
     */
    @Test
    public void checkConfigurationInitialize() {

        Configuration configuration = configurationService.getConfiguration();

        Assert.assertEquals(configuration.getIndex(), Initializer.CONFIGURATION_INDEX);
        Assert.assertEquals(configuration.getFreshObdPriority(), Initializer.DEFAULT_FRESH_OBD_PRIORITY);
        Assert.assertEquals(configuration.getFreshObdServiceId(), Initializer.DEFAULT_FRESH_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getRetryDay1ObdPriority(), Initializer.DEFAULT_RETRY_DAY1_OBD_PRIORITY);
        Assert.assertEquals(configuration.getRetryDay1ObdServiceId(), Initializer.DEFAULT_RETRY_DAY1_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getRetryDay2ObdPriority(), Initializer.DEFAULT_RETRY_DAY2_OBD_PRIORITY);
        Assert.assertEquals(configuration.getRetryDay2ObdServiceId(), Initializer.DEFAULT_RETRY_DAY2_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getRetryDay3ObdPriority(), Initializer.DEFAULT_RETRY_DAY3_OBD_PRIORITY);
        Assert.assertEquals(configuration.getRetryDay3ObdServiceId(), Initializer.DEFAULT_RETRY_DAY3_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getObdFileServerIp(),Initializer.DEFAULT_OBD_FILE_SERVER_IP);
        Assert.assertEquals(configuration.getObdFilePathOnServer(),Initializer.DEFAULT_OBD_FILE_PATH_ON_SERVER);
        Assert.assertEquals(configuration.getObdIvrUrl(),Initializer.DEFAULT_OBD_IVR_URL);
        Assert.assertEquals(configuration.getObdFileServerSshUsername(),Initializer.DEFAULT_OBD_FILE_SERVER_SSH_USERNAME);
        Assert.assertEquals(configuration.getObdCreationEventCronExpression(),Initializer.DEFAULT_OBD_CREATION_EVENT_CRON_EXPRESSION);
        Assert.assertEquals(configuration.getObdNotificationEventCronExpression(),Initializer.DEFAULT_OBD_NOTIFICATION_EVENT_CRON_EXPRESSION);
        Assert.assertEquals(configuration.getRetryIntervalForObdPreparationInMins(), Initializer.DEFAULT_RETRY_INTERVAL_FOR_OBD_PREPARATION_IN_MINS);
        Assert.assertEquals(configuration.getMaxObdPreparationRetryCount(), Initializer.DEFAULT_MAX_OBD_PREPARATION_RETRY_COUNT);

    }
}

