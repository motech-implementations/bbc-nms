package org.motechproject.nms.mobilekunji.it.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.motechproject.nms.mobilekunji.Initializer;
import org.motechproject.nms.mobilekunji.domain.Configuration;
import org.motechproject.nms.mobilekunji.repository.ConfigurationDataService;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.motechproject.nms.mobilekunji.service.impl.ConfigurationServiceImpl;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This Class models the integration testing for configuration Service
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ConfigurationServiceIT extends BasePaxIT {

    @Inject
    private ConfigurationDataService configurationDataService;

    private ConfigurationService configurationService;

    private Initializer initializer;

    @Before
    public void setUp() {

        configurationService = new ConfigurationServiceImpl(configurationDataService);

        initializer = new Initializer(configurationService);

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
        assertTrue(0 == configuration.getNmsMkCappingType());
        assertTrue(0 == configuration.getNmsMkNationalCapValue());
        assertTrue(1 == configuration.getNmsMkMaxWelcomeMessage());
        assertTrue(2 == configuration.getNmsMkMaxEndofusageMessage());
        assertTrue(1L == configuration.getIndex());

    }



}
