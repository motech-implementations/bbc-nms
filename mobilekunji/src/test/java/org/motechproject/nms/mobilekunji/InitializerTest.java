package org.motechproject.nms.mobilekunji;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;

import static org.mockito.MockitoAnnotations.initMocks;

public class InitializerTest {


    @Mock
    private ConfigurationService configurationService;


    Initializer initializer;

    @Before
    public void setUp(){
        initMocks(this);
        initializer = new Initializer(configurationService);
    }

    @Test
    public void testInitializeConfiguration() throws Exception {
        initializer.initializeConfiguration();
    }
}