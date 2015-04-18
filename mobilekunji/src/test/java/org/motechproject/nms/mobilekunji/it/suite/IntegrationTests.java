package org.motechproject.nms.mobilekunji.it.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.nms.mobilekunji.it.event.handler.ContentUploadCsvHandlerIT;
import org.motechproject.nms.mobilekunji.it.service.ConfigurationServiceIT;
import org.motechproject.nms.mobilekunji.it.web.CallerDataControllerIT;

/**
 * Parent IT class to run all the individual IT cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ContentUploadCsvHandlerIT.class,
        ConfigurationServiceIT.class, CallerDataControllerIT.class})
public class IntegrationTests {
}
