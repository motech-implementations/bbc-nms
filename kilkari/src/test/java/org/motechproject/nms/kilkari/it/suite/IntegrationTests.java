package org.motechproject.nms.kilkari.it.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.nms.kilkari.it.event.handler.CsvMctsChildHandlerTestIT;
import org.motechproject.nms.kilkari.it.event.handler.ContentUploadCsvHandlerIT;
import org.motechproject.nms.kilkari.it.event.handler.CsvMctsMotherHandlerIT;
import org.motechproject.nms.kilkari.it.initializer.InitializerIT;
import org.motechproject.nms.kilkari.it.web.SubscriptionControllerIT;

/**
 * Parent IT class to run all the individual IT cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ CsvMctsMotherHandlerIT.class,
        ContentUploadCsvHandlerIT.class, CsvMctsChildHandlerTestIT.class,
        InitializerIT.class, SubscriptionControllerIT.class })
public class IntegrationTests {
}
