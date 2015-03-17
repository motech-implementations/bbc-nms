package org.motechproject.nms.mobileacademy.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseFlags contains flags to determine whether files for complete course has
 * arrived or not.
 *
 */
public class CourseFlag {

    private List<ChapterFlag> chapterFlags;

    /**
     * This constructor initializes the courseFlag for all the files of a
     * course.
     */
    public CourseFlag() {
        List<ChapterFlag> chapterFlags = new ArrayList<ChapterFlag>();
        for (int i = 0; i < MobileAcademyConstants.NUM_OF_CHAPTERS; i++) {
            chapterFlags.add(new ChapterFlag(i + 1));
        }
        this.chapterFlags = chapterFlags;
    }

    /**
     * @param chapterNo: 1,2..11 chapterNo for which chapterFlag need to be
     *            fetched
     * @return ChapterFlag of the chapterNo.
     */
    public ChapterFlag getChapterFlag(int chapterNo) {
        ChapterFlag returnChapterFlag = null;
        for (ChapterFlag chapterFlag : chapterFlags) {
            if (chapterFlag.getChapterNo() == chapterNo) {
                return chapterFlag;
            }
        }
        return returnChapterFlag;
    }

    /**
     * @return true if all the files have arrived against a course.
     */
    public boolean hasCompleteCourseArrived() {
        boolean status = true;
        outer: for (int i = 1; i <= MobileAcademyConstants.NUM_OF_CHAPTERS; i++) {
            ChapterFlag chapterFlag = getChapterFlag(i);
            for (int j = 1; j <= MobileAcademyConstants.NUM_OF_LESSONS; j++) {
                LessonFlag lessonFlag = chapterFlag.getLessonFlag(j);
                if (!(lessonFlag.isFlagForLessonContentFile() && lessonFlag
                        .isFlagForLessonEndMenuFile())) {
                    status = false;
                    break outer;
                }
            }
            for (int j = 1; j <= MobileAcademyConstants.NUM_OF_QUESTIONS; j++) {
                QuestionFlag questionFlag = chapterFlag.getQuestionFlag(j);
                if (!(questionFlag.isFlagForQuestionContentFile()
                        && questionFlag.isFlagForQuestionCorrectAnswerFile() && questionFlag
                            .isFlagForQuestionWrongAnswerFile())) {
                    status = false;
                    break outer;
                }
            }
            if (!chapterFlag.isFlagForQuizHeader()) {
                status = false;
                break outer;
            }
            for (int j = 0; j < MobileAcademyConstants.NUM_OF_SCORE_FILES; j++) {
                ScoreFlag scoreFlag = chapterFlag.getScoreFlag(j);
                if (!scoreFlag.isFlagForScoreFilesOfChapter()) {
                    status = false;
                    break outer;
                }
            }
            if (!chapterFlag.isFlagForChapterEndMenuFile()) {
                status = false;
                break outer;
            }
        }
        return status;
    }

    /**
     * Marks the lessonContent file
     * 
     * @param chapterNo 1,2..11 chapter Index for which the file has arrived
     * @param lessonNo 1,2..4 lessonIndex for which file has arrived
     */
    public void markLessonContent(int chapterNo, int lessonNo) {
        ChapterFlag chapterFlag = getChapterFlag(chapterNo);
        LessonFlag lessonflag = chapterFlag.getLessonFlag(lessonNo);
        lessonflag.setFlagForLessonContentFile(true);
    }

    /**
     * Marks the lessonEndMenu file
     * 
     * @param chapterNo 1,2..11 chapter Index for which the file has arrived
     * @param lessonNo 1,2..4 lessonIndex for which file has arrived
     */
    public void markLessonEndMenu(int chapterNo, int lessonNo) {
        ChapterFlag chapterFlag = getChapterFlag(chapterNo);
        LessonFlag lessonflag = chapterFlag.getLessonFlag(lessonNo);
        lessonflag.setFlagForLessonEndMenuFile(true);
    }

    /**
     * Marks the Quiz Header file for a chapter
     * 
     * @param chapterNo 1,2..11 chapter Index for which the file has arrived
     */
    public void markQuizHeader(int chapterNo) {
        ChapterFlag chapterFlag = getChapterFlag(chapterNo);
        chapterFlag.setFlagForQuizHeader(true);
    }

    /**
     * Marks the questionContent file
     * 
     * @param chapterNo 1,2..11 chapter Index for which the file has arrived
     * @param questionNo 1,2..4 questionIndex for which file has arrived
     */
    public void markQuestionContent(int chapterNo, int questionNo) {
        ChapterFlag chapterFlag = getChapterFlag(chapterNo);
        QuestionFlag questionflag = chapterFlag.getQuestionFlag(questionNo);
        questionflag.setFlagForQuestionContentFile(true);
    }

    /**
     * Marks the questionCorrectAnswer file
     * 
     * @param chapterNo 1,2..11 chapter Index for which the file has arrived
     * @param questionNo 1,2..4 questionIndex for which file has arrived
     */
    public void markQuestionCorrectAnswer(int chapterNo, int questionNo) {
        ChapterFlag chapterFlag = getChapterFlag(chapterNo);
        QuestionFlag questionflag = chapterFlag.getQuestionFlag(questionNo);
        questionflag.setFlagForQuestionCorrectAnswerFile(true);
    }

    /**
     * Marks the questionWrongAnswer file
     * 
     * @param chapterNo 1,2..11 chapter Index for which the file has arrived
     * @param questionNo 1,2..4 questionIndex for which file has arrived
     */
    public void markQuestionWrongAnswer(int chapterNo, int questionNo) {
        ChapterFlag chapterFlag = getChapterFlag(chapterNo);
        QuestionFlag questionflag = chapterFlag.getQuestionFlag(questionNo);
        questionflag.setFlagForQuestionWrongAnswerFile(true);
    }

    /**
     * Marks the EndMenu file of chapter
     * 
     * @param chapterNo 1,2..11 chapter Index for which the file has arrived
     */
    public void markChapterEndMenu(int chapterNo) {
        ChapterFlag chapterFlag = getChapterFlag(chapterNo);
        chapterFlag.setFlagForChapterEndMenuFile(true);
    }

    /**
     * Marks the Score file of chapter
     * 
     * @param chapterNo 1,2..11 chapter Index for which the file has arrived
     * @param scoreNo 0,1,2..4 scoreIndex for which file has arrived
     */
    public void markScoreFile(int chapterNo, int scoreNo) {
        ChapterFlag chapterFlag = getChapterFlag(chapterNo);
        ScoreFlag scoreflag = chapterFlag.getScoreFlag(scoreNo);
        scoreflag.setFlagForScoreFilesOfChapter(true);
    }
}
