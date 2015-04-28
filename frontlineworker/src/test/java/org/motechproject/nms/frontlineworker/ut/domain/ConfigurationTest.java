package org.motechproject.nms.frontlineworker.ut.domain;

import org.junit.Test;
import org.motechproject.nms.frontlineworker.domain.Configuration;

import static org.junit.Assert.assertTrue;

/**
 * This class performs the UT of Configuration records.
 */
public class ConfigurationTest {

    Configuration configuration = new Configuration();

    @Test
    public void testShouldSetWithIndex() {

        configuration.setIndex(1L);
        assertTrue(1 == configuration.getIndex());
    }

    @Test
    public void testShouldSetWithPurgeDate() {

        configuration.setPurgeDate(42);
        assertTrue(42 == configuration.getPurgeDate());
    }

    @Test
    public void testShouldSetWithDeletionEventCronExpression() {

        configuration.setDeletionEventCronExpression("0 1 0 * * ?");
        assertTrue("0 1 0 * * ?" == configuration.getDeletionEventCronExpression());
    }
}
