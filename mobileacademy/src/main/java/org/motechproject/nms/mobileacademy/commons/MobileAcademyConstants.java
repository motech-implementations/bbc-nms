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

    public static final String UNDERSCORE_DELIMITER = "_";

    public static final String END_MENU = "EndMenu";

    /* Constants used in making the course JSON file */

    public static final String COURSE_KEY_VERSION = "courseVersion";

    public static final String COURSE_KEY_NAME = "name";

    public static final String COURSE_KEY_CONTENT = "content";

    public static final String COURSE_KEY_CHAPTERS = "chapters";

    public static final String COURSE_KEY_LESSONS = "lessons";

    public static final String COURSE_KEY_QUIZ = "quiz";

    public static final String COURSE_KEY_QUESTIONS = "questions";

    public static final String COURSE_KEY_CORRECT_ANSWER_OPTION = "correctAnswerOption";

    public static final String COURSE_KEY_QUESTION = "question";

    public static final String COURSE_KEY_LESSON = "lesson";

    public static final String COURSE_KEY_CORRECT_ANSWER = "correctAnswer";

    public static final String COURSE_KEY_WRONG_ANSWER = "wrongAnswer";

    public static final String COURSE_KEY_MENU = "menu";

    public static final String COURSE_KEY_SCORE = "Score";

    public static final String COURSE_KEY_ID = "id";

    public static final String COURSE_KEY_FILE = "file";

    public static final String COURSE_KEY_FILES = "files";

    public static final String COURSE_KEY_QUIZ_HEADER = "QuizHeader";

    // 1 file for Chapter End Menu , 2 files For Lesson: Content and Menu file
    // 3 files For Question: content,correct answer and wrong Answer file
    // 1 file For Quiz Header in each Quiz
    public static final int MIN_FILES_PER_COURSE = NUM_OF_CHAPTERS
            * (1 + NUM_OF_LESSONS * 2 + NUM_OF_QUESTIONS * 3 + 1 + NUM_OF_SCORE_FILES);

    public static final String COURSE_CSV_UPLOAD_SUCCESS_EVENT = "mds.crud.mobileacademy.CourseContentCsv.csv-import.success";

    public static final String TWO_DIGIT_INTEGER_FORMAT = "%02d";

    public static final String INCONSISTENT_DATA_MESSAGE = "inconsistent data for [ %s ]";

    public static final String INSUFFICIENT_RECORDS_MESSAGE = "Insufficient Records";

    public static final String INCONSISTENT_RECORDS_MESSAGE = "Inconsistent Records";

    public static final String INSUFFICIENT_RECORDS_FOR_MODIFY = "Insufficient Records for modifying the file name/Metadata for Content Name: [ %s ]. Hence Aborting the Modification process for this content name.";

    public static final String INCONSISTENT_RECORD_FOR_MODIFY = "Inconsistent Record for modifying the file name/Metadata for Content Name: [ %s ]. Hence Aborting the Modification process for this content name.";

    public static final String INSUFFICIENT_RECORDS_FOR_ADD = "Insufficient Records for adding the course for Language Location Code: [ %d ]. Hence Aborting the Course Addition process for this Language location code.";

    public static final String INCONSISTENT_RECORDS_FOR_ADD = "Inconsistent Record for adding the course for Language Location Code: [ %d ]. Hence Aborting the Course Addition process for this Language location code.";

    public static final String INSUFFICIENT_RECORDS_FOR_DEL = "Insufficient Records for deleting the course for Language Location Code: [ %d ]. Hence Aborting the Course Deletion process for this Language location code.";

    public static final String INCOMPLETE_RECORDS_FOR_DEL = "Incomplete Records for deleting the course for Language Location Code: [ %d ]. Hence Aborting the Course Deletion process for this Language location code.";

    public static final String CONTENT_NAME = "Content Name";

    public static final String INVALID_CONTENT_TYPE = "Invalid Content Type for [ %s ]";

    public static final String LOG_MSG_ORIGINAL_FILE_NAME = "original file name: {}";

    public static final String LOG_MSG_NEW_FILE_NAME = "new file name: {}";

    public static final String NO_COURSE_PRESENT = "Course not available";

    public static final String COURSE_UPLOAD_ONGOING = "Course upload ongoing";

    public final static int MILLIS_TO_SEC_CONVERTER = 1000;

    /* Mobile academy service configuration parameters -Start */

    public static final Long CONFIG_DEFAULT_RECORD_INDEX = 1l;

    public static final Integer CONFIG_DEFAULT_CAPPING_TYPE = 0;

    public static final Integer CONFIG_DEFAULT_MAX_ALLOW_END_USAGE_PROMPT = 2;

    public static final Integer CONFIG_DEFAULT_COURSE_QUALIFYING_SCORE = 22;

    public static final Integer CONFIG_DEFAULT_NATIONAL_CAP_VALUE = 0;

    public static final Integer CONFIG_DEFAULT_LANGUAGE_LOCATION_CODE = 1;

    public static final String CONFIG_DEFAULT_SMS_SENDER_ADDRESS = "DEFAULT_SMS_SENDER_ADDRESS";

    /* Mobile academy service configuration parameters -End */

    public static final Integer MAX_ALLOWED_USAGE_PULSE_FOR_UNCAPPED = -1;

    public static final String UNKNOWN_CIRCLE_CODE = "99";

    public static final String EMPTY_JSON = "{}";

    public static final Integer DEFAULT_CURRENT_USAGE_IN_PULSES = 0;

    public static final Integer DEFAULT_END_OF_USAGE_PROMPT_COUNTER = 0;

    /* Parameters for controllers */

    public static final String REQUEST_PARAM_CALLING_NUMBER = "callingNumber";

    public static final String REQUEST_PARAM_OPERATOR = "operator";

    public static final String REQUEST_PARAM_CIRCLE = "circle";

    public static final String REQUEST_PARAM_CALL_ID = "callId";

    public static final String REQUEST_PARAM_LLC = "languageLocationCode";

    public static final String BOOKMARK_ID = "bookmarkId";

    public static final String BOOKMARK = "bookmark";

    public static final String COURSE_COMPLETED = "COURSE_COMPLETED";

}
