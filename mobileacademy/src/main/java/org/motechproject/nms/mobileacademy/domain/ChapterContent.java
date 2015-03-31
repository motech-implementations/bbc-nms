package org.motechproject.nms.mobileacademy.domain;

import java.util.List;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * ChapterContent object to refer chapter related data.
 *
 */
@Entity(recordHistory = true)
public class ChapterContent extends MdsEntity {

    @Field
    private Integer chapterNumber;

    @Field
    private String name;

    @Field
    private String audioFile;

    @Field
    @Cascade(delete = true)
    private List<LessonContent> lessons;

    @Field
    @Cascade(delete = true)
    private List<ScoreContent> scoreContents;

    @Cascade(delete = true)
    @Field
    private QuizContent quiz;

    /**
     * constructor with 0 arguments.
     */
    public ChapterContent() {

    }

    /**
     * constructor with all arguments.
     * 
     * @param chapterNumber chapter number identifier i.e 1, 2, ..11
     * @param name chapter meta data field i.e menu
     * @param audioFile name of the audio file
     * @param lessons list of lesson content
     * @param scoreContents list of score
     * @param quiz quiz object related to chapter
     */
    public ChapterContent(Integer chapterNumber, String name, String audioFile,
            List<LessonContent> lessons, List<ScoreContent> scoreContents,
            QuizContent quiz) {
        this.chapterNumber = chapterNumber;
        this.name = name;
        this.audioFile = audioFile;
        this.lessons = lessons;
        this.scoreContents = scoreContents;
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

    public List<ScoreContent> getScores() {
        return scoreContents;
    }

    public void setScores(List<ScoreContent> scoreContents) {
        this.scoreContents = scoreContents;
    }

    public QuizContent getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizContent quiz) {
        this.quiz = quiz;
    }

}