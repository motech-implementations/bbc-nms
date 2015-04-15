package org.motechproject.nms.mobileacademy.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

@Entity
public class ContentLog extends MdsEntity {

    @Field(required = true)
    @UIDisplayable(position = 0)
    private Long callId;

    @Field(required = true)
    @UIDisplayable(position = 1)
    private Integer languageLocationCode;

    @Field(required = true)
    @UIDisplayable(position = 2)
    private String type;

    @Field(required = true)
    @UIDisplayable(position = 3)
    private String contentName;

    @Field(required = true)
    @UIDisplayable(position = 4)
    private String contentFile;

    @Field(required = true)
    @UIDisplayable(position = 5)
    private DateTime startTime;

    @Field(required = true)
    @UIDisplayable(position = 6)
    private DateTime endTime;

    @Field(required = true)
    @UIDisplayable(position = 7)
    private Boolean completionFlag;

    @Field
    @UIDisplayable(position = 8)
    private DateTime courseStartDate;

    @Field
    @UIDisplayable(position = 9)
    private DateTime courseEndDate;

    @Field
    @UIDisplayable(position = 10)
    private Boolean correctAnswerReceived;

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getCompletionFlag() {
        return completionFlag;
    }

    public void setCompletionFlag(Boolean completionFlag) {
        this.completionFlag = completionFlag;
    }

    public DateTime getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(DateTime courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public DateTime getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(DateTime courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public Boolean getCorrectAnswerReceived() {
        return correctAnswerReceived;
    }

    public void setCorrectAnswerReceived(Boolean correctAnswerReceived) {
        this.correctAnswerReceived = correctAnswerReceived;
    }

}
