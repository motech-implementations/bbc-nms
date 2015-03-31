package org.motechproject.nms.mobileacademy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Question;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.commons.OperatorDetails;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * CoursePopulateServiceImpl class implements CoursePopulateService interface to
 * perform course populate related operations in mtraining and content tables.
 *
 */
@Service("CourseService")
public class CourseServiceImpl implements CourseService {

    @Autowired
    private MTrainingService mTrainingService;

    @Autowired
    private ChapterContentDataService chapterContentDataService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CourseServiceImpl.class);

    @Override
    public Course populateMtrainingCourseData(OperatorDetails operatorDetails) {
        List<Chapter> chapters = new ArrayList<>();
        LOGGER.info("populating course protoype in mTraining");
        for (int chapterCount = 1; chapterCount <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCount++) {
            List<Lesson> lessons = new ArrayList<>();
            for (int lessonCount = 1; lessonCount <= MobileAcademyConstants.NUM_OF_LESSONS; lessonCount++) {
                Lesson lesson = new Lesson(
                        MobileAcademyConstants.LESSON
                                + String.format(
                                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                        lessonCount), null, null);
                lesson.setCreator(operatorDetails.getCreator());
                lesson.setModifiedBy(operatorDetails.getModifiedBy());
                lesson.setOwner(operatorDetails.getOwner());
                lessons.add(lesson);
            }
            List<Question> questions = new ArrayList<>();
            for (int questionCount = 1; questionCount <= MobileAcademyConstants.NUM_OF_QUESTIONS; questionCount++) {
                Question question = new Question(
                        MobileAcademyConstants.QUESTION
                                + String.format(
                                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                        questionCount), null);
                questions.add(question);
            }
            Quiz quiz = new Quiz(MobileAcademyConstants.QUIZ, null, null,
                    questions, 0.0);
            quiz.setCreator(operatorDetails.getCreator());
            quiz.setModifiedBy(operatorDetails.getModifiedBy());
            quiz.setOwner(operatorDetails.getOwner());
            Chapter chapter = new Chapter(MobileAcademyConstants.CHAPTER
                    + String.format(
                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                            chapterCount), null, null, lessons, quiz);
            chapter.setCreator(operatorDetails.getCreator());
            chapter.setModifiedBy(operatorDetails.getModifiedBy());
            chapter.setOwner(operatorDetails.getOwner());
            chapters.add(chapter);
        }

        Course course = new Course(MobileAcademyConstants.DEFAULT_COURSE_NAME,
                CourseUnitState.Inactive, null, chapters);
        course.setCreator(operatorDetails.getCreator());
        course.setModifiedBy(operatorDetails.getModifiedBy());
        course.setOwner(operatorDetails.getOwner());
        mTrainingService.createCourse(course);
        LOGGER.info("Course Structure in Mtraining Populated with course state Inactive");
        return course;
    }

    @Override
    public Course getMtrainingCourse() {
        Course course = null;
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAULT_COURSE_NAME);

        if (CollectionUtils.isNotEmpty(courses)) {
            course = courses.get(0);
        }
        return course;
    }

    @Override
    public CourseUnitState findCourseState() {
        CourseUnitState state = null;
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAULT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
            state = courses.get(0).getState();
        }
        return state;
    }

    @Override
    public int getCorrectAnswerOption(Integer chapterNo, Integer questionNo) {
        List<Chapter> chapters = mTrainingService
                .getChapterByName(MobileAcademyConstants.CHAPTER
                        + String.format(
                                MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                chapterNo));
        Chapter chapter;
        Quiz quiz;
        if (CollectionUtils.isNotEmpty(chapters)) {
            chapter = chapters.get(0);
            quiz = chapter.getQuiz();
            for (Question question : quiz.getQuestions()) {
                if (question
                        .getQuestion()
                        .equalsIgnoreCase(
                                MobileAcademyConstants.QUESTION
                                        + String.format(
                                                MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                                questionNo))) {
                    String answer = question.getAnswer();

                    return Integer.valueOf(answer);
                }
            }
        }
        return 0;
    }

    @Override
    public void updateCourseState(CourseUnitState courseUnitState,
            OperatorDetails operatorDetails) {
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAULT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
            Course course = courses.get(0);
            course.setState(courseUnitState);
            course.setModifiedBy(operatorDetails.getModifiedBy());
            mTrainingService.updateCourse(course);
            LOGGER.info("Course State updated to:{}", courseUnitState);
        }
    }

    @Override
    public void updateCorrectAnswer(String chapterName, String questionName,
            String answer, OperatorDetails operatorDetails) {
        List<Chapter> chapters = mTrainingService.getChapterByName(chapterName);
        if (CollectionUtils.isNotEmpty(chapters)) {
            Chapter chapter = chapters.get(0);
            Quiz quiz = chapter.getQuiz();
            for (Question question : quiz.getQuestions()) {
                if (questionName.equalsIgnoreCase(question.getQuestion())) {
                    question.setAnswer(answer);
                    quiz.setModifiedBy(operatorDetails.getModifiedBy());
                    mTrainingService.updateQuiz(quiz);
                    break;
                }
            }
        }
    }

    @Override
    public List<ChapterContent> getAllChapterContents() {
        return chapterContentDataService.retrieveAll();
    }

    @Override
    public LessonContent getLessonContent(List<ChapterContent> chapterContents,
            int chapterId, int lessonId, String type) {
        LessonContent lessonContentReturn = null;
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            outer: for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (LessonContent lessonContent : chapterContent
                            .getLessons()) {
                        if ((lessonContent.getLessonNumber() == lessonId)
                                && (lessonContent.getName()
                                        .equalsIgnoreCase(type))) {
                            lessonContentReturn = lessonContent;
                            break outer;
                        }
                    }
                }
            }
        }
        return lessonContentReturn;
    }

    @Override
    public void setLessonContent(int chapterId, int lessonId, String type,
            String fileName, OperatorDetails operatorDetails) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            outer: for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (LessonContent lessonContent : chapterContent
                            .getLessons()) {
                        if ((lessonContent.getLessonNumber() == lessonId)
                                && (lessonContent.getName()
                                        .equalsIgnoreCase(type))) {
                            lessonContent.setAudioFile(fileName);
                            lessonContent.setModifiedBy(operatorDetails
                                    .getModifiedBy());
                            chapterContent.setModifiedBy(operatorDetails
                                    .getModifiedBy());
                            chapterContentDataService.update(chapterContent);
                            if (type.equalsIgnoreCase(MobileAcademyConstants.CONTENT_MENU)) {
                                LOGGER.info(
                                        "Menu file for Chapter{} Lesson{} updated to:{}",
                                        chapterId, lessonId, fileName);
                            } else if (type
                                    .equalsIgnoreCase(MobileAcademyConstants.CONTENT_LESSON)) {
                                LOGGER.info(
                                        "Content file for Chapter{} Lesson{} updated to:{}",
                                        chapterId, lessonId, fileName);
                            }
                            break outer;
                        }
                    }
                }
            }
        }
    }

    @Override
    public QuestionContent getQuestionContent(
            List<ChapterContent> chapterContents, int chapterId,
            int questionId, String type) {
        QuestionContent questionContentReturn = null;
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            outer: for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (QuestionContent questionContent : chapterContent
                            .getQuiz().getQuestions()) {
                        if ((questionContent.getQuestionNumber() == questionId)
                                && (questionContent.getName()
                                        .equalsIgnoreCase(type))) {
                            questionContentReturn = questionContent;
                            break outer;
                        }
                    }
                }
            }
        }
        return questionContentReturn;
    }

    @Override
    public void setQuestionContent(int chapterId, int questionId, String type,
            String fileName, OperatorDetails operatorDetails) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            outer: for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (QuestionContent questionContent : chapterContent
                            .getQuiz().getQuestions()) {
                        if ((questionContent.getQuestionNumber() == questionId)
                                && (questionContent.getName()
                                        .equalsIgnoreCase(type))) {
                            questionContent.setAudioFile(fileName);
                            questionContent.setModifiedBy(operatorDetails
                                    .getModifiedBy());
                            chapterContent.setModifiedBy(operatorDetails
                                    .getModifiedBy());
                            chapterContentDataService.update(chapterContent);
                            if (type.equalsIgnoreCase(MobileAcademyConstants.CONTENT_QUESTION)) {
                                LOGGER.info(
                                        "Question Content file for Chapter{} Question{} updated to:{}",
                                        chapterId, questionId, fileName);
                            } else if (type
                                    .equalsIgnoreCase(MobileAcademyConstants.CONTENT_WRONG_ANSWER)) {
                                LOGGER.info(
                                        "Wrong Answer file for Chapter{} Question{} updated to:{}",
                                        chapterId, questionId, fileName);
                            } else if (type
                                    .equalsIgnoreCase(MobileAcademyConstants.CONTENT_CORRECT_ANSWER)) {
                                LOGGER.info(
                                        "Correct Answer file for Chapter{} Question{} updated to:{}",
                                        chapterId, questionId, fileName);
                            }

                            break outer;
                        }
                    }
                }
            }
        }
    }

    @Override
    public ScoreContent getScore(List<ChapterContent> chapterContents,
            int chapterId, int scoreId, String type) {
        ScoreContent scoreContentReturn = null;
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            outer: for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (ScoreContent scoreContent : chapterContent.getScores()) {
                        if ((scoreContent.getName()
                                .equalsIgnoreCase(type
                                        + String.format(
                                                MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                                scoreId)))) {
                            scoreContentReturn = scoreContent;
                            break outer;
                        }
                    }
                }
            }
        }
        return scoreContentReturn;
    }

    @Override
    public void setScore(int chapterId, int scoreId, String type,
            String fileName, OperatorDetails operatorDetails) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            outer: for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (ScoreContent scoreContent : chapterContent.getScores()) {
                        if ((scoreContent.getName()
                                .equalsIgnoreCase(type
                                        + String.format(
                                                MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                                scoreId)))) {
                            scoreContent.setAudioFile(fileName);
                            scoreContent.setModifiedBy(operatorDetails
                                    .getModifiedBy());
                            chapterContent.setModifiedBy(operatorDetails
                                    .getModifiedBy());
                            chapterContentDataService.update(chapterContent);
                            LOGGER.info(
                                    "Content file for Chapter{} Score{} updated to:{}",
                                    chapterId, scoreId, fileName);
                            break outer;
                        }
                    }
                }
            }
        }
    }

    @Override
    public ChapterContent getChapterContent(
            List<ChapterContent> chapterContents, int chapterId, String type) {
        ChapterContent chapterContentReturn = null;
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            outer: for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    if (chapterContent.getName().equalsIgnoreCase(type)) {
                        chapterContentReturn = chapterContent;
                        break outer;
                    }
                }
            }
        }
        return chapterContentReturn;
    }

    @Override
    public void setChapterContent(int chapterId, String type, String fileName,
            OperatorDetails operatorDetails) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    if (chapterContent.getName().equalsIgnoreCase(type)) {
                        chapterContent.setAudioFile(fileName);
                        chapterContent.setModifiedBy(operatorDetails
                                .getModifiedBy());
                        chapterContentDataService.update(chapterContent);
                        LOGGER.info(
                                "Content file for ChapterEndMenu{} updated to:{}",
                                chapterId, fileName);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public QuizContent getQuizContent(List<ChapterContent> chapterContents,
            int chapterId, String type) {
        QuizContent quizContentReturn = null;
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            outer: for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    QuizContent quizContent = chapterContent.getQuiz();
                    if (quizContent.getName().equalsIgnoreCase(type)) {
                        quizContentReturn = quizContent;
                        break outer;
                    }
                }
            }
        }
        return quizContentReturn;
    }

    @Override
    public void setQuizContent(int chapterId, String type, String fileName,
            OperatorDetails operatorDetails) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    QuizContent quizContent = chapterContent.getQuiz();
                    if (quizContent.getName().equalsIgnoreCase(type)) {
                        quizContent.setAudioFile(fileName);
                        quizContent.setModifiedBy(operatorDetails
                                .getModifiedBy());
                        chapterContent.setModifiedBy(operatorDetails
                                .getModifiedBy());
                        chapterContentDataService.update(chapterContent);
                        LOGGER.info(
                                "Header file for quiz of Chapter{} updated to:{}",
                                chapterId, fileName);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void createChapterContent(ChapterContent chapterContent) {
        chapterContentDataService.create(chapterContent);
    }

    @Override
    public Integer getCurrentCourseVersion() {
        Course course = getMtrainingCourse();
        if (course == null || course.getState() == CourseUnitState.Inactive) {
            return null;
        }
        DateTime date = course.getModificationDate();
        LOGGER.debug("Course version returned :{} in milliSeconds",
                date.getMillis());

        return (int) (date.getMillis() / MobileAcademyConstants.MILLIS_TO_SEC_CONVERTER);
    }

    @Override
    public void updateCourseVersion(String username) {
        Course course = getMtrainingCourse();
        if (course != null) {
            course.setModifiedBy(username);
            mTrainingService.updateCourse(course);
            LOGGER.info("Course version updated successfully");
        }
    }

    @Override
    public String getCourseJson() {
        /*
         * It is assumed that controller will check for condition in which no
         * course is present in the system, so this method is called only if
         * there is a course in system
         */
        LOGGER.trace("getCourseJSON: started");
        List<ChapterContent> chapterContents = getAllChapterContents();
        JsonObject courseJsonObject = new JsonObject();
        courseJsonObject.addProperty(MobileAcademyConstants.COURSE_KEY_NAME,
                MobileAcademyConstants.DEFAULT_COURSE_NAME);

        courseJsonObject.addProperty(MobileAcademyConstants.COURSE_KEY_VERSION,
                getCurrentCourseVersion());

        courseJsonObject.add(MobileAcademyConstants.COURSE_KEY_CHAPTERS,
                generateChapterListJson(chapterContents));
        LOGGER.debug("Course Json returned successfully from getCourseJSON");

        return courseJsonObject.toString();
    }

    /*
     * This function generates JSONArray for chapters
     * 
     * @param chapterContents
     * 
     * @return JsonArray for chapters
     */
    private JsonArray generateChapterListJson(
            List<ChapterContent> chapterContents) {
        JsonArray chapterListJson = new JsonArray();
        for (int chapterNo = 1; chapterNo <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterNo++) {
            chapterListJson
                    .add(generateChapterJson(chapterNo, chapterContents));
            LOGGER.trace("Chapter-{} added in course Json", chapterNo);
        }
        return chapterListJson;
    }

    /*
     * @param chapterNo
     * 
     * @param chapterContents
     * 
     * @return JsonObject corresponding to the chapterNo
     */
    private JsonObject generateChapterJson(int chapterNo,
            List<ChapterContent> chapterContents) {
        JsonObject courseJson = new JsonObject();
        courseJson
                .addProperty(
                        MobileAcademyConstants.COURSE_KEY_NAME,
                        MobileAcademyConstants.CHAPTER
                                + String.format(
                                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                        chapterNo));
        courseJson.add(MobileAcademyConstants.COURSE_KEY_CONTENT,
                generateContentForChapter(chapterNo, chapterContents));
        courseJson.add(MobileAcademyConstants.COURSE_KEY_LESSONS,
                generateLessonListForChapter(chapterNo, chapterContents));
        LOGGER.trace("lessons for Chapter-{} added in course Json", chapterNo);
        courseJson.add(MobileAcademyConstants.COURSE_KEY_QUIZ,
                generateQuizForChapter(chapterNo, chapterContents));
        LOGGER.trace("quiz for Chapter-{} added in course Json", chapterNo);
        return courseJson;
    }

    /*
     * @param chapterNo
     * 
     * @param chapterContents
     * 
     * @return JsonObject corresponding to the quiz of chapter as per chapterNo
     */
    private JsonObject generateQuizForChapter(int chapterNo,
            List<ChapterContent> chapterContents) {
        JsonObject quizJson = new JsonObject();
        quizJson.addProperty(MobileAcademyConstants.COURSE_KEY_NAME,
                MobileAcademyConstants.QUIZ);
        quizJson.add(MobileAcademyConstants.COURSE_KEY_CONTENT,
                generateContentForQuiz(chapterNo, chapterContents));
        quizJson.add(MobileAcademyConstants.COURSE_KEY_QUESTIONS,
                generateQuestionListForQuiz(chapterNo, chapterContents));
        return quizJson;
    }

    /*
     * @param chapterNo
     * 
     * @return JsonArray corresponding to the questions of chapter as per
     * chapterNo
     */
    private JsonArray generateQuestionListForQuiz(int chapterNo,
            List<ChapterContent> chapterContents) {
        JsonArray questionsJsonArray = new JsonArray();
        for (int questionNo = 1; questionNo <= MobileAcademyConstants.NUM_OF_QUESTIONS; questionNo++) {
            questionsJsonArray.add(generateQuestionJson(chapterNo, questionNo,
                    chapterContents));
        }
        return questionsJsonArray;
    }

    /*
     * @param chapterNo
     * 
     * @param questionNo
     * 
     * @param chapterContents
     * 
     * @return JsonObject corresponding to the question of chapter as per
     * chapterNo & questionNo
     */
    private JsonObject generateQuestionJson(int chapterNo, int questionNo,
            List<ChapterContent> chapterContents) {
        JsonObject questionJson = new JsonObject();

        questionJson
                .addProperty(
                        MobileAcademyConstants.COURSE_KEY_NAME,
                        MobileAcademyConstants.QUESTION
                                + String.format(
                                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                        questionNo));
        questionJson.addProperty(
                MobileAcademyConstants.COURSE_KEY_CORRECT_ANSWER_OPTION,
                getCorrectAnswerOption(chapterNo, questionNo));
        questionJson.add(
                MobileAcademyConstants.COURSE_KEY_CONTENT,
                generateContentForQuestion(chapterNo, questionNo,
                        chapterContents));

        return questionJson;
    }

    /*
     * @param chapterNo
     * 
     * @param questionNo
     * 
     * @param chapterContents
     * 
     * @return JsonObject corresponding to the content for question of chapter
     * as per chapterNo & questionNo
     */
    private JsonObject generateContentForQuestion(int chapterNo,
            int questionNo, List<ChapterContent> chapterContents) {
        JsonObject contentJson = new JsonObject();
        String nameString;
        String audioFile;

        nameString = MobileAcademyConstants.CHAPTER
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        chapterNo)
                + MobileAcademyConstants.UNDERSCORE_DELIMITER
                + MobileAcademyConstants.QUESTION
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        questionNo);
        contentJson.addProperty(MobileAcademyConstants.COURSE_KEY_ID,
                nameString);

        audioFile = getQuestionContent(chapterContents, chapterNo, questionNo,
                MobileAcademyConstants.CONTENT_QUESTION).getAudioFile();
        contentJson.addProperty(MobileAcademyConstants.COURSE_KEY_QUESTION,
                audioFile);

        audioFile = getQuestionContent(chapterContents, chapterNo, questionNo,
                MobileAcademyConstants.CONTENT_CORRECT_ANSWER).getAudioFile();
        contentJson.addProperty(
                MobileAcademyConstants.COURSE_KEY_CORRECT_ANSWER, audioFile);

        audioFile = getQuestionContent(chapterContents, chapterNo, questionNo,
                MobileAcademyConstants.CONTENT_WRONG_ANSWER).getAudioFile();
        contentJson.addProperty(MobileAcademyConstants.COURSE_KEY_WRONG_ANSWER,
                audioFile);

        return contentJson;
    }

    /*
     * @param chapterNo
     * 
     * @param chapterContents
     * 
     * @return JsonObject corresponding to the content for quiz of chapter as
     * per chapterNo
     */
    private JsonObject generateContentForQuiz(int chapterNo,
            List<ChapterContent> chapterContents) {
        JsonObject contentJson = new JsonObject();
        String idString;
        String audioFile;

        idString = MobileAcademyConstants.CHAPTER
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        chapterNo)
                + MobileAcademyConstants.UNDERSCORE_DELIMITER
                + MobileAcademyConstants.COURSE_KEY_QUIZ_HEADER;

        audioFile = getQuizContent(chapterContents, chapterNo,
                MobileAcademyConstants.COURSE_KEY_QUIZ_HEADER).getAudioFile();

        contentJson.add(MobileAcademyConstants.COURSE_KEY_MENU,
                generateIdFileNode(idString, Arrays.asList(audioFile)));
        return contentJson;
    }

    /*
     * @param chapterNo
     * 
     * @param chapterContents
     * 
     * @return JsonArray corresponding to the lessons of chapter as per
     * chapterNo
     */
    private JsonArray generateLessonListForChapter(int chapterNo,
            List<ChapterContent> chapterContents) {
        JsonArray lessonJsonArray = new JsonArray();
        for (int lessonNo = 1; lessonNo <= MobileAcademyConstants.NUM_OF_LESSONS; lessonNo++) {
            lessonJsonArray.add(generateLessonJson(chapterNo, lessonNo,
                    chapterContents));
        }
        return lessonJsonArray;
    }

    /*
     * @param chapterNo
     * 
     * @param lessonNo
     * 
     * @param chapterContents
     * 
     * @return JsonObject corresponding to the lesson of chapter as per
     * chapterNo and lessonNo
     */
    private JsonObject generateLessonJson(int chapterNo, int lessonNo,
            List<ChapterContent> chapterContents) {
        JsonObject lessonJson = new JsonObject();

        lessonJson
                .addProperty(
                        MobileAcademyConstants.COURSE_KEY_NAME,
                        MobileAcademyConstants.LESSON
                                + String.format(
                                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                        lessonNo));
        lessonJson.add(MobileAcademyConstants.COURSE_KEY_CONTENT,
                generateContentForLesson(chapterNo, lessonNo, chapterContents));

        return lessonJson;
    }

    /*
     * @param chapterNo
     * 
     * @param lessonNo
     * 
     * @param chapterContents
     * 
     * @return JsonObject corresponding to the content of lesson of chapter as
     * per chapterNo and lessonNo
     */
    private JsonObject generateContentForLesson(int chapterNo, int lessonNo,
            List<ChapterContent> chapterContents) {
        JsonObject contentJson = new JsonObject();
        String idString;
        String audioFile;

        idString = MobileAcademyConstants.CHAPTER
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        chapterNo)
                + MobileAcademyConstants.UNDERSCORE_DELIMITER
                + MobileAcademyConstants.LESSON
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        lessonNo);
        audioFile = getLessonContent(chapterContents, chapterNo, lessonNo,
                MobileAcademyConstants.CONTENT_LESSON).getAudioFile();
        contentJson.add(MobileAcademyConstants.COURSE_KEY_LESSON,
                generateIdFileNode(idString, Arrays.asList(audioFile)));

        audioFile = getLessonContent(chapterContents, chapterNo, lessonNo,
                MobileAcademyConstants.CONTENT_MENU).getAudioFile();
        idString = MobileAcademyConstants.CHAPTER
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        chapterNo)
                + MobileAcademyConstants.UNDERSCORE_DELIMITER
                + MobileAcademyConstants.LESSON
                + MobileAcademyConstants.END_MENU
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        lessonNo);
        contentJson.add(MobileAcademyConstants.COURSE_KEY_MENU,
                generateIdFileNode(idString, Arrays.asList(audioFile)));

        return contentJson;
    }

    /*
     * @param chapterNo
     * 
     * @param chapterContents
     * 
     * @return JsonObject corresponding to the content of chapter as per
     * chapterNo
     */
    private JsonObject generateContentForChapter(int chapterNo,
            List<ChapterContent> chapterContents) {
        JsonObject contentJson = new JsonObject();
        String idString;
        String audioFile;

        idString = MobileAcademyConstants.CHAPTER
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        chapterNo)
                + MobileAcademyConstants.UNDERSCORE_DELIMITER
                + MobileAcademyConstants.END_MENU;
        audioFile = getChapterContent(chapterContents, chapterNo,
                MobileAcademyConstants.CONTENT_MENU).getAudioFile();
        contentJson.add(MobileAcademyConstants.COURSE_KEY_MENU,
                generateIdFileNode(idString, Arrays.asList(audioFile)));

        idString = MobileAcademyConstants.CHAPTER
                + String.format(
                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                        chapterNo)
                + MobileAcademyConstants.UNDERSCORE_DELIMITER
                + MobileAcademyConstants.COURSE_KEY_SCORE;
        List<String> scoreFiles = getScoreFiles(chapterNo, chapterContents);

        contentJson.add(MobileAcademyConstants.SCORE,
                generateIdFileNode(idString, scoreFiles));

        return contentJson;
    }

    /*
     * @param chapterNo
     * 
     * @param chapterContents
     * 
     * @return This returns all the score files in sequence for a particular
     * chapter
     */
    private List<String> getScoreFiles(int chapterNo,
            List<ChapterContent> chapterContents) {
        List<String> scoreFiles = new ArrayList<String>();
        for (int scoreNo = 0; scoreNo <= MobileAcademyConstants.NUM_OF_SCORES; scoreNo++) {
            scoreFiles.add(getScore(chapterContents, chapterNo, scoreNo,
                    MobileAcademyConstants.SCORE).getAudioFile());
        }
        return scoreFiles;
    }

    /*
     * @param idString
     * 
     * @param files
     * 
     * @return This function returns a JSON node with "id" and "file" key in
     * that. If the list of files contain more than one file, key will be
     * "files" instead of "file"
     */
    private JsonObject generateIdFileNode(String idString, List<String> files) {
        JsonObject node = new JsonObject();
        if (CollectionUtils.isNotEmpty(files)) {
            node.addProperty(MobileAcademyConstants.COURSE_KEY_ID, idString);
            if (files.size() == 1) {
                node.addProperty(MobileAcademyConstants.COURSE_KEY_FILE,
                        files.get(0));
            } else {
                Gson gson = new Gson();
                node.add(MobileAcademyConstants.COURSE_KEY_FILES,
                        gson.toJsonTree(files));
            }
        }
        return node;
    }
}