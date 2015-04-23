package org.motechproject.nms.frontlineworker.it.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.nms.frontlineworker.it.event.handler.FrontlineWorkerHandlerIT;
import org.motechproject.nms.frontlineworker.it.event.handler.UserProfileDetailsImplIT;

/**
 * Parent IT class to run all the individual IT cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ FrontlineWorkerHandlerIT.class,
        UserProfileDetailsImplIT.class })
public class IntegrationTests {
}
