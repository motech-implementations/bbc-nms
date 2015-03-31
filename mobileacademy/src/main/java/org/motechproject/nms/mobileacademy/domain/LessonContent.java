package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * LessonContent object to refer lesson related data.
 *
 */
@Entity(recordHistory = true)
public class LessonContent extends MdsEntity {

    @Field
    private Integer lessonNumber;

    @Field
    private String name;

    @Field
    private String audioFile;

    /**
     * constructor with 0 arguments.
     */
    public LessonContent() {
    }

    /**
     * constructor with all arguments.
     * 
     * @param lessonNumber lesson number identifier i.e. 1, 2, 3, 4
     * @param name name the meta content i.e. lesson, menu
     * @param audioFile name of the audio file.
     */
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