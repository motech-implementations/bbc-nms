package org.motechproject.nms.mobileacademy.service.impl;

/**
 * Created by nitin on 2/9/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.ContentType;
import org.motechproject.nms.mobileacademy.commons.CourseFlags;
import org.motechproject.nms.mobileacademy.commons.FileType;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.commons.Record;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;
import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.Score;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.motechproject.nms.mobileacademy.service.CourseRawContentService;
import org.motechproject.nms.mobileacademy.service.FLWService;
import org.motechproject.nms.mobileacademy.service.RecordProcessService;
import org.motechproject.nms.mobileacademy.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yogesh
 * 
 *
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

    @Autowired
    private FLWService flwService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String processRawRecords(List<CourseRawContent> courseRawContents) {

        Map<Integer, List<CourseRawContent>> mapForAddRecords = new HashMap<Integer, List<CourseRawContent>>();
        Map<String, List<CourseRawContent>> mapForModifyRecords = new HashMap<String, List<CourseRawContent>>();
        Map<Integer, List<CourseRawContent>> mapForDeleteRecords = new HashMap<Integer, List<CourseRawContent>>();

        List<Integer> listOfExistingLLCinCPC = courseProcessedContentService
                .getListOfAllExistingLLcs();

        for (CourseRawContent courseRawContent : courseRawContents) {
            if (!(validateSchema(courseRawContent) && validateCircleAndLLC(courseRawContent))) {
                courseRawContentService.delete(courseRawContent);
                courseRawContents.remove(courseRawContent);
                continue;
            }
            if (courseRawContent.getOperation().equalsIgnoreCase(
                    MobileAcademyConstants.COURSE_DEL)) {
                putRecordInDeleteMap(mapForDeleteRecords, courseRawContent);
            } else {
                int LLC = Integer.parseInt(courseRawContent
                        .getLanguageLocationCode());
                if (listOfExistingLLCinCPC.contains(LLC)) {
                    courseRawContent
                            .setOperation(MobileAcademyConstants.COURSE_MOD);
                    putRecordInModifyMap(mapForModifyRecords, courseRawContent);
                } else {
                    courseRawContent
                            .setOperation(MobileAcademyConstants.COURSE_MOD);
                    putRecordInAddMap(mapForAddRecords, courseRawContent);
                }
            }
            courseRawContents.remove(courseRawContent);
        }

        processAddRecords(mapForAddRecords);

        processModificationRecords(mapForModifyRecords);

        processDeleteRecords(mapForDeleteRecords);

        return "Records Processed Successfully";

    }

    private boolean validateCircleAndLLC(CourseRawContent courseRawContent) {
        String circle = courseRawContent.getCircle();
        int llc = Integer.parseInt(courseRawContent.getLanguageLocationCode());
        if (!flwService.isCircleValid(circle)) {
            // log error circle not valid
            return false;
        }
        if (!flwService.isLLCValidInCircle(circle, llc)) {
            // log error: circle not valid in LLC
            return false;
        }
        return true;
    }

    private void putRecordInAddMap(
            Map<Integer, List<CourseRawContent>> mapForAddRecords,
            CourseRawContent courseRawContent) {
        int LLC = Integer.parseInt(courseRawContent.getLanguageLocationCode());
        if (mapForAddRecords.containsKey(LLC)) {
            mapForAddRecords.get(LLC).add(courseRawContent);
        } else {
            ArrayList<CourseRawContent> listOfRecords = new ArrayList<CourseRawContent>();
            listOfRecords.add(courseRawContent);
            mapForAddRecords.put(LLC, listOfRecords);
        }
    }

    private void putRecordInModifyMap(
            Map<String, List<CourseRawContent>> mapForModifyRecords,
            CourseRawContent courseRawContent) {
        String key = courseRawContent.getContentName();
        if (mapForModifyRecords.containsKey(key)) {
            mapForModifyRecords.get(key).add(courseRawContent);
        } else {
            ArrayList<CourseRawContent> listOfRecords = new ArrayList<CourseRawContent>();
            listOfRecords.add(courseRawContent);
            mapForModifyRecords.put(key, listOfRecords);
        }
    }

    private void putRecordInDeleteMap(
            Map<Integer, List<CourseRawContent>> mapForDeleteRecords,
            CourseRawContent courseRawContent) {
        int LLC = Integer.parseInt(courseRawContent.getLanguageLocationCode());
        if (mapForDeleteRecords.containsKey(LLC)) {
            mapForDeleteRecords.get(LLC).add(courseRawContent);
        } else {
            ArrayList<CourseRawContent> listOfRecords = new ArrayList<CourseRawContent>();
            listOfRecords.add(courseRawContent);
            mapForDeleteRecords.put(LLC, listOfRecords);
        }
    }

    private boolean validateSchema(CourseRawContent courseRawContent) {

        if (StringUtils.isBlank(courseRawContent.getContentId())) {
            logger.info("*****Error: ContentID missing*****");
            return false;
        }
        try {
            Integer.parseInt(courseRawContent.getContentId());
        } catch (NumberFormatException exception) {
            // log Error(incorrect format);
            logger.info("*****Error: ContentID not Integer*****");
            return false;
        }
        if (StringUtils.isBlank(courseRawContent.getLanguageLocationCode())) {
            // log Error(Missing)
            logger.info("*****Error: LLC missing*****");
            return false;
        }
        try {
            Integer.parseInt(courseRawContent.getLanguageLocationCode());
        } catch (NumberFormatException exception) {
            logger.info("*****Error: LLC not numeric*****");
            return false;
        }
        if (StringUtils.isBlank(courseRawContent.getContentName())) {
            logger.info("*****Error: ContentName Missing*****");
            return false;
        }

        if (StringUtils.isBlank(courseRawContent.getContentDuration())) {
            // log Error(Missing)

            logger.info("*****Error: ContentDuration missing*****");
            return false;
        }
        try {
            Integer.parseInt(courseRawContent.getContentDuration());
        } catch (NumberFormatException exception) {
            // log Error(incorrect format);
            logger.info("*****Error: ContentDuration not Numeric*****");
            return false;
        }

        if (StringUtils.isBlank(courseRawContent.getContentFile())) {
            // log Error(Missing)
            logger.info("*****Error: ContentFile missing*****");
            return false;
        }
        return true;
    }

    private void processModificationRecords(
            Map<String, List<CourseRawContent>> mapForModifyRecords) {

        Map<String, List<CourseRawContent>> fileNameChangeRecords = new HashMap<String, List<CourseRawContent>>();

        if (!mapForModifyRecords.isEmpty()) {
            for (String contentName : mapForModifyRecords.keySet()) {

                List<CourseRawContent> courseRawContents = mapForModifyRecords
                        .get(contentName);
                if (CollectionUtils.isNotEmpty(courseRawContents)) {
                    for (CourseRawContent courseRawContent : courseRawContents) {

                        Record record = validateRawContent(courseRawContent);
                        if (record == null) {
                            // logger.info("Record " + i++ +
                            // "Validation Failed");
                            // delete all records of ADD for the LLC
                            // stop the operation
                            mapForModifyRecords.get(contentName).remove(
                                    courseRawContent);
                            courseRawContentService.delete(courseRawContent);
                            continue;
                        }

                        if (isRecordChangingTheFileName(record)) {
                            continue;
                        } else {
                            int LLC = Integer.parseInt(courseRawContent
                                    .getLanguageLocationCode());
                            CourseProcessedContent CPC = courseProcessedContentService
                                    .getRecordforModification(courseRawContent
                                            .getCircle(), LLC, courseRawContent
                                            .getContentName().toUpperCase());

                            if (CPC != null) {
                                CPC.setContentDuration(Integer
                                        .parseInt(courseRawContent
                                                .getContentDuration()));
                                CPC.setContentID(Integer
                                        .parseInt(courseRawContent
                                                .getContentId()));
                                courseProcessedContentService.update(CPC);
                            }
                            mapForModifyRecords.get(contentName).remove(
                                    courseRawContent);
                            courseRawContentService.delete(courseRawContent);
                        }
                    }
                }
            }
        }

        // Start Processing the file change records:
        if (!mapForModifyRecords.isEmpty()) {
            for (String contentName : fileNameChangeRecords.keySet()) {

                boolean updateContentFile = true;
                List<Integer> listOfExistingLlc = courseProcessedContentService
                        .getListOfAllExistingLLcs();

                List<CourseRawContent> courseRawContents = mapForModifyRecords
                        .get(contentName);
                if (courseRawContents.size() < listOfExistingLlc.size()) {
                    // log error.. Can't modify the content file. Data not
                    // received for all existing LLCS
                    mapForModifyRecords.remove(contentName);
                    Helper.deleteCourseRawContentsByList(courseRawContents);
                    continue;
                }

                String fileName = fileNameChangeRecords.get(contentName).get(0)
                        .getContentFile();

                for (CourseRawContent courseRawContent : fileNameChangeRecords
                        .get(contentName)) {
                    int LLC = Integer.parseInt(courseRawContent
                            .getLanguageLocationCode());
                    if (!fileName.equals(courseRawContent.getContentFile())) {
                        // Log Error: File Name not same for all the files to be
                        // modified.
                        mapForModifyRecords.remove(contentName);
                        Helper.deleteCourseRawContentsByList(courseRawContents);
                        updateContentFile = false;
                        break;
                    }
                    listOfExistingLlc.remove(LLC);
                }

                if (!updateContentFile) {
                    continue;
                }

                // If data has arrived for all the existing LLCS.
                if (CollectionUtils.isEmpty(listOfExistingLlc)) {

                    Record record = validateRawContent(fileNameChangeRecords
                            .get(contentName).get(0));

                    if (record != null) {
                        determineTypeAndUpdateChapterContent(record);
                    }

                    for (CourseRawContent courseRawContent : fileNameChangeRecords
                            .get(contentName)) {
                        int LLC = Integer.parseInt(courseRawContent
                                .getLanguageLocationCode());
                        CourseProcessedContent CPC = courseProcessedContentService
                                .getRecordforModification(
                                        courseRawContent.getCircle(), LLC,
                                        contentName);
                        if (CPC != null) {
                            CPC.setContentFile(fileName);
                            CPC.setContentDuration(Integer
                                    .parseInt(courseRawContent
                                            .getContentDuration()));
                            CPC.setContentID(Integer.parseInt(courseRawContent
                                    .getContentId()));

                            courseProcessedContentService.update(CPC);
                        }
                        mapForModifyRecords.get(contentName).remove(
                                courseRawContent);
                        courseRawContentService.delete(courseRawContent);
                    }
                }
            }
        }
    }

    private void determineTypeAndUpdateChapterContent(Record record) {
        if (record.getType() == FileType.LessonContent) {
            coursePopulateService
                    .setLessonContent(record.getChapterId(),
                            record.getLessonId(),
                            MobileAcademyConstants.CONTENT_LESSON,
                            record.getFileName());
        } else if (record.getType() == FileType.LessonEndMenu) {
            coursePopulateService.setLessonContent(record.getChapterId(),
                    record.getLessonId(), MobileAcademyConstants.CONTENT_MENU,
                    record.getFileName());
        } else if (record.getType() == FileType.QuestionContent) {
            coursePopulateService.setQuestionContent(record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_QUESTION,
                    record.getFileName());
        } else if (record.getType() == FileType.CorrectAnswer) {
            coursePopulateService.setQuestionContent(record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_CORRECT_ANSWER,
                    record.getFileName());
        } else if (record.getType() == FileType.WrongAnswer) {
            coursePopulateService.setQuestionContent(record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_WRONG_ANSWER,
                    record.getFileName());
        } else if (record.getType() == FileType.ChapterEndMenu) {
            coursePopulateService.setChapterContent(record.getChapterId(),
                    MobileAcademyConstants.CONTENT_MENU, record.getFileName());
        } else if (record.getType() == FileType.QuizHeader) {
            coursePopulateService.setQuizContent(record.getChapterId(),
                    MobileAcademyConstants.CONTENT_QUIZ_HEADER,
                    record.getFileName());
        } else if (record.getType() == FileType.Score) {
            coursePopulateService.setScore(record.getChapterId(),
                    record.getScoreID(), MobileAcademyConstants.SCORE,
                    record.getFileName());
        }
    }

    private boolean isRecordChangingTheFileName(Record record) {
        if (record.getType() == FileType.LessonContent) {
            LessonContent lessonContent = coursePopulateService
                    .getLessonContent(record.getChapterId(),
                            record.getLessonId(),
                            MobileAcademyConstants.CONTENT_LESSON);
            if (!lessonContent.getAudioFile().equals(record.getFileName())) {
                return true;
            }
        } else if (record.getType() == FileType.LessonEndMenu) {
            LessonContent lessonContent = coursePopulateService
                    .getLessonContent(record.getChapterId(),
                            record.getLessonId(),
                            MobileAcademyConstants.CONTENT_MENU);
            if (!lessonContent.getAudioFile().equals(record.getFileName())) {
                return true;
            }
        } else if (record.getType() == FileType.QuestionContent) {
            QuestionContent questionContent = coursePopulateService
                    .getQuestionContent(record.getChapterId(),
                            record.getQuestionId(),
                            MobileAcademyConstants.CONTENT_QUESTION);
            if (!questionContent.getAudioFile().equals(record.getFileName())) {
                return true;
            }
        } else if (record.getType() == FileType.CorrectAnswer) {
            QuestionContent questionContent = coursePopulateService
                    .getQuestionContent(record.getChapterId(),
                            record.getQuestionId(),
                            MobileAcademyConstants.CONTENT_CORRECT_ANSWER);
            if (!questionContent.getAudioFile().equals(record.getFileName())) {
                return true;
            }
        } else if (record.getType() == FileType.WrongAnswer) {
            QuestionContent questionContent = coursePopulateService
                    .getQuestionContent(record.getChapterId(),
                            record.getQuestionId(),
                            MobileAcademyConstants.CONTENT_WRONG_ANSWER);
            if (!questionContent.getAudioFile().equals(record.getFileName())) {
                return true;
            }
        } else if (record.getType() == FileType.ChapterEndMenu) {
            ChapterContent chapterContent = coursePopulateService
                    .getChapterContent(record.getChapterId(),
                            MobileAcademyConstants.CONTENT_MENU);
            if (!chapterContent.getAudioFile().equals(record.getFileName())) {
                return true;
            }
        } else if (record.getType() == FileType.QuizHeader) {
            QuizContent quizContent = coursePopulateService.getQuizContent(
                    record.getChapterId(),
                    MobileAcademyConstants.CONTENT_QUIZ_HEADER);
            if (!quizContent.getAudioFile().equals(record.getFileName())) {
                return true;
            }
        } else if (record.getType() == FileType.Score) {
            Score score = coursePopulateService.getScore(record.getChapterId(),
                    record.getScoreID(), MobileAcademyConstants.SCORE);
            if (!score.getAudioFile().equals(record.getFileName())) {
                return true;
            }
        }
        return false;
    }

    private void processAddRecords(
            Map<Integer, List<CourseRawContent>> mapForAddRecords) {
        boolean populateCourseStructure = false;
        CourseFlags courseFlags = new CourseFlags();
        List<Record> answerOptionRecordList = new ArrayList<Record>();

        if (!mapForAddRecords.isEmpty()) {
            for (Integer LLC : mapForAddRecords.keySet()) {
                List<CourseRawContent> courseRawContents = mapForAddRecords
                        .get(LLC);
                if (CollectionUtils.isNotEmpty(courseRawContents)) {
                    if (courseRawContents.size() != MobileAcademyConstants.minNoOfEntriesInCSVPerCourse) {
                        mapForAddRecords.remove(LLC);
                        Helper.deleteCourseRawContentsByList(courseRawContents);
                        logger.info(
                                "There must be exact {} records to populate the course corresponding to LLC:{}.",
                                MobileAcademyConstants.minNoOfEntriesInCSVPerCourse,
                                LLC);
                        continue;
                    }

                    if (coursePopulateService.findCourseState() == CourseUnitState.Inactive)
                        populateCourseStructure = true;

                    courseFlags.resetTheFlags();
                    answerOptionRecordList.clear();

                    List<ChapterContent> chapterContents;
                    if (populateCourseStructure) {
                        // Get a new Course instance;
                        chapterContents = createChapterContentPrototype();
                    } else {
                        // Get the current Course instance;
                        chapterContents = coursePopulateService
                                .getAllChapterContents();
                    }

                    for (CourseRawContent courseRawContent : courseRawContents) {
                        Record record = validateRawContent(courseRawContent);
                        if (record == null) {
                            // Write Error in this record: Hence other cann't be
                            // processed
                            mapForAddRecords.remove(LLC);
                            Helper.deleteCourseRawContentsByList(courseRawContents);
                        }

                        // if record is question type, store it somewhere so
                        // that
                        // mtraining can be populated at the last.
                        //
                        if (populateCourseStructure) {
                            if (record.getType() == FileType.QuestionContent) {
                                answerOptionRecordList.add(record);
                            }
                            checkTypeAndAddToChapterContent(
                                    record,
                                    chapterContents.get(record.getChapterId() - 1),
                                    courseFlags);
                        } else {
                            if (!checkRecordConsistencyAndMarkFlag(
                                    record,
                                    chapterContents.get(record.getChapterId() - 1),
                                    courseFlags)) {
                                // log error record inconsistent
                                // Delete all records of the LLC for add..
                                // stop the operation
                                mapForAddRecords.remove(LLC);
                                Helper.deleteCourseRawContentsByList(courseRawContents);
                                break;
                            }
                        }
                    }

                    if (courseFlags.hasCompleteCourseArrived()) {
                        for (CourseRawContent courseRawContent : courseRawContents) {
                            updateRecordInContentProcessedTable(courseRawContent);
                            courseRawContentService.delete(courseRawContent);
                            // logger.info("Record" + ++i
                            // + " has been processed successfully");
                        }
                        // Update Course;
                        if (populateCourseStructure) {
                            for (int chapterCounter = 0; chapterCounter < MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCounter++) {
                                chapterContentDataService
                                        .create(chapterContents
                                                .get(chapterCounter));
                            }
                            // Update AnswerOptionList
                            // Change the state to Active
                            processAnswerOptionRecordList(answerOptionRecordList);
                            coursePopulateService
                                    .updateCourseState(CourseUnitState.Active);
                        }
                    }

                }
            }
        }

    }

    public void processDeleteRecords(
            Map<Integer, List<CourseRawContent>> mapForDeleteRecords) {

        if (!mapForDeleteRecords.isEmpty()) {
            for (Integer LLC : mapForDeleteRecords.keySet()) {
                CourseFlags courseFlags = new CourseFlags();
                courseFlags.resetTheFlags();

                List<CourseRawContent> courseRawContents = mapForDeleteRecords
                        .get(LLC);

                if (courseRawContents.size() < MobileAcademyConstants.minNoOfEntriesInCSVPerCourse) {
                    // log error: Size is less than minimum required
                    mapForDeleteRecords.remove(LLC);
                    Helper.deleteCourseRawContentsByList(courseRawContents);
                }

                for (CourseRawContent courseRawContent : courseRawContents) {
                    Record record = new Record();
                    if (!validateContentName(courseRawContent, record)) {
                        // error in particular record
                        courseRawContentService.delete(courseRawContent);
                        mapForDeleteRecords.get(LLC).remove(courseRawContent);
                        continue;
                    }
                    checkRecordTypeAndMarkCourseFlag(record, courseFlags);
                }

                if (courseFlags.hasCompleteCourseArrived()) {
                    courseProcessedContentService.deleteRecordsByLLC(LLC);
                    // If this was the last LLC in CPC
                    if (courseProcessedContentService
                            .getListOfAllExistingLLcs().size() == 0) {
                        coursePopulateService
                                .updateCourseState(CourseUnitState.Inactive);
                        chapterContentDataService.deleteAll();
                    }
                } else {
                    // log error: complete records haven't arrived for deleting
                    // the course.
                    logger.info("Error in deleting complete course");
                }
                mapForDeleteRecords.remove(LLC);
                Helper.deleteCourseRawContentsByList(courseRawContents);
            }
        }
    }

    private void checkRecordTypeAndMarkCourseFlag(Record record,
            CourseFlags courseFlags) {
        if (record.getType() == FileType.LessonContent) {
            courseFlags.markLessonContent(record.getChapterId(),
                    record.getLessonId());
        } else if (record.getType() == FileType.LessonEndMenu) {
            courseFlags.markLessonEndMenu(record.getChapterId(),
                    record.getLessonId());
        } else if (record.getType() == FileType.QuizHeader) {
            courseFlags.markQuizHeader(record.getChapterId());
        } else if (record.getType() == FileType.QuestionContent) {
            courseFlags.markQuestionContent(record.getChapterId(),
                    record.getQuestionId());
        } else if (record.getType() == FileType.CorrectAnswer) {
            courseFlags.markQuestionCorrectAnswer(record.getChapterId(),
                    record.getQuestionId());
        } else if (record.getType() == FileType.WrongAnswer) {
            courseFlags.markQuestionWrongAnswer(record.getChapterId(),
                    record.getQuestionId());
        } else if (record.getType() == FileType.ChapterEndMenu) {
            courseFlags.markChapterEndMenu(record.getChapterId());
        } else if (record.getType() == FileType.Score) {
            courseFlags.markScoreFile(record.getChapterId(),
                    record.getScoreID());
        }
    }

    private void processAnswerOptionRecordList(
            List<Record> answerOptionRecordList) {
        for (Record answerRecord : answerOptionRecordList) {
            coursePopulateService
                    .updateCorrectAnswer(
                            MobileAcademyConstants.CHAPTER
                                    + String.format("%02d",
                                            answerRecord.getChapterId()),
                            MobileAcademyConstants.QUESTION
                                    + String.format("%02d",
                                            answerRecord.getQuestionId()),
                            String.valueOf(answerRecord.getAnswerId()));
        }
    }

    private boolean checkRecordConsistencyAndMarkFlag(Record record,
            ChapterContent chapterContent, CourseFlags courseFlags) {
        if (record.getType() == FileType.LessonContent) {
            for (LessonContent lessonContent : chapterContent.getLessons()) {
                if (lessonContent.getLessonNumber() == record.getLessonId()
                        && lessonContent.getName().equalsIgnoreCase(
                                MobileAcademyConstants.CONTENT_LESSON)) {
                    if (lessonContent.getAudioFile() != record.getFileName()) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
            courseFlags.markLessonContent(record.getChapterId(),
                    record.getLessonId());

        } else if (record.getType() == FileType.LessonEndMenu) {
            for (LessonContent lessonContent : chapterContent.getLessons()) {
                if ((lessonContent.getLessonNumber() == record.getLessonId())
                        && (lessonContent.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_MENU))) {
                    if (lessonContent.getAudioFile() != record.getFileName()) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
            courseFlags.markLessonEndMenu(record.getChapterId(),
                    record.getLessonId());

        } else if (record.getType() == FileType.QuizHeader) {
            QuizContent quiz = chapterContent.getQuiz();
            if ((quiz.getName()
                    .equalsIgnoreCase(MobileAcademyConstants.CONTENT_QUIZ_HEADER))) {
                quiz.setAudioFile(record.getFileName());
            }
            courseFlags.markQuizHeader(record.getChapterId());

        } else if (record.getType() == FileType.QuestionContent) {
            for (QuestionContent questionContent : chapterContent.getQuiz()
                    .getQuestions()) {
                if ((questionContent.getQuestionNumber() == record
                        .getQuestionId())
                        && (questionContent.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_QUESTION))) {
                    if (questionContent.getAudioFile() != record.getFileName()) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
            courseFlags.markQuestionContent(record.getChapterId(),
                    record.getQuestionId());

        } else if (record.getType() == FileType.CorrectAnswer) {
            for (QuestionContent questionContent : chapterContent.getQuiz()
                    .getQuestions()) {
                if ((questionContent.getQuestionNumber() == record
                        .getQuestionId())
                        && (questionContent.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_CORRECT_ANSWER))) {
                    if (questionContent.getAudioFile() != record.getFileName()) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
            courseFlags.markQuestionCorrectAnswer(record.getChapterId(),
                    record.getQuestionId());

        } else if (record.getType() == FileType.WrongAnswer) {
            for (QuestionContent questionContent : chapterContent.getQuiz()
                    .getQuestions()) {
                if ((questionContent.getQuestionNumber() == record
                        .getQuestionId())
                        && (questionContent.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_WRONG_ANSWER))) {
                    if (questionContent.getAudioFile() != record.getFileName()) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
            courseFlags.markQuestionWrongAnswer(record.getChapterId(),
                    record.getQuestionId());

        } else if (record.getType() == FileType.ChapterEndMenu) {
            if (chapterContent.getName().equalsIgnoreCase(
                    MobileAcademyConstants.CONTENT_MENU))
                if (chapterContent.getAudioFile() != record.getFileName()) {
                    return false;
                }
            courseFlags.markChapterEndMenu(record.getChapterId());

        } else if (record.getType() == FileType.Score) {
            for (Score scoreContent : chapterContent.getScores()) {
                if (scoreContent.getName().equalsIgnoreCase(
                        MobileAcademyConstants.SCORE
                                + String.format("%02d", record.getScoreID()))) {
                    if (scoreContent.getAudioFile() != record.getFileName()) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
            courseFlags.markScoreFile(record.getChapterId(),
                    record.getScoreID());
        }
        // log file name doesn't match the original in dB.
        return true;
    }

    private List<ChapterContent> createChapterContentPrototype() {
        List<ChapterContent> listOfChapters = new ArrayList<ChapterContent>();

        for (int chapterCount = 1; chapterCount <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCount++) {
            List<LessonContent> lessons = createLessonList();
            List<QuestionContent> questions = createQuestionList();
            List<Score> scores = createScoresList();
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

    private List<Score> createScoresList() {
        List<Score> scoreList = new ArrayList<>();
        for (int scoreCount = 0; scoreCount <= MobileAcademyConstants.NUM_OF_SCORES; scoreCount++) {
            Score score = new Score(MobileAcademyConstants.SCORE
                    + String.format("%02d", scoreCount), null);
            scoreList.add(score);
        }
        return scoreList;
    }

    private List<QuestionContent> createQuestionList() {
        List<QuestionContent> questionList = new ArrayList<>();
        for (int questionCount = 1; questionCount <= MobileAcademyConstants.NUM_OF_QUESTIONS; questionCount++) {
            QuestionContent questionContent = new QuestionContent(
                    questionCount, MobileAcademyConstants.CONTENT_QUESTION,
                    null);
            questionList.add(questionContent);
            questionContent = new QuestionContent(questionCount,
                    MobileAcademyConstants.CONTENT_CORRECT_ANSWER, null);
            questionList.add(questionContent);
            questionContent = new QuestionContent(questionCount,
                    MobileAcademyConstants.CONTENT_WRONG_ANSWER, null);
            questionList.add(questionContent);
        }
        return questionList;
    }

    private List<LessonContent> createLessonList() {
        List<LessonContent> lessonList = new ArrayList<>();
        for (int lessonCount = 1; lessonCount <= MobileAcademyConstants.NUM_OF_LESSONS; lessonCount++) {
            LessonContent lessonContent = new LessonContent(lessonCount,
                    MobileAcademyConstants.CONTENT_MENU, null);
            lessonList.add(lessonContent);
            lessonContent = new LessonContent(lessonCount,
                    MobileAcademyConstants.CONTENT_LESSON, null);
            lessonList.add(lessonContent);
        }
        return lessonList;
    }

    private void updateRecordInContentProcessedTable(
            CourseRawContent courseRawContent) {
        courseProcessedContentService.create(new CourseProcessedContent(Integer
                .parseInt(courseRawContent.getContentId()), courseRawContent
                .getCircle().toUpperCase(), Integer.parseInt(courseRawContent
                .getLanguageLocationCode()), courseRawContent.getContentName()
                .toUpperCase(), ContentType.getFor(courseRawContent
                .getContentType()), courseRawContent.getContentFile(), Integer
                .parseInt(courseRawContent.getContentDuration()),
                courseRawContent.getMetaData().toUpperCase()));
    }

    private Record validateRawContent(CourseRawContent courseRawContent) {
        Record record = new Record();

        if (!validateContentName(courseRawContent, record)) {
            return null;
        }

        if (record.getType() == FileType.QuestionContent) {
            String metaData = courseRawContent.getMetaData();
            if (metaData.isEmpty()) {
                logger.info("No MetaData found");
                return null;
            }
            if (!metaData.substring(0, metaData.indexOf(":")).equalsIgnoreCase(
                    "CorrectAnswer")) {
                // log correctAnswer in incorrect format
                logger.info("*****Error: CorrectAnswer not found*****");
                return null;
            } else {
                try {
                    record.setAnswerId(Integer.parseInt(metaData
                            .substring(metaData.indexOf(":") + 1)));
                } catch (NumberFormatException exception) {
                    // log chapter-id NOT NUMERIC
                    logger.info("*****Error: CorrectAnswerOption not Numeric*****");
                    return null;
                }
            }
        }

        record.setFileName(courseRawContent.getContentFile());
        return record;
    }

    private boolean validateContentName(CourseRawContent courseRawContent,
            Record record) {
        String contentName = courseRawContent.getContentName().trim();
        if (contentName.indexOf("_") == -1) {
            logger.info("*****Error: ContentName not separated by _*****");
            return false;
        }

        String chapterString = contentName.substring(0,
                contentName.indexOf("_"));
        String subString = contentName.substring(1 + contentName.indexOf("_"));

        if (StringUtils.isBlank(subString)) {
            logger.info("*****Error: Unable to find complete content name*****");
            return false;
        }

        if (!chapterString.substring(0, chapterString.length() - 2)
                .equalsIgnoreCase("Chapter")) {
            // log Chapter in incorrect format
            logger.info("*****Error: Chapter not found*****");
            return false;
        } else {
            try {
                record.setChapterId(Integer.parseInt(chapterString
                        .substring(chapterString.length() - 2)));
            } catch (NumberFormatException exception) {
                // log CHAPTER-id NOT NUMERIC
                logger.info("*****Error: ChapterID not Numeric*****");
                return false;
            }
        }

        if (!isTypeDeterminable(record, subString))
            return false;

        return true;
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
            // log unable to determine the index in content name
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

    private void checkTypeAndAddToChapterContent(Record record,
            ChapterContent chapterContent, CourseFlags courseFlags) {

        if (record.getType() == FileType.LessonContent) {
            List<LessonContent> lessons = chapterContent.getLessons();
            for (LessonContent lesson : lessons) {
                if ((lesson.getLessonNumber() == record.getLessonId())
                        && (lesson.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_LESSON))) {
                    lesson.setAudioFile(record.getFileName());
                }
            }
            courseFlags.markLessonContent(record.getChapterId(),
                    record.getLessonId());

        } else if (record.getType() == FileType.LessonEndMenu) {
            List<LessonContent> lessons = chapterContent.getLessons();
            for (LessonContent lesson : lessons) {
                if ((lesson.getLessonNumber() == record.getLessonId())
                        && (lesson.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_MENU))) {
                    lesson.setAudioFile(record.getFileName());
                }
            }
            courseFlags.markLessonEndMenu(record.getChapterId(),
                    record.getLessonId());

        } else if (record.getType() == FileType.QuizHeader) {
            QuizContent quiz = chapterContent.getQuiz();
            if ((quiz.getName()
                    .equalsIgnoreCase(MobileAcademyConstants.CONTENT_QUIZ_HEADER))) {
                quiz.setAudioFile(record.getFileName());
            }
            courseFlags.markQuizHeader(record.getChapterId());

        } else if (record.getType() == FileType.QuestionContent) {
            List<QuestionContent> questions = chapterContent.getQuiz()
                    .getQuestions();
            for (QuestionContent question : questions) {
                if ((question.getQuestionNumber() == record.getQuestionId())
                        && (question.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_QUESTION))) {
                    question.setAudioFile(record.getFileName());
                }
            }
            courseFlags.markQuestionContent(record.getChapterId(),
                    record.getQuestionId());

        } else if (record.getType() == FileType.CorrectAnswer) {
            List<QuestionContent> questions = chapterContent.getQuiz()
                    .getQuestions();
            for (QuestionContent question : questions) {
                if ((question.getQuestionNumber() == record.getQuestionId())
                        && (question.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_CORRECT_ANSWER))) {
                    question.setAudioFile(record.getFileName());
                }
            }
            courseFlags.markQuestionCorrectAnswer(record.getChapterId(),
                    record.getQuestionId());

        } else if (record.getType() == FileType.WrongAnswer) {
            List<QuestionContent> questions = chapterContent.getQuiz()
                    .getQuestions();
            for (QuestionContent question : questions) {
                if ((question.getQuestionNumber() == record.getQuestionId())
                        && (question.getName()
                                .equalsIgnoreCase(MobileAcademyConstants.CONTENT_WRONG_ANSWER))) {
                    question.setAudioFile(record.getFileName());
                }
            }
            courseFlags.markQuestionWrongAnswer(record.getChapterId(),
                    record.getQuestionId());

        } else if (record.getType() == FileType.ChapterEndMenu) {
            if (chapterContent.getName().equalsIgnoreCase(
                    MobileAcademyConstants.CONTENT_MENU))
                chapterContent.setAudioFile(record.getFileName());
            courseFlags.markChapterEndMenu(record.getChapterId());

        } else if (record.getType() == FileType.Score) {
            List<Score> scores = chapterContent.getScores();
            for (Score score : scores) {
                if (score.getName().equalsIgnoreCase(
                        MobileAcademyConstants.SCORE
                                + String.format("%02d", record.getScoreID()))) {
                    score.setAudioFile(record.getFileName());
                }
            }
            courseFlags.markScoreFile(record.getChapterId(),
                    record.getScoreID());

        }
    }

}