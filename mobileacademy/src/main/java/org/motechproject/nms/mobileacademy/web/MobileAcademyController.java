package org.motechproject.nms.mobileacademy.web;

import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.repository.ChapterContentDataService;
import org.motechproject.nms.mobileacademy.repository.CourseContentCsvDataService;
import org.motechproject.nms.mobileacademy.service.CSVRecordProcessService;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.motechproject.nms.mobileacademy.service.CourseContentCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for MobileAcademy Module, currently using it for testing services.
 * Will be removed later.
 */
@Controller
public class MobileAcademyController {

	@Autowired
	private CourseContentCsvService courseContentCsvService;

	@Autowired
	private CourseProcessedContentService courseProcessedContentService;

	@Autowired
	private CoursePopulateService coursePopulateService;

	@Autowired
	private CSVRecordProcessService csvRecordProcessService;

	@Autowired
	private CourseContentCsvDataService courseContentCsvDataService;

	@Autowired
	private ChapterContentDataService chapterContentDataService;

	private static final String OK = "OK";

	@RequestMapping("/web-api/status")
	@ResponseBody
	public String status() {
		return OK;
	}

	@RequestMapping(value = "/processData")
	@ResponseBody
	public String processData() {
		return csvRecordProcessService.processRawRecords(
				courseContentCsvDataService.retrieveAll(), "ccontroller-csv");
	}

	@RequestMapping(value = "/deleteData")
	@ResponseBody
	public String deleteData() {
		courseContentCsvService.deleteAll();
		courseProcessedContentService.deleteAll();
		chapterContentDataService.deleteAll();
		return "Data Deleted";
	}

	@RequestMapping(value = "/resetState")
	@ResponseBody
	public String resetState() {
		coursePopulateService.updateCourseState(CourseUnitState.Inactive);
		return "Course Unit State is reset";
	}

}
