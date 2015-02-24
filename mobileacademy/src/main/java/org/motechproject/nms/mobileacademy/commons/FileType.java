package org.motechproject.nms.mobileacademy.commons;

/**
 * Enumeration for different type of course content files.
 */

public enum FileType {
    LESSON_CONTENT("Lesson"), LESSON_END_MENU("LessonEndMenu"), QUIZ_HEADER(
            "QuizHeader"), QUESTION_CONTENT("Question"), CORRECT_ANSWER(
            "CorrectAnswer"), WRONG_ANSWER("WrongAnswer"), CHAPTER_END_MENU(
            "EndMenu"), SCORE("Score");

    private final String name;

    private FileType(String name) {
        this.name = name;
    }

    /**
     * Find File Type by file name
     * 
     * @param fileTypeName name of the file type , i.e. LessonContent,
     *            LessonEndMenu etc.
     * @return FileType object which can be null also if filename doesn't match
     *         the type.
     */
    public static FileType getFor(String fileTypeName) {
        FileType returnFileType = null;
        for (FileType fileType : FileType.values()) {
            if (fileType.name.equalsIgnoreCase(fileTypeName)) {
                returnFileType = fileType;
                break;
            }
        }
        return returnFileType;
    }

}