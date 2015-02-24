package org.motechproject.nms.mobileacademy.commons;

import java.util.Arrays;

public class CourseFlags {

    // This array is flag for LessonContent File and LessonEnd Menu file of a
    // lesson in a chapter
    public boolean flagForLessonFilesOfChapter[][][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_LESSONS][2];

    // This corresponds to QuestionContent File, Correct Answer file and Wrong
    // Answer File of a question in quiz
    boolean flagForQuestionFilesOfChapter[][][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_QUESTIONS][3];

    // This corresponds to the Quiz header files for each chapter
    boolean flagForQuizHeader[] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS];

    // This array is flag for Score Files 0,1,2,3 and 4 in a chapter.
    boolean flagForScoreFilesOfChapter[][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_SCORE_FILES];

    // This array is flag for End menu files of chapter.
    boolean flagForChapterEndMenu[] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS];

    public void resetTheFlags() {
        for (boolean[][] row2d : flagForLessonFilesOfChapter) {
            for (boolean[] row1d : row2d) {
                Arrays.fill(row1d, false);
            }
        }
        for (boolean[][] row2d : flagForQuestionFilesOfChapter) {
            for (boolean[] row1d : row2d) {
                Arrays.fill(row1d, false);
            }
        }
        for (boolean[] row1d : flagForScoreFilesOfChapter) {
            Arrays.fill(row1d, false);
        }
        Arrays.fill(flagForQuizHeader, false);
        Arrays.fill(flagForChapterEndMenu, false);
    }

    public boolean hasCompleteCourseArrived() {
        for (int i = 0; i < MobileAcademyConstants.NUM_OF_CHAPTERS; i++) {
            for (int j = 0; j < MobileAcademyConstants.NUM_OF_LESSONS; j++) {
                if (!(flagForLessonFilesOfChapter[i][j][0] && flagForLessonFilesOfChapter[i][j][1])) {
                    return false;
                }
            }
            for (int j = 0; j < MobileAcademyConstants.NUM_OF_QUESTIONS; j++) {
                if (!(flagForQuestionFilesOfChapter[i][j][0]
                        && flagForQuestionFilesOfChapter[i][j][1] && flagForQuestionFilesOfChapter[i][j][2])) {
                    return false;
                }
            }
            if (!flagForQuizHeader[i])
                return false;
            for (int j = 0; j < MobileAcademyConstants.NUM_OF_SCORE_FILES; j++) {
                if (!(flagForScoreFilesOfChapter[i][j])) {
                    return false;
                }
            }
            if (!flagForChapterEndMenu[i])
                return false;
        }
        return true;
    }

    public void markLessonContent(int chapterId, int lessonId) {
        flagForLessonFilesOfChapter[chapterId - 1][lessonId - 1][0] = true;
    }

    public void markLessonEndMenu(int chapterId, int lessonId) {
        flagForLessonFilesOfChapter[chapterId - 1][lessonId - 1][1] = true;
    }

    public void markQuizHeader(int chapterId) {
        flagForQuizHeader[chapterId - 1] = true;
    }

    public void markQuestionContent(int chapterId, int questionId) {
        flagForQuestionFilesOfChapter[chapterId - 1][questionId - 1][0] = true;
    }

    public void markQuestionCorrectAnswer(int chapterId, int questionId) {
        flagForQuestionFilesOfChapter[chapterId - 1][questionId - 1][1] = true;
    }

    public void markQuestionWrongAnswer(int chapterId, int questionId) {
        flagForQuestionFilesOfChapter[chapterId - 1][questionId - 1][2] = true;
    }

    public void markChapterEndMenu(int chapterId) {
        flagForChapterEndMenu[chapterId - 1] = true;
    }

    public void markScoreFile(int chapterId, int scoreID) {
        flagForScoreFilesOfChapter[chapterId - 1][scoreID] = true;
    }
}
