package org.motechproject.nms.mobileacademy.domain;


import java.util.List;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

@Entity
public class QuizContent extends MdsEntity {

    @Field
    private String name;

    @Field
    private String audioFile;

    @Field
    private List<QuestionContent> questions;

    public QuizContent() {

    }

    public QuizContent(String name, String audioFile,
            List<QuestionContent> questions) {
        this.name = name;
        this.audioFile = audioFile;
        this.questions = questions;
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

    public List<QuestionContent> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionContent> questions) {
        this.questions = questions;
    }

}
