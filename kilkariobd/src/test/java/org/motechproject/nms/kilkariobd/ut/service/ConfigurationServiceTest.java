package org.motechproject.nms.kilkariobd.ut.service;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.nms.kilkariobd.repository.ConfigurationDataService;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.service.impl.ConfigurationServiceImpl;

import static org.mockito.MockitoAnnotations.initMocks;

public class ConfigurationServiceTest {

    @Mock
    private ConfigurationDataService configurationDataService;

    @InjectMocks
    ConfigurationService configurationService = new ConfigurationServiceImpl();

    @Before
    public void init() {

        initMocks(this);
    }

    @Test
    public void shouldCheckIfConfigurationIsPresent() {

        Mockito.when(configurationDataService.count()).thenReturn(1L);
        Assert.assertTrue(configurationService.isConfigurationPresent());

    }

    @Test
    public void shouldReturnFalseCheckIfConfigurationIsNotPresent() {

        Mockito.when(configurationDataService.count()).thenReturn(0L);
        Assert.assertFalse(configurationService.isConfigurationPresent());
    }
}
