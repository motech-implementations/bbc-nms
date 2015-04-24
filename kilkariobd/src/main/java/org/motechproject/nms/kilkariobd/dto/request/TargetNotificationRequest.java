package org.motechproject.nms.kilkariobd.dto.request;

import com.google.gson.annotations.Expose;

/**
 * Entity represents the TargetNotificationRequest.
 */
public class TargetNotificationRequest {

    @Expose
    private String fileName;

    @Expose
    private String checksum;

    @Expose
    private Long recordsCount;

    public TargetNotificationRequest() {}

    public TargetNotificationRequest(String fileName, String checksum, Long recordsCount) {
        this.fileName = fileName;
        this.checksum = checksum;
        this.recordsCount = recordsCount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(Long recordsCount) {
        this.recordsCount = recordsCount;
    }
}
