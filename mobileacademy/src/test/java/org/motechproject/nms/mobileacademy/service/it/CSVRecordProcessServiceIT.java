package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;
import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseProcessedContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseContentCsvDataService;
import org.motechproject.nms.mobileacademy.service.CSVRecordProcessService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

/**
 * /** Verify that CSVRecordProcessService is present and functional
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CSVRecordProcessServiceIT extends BasePaxIT {

    @Inject
    private CSVRecordProcessService csvRecordProcessService;

    @Inject
    private CourseContentCsvDataService courseContentCsvDataService;

    @Inject
    private CourseProcessedContentDataService courseProcessedContentDataService;

    @Inject
    private ChapterContentDataService chapterContentDataService;

    @Inject
    private MTrainingService mTrainingService;

    /**
     * test CoursePopulateService Instance is not null
     */
    @Test
    public void testCoursePopulateServiceInstance() throws Exception {

        assertNotNull(csvRecordProcessService);
    }

    /**
     * clear Mobile Academy and mtraining data related to course
     */
    private void clearMobileAcademyData() {
        courseContentCsvDataService.deleteAll();
        courseProcessedContentDataService.deleteAll();
        chapterContentDataService.deleteAll();
        List<Course> courses = mTrainingService
                .getCourseByName(MobileAcademyConstants.DEFAULT_COURSE_NAME);
        if (CollectionUtils.isNotEmpty(courses)) {
            Course course = courses.get(0);
            List<Chapter> chapters = course.getChapters();
            if (CollectionUtils.isNotEmpty(chapters)) {
                for (Chapter chapter : chapters) {
                    List<Lesson> lessons = chapter.getLessons();
                    if (CollectionUtils.isNotEmpty(lessons)) {
                        for (Lesson lesson : lessons) {
                            mTrainingService.deleteLesson(lesson.getId());
                        }
                    }
                    mTrainingService.deleteChapter(chapter.getId());
                }
            }
            mTrainingService.deleteCourse(course.getId());
        }
    }

    /**
     * test Course Populate Success scenario
     * 
     * @throws Exception
     */
    @Test
    public void testCoursePopulateSuccess() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseContentCsvs.size();
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());
    }

    /**
     * test Course Populate For more Records
     * 
     * @throws Exception
     */
    @Test
    public void testCoursePopulateForMoreRecords() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        // Added one more record
        courseContentCsvs.add(addNewRecord(courseContentCsvs.get(0)));
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(0, courseProcessedContents.size());
    }

    /**
     * test Course Populate For Less Records
     * 
     * @throws Exception
     */
    @Test
    public void testCoursePopulateForLessRecords() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        // Removed one record
        courseContentCsvDataService.delete(courseContentCsvs.get(1));
        courseContentCsvs.remove(1);
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(0, courseProcessedContents.size());
    }

    /**
     * test Course Delete Success scenario
     * 
     * @throws Exception
     */
    @Test
    public void testCourseDeleteSuccess() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseRawContentsForAdd = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseRawContentsForAdd.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseRawContentsForAdd.size();
        csvRecordProcessService.processRawRecords(courseRawContentsForAdd,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());
        // Records Processed for DEL Begin
        List<CourseContentCsv> courseRawContentsForDel = findCourseRawContentListFromCsv(null);
        for (CourseContentCsv courseContentCsv : courseRawContentsForDel) {
            courseContentCsv.setOperation("DEL");
        }
        csvRecordProcessService.processRawRecords(courseRawContentsForDel,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContentsDel = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(0, courseProcessedContentsDel.size());

    }

    /**
     * test Course Delete operation ForLess Records
     * 
     * @throws Exception
     */
    @Test
    public void testCourseDeleteForLessRecords() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseRawContentsForAdd = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseRawContentsForAdd.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseRawContentsForAdd.size();
        csvRecordProcessService.processRawRecords(courseRawContentsForAdd,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());
        // Records Processed for DEL Begin
        List<CourseContentCsv> courseRawContentsForDel = findCourseRawContentListFromCsv(null);
        for (CourseContentCsv courseContentCsv : courseRawContentsForDel) {
            courseContentCsv.setOperation("DEL");
        }
        // Removed one record
        courseContentCsvDataService.delete(courseRawContentsForDel.get(1));
        courseRawContentsForDel.remove(1);
        csvRecordProcessService.processRawRecords(courseRawContentsForDel,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContentsDel = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContentsDel.size());// No
                                                                        // Record
                                                                        // Deleted

    }

    /**
     * find CourseContentCsv List From Csv
     * 
     * @return List<CourseContentCsv>
     */
    private List<CourseContentCsv> findCourseRawContentListFromCsv(String llc) {
        List<CourseContentCsv> courseContentCsvs = new ArrayList<>();
        // Input file which needs to be parsed
        String fileToParse = "src//test//resources//CourseContentCsv.csv";
        BufferedReader fileReader = null;
        // Delimiter used in CSV file
        final String DELIMITER = ",";
        try {
            String line = "";
            // Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
            // Read the file line by line
            int rowCount = 0;
            while ((line = fileReader.readLine()) != null) {
                if (rowCount == 0) {
                    rowCount++;
                    continue;
                }
                // Get all tokens available in line
                int arrayIndex = 0;
                String[] tokens = line.split(DELIMITER);
                CourseContentCsv courseContentCsv = new CourseContentCsv();
                courseContentCsv.setOperation(tokens[arrayIndex++]);
                courseContentCsv.setContentId(tokens[arrayIndex++]);
                courseContentCsv.setCircle(tokens[arrayIndex++]);
                if (StringUtils.isNotBlank(llc)) {
                    courseContentCsv.setLanguageLocationCode(llc);
                    arrayIndex = arrayIndex + 1;
                } else {
                    courseContentCsv
                            .setLanguageLocationCode(tokens[arrayIndex++]);
                }
                courseContentCsv.setContentName(tokens[arrayIndex++]);
                courseContentCsv.setContentType(tokens[arrayIndex++]);
                courseContentCsv.setContentFile(tokens[arrayIndex++]);
                courseContentCsv.setContentDuration(tokens[arrayIndex++]);
                if (tokens.length > arrayIndex) {
                    courseContentCsv.setMetaData(tokens[arrayIndex]);
                }
                courseContentCsvs.add(courseContentCsvDataService
                        .create(courseContentCsv));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return courseContentCsvs;
    }

    /**
     * test Course Update Success scenario for one LLc
     * 
     * @throws Exception
     */
    @Test
    public void testCourseUpdateSuccessForOneLlc() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord = courseContentCsvs
                .get(0);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseContentCsvs.size();
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // Update process
        String circle = courseRawContentUpdateRecord.getCircle();
        Integer languageLocationCode = Integer
                .parseInt(courseRawContentUpdateRecord
                        .getLanguageLocationCode());
        String contentName = courseRawContentUpdateRecord.getContentName();
        courseRawContentUpdateRecord.setContentId("222");
        courseRawContentUpdateRecord.setContentDuration("333");
        courseContentCsvs = new ArrayList<>();
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord));
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle.toUpperCase(),
                        languageLocationCode, contentName.toUpperCase());
        assertEquals(222, courseProcessedContent.getContentID().longValue());
        assertEquals(333, courseProcessedContent.getContentDuration()
                .longValue());

    }

    /**
     * test Course Update when update for only one LLc present while in course
     * processed have two llcs data- updating audio file
     * 
     * @throws Exception
     */
    @Test
    public void testCourseUpdateWhenOneLlcDataPresent() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord1 = courseContentCsvs
                .get(0);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseContentCsvs.size();
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // add other LLC records i.e 20
        List<CourseContentCsv> courseRawContents2 = findCourseRawContentListFromCsv("20");
        Integer llc2 = Integer.parseInt(courseRawContents2.get(0)
                .getLanguageLocationCode());
        long rawContentSize2 = courseRawContents2.size();
        csvRecordProcessService.processRawRecords(courseRawContents2,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents2 = courseProcessedContentDataService
                .findContentByLlc(llc2);
        assertEquals(rawContentSize2, courseProcessedContents2.size());

        // Update process for only one LLc
        String circle = courseRawContentUpdateRecord1.getCircle();
        Integer languageLocationCode = Integer
                .parseInt(courseRawContentUpdateRecord1
                        .getLanguageLocationCode());
        String contentName = courseRawContentUpdateRecord1.getContentName();
        // original values before update
        String contentFileOriginal = courseRawContentUpdateRecord1
                .getContentFile();
        courseRawContentUpdateRecord1.setContentFile("ch_test_update.wav");
        courseContentCsvs = new ArrayList<>();
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord1));
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle.toUpperCase(),
                        languageLocationCode, contentName.toUpperCase());
        assertEquals(contentFileOriginal,
                courseProcessedContent.getContentFile());

    }

    /**
     * test Course Update when update for two LLc present while in course
     * processed have two llcs data- updating audio file
     * 
     * @throws Exception
     */
    @Test
    public void testCourseUpdateWhenTwoLlcDataPresent() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord1 = courseContentCsvs
                .get(0);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseContentCsvs.size();
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // add other LLC records i.e 20
        List<CourseContentCsv> courseRawContents2 = findCourseRawContentListFromCsv("20");
        Integer llc2 = Integer.parseInt(courseRawContents2.get(0)
                .getLanguageLocationCode());
        long rawContentSize2 = courseRawContents2.size();
        csvRecordProcessService.processRawRecords(courseRawContents2,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents2 = courseProcessedContentDataService
                .findContentByLlc(llc2);
        assertEquals(rawContentSize2, courseProcessedContents2.size());

        // Update process for only one LLc
        String circle = courseRawContentUpdateRecord1.getCircle();
        Integer languageLocationCode = Integer
                .parseInt(courseRawContentUpdateRecord1
                        .getLanguageLocationCode());
        String contentName = courseRawContentUpdateRecord1.getContentName();
        courseRawContentUpdateRecord1.setContentFile("ch_test_update.wav");
        courseContentCsvs = new ArrayList<>();
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord1));
        // add another update record for new llc
        courseRawContentUpdateRecord1.setLanguageLocationCode("20");
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord1));
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent1 = courseProcessedContentDataService
                .findByCircleLlcContentName(circle.toUpperCase(),
                        languageLocationCode, contentName.toUpperCase());
        CourseProcessedContent courseProcessedContent2 = courseProcessedContentDataService
                .findByCircleLlcContentName(circle.toUpperCase(), 20,
                        contentName.toUpperCase());// For LLC 20
        assertEquals("ch_test_update.wav",
                courseProcessedContent1.getContentFile());
        assertEquals("ch_test_update.wav",
                courseProcessedContent2.getContentFile());

    }

    /**
     * add New Record in course raw content table.
     * 
     * @param courseRawContentParam
     * @return CourseContentCsv
     */
    private CourseContentCsv addNewRecord(CourseContentCsv courseRawContentParam) {
        CourseContentCsv courseContentCsv = new CourseContentCsv();
        courseContentCsv.setOperation(courseRawContentParam.getOperation());
        courseContentCsv.setContentId(courseRawContentParam.getContentId());
        courseContentCsv.setCircle(courseRawContentParam.getCircle());
        courseContentCsv.setLanguageLocationCode(courseRawContentParam
                .getLanguageLocationCode());
        courseContentCsv.setContentName(courseRawContentParam.getContentName());
        courseContentCsv.setContentType(courseRawContentParam.getContentType());
        courseContentCsv.setContentFile(courseRawContentParam.getContentFile());
        courseContentCsv.setContentDuration(courseRawContentParam
                .getContentDuration());
        courseContentCsv.setMetaData(courseRawContentParam.getMetaData());
        return courseContentCsvDataService.create(courseContentCsv);
    }

    /**
     * test Update For All Records Of One Chapter
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateForAllRecordsOfOneChapter() throws Exception {
        clearMobileAcademyData();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseContentCsvs.size();
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // Update process

        courseContentCsvs = new ArrayList<>();
        courseContentCsvs = findCourseRawContentUpdateListFromCsv();
        csvRecordProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent1 = courseProcessedContentDataService
                .findByCircleLlcContentName("AP", 14, "CHAPTER01_LESSON01");
        CourseProcessedContent courseProcessedContent2 = courseProcessedContentDataService
                .findByCircleLlcContentName("AP", 14, "CHAPTER01_QUIZHEADER");
        CourseProcessedContent courseProcessedContent3 = courseProcessedContentDataService
                .findByCircleLlcContentName("AP", 14, "CHAPTER01_QUESTION02");
        CourseProcessedContent courseProcessedContent4 = courseProcessedContentDataService
                .findByCircleLlcContentName("AP", 14, "CHAPTER01_SCORE02");
        assertEquals(222, courseProcessedContent1.getContentID().longValue());
        assertEquals(333, courseProcessedContent1.getContentDuration()
                .longValue());
        assertEquals("ch_test_update.wav",
                courseProcessedContent1.getContentFile());

        assertEquals(222, courseProcessedContent2.getContentID().longValue());
        assertEquals(333, courseProcessedContent2.getContentDuration()
                .longValue());
        assertEquals("ch_test_update.wav",
                courseProcessedContent2.getContentFile());

        assertEquals(222, courseProcessedContent3.getContentID().longValue());
        assertEquals(333, courseProcessedContent3.getContentDuration()
                .longValue());
        assertEquals("ch_test_update.wav",
                courseProcessedContent3.getContentFile());

        assertEquals(222, courseProcessedContent4.getContentID().longValue());
        assertEquals(333, courseProcessedContent4.getContentDuration()
                .longValue());
        assertEquals("ch_test_update.wav",
                courseProcessedContent4.getContentFile());

    }

    /**
     * find CourseContentCsv List From Csv having updated records for chapter 1.
     * Updating contentId, duration and audio file name .
     * 
     * @return List<CourseContentCsv>
     */
    private List<CourseContentCsv> findCourseRawContentUpdateListFromCsv() {
        List<CourseContentCsv> courseContentCsvs = new ArrayList<>();
        // Input file which needs to be parsed
        String fileToParse = "src//test//resources//CourseContentCsv.csv";
        BufferedReader fileReader = null;
        // Delimiter used in CSV file
        final String DELIMITER = ",";
        try {
            String line = "";
            // Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
            // Read the file line by line
            int rowCount = 0;
            while ((line = fileReader.readLine()) != null && rowCount < 28) {
                if (rowCount == 0) {
                    rowCount++;
                    continue;
                }
                rowCount++;
                // Get all tokens available in line
                int arrayIndex = 0;
                String[] tokens = line.split(DELIMITER);
                CourseContentCsv courseContentCsv = new CourseContentCsv();
                courseContentCsv.setOperation(tokens[arrayIndex++]);
                courseContentCsv.setContentId("222");
                arrayIndex = arrayIndex + 1;
                courseContentCsv.setCircle(tokens[arrayIndex++]);
                courseContentCsv.setLanguageLocationCode(tokens[arrayIndex++]);
                courseContentCsv.setContentName(tokens[arrayIndex++]);
                courseContentCsv.setContentType(tokens[arrayIndex++]);
                courseContentCsv.setContentFile("ch_test_update.wav");
                arrayIndex = arrayIndex + 1;
                courseContentCsv.setContentDuration("333");
                arrayIndex = arrayIndex + 1;
                if (tokens.length > arrayIndex) {
                    courseContentCsv.setMetaData(tokens[arrayIndex]);
                }
                courseContentCsvs.add(courseContentCsvDataService
                        .create(courseContentCsv));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return courseContentCsvs;
    }
}
