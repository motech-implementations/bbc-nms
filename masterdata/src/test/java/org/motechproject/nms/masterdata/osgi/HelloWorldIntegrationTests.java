package org.motechproject.nms.masterdata.osgi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * HelloWorld bundle integration tests suite.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        HelloWorldServiceIT.class,
        HelloWorldRecordServiceIT.class
})
public class HelloWorldIntegrationTests {
}
