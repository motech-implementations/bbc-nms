package org.motechproject.nms.mobileacademy.web;

import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CSVRecordProcessService;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.motechproject.nms.mobileacademy.service.CourseRawContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for MobileAcademy Module
 */
@Controller
public class MobileAcademyController {

    @Autowired
    private CourseRawContentService courseRawContentService;

    @Autowired
    private CourseProcessedContentService courseProcessedContentService;

    @Autowired
    private CoursePopulateService coursePopulateService;

    @Autowired
    private CSVRecordProcessService csvRecordProcessService;

    @Autowired
    private CourseRawContentDataService courseRawContentDataService;

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
                courseRawContentDataService.retrieveAll(), "csv name");
    }

    @RequestMapping(value = "/deleteData")
    @ResponseBody
    public String deleteData() {
        courseRawContentService.deleteAll();
        courseProcessedContentService.deleteAll();
        return "Data Deleted";
    }

    @RequestMapping(value = "/resetState")
    @ResponseBody
    public String resetState() {
        coursePopulateService.updateCourseState(CourseUnitState.Inactive);
        return "Course Unit State is reset";
    }

}
