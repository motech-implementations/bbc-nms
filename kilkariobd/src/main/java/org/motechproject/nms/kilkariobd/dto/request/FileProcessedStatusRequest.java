package org.motechproject.nms.kilkariobd.dto.request;

import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;

public class FileProcessedStatusRequest {

    FileProcessingStatus fileProcessingStatus;
    String fileName;

    public FileProcessedStatusRequest(FileProcessingStatus status, String fileName) {
        this.fileProcessingStatus = status;
        this.fileName = fileName;
    }

    public FileProcessingStatus getCdrFileProcessingStatus() {
        return fileProcessingStatus;
    }

    public void setCdrFileProcessingStatus(FileProcessingStatus cdrFileProcessingStatus) {
        fileProcessingStatus = cdrFileProcessingStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
