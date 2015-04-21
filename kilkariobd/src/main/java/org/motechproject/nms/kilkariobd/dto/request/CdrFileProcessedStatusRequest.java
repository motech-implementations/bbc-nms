package org.motechproject.nms.kilkariobd.dto.request;

public class CdrFileProcessedStatusRequest {

    Integer CdrFileProcessingStatus;
    String fileName;

    public CdrFileProcessedStatusRequest(Integer status, String fileName) {
        this.CdrFileProcessingStatus = status;
        this.fileName = fileName;
    }

    public Integer getCdrFileProcessingStatus() {
        return CdrFileProcessingStatus;
    }

    public void setCdrFileProcessingStatus(Integer cdrFileProcessingStatus) {
        CdrFileProcessingStatus = cdrFileProcessingStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
