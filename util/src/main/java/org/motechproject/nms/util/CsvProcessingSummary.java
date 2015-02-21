package org.motechproject.nms.util;

/**
 * This class contains the count of records successfully uploaded
 * and the count of records failed to upload after processing of
 * bulk upload file(csv file)
 */
public class CsvProcessingSummary {

    private int successCount;

    private int failureCount;

    public CsvProcessingSummary(Integer successCount, Integer failureCount) {
        this.successCount = successCount;
        this.failureCount = failureCount;
    }

    public CsvProcessingSummary() {
        this.successCount = 0;
        this.failureCount = 0;
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

    public void incrementSuccessCount() {

        successCount++;
    }
    public void incrementFailureCount() {

        failureCount++;
    }
}
