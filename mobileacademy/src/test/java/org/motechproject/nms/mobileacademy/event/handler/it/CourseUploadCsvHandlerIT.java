package org.motechproject.nms.mobileacademy.event.handler.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;
import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
import org.motechproject.nms.mobileacademy.event.handler.CourseUploadCsvHandler;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseProcessedContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CSVRecordProcessService;
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
    private CSVRecordProcessService csvRecordProcessService;

    @Inject
    private CourseRawContentDataService courseRawContentDataService;

    @Inject
    private CourseProcessedContentDataService courseProcessedContentDataService;

    @Inject
    private ChapterContentDataService chapterContentDataService;

    @Inject
    private MTrainingService mTrainingService;

    private CourseUploadCsvHandler courseUploadCsvHandler;

    /**
     * setUp method called before each test case
     */
    @Before
    public void setUp() {
        assertNotNull(csvRecordProcessService);
        assertNotNull(courseRawContentDataService);
        courseUploadCsvHandler = new CourseUploadCsvHandler(
                csvRecordProcessService, courseRawContentDataService);
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
        params.put("csv-import.filename", "CourseRawContent");
        return new MotechEvent(
                MobileAcademyConstants.COURSE_CSV_UPLOAD_SUCCESS_EVENT, params);
    }

    /**
     * clear MobileAcademy Data
     */
    private void clearMobileAcademyData() {
        courseRawContentDataService.deleteAll();
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
     * find CourseRawContent Ids List From Csv
     * 
     * @return List<Long>
     */
    private List<Long> findCourseRawContentIdsListFromCsv() {
        List<Long> courseRawContentIds = new ArrayList<>();
        // Input file which needs to be parsed
        String fileToParse = "src//test//resources//CourseRawContent.csv";
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
                CourseRawContent courseRawContent = new CourseRawContent();
                courseRawContent.setOperation(tokens[arrayIndex++]);
                courseRawContent.setContentId(tokens[arrayIndex++]);
                courseRawContent.setCircle(tokens[arrayIndex++]);

                courseRawContent.setLanguageLocationCode(tokens[arrayIndex++]);
                courseRawContent.setContentName(tokens[arrayIndex++]);
                courseRawContent.setContentType(tokens[arrayIndex++]);
                courseRawContent.setContentFile(tokens[arrayIndex++]);
                courseRawContent.setContentDuration(tokens[arrayIndex++]);
                if (tokens.length > arrayIndex) {
                    courseRawContent.setMetaData(tokens[arrayIndex]);
                }
                CourseRawContent courseRawContentReturn = courseRawContentDataService
                        .create(courseRawContent);
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
