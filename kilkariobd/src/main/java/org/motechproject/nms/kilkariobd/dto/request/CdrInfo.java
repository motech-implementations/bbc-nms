package org.motechproject.nms.kilkariobd.dto.request;

import org.codehaus.jackson.annotate.JsonProperty;

public class CdrInfo {
    @JsonProperty
    String cdrFile;

    @JsonProperty
    String cdrChecksum;

    @JsonProperty
    Long recordsCount;

    public String getCdrFile() {
        return cdrFile;
    }

    public void setCdrFile(String cdrFile) {
        this.cdrFile = cdrFile;
    }

    public String getCdrChecksum() {
        return cdrChecksum;
    }

    public void setCdrChecksum(String cdrChecksum) {
        this.cdrChecksum = cdrChecksum;
    }

    public Long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(Long recordsCount) {
        this.recordsCount = recordsCount;
    }
}
