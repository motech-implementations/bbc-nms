package org.motechproject.nms.mobileacademy.event.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;
import org.motechproject.nms.mobileacademy.service.CSVRecordProcessService;
import org.motechproject.nms.mobileacademy.service.CourseContentCsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class handles success and failure events for CourseContentCsv that
 * contains course structure.
 */

@Component
public class CourseUploadCsvHandler {

    private CSVRecordProcessService csvRecordProcessService;

    private CourseContentCsvService courseContentCsvService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CourseUploadCsvHandler.class);

    @Autowired
    public CourseUploadCsvHandler(
            CSVRecordProcessService csvRecordProcessService,
            CourseContentCsvService courseContentCsvService) {
        this.csvRecordProcessService = csvRecordProcessService;
        this.courseContentCsvService = courseContentCsvService;
    }

    /**
     * This method provides a listener to the course csv upload success
     * scenario.
     * 
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = { MobileAcademyConstants.COURSE_CSV_UPLOAD_SUCCESS_EVENT })
    public void courseCsvDataSuccessHandler(MotechEvent motechEvent) {
        LOGGER.info("Entered into COURSE_CSV_UPLOAD_SUCCESS_EVENT Handler");
        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>) params
                .get("csv-import.created_ids");
        String csvFileName = (String) params.get("csv-import.filename");
        LOGGER.info("Csv file name received in event : {}", csvFileName);

        csvRecordProcessService.processRawRecords(
                findListOfCourseRawContents(createdIds), csvFileName);

        LOGGER.info("Finished processing COURSE_CSV_UPLOAD_SUCCESS_EVENT Handler");
    }

    /**
     * find List Of CourseContentCsv on the basis of received Id
     * 
     * @param createdIds List of created id on csv upload
     * @return List<CourseContentCsv> List Of CourseContentCsv
     */
    private List<CourseContentCsv> findListOfCourseRawContents(
            List<Long> createdIds) {
        List<CourseContentCsv> listOfCourseRawContents = new ArrayList<>();
        for (Long id : createdIds) {
            listOfCourseRawContents.add(courseContentCsvService.findById(id));
        }
        return listOfCourseRawContents;
    }

}
