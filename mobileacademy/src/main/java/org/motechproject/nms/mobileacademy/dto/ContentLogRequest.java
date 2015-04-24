package org.motechproject.nms.mobileacademy.dto;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * ContentLogRequest object contain detailed call information(i.e content name,
 * type , contentFile) and mapped to callDetails API request content parameter.
 *
 */
public class ContentLogRequest {

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
    private String completionFlag;

    @JsonProperty
    private String correctAnswerReceived;

    public void setType(String type) {
        this.type = type;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCompletionFlag(String completionFlag) {
        this.completionFlag = completionFlag;
    }

    public void setCorrectAnswerReceived(String correctAnswerReceived) {
        this.correctAnswerReceived = correctAnswerReceived;
    }

    public String getType() {
        return type;
    }

    public String getContentName() {
        return contentName;
    }

    public String getContentFile() {
        return contentFile;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCompletionFlag() {
        return completionFlag;
    }

    public String getCorrectAnswerReceived() {
        return correctAnswerReceived;
    }

    @Override
    public String toString() {
        return "ContentLogRequest{" + "contentType=" + type + ", contentName='"
                + contentName + '\'' + ", contentFilee='" + contentFile + '\''
                + ", startTime=" + startTime + ", endTime=" + endTime
                + ", completionFlag=" + completionFlag
                + ", correctAnswerReceived=" + correctAnswerReceived + '}';
    }
}
