package org.motechproject.nms.util.it;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.util.constants.Constants;
import org.motechproject.nms.util.initializer.Initializer;
import org.motechproject.osgi.web.domain.LogMapping;
import org.motechproject.osgi.web.service.ServerLogService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.List;

/**
 * This class contains test case to test Initializer for
 * initializing the default log level for all the modules to INFO.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)

public class InitializerIT extends BasePaxIT {

    @Inject
    private ServerLogService serverLogService;

    private Initializer initializer;

    @Before
    public void setUp() {
        initializer = new Initializer(serverLogService);
    }

    @After
    public void tearDown() {
        serverLogService.removeLogger(Constants.NMS_PACKAGE_NAME);
    }


    /**
     * This test case tests that Initializer is correctly setting
     * the log level for all the modules in nms to Info.
     */
    @Test
    public void shouldInitializeLogLevelToInfo() {

        List<LogMapping> listOfLoggers = serverLogService.getLogLevels();

        for(LogMapping logger : listOfLoggers) {

            if(StringUtils.contains(logger.getLogName(), Constants.NMS_PACKAGE_NAME)) {
                Assert.assertEquals(Constants.NMS_DEFAULT_LOG_LEVEL, logger.getLogLevel());
            }
        }
    }
}
