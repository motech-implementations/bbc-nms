package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.motechproject.nms.mobileacademy.commons.CourseFlags;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;

public class CourseFlagsTest {

    @InjectMocks
    CourseFlags courseFlag = new CourseFlags();

    @SuppressWarnings("unused")
    @Test
    public void testHasCompleteCourseArrived() {

        boolean flagForLessonFilesOfChapter[][][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_LESSONS][2];
        boolean flagForQuestionFilesOfChapter[][][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_QUESTIONS][3];
        boolean flagForQuizHeader[] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS];
        boolean flagForScoreFilesOfChapter[][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_SCORE_FILES];
        boolean flagForChapterEndMenu[] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS];
        courseFlag.resetTheFlags();
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
    }

}
