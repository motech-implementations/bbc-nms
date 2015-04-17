package org.motechproject.nms.kilkariobd.dto.request;

import org.codehaus.jackson.annotate.JsonProperty;

public class CdrNotificationRequest {
    @JsonProperty
    String fileName;

    @JsonProperty
    CdrInfo cdrSummary;

    @JsonProperty
    CdrInfo cdrDetail;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public CdrInfo getCdrSummary() {
        return cdrSummary;
    }

    public void setCdrSummary(CdrInfo cdrSummary) {
        this.cdrSummary = cdrSummary;
    }

    public CdrInfo getCdrDetail() {
        return cdrDetail;
    }

    public void setCdrDetail(CdrInfo cdrDetail) {
        this.cdrDetail = cdrDetail;
    }

    public void validateMandatoryParameters() {}

}
