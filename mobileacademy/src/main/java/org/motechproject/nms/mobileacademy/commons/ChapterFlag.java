package org.motechproject.nms.mobileacademy.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nitin
 *
 */
public class ChapterFlag {

    private int chapterNo;

    private List<LessonFlag> lessonFlags;

    private List<QuestionFlag> questionFlags;

    private List<ScoreFlag> scoreFlags;

    private boolean flagForQuizHeader = false;

    private boolean flagForChapterEndMenuFile = false;

    /**
     * Initializes the chapterFlag for all the files corresponding to a
     * particular chapter
     * 
     * @param chapterNo Chapter Index 1,2..11
     */
    public ChapterFlag(int chapterNo) {
        this.chapterNo = chapterNo;

        List<LessonFlag> lessonFlags = new ArrayList<LessonFlag>();
        for (int i = 1; i <= MobileAcademyConstants.NUM_OF_LESSONS; i++)
            lessonFlags.add(new LessonFlag(i));
        this.lessonFlags = lessonFlags;

        List<QuestionFlag> questionFlags = new ArrayList<QuestionFlag>();
        for (int i = 1; i <= MobileAcademyConstants.NUM_OF_QUESTIONS; i++)
            questionFlags.add(new QuestionFlag(i));
        this.questionFlags = questionFlags;

        List<ScoreFlag> scoreFlags = new ArrayList<ScoreFlag>();
        for (int i = 0; i <= MobileAcademyConstants.NUM_OF_SCORES; i++)
            scoreFlags.add(new ScoreFlag(i));
        this.scoreFlags = scoreFlags;
    }

    /**
     * @param lessonNo: Lesson Index 1,2..4
     * @return LessonFlag corresponding to the lesson Index
     */
    public LessonFlag getLessonFlag(int lessonNo) {
        LessonFlag lessonFlag = null;
        for (LessonFlag lessonFlagIterator : lessonFlags) {
            if (lessonFlagIterator.getLessonNo() == lessonNo)
                return lessonFlagIterator;
        }
        return lessonFlag;
    }

    /**
     * @param questionNo: Question Index 1,2..4
     * @return questionFlag corresponding to the question index
     */
    public QuestionFlag getQuestionFlag(int questionNo) {
        QuestionFlag questionFlag = null;
        for (QuestionFlag questionFlagIterator : questionFlags) {
            if (questionFlagIterator.getQuestionNo() == questionNo)
                return questionFlagIterator;
        }
        return questionFlag;
    }

    /**
     * @param scoreNo: Score Index 0,1,2..4
     * @return ScoreFlag corresponding to the Score index
     */
    public ScoreFlag getScoreFlag(int scoreNo) {
        ScoreFlag scoreFlag = null;
        for (ScoreFlag scoreFlagIterator : scoreFlags) {
            if (scoreFlagIterator.getScoreNo() == scoreNo)
                return scoreFlagIterator;
        }
        return scoreFlag;
    }

    /**
     * @return ChapterNo corresponding to this instance
     */
    public int getChapterNo() {
        return chapterNo;
    }

    /**
     * @return true if the quiz header file for chapter has arrived
     */
    public boolean isFlagForQuizHeader() {
        return flagForQuizHeader;
    }

    /**
     * Marks the arrival of quiz header file for chapter
     * 
     * @param flagForQuizHeader
     */
    public void setFlagForQuizHeader(boolean flagForQuizHeader) {
        this.flagForQuizHeader = flagForQuizHeader;
    }

    /**
     * @return true if the end menu file for chapter has arrived
     */
    public boolean isFlagForChapterEndMenuFile() {
        return flagForChapterEndMenuFile;
    }

    /**
     * Marks the arrival of end menu file for chapter
     * 
     * @param flagForChapterEndMenuFile
     */
    public void setFlagForChapterEndMenuFile(boolean flagForChapterEndMenuFile) {
        this.flagForChapterEndMenuFile = flagForChapterEndMenuFile;
    }

}
