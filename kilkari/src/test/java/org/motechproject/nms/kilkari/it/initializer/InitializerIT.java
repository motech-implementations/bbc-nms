package org.motechproject.nms.kilkari.it.initializer;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.initializer.Initializer;
import org.motechproject.nms.kilkari.repository.ConfigurationDataService;
import org.motechproject.nms.kilkari.service.ActiveUserService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

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
    private ActiveUserService activeUserService;


    private Initializer initializer;

    @Before
    public void setUp() {
        initializer = new Initializer(configurationService, activeUserService);
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
        Assert.assertEquals(configuration.getNumMsgPerWeek(), Initializer.DEFAULT_NUMBER_OF_MSG_PER_WEEk);
        Assert.assertEquals(configuration.getFreshObdPriority(), Initializer.DEFAULT_FRESH_OBD_PRIORITY);
        Assert.assertEquals(configuration.getFreshObdServiceId(), Initializer.DEFAULT_FRESH_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getMaxAllowedActiveBeneficiaryCount(), Initializer.DEFAULT_ALLOWED_BENEFICIARY_COUNT);
        Assert.assertEquals(configuration.getRetryDay1ObdPriority(), Initializer.DEFAULT_RETRY_DAY1_OBD_PRIORITY);
        Assert.assertEquals(configuration.getRetryDay1ObdServiceId(), Initializer.DEFAULT_RETRY_DAY1_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getRetryDay2ObdPriority(), Initializer.DEFAULT_RETRY_DAY2_OBD_PRIORITY);
        Assert.assertEquals(configuration.getRetryDay2ObdServiceId(), Initializer.DEFAULT_RETRY_DAY2_OBD_SERVICE_ID);
        Assert.assertEquals(configuration.getRetryDay3ObdPriority(), Initializer.DEFAULT_RETRY_DAY3_OBD_PRIORITY);
        Assert.assertEquals(configuration.getRetryDay3ObdServiceId(), Initializer.DEFAULT_RETRY_DAY3_OBD_SERVICE_ID);

    }

}
