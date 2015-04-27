package org.motechproject.nms.mobileacademy.event.handler.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
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
import org.motechproject.nms.mobileacademy.event.handler.CourseUploadCsvHandler;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseContentCsvDataService;
import org.motechproject.nms.mobileacademy.repository.CourseProcessedContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseContentCsvService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.motechproject.nms.mobileacademy.service.RecordsProcessService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

/**
 * Verify that CourseUploadCsvHandler is present and functional
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CourseUploadCsvHandlerIT extends BasePaxIT {

    @Inject
    private RecordsProcessService recordsProcessService;

    @Inject
    private CourseContentCsvDataService courseContentCsvDataService;

    @Inject
    private CourseProcessedContentDataService courseProcessedContentDataService;

    @Inject
    private ChapterContentDataService chapterContentDataService;

    @Inject
    private MTrainingService mTrainingService;

    private CourseUploadCsvHandler courseUploadCsvHandler;

    @Inject
    private CourseContentCsvService courseContentCsvService;

    @Inject
    private CourseProcessedContentService courseProcessedContentService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private StateService stateService;

    @Inject
    private CircleService circleService;

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
     * setUp method called before each test case
     */
    @Before
    public void setUp() {
        assertNotNull(recordsProcessService);
        assertNotNull(courseContentCsvService);
        courseUploadCsvHandler = new CourseUploadCsvHandler(
                recordsProcessService, courseContentCsvService);
        assertNotNull(courseUploadCsvHandler);
        clearMobileAcademyData();

    }

    /**
     * tearDown method called after each test case
     */
    @After
    public void tearDown() {
        clearMobileAcademyData();
    }

    /**
     * test CourseCsvData Success Handler
     */
    @Test
    public void testCourseCsvDataSuccessHandler() {
        List<Long> contentIds = findCourseRawContentIdsListFromCsv();
        createLlcCircleMapping();
        courseUploadCsvHandler
                .courseCsvDataSuccessHandler(createMotechEvent(contentIds));
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(14);
        assertEquals(contentIds.size(), courseProcessedContents.size());

    }

    /**
     * create MotechEvent
     * 
     * @param ids
     * @return MotechEvent
     */
    public MotechEvent createMotechEvent(List<Long> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "CourseContentCsv");
        return new MotechEvent(
                MobileAcademyConstants.COURSE_CSV_UPLOAD_SUCCESS_EVENT, params);
    }

    /**
     * clear MobileAcademy Data
     */
    private void clearMobileAcademyData() {
        courseContentCsvService.deleteAll();
        courseProcessedContentService.deleteAll();
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
     * find CourseContentCsv Ids List From Csv
     * 
     * @return List<Long>
     */
    private List<Long> findCourseRawContentIdsListFromCsv() {
        List<Long> courseRawContentIds = new ArrayList<>();
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

                courseContentCsv.setLanguageLocationCode(tokens[arrayIndex++]);
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
                CourseContentCsv courseRawContentReturn = courseContentCsvDataService
                        .create(courseContentCsv);
                courseRawContentIds.add(courseRawContentReturn.getId());

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
        return courseRawContentIds;
    }
}
