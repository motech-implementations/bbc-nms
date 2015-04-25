package org.motechproject.nms.kilkariobd.it.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.nms.kilkariobd.it.initializer.InitializerIT;

/**
 * Parent IT class to run all the individual IT cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ InitializerIT.class })
public class IntegrationTests {
}
