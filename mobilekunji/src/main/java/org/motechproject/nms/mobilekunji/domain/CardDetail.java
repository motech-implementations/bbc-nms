package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data for CardDetail records
 */

@Entity(recordHistory = true)
public class CardDetail extends MdsEntity {

    @Field
    private String mkCardNumber;

    @Field
    private String contentName;

    @Field
    private String audioFileName;

    @Field
    private Long startTime;

    @Field
    private Long endTime;

    public String getMkCardNumber() {
        return mkCardNumber;
    }

    public void setMkCardNumber(String mkCardNumber) {
        this.mkCardNumber = mkCardNumber;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "CardDetail{" +
                "mkcardNumber=" + mkCardNumber +
                ", contentName='" + contentName + '\'' +
                ", audioFileName='" + audioFileName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
