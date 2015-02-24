package org.motechproject.nms.mobileacademy.service;

import java.util.List;

import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.Score;

/**
 * Service interface contains APIs to perform course populate operations in
 * mtraining and content tables.
 *
 */
public interface CoursePopulateService {

    /**
     *
     * populate course static Data in mtraining.
     *
     */
    public void populateMtrainingCourseData();

    /**
     * find Course State
     * 
     * @return Course state enum contain course state
     */
    public CourseUnitState findCourseState();

    /**
     * update Course State
     * 
     * @param courseUnitState Course state enum contain course state
     */
    public void updateCourseState(CourseUnitState courseUnitState);

    /**
     * update Correct Answer in mtraining
     * 
     * @param chapterName refer to chapterIdentifier i.e Chapter01,Chapter02
     * @param questionName refer to question identifier i.e
     *            Question01,Question02
     * @param answer refer to answer identifier i.e 1,2
     */
    public void updateCorrectAnswer(String chapterName, String questionName,
            String answer);

    public List<ChapterContent> getAllChapterContents();

    public LessonContent getLessonContent(int chapterId, int lessonId,
            String type);

    public QuestionContent getQuestionContent(int chapterId, int questionId,
            String type);

    public Score getScore(int chapterId, int scoreId, String type);

    public ChapterContent getChapterContent(int chapterId, String type);

    public QuizContent getQuizContent(int chapterId, String type);

    public void setLessonContent(int chapterId, int lessonId, String type,
            String fileName);

    public void setQuestionContent(int chapterId, int questionId, String type,
            String fileName);

    public void setScore(int chapterId, int scoreId, String type,
            String fileName);

    public void setChapterContent(int chapterId, String type, String fileName);

    public void setQuizContent(int chapterId, String type, String fileName);
}
