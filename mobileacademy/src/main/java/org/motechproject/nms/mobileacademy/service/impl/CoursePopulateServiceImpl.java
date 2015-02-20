package org.motechproject.nms.mobileacademy.service.impl;


import org.apache.commons.collections.CollectionUtils;
import org.motechproject.mtraining.domain.*;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.motechproject.nms.mobileacademy.domain.MobileAcademyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("CoursePopulateService")
public class CoursePopulateServiceImpl implements CoursePopulateService {

    @Autowired
    private MTrainingService mTrainingService;

    @Autowired
    private CourseRawContentDataService courseRawContentDataService;

    @Autowired
    private ChapterContentDataService chapterContentDataService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private void populateMtrainingCourseData() {
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

        logger.info("Course Structure in Mtraining Populated");
    }

    public CourseUnitState findCourseState() {
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAUlT_COURSE_NAME);
        if(CollectionUtils.isEmpty(courses)) {
            populateMtrainingCourseData();
            return CourseUnitState.Inactive;
        }
        else if (CollectionUtils.isNotEmpty(courses)) {
            return courses.get(0).getState();
        }
        return null;
    }

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
    public void updateCorrectAnswer(int chapterId, int questionId, int answerId) {

        logger.info("Correct Answer Updated");
    }

}
