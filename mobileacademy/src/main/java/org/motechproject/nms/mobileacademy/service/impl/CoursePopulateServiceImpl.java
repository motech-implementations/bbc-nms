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
import org.motechproject.nms.mobileacademy.domain.Score;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for course population and querying in mtraining
 * 
 * @author YOGESH
 *
 */
@Service("CoursePopulateService")
public class CoursePopulateServiceImpl implements CoursePopulateService {

    @Autowired
    private MTrainingService mTrainingService;

    @Autowired
    private CourseRawContentDataService courseRawContentDataService;

    @Autowired
    private ChapterContentDataService chapterContentDataService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CoursePopulateServiceImpl.class);

    @Override
    public void populateMtrainingCourseData() {
        List<Chapter> chapters = new ArrayList<>();
        for (int chapterCount = 1; chapterCount <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCount++) {
            List<Lesson> lessons = new ArrayList<>();
            for (int lessonCount = 1; lessonCount <= MobileAcademyConstants.NUM_OF_LESSONS; lessonCount++) {
                Lesson lesson = new Lesson(MobileAcademyConstants.LESSON
                        + String.format("%02d", lessonCount), null, null);
                lessons.add(lesson);
            }
            List<Question> questions = new ArrayList<>();
            for (int questionCount = 1; questionCount <= MobileAcademyConstants.NUM_OF_QUESTIONS; questionCount++) {
                Question question = new Question(
                        MobileAcademyConstants.QUESTION
                                + String.format("%02d", questionCount), null);
                questions.add(question);
            }
            Quiz quiz = new Quiz(MobileAcademyConstants.QUIZ, null, null,
                    questions, 0.0);
            Chapter chapter = new Chapter(MobileAcademyConstants.CHAPTER
                    + String.format("%02d", chapterCount), null, null, lessons,
                    quiz);
            chapters.add(chapter);
        }

        Course course = new Course(MobileAcademyConstants.DEFAUlT_COURSE_NAME,
                CourseUnitState.Inactive, null, chapters);
        mTrainingService.createCourse(course);
        LOGGER.info("Course Structure in Mtraining Populated");
    }

    @Override
    public CourseUnitState findCourseState() {
    	CourseUnitState state = null;
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAUlT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
        	state = courses.get(0).getState();
            
        }
        return state;
    }

    @Override
    public void updateCourseState(CourseUnitState courseUnitState) {
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAUlT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
            Course course = courses.get(0);
            course.setState(courseUnitState);
            mTrainingService.updateCourse(course);
        }
    }

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

    @Override
    public List<ChapterContent> getAllChapterContents() {
        chapterContentDataService.retrieveAll();
        return null;
    }

    @Override
    public LessonContent getLessonContent(int chapterId, int lessonId,
            String type) {
        for (LessonContent lessonContent : chapterContentDataService
                .retrieveAll().get(chapterId - 1).getLessons()) {
            if ((lessonContent.getLessonNumber() == lessonId)
                    && (lessonContent.getName().equalsIgnoreCase(type))) {
                return lessonContent;
            }
        }
        return null;
    }

    public void setLessonContent(int chapterId, int lessonId, String type,
            String fileName) {
        ChapterContent chapterContent = chapterContentDataService.retrieveAll()
                .get(chapterId - 1);
        for (LessonContent lessonContent : chapterContent.getLessons()) {
            if ((lessonContent.getLessonNumber() == lessonId)
                    && (lessonContent.getName().equalsIgnoreCase(type))) {
                lessonContent.setAudioFile(fileName);
                chapterContentDataService.update(chapterContent);
                return;
            }
        }
    }

    public QuestionContent getQuestionContent(int chapterId, int questionId,
            String type) {
        for (QuestionContent questionContent : chapterContentDataService
                .retrieveAll().get(chapterId - 1).getQuiz().getQuestions()) {
            if ((questionContent.getQuestionNumber() == questionId)
                    && (questionContent.getName().equalsIgnoreCase(type))) {
                return questionContent;
            }
        }
        return null;
    }

    public void setQuestionContent(int chapterId, int questionId, String type,
            String fileName) {
        ChapterContent chapterContent = chapterContentDataService.retrieveAll()
                .get(chapterId - 1);
        for (QuestionContent questionContent : chapterContent.getQuiz()
                .getQuestions()) {
            if ((questionContent.getQuestionNumber() == questionId)
                    && (questionContent.getName().equalsIgnoreCase(type))) {
                questionContent.setAudioFile(fileName);
                chapterContentDataService.update(chapterContent);
                return;
            }
        }
    }

    public Score getScore(int chapterId, int scoreId, String type) {
        for (Score score : chapterContentDataService.retrieveAll()
                .get(chapterId - 1).getScores()) {
            if ((score.getName().equalsIgnoreCase(type
                    + String.format("%02d", scoreId)))) {
                return score;
            }
        }
        return null;
    }

    public void setScore(int chapterId, int scoreId, String type,
            String fileName) {
        ChapterContent chapterContent = chapterContentDataService.retrieveAll()
                .get(chapterId - 1);
        for (Score score : chapterContent.getScores()) {
            if ((score.getName().equalsIgnoreCase(type
                    + String.format("%02d", scoreId)))) {
                score.setAudioFile(fileName);
                chapterContentDataService.update(chapterContent);
                return;
            }
        }
    }

    public ChapterContent getChapterContent(int chapterId, String type) {
        ChapterContent chapterContent = chapterContentDataService.retrieveAll()
                .get(chapterId - 1);
        if (chapterContent.getName().equalsIgnoreCase(type)) {
            return chapterContent;
        }
        return null;
    }

    public void setChapterContent(int chapterId, String type, String fileName) {
        ChapterContent chapterContent = chapterContentDataService.retrieveAll()
                .get(chapterId - 1);
        if (chapterContent.getName().equalsIgnoreCase(type)) {
            chapterContent.setAudioFile(fileName);
            chapterContentDataService.update(chapterContent);
            return;
        }
    }

    public QuizContent getQuizContent(int chapterId, String type) {
        QuizContent quizContent = chapterContentDataService.retrieveAll()
                .get(chapterId - 1).getQuiz();
        if (quizContent.getName().equalsIgnoreCase(type)) {
            return quizContent;
        }
        return null;
    }

    public void setQuizContent(int chapterId, String type, String fileName) {
        ChapterContent chapterContent = chapterContentDataService.retrieveAll()
                .get(chapterId - 1);
        QuizContent quizContent = chapterContent.getQuiz();
        if (quizContent.getName().equalsIgnoreCase(type)) {
            quizContent.setAudioFile(fileName);
            chapterContentDataService.update(chapterContent);
            return;
        }
    }
}
