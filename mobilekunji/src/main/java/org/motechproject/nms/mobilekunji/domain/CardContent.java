package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data for CardContent records
 */

@Entity(recordHistory = true)
public class CardContent extends MdsEntity {

    @Field
    private Integer mkcardNumber;

    @Field
    private String contentName;

    @Field
    private String audioFileName;

    @Field
    private Integer startTime;

    @Field
    private Integer endTime;


    public Integer getMkcardNumber() {
        return mkcardNumber;
    }

    public void setMkcardNumber(Integer mkcardNumber) {
        this.mkcardNumber = mkcardNumber;
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

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "CardContent{" +
                "mkcardNumber=" + mkcardNumber +
                ", contentName='" + contentName + '\'' +
                ", audioFileName='" + audioFileName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
