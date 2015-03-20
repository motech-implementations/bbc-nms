package org.motechproject.nms.mobileacademy.web;

import org.apache.log4j.Logger;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * UserController handles following requests i.e Get User, Set language location
 * code, Save Call details.
 *
 */
@Controller
public class CourseController extends BaseController {

    private static final Logger LOGGER = Logger
            .getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    /**
     * Get MA Course JSON
     */
    @RequestMapping(value = "/getMACourse", method = RequestMethod.GET)
    public @ResponseBody String getMACourse() {
        LOGGER.debug("getMACourse: Started");
        String courseJson = courseService.getCourseJson();
        LOGGER.debug("getMACourse: Ended");
        return courseJson;
    }

    /**
     * Get Current MA Course version
     */
    @RequestMapping(value = "/getMACourseVersion", method = RequestMethod.GET)
    public @ResponseBody long getMACourseVersion() {
        LOGGER.debug("getMACourseVersion: Started");
        long courseVersion = courseService.getCurrentCourseVersion();
        LOGGER.debug("getMACourseVersion: Ended");
        return courseVersion;
    }
}
