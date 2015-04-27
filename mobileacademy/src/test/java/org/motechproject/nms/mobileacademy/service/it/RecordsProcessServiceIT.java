package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mtraining.domain.Chapter;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.Lesson;
import org.motechproject.mtraining.service.MTrainingService;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseContentCsvDataService;
import org.motechproject.nms.mobileacademy.repository.CourseProcessedContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.motechproject.nms.mobileacademy.service.RecordsProcessService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * /** Verify that csvRecordProcessService is present and functional
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class RecordsProcessServiceIT extends BasePaxIT {

    @Inject
    private CourseContentCsvDataService courseContentCsvDataService;

    @Inject
    private CourseProcessedContentDataService courseProcessedContentDataService;

    @Inject
    private CourseService courseService;

    @Inject
    private ChapterContentDataService chapterContentDataService;

    @Inject
    private MTrainingService mTrainingService;

    @Inject
    private RecordsProcessService recordsProcessService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private StateService stateService;

    @Inject
    private CircleService circleService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(RecordsProcessServiceIT.class);

    Circle circleData = new Circle();

    State stateData = new State();

    public void createLlcCircleMapping() {

        District merrut = new District();

        District agra = new District();

        District mathura = new District();

        Set<District> districtSet = new HashSet<District>();

        stateData.setName("Andhra Pradesh");
        stateData.setStateCode(25L);
        stateData.setDistrict(districtSet);

        merrut.setName("Meerut");
        merrut.setStateCode(25L);
        merrut.setDistrictCode(3L);

        agra.setName("Agra");
        agra.setStateCode(25L);
        agra.setDistrictCode(4L);

        mathura.setName("Mathura");
        mathura.setStateCode(25L);
        mathura.setDistrictCode(5L);

        stateData.getDistrict().add(merrut);
        stateData.getDistrict().add(agra);
        stateData.getDistrict().add(mathura);

        stateService.create(stateData);

        circleData.setName("Andhra Pradesh");
        circleData.setCode("AP");
        circleService.create(circleData);

        LanguageLocationCode languageLocationCode = getLangLocCodeDefaultSetting();
        languageLocationCode.setDistrictCode(3L);
        languageLocationCode.setLanguageLocationCode("14");
        languageLocationCode.setDistrict(merrut);
        languageLocationCodeService.create(languageLocationCode);

        languageLocationCode = getLangLocCodeDefaultSetting();
        languageLocationCode.setDistrictCode(4L);
        languageLocationCode.setLanguageLocationCode("20");
        languageLocationCode.setDistrict(agra);
        languageLocationCodeService.create(languageLocationCode);

        languageLocationCode = getLangLocCodeDefaultSetting();
        languageLocationCode.setDistrictCode(5L);
        languageLocationCode.setLanguageLocationCode("30");
        languageLocationCode.setDistrict(mathura);
        languageLocationCodeService.create(languageLocationCode);
    }

    LanguageLocationCode getLangLocCodeDefaultSetting() {
        LanguageLocationCode languageLocationCode = new LanguageLocationCode();

        languageLocationCode.setCircleCode("AP");
        languageLocationCode.setStateCode(25L);
        languageLocationCode.setCircle(circleData);
        languageLocationCode.setState(stateData);

        languageLocationCode.setLanguageMK("Hindi");
        languageLocationCode.setLanguageKK("Hindi");
        languageLocationCode.setLanguageMA("Hindi");
        return languageLocationCode;
    }

    /**
     * clear Mobile Academy and mtraining data related to course
     */
    private void clearMobileAcademyData() {
        courseContentCsvDataService.deleteAll();
        courseProcessedContentDataService.deleteAll();
        chapterContentDataService.deleteAll();
        languageLocationCodeService.deleteAll();
        stateService.deleteAll();
        circleService.deleteAll();

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
        createLlcCircleMapping();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
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
        createLlcCircleMapping();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        // Added one more record
        courseContentCsvs.add(addNewRecord(courseContentCsvs.get(0)));
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        recordsProcessService.processRawRecords(courseContentCsvs,
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
        createLlcCircleMapping();
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        // Removed one record
        courseContentCsvDataService.delete(courseContentCsvs.get(1));
        courseContentCsvs.remove(1);
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(0, courseProcessedContents.size());
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
                courseContentCsv.setCreator("Test creator");
                courseContentCsv.setOwner("Test owner");
                courseContentCsv.setModifiedBy("Test modifier");
                courseContentCsvs.add(courseContentCsvDataService
                        .create(courseContentCsv));
            }
        } catch (Exception e) {
            LOGGER.error("Exception occured", e);
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                LOGGER.error("IOException occured", e);
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
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord = courseContentCsvs
                .get(0);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
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
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, languageLocationCode,
                        contentName);
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
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord1 = courseContentCsvs
                .get(0);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // add other LLC records i.e 20
        List<CourseContentCsv> courseRawContents2 = findCourseRawContentListFromCsv("20");
        Integer llc2 = Integer.parseInt(courseRawContents2.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc2, "AP");
        long rawContentSize2 = courseRawContents2.size();
        recordsProcessService.processRawRecords(courseRawContents2,
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
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, languageLocationCode,
                        contentName);
        assertEquals(contentFileOriginal,
                courseProcessedContent.getContentFile());
    }

    /*
     * this test case is used to test update scenario when provide with record
     * with different audio file names for existing LLCs
     */
    @Test
    public void testCourseUpdateWithTwoLlcFail() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord1 = courseContentCsvs
                .get(0);
        Integer llc1 = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc1, "AP");
        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc1);
        assertEquals(rawContentSize, courseProcessedContents.size());
        // add other LLC records i.e 20
        List<CourseContentCsv> courseRawContents2 = findCourseRawContentListFromCsv("20");
        Integer llc2 = Integer.parseInt(courseRawContents2.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc2, "AP");
        long rawContentSize2 = courseRawContents2.size();
        recordsProcessService.processRawRecords(courseRawContents2,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents2 = courseProcessedContentDataService
                .findContentByLlc(llc2);
        assertEquals(rawContentSize2, courseProcessedContents2.size());

        // Update File for both LLCs but with different audio files
        String circle = courseRawContentUpdateRecord1.getCircle();
        // original values before update
        String contentFileOriginal = courseRawContentUpdateRecord1
                .getContentFile();
        String contentName = courseRawContentUpdateRecord1.getContentName();
        courseContentCsvs = new ArrayList<>();
        courseRawContentUpdateRecord1.setContentFile("ch_test1_update.wav");
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord1));
        courseRawContentUpdateRecord1.setLanguageLocationCode(Integer
                .toString(llc2));
        courseRawContentUpdateRecord1.setContentFile("ch_test2_update.wav");
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord1));
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, llc1, contentName);
        assertEquals(contentFileOriginal,
                courseProcessedContent.getContentFile());
        courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, llc2, contentName);
        assertEquals(contentFileOriginal,
                courseProcessedContent.getContentFile());
        Integer correctAnswer = courseService.getCorrectAnswerOption(1, 1);
        courseContentCsvs = new ArrayList<>();
        courseRawContentUpdateRecord1.setContentName("Chapter01_Question01");
        courseRawContentUpdateRecord1.setLanguageLocationCode(Integer
                .toString(llc1));
        courseRawContentUpdateRecord1
                .setContentFile("Testing Correct Answer.wav");
        courseRawContentUpdateRecord1.setMetaData("correctAnswer:1");
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord1));
        courseRawContentUpdateRecord1.setLanguageLocationCode(Integer
                .toString(llc2));
        courseRawContentUpdateRecord1.setMetaData("correctAnswer:2");
        courseContentCsvs.add(courseRawContentUpdateRecord1);
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        assertEquals(correctAnswer, courseService.getCorrectAnswerOption(1, 1));
        courseContentCsvs = new ArrayList<>();
        courseRawContentUpdateRecord1.setContentName("Chapter01_Question01");
        courseRawContentUpdateRecord1.setLanguageLocationCode(Integer
                .toString(llc1));
        courseRawContentUpdateRecord1
                .setContentFile("Testing Correct Answer.wav");
        courseRawContentUpdateRecord1.setMetaData("correctAnswer:2");
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord1));
        courseRawContentUpdateRecord1.setLanguageLocationCode(Integer
                .toString(llc2));
        courseContentCsvs.add(courseRawContentUpdateRecord1);
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        correctAnswer = 2;
        assertEquals(correctAnswer, courseService.getCorrectAnswerOption(1, 1));
    }

    /*
     * this test case is used to test course add scenario when provided with
     * inconsistent data
     */
    @Test
    public void testCourseAddWithInconsistentData() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // add other LLC records i.e 20
        List<CourseContentCsv> courseRawContents2 = findCourseRawContentListFromCsv("20");
        Integer llc2 = Integer.parseInt(courseRawContents2.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc2, "AP");
        courseRawContents2.get(4).setContentFile("Inconsistent Audio File");
        recordsProcessService.processRawRecords(courseRawContents2,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents2 = courseProcessedContentDataService
                .findContentByLlc(llc2);
        assertEquals(0, courseProcessedContents2.size());
    }

    /*
     * this test case is used to test course add when provided with duplicate
     * data
     */
    @Test
    public void testCourseAddWithDuplicateData() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord1 = courseContentCsvs
                .get(0);
        courseContentCsvs.remove(1);
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord1));
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(0, courseProcessedContents.size());
    }

    /*
     * this test case is used to test course add with chapter id out of bound
     */
    @Test
    public void testCourseAddWithOutOfBoundIndices() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord1 = courseContentCsvs
                .get(0);
        courseContentCsvs.remove(0);
        courseRawContentUpdateRecord1.setContentName("Chapter15_Lesson01");
        courseContentCsvs.add(courseRawContentUpdateRecord1);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(0, courseProcessedContents.size());
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
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord1 = courseContentCsvs
                .get(0);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // add other LLC records i.e 20
        List<CourseContentCsv> courseRawContents2 = findCourseRawContentListFromCsv("20");
        Integer llc2 = Integer.parseInt(courseRawContents2.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc2, "AP");
        long rawContentSize2 = courseRawContents2.size();
        recordsProcessService.processRawRecords(courseRawContents2,
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
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent1 = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, languageLocationCode,
                        contentName);
        CourseProcessedContent courseProcessedContent2 = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, 20, contentName);// For LLC
        // 20
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
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // Update process
        courseContentCsvs = new ArrayList<>();
        courseContentCsvs = findCourseRawContentUpdateListFromCsv();
        recordsProcessService.processRawRecords(courseContentCsvs,
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
                courseContentCsv.setCreator("Test creator");
                courseContentCsv.setOwner("Test owner");
                courseContentCsv.setModifiedBy("Test modifier");
                courseContentCsvs.add(courseContentCsvDataService
                        .create(courseContentCsv));
            }
        } catch (Exception e) {
            LOGGER.error("Exception occured", e);
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                LOGGER.error("IOException occured", e);
            }
        }
        return courseContentCsvs;
    }

    /**
     * test Course Update when update for two LLc present with different names
     * and course processed also have two LLCs data - updating audio file
     *
     * @throws Exception
     */
    @Test
    public void testCourseUpdateWhenTwoLlcDataPresentAndFails()
            throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord1 = courseContentCsvs
                .get(0);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // add other LLC records i.e 20
        List<CourseContentCsv> courseRawContents2 = findCourseRawContentListFromCsv("20");
        Integer llc2 = Integer.parseInt(courseRawContents2.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc2, "AP");
        long rawContentSize2 = courseRawContents2.size();
        recordsProcessService.processRawRecords(courseRawContents2,
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
        courseContentCsvs.add(courseRawContentUpdateRecord1);
        // add another update record for new llc
        courseRawContentUpdateRecord1.setLanguageLocationCode(llc2.toString());
        courseRawContentUpdateRecord1.setContentFile("ch_test_update1.wav");
        courseContentCsvs.add(courseRawContentUpdateRecord1);
        CourseProcessedContent courseProcessedContent1 = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, languageLocationCode,
                        contentName);
        String originalFileName = courseProcessedContent1.getContentFile();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        courseProcessedContent1 = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, languageLocationCode,
                        contentName);
        CourseProcessedContent courseProcessedContent2 = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, 20, contentName);
        String newFileName1 = courseProcessedContent1.getContentFile();
        String newFileName2 = courseProcessedContent2.getContentFile();
        assertEquals(newFileName1, originalFileName);
        assertEquals(newFileName2, originalFileName);
    }

    /**
     * test Course Add when add for new LLc comes with different file names than
     * the course already existing in CPC for a LLC
     *
     * @throws Exception
     */
    @Test
    public void testCourseAddWhenOneLlcDataPresentAndFails() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // add other LLC records i.e 20
        List<CourseContentCsv> courseRawContents2 = findCourseRawContentListFromCsv("20");
        // createCircleLlcMapping(20, "AP");
        courseRawContents2.get((int) (rawContentSize - 2)).setContentFile(
                "Dummy Test Case File");
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(20);
        assertTrue(CollectionUtils.isEmpty(courseProcessedContents));
    }

    /*
     * this test case is used to test course populate fail scenario when
     * provided with invalid data
     */
    @Test
    public void testCoursePopulateFailForInvalidData() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        courseContentCsvs.get(0).setLanguageLocationCode("sw");// LLC as string
        courseContentCsvs.get(1).setContentName("Test");// no underscore in
        // content name
        courseContentCsvs.get(2).setContentName("Chapter12_Lesson02");
        // createCircleLlcMapping(14, "AP");
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(14);
        assertEquals(0, courseProcessedContents.size());
    }

    /*
     * this test case is used to test update fail scenario when provided with
     * invalid data
     */
    @Test
    public void testCourseUpdateFailForInvalidData() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord = courseContentCsvs
                .get(0);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());
        // Update process
        String circle = courseRawContentUpdateRecord.getCircle();
        Integer languageLocationCode = Integer
                .parseInt(courseRawContentUpdateRecord
                        .getLanguageLocationCode());
        String contentNameUpdate = "Chapter01Lesson01";
        courseRawContentUpdateRecord.setContentName(contentNameUpdate);
        courseContentCsvs = new ArrayList<>();
        courseContentCsvs.add(addNewRecord(courseRawContentUpdateRecord));
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, languageLocationCode,
                        contentNameUpdate);
        assertNull(courseProcessedContent);
    }

    /*
     * this test case is used to test whether the answer option update
     * successfully when provided with valid values
     */
    @Test
    public void testAnswerOptionUpdateSuccess() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        String contentName = "Chapter01_Question01";
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord = courseContentCsvs
                .get(0);
        String circle = courseRawContentUpdateRecord.getCircle();
        Integer correctAnswer;
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());
        correctAnswer = 1;
        assertEquals(correctAnswer, courseService.getCorrectAnswerOption(1, 1));
        courseRawContentUpdateRecord.setContentName(contentName);
        courseRawContentUpdateRecord.setMetaData("correctAnswer:2");
        courseRawContentUpdateRecord
                .setContentFile("File Changed for Question Content.wav");
        courseContentCsvs = new ArrayList<>();
        courseContentCsvs.add(courseRawContentUpdateRecord);
        recordsProcessService.processRawRecords(courseContentCsvs,
                "Answer Option change.csv");
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, llc, contentName);
        assertEquals(courseProcessedContent.getContentFile(),
                "File Changed for Question Content.wav");
        correctAnswer = 2;
        assertEquals(correctAnswer, courseService.getCorrectAnswerOption(1, 1));
    }

    /*
     * this test case is used to test modification scenario when there is no
     * change in the record to be uploaded
     */
    @Test
    public void testModificationWithNoChangeInRecord() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        Integer correctAnswer;
        String contentName = "Chapter01_Question01";
        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        CourseContentCsv courseRawContentUpdateRecord = courseContentCsvs
                .get(0);
        String circle = courseRawContentUpdateRecord.getCircle();
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());

        String fileName = "";
        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, llc, contentName);
        fileName = courseProcessedContent.getContentFile();
        correctAnswer = 1;
        assertEquals(correctAnswer, courseService.getCorrectAnswerOption(1, 1));
        courseRawContentUpdateRecord.setContentName(contentName);
        courseRawContentUpdateRecord.setMetaData("correctAnswer:02");
        courseRawContentUpdateRecord.setContentFile(fileName);
        courseContentCsvs = new ArrayList<>();
        courseContentCsvs.add(courseRawContentUpdateRecord);
        recordsProcessService.processRawRecords(courseContentCsvs,
                "Answer Option change.csv");
        courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, llc, contentName);
        assertEquals(courseProcessedContent.getContentFile(), fileName);
        correctAnswer = 2;
        assertEquals(correctAnswer, courseService.getCorrectAnswerOption(1, 1));
    }

    /*
     * this test case is used to test answer option update for multiple LLCs at
     * one go.
     */
    @Test
    public void testAnswerOptionChangeForMultipleLlc() throws Exception {
        clearMobileAcademyData();
        createLlcCircleMapping();

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);

        Integer llc1 = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc1, "AP");
        long rawContentSize = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc1);
        assertEquals(rawContentSize, courseProcessedContents.size());

        // add other LLC records i.e 20
        courseContentCsvs = findCourseRawContentListFromCsv("20");
        Integer llc2 = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc2, "AP");
        long rawContentSize2 = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc2);
        assertEquals(rawContentSize2, courseProcessedContents.size());

        // add other LLC records i.e 30
        courseContentCsvs = findCourseRawContentListFromCsv("30");
        Integer llc3 = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        // createCircleLlcMapping(llc3, "AP");
        long rawContentSize3 = courseContentCsvs.size();
        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");
        courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc2);
        assertEquals(rawContentSize3, courseProcessedContents.size());

        // Will update the correct answer option for Question 2 of Chapter 01
        // from 2 to 1
        int correctAnswer = courseService.getCorrectAnswerOption(1, 2);

        courseContentCsvs = new ArrayList<CourseContentCsv>();
        courseContentCsvs.add(new CourseContentCsv("100014", "AP", llc1
                .toString(), "Chapter01_Question02", "Content", "ch1_q2.wav",
                "150", "correctAnswer:1"));
        courseContentCsvs.add(new CourseContentCsv("100015", "AP", llc2
                .toString(), "Chapter01_Question02", "Content", "ch1_q2.wav",
                "150", "correctAnswer:1"));
        courseContentCsvs.add(new CourseContentCsv("100016", "AP", llc3
                .toString(), "Chapter01_Question02", "Content", "ch1_q2.wav",
                "150", "correctAnswer:1"));

        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");

        correctAnswer = courseService.getCorrectAnswerOption(1, 2);
        assertEquals(1, correctAnswer);
    }
}