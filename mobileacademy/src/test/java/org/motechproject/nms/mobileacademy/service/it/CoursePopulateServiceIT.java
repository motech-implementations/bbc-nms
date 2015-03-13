package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.commons.OperatorDetails;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CoursePopulateServiceIT extends BasePaxIT {

    @Inject
    private CoursePopulateService coursePopulateService;

    @Inject
    private MTrainingService mTrainingService;

    @Test
    public void testCoursePopulateSuccess() {
        clearMobileAcademyData();
        CourseUnitState state = null;
        state = coursePopulateService.findCourseState();
        assertNull(state);

        OperatorDetails operatorDetails = new OperatorDetails();

        coursePopulateService.populateMtrainingCourseData(operatorDetails);
        state = coursePopulateService.findCourseState();
        assertNotNull(state);
        assertSame(state, CourseUnitState.Inactive);
    }

    private void clearMobileAcademyData() {
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAULT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
            Course course = courses.get(0);
            List<Chapter> chapters = course.getChapters();
            if (CollectionUtils.isNotEmpty(chapters)) {
                for (Chapter chapter : chapters) {
                    List<Lesson> lessons = chapter.getLessons();
                    if (CollectionUtils.isNotEmpty(lessons)) {
                        for (Lesson lesson : lessons) {
                            mTrainingService.deleteLesson(lesson.getId());
                        }
                    }
                    mTrainingService.deleteChapter(chapter.getId());
                }
            }
            mTrainingService.deleteCourse(course.getId());
        }
    }

}
