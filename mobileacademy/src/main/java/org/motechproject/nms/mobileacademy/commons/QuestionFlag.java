package org.motechproject.nms.mobileacademy.commons;

public class QuestionFlag {

    private int questionNo;

    private boolean flagForQuestionContentFile = false;

    private boolean flagForQuestionCorrectAnswerFile = false;

    private boolean flagForQuestionWrongAnswerFile = false;

    /**
     * Initializes the QuestionFlag for all the files corresponding to a
     * particular question
     * 
     * @param questionNo question Index 1,2..4
     */
    public QuestionFlag(int questionNo) {
        this.questionNo = questionNo;
    }

    /**
     * @return questionNo corresponding to this instance
     */
    public int getQuestionNo() {
        return questionNo;
    }

    /**
     * @return true if the content file for question has arrived
     */
    public boolean isFlagForQuestionContentFile() {
        return flagForQuestionContentFile;
    }

    /**
     * Marks the arrival of content file for question
     * 
     * @param flagForQuestionContentFile
     */
    public void setFlagForQuestionContentFile(boolean flagForQuestionContentFile) {
        this.flagForQuestionContentFile = flagForQuestionContentFile;
    }

    /**
     * @return true if the correct answer file for question has arrived
     */
    public boolean isFlagForQuestionCorrectAnswerFile() {
        return flagForQuestionCorrectAnswerFile;
    }

    /**
     * Marks the arrival of correct answer file for question
     * 
     * @param flagForQuestionCorrectAnswerFile
     */
    public void setFlagForQuestionCorrectAnswerFile(
            boolean flagForQuestionCorrectAnswerFile) {
        this.flagForQuestionCorrectAnswerFile = flagForQuestionCorrectAnswerFile;
    }

    /**
     * @return true if the wrong answer file for question has arrived
     */
    public boolean isFlagForQuestionWrongAnswerFile() {
        return flagForQuestionWrongAnswerFile;
    }

    /**
     * Marks the arrival of wrong answer file for question
     * 
     * @param flagForQuestionWrongAnswerFile
     */
    public void setFlagForQuestionWrongAnswerFile(
            boolean flagForQuestionWrongAnswerFile) {
        this.flagForQuestionWrongAnswerFile = flagForQuestionWrongAnswerFile;
    }

}
