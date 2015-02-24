package org.motechproject.nms.mobileacademy.commons;

public class ScoreFlag {

    private int scoreNo;

    private boolean flagForScoreFilesOfChapter = false;

    /**
     * Initializes the ScoreFlag for file corresponding to a particular score
     * 
     * @param scoreNo score Index 0,1,2..4
     */
    public ScoreFlag(int scoreNo) {
        this.scoreNo = scoreNo;
    }

    /**
     * @return scoreNo corresponding to this instance
     */
    public int getScoreNo() {
        return scoreNo;
    }

    /**
     * @return true if the score file for this scoreNo has arrived
     */
    public boolean isFlagForScoreFilesOfChapter() {
        return flagForScoreFilesOfChapter;
    }

    /**
     * Marks the arrival of score file for this scoreNo of question
     * 
     * @param flagForScoreFilesOfChapter
     */
    public void setFlagForScoreFilesOfChapter(boolean flagForScoreFilesOfChapter) {
        this.flagForScoreFilesOfChapter = flagForScoreFilesOfChapter;
    }

}
