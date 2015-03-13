package org.motechproject.nms.util.ut;

import org.junit.Assert;
import org.junit.Ignore;
import org.motechproject.nms.util.CsvProcessingSummary;

/**
 * Test class to test CsvProcessingSummary
 */
public class CsvProcessingSummaryTest {

    /**
     * Test case to test that failure count and success count
     * are correctly being set in CsvProcessingSummary
     */
    @Ignore
    public void shouldSetValuesInCsvProcessingSummary() {

        CsvProcessingSummary csvProcessingSummary = new CsvProcessingSummary();
        Assert.assertTrue(0 == csvProcessingSummary.getFailureCount());
        Assert.assertTrue(0 == csvProcessingSummary.getSuccessCount());

        csvProcessingSummary.setFailureCount(10);
        csvProcessingSummary.setSuccessCount(90);

        Assert.assertTrue(10 == csvProcessingSummary.getFailureCount());
        Assert.assertTrue(90 == csvProcessingSummary.getSuccessCount());

        csvProcessingSummary.incrementFailureCount();
        Assert.assertTrue(11 == csvProcessingSummary.getFailureCount());

        csvProcessingSummary.incrementSuccessCount();
        Assert.assertTrue(91 == csvProcessingSummary.getSuccessCount());

    }
}
