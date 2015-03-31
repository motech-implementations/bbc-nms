package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * QuestionContent object to refer question related data.
 *
 */
@Entity(recordHistory = true)
public class QuestionContent extends MdsEntity {

    @Field
    private Integer questionNumber;

    @Field
    private String name;

    @Field
    private String audioFile;

    /**
     * constructor with 0 arguments.
     */
    public QuestionContent() {

    }

    /**
     * constructor with all arguments.
     * 
     * @param questionNumber question number identifier i.e. 1, 2, 3, 4
     * @param name name of the meta field i.e question, correctAnswer,
     *            wrongAnswer
     * @param audioFile name of the audio file.
     */
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