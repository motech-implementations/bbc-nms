package org.motechproject.nms.mobileacademy.service.ut;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.nms.mobileacademy.commons.CourseFlag;
import org.motechproject.nms.mobileacademy.commons.FileType;
import org.motechproject.nms.mobileacademy.commons.Record;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;
import org.motechproject.nms.mobileacademy.helper.RecordsProcessHelper;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseContentCsvDataService;
import org.motechproject.nms.mobileacademy.service.CourseContentCsvService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.motechproject.nms.mobileacademy.service.impl.RecordsProcessServiceImpl;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.service.BulkUploadErrLogService;

public class RecordsProcessServiceImplTest {

    @InjectMocks
    RecordsProcessServiceImpl recordsProcessServiceImpl = new RecordsProcessServiceImpl();

    @Mock
    private CourseContentCsvService courseContentCsvService;

    @Mock
    private CourseProcessedContentService courseProcessedContentService;

    @Mock
    private ChapterContentDataService chapterContentDataService;

    @Mock
    public CourseContentCsvDataService courseContentCsvDataService;

    @Mock
    private CourseService courseService;

    @Mock
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /*
     * This test case is used to test the processing of raw records with valid
     * data.
     */
    @SuppressWarnings("serial")
    @Test
    public void testProcessRawRecords() {
        List<CourseContentCsv> courseContentCsvRecords = new ArrayList<CourseContentCsv>() {
        };
        courseContentCsvRecords.add(new CourseContentCsv("100014", "AP", "14",
                "Chapter01_Lesson01", "Content", "ch1_l1.wav", "150", ""));
        courseContentCsvRecords.add(new CourseContentCsv("100015", "AP", "14",
                "Chapter01_Lesson01", "Content", "ch1_l1.wav", "150", ""));
        courseContentCsvRecords.add(new CourseContentCsv("100016", "AP", "14",
                "Chapter01_Lesson01", "Content", "ch1_l1.wav", "150", ""));
        courseContentCsvRecords.add(new CourseContentCsv("100017", "AP", "14",
                "Chapter01_Lesson02", "Content", "ch1_l2.wav", "150", ""));
        assertEquals(recordsProcessServiceImpl.processRawRecords(
                courseContentCsvRecords, "Book1.csv"),
                "Records Processed Successfully");
    }

    /*
     * this test case is used to validate schema when provided with invalid
     * ContentId.
     */
    @Test
    public void testValidateSchemaForInvalidContentId() {
        Boolean flag = true;
        CourseContentCsv courseContentCsv = new CourseContentCsv("CI14", "AP",
                "14", "Chapter01_Lesson01", "Content", "ch1_l1.wav", "150", "");
        try {
            RecordsProcessHelper.validateSchema(courseContentCsv);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate schema when provided with invalid LLC.
     */
    @Test
    public void testValidateSchemaForInvalidLLC() {
        Boolean flag = true;
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "XX", "Chapter01_Lesson01", "Content", "ch1_l1.wav",
                "150", "");

        try {
            RecordsProcessHelper.validateSchema(courseContentCsv);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate schema when provided with invalid
     * Content Type.
     */
    @Test
    public void testValidateSchemaForInvalidContentType() {
        Boolean flag = true;
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "XX", "Chapter01_Lesson01", "ABC", "ch1_l1.wav", "150",
                "");

        try {
            RecordsProcessHelper.validateSchema(courseContentCsv);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate schema when provided with Empty
     * ContentName.
     */
    @Test
    public void testValidateSchemaForEmptyContentName() {
        Boolean flag = true;
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "14", "", "Content", "ch1_l1.wav", "150", "");
        try {
            RecordsProcessHelper.validateSchema(courseContentCsv);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate schema when provided with ContentName
     * as NULL.
     */
    @Test
    public void testValidateSchemaForNullContentName() {
        Boolean flag = true;
        CourseContentCsv courseContentCsv = new CourseContentCsv("14", "AP",
                "14", "null", "Content", "ch1_l1.wav", "150", "");
        try {
            RecordsProcessHelper.validateSchema(courseContentCsv);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * This test case is used to determine the type of the record and update its
     * Chapter Content accordingly.
     */
    @Test
    public void testDetermineTypeAndUpdateChapterContent() {
        Record record = new Record();
        record.setType(FileType.LESSON_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");
        Method method;
        try {
            method = RecordsProcessServiceImpl.class.getDeclaredMethod(
                    "determineTypeAndUpdateChapterContent",
                    new Class[] { Record.class });
            method.setAccessible(true);
            method.invoke(recordsProcessServiceImpl, new Object[] { record });
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
        }
    }

    /*
     * This test case is used to test the processing of raw records with Add as
     * Operation.
     */
    @Test
    public void testProcessAddRecords() {
        Map<Integer, List<CourseContentCsv>> mapForAddRecords = new HashMap<Integer, List<CourseContentCsv>>();
        List<CourseContentCsv> listCourseRecords = new ArrayList<>();
        listCourseRecords.add(new CourseContentCsv("100014", "AP", "14",
                "Chapter01_Lesson01", "Content", "ch1_l1.wav", "150", ""));
        listCourseRecords.add(new CourseContentCsv("100015", "AP", "14",
                "Chapter01_Lesson03", "Content", "ch1_l3.wav", "250", ""));
        listCourseRecords.add(new CourseContentCsv("100016", "AP", "14",
                "Chapter01_Lesson04", "Content", "ch1_l4.wav", "150", ""));
        listCourseRecords.add(new CourseContentCsv("100017", "AP", "14",
                "Chapter01_Lesson02", "Content", "ch1_l2.wav", "450", ""));
        mapForAddRecords.put(14, listCourseRecords);
        Method method;
        try {
            method = RecordsProcessServiceImpl.class.getDeclaredMethod(
                    "processAddRecords", new Class[] { Map.class });
            method.setAccessible(true);
            method.invoke(recordsProcessServiceImpl,
                    new Object[] { mapForAddRecords });
            assertTrue(mapForAddRecords.isEmpty());
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
        }
    }

    /*
     * This test case is used to Check the record for consistency and mark the
     * appropriate course flag.
     */
    @SuppressWarnings("serial")
    @Test
    public void testCheckRecordConsistencyAndMarkFlag() {
        Method method;
        Record record = new Record();
        record.setType(FileType.LESSON_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setQuestionId(1);
        record.setFileName("Ch1_l1.wav");
        List<QuestionContent> questionContents = new ArrayList<QuestionContent>() {
        };
        questionContents.add(new QuestionContent(1, "question", "ch1_q1.wav"));
        questionContents.add(new QuestionContent(1, "correctAnswer",
                "ch1_q1_ca.wav"));
        questionContents.add(new QuestionContent(1, "wrongAnswer",
                "ch1_q1_wta.wav"));
        questionContents.add(new QuestionContent(2, "question", "ch1_q2.wav"));
        QuizContent quiz = new QuizContent("quizHeader", "ch1_qp.wav",
                questionContents);
        List<ScoreContent> scoreContent = new ArrayList<ScoreContent>() {
        };
        scoreContent.add(new ScoreContent("score01", "Ch1_1ca.wav"));
        scoreContent.add(new ScoreContent("score00", "Ch1_0ca.wav"));
        scoreContent.add(new ScoreContent("score02", "Ch1_2ca.wav"));
        List<LessonContent> lessons = new ArrayList<LessonContent>() {
        };
        lessons.add(new LessonContent(1, "lesson", "Ch1_l1.wav"));
        lessons.add(new LessonContent(1, "menu", "Ch1_endMenu.wav"));
        lessons.add(new LessonContent(2, "lesson", "Ch1_l3.wav"));
        lessons.add(new LessonContent(4, "lesson", "Ch1_l4.wav"));
        lessons.add(new LessonContent(3, "lesson", "Ch1_l1.wav"));
        ChapterContent chapterContent = new ChapterContent(1, "menu",
                "ch1_l1.wav", lessons, scoreContent, quiz);
        CourseFlag courseFlags = new CourseFlag();
        try {
            method = RecordsProcessServiceImpl.class.getDeclaredMethod(
                    "checkRecordConsistencyAndMarkFlag", new Class[] {
                            Record.class, List.class, CourseFlag.class });
            method.setAccessible(true);
            method.invoke(recordsProcessServiceImpl, new Object[] { record,
                    Arrays.asList(chapterContent), courseFlags });
            assertTrue(courseFlags.getChapterFlag(1).getLessonFlag(1)
                    .isFlagForLessonContentFile());

            record.setFileName("ABC.wav");
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setType(FileType.QUESTION_CONTENT);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setType(FileType.CORRECT_ANSWER);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setType(FileType.WRONG_ANSWER);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setType(FileType.LESSON_CONTENT);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setType(FileType.LESSON_END_MENU);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setType(FileType.CHAPTER_END_MENU);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setType(FileType.QUIZ_HEADER);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setType(FileType.SCORE);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

            record.setChapterId(15);
            assertFalse((boolean) method.invoke(recordsProcessServiceImpl,
                    new Object[] { record, Arrays.asList(chapterContent),
                            courseFlags }));

        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
        }
    }

    /*
     * this test case is used to validate raw content when provided with valid
     * data.
     */
    @Test
    public void testValidateRawContent() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "14", "Chapter01_Lesson01", "Content", "ch1_l1.wav",
                "150", "");
        Record record = new Record();
        record.setType(FileType.LESSON_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");
        Boolean flag = true;
        try {
            RecordsProcessHelper.validateRawContent(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertTrue(flag);
    }

    /*
     * this test case is used to validate raw content when provided with invalid
     * meta data
     */
    @Test
    public void testValidateRawContentwithInvalidMetaData() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "14", "Chapter01_Question01", "Content", "ch1_q1.wav",
                "150", "RightAnswer:01");
        Record record = new Record();
        record.setType(FileType.QUESTION_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_q1.wav");
        Boolean flag = true;
        try {
            RecordsProcessHelper.validateRawContent(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate raw content when provided with out of
     * bound index value for answer option
     */
    @Test
    public void testValidateRawContentWithOutOfBoundAnswerIndex() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "14", "Chapter01_Question01", "Content", "ch1_q1.wav",
                "150", "CorrectAnswer:04");
        Record record = new Record();
        record.setType(FileType.QUESTION_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_q1.wav");
        Boolean flag = true;
        try {
            RecordsProcessHelper.validateRawContent(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate raw content when provided with empty
     * MetaData Field.
     */
    @Test
    public void testValidateRawContentForEmptyMetaData() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100023",
                "AP", "14", "Chapter01_Question01", "Content", "ch1_q1.wav",
                "20", "");
        Record record = new Record();
        record.setType(FileType.QUESTION_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");
        Boolean flag = true;
        try {
            RecordsProcessHelper.validateRawContent(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate raw content when provided with no
     * Correct Answer Id.
     */
    @Test
    public void testValidateRawContentForEmptyCorrectAnswer() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100023",
                "AP", "14", "Chapter01_Question01", "Content", "ch1_q1.wav",
                "20", "CorrectAnswer:");
        Record record = new Record();
        record.setType(FileType.QUESTION_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");
        Boolean flag = true;
        try {
            RecordsProcessHelper.validateRawContent(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate raw content when provided with Invalid
     * Correct Answer Id.
     */
    @Test
    public void testValidateRawContentForInvalidCorrectAnswer() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100023",
                "AP", "14", "Chapter01_Question01", "Content", "ch1_q1.wav",
                "20", "CorrectAnswer:X");
        Record record = new Record();
        record.setType(FileType.QUESTION_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");

        Boolean flag = true;
        try {
            RecordsProcessHelper.validateRawContent(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate raw content when provided with
     * Improper Content Name.
     */
    @Test
    public void testPartialValidateContentName() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "14", "Chapter01_", "Content", "ch1_l1.wav", "150", "");
        Record record = new Record();
        record.setType(FileType.LESSON_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");
        Boolean flag = true;

        try {
            RecordsProcessHelper.validateContentName(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate raw content when provided with
     * ContentName with missing delimeter.
     */
    @Test
    public void testValidateContentNameWithMissingDelimeter() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "14", "Chapter01Lesson01", "Content", "ch1_l1.wav",
                "150", "");
        Record record = new Record();
        record.setType(FileType.LESSON_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");
        Boolean flag = true;
        try {
            RecordsProcessHelper.validateContentName(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate content name when content name has
     * lesson id has non numeric index
     */
    @Test
    public void testValidateContentNameWithNotNumericIndex() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "14", "Chapter01_LessonAB", "Content", "ch1_l1.wav",
                "150", "");
        Record record = new Record();
        record.setType(FileType.LESSON_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");
        Boolean flag = true;
        try {
            RecordsProcessHelper.validateContentName(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);

        flag = true;
        courseContentCsv.setContentName("ChapterAB_LessonAB");
        try {
            RecordsProcessHelper.validateContentName(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to validate the content name when content name has
     * chapter id out of bound
     */
    @Test
    public void testValidateContentNameWithOutOfBoundIndex() {
        CourseContentCsv courseContentCsv = new CourseContentCsv("100014",
                "AP", "14", "Chapter21_Lesson01", "Content", "ch1_l1.wav",
                "150", "");
        Record record = new Record();
        record.setType(FileType.LESSON_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Ch1_l1.wav");
        Boolean flag = true;
        try {
            RecordsProcessHelper.validateContentName(courseContentCsv, record);
        } catch (DataValidationException e) {
            flag = false;
        }
        assertFalse(flag);
    }

    /*
     * this test case is used to test whether the type is determinable Or Not
     */
    @Test
    public void testIsTypeDeterminable() {
        Record record = new Record();

        assertFalse(RecordsProcessHelper.isTypeDeterminable(record, "Lesson"));
        assertNull(record.getType());
        assertFalse(RecordsProcessHelper.isTypeDeterminable(record, "LessonAB"));
        assertNull(record.getType());
        assertFalse(RecordsProcessHelper.isTypeDeterminable(record, "ABCDEF02"));
        assertNull(record.getType());
        assertFalse(RecordsProcessHelper.isTypeDeterminable(record, "Lesson08"));
        assertNull(record.getType());
        assertFalse(RecordsProcessHelper.isTypeDeterminable(record,
                "Question08"));
        assertNull(record.getType());
        assertFalse(RecordsProcessHelper.isTypeDeterminable(record, "Score08"));
        assertNull(record.getType());

        RecordsProcessHelper.isTypeDeterminable(record, "Lesson01");
        assertSame(FileType.LESSON_CONTENT, record.getType());
    }

    /*
     * this test case is used to check the type of record and add to the
     * ChapterContent.
     */
    @SuppressWarnings("serial")
    @Test
    public void testCheckTypeAndAddToChapterContent() {
        Record record = new Record();
        record.setType(FileType.LESSON_CONTENT);
        record.setChapterId(1);
        record.setLessonId(1);
        record.setFileName("Lesson Content File");
        CourseFlag courseFlags = new CourseFlag();
        List<QuestionContent> questionContent = new ArrayList<QuestionContent>() {
        };
        questionContent.add(new QuestionContent(1, "question", "ch1_q1.wav"));
        questionContent.add(new QuestionContent(1, "correctAnswer",
                "ch1_q1_ca.wav"));
        questionContent.add(new QuestionContent(2, "question", "ch1_q2.wav"));
        QuizContent quiz = new QuizContent("quiz header", "ch1_qp.wav",
                questionContent);
        List<ScoreContent> scoreContent = new ArrayList<ScoreContent>() {
        };
        scoreContent.add(new ScoreContent("score01", "Ch1_1ca.wav"));
        scoreContent.add(new ScoreContent("score00", "Ch1_0ca.wav"));
        scoreContent.add(new ScoreContent("score02", "Ch1_2ca.wav"));
        List<LessonContent> lessons = new ArrayList<LessonContent>() {
        };
        lessons.add(new LessonContent(1, "lesson", "Ch1_l1.wav"));
        lessons.add(new LessonContent(2, "lesson", "Ch1_l3.wav"));
        lessons.add(new LessonContent(4, "lesson", "Ch1_l4.wav"));
        lessons.add(new LessonContent(1, "lesson", "Ch1_l1.wav"));
        ChapterContent chapterContent = new ChapterContent(14, "content",
                "ch1_l1.wav", lessons, scoreContent, quiz);
        RecordsProcessHelper.checkTypeAndAddToChapterContent(record,
                Arrays.asList(chapterContent), courseFlags);
        assertFalse(courseFlags.getChapterFlag(1).getLessonFlag(1)
                .isFlagForLessonContentFile());

        chapterContent.setChapterNumber(1);
        RecordsProcessHelper.checkTypeAndAddToChapterContent(record,
                Arrays.asList(chapterContent), courseFlags);
        assertTrue(courseFlags.getChapterFlag(1).getLessonFlag(1)
                .isFlagForLessonContentFile());

        record.setChapterId(15);
        RecordsProcessHelper.checkTypeAndAddToChapterContent(record,
                Arrays.asList(chapterContent), courseFlags);
        assertNull(courseFlags.getChapterFlag(15));
    }
}
