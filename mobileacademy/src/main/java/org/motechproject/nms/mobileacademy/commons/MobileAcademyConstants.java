package org.motechproject.nms.mobileacademy.commons;

/**
 * Class containing constants used in mobile academy module.
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

    public static final String DEFAULT_COURSE_NAME = "MobileAcademyCourse";

    public static final String COURSE_ADD = "ADD";

    public static final String COURSE_MOD = "MOD";

    public static final String COURSE_DEL = "DEL";

    // 1 file for Chapter End Menu , 2 files For Lesson: Content and Menu file
    // 3 files For Question: content,correct answer and wrong Answer file
    // 1 file For Quiz Header in each Quiz
    public static final int MIN_FILES_PER_COURSE = NUM_OF_CHAPTERS
            * (1 + NUM_OF_LESSONS * 2 + NUM_OF_QUESTIONS * 3 + 1 + NUM_OF_SCORE_FILES);

    public static final String COURSE_CSV_UPLOAD_SUCCESS_EVENT = "mds.crud.mobileacademy.CourseContentCsv.csv-import.success";

    public static final String COURSE_CSV_UPLOAD_FAILURE_EVENT = "mds.crud.mobileacademy.CourseContentCsv.csv-import.failure";

    public static final String TWO_DIGIT_INTEGER_FORMAT = "%02d";

    public static final String INCONSISTENT_DATA_MESSAGE = "inconsistent data for [ %s ]";

    public static final String INSUFFICIENT_RECORDS_MESSAGE = "Insufficient Records";

    public static final String INCONSISTENT_RECORDS_MESSAGE = "Inconsistent Records";

    public static final String INSUFFICIENT_RECORDS_FOR_MODIFY = "Insufficient Records for modifying the file name for Content Name: [ %s ]. Hence Aborting the Modification process for this content file name.";

    public static final String INCONSISTENT_RECORD_FOR_MODIFY = "Inconsistent Record for modifying the file name for Content Name: [ %s ]. Hence Aborting the Modification process for this content file name.";

    public static final String INSUFFICIENT_RECORDS_FOR_ADD = "Insufficient Records for adding the course for Language Location Code: [ %d ]. Hence Aborting the Course Addition process for this Language location code.";

    public static final String INCONSISTENT_RECORDS_FOR_ADD = "Inconsistent Record for adding the course for Language Location Code: [ %d ]. Hence Aborting the Course Addition process for this Language location code.";

    public static final String INSUFFICIENT_RECORDS_FOR_DEL = "Insufficient Records for deleting the course for Language Location Code: [ %d ]. Hence Aborting the Course Deletion process for this Language location code.";

    public static final String INCOMPLETE_RECORDS_FOR_DEL = "Incomplete Records for deleting the course for Language Location Code: [ %d ]. Hence Aborting the Course Deletion process for this Language location code.";

    public static final String CONTENT_NAME = "Content Name";

    public static final String LOG_MSG_ORIGINAL_FILE_NAME = "original file name: {}";

    public static final String LOG_MSG_NEW_FILE_NAME = "new file name: {}";

    private MobileAcademyConstants() {
    }
}
