package org.motechproject.nms.mobileacademy.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.motechproject.nms.mobileacademy.commons.ContentType;
import org.motechproject.nms.mobileacademy.commons.CourseFlag;
import org.motechproject.nms.mobileacademy.commons.FileType;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.commons.OperatorDetails;
import org.motechproject.nms.mobileacademy.commons.Record;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Record Process Helper contains static methods used by csv upload processor
 * service.
 *
 */
public class RecordsProcessHelper {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(RecordsProcessHelper.class);

    /*
     * This function adds the records into a Map having LLC of record as key
     * 
     * The map will be process afterwards for processing ADD Records
     */
    public static void putRecordInAddMap(
            Map<Integer, List<CourseContentCsv>> mapForAddRecords,
            CourseContentCsv courseContentCsv) {
        int languageLocCode = Integer.parseInt(courseContentCsv
                .getLanguageLocationCode());
        if (mapForAddRecords.containsKey(languageLocCode)) {
            mapForAddRecords.get(languageLocCode).add(courseContentCsv);
        } else {
            List<CourseContentCsv> listOfRecords = new ArrayList<CourseContentCsv>();
            listOfRecords.add(courseContentCsv);
            mapForAddRecords.put(languageLocCode, listOfRecords);
        }
    }

    /*
     * This function adds the records into a Map having contentName of record as
     * key
     * 
     * The map will be process afterwards for processing "MOD"ify Records
     */
    public static void putRecordInModifyMap(
            Map<String, List<CourseContentCsv>> mapForModifyRecords,
            CourseContentCsv courseContentCsv) {
        String key = courseContentCsv.getContentName().toUpperCase();
        if (mapForModifyRecords.containsKey(key)) {
            mapForModifyRecords.get(key).add(courseContentCsv);
        } else {
            List<CourseContentCsv> listOfRecords = new ArrayList<CourseContentCsv>();
            listOfRecords.add(courseContentCsv);
            mapForModifyRecords.put(key, listOfRecords);
        }
    }

    /*
     * This function is used to create the static course data in the content
     * tables.
     */
    public static List<ChapterContent> createChapterContentPrototype() {
        List<ChapterContent> listOfChapters = new ArrayList<ChapterContent>();

        for (int chapterCount = 1; chapterCount <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCount++) {
            List<LessonContent> lessons = createListOfLessons();
            List<QuestionContent> questions = createListOfQuestions();
            List<ScoreContent> scoreContents = createListOfScores();
            QuizContent quiz = new QuizContent(
                    MobileAcademyConstants.CONTENT_QUIZ_HEADER, null, questions);
            ChapterContent chapterContent = new ChapterContent(chapterCount,
                    MobileAcademyConstants.CONTENT_MENU, null, lessons,
                    scoreContents, quiz);
            listOfChapters.add(chapterContent);
        }

        LOGGER.info("Course Prototype created in content table");
        return listOfChapters;
    }

    /*
     * This function creates theList of Score content files to be included in a
     * chapter
     */
    public static List<ScoreContent> createListOfScores() {
        List<ScoreContent> scoreList = new ArrayList<>();
        for (int scoreCount = 0; scoreCount <= MobileAcademyConstants.NUM_OF_SCORES; scoreCount++) {
            ScoreContent scoreContent = new ScoreContent(
                    MobileAcademyConstants.SCORE
                            + String.format(
                                    MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                    scoreCount), null);
            scoreList.add(scoreContent);
        }
        return scoreList;
    }

    /*
     * This function creates the List of QuestionContent files to be included in
     * a quiz of chapter
     */
    public static List<QuestionContent> createListOfQuestions() {
        List<QuestionContent> questionList = new ArrayList<>();
        for (int questionCount = 1; questionCount <= MobileAcademyConstants.NUM_OF_QUESTIONS; questionCount++) {
            QuestionContent questionContent = new QuestionContent(
                    questionCount, MobileAcademyConstants.CONTENT_QUESTION,
                    null);
            questionList.add(questionContent);
            questionContent = new QuestionContent(questionCount,
                    MobileAcademyConstants.CONTENT_CORRECT_ANSWER, null);
            questionList.add(questionContent);
            questionContent = new QuestionContent(questionCount,
                    MobileAcademyConstants.CONTENT_WRONG_ANSWER, null);
            questionList.add(questionContent);
        }
        return questionList;
    }

    /*
     * This function creates the List of LessonContent files to be included in a
     * chapter
     */
    public static List<LessonContent> createListOfLessons() {
        List<LessonContent> lessonList = new ArrayList<>();
        for (int lessonCount = 1; lessonCount <= MobileAcademyConstants.NUM_OF_LESSONS; lessonCount++) {
            LessonContent lessonContent = new LessonContent(lessonCount,
                    MobileAcademyConstants.CONTENT_MENU, null);
            lessonList.add(lessonContent);
            lessonContent = new LessonContent(lessonCount,
                    MobileAcademyConstants.CONTENT_LESSON, null);
            lessonList.add(lessonContent);
        }
        return lessonList;
    }

    /*
     * This function does the schema level validation on a CourseContentCsv
     * Record. In case a erroneous field, throws DataValidationException
     */
    public static void validateSchema(CourseContentCsv courseContentCsv)
            throws DataValidationException {

        ParseDataHelper.validateAndParseInt("Content ID",
                courseContentCsv.getContentId(), true);

        ParseDataHelper.validateAndParseInt("Language Location Code",
                courseContentCsv.getLanguageLocationCode(), true);

        ParseDataHelper.validateAndParseString("Content Name",
                courseContentCsv.getContentName(), true);

        ParseDataHelper.validateAndParseString("Content Type",
                courseContentCsv.getContentType(), true);

        ParseDataHelper.validateAndParseInt("Content Duration",
                courseContentCsv.getContentDuration(), true);

        ParseDataHelper.validateAndParseString("Content File",
                courseContentCsv.getContentFile(), true);
    }

    public static void processError(BulkUploadError errorDetail,
            DataValidationException ex, String uniqueRecordIdentifier) {
        errorDetail.setErrorCategory(ex.getErrorCode());
        errorDetail.setRecordDetails(uniqueRecordIdentifier);
        errorDetail.setErrorDescription(ex.getErrorDesc());
    }

    /**
     * update Chapter Content For User Details
     * 
     * @param chapterContent
     * @param operatorDetails
     */
    public static void updateChapterContentForUserDetails(
            ChapterContent chapterContent, OperatorDetails operatorDetails) {
        for (LessonContent lessonContent : chapterContent.getLessons()) {
            lessonContent.setCreator(operatorDetails.getCreator());
            lessonContent.setModifiedBy(operatorDetails.getModifiedBy());
            lessonContent.setOwner(operatorDetails.getOwner());
        }
        for (ScoreContent scoreContent : chapterContent.getScores()) {
            scoreContent.setCreator(operatorDetails.getCreator());
            scoreContent.setModifiedBy(operatorDetails.getModifiedBy());
            scoreContent.setOwner(operatorDetails.getOwner());
        }

        QuizContent quiz = chapterContent.getQuiz();
        for (QuestionContent questionContent : quiz.getQuestions()) {
            questionContent.setCreator(operatorDetails.getCreator());
            questionContent.setModifiedBy(operatorDetails.getModifiedBy());
            questionContent.setOwner(operatorDetails.getOwner());
        }
        quiz.setCreator(operatorDetails.getCreator());
        quiz.setModifiedBy(operatorDetails.getModifiedBy());
        quiz.setOwner(operatorDetails.getOwner());

        chapterContent.setCreator(operatorDetails.getCreator());
        chapterContent.setModifiedBy(operatorDetails.getModifiedBy());
        chapterContent.setOwner(operatorDetails.getOwner());

    }

    /*
     * This function validates the CourseContentCsv record and returns the
     * record object, populated on the basis of contentName in the raw record.
     * In case of error in the record, it returns null.
     */
    public static void validateRawContent(CourseContentCsv courseContentCsv,
            Record record) throws DataValidationException {

        validateContentName(courseContentCsv, record);

        validateContentType(courseContentCsv);

        if (record.getType() == FileType.QUESTION_CONTENT) {
            String metaData = ParseDataHelper.validateAndParseString(
                    "METADATA", courseContentCsv.getMetaData(), true);

            if (!("CorrectAnswer").equalsIgnoreCase(metaData.substring(0,
                    metaData.indexOf(':')))) {
                LOGGER.warn("Invalid format of METADATA for contentID:{}",
                        courseContentCsv.getContentId());
                ParseDataHelper.raiseInvalidDataException("METADATA",
                        courseContentCsv.getMetaData());
            } else {
                int answerNo = ParseDataHelper.validateAndParseInt("",
                        metaData.substring(metaData.indexOf(':') + 1), true);
                if (verifyRange(answerNo, 1, 2)) {
                    record.setAnswerId(answerNo);
                } else {
                    LOGGER.warn("Answer option out of range for contentID:{}",
                            courseContentCsv.getContentId());
                    ParseDataHelper.raiseInvalidDataException("METADATA",
                            courseContentCsv.getMetaData());
                }
            }
        }

        record.setFileName(courseContentCsv.getContentFile());
    }

    /*
     * @param fieldName Name of the field in which data is inconsistent
     * 
     * @param message To be displayed. after the message it will show "in
     * field<fieldName>"
     * 
     * @throws DataValidationException
     */
    public static void raiseInconsistentDataException(String fieldName,
            String message) throws DataValidationException {

        String errDesc = message + " in field [" + fieldName + "]";
        throw new DataValidationException(null,
                ErrorCategoryConstants.INCONSISTENT_DATA, errDesc, "");
    }

    /*
     * This function validates the content Type in a CourseContentCsv Record. If
     * the content Type is anything other than CONTENT or PROMPT, it throws an
     * error.
     */
    public static void validateContentType(CourseContentCsv courseContentCsv)
            throws DataValidationException {
        if (ContentType.findByName(courseContentCsv.getContentType()) == null) {
            LOGGER.warn("Invalid content type for contentID: {}",
                    courseContentCsv.getContentId());
            ParseDataHelper.raiseInvalidDataException("Content Type",
                    courseContentCsv.getContentType());
        }
    }

    /*
     * This function validates the content Name in a CourseContentCsv Record. In
     * case of Sunny Scenario, it sets the indices in the record object and
     * return true. while in case of any error in the content name field, it
     * returns false.
     */
    public static void validateContentName(CourseContentCsv courseContentCsv,
            Record record) throws DataValidationException {
        String contentName = courseContentCsv.getContentName().trim();
        boolean recordDataValidation = true;
        if (contentName.indexOf('_') == -1) {
            LOGGER.warn("Invalid content name for contentID:{}",
                    courseContentCsv.getContentId());
            raiseInconsistentDataException(MobileAcademyConstants.CONTENT_NAME,
                    ErrorCategoryConstants.INCONSISTENT_DATA);
        }

        String chapterString = contentName.substring(0,
                contentName.indexOf('_'));
        String subString = contentName.substring(1 + contentName.indexOf('_'));

        if (StringUtils.isBlank(subString)
                || !("Chapter").equalsIgnoreCase(chapterString.substring(0,
                        chapterString.length() - 2))) {
            LOGGER.warn("Invalid content name for contentID:{}",
                    courseContentCsv.getContentId());
            raiseInconsistentDataException(MobileAcademyConstants.CONTENT_NAME,
                    String.format(
                            MobileAcademyConstants.INCONSISTENT_DATA_MESSAGE,
                            courseContentCsv.getContentId()));
        }

        try {
            record.setChapterId(Integer.parseInt(chapterString
                    .substring(chapterString.length() - 2)));
        } catch (NumberFormatException exception) {
            LOGGER.warn("Invalid content name for contentID:{}",
                    courseContentCsv.getContentId());
            raiseInconsistentDataException(MobileAcademyConstants.CONTENT_NAME,
                    String.format(
                            MobileAcademyConstants.INCONSISTENT_DATA_MESSAGE,
                            courseContentCsv.getContentId()));
        }

        if (!verifyRange(record.getChapterId(), 1,
                MobileAcademyConstants.NUM_OF_CHAPTERS)) {
            recordDataValidation = false;
        }

        if ((!recordDataValidation) || (!isTypeDeterminable(record, subString))) {
            raiseInconsistentDataException(MobileAcademyConstants.CONTENT_NAME,
                    String.format(
                            MobileAcademyConstants.INCONSISTENT_DATA_MESSAGE,
                            courseContentCsv.getContentId()));
        }

    }

    /*
     * This function checks if the type of the file to which the records points
     * to is determinable from the substring in content Name. in case of sunny
     * Scenario, it sets the file-type in record object and returns true, while
     * in case of any error, it returns false.
     */
    public static boolean isTypeDeterminable(Record record, String subString) {
        // If the substring is "QuizHeader" or "EndMenu", it will be determined.
        FileType fileType = FileType.getFor(subString);
        if ((fileType == FileType.QUIZ_HEADER)
                || (fileType == FileType.CHAPTER_END_MENU)) {
            record.setType(fileType);
            return true;
        }
        String type = subString.substring(0, subString.length() - 2);
        String indexString = subString.substring(subString.length() - 2);
        int index;
        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException exception) {
            LOGGER.debug(exception.getMessage());
            return false;
        }
        return determineTypeForLessonQuesScore(record, type, index);
    }

    /**
     * If the type is other than "QuizHeader" and "EndMenu", it will be
     * determined.
     * 
     * @param record
     * @param type
     * @param index
     * @return boolean
     */
    public static boolean determineTypeForLessonQuesScore(Record record,
            String type, int index) {
        FileType fileType;
        fileType = FileType.getFor(type);

        if ((fileType == FileType.LESSON_CONTENT)
                || (fileType == FileType.LESSON_END_MENU)) {
            if (!verifyRange(index, 1, MobileAcademyConstants.NUM_OF_LESSONS)) {
                return false;
            }
            record.setLessonId(index);
            record.setType(fileType);
            return true;
        } else if ((fileType == FileType.QUESTION_CONTENT)
                || (fileType == FileType.CORRECT_ANSWER)
                || (fileType == FileType.WRONG_ANSWER)) {
            if (!verifyRange(index, 1, MobileAcademyConstants.NUM_OF_QUESTIONS)) {
                return false;
            }
            record.setQuestionId(index);
            record.setType(fileType);
            return true;
        } else if (fileType == FileType.SCORE) {
            if (!verifyRange(index, 0, MobileAcademyConstants.NUM_OF_SCORES)) {
                return false;
            }
            record.setScoreID(index);
            record.setType(fileType);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param value : CURRENT VALUE OF PARAM
     * @param minValue : The minimum possible value
     * @param maxValue : The maximum possible value
     * @return : true if the current value lies in range
     */
    public static boolean verifyRange(int value, int minValue, int maxValue) {
        if (value < minValue || value > maxValue) {
            return false;
        }
        return true;
    }

    /*
     * This function checks the file-type to which the record refers and on the
     * basis of that, it populates the chapterContent Prototype Object and marks
     * the flag in courseFlags for successful arrival of the file.
     */
    public static void checkTypeAndAddToChapterContent(Record record,
            List<ChapterContent> chapterContents, CourseFlag courseFlag) {
        ChapterContent chapterContent = null;
        for (ChapterContent chapter : chapterContents) {
            if (chapter.getChapterNumber() == record.getChapterId()) {
                chapterContent = chapter;
                break;
            }
        }
        if (chapterContent == null) {
            return;
        } else {
            if ((record.getType() == FileType.LESSON_CONTENT)
                    || (record.getType() == FileType.LESSON_END_MENU)) {
                checkTypeAndAddToChapterContentForLesson(record, courseFlag,
                        chapterContent);

            } else if ((record.getType() == FileType.QUESTION_CONTENT)
                    || (record.getType() == FileType.CORRECT_ANSWER)
                    || (record.getType() == FileType.WRONG_ANSWER)) {
                checkTypeAndAddToChapterContentForQuestion(record, courseFlag,
                        chapterContent);

            } else if (record.getType() == FileType.QUIZ_HEADER) {
                QuizContent quiz = chapterContent.getQuiz();
                if ((MobileAcademyConstants.CONTENT_QUIZ_HEADER
                        .equalsIgnoreCase(quiz.getName()))) {
                    quiz.setAudioFile(record.getFileName());
                }
                courseFlag.markQuizHeader(record.getChapterId());

            } else if (record.getType() == FileType.CHAPTER_END_MENU) {
                if (MobileAcademyConstants.CONTENT_MENU
                        .equalsIgnoreCase(chapterContent.getName())) {
                    chapterContent.setAudioFile(record.getFileName());
                }
                courseFlag.markChapterEndMenu(record.getChapterId());
            } else if (record.getType() == FileType.SCORE) {
                List<ScoreContent> scoreContents = chapterContent.getScores();
                for (ScoreContent scoreContent : scoreContents) {
                    if ((MobileAcademyConstants.SCORE + String.format(
                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                            record.getScoreID())).equalsIgnoreCase(scoreContent
                            .getName())) {
                        scoreContent.setAudioFile(record.getFileName());
                        break;
                    }
                }
                courseFlag.markScoreFile(record.getChapterId(),
                        record.getScoreID());
            }
        }
    }

    /**
     * check the type and add question content to chapter
     * 
     * @param record
     * @param courseFlag
     * @param chapterContent
     */
    public static void checkTypeAndAddToChapterContentForQuestion(
            Record record, CourseFlag courseFlag, ChapterContent chapterContent) {
        if (record.getType() == FileType.QUESTION_CONTENT) {
            List<QuestionContent> questions = chapterContent.getQuiz()
                    .getQuestions();
            for (QuestionContent question : questions) {
                if ((question.getQuestionNumber() == record.getQuestionId())
                        && (MobileAcademyConstants.CONTENT_QUESTION
                                .equalsIgnoreCase(question.getName()))) {
                    question.setAudioFile(record.getFileName());
                    break;
                }
            }
            courseFlag.markQuestionContent(record.getChapterId(),
                    record.getQuestionId());

        } else if (record.getType() == FileType.CORRECT_ANSWER) {
            List<QuestionContent> questions = chapterContent.getQuiz()
                    .getQuestions();
            for (QuestionContent question : questions) {
                if ((question.getQuestionNumber() == record.getQuestionId())
                        && (MobileAcademyConstants.CONTENT_CORRECT_ANSWER
                                .equalsIgnoreCase(question.getName()))) {
                    question.setAudioFile(record.getFileName());
                    break;
                }
            }
            courseFlag.markQuestionCorrectAnswer(record.getChapterId(),
                    record.getQuestionId());

        } else if (record.getType() == FileType.WRONG_ANSWER) {
            List<QuestionContent> questions = chapterContent.getQuiz()
                    .getQuestions();
            for (QuestionContent question : questions) {
                if ((question.getQuestionNumber() == record.getQuestionId())
                        && (MobileAcademyConstants.CONTENT_WRONG_ANSWER
                                .equalsIgnoreCase(question.getName()))) {
                    question.setAudioFile(record.getFileName());
                    break;
                }
            }
            courseFlag.markQuestionWrongAnswer(record.getChapterId(),
                    record.getQuestionId());

        }

    }

    /**
     * check the type and add content for lesson to chapter
     * 
     * @param record
     * @param courseFlag
     * @param chapterContent
     */
    public static void checkTypeAndAddToChapterContentForLesson(Record record,
            CourseFlag courseFlag, ChapterContent chapterContent) {
        if (record.getType() == FileType.LESSON_CONTENT) {
            List<LessonContent> lessons = chapterContent.getLessons();
            for (LessonContent lesson : lessons) {
                if ((lesson.getLessonNumber() == record.getLessonId())
                        && (MobileAcademyConstants.CONTENT_LESSON
                                .equalsIgnoreCase(lesson.getName()))) {
                    lesson.setAudioFile(record.getFileName());
                    break;
                }
            }
            courseFlag.markLessonContent(record.getChapterId(),
                    record.getLessonId());

        } else if (record.getType() == FileType.LESSON_END_MENU) {
            List<LessonContent> lessons = chapterContent.getLessons();
            for (LessonContent lesson : lessons) {
                if ((lesson.getLessonNumber() == record.getLessonId())
                        && (MobileAcademyConstants.CONTENT_MENU
                                .equalsIgnoreCase(lesson.getName()))) {
                    lesson.setAudioFile(record.getFileName());
                    break;
                }
            }
            courseFlag.markLessonEndMenu(record.getChapterId(),
                    record.getLessonId());

        }
    }

}
