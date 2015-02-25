package org.motechproject.nms.mobileacademy.commons;

/**
 * Enumeration for different type of course content files.
 */

public enum FileType {
    LessonContent, LessonEndMenu, QuizHeader, QuestionContent, CorrectAnswer, WrongAnswer, ChapterEndMenu, Score;

    public FileType getFor(String type) {
        for (FileType fileType : FileType.values()) {
            if (fileType.equals(type))
                return fileType;
        }
        return null;
    }
}
