package org.motechproject.nms.mobileacademy.web;

import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.service.CoursePopulateService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.motechproject.nms.mobileacademy.service.CourseRawContentService;
import org.motechproject.nms.mobileacademy.service.RecordProcessService;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for HelloWorld message and bundle status.
 */
@Controller
public class MobileAcademyController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CourseRawContentService courseRawContentService;

    @Autowired
    CourseProcessedContentService courseProcessedContentService;

    @Autowired
    CoursePopulateService coursePopulateService;

    @Autowired
    RecordProcessService recordProcessService;

    private static final String OK = "OK";

    @RequestMapping("/web-api/status")
    @ResponseBody
    public String status() {
        return OK;
    }

    @RequestMapping(value = "/processData")
    @ResponseBody
    public String processData(){
        return recordProcessService.processRawRecords();
    }

    @RequestMapping(value = "/deleteData")
    @ResponseBody
    public String deleteData(){
        courseRawContentService.deleteAll();
        courseProcessedContentService.deleteAll();
        return "Data Deleted";
    }

    @RequestMapping(value = "/resetState")
    @ResponseBody
    public String resetState(){
        coursePopulateService.updateCourseState(CourseUnitState.Inactive);
        return "Course Unit State is reset";
    }


}
