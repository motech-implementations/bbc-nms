package org.motechproject.nms.kilkari.osgi;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkari.Initializer;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.repository.ConfigurationDataService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

/**
 * This class contains test case to test Initializer
 * for initializing the kilkari configurations
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)

public class InitializerIT extends BasePaxIT {

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private ConfigurationDataService configurationDataService;

    private Initializer initializer;

    @Before
    public void setUp() {
        initializer = new Initializer(configurationService);
    }

    @After
    public void tearDown() {
        configurationDataService.deleteAll();
    }

    /**
     * This test case tests that Initializer is correctly
     * initializing the kilkari configurations with default values.
     */
    @Test
    public void shouldInitializeConfiguration() {

        Configuration configuration = configurationService.getConfiguration();

        Assert.assertEquals(configuration.getIndex(), Initializer.CONFIGURATION_INDEX);
        Assert.assertEquals(configuration.getNmsKk48WeeksPackMsgsPerWeek(), Initializer.DEFAULT_48_WEEKS_PACK_MSGS_PER_WEEK);
        Assert.assertEquals(configuration.getNmsKk72WeeksPackMsgsPerWeek(), Initializer.DEFAULT_72_WEEKS_PACK_MSGS_PER_WEEK);
        Assert.assertEquals(configuration.getNmsKkFreshObdPriority(), Initializer.DEFAULT_FRESH_OBD_PRIORITY);
        Assert.assertEquals(configuration.getNmsKkFreshObdServiceId(), Initializer.DEFAULT_FRESH_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getNmsKkMaxAllowedActiveBeneficiaryCount(), Initializer.DEFAULT_ALLOWED_BENEFICIARY_COUNT);
        Assert.assertEquals(configuration.getNmsKkRetryDay1ObdPriority(), Initializer.DEFAULT_RETRY_DAY1_OBD_PRIORITY);
        Assert.assertEquals(configuration.getNmsKkRetryDay1ObdServiceId(), Initializer.DEFAULT_RETRY_DAY1_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getNmsKkRetryDay2ObdPriority(), Initializer.DEFAULT_RETRY_DAY2_OBD_PRIORITY);
        Assert.assertEquals(configuration.getNmsKkRetryDay2ObdServiceId(), Initializer.DEFAULT_RETRY_DAY2_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getNmsKkRetryDay3ObdPriority(), Initializer.DEFAULT_RETRY_DAY3_OBD_PRIORITY);
        Assert.assertEquals(configuration.getNmsKkRetryDay3ObdServiceId(), Initializer.DEFAULT_RETRY_DAY3_OBD_SERVICE_ID);

    }

}
