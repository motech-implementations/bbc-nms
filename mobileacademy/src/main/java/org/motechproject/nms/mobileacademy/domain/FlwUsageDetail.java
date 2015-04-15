package org.motechproject.nms.mobileacademy.domain;

import javax.jdo.annotations.Unique;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

/**
 * FlwUsageDetail object refer to mobile academy usage by front line worker
 *
 */
@Entity
public class FlwUsageDetail extends MdsEntity {

    // mapped to PK of FrontLineWorker table
    @Unique
    @Field(required = true)
    @UIDisplayable(position = 0)
    private Long flwId;

    @Field(required = true)
    @UIDisplayable(position = 1)
    private Long msisdn;

    @Field(required = true)
    @UIDisplayable(position = 2)
    private Integer currentUsageInPulses;

    @Field(required = true)
    @UIDisplayable(position = 3)
    private Integer endOfUsagePromptCounter;

    @Field
    @UIDisplayable(position = 4)
    private DateTime courseStartDate;

    @Field
    @UIDisplayable(position = 5)
    private DateTime courseEndDate;

    @Field(required = true)
    @UIDisplayable(position = 6)
    private DateTime lastAccessTime;

    public Long getFlwId() {
        return flwId;
    }

    public void setFlwId(Long flwId) {
        this.flwId = flwId;
    }

    public Long getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(Long msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getCurrentUsageInPulses() {
        return currentUsageInPulses;
    }

    public void setCurrentUsageInPulses(Integer currentUsageInPulses) {
        this.currentUsageInPulses = currentUsageInPulses;
    }

    public Integer getEndOfUsagePromptCounter() {
        return endOfUsagePromptCounter;
    }

    public void setEndOfUsagePromptCounter(Integer endOfUsagePromptCounter) {
        this.endOfUsagePromptCounter = endOfUsagePromptCounter;
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

    public DateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

}
