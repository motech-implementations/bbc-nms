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
import org.motechproject.nms.mobileacademy.commons.UserDetailsDTO;
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

    @Override
    public Course populateMtrainingCourseData(UserDetailsDTO userDetailsDTO) {
        List<Chapter> chapters = new ArrayList<>();
        for (int chapterCount = 1; chapterCount <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCount++) {
            List<Lesson> lessons = new ArrayList<>();
            for (int lessonCount = 1; lessonCount <= MobileAcademyConstants.NUM_OF_LESSONS; lessonCount++) {
                Lesson lesson = new Lesson(
                        MobileAcademyConstants.LESSON
                                + String.format(
                                        MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                        lessonCount), null, null);
                lesson.setCreator(userDetailsDTO.getCreator());
                lesson.setModifiedBy(userDetailsDTO.getModifiedBy());
                lesson.setOwner(userDetailsDTO.getOwner());
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
            quiz.setCreator(userDetailsDTO.getCreator());
            quiz.setModifiedBy(userDetailsDTO.getModifiedBy());
            quiz.setOwner(userDetailsDTO.getOwner());
            Chapter chapter = new Chapter(MobileAcademyConstants.CHAPTER
                    + String.format(
                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                            chapterCount), null, null, lessons, quiz);
            chapter.setCreator(userDetailsDTO.getCreator());
            chapter.setModifiedBy(userDetailsDTO.getModifiedBy());
            chapter.setOwner(userDetailsDTO.getOwner());
            chapters.add(chapter);
        }

        Course course = new Course(MobileAcademyConstants.DEFAULT_COURSE_NAME,
                CourseUnitState.Inactive, null, chapters);
        course.setCreator(userDetailsDTO.getCreator());
        course.setModifiedBy(userDetailsDTO.getModifiedBy());
        course.setOwner(userDetailsDTO.getOwner());
        mTrainingService.createCourse(course);
        LOGGER.info("Course Structure in Mtraining Populated");
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
            UserDetailsDTO userDetailsDTO) {
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAULT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
            Course course = courses.get(0);
            course.setState(courseUnitState);
            course.setModifiedBy(userDetailsDTO.getModifiedBy());
            mTrainingService.updateCourse(course);
        }
    }

    @Override
    public void updateCorrectAnswer(String chapterName, String questionName,
            String answer, UserDetailsDTO userDetailsDTO) {
        List<Chapter> chapters = mTrainingService.getChapterByName(chapterName);
        if (CollectionUtils.isNotEmpty(chapters)) {
            Chapter chapter = chapters.get(0);
            Quiz quiz = chapter.getQuiz();
            for (Question question : quiz.getQuestions()) {
                if (questionName.equalsIgnoreCase(question.getQuestion())) {
                    question.setAnswer(answer);
                    quiz.setModifiedBy(userDetailsDTO.getModifiedBy());
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
            String fileName, UserDetailsDTO userDetailsDTO) {
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
                            lessonContent.setModifiedBy(userDetailsDTO
                                    .getModifiedBy());
                            chapterContent.setModifiedBy(userDetailsDTO
                                    .getModifiedBy());
                            chapterContentDataService.update(chapterContent);
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
            String fileName, UserDetailsDTO userDetailsDTO) {
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
                            questionContent.setModifiedBy(userDetailsDTO
                                    .getModifiedBy());
                            chapterContent.setModifiedBy(userDetailsDTO
                                    .getModifiedBy());
                            chapterContentDataService.update(chapterContent);
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
            String fileName, UserDetailsDTO userDetailsDTO) {
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
                            scoreContent.setModifiedBy(userDetailsDTO
                                    .getModifiedBy());
                            chapterContent.setModifiedBy(userDetailsDTO
                                    .getModifiedBy());
                            chapterContentDataService.update(chapterContent);
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
            UserDetailsDTO userDetailsDTO) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    if (chapterContent.getName().equalsIgnoreCase(type)) {
                        chapterContent.setAudioFile(fileName);
                        chapterContent.setModifiedBy(userDetailsDTO
                                .getModifiedBy());
                        chapterContentDataService.update(chapterContent);
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
            UserDetailsDTO userDetailsDTO) {
        List<ChapterContent> chapterContents = chapterContentDataService
                .retrieveAll();
        if (CollectionUtils.isNotEmpty(chapterContents)) {
            for (ChapterContent chapterContent : chapterContents) {
                if (chapterContent.getChapterNumber() == chapterId) {
                    QuizContent quizContent = chapterContent.getQuiz();
                    if (quizContent.getName().equalsIgnoreCase(type)) {
                        quizContent.setAudioFile(fileName);
                        quizContent.setModifiedBy(userDetailsDTO
                                .getModifiedBy());
                        chapterContent.setModifiedBy(userDetailsDTO
                                .getModifiedBy());
                        chapterContentDataService.update(chapterContent);
                        break;
                    }
                }
            }
        }
    }
}