package org.motechproject.nms.mobileacademy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.ContentType;
import org.motechproject.nms.mobileacademy.commons.CourseFlag;
import org.motechproject.nms.mobileacademy.commons.FileType;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.commons.OperatorDetails;
import org.motechproject.nms.mobileacademy.commons.Record;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;
import org.motechproject.nms.mobileacademy.helper.RecordsProcessHelper;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseContentCsvService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.motechproject.nms.mobileacademy.service.RecordsProcessService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * This class contains the implementation for RecordsProcessService to process
 * CSV records
 *
 *
 */
@Service("RecordsProcessService")
public class RecordsProcessServiceImpl implements RecordsProcessService {

    @Autowired
    private CourseContentCsvService courseContentCsvService;

    @Autowired
    private CourseProcessedContentService courseProcessedContentService;

    @Autowired
    private ChapterContentDataService chapterContentDataService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(RecordsProcessServiceImpl.class);

    @Override
    public String processRawRecords(List<CourseContentCsv> courseContentCsvs,
            String csvFileName) {
        LOGGER.info("Record Processing Started for csv file: {}", csvFileName);
        chapterContentDataService
                .doInTransaction(new TransactionCallback<ChapterContent>() {

                    List<CourseContentCsv> courseContentCsvs;

                    String csvFileName;

                    private TransactionCallback<ChapterContent> init(
                            List<CourseContentCsv> courseContentCsvs,
                            String csvFileName) {
                        this.courseContentCsvs = courseContentCsvs;
                        this.csvFileName = csvFileName;
                        return this;
                    }

                    @Override
                    public ChapterContent doInTransaction(
                            TransactionStatus status) {
                        ChapterContent transactionObject = null;
                        processRawRecordsInTransaction(courseContentCsvs,
                                csvFileName);
                        return transactionObject;
                    }
                }.init(courseContentCsvs, csvFileName));
        LOGGER.info("Record Processing complete for csv file: {}", csvFileName);
        return "Records Processed Successfully";
    }

    /*
     * This function processes all the CSV upload records. This function is
     * called in a transaction call so in case of any error, the changes are
     * reverted back.
     */
    private void processRawRecordsInTransaction(
            List<CourseContentCsv> courseContentCsvs, String csvFileName) {
        DateTime timeOfUpload = new DateTime();
        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus("",
                csvFileName, timeOfUpload, 0, 0);
        BulkUploadError bulkUploadError = new BulkUploadError(csvFileName,
                timeOfUpload, RecordType.COURSE_CONTENT, "", "", "");

        Map<Integer, List<CourseContentCsv>> mapForAddRecords = new HashMap<Integer, List<CourseContentCsv>>();
        Map<String, List<CourseContentCsv>> mapForModifyRecords = new HashMap<String, List<CourseContentCsv>>();

        List<Integer> listOfExistingLlc = courseProcessedContentService
                .getListOfAllExistingLlcs();
        OperatorDetails operatorDetails = new OperatorDetails();

        if (CollectionUtils.isNotEmpty(courseContentCsvs)
                && listOfExistingLlc != null) {
            // set user details from first record
            operatorDetails.setCreator(courseContentCsvs.get(0).getCreator());
            bulkUploadStatus.setUploadedBy(operatorDetails.getCreator());
            operatorDetails.setModifiedBy(courseContentCsvs.get(0)
                    .getModifiedBy());
            operatorDetails.setOwner(courseContentCsvs.get(0).getOwner());

            Iterator<CourseContentCsv> recordIterator = courseContentCsvs
                    .iterator();
            while (recordIterator.hasNext()) {
                CourseContentCsv courseContentCsv = recordIterator.next();
                try {
                    RecordsProcessHelper.validateSchema(courseContentCsv);
                } catch (DataValidationException ex) {
                    RecordsProcessHelper.processError(
                            bulkUploadError,
                            ex,
                            String.format("ContentID: %s",
                                    courseContentCsv.getContentId()));
                    bulkUploadErrLogService
                            .writeBulkUploadErrLog(bulkUploadError);
                    bulkUploadStatus.incrementFailureCount();
                    recordIterator.remove();
                    courseContentCsvService.delete(courseContentCsv);
                    LOGGER.warn("Schema Validation failed for Content ID: {}",
                            courseContentCsv.getContentId());
                    continue;
                }
                int languageLocCode = Integer.parseInt(courseContentCsv
                        .getLanguageLocationCode());
                /*
                 * If LLC corresponding to record exists, consider the record
                 * for modification of course
                 */
                if (listOfExistingLlc.contains(languageLocCode)) {
                    RecordsProcessHelper.putRecordInModifyMap(
                            mapForModifyRecords, courseContentCsv);
                    LOGGER.debug(
                            "Record moved to Modify Map for Content ID: {}",
                            courseContentCsv.getContentId());
                }
                /*
                 * If LLC corresponding to record doesn'tt exist, consider the
                 * record for addition of course
                 */
                else {
                    RecordsProcessHelper.putRecordInAddMap(mapForAddRecords,
                            courseContentCsv);
                    LOGGER.debug(
                            "Record moved to Addition Map for Content ID: {}",
                            courseContentCsv.getContentId());
                }
                recordIterator.remove();
            }
        }

        processAddRecords(mapForAddRecords, bulkUploadError, bulkUploadStatus,
                operatorDetails);
        processModificationRecords(mapForModifyRecords, bulkUploadError,
                bulkUploadStatus, operatorDetails);

        bulkUploadErrLogService
                .writeBulkUploadProcessingSummary(bulkUploadStatus);
    }

    /*
     * This function takes The Map having CourserawContent Records for the
     * modification and processes them
     */
    private void processModificationRecords(
            Map<String, List<CourseContentCsv>> mapForModifyRecords,
            BulkUploadError bulkUploadError, BulkUploadStatus bulkUploadStatus,
            OperatorDetails operatorDetails) {
        if (!mapForModifyRecords.isEmpty()) {
            /* Iterator for different content names */
            Iterator<String> contentNamesIterator = mapForModifyRecords
                    .keySet().iterator();
            while (contentNamesIterator.hasNext()) {
                String contentName = contentNamesIterator.next();
                List<CourseContentCsv> csvRecordList = mapForModifyRecords
                        .get(contentName);
                if (CollectionUtils.isNotEmpty(csvRecordList)) {
                    /*
                     * Iterator for records of different LLCs for a specific
                     * content name
                     */
                    Iterator<CourseContentCsv> csvRecordIterator = csvRecordList
                            .iterator();

                    while (csvRecordIterator.hasNext()) {
                        CourseContentCsv csvRecord = csvRecordIterator.next();
                        Record record = new Record();
                        try {
                            RecordsProcessHelper.validateRawContent(csvRecord,
                                    record);
                        } catch (DataValidationException exc) {
                            LOGGER.warn(
                                    "Record Validation failed for content ID: {}",
                                    csvRecord.getContentId());
                            RecordsProcessHelper.processError(
                                    bulkUploadError,
                                    exc,
                                    String.format("ContentID: %s",
                                            csvRecord.getContentId()));
                            bulkUploadErrLogService
                                    .writeBulkUploadErrLog(bulkUploadError);
                            bulkUploadStatus.incrementFailureCount();
                            csvRecordIterator.remove();
                            courseContentCsvService.delete(csvRecord);
                            continue;
                        }

                        /*
                         * If Record is changing the Audio File name or correct
                         * answer option for a question, process it at last. Let
                         * them remain on map
                         */
                        if (isRecordChangingTheFileName(record)
                                || isRecordChangingTheAnswerOption(record)) {
                            continue;
                        }
                        /*
                         * Records corresponding to ContentID and Duration
                         * change will be processed here.
                         */
                        else {
                            int languageLocCode = Integer.parseInt(csvRecord
                                    .getLanguageLocationCode());
                            CourseProcessedContent courseProcessedContent = courseProcessedContentService
                                    .getRecordforModification(csvRecord
                                            .getCircle(), languageLocCode,
                                            csvRecord.getContentName().trim());
                            if (courseProcessedContent != null) {
                                int contentDuration = Integer
                                        .parseInt(csvRecord
                                                .getContentDuration());
                                int contentId = Integer.parseInt(csvRecord
                                        .getContentId());
                                if ((courseProcessedContent
                                        .getContentDuration() != contentDuration)
                                        || (courseProcessedContent
                                                .getContentID() != contentId)) {
                                    LOGGER.info(
                                            "ContentID and duration updated for content name: {}, LLC: {}",
                                            csvRecord.getContentName(),
                                            csvRecord.getLanguageLocationCode());
                                    courseProcessedContent
                                            .setContentDuration(contentDuration);
                                    courseProcessedContent
                                            .setContentID(contentId);
                                    courseProcessedContent
                                            .setModifiedBy(operatorDetails
                                                    .getModifiedBy());
                                    courseProcessedContentService
                                            .update(courseProcessedContent);
                                }
                            }
                            bulkUploadStatus.incrementSuccessCount();
                            csvRecordIterator.remove();
                            courseContentCsvService.delete(csvRecord);
                        }
                    }
                }
                csvRecordList = mapForModifyRecords.get(contentName);
                if (CollectionUtils.isEmpty(csvRecordList)) {
                    contentNamesIterator.remove();
                }
            }
        }
        // Start Processing the file change records:
        if (!mapForModifyRecords.isEmpty()) {
            processModificationRecordForAnswerOrFileChange(mapForModifyRecords,
                    bulkUploadError, bulkUploadStatus, operatorDetails);
        }
    }

    /*
     * This function process the modification records for which there is change
     * in Audio File name or correct answer option
     */
    private void processModificationRecordForAnswerOrFileChange(
            Map<String, List<CourseContentCsv>> mapForModifyRecords,
            BulkUploadError bulkUploadError, BulkUploadStatus bulkUploadStatus,
            OperatorDetails operatorDetails) {
        Iterator<String> contentNamesIterator = mapForModifyRecords.keySet()
                .iterator();
        while (contentNamesIterator.hasNext()) {
            String contentName = contentNamesIterator.next();
            boolean flagForUpdatingMetaData = false;
            boolean flagForAbortingModification = false;
            // Getting new List as the list return is unmodifiable
            List<Integer> listOfExistingLlc = new ArrayList<Integer>(
                    courseProcessedContentService.getListOfAllExistingLlcs());
            List<CourseContentCsv> courseContentCsvs = mapForModifyRecords
                    .get(contentName);
            if (courseContentCsvs.size() < listOfExistingLlc.size()) {
                LOGGER.warn(
                        "Records corresponding to all the existing LLCs not received for modification against content name: {}",
                        contentName);
                bulkUploadError.setRecordDetails(String.format(
                        "ContentName: %s", contentName));
                bulkUploadError
                        .setErrorCategory(ErrorCategoryConstants.INCONSISTENT_DATA);
                bulkUploadError.setErrorDescription(String.format(
                        MobileAcademyConstants.INSUFFICIENT_RECORDS_FOR_MODIFY,
                        contentName));
                bulkUploadErrLogService.writeBulkUploadErrLog(bulkUploadError);
                contentNamesIterator.remove();
                deleteCourseRawContentsByList(courseContentCsvs, true,
                        bulkUploadStatus);
                continue;
            }
            String fileName = mapForModifyRecords.get(contentName).get(0)
                    .getContentFile();
            String metaData = "";
            /*
             * This block of code is just being written for the purpose to know
             * whether this bunch of record refers to questionContent type or
             * not
             */
            Record record = new Record();
            try {
                RecordsProcessHelper.validateRawContent(mapForModifyRecords
                        .get(contentName).get(0), record);
            } catch (DataValidationException e1) {
                LOGGER.error("", e1);
            }
            if ((record.getType() == FileType.QUESTION_CONTENT)
                    && (isRecordChangingTheAnswerOption(record))) {
                metaData = mapForModifyRecords.get(contentName).get(0)
                        .getMetaData();
                flagForUpdatingMetaData = true;
            }
            Iterator<CourseContentCsv> courseRawContentsIterator = courseContentCsvs
                    .iterator();
            while (courseRawContentsIterator.hasNext()) {
                CourseContentCsv courseContentCsv = courseRawContentsIterator
                        .next();
                int languageLocCode = Integer.parseInt(courseContentCsv
                        .getLanguageLocationCode());
                if (!fileName.equals(courseContentCsv.getContentFile())) {
                    LOGGER.warn(
                            "Content file name does not match for content name: {}, contentID: {}",
                            contentName, courseContentCsv.getContentId());
                    flagForAbortingModification = true;
                }
                /*
                 * Check for consistency of metaData only if record corresponds
                 * to question content type
                 */
                if (flagForUpdatingMetaData) {
                    if (!metaData.equalsIgnoreCase(courseContentCsv
                            .getMetaData())) {
                        LOGGER.warn(
                                "Correct Answer Option(Metadata) does not match for content name: {}, contentID: {}",
                                contentName, courseContentCsv.getContentId());
                        flagForAbortingModification = true;
                    }
                }
                if (flagForAbortingModification) {
                    LOGGER.warn(
                            "Course modification aborted for content name: {}",
                            contentName);
                    bulkUploadError.setRecordDetails(String.format(
                            "Content ID: %s", courseContentCsv.getContentId()));
                    bulkUploadError
                            .setErrorCategory(ErrorCategoryConstants.INCONSISTENT_DATA);
                    bulkUploadError
                            .setErrorDescription(String
                                    .format(MobileAcademyConstants.INCONSISTENT_RECORD_FOR_MODIFY,
                                            contentName));
                    bulkUploadErrLogService
                            .writeBulkUploadErrLog(bulkUploadError);
                    deleteCourseRawContentsByList(courseContentCsvs, true,
                            bulkUploadStatus);
                    contentNamesIterator.remove();
                    break;
                }
                listOfExistingLlc.remove(Integer.valueOf(languageLocCode));
            }
            if (flagForAbortingModification) {
                continue;
            }
            // If data has arrived for all the existing LLCS.
            if (CollectionUtils.isEmpty(listOfExistingLlc)) {
                updateModificationRecordsInSystem(mapForModifyRecords,
                        bulkUploadStatus, operatorDetails, contentName,
                        fileName, flagForUpdatingMetaData);
                courseService.updateCourseVersion(operatorDetails.getCreator());
            } else {
                // Not sufficient records for a course
                LOGGER.warn("Course not modified for content name: {}",
                        contentName);
                LOGGER.warn("Records for all exisiting LLCs not recieved");

                bulkUploadError.setRecordDetails(String.format(
                        "Content Name: %s", contentName));
                bulkUploadError
                        .setErrorCategory(ErrorCategoryConstants.INCONSISTENT_DATA);
                bulkUploadError.setErrorDescription(String.format(
                        MobileAcademyConstants.INSUFFICIENT_RECORDS_FOR_MODIFY,
                        contentName));
                bulkUploadErrLogService.writeBulkUploadErrLog(bulkUploadError);

                deleteCourseRawContentsByList(
                        mapForModifyRecords.get(contentName), true,
                        bulkUploadStatus);
            }
        }
    }

    /*
     * This function is used for updating the modification record in the system
     * with given details
     * 
     * @param mapForModifyRecords: Map having the modification raw records
     * 
     * @param bulkUploadStatus: status for bulk upload
     * 
     * @param operatorDetails: details of operator performing the modification
     * 
     * @param contentName: content Name for which modification need to be done
     * 
     * @param fileName: content audio file name
     * 
     * @param flagForUpdatingMetaData: flag if metadata need to be updated or
     * not
     */
    private void updateModificationRecordsInSystem(
            Map<String, List<CourseContentCsv>> mapForModifyRecords,
            BulkUploadStatus bulkUploadStatus, OperatorDetails operatorDetails,
            String contentName, String fileName, boolean flagForUpdatingMetaData) {
        /*
         * This is done just to know the type of file to which this bunch of
         * modification record refers to.
         */
        Record record = new Record();
        Integer correctAnswerOption = null;
        try {
            if (mapForModifyRecords.get(contentName) != null) {
                RecordsProcessHelper.validateRawContent(mapForModifyRecords
                        .get(contentName).get(0), record);
            }
        } catch (DataValidationException e) {
            LOGGER.debug(e.getMessage(), e);
        }
        if (isRecordChangingTheFileName(record)) {
            determineTypeAndUpdateChapterContent(record, operatorDetails);
            LOGGER.info("Audio file name has been changed for contentName: {}",
                    contentName);
        }
        if (flagForUpdatingMetaData) {
            correctAnswerOption = record.getAnswerId();
            courseService
                    .updateCorrectAnswer(
                            MobileAcademyConstants.CHAPTER
                                    + String.format(
                                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                            record.getChapterId()),
                            MobileAcademyConstants.QUESTION
                                    + String.format(
                                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                            record.getQuestionId()), Integer
                                    .toString(correctAnswerOption),
                            operatorDetails);
            LOGGER.info(
                    "Correct Answer Option for contentName: {} has been changed to :{}",
                    contentName, correctAnswerOption);
        }
        List<CourseContentCsv> fileModifyingRecords = mapForModifyRecords
                .get(contentName);
        Iterator<CourseContentCsv> fileModifyingRecordsIterator = fileModifyingRecords
                .iterator();
        while (fileModifyingRecordsIterator.hasNext()) {
            CourseContentCsv courseContentCsv = fileModifyingRecordsIterator
                    .next();
            int languageLocCode = Integer.parseInt(courseContentCsv
                    .getLanguageLocationCode());
            CourseProcessedContent courseProcessedContent = courseProcessedContentService
                    .getRecordforModification(courseContentCsv.getCircle(),
                            languageLocCode, contentName);
            if (courseProcessedContent != null) {
                courseProcessedContent.setContentFile(fileName);
                courseProcessedContent.setContentDuration(Integer
                        .parseInt(courseContentCsv.getContentDuration()));
                courseProcessedContent.setContentID(Integer
                        .parseInt(courseContentCsv.getContentId()));
                courseProcessedContent.setModifiedBy(operatorDetails
                        .getModifiedBy());
                if (flagForUpdatingMetaData) {
                    courseProcessedContent
                            .setMetadata(MobileAcademyConstants.CONTENT_CORRECT_ANSWER
                                    .toUpperCase()
                                    + ":"
                                    + Integer.toString(correctAnswerOption));
                }
                courseProcessedContentService.update(courseProcessedContent);
                LOGGER.debug(
                        "Record modified in ContentProcessedTable for LLC: {}, content Name: {}",
                        languageLocCode, contentName);
            }
            bulkUploadStatus.incrementSuccessCount();
            fileModifyingRecordsIterator.remove();
            courseContentCsvService.delete(courseContentCsv);
        }
        LOGGER.warn("Course modified for content name: {}", contentName);
    }

    /*
     * This is used for deleting the records from internal list and updating the
     * bulkUploadStatus for error and success scenarios
     */
    private void deleteCourseRawContentsByList(
            List<CourseContentCsv> courseContentCsvs, Boolean hasErrorOccured,
            BulkUploadStatus bulkUploadStatus) {
        if (CollectionUtils.isNotEmpty(courseContentCsvs)) {
            for (CourseContentCsv courseContentCsv : courseContentCsvs) {
                if (hasErrorOccured) {
                    bulkUploadStatus.incrementFailureCount();
                } else {
                    bulkUploadStatus.incrementSuccessCount();
                }
                courseContentCsvService.delete(courseContentCsv);
            }
        }
    }

    /*
     * This function is used to update the filename on the basis of File-type in
     * record object, into courseContent tables
     */
    private void determineTypeAndUpdateChapterContent(Record record,
            OperatorDetails operatorDetails) {
        if (record.getType() == FileType.LESSON_CONTENT) {
            courseService.setLessonContent(record.getChapterId(),
                    record.getLessonId(),
                    MobileAcademyConstants.CONTENT_LESSON,
                    record.getFileName(), operatorDetails);
        } else if (record.getType() == FileType.LESSON_END_MENU) {
            courseService.setLessonContent(record.getChapterId(),
                    record.getLessonId(), MobileAcademyConstants.CONTENT_MENU,
                    record.getFileName(), operatorDetails);
        } else if (record.getType() == FileType.QUESTION_CONTENT) {
            courseService.setQuestionContent(record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_QUESTION,
                    record.getFileName(), operatorDetails);
        } else if (record.getType() == FileType.CORRECT_ANSWER) {
            courseService.setQuestionContent(record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_CORRECT_ANSWER,
                    record.getFileName(), operatorDetails);
        } else if (record.getType() == FileType.WRONG_ANSWER) {
            courseService.setQuestionContent(record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_WRONG_ANSWER,
                    record.getFileName(), operatorDetails);
        } else if (record.getType() == FileType.CHAPTER_END_MENU) {
            courseService.setChapterContent(record.getChapterId(),
                    MobileAcademyConstants.CONTENT_MENU, record.getFileName(),
                    operatorDetails);
        } else if (record.getType() == FileType.QUIZ_HEADER) {
            courseService.setQuizContent(record.getChapterId(),
                    MobileAcademyConstants.CONTENT_QUIZ_HEADER,
                    record.getFileName(), operatorDetails);
        } else if (record.getType() == FileType.SCORE) {
            courseService.setScore(record.getChapterId(), record.getScoreID(),
                    MobileAcademyConstants.SCORE, record.getFileName(),
                    operatorDetails);
        }
    }

    /*
     * This checks if a modify record is also changing the name of the file
     * currently existing the system. If yes, it returns true.
     */
    private boolean isRecordChangingTheFileName(Record record) {
        boolean status = false;
        List<ChapterContent> chapterContents = courseService
                .getAllChapterContents();
        if (record.getType() == FileType.LESSON_CONTENT) {
            LessonContent lessonContent = courseService
                    .getLessonContent(chapterContents, record.getChapterId(),
                            record.getLessonId(),
                            MobileAcademyConstants.CONTENT_LESSON);
            if (!lessonContent.getAudioFile().equals(record.getFileName())) {
                status = true;
            }
        } else if (record.getType() == FileType.LESSON_END_MENU) {
            LessonContent lessonContent = courseService.getLessonContent(
                    chapterContents, record.getChapterId(),
                    record.getLessonId(), MobileAcademyConstants.CONTENT_MENU);
            if (!lessonContent.getAudioFile().equals(record.getFileName())) {
                status = true;
            }
        } else if (record.getType() == FileType.QUESTION_CONTENT) {
            QuestionContent questionContent = courseService.getQuestionContent(
                    chapterContents, record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_QUESTION);
            if (!questionContent.getAudioFile().equals(record.getFileName())) {
                status = true;
            }
        } else if (record.getType() == FileType.CORRECT_ANSWER) {
            QuestionContent questionContent = courseService.getQuestionContent(
                    chapterContents, record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_CORRECT_ANSWER);
            if (!questionContent.getAudioFile().equals(record.getFileName())) {
                status = true;
            }
        } else if (record.getType() == FileType.WRONG_ANSWER) {
            QuestionContent questionContent = courseService.getQuestionContent(
                    chapterContents, record.getChapterId(),
                    record.getQuestionId(),
                    MobileAcademyConstants.CONTENT_WRONG_ANSWER);
            if (!questionContent.getAudioFile().equals(record.getFileName())) {
                status = true;
            }
        } else if (record.getType() == FileType.CHAPTER_END_MENU) {
            ChapterContent chapterContent = courseService.getChapterContent(
                    chapterContents, record.getChapterId(),
                    MobileAcademyConstants.CONTENT_MENU);
            if (!chapterContent.getAudioFile().equals(record.getFileName())) {
                status = true;
            }
        } else if (record.getType() == FileType.QUIZ_HEADER) {
            QuizContent quizContent = courseService.getQuizContent(
                    chapterContents, record.getChapterId(),
                    MobileAcademyConstants.CONTENT_QUIZ_HEADER);
            if (!quizContent.getAudioFile().equals(record.getFileName())) {
                status = true;
            }
        } else if (record.getType() == FileType.SCORE) {
            ScoreContent scoreContent = courseService.getScore(chapterContents,
                    record.getChapterId(), record.getScoreID(),
                    MobileAcademyConstants.SCORE);
            if (!scoreContent.getAudioFile().equals(record.getFileName())) {
                status = true;
            }
        }
        return status;
    }

    /*
     * This function takes the list of CourseContentCsv records against which
     * the file need to be added into the course
     */
    private void processAddRecords(
            Map<Integer, List<CourseContentCsv>> mapForAddRecords,
            BulkUploadError bulkUploadError, BulkUploadStatus bulkUploadStatus,
            OperatorDetails operatorDetails) {
        boolean populateCourseStructure = false;
        List<Record> answerOptionRecordList = new ArrayList<Record>();
        boolean abortAdditionProcess = false;
        if (!mapForAddRecords.isEmpty()) {
            Iterator<Integer> distictLLCIterator = mapForAddRecords.keySet()
                    .iterator();
            while (distictLLCIterator.hasNext()) {
                CourseFlag courseFlag = new CourseFlag();
                abortAdditionProcess = false;
                populateCourseStructure = false;
                Integer languageLocCode = distictLLCIterator.next();
                List<CourseContentCsv> courseContentCsvs = mapForAddRecords
                        .get(languageLocCode);
                if (CollectionUtils.isNotEmpty(courseContentCsvs)) {
                    if (courseContentCsvs.size() != MobileAcademyConstants.MIN_FILES_PER_COURSE) {
                        LOGGER.warn(
                                "There must be exact {} records to populate the course corresponding to LLC:{}.",
                                MobileAcademyConstants.MIN_FILES_PER_COURSE,
                                languageLocCode);
                        deleteCourseRawContentsByList(courseContentCsvs, true,
                                bulkUploadStatus);
                        bulkUploadError.setRecordDetails(String.format(
                                "LLC: %d", languageLocCode));
                        bulkUploadError
                                .setErrorCategory(ErrorCategoryConstants.INCONSISTENT_DATA);
                        bulkUploadError
                                .setErrorDescription(String
                                        .format(MobileAcademyConstants.INSUFFICIENT_RECORDS_FOR_ADD,
                                                languageLocCode));
                        bulkUploadErrLogService
                                .writeBulkUploadErrLog(bulkUploadError);
                        distictLLCIterator.remove();
                        continue;
                    }
                    Course course = courseService.getMtrainingCourse();
                    if (course == null) {
                        populateCourseStructure = true;
                    }
                    answerOptionRecordList.clear();
                    List<ChapterContent> chapterContents = courseService
                            .getAllChapterContents();
                    if (CollectionUtils.isEmpty(chapterContents)) {
                        chapterContents = RecordsProcessHelper
                                .createChapterContentPrototype();
                    }
                    Iterator<CourseContentCsv> courseRawContentsIterator = courseContentCsvs
                            .iterator();
                    while (courseRawContentsIterator.hasNext()) {
                        CourseContentCsv courseContentCsv = courseRawContentsIterator
                                .next();
                        Record record = new Record();
                        try {
                            RecordsProcessHelper.validateRawContent(
                                    courseContentCsv, record);
                        } catch (DataValidationException exc) {
                            abortAdditionProcess = true;
                            LOGGER.warn(
                                    "Record validation failed for content ID: {}",
                                    courseContentCsv.getContentId());
                            RecordsProcessHelper.processError(bulkUploadError,
                                    exc, String.format("ContentID: %s",
                                            courseContentCsv.getContentId()));
                            bulkUploadErrLogService
                                    .writeBulkUploadErrLog(bulkUploadError);
                            bulkUploadError.setRecordDetails(String.format(
                                    "Content ID: %s",
                                    courseContentCsv.getContentId()));
                            bulkUploadError
                                    .setErrorCategory(ErrorCategoryConstants.INCONSISTENT_DATA);
                            bulkUploadError
                                    .setErrorDescription(String
                                            .format(MobileAcademyConstants.INCONSISTENT_RECORDS_FOR_ADD,
                                                    languageLocCode));
                            bulkUploadErrLogService
                                    .writeBulkUploadErrLog(bulkUploadError);
                            deleteCourseRawContentsByList(courseContentCsvs,
                                    true, bulkUploadStatus);
                            distictLLCIterator.remove();
                            break;
                        }
                        if (populateCourseStructure) {
                            if (record.getType() == FileType.QUESTION_CONTENT) {
                                answerOptionRecordList.add(record);
                            }
                            RecordsProcessHelper
                                    .checkTypeAndAddToChapterContent(record,
                                            chapterContents, courseFlag);
                        } else {
                            if (!checkRecordConsistencyAndMarkFlag(record,
                                    chapterContents, courseFlag)) {
                                LOGGER.info(
                                        "Record with content ID: {} is not consistent with the data already existing in the system",
                                        courseContentCsv.getContentId());
                                bulkUploadError.setRecordDetails(String.format(
                                        "Content ID: %s",
                                        courseContentCsv.getContentId()));
                                bulkUploadError
                                        .setErrorCategory(ErrorCategoryConstants.INCONSISTENT_DATA);
                                bulkUploadError
                                        .setErrorDescription(String
                                                .format(MobileAcademyConstants.INCONSISTENT_RECORDS_FOR_ADD,
                                                        languageLocCode));
                                bulkUploadErrLogService
                                        .writeBulkUploadErrLog(bulkUploadError);
                                distictLLCIterator.remove();
                                deleteCourseRawContentsByList(
                                        courseContentCsvs, true,
                                        bulkUploadStatus);
                                abortAdditionProcess = true;
                                break;
                            }
                        }
                    }
                    if (abortAdditionProcess) {
                        continue;
                    }
                    if (courseFlag.hasCompleteCourseArrived()) {
                        courseRawContentsIterator = courseContentCsvs
                                .iterator();
                        while (courseRawContentsIterator.hasNext()) {
                            CourseContentCsv courseContentCsv = courseRawContentsIterator
                                    .next();
                            bulkUploadStatus.incrementSuccessCount();
                            updateRecordInContentProcessedTable(
                                    courseContentCsv, operatorDetails);
                            courseContentCsvService.delete(courseContentCsv);
                            courseRawContentsIterator.remove();
                        }
                        // Update Course
                        if (populateCourseStructure) {
                            // Generate course in Mtraining now
                            course = courseService
                                    .populateMtrainingCourseData(operatorDetails);

                            for (int chapterCounter = 0; chapterCounter < MobileAcademyConstants.NUM_OF_CHAPTERS; chapterCounter++) {
                                ChapterContent chapterContent = chapterContents
                                        .get(chapterCounter);
                                RecordsProcessHelper
                                        .updateChapterContentForUserDetails(
                                                chapterContent, operatorDetails);
                                courseService
                                        .createChapterContent(chapterContent);
                            }
                            // Update AnswerOptionList
                            // Change the state to Active
                            processListOfAnswerOptionRecords(
                                    answerOptionRecordList, operatorDetails);
                            courseService.updateCourseState(
                                    CourseUnitState.Active, operatorDetails);

                        }
                        LOGGER.info("Course Added successfully for LLC: {}",
                                languageLocCode);
                    } else {
                        bulkUploadError.setRecordDetails(String.format(
                                "LLC: %d", languageLocCode));
                        bulkUploadError
                                .setErrorCategory(ErrorCategoryConstants.INCONSISTENT_DATA);
                        bulkUploadError
                                .setErrorDescription(String
                                        .format(MobileAcademyConstants.INSUFFICIENT_RECORDS_FOR_ADD,
                                                languageLocCode));
                        bulkUploadErrLogService
                                .writeBulkUploadErrLog(bulkUploadError);
                        LOGGER.warn(
                                "Records for complete course haven't arrived to add the course for LLC: {}",
                                languageLocCode);

                        deleteCourseRawContentsByList(courseContentCsvs, true,
                                bulkUploadStatus);
                    }
                }
            }
        }
    }

    /*
     * this function updates the correct option for different questions in the
     * mTraining module.
     */
    private void processListOfAnswerOptionRecords(
            List<Record> answerOptionRecordList, OperatorDetails operatorDetails) {
        for (Record answerRecord : answerOptionRecordList) {
            courseService
                    .updateCorrectAnswer(
                            MobileAcademyConstants.CHAPTER
                                    + String.format(
                                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                            answerRecord.getChapterId()),
                            MobileAcademyConstants.QUESTION
                                    + String.format(
                                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                                            answerRecord.getQuestionId()),
                            String.valueOf(answerRecord.getAnswerId()),
                            operatorDetails);
        }
    }

    /*
     * This function checks whether a ADD record is having the same file Name
     * for a file which is currently existing in the system. In positive
     * scenarios, it also marks for successful arrival of the file in the course
     * flags
     */
    private boolean checkRecordConsistencyAndMarkFlag(Record record,
            List<ChapterContent> chapterContents, CourseFlag courseFlag) {
        boolean status = true;
        ChapterContent chapterContent = null;
        for (ChapterContent chapter : chapterContents) {
            if (chapter.getChapterNumber() == record.getChapterId()) {
                chapterContent = chapter;
                break;
            }
        }
        if (chapterContent == null) {
            return false;
        } else {
            if ((record.getType() == FileType.LESSON_CONTENT)
                    || (record.getType() == FileType.LESSON_END_MENU)) {
                status = checkRecordConsistencyAndMarkFlagForLesson(record,
                        chapterContent, courseFlag, status);
            } else if ((record.getType() == FileType.QUESTION_CONTENT)
                    || (record.getType() == FileType.CORRECT_ANSWER)
                    || (record.getType() == FileType.WRONG_ANSWER)) {
                status = checkRecordConsistencyAndMarkFlagForQuestion(record,
                        chapterContent, courseFlag, status);
            } else if (record.getType() == FileType.QUIZ_HEADER) {
                QuizContent quizContent = chapterContent.getQuiz();
                if ((MobileAcademyConstants.CONTENT_QUIZ_HEADER
                        .equalsIgnoreCase(quizContent.getName()))) {
                    if (!quizContent.getAudioFile()
                            .equals(record.getFileName())) {
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_ORIGINAL_FILE_NAME,
                                quizContent.getAudioFile());
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_NEW_FILE_NAME,
                                record.getFileName());
                        status = false;
                    } else {
                        courseFlag.markQuizHeader(record.getChapterId());
                    }
                }
            } else if (record.getType() == FileType.CHAPTER_END_MENU) {
                if (MobileAcademyConstants.CONTENT_MENU
                        .equalsIgnoreCase(chapterContent.getName())) {
                    if (!chapterContent.getAudioFile().equals(
                            record.getFileName())) {
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_ORIGINAL_FILE_NAME,
                                chapterContent.getAudioFile());
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_NEW_FILE_NAME,
                                record.getFileName());
                        status = false;
                    } else {
                        courseFlag.markChapterEndMenu(record.getChapterId());
                    }
                }
            } else if (record.getType() == FileType.SCORE) {
                for (ScoreContent scoreContent : chapterContent.getScores()) {
                    if ((MobileAcademyConstants.SCORE + String.format(
                            MobileAcademyConstants.TWO_DIGIT_INTEGER_FORMAT,
                            record.getScoreID())).equalsIgnoreCase(scoreContent
                            .getName())) {
                        if (!scoreContent.getAudioFile().equals(
                                record.getFileName())) {
                            LOGGER.debug(
                                    MobileAcademyConstants.LOG_MSG_ORIGINAL_FILE_NAME,
                                    scoreContent.getAudioFile());
                            LOGGER.debug(
                                    MobileAcademyConstants.LOG_MSG_NEW_FILE_NAME,
                                    record.getFileName());
                            status = false;
                        } else {
                            courseFlag.markScoreFile(record.getChapterId(),
                                    record.getScoreID());
                        }
                        break;
                    }
                }
            }
        }
        return status;
    }

    private boolean checkRecordConsistencyAndMarkFlagForLesson(Record record,
            ChapterContent chapterContent, CourseFlag courseFlag, boolean status) {
        if (record.getType() == FileType.LESSON_CONTENT) {
            for (LessonContent lessonContent : chapterContent.getLessons()) {
                if (lessonContent.getLessonNumber() == record.getLessonId()
                        && MobileAcademyConstants.CONTENT_LESSON
                                .equalsIgnoreCase(lessonContent.getName())) {
                    if (!lessonContent.getAudioFile().equals(
                            record.getFileName())) {
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_ORIGINAL_FILE_NAME,
                                lessonContent.getAudioFile());
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_NEW_FILE_NAME,
                                record.getFileName());
                        status = false;
                    } else {
                        courseFlag.markLessonContent(record.getChapterId(),
                                record.getLessonId());
                    }
                    break;
                }
            }
        } else if (record.getType() == FileType.LESSON_END_MENU) {
            for (LessonContent lessonContent : chapterContent.getLessons()) {
                if ((lessonContent.getLessonNumber() == record.getLessonId())
                        && (MobileAcademyConstants.CONTENT_MENU
                                .equalsIgnoreCase(lessonContent.getName()))) {
                    if (!lessonContent.getAudioFile().equals(
                            record.getFileName())) {
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_ORIGINAL_FILE_NAME,
                                lessonContent.getAudioFile());
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_NEW_FILE_NAME,
                                record.getFileName());
                        status = false;
                    } else {
                        courseFlag.markLessonEndMenu(record.getChapterId(),
                                record.getLessonId());
                    }
                    break;
                }
            }
        }
        return status;
    }

    private boolean checkRecordConsistencyAndMarkFlagForQuestion(Record record,
            ChapterContent chapterContent, CourseFlag courseFlag, boolean status) {
        if (record.getType() == FileType.QUESTION_CONTENT) {
            for (QuestionContent questionContent : chapterContent.getQuiz()
                    .getQuestions()) {
                if ((questionContent.getQuestionNumber() == record
                        .getQuestionId())
                        && (MobileAcademyConstants.CONTENT_QUESTION
                                .equalsIgnoreCase(questionContent.getName()))) {
                    if ((!questionContent.getAudioFile().equals(
                            record.getFileName()))
                            || !answerOptionMatcher(record)) {
                        LOGGER.debug("Correct Answer Option or fileName doesn't match");
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_ORIGINAL_FILE_NAME,
                                questionContent.getAudioFile());
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_NEW_FILE_NAME,
                                record.getFileName());
                        status = false;
                    } else {
                        courseFlag.markQuestionContent(record.getChapterId(),
                                record.getQuestionId());
                    }
                    break;
                }
            }
        } else if (record.getType() == FileType.CORRECT_ANSWER) {
            for (QuestionContent questionContent : chapterContent.getQuiz()
                    .getQuestions()) {
                if ((questionContent.getQuestionNumber() == record
                        .getQuestionId())
                        && (MobileAcademyConstants.CONTENT_CORRECT_ANSWER
                                .equalsIgnoreCase(questionContent.getName()))) {
                    if (!questionContent.getAudioFile().equals(
                            record.getFileName())) {
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_ORIGINAL_FILE_NAME,
                                questionContent.getAudioFile());
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_NEW_FILE_NAME,
                                record.getFileName());
                        status = false;
                    } else {
                        courseFlag.markQuestionCorrectAnswer(
                                record.getChapterId(), record.getQuestionId());
                    }
                    break;
                }
            }
        } else if (record.getType() == FileType.WRONG_ANSWER) {
            for (QuestionContent questionContent : chapterContent.getQuiz()
                    .getQuestions()) {
                if ((questionContent.getQuestionNumber() == record
                        .getQuestionId())
                        && (MobileAcademyConstants.CONTENT_WRONG_ANSWER
                                .equalsIgnoreCase(questionContent.getName()))) {
                    if (!questionContent.getAudioFile().equals(
                            record.getFileName())) {
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_ORIGINAL_FILE_NAME,
                                questionContent.getAudioFile());
                        LOGGER.debug(
                                MobileAcademyConstants.LOG_MSG_NEW_FILE_NAME,
                                record.getFileName());
                        status = false;
                    } else {
                        courseFlag.markQuestionWrongAnswer(
                                record.getChapterId(), record.getQuestionId());
                    }
                    break;
                }
            }
        }
        return status;
    }

    private boolean answerOptionMatcher(Record record) {
        int questionNo = record.getQuestionId();
        int answerNo = record.getAnswerId();
        int chapterNo = record.getChapterId();
        return (answerNo == courseService.getCorrectAnswerOption(chapterNo,
                questionNo));
    }

    /*
     * This function takes the CourserRawContent record as input and based on
     * that It creates a CourseProcessedContent Record in CourseProcessedContent
     * table chapter
     */
    private void updateRecordInContentProcessedTable(
            CourseContentCsv courseContentCsv, OperatorDetails operatorDetails) {
        String metaData = "";
        ContentType contentType;
        if (StringUtils.isNotBlank(courseContentCsv.getMetaData())) {
            metaData = courseContentCsv.getMetaData().toUpperCase();
        }
        contentType = ContentType.findByName(courseContentCsv.getContentType());

        CourseProcessedContent courseProcessedContent = new CourseProcessedContent(
                Integer.parseInt(courseContentCsv.getContentId()),
                courseContentCsv.getCircle().toUpperCase(),
                Integer.parseInt(courseContentCsv.getLanguageLocationCode()),
                courseContentCsv.getContentName().toUpperCase().trim(),
                contentType, courseContentCsv.getContentFile(),
                Integer.parseInt(courseContentCsv.getContentDuration()),
                metaData);
        courseProcessedContent.setCreator(operatorDetails.getCreator());
        courseProcessedContent.setModifiedBy(operatorDetails.getModifiedBy());
        courseProcessedContent.setOwner(operatorDetails.getOwner());
        courseProcessedContentService.create(courseProcessedContent);
    }

    private boolean isRecordChangingTheAnswerOption(Record record) {
        if (record.getType() != FileType.QUESTION_CONTENT) {
            return false;
        } else {
            if (courseService.getCorrectAnswerOption(record.getChapterId(),
                    record.getQuestionId()) != record.getAnswerId()) {
                return true;
            }
        }
        return false;
    }
}