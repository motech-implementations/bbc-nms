package org.motechproject.nms.mobileacademy.dto;

import org.codehaus.jackson.annotate.JsonProperty;

//This is just a placeholder for save call details API.
//actual implementation would be done in sprint 1504
public class ContentDetails {

    @JsonProperty
    private String type;

    @JsonProperty
    private String contentName;

    @JsonProperty
    private String contentFile;

    @JsonProperty
    private String startTime;

    @JsonProperty
    private String endTime;

    @JsonProperty
    private boolean completionFlag;

    public String getContentType() {
        return type;
    }

    public void setContentType(String type) {
        this.type = type;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isCompletionFlag() {
        return completionFlag;
    }

    public void setCompletionFlag(boolean completionFlag) {
        this.completionFlag = completionFlag;
    }

    @Override
    public String toString() {
        return "ContentDetails{" + "contentType=" + type + ", contentName='"
                + contentName + '\'' + ", contentFilee='" + contentFile + '\''
                + ", startTime=" + startTime + ", endTime=" + endTime
                + ", completionFlag=" + completionFlag + '}';
    }
}
