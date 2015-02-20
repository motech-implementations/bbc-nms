package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;


@Entity
public class LessonContent extends MdsEntity {

    @Field
    private Integer lessonNumber;

    @Field
    private String name;

    @Field
    private String audioFile;

    public LessonContent() {
    }

    public LessonContent(Integer lessonNumber, String name, String audioFile) {
        this.lessonNumber = lessonNumber;
        this.name = name;
        this.audioFile = audioFile;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

}
