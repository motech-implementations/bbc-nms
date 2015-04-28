package org.motechproject.nms.frontlineworker.it.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.motechproject.nms.frontlineworker.Initializer;
import org.motechproject.nms.frontlineworker.domain.Configuration;
import org.motechproject.nms.frontlineworker.repository.ConfigurationDataService;
import org.motechproject.nms.frontlineworker.service.ConfigurationService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * This class is used to test(IT) the operations of ConfigurationService.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ConfigurationServiceIT extends BasePaxIT {

    @Inject
    private ConfigurationDataService configurationDataService;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private MotechSchedulerService motechSchedulerService;

    private Initializer initializer;

    @Before
    public void setUp() {


        initializer = new Initializer(configurationService, motechSchedulerService);

        assertNotNull(configurationDataService);
        assertNotNull(configurationService);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDefaultValues() {
        Configuration configuration;
        initializer.initializeConfiguration();
        configuration = configurationService.getConfiguration();

        assertNotNull(configuration);
        assertTrue(1L == configuration.getIndex());
        assertTrue(42 == configuration.getPurgeDate());
        assertEquals("0 1 0 * * ?", configuration.getDeletionEventCronExpression());
    }
}
