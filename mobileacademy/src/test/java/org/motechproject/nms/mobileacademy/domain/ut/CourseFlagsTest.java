package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.motechproject.nms.mobileacademy.commons.CourseFlag;

public class CourseFlagsTest {

    @InjectMocks
    CourseFlag courseFlag = new CourseFlag();

    @Test
    public void testHasCompleteCourseArrived() {

        assertFalse(courseFlag.hasCompleteCourseArrived());

        for (int i = 1; i <= 11; i++) {
            for (int j = 1; j <= 4; j++) {
                courseFlag.markLessonContent(i, j);
                courseFlag.markLessonEndMenu(i, j);
            }

        }
        assertFalse(courseFlag.hasCompleteCourseArrived());

        for (int i = 1; i <= 11; i++) {
            for (int j = 1; j <= 4; j++) {
                courseFlag.markQuestionContent(i, j);
                courseFlag.markQuestionCorrectAnswer(i, j);
                courseFlag.markQuestionWrongAnswer(i, j);
            }
        }
        assertFalse(courseFlag.hasCompleteCourseArrived());

        for (int i = 1; i <= 11; i++)
            courseFlag.markQuizHeader(i);
        assertFalse(courseFlag.hasCompleteCourseArrived());

        for (int i = 1; i <= 11; i++) {
            for (int j = 0; j <= 4; j++)
                courseFlag.markScoreFile(i, j);
        }
        assertFalse(courseFlag.hasCompleteCourseArrived());

        for (int i = 1; i <= 11; i++)
            courseFlag.markChapterEndMenu(i);
        assertTrue(courseFlag.hasCompleteCourseArrived());
    }

}
