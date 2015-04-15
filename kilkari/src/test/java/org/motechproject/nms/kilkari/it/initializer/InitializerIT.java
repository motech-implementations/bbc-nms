package org.motechproject.nms.kilkari.it.initializer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.initializer.Initializer;
import org.motechproject.nms.kilkari.repository.ActiveSubscriptionCountDataService;
import org.motechproject.nms.kilkari.repository.ConfigurationDataService;
import org.motechproject.nms.kilkari.service.ActiveSubscriptionCountService;
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
    
    @Inject
    protected ActiveSubscriptionCountService activeSubscriptionCountService;
    
    @Inject
    protected ActiveSubscriptionCountDataService activeSubscriptionCountDataService;

    private Initializer initializer;

    @Before
    public void setUp() {
        initializer = new Initializer(configurationService, activeSubscriptionCountService);
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
    public void checkConfigurationInitialize() {

        Configuration configuration = configurationService.getConfiguration();

        Assert.assertEquals(configuration.getIndex(), Initializer.CONFIGURATION_INDEX);
        Assert.assertEquals(configuration.getNumMsgPerWeek(), Initializer.DEFAULT_NUMBER_OF_MSG_PER_WEEK);
        Assert.assertEquals(configuration.getMaxAllowedActiveBeneficiaryCount(), Initializer.DEFAULT_ALLOWED_BENEFICIARY_COUNT);

        Long expectedActiveSubscriptionCount = Initializer.DEFAULT_ACTIVEUSER_COUNT;
        Long actualActiveSubscriptionCount = activeSubscriptionCountService.getActiveSubscriptionCount();
        Assert.assertTrue(expectedActiveSubscriptionCount == actualActiveSubscriptionCount);
    }
}
