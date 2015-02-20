package org.motechproject.nms.mobileacademy.domain;

/**
 * Created by nitin on 2/9/15.
 */


public enum FileType {
    LessonContent,
    LessonEndMenu,
    QuizHeader,
    QuestionContent,
    CorrectAnswer,
    WrongAnswer,
    ChapterEndMenu,
    Score;

    public FileType getFor(String type){
        for(FileType fileType: FileType.values()){
            if(fileType.equals(type))
                return fileType;
        }
        return null;
    }
}
