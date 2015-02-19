package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import java.util.List;

@Entity
public class ChapterContent extends MdsEntity {

    @Field
    private Integer chapterNumber;

    @Field
    private String name;

    @Field
    private String audioFile;

    @Field
    private List<LessonContent> lessons;

    @Field
    private List<Score> scores;

    @Field
    private QuizContent quiz;

    public ChapterContent() {

    }

    public ChapterContent(Integer chapterNumber, String name, String audioFile,
            List<LessonContent> lessons, List<Score> scores, QuizContent quiz) {
        this.chapterNumber = chapterNumber;
        this.name = name;
        this.audioFile = audioFile;
        this.lessons = lessons;
        this.scores = scores;
        this.quiz = quiz;
    }

    public Integer getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(Integer chapterNumber) {
        this.chapterNumber = chapterNumber;
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

    public List<LessonContent> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonContent> lessons) {
        this.lessons = lessons;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public QuizContent getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizContent quiz) {
        this.quiz = quiz;
    }

}
