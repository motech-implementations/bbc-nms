package org.motechproject.nms.mobilekunji.service.ut;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.mobilekunji.repository.ConfigurationDataService;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.motechproject.nms.mobilekunji.service.impl.ConfigurationServiceImpl;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit tests for Activity Service
 */
public class ConfigurationServiceUnitTest {
    @Mock
    private ConfigurationService configurationService;

    @Mock
    private ConfigurationDataService configurationDataService;

    @Before
    public void setup() {
        initMocks(this);
        configurationService = new ConfigurationServiceImpl(configurationDataService);
    }

    @Test
    public void getConfiguration() {

    }
}
