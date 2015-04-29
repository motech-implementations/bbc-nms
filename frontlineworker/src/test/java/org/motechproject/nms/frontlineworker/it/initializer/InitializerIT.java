package org.motechproject.nms.frontlineworker.it.initializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.frontlineworker.Initializer;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.Configuration;
import org.motechproject.nms.frontlineworker.repository.ConfigurationDataService;
import org.motechproject.nms.frontlineworker.service.ConfigurationService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

/**
 * This class contains test case to test Initializer
 * for initializing the FrontLineWorker configurations
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class InitializerIT {

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

    /**
     * This test case tests that Initializer is correctly
     * initializing the FrontLineWorker configurations with default values.
     */

    @Test
    public void checkConfigurationInitialize() {

        Configuration configuration = configurationService.getConfiguration();

        Assert.assertEquals(configuration.getIndex(), ConfigurationConstants.CONFIGURATION_INDEX);
        Assert.assertEquals(configuration.getDeletionEventCronExpression(), ConfigurationConstants.DELETION_EVENT_CRON_EXPRESSION);
        Assert.assertEquals(configuration.getPurgeDate(), ConfigurationConstants.PURGE_DATE);
    }
}
