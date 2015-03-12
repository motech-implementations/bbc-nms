package org.motechproject.nms.mobileacademy.service;

import java.util.List;

import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.UserDetailsDTO;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;

/**
 * Service interface contains APIs to perform course populate operations in
 * mtraining and content tables.
 */
public interface CoursePopulateService {

    /**
     *
     * populate course static Data in mtraining.
     * 
     * @param userDetailsDTO object contain user details
     * @return
     *
     */
    public Course populateMtrainingCourseData(UserDetailsDTO userDetailsDTO);

    /**
     *
     * To get course static Data in mtraining.
     * 
     * @return
     *
     */
    public Course getMtrainingCourse();

    /**
     * find Course State
     * 
     * @return Course state enum contain course state and it can be null;
     */
    public CourseUnitState findCourseState();

    /**
     * update Course State
     * 
     * @param courseUnitState Course state enum contain course state
     * @param userDetailsDTO object contain user details
     */
    public void updateCourseState(CourseUnitState courseUnitState,
            UserDetailsDTO userDetailsDTO);

    /**
     * update Correct Answer in mtraining
     * 
     * @param chapterName refer to chapterIdentifier i.e Chapter01,Chapter02
     * @param questionName refer to question identifier i.e
     *            Question01,Question02
     * @param answer refer to answer identifier i.e 1,2
     * @param userDetailsDTO object contain user details
     */
    public void updateCorrectAnswer(String chapterName, String questionName,
            String answer, UserDetailsDTO userDetailsDTO);

    /**
     * This returns all the chapterContents saved for a course.
     * 
     * @return List having the chapter content Objects
     */
    public List<ChapterContent> getAllChapterContents();

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param lessonNo refers to index of lesson in a chapter.. 1,2..4
     * @param type refers to the type of lesson files: "Lesson" for lesson
     *            content file or "menu" for end menu file of lesson
     * @return LessonContent Object having the audio File
     */
    public LessonContent getLessonContent(List<ChapterContent> chapterContents,
            int chapterNo, int lessonNo, String type);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param questionNo refers to index of question in a chapter.. 1,2..4
     * @param type refers to type of file stored for questions: "question" for
     *            question content file or "correctAnswer" for correct answer
     *            audio file of question or "wrongAnswer" for wrong answer audio
     *            file of question
     * @return QuestionContent Object having the audio file
     */
    public QuestionContent getQuestionContent(
            List<ChapterContent> chapterContents, int chapterNo,
            int questionNo, String type);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param scoreNo refers to the score in a quiz.. 0,1,2..4
     * @param type refers to type of file stored for scores: "score"
     * @return Score Object having the audio file to be played for a score
     */
    public ScoreContent getScore(List<ChapterContent> chapterContents,
            int chapterNo, int scoreNo, String type);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param type refers to type of file stored for chapters: "menu"
     * @return refers to ChapterContent Object having the audio file to be
     *         played for end-menu of chapter
     */
    public ChapterContent getChapterContent(
            List<ChapterContent> chapterContents, int chapterNo, String type);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param type refers to type of file stored for quiz: "quizHeader"
     * @return refers to QuizContent Object having the audio file to be played
     *         for quiz header
     */
    public QuizContent getQuizContent(List<ChapterContent> chapterContents,
            int chapterNo, String type);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param lessonNo refers to index of lesson in a chapter.. 1,2..4
     * @param type refers to the type of lesson files: "Lesson" for lesson
     *            content file or "menu" for end menu file of lesson
     * @param fileName name of audio file for lesson content to be updated
     * @param userDetailsDTO object contain user details
     */
    public void setLessonContent(int chapterNo, int lessonNo, String type,
            String fileName, UserDetailsDTO userDetailsDTO);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param questionNo refers to index of question in a chapter.. 1,2..4
     * @param type refers to type of file stored for questions: "question" for
     *            question content file or "correctAnswer" for correct answer
     *            audio file of question or "wrongAnswer" for wrong answer audio
     *            file of question
     * @param fileName name of audio file for lesson content to be updated
     * @param userDetailsDTO object contain user details
     */
    public void setQuestionContent(int chapterNo, int questionNo, String type,
            String fileName, UserDetailsDTO userDetailsDTO);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param scoreNo refers to the score in a quiz.. 0,1,2..4
     * @param type refers to type of file stored for scores: "score"
     * @param fileName name of audio file for lesson content to be updated
     * @param userDetailsDTO object contain user details
     */
    public void setScore(int chapterNo, int scoreNo, String type,
            String fileName, UserDetailsDTO userDetailsDTO);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param type refers to type of file stored for chapters: "menu"
     * @param fileName name of audio file for lesson content to be updated
     * @param userDetailsDTO object contain user details
     */
    public void setChapterContent(int chapterNo, String type, String fileName,
            UserDetailsDTO userDetailsDTO);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param type refers to type of file stored for quiz: "quizHeader"
     * @param fileName name of audio file for lesson content to be updated
     * @param userDetailsDTO object contain user details
     */
    public void setQuizContent(int chapterNo, String type, String fileName,
            UserDetailsDTO userDetailsDTO);

    /**
     * @param chapterNo refers to index of chapter.. 1,2,3..11
     * @param questionNo refers to index of question in a chapter.. 1,2..4
     * @return correct answer option of the question
     */
    public int getCorrectAnswerOption(Integer chapterNo, Integer questionNo);
}
