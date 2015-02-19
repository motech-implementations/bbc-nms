package org.motechproject.nms.mobileacademy.domain;

/**
 * Created by nitin on 2/13/15.
 */
public class Record {
    private int chapterId;
    private int scoreID;
    private int lessonId;
    private FileType type;
    private int questionId;
    private int answerId;
    private CourseOperator operator;
    private String fileName;

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getScoreID() {
        return scoreID;
    }

    public void setScoreID(int scoreID) {
        this.scoreID = scoreID;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public CourseOperator getOperator() {
        return operator;
    }

    public void setOperator(CourseOperator operator) {
        this.operator = operator;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
}
