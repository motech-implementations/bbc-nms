package org.motechproject.nms.kilkari.osgi;

import org.junit.runner.RunWith;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

/**
 * Verify that HelloWorldService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HelloWorldServiceIT extends BasePaxIT {

    /*@Inject
    private HelloWorldService helloService;

    @Test
    public void testHelloWorldServicePresent() throws Exception {
        assertNotNull(helloService);
        assertNotNull(helloService.sayHello());
    }*/
}
