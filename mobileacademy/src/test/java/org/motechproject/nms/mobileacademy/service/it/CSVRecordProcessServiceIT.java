package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CSVRecordProcessService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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

    private static final org.apache.log4j.Logger LOGGER = Logger
            .getLogger(CSVRecordProcessServiceIT.class);

    @Test
    public void testCoursePopulateServiceInstance() throws Exception {

        assertNotNull(csvRecordProcessService);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testProcessRawRecords() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                    this.getClass().getClassLoader());
            csvRecordProcessService.processRawRecords(
                    findCourseRawContentList(), "CourseRawContent.csv");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            assertFalse(true);
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    private List<CourseRawContent> findCourseRawContentList() {
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
}
