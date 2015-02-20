package org.motechproject.nms.util;

/**
 * Created by abhishek on 20/2/15.
 */
public class CsvProcessingSummary {

    private int successCount;

    private int failureCount;

    public CsvProcessingSummary(int successCount, int failureCount) {
        this.successCount = successCount;
        this.failureCount = failureCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }
}
