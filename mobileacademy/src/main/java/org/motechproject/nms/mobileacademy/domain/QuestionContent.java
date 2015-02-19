package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

@Entity
public class QuestionContent extends MdsEntity {

    @Field
    private Integer questionNumber;

    @Field
    private String name;

    @Field
    private String audioFile;

    public QuestionContent() {

    }

    public QuestionContent(Integer questionNumber, String name, String audioFile) {
        this.questionNumber = questionNumber;
        this.name = name;
        this.audioFile = audioFile;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
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
