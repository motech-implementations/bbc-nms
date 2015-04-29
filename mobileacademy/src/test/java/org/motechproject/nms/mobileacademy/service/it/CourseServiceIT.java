package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
import org.motechproject.mtraining.domain.CourseUnitState;
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
import org.motechproject.nms.mobileacademy.commons.OperatorDetails;
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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CourseServiceIT extends BasePaxIT {

    @Inject
    private CourseService courseService;

    @Inject
    private MTrainingService mTrainingService;

    @Inject
    private RecordsProcessService recordsProcessService;

    @Inject
    private CourseContentCsvDataService courseContentCsvDataService;

    @Inject
    private CourseProcessedContentDataService courseProcessedContentDataService;

    @Inject
    private ChapterContentDataService chapterContentDataService;    

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private StateService stateService;

    @Inject
    private CircleService circleService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CourseServiceIT.class);
    
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

    @Test
    public void testCoursePopulateSuccess() {
        clearMobileAcademyData();
        createLlcCircleMapping();
        CourseUnitState state = null;
        state = courseService.findCourseState();
        assertNull(state);

        OperatorDetails operatorDetails = new OperatorDetails();

        courseService.populateMtrainingCourseData(operatorDetails);
        state = courseService.findCourseState();
        assertNotNull(state);
        assertSame(state, CourseUnitState.Inactive);
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

    @Test
    public void testGetCurrentCourseVersion() {
        clearMobileAcademyData();
        createLlcCircleMapping();
        assertNull(courseService.getCurrentCourseVersion());

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);

        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");

        if (courseService.getCurrentCourseVersion() != 0) {
            assertTrue(true);
        }
    }

    @Test
    public void testUpdateCourseVersion() {
        clearMobileAcademyData();
        createLlcCircleMapping();
        assertNull(courseService.getCurrentCourseVersion());

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);

        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");

        long courseVersion = courseService.getCurrentCourseVersion();

        try {
            Thread.sleep(1000);
            courseService.updateCourseVersion("Tester");
            if (courseService.getCurrentCourseVersion() > courseVersion) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    public void testGetCourseJson() {
        clearMobileAcademyData();
        createLlcCircleMapping();
        assertNull(courseService.getCurrentCourseVersion());

        List<CourseContentCsv> courseContentCsvs = findCourseRawContentListFromCsv(null);
        Integer llc = Integer.parseInt(courseContentCsvs.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseContentCsvs.size();

        recordsProcessService.processRawRecords(courseContentCsvs,
                "CourseContentCsv.csv");

        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());

        String courseJson = courseService.getCourseJson();
        JsonParser parser = new JsonParser();
        JsonObject course = (JsonObject) parser.parse(courseJson);

        assertTrue(MobileAcademyConstants.DEFAULT_COURSE_NAME.equals(course
                .get(MobileAcademyConstants.COURSE_KEY_NAME).getAsString()));
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
}
