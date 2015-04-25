package org.motechproject.nms.mobileacademy.it.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.nms.mobileacademy.event.handler.it.CourseUploadCsvHandlerIT;
import org.motechproject.nms.mobileacademy.service.it.CourseBookmarkServiceIT;
import org.motechproject.nms.mobileacademy.service.it.CourseServiceIT;
import org.motechproject.nms.mobileacademy.service.it.RecordsProcessServiceIT;
import org.motechproject.nms.mobileacademy.service.it.UserDetailsServiceIT;

/**
 * Parent IT class to run all the individual IT cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ CourseBookmarkServiceIT.class, CourseServiceIT.class,
        RecordsProcessServiceIT.class, UserDetailsServiceIT.class,
        CourseUploadCsvHandlerIT.class })
public class IntegrationTests {
}
