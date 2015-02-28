package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verify that CoursePopulateService is present and functional
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CoursePopulateServiceIT extends BasePaxIT {

    @Inject
    private CoursePopulateService coursePopulateService;

    @Test
    public void testCoursePopulateServiceInstance() throws Exception {
        assertNotNull(coursePopulateService);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testPopulateMtrainingCourseData() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            coursePopulateService.populateMtrainingCourseData();
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testGetAllChapterContents() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            assertNotNull(coursePopulateService.getAllChapterContents());
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testGetChapterContent() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            assertNotNull(coursePopulateService.getChapterContent(01, "menu"));
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    public void testGetLessonContent() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            assertNotNull(coursePopulateService
                    .getLessonContent(01, 01, "menu"));
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testGetMtrainingCourse() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            assertNotNull(coursePopulateService.getMtrainingCourse());
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testFindCourseState() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            assertNotNull(coursePopulateService.findCourseState());
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testGetQuestionContent() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            assertNotNull(coursePopulateService.getQuestionContent(01, 01,
                    "question"));
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testGetQuizContent() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            assertNotNull(coursePopulateService
                    .getQuizContent(01, "quizHeader"));
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testSetChapterContent() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            coursePopulateService.setChapterContent(01, "menu",
                    "chkajsdkajs.wav");
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testSetLessonContent() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            coursePopulateService.setLessonContent(01, 01, "menu", "ch12.wav");
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testSetQuestionContent() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            coursePopulateService.setQuestionContent(01, 01, "question",
                    "ch12.wav");
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testSetQuizContent() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            coursePopulateService.setQuizContent(01, "quizHeader", "ch12.wav");
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testSetScore() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            coursePopulateService.setScore(01, 01, "score", "ch12.wav");
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }
}
