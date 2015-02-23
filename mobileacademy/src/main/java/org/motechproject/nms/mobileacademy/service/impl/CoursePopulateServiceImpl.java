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
import org.motechproject.nms.mobileacademy.domain.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for course population and querying in mtraining
 * 
 *
 */
@Service("CoursePopulateService")
public class CoursePopulateServiceImpl implements CoursePopulateService {

    @Autowired
    private MTrainingService mTrainingService;

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
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAUlT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
            return courses.get(0).getState();
        }
        return null;
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
}
