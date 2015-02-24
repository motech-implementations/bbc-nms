package org.motechproject.nms.mobileacademy.commons;

public class LessonFlag {

    private int lessonNo;

    private boolean flagForLessonContentFile = false;

    private boolean flagForLessonEndMenuFile = false;

    /**
     * Initializes the LessonFlag for all the files corresponding to a
     * particular Lesson
     * 
     * @param lessonNo Lesson Index 1,2..4
     */
    public LessonFlag(int lessonNo) {
        this.lessonNo = lessonNo;
    }

    /**
     * @return lessonNo corresponding to this instance
     */
    public int getLessonNo() {
        return lessonNo;
    }

    /**
     * @return true if the content file for Lesson has arrived
     */
    public boolean isFlagForLessonContentFile() {
        return flagForLessonContentFile;
    }

    /**
     * Marks the arrival of content file for Lesson
     * 
     * @param flagForLessonContentFile
     */
    public void setFlagForLessonContentFile(boolean flagForLessonContentFile) {
        this.flagForLessonContentFile = flagForLessonContentFile;
    }

    /**
     * @return true if the end menu file for Lesson has arrived
     */
    public boolean isFlagForLessonEndMenuFile() {
        return flagForLessonEndMenuFile;
    }

    /**
     * Marks the arrival of end menu file for Lesson
     * 
     * @param flagForLessonEndMenuFile
     */
    public void setFlagForLessonEndMenuFile(boolean flagForLessonEndMenuFile) {
        this.flagForLessonEndMenuFile = flagForLessonEndMenuFile;
    }

}
