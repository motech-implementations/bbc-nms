package org.motechproject.nms.util;

/**
 * Created by abhishek on 20/2/15.
 */
public class CsvProcessingSummary {

    private Integer successCount;

    private Integer failureCount;

    public CsvProcessingSummary(Integer successCount, Integer failureCount) {
        this.successCount = successCount;
        this.failureCount = failureCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }
}
