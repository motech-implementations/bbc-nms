package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;
import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
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
 * /** Verify that CSVRecordProcessService is present and functional
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CSVRecordProcessServiceIT extends BasePaxIT {

    @Inject
    private CSVRecordProcessService csvRecordProcessService;

    @Inject
    private CourseRawContentDataService courseRawContentDataService;

    @Inject
    private CourseProcessedContentDataService courseProcessedContentDataService;

    @Inject
    private ChapterContentDataService chapterContentDataService;

    /**
     * test CoursePopulateService Instance is not null
     */
    @Test
    public void testCoursePopulateServiceInstance() throws Exception {

        assertNotNull(csvRecordProcessService);
    }

    /**
     * clear Mobile Academy Data
     */
    private void clearMobileAcademyData() {
        courseRawContentDataService.deleteAll();
        courseProcessedContentDataService.deleteAll();
        chapterContentDataService.deleteAll();
    }

    /**
     * test Course Populate Success scenario
     * 
     * @throws Exception
     */
    @Test
    public void testCoursePopulateSuccess() throws Exception {
        clearMobileAcademyData();
        List<CourseRawContent> courseRawContents = findCourseRawContentListFromCsv();
        Integer llc = Integer.parseInt(courseRawContents.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseRawContents.size();
        csvRecordProcessService.processRawRecords(courseRawContents,
                "CourseRawContent.csv");
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
        List<CourseRawContent> courseRawContents = findCourseRawContentListFromCsv();
        // Added one more record
        courseRawContents.add(addNewRecord(courseRawContents.get(0)));
        Integer llc = Integer.parseInt(courseRawContents.get(0)
                .getLanguageLocationCode());
        csvRecordProcessService.processRawRecords(courseRawContents,
                "CourseRawContent.csv");
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
        List<CourseRawContent> courseRawContents = findCourseRawContentListFromCsv();
        Integer llc = Integer.parseInt(courseRawContents.get(0)
                .getLanguageLocationCode());
        // Removed one record
        courseRawContentDataService.delete(courseRawContents.get(1));
        courseRawContents.remove(1);
        csvRecordProcessService.processRawRecords(courseRawContents,
                "CourseRawContent.csv");
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
        List<CourseRawContent> courseRawContentsForAdd = findCourseRawContentListFromCsv();
        Integer llc = Integer.parseInt(courseRawContentsForAdd.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseRawContentsForAdd.size();
        csvRecordProcessService.processRawRecords(courseRawContentsForAdd,
                "CourseRawContent.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());
        // Records Processed for DEL Begin
        List<CourseRawContent> courseRawContentsForDel = findCourseRawContentListFromCsv();
        for (CourseRawContent courseRawContent : courseRawContentsForDel) {
            courseRawContent.setOperation("DEL");
        }
        csvRecordProcessService.processRawRecords(courseRawContentsForDel,
                "CourseRawContent.csv");
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
        List<CourseRawContent> courseRawContentsForAdd = findCourseRawContentListFromCsv();
        Integer llc = Integer.parseInt(courseRawContentsForAdd.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseRawContentsForAdd.size();
        csvRecordProcessService.processRawRecords(courseRawContentsForAdd,
                "CourseRawContent.csv");
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContents.size());
        // Records Processed for DEL Begin
        List<CourseRawContent> courseRawContentsForDel = findCourseRawContentListFromCsv();
        for (CourseRawContent courseRawContent : courseRawContentsForDel) {
            courseRawContent.setOperation("DEL");
        }
        // Removed one record
        courseRawContentDataService.delete(courseRawContentsForDel.get(1));
        courseRawContentsForDel.remove(1);
        csvRecordProcessService.processRawRecords(courseRawContentsForDel,
                "CourseRawContent.csv");
        List<CourseProcessedContent> courseProcessedContentsDel = courseProcessedContentDataService
                .findContentByLlc(llc);
        assertEquals(rawContentSize, courseProcessedContentsDel.size());// No
                                                                        // Record
                                                                        // Deleted

    }

    /**
     * find CourseRawContent List From Csv
     * 
     * @return List<CourseRawContent>
     */
    private List<CourseRawContent> findCourseRawContentListFromCsv() {
        List<CourseRawContent> courseRawContents = new ArrayList<>();
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
                courseRawContents.add(courseRawContentDataService
                        .create(courseRawContent));

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
        return courseRawContents;
    }

    /**
     * test Course Update Success scenario
     * 
     * @throws Exception
     */
    @Test
    public void testCourseUpdateSuccess() throws Exception {
        clearMobileAcademyData();
        List<CourseRawContent> courseRawContents = findCourseRawContentListFromCsv();
        CourseRawContent courseRawContentUpdateRecord = courseRawContents
                .get(0);
        Integer llc = Integer.parseInt(courseRawContents.get(0)
                .getLanguageLocationCode());
        long rawContentSize = courseRawContents.size();
        csvRecordProcessService.processRawRecords(courseRawContents,
                "CourseRawContent.csv");
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
        courseRawContents = new ArrayList<>();
        courseRawContents.add(addNewRecord(courseRawContentUpdateRecord));
        csvRecordProcessService.processRawRecords(courseRawContents,
                "CourseRawContent.csv");
        CourseProcessedContent courseProcessedContent = courseProcessedContentDataService
                .findByCircleLlcContentName(circle, languageLocationCode,
                        contentName);
        assertEquals(222, courseProcessedContent.getContentID().longValue());
        assertEquals(333, courseProcessedContent.getContentDuration()
                .longValue());

    }

    /**
     * add New Record in course raw content table.
     * 
     * @param courseRawContentParam
     * @return CourseRawContent
     */
    private CourseRawContent addNewRecord(CourseRawContent courseRawContentParam) {
        CourseRawContent courseRawContent = new CourseRawContent();
        courseRawContent.setOperation(courseRawContentParam.getOperation());
        courseRawContent.setContentId(courseRawContentParam.getContentId());
        courseRawContent.setCircle(courseRawContentParam.getCircle());
        courseRawContent.setLanguageLocationCode(courseRawContentParam
                .getLanguageLocationCode());
        courseRawContent.setContentName(courseRawContentParam.getContentName());
        courseRawContent.setContentType(courseRawContentParam.getContentType());
        courseRawContent.setContentFile(courseRawContentParam.getContentFile());
        courseRawContent.setContentDuration(courseRawContentParam
                .getContentDuration());
        courseRawContent.setMetaData(courseRawContentParam.getMetaData());
        return courseRawContentDataService.create(courseRawContent);
    }
}
