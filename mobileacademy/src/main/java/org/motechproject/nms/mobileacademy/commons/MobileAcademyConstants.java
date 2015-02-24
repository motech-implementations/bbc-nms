package org.motechproject.nms.mobileacademy.commons;

/**
 * Class containing constants used in mobile academy.
 *
 */
public class MobileAcademyConstants {

    public static final String OK = "OK";

    public static final int NUM_OF_CHAPTERS = 11;

    public static final int NUM_OF_LESSONS = 4;

    public static final int NUM_OF_QUESTIONS = 4;

    public static final int NUM_OF_SCORES = 4;

    public static final int NUM_OF_SCORE_FILES = 5;

    public static final String CHAPTER = "Chapter";

    public static final String LESSON = "Lesson";

    public static final String QUIZ = "Quiz";

    public static final String QUESTION = "Question";

    public static final String CONTENT_MENU = "menu";

    public static final String CONTENT_LESSON = "lesson";

    public static final String CONTENT_QUIZ_HEADER = "quizHeader";

    public static final String CONTENT_QUESTION = "question";

    public static final String CONTENT_CORRECT_ANSWER = "correctAnswer";

    public static final String CONTENT_WRONG_ANSWER = "wrongAnswer";

    public static final String SCORE = "score";

    public static final String DEFAUlT_COURSE_NAME = "MobileAcademyCourse";

    public static final String COURSE_ADD = "ADD";

    public static final String COURSE_MOD = "MOD";

    public static final String COURSE_DEL = "DEL";

    public static final int minNoOfEntriesInCSVPerCourse = NUM_OF_CHAPTERS * (1 // For
                                                                                // Chapter
                                                                                // End
                                                                                // Menu
            + NUM_OF_LESSONS * 2 // For Lesson Content and Menu file
            + NUM_OF_QUESTIONS * 3 // For Question content,correct answer and
                                   // wrong Answer file
            + 1 // For Quiz Header in each Quiz
    + NUM_OF_SCORE_FILES);

    public static final String COURSE_CSV_UPLOAD_SUCCESS = "mds.crud.mobileacademymodule.CourseRawContent.csv-import.success";

    public static final String COURSE_CSV_UPLOAD_FAILED = "mds.crud.mobileacademymodule.CourseRawContent.csv-import.failed";

    private MobileAcademyConstants() {
    }
}
