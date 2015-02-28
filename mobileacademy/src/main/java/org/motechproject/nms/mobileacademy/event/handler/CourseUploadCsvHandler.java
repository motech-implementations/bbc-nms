package org.motechproject.nms.mobileacademy.event.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CSVRecordProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class handles success and failure events for CourseRawContent that
 * contains course structure.
 */

@Component
public class CourseUploadCsvHandler {

	@Autowired
	private CSVRecordProcessService csvRecordProcessService;

	@Autowired
	private CourseRawContentDataService courseRawContentDataService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CourseUploadCsvHandler.class);

	/**
	 * This method provides a listener to the course csv upload success
	 * scenario.
	 * 
	 * @param motechEvent
	 *            name of the event raised during upload
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
	 * find List Of CourseRawContent on the basis of received Id
	 * 
	 * @param createdIds
	 *            List of created id on csv upload
	 * @return List<CourseRawContent> List Of CourseRawContent
	 */
	private List<CourseRawContent> findListOfCourseRawContents(
			List<Long> createdIds) {
		List<CourseRawContent> listOfCourseRawContents = new ArrayList<>();
		for (Long id : createdIds) {
			listOfCourseRawContents.add(courseRawContentDataService
					.findById(id));
		}
		return listOfCourseRawContents;
	}

	/**
	 * This method provides a listener to the course csv upload failure
	 * scenario.
	 * 
	 * @param motechEvent
	 *            name of the event raised during upload
	 */
	@MotechListener(subjects = { MobileAcademyConstants.COURSE_CSV_UPLOAD_FAILURE_EVENT })
	public void courseCsvDataFailureHandler(MotechEvent motechEvent) {
		LOGGER.info("Entered into COURSE_CSV_UPLOAD_FAILURE_EVENT Handler");
		Map<String, Object> params = motechEvent.getParameters();
		LOGGER.info(
				"Start processing CourseRawContent-import failure for upload {}",
				params.toString());
		List<Long> createdIds = (ArrayList<Long>) params
				.get("csv-import.created_ids");
		for (Long id : createdIds) {
			CourseRawContent courseRawContent = courseRawContentDataService
					.findById(id);
			if (courseRawContent != null) {
				courseRawContentDataService.delete(courseRawContent);
				LOGGER.info(
						"Record deleted successfully from CourseRawContent table for id {}",
						id.toString());
			}
		}
		LOGGER.info("Finished processing COURSE_CSV_UPLOAD_FAILURE_EVENT Handler");
	}

}
