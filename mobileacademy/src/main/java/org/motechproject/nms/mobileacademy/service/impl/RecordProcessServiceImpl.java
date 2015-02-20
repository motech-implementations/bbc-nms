package org.motechproject.nms.mobileacademy.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.domain.*;
import org.motechproject.nms.mobileacademy.domain.Record;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;

import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.motechproject.nms.mobileacademy.service.CourseRawContentService;
import org.motechproject.nms.mobileacademy.service.RecordProcessService;
import org.motechproject.nms.mobileacademy.domain.ContentType;
import org.motechproject.nms.mobileacademy.domain.CourseOperator;
import org.motechproject.nms.mobileacademy.domain.FileType;
import org.motechproject.nms.mobileacademy.domain.MobileAcademyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nitin on 2/9/15.
 */
@Service("RecordProcessService")
public class RecordProcessServiceImpl implements RecordProcessService {

    @Autowired
    private CourseRawContentService courseRawContentService;

    @Autowired
    private CourseProcessedContentService courseProcessedContentService;

    @Autowired
    private ChapterContentDataService chapterContentDataService;

    @Autowired
    private CoursePopulateService coursePopulateService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    String Response = "";
    
    boolean flagForLessonFilesOfChapter[][][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_LESSONS][2];
    boolean flagForQuestionFilesOfChapter[][][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_QUESTIONS][3];
    boolean flagForQuizHeader[] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS];
    boolean flagForScoreFilesOfChapter[][] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS][MobileAcademyConstants.NUM_OF_SCORE_FILES];
    boolean flagForChapterEndMenu[] = new boolean[MobileAcademyConstants.NUM_OF_CHAPTERS];
    
    @Override
    public String processRawRecords() {
        int minNoOfEntriesInCSVPerCourse = MobileAcademyConstants.NUM_OF_CHAPTERS * (
                1    //For Chapter End Menu
                        + MobileAcademyConstants.NUM_OF_LESSONS * 2    //For Lesson Content and Menu file
                        + MobileAcademyConstants.NUM_OF_QUESTIONS * 3    //For Question content, correct answer and wrong Answer file
                        + 1        //For Quiz Header in each Quiz
                        + MobileAcademyConstants.NUM_OF_SCORE_FILES
        );

        int i = 0;

        List<String> listOfLLCs = courseRawContentService.findCourseRawContentLlcIds("ADD");

        if (CollectionUtils.isNotEmpty(listOfLLCs)) {
            for (String llcString : listOfLLCs) {
                int LLC;
                try {
                    LLC = Integer.parseInt(llcString);
                } catch(NumberFormatException exc) {
                    logger.info("LLC " + llcString + "is not numeric. Deleting all Addition records corresponding to it.");
                    courseRawContentService.deleteCourseRawContentByLLcAndOperation(llcString, "ADD");
                    continue;
                }
                if (coursePopulateService.findCourseState() == CourseUnitState.Active) {
                    logger.info("System will not consider any records to be added. Only modify records will be considered");
                    break;
                }

                resetTheFlags();

                List<CourseRawContent> courseRawContents = courseRawContentService.findContentByLlcAndOperation(llcString, "ADD");
                if (CollectionUtils.isNotEmpty(courseRawContents) && courseRawContents.size() < minNoOfEntriesInCSVPerCourse) {
                    courseRawContentService.deleteCourseRawContentByLLcAndOperation(llcString,"ADD");
                    logger.info("Insufficient Records to populate the course against Language Location Code:" + LLC);
                    continue;
                }

                // Get a new Course instance;
                List<ChapterContent> chapterContents = createChapterContentPrototype();

                for (CourseRawContent courseRawContent : courseRawContents) {
                    logger.info("Record" + ++i + " is being processed");
                    Record record = validateRawContent(courseRawContent);
                    if (record == null) {
                        logger.info("Record " + i++ + "Validation Failed");
                        continue;
                    }
                    //if record is question type, store it somewhere so that mtraining can be populated at the last.
                    //
                    checkTypeAndProcessRecord(record, chapterContents.get(record.getChapterId() - 1));
                    logger.info("Record" + i++ + " Validated");
                    //System.out.println("Response-" + i++ + " :" + Response + "\n");
                }

                if (hasCompleteCourseArrived()) {

                    for (CourseRawContent courseRawContent : courseRawContents) {
                        updateRecordInContentProcessedTable(courseRawContent);
                        courseRawContentService.delete(courseRawContent);
                        logger.info("Record" + ++i + " has been processed successfully");
                    }
                    //Update Course;
                    for (i = 0; i < MobileAcademyConstants.NUM_OF_CHAPTERS; i++) {
                        chapterContentDataService.create(chapterContents.get(i));
                    }
                    //Change the state to Active
                    coursePopulateService.updateCourseState(CourseUnitState.Active);
                }
            }

        }

        listOfLLCs = courseRawContentService.findCourseRawContentLlcIds("MOD");
        if (CollectionUtils.isNotEmpty(listOfLLCs)) {
            for (String llcString : listOfLLCs) {
                //get Course
                int LLC;
                try {
                    LLC = Integer.parseInt(llcString);
                } catch (NumberFormatException exc) {
                    logger.info("LLC " + llcString + "is not numeric. Deleting all modification records corresponding to it.");
                    courseRawContentService.deleteCourseRawContentByLLcAndOperation(llcString, "MOD");
                    continue;
                }
                for (CourseRawContent courseRawContent : courseRawContentService.findContentByLlcAndOperation(llcString, "MOD")) {
                    Record record = validateRawContent(courseRawContent);

                    if (record == null) {
                        logger.info("Record " + i++ + "Validation Failed");
                        courseRawContentService.delete(courseRawContent);
                        continue;
                    }

                    ChapterContent chapterContent = chapterContentDataService.findChapterContentByNumber(record.getChapterId());
                    checkTypeAndProcessRecord(record, chapterContent);

                    updateRecordInContentProcessedTable(courseRawContent);
                    courseRawContentService.delete(courseRawContent);

                    chapterContentDataService.update(chapterContent);

                    logger.info("Record" + i++ + " Validated");
                    //System.out.println("Response-" + i++ + " :" + Response + "\n");
                }
            }
        }
        return "Records Processed";
    }

    private void resetTheFlags() {
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

    private List<ChapterContent> createChapterContentPrototype() {
        List<ChapterContent> listOfChapters = new ArrayList<ChapterContent>();

        for (int chapterCount = 1; chapterCount <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCount++) {
            List<LessonContent> lessons = new ArrayList<>();
            for (int lessonCount = 1; lessonCount <= MobileAcademyConstants.NUM_OF_LESSONS; lessonCount++) {
                LessonContent lessonContent = new LessonContent(lessonCount,
                        MobileAcademyConstants.CONTENT_MENU, null);
                lessons.add(lessonContent);
                lessonContent = new LessonContent(lessonCount,
                        MobileAcademyConstants.CONTENT_LESSON, null);
                lessons.add(lessonContent);
            }
            List<QuestionContent> questions = new ArrayList<>();
            for (int questionCount = 1; questionCount <= MobileAcademyConstants.NUM_OF_QUESTIONS; questionCount++) {
                QuestionContent questionContent = new QuestionContent(
                        questionCount, MobileAcademyConstants.CONTENT_QUESTION,
                        null);
                questions.add(questionContent);
                questionContent = new QuestionContent(questionCount,
                        MobileAcademyConstants.CONTENT_CORRECT_ANSWER, null);
                questions.add(questionContent);
                questionContent = new QuestionContent(questionCount,
                        MobileAcademyConstants.CONTENT_WRONG_ANSWER, null);
                questions.add(questionContent);
            }
            List<Score> scores = new ArrayList<>();
            for (int scoreCount = 0; scoreCount <= MobileAcademyConstants.NUM_OF_SCORES; scoreCount++) {
                Score score = new Score(MobileAcademyConstants.SCORE
                        + String.format("%02d", scoreCount), null);
                scores.add(score);
            }
            QuizContent quiz = new QuizContent(
                    MobileAcademyConstants.CONTENT_QUIZ_HEADER, null, questions);
            ChapterContent chapterContent = new ChapterContent(chapterCount,
                    MobileAcademyConstants.CONTENT_MENU, null, lessons, scores,
                    quiz);
            listOfChapters.add(chapterContent);
        }

        logger.info("Course Prototype created in content table");
        return listOfChapters;
    }

    private void updateRecordInContentProcessedTable(CourseRawContent courseRawContent) {
        courseProcessedContentService.create(new CourseProcessedContent(
                Integer.parseInt(courseRawContent.getContentId()),
                courseRawContent.getCircle(),
                Integer.parseInt(courseRawContent.getLanguageLocationCode()),
                courseRawContent.getContentName(),
                ContentType.getFor(courseRawContent.getContentType()),
                courseRawContent.getContentFile(),
                Integer.parseInt(courseRawContent.getContentDuration()),
                courseRawContent.getMetaData()
        ));
    }
    
    private boolean hasCompleteCourseArrived(){
    	
    	for(int i=0; i<MobileAcademyConstants.NUM_OF_CHAPTERS; i++) {
    		for(int j=0; j<MobileAcademyConstants.NUM_OF_LESSONS; j++) {
    			if(!(flagForLessonFilesOfChapter[i][j][0] && flagForLessonFilesOfChapter[i][j][1])){
    				return false;
    			}
    		}
    		for(int j=0; j<MobileAcademyConstants.NUM_OF_QUESTIONS; j++) {
    			if(!(flagForQuestionFilesOfChapter[i][j][0] && flagForQuestionFilesOfChapter[i][j][1] && flagForQuestionFilesOfChapter[i][j][2])){
    				return false;
    			}
    		}
    		if(!flagForQuizHeader[i])
    			return false;
    		
    		for(int j=0; j<MobileAcademyConstants.NUM_OF_SCORE_FILES; j++) {
    			if(!(flagForScoreFilesOfChapter[i][j])){
    				return false;
    			}
    		}
    		
    		if(!flagForChapterEndMenu[i])
    			return false;
    	}
    	
    	return true;
    }

    private Record validateRawContent(CourseRawContent courseRawContent) {
        Record record = new Record();

        if (StringUtils.isNotBlank(courseRawContent.getOperation()) && courseRawContent.getOperation().trim().equalsIgnoreCase("MOD")) {
            record.setOperator(CourseOperator.MOD);
        } else {
            record.setOperator(CourseOperator.ADD);
        }
        if (StringUtils.isBlank(courseRawContent.getContentId())) {
            logger.info("*****Error: ContentID missing*****");
            return null;
        }
        try {
            Integer.parseInt(courseRawContent.getContentId());
        } catch (NumberFormatException exception) {
            //log Error(incorrect format);
            logger.info("*****Error: ContentID not Integer*****");
            return null;
        }
        if (StringUtils.isBlank(courseRawContent.getLanguageLocationCode())) {
            //log Error(Missing)
            logger.info("*****Error: LLC missing*****");
            return null;
        }
        try {
            Integer.parseInt(courseRawContent.getLanguageLocationCode());
        } catch (NumberFormatException exception) {
            //log Error(incorrect format);

            logger.info("*****Error: LLC not numeric*****");
            return null;
        }

        String contentName;
        if (StringUtils.isNotBlank(courseRawContent.getContentName())) {
            //log incorrect format
            contentName = courseRawContent.getContentName().trim();
            if(contentName.indexOf("_") == -1) {
                logger.info("*****Error: ContentName not separated by _*****");
                return null;
            }
        } else {
            logger.info("*****Error: ContentName Missing*****");
            return null;
        }

        String chapterString = contentName.substring(0, contentName.indexOf("_"));
        String subString = contentName.substring(1 + contentName.indexOf("_"));

        if(StringUtils.isBlank(subString)) {
            logger.info("*****Error: Unable to find complete content name*****");
            return null;
        }

        if (!chapterString.substring(0, chapterString.length() - 2).equalsIgnoreCase("Chapter")) {
            //log Chapter in incorrect format
            logger.info("*****Error: Chapter not found*****");
            return null;
        } else {
            try {
                record.setChapterId(Integer.parseInt(chapterString.substring(chapterString.length() - 2)));
            } catch (NumberFormatException exception) {
                //log CHAPTER-id NOT NUMERIC
                logger.info("*****Error: ChapterID not Numeric*****");
                return null;
            }
        }

        if (!isTypeDeterminable(record, subString))
            return null;

        if (StringUtils.isBlank(courseRawContent.getContentDuration())) {
            //log Error(Missing)

            logger.info("*****Error: ContentDuration missing*****");
            return null;
        }
        try {
            Integer.parseInt(courseRawContent.getContentDuration());
        } catch (NumberFormatException exception) {
            //log Error(incorrect format);

            logger.info("*****Error: ContentDuration not Numeric*****");
            return null;
        }

        if (record.getType() == FileType.QuestionContent) {
            String metaData = courseRawContent.getMetaData();
            if(metaData.isEmpty()){
                logger.info("No MetaData found");
                return null;
            }
            if (!metaData.substring(0, metaData.indexOf(":")).equalsIgnoreCase("CorrectAnswer")) {
                //log correctAnswer in incorrect format
                logger.info("*****Error: CorrectAnswer not found*****");
                return null;
            } else {
                try {
                    record.setAnswerId(Integer.parseInt(metaData.substring(metaData.indexOf(":") + 1)));
                } catch (NumberFormatException exception) {
                    //log chapter-id NOT NUMERIC
                    logger.info("*****Error: CorrectAnswerOption not Numeric*****");
                    return null;
                }
            }
        }

        if (StringUtils.isBlank(courseRawContent.getContentFile())) {
            //log Error(Missing)
            logger.info("*****Error: ContentFile missing*****");
            return null;
        }

        record.setFileName(courseRawContent.getContentFile());
        return record;
    }

    private boolean isTypeDeterminable(Record record, String subString) {

        if (subString.equalsIgnoreCase("QuizHeader")) {
            record.setType(FileType.QuizHeader);
            return true;
        }

        if (subString.equalsIgnoreCase("EndMenu")) {
            record.setType(FileType.ChapterEndMenu);
            return true;
        }

        String type = subString.substring(0, subString.length() - 2);
        String indexString = subString.substring(subString.length() - 2);
        int index;

        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException exception) {
            //log unable to determine the index in content name
            logger.info("*****Error: Second Index in content name not Numeric*****");

            return false;
        }

        if (type.equalsIgnoreCase("Lesson")) {
            record.setType(FileType.LessonContent);
            record.setLessonId(index);
            return true;
        } else if (type.equalsIgnoreCase("LessonEndMenu")) {
            record.setType(FileType.LessonEndMenu);
            record.setLessonId(index);
            return true;
        } else if (type.equalsIgnoreCase("Question")) {
            record.setQuestionId(index);
            record.setType(FileType.QuestionContent);
            return true;
        } else if (type.equalsIgnoreCase("CorrectAnswer")) {
            record.setQuestionId(index);
            record.setType(FileType.CorrectAnswer);
            return true;
        } else if (type.equalsIgnoreCase("WrongAnswer")) {
            record.setQuestionId(index);
            record.setType(FileType.WrongAnswer);
            return true;
        } else if (type.equalsIgnoreCase("Score")) {
            record.setScoreID(index);
            record.setType(FileType.Score);
            return true;
        } else {
            // Log.. Second String not correct
            logger.info("*****Error: Unable to determine the type*****");
            return false;
        }
    }

    private void checkTypeAndProcessRecord(Record record, ChapterContent chapterContent) {
    	
    	if (record.getType() == FileType.LessonContent) {
            List<LessonContent> lessons = chapterContent.getLessons();
            for(LessonContent lesson: lessons) {
                if((lesson.getLessonNumber()==record.getLessonId())&&
                        (lesson.getName().equalsIgnoreCase(MobileAcademyConstants.CONTENT_LESSON))) {
                    lesson.setAudioFile(record.getFileName());
                }
            }
            flagForLessonFilesOfChapter[record.getChapterId()-1][record.getLessonId()-1][0] = true;
        }
        else if (record.getType() == FileType.LessonEndMenu) {
            List<LessonContent> lessons = chapterContent.getLessons();
            for(LessonContent lesson: lessons) {
                if((lesson.getLessonNumber()==record.getLessonId())&&
                        (lesson.getName().equalsIgnoreCase(MobileAcademyConstants.CONTENT_MENU))) {
                    lesson.setAudioFile(record.getFileName());
                }
            }

            flagForLessonFilesOfChapter[record.getChapterId()-1][record.getLessonId()-1][1] = true;
        }
        else if (record.getType() == FileType.QuizHeader) {
            QuizContent quiz = chapterContent.getQuiz();

            if((quiz.getName().equalsIgnoreCase(MobileAcademyConstants.CONTENT_QUIZ_HEADER))) {
                quiz.setAudioFile(record.getFileName());
            }

            flagForQuizHeader[record.getChapterId()-1] = true;
        }
        else if (record.getType() == FileType.QuestionContent) {
            List<QuestionContent> questions = chapterContent.getQuiz().getQuestions();
            for(QuestionContent question: questions) {
                if((question.getQuestionNumber()==record.getQuestionId())&&
                        (question.getName().equalsIgnoreCase(MobileAcademyConstants.CONTENT_QUESTION))) {
                    question.setAudioFile(record.getFileName());
                }
            }
            coursePopulateService.updateCorrectAnswer(record.getChapterId(), record.getQuestionId(), record.getAnswerId());
            flagForQuestionFilesOfChapter[record.getChapterId()-1][record.getQuestionId()-1][0] = true;
        }
        else if (record.getType() == FileType.CorrectAnswer) {
            List<QuestionContent> questions = chapterContent.getQuiz().getQuestions();
            for(QuestionContent question: questions) {
                if((question.getQuestionNumber()==record.getQuestionId())&&
                        (question.getName().equalsIgnoreCase(MobileAcademyConstants.CONTENT_CORRECT_ANSWER))) {
                    question.setAudioFile(record.getFileName());
                }
            }

            flagForQuestionFilesOfChapter[record.getChapterId()-1][record.getQuestionId()-1][1] = true;
        }
        else if (record.getType() == FileType.WrongAnswer) {
            List<QuestionContent> questions = chapterContent.getQuiz().getQuestions();
            for(QuestionContent question: questions) {
                if((question.getQuestionNumber()==record.getQuestionId())&&
                        (question.getName().equalsIgnoreCase(MobileAcademyConstants.CONTENT_WRONG_ANSWER))) {
                    question.setAudioFile(record.getFileName());
                }
            }
            flagForQuestionFilesOfChapter[record.getChapterId()-1][record.getQuestionId()-1][2] = true;
        }
        else if (record.getType() == FileType.ChapterEndMenu) {
            if(chapterContent.getName().equalsIgnoreCase(MobileAcademyConstants.CONTENT_MENU))
                chapterContent.setAudioFile(record.getFileName());
            flagForChapterEndMenu[record.getChapterId()-1] = true;
        }
        else if (record.getType() == FileType.Score) {
            List<Score> scores = chapterContent.getScores();
            for(Score score: scores) {
                if(score.getName().equalsIgnoreCase(MobileAcademyConstants.SCORE + String.format("%02d", record.getScoreID()))) {
                    score.setAudioFile(record.getFileName());
                }
            }
            flagForScoreFilesOfChapter[record.getChapterId()-1][record.getScoreID()] = true;
        }
    }

}