package org.motechproject.nms.mobileacademy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.domain.Question;
import org.motechproject.mtraining.domain.Quiz;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CoursePopulateServiceImpl class implements CoursePopulateService interface to
 * perform course populate related operations in mtraining and content tables.
 *
 */
@Service("CoursePopulateService")
public class CoursePopulateServiceImpl implements CoursePopulateService {

    @Autowired
    private MTrainingService mTrainingService;

    @Autowired
    private ChapterContentDataService chapterContentDataService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CoursePopulateServiceImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * populateMtrainingCourseData()
     */
    @Override
    public Course populateMtrainingCourseData() {
        List<Chapter> chapters = new ArrayList<>();
        for (int chapterCount = 1; chapterCount <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCount++) {
            List<Lesson> lessons = new ArrayList<>();
            for (int lessonCount = 1; lessonCount <= MobileAcademyConstants.NUM_OF_LESSONS; lessonCount++) {
                Lesson lesson = new Lesson(
                        MobileAcademyConstants.LESSON
                                + String.format(
                                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                        lessonCount), null, null);
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
            Chapter chapter = new Chapter(MobileAcademyConstants.CHAPTER
                    + String.format(
                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                            chapterCount), null, null, lessons, quiz);
            chapters.add(chapter);
        }

        Course course = new Course(MobileAcademyConstants.DEFAULT_COURSE_NAME,
                CourseUnitState.Inactive, null, chapters);
        mTrainingService.createCourse(course);
        LOGGER.info("Course Structure in Mtraining Populated");
        return course;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * getMtrainingCourse()
     * 
     * To get the current course in MTraining
     */
    @Override
    public Course getMtrainingCourse() {
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAULT_COURSE_NAME);

        if (CollectionUtils.isNotEmpty(courses)) {
            return courses.get(0);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * findCourseState()
     */
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

    public boolean matchAnswerOption(Integer chapterNo, Integer questionNo,
            Integer optionNo) {

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
                    try {
                        return (Integer.parseInt(answer) == optionNo);
                    } catch (NumberFormatException e) {
                        LOGGER.info(
                                "Answer Option not matching for Chapter {}, Question {}",
                                chapterNo, questionNo);
                    }
                }
            }

        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * updateCourseState(org.motechproject.mtraining.domain.CourseUnitState)
     */
    @Override
    public void updateCourseState(CourseUnitState courseUnitState) {
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAULT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
            Course course = courses.get(0);
            course.setState(courseUnitState);
            mTrainingService.updateCourse(course);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * updateCorrectAnswer(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateCorrectAnswer(String chapterName, String questionName,
            String answer) {
        List<Chapter> chapters = mTrainingService.getChapterByName(chapterName);
        if (CollectionUtils.isNotEmpty(chapters)) {
            Chapter chapter = chapters.get(0);
            Quiz quiz = chapter.getQuiz();
            for (Question question : quiz.getQuestions()) {
                if (questionName.equalsIgnoreCase(question.getQuestion())) {
                    question.setAnswer(answer);
                    mTrainingService.updateQuiz(quiz);
                    break;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * getAllChapterContents()
     */
    @Override
    public List<ChapterContent> getAllChapterContents() {
        return chapterContentDataService.retrieveAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * getLessonContent(int, int, java.lang.String)
     */
    @Override
    public LessonContent getLessonContent(int chapterId, int lessonId,
            String type) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (LessonContent lessonContent : chapterContent
                            .getLessons()) {
                        if ((lessonContent.getLessonNumber() == lessonId)
                                && (lessonContent.getName()
                                        .equalsIgnoreCase(type))) {
                            return lessonContent;
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * setLessonContent(int, int, java.lang.String, java.lang.String)
     */
    @Override
    public void setLessonContent(int chapterId, int lessonId, String type,
            String fileName) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (LessonContent lessonContent : chapterContent
                            .getLessons()) {
                        if ((lessonContent.getLessonNumber() == lessonId)
                                && (lessonContent.getName()
                                        .equalsIgnoreCase(type))) {
                            lessonContent.setAudioFile(fileName);
                            chapterContentDataService.update(chapterContent);
                            return;
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * getQuestionContent(int, int, java.lang.String)
     */
    @Override
    public QuestionContent getQuestionContent(int chapterId, int questionId,
            String type) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (QuestionContent questionContent : chapterContent
                            .getQuiz().getQuestions()) {
                        if ((questionContent.getQuestionNumber() == questionId)
                                && (questionContent.getName()
                                        .equalsIgnoreCase(type))) {
                            return questionContent;
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * setQuestionContent(int, int, java.lang.String, java.lang.String)
     */
    @Override
    public void setQuestionContent(int chapterId, int questionId, String type,
            String fileName) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (QuestionContent questionContent : chapterContent
                            .getQuiz().getQuestions()) {
                        if ((questionContent.getQuestionNumber() == questionId)
                                && (questionContent.getName()
                                        .equalsIgnoreCase(type))) {
                            questionContent.setAudioFile(fileName);
                            chapterContentDataService.update(chapterContent);
                            return;
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CoursePopulateService#getScore
     * (int, int, java.lang.String)
     */
    @Override
    public ScoreContent getScore(int chapterId, int scoreId, String type) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (ScoreContent scoreContent : chapterContent.getScores()) {
                        if ((scoreContent.getName()
                                .equalsIgnoreCase(type
                                        + String.format(
                                                MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                                scoreId)))) {
                            return scoreContent;
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CoursePopulateService#setScore
     * (int, int, java.lang.String, java.lang.String)
     */
    @Override
    public void setScore(int chapterId, int scoreId, String type,
            String fileName) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    for (ScoreContent scoreContent : chapterContent.getScores()) {
                        if ((scoreContent.getName()
                                .equalsIgnoreCase(type
                                        + String.format(
                                                MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                                scoreId)))) {
                            scoreContent.setAudioFile(fileName);
                            chapterContentDataService.update(chapterContent);
                            return;
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * getChapterContent(int, java.lang.String)
     */
    @Override
    public ChapterContent getChapterContent(int chapterId, String type) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    if (chapterContent.getName().equalsIgnoreCase(type)) {
                        return chapterContent;
                    }
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * setChapterContent(int, java.lang.String, java.lang.String)
     */
    @Override
    public void setChapterContent(int chapterId, String type, String fileName) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    if (chapterContent.getName().equalsIgnoreCase(type)) {
                        chapterContent.setAudioFile(fileName);
                        chapterContentDataService.update(chapterContent);
                        return;
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * getQuizContent(int, java.lang.String)
     */
    @Override
    public QuizContent getQuizContent(int chapterId, String type) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    QuizContent quizContent = chapterContent.getQuiz();
                    if (quizContent.getName().equalsIgnoreCase(type)) {
                        return quizContent;
                    }
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.CoursePopulateService#
     * setQuizContent(int, java.lang.String, java.lang.String)
     */
    @Override
    public void setQuizContent(int chapterId, String type, String fileName) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    QuizContent quizContent = chapterContent.getQuiz();
                    if (quizContent.getName().equalsIgnoreCase(type)) {
                        quizContent.setAudioFile(fileName);
                        chapterContentDataService.update(chapterContent);
                        return;
                    }
                }
            }
        }
    }
}