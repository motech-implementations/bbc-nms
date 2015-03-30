package org.motechproject.nms.mobileacademy.web;

import org.apache.log4j.Logger;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * CourseController handles requests for Get Course and get Course version
 *
 */
@Controller
public class CourseController extends BaseController {

    private static final Logger LOGGER = Logger
            .getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    /**
     * Get Course API
     * 
     * @return the current course in system in JSON format
     */
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public String getCourse() throws InternalException {
        LOGGER.debug("getCourse: Started");
        Course course = courseService.getMtrainingCourse();
        if (course == null || course.getState() == CourseUnitState.Inactive) {
            LOGGER.error(MobileAcademyConstants.NO_COURSE_PRESENT);
            throw new InternalException(
                    MobileAcademyConstants.NO_COURSE_PRESENT);
        } else {
            LOGGER.debug("getCourse: Ended");
            return courseService.getCourseJson();
        }
    }

    /**
     * Get Course Version API
     * 
     * @return the version of the current course in the system in Integer
     */
    @RequestMapping(value = "/courseVersion", method = RequestMethod.GET)
    public String getCourseVersion() throws InternalException {
        LOGGER.debug("getCourseVersion: Started");
        Integer courseVersion = courseService.getCurrentCourseVersion();
        if (courseVersion == null) {
            LOGGER.error(MobileAcademyConstants.NO_COURSE_PRESENT);
            throw new InternalException(
                    MobileAcademyConstants.NO_COURSE_PRESENT);
        } else {
            LOGGER.debug("getCourseVersion: Ended");
            return getJsonNode(MobileAcademyConstants.COURSE_KEY_VERSION,
                    courseVersion);
        }
    }

    /*
     * This provides a JSON node in form of key-value node for integer value
     */
    private String getJsonNode(String key, Integer value) {
        String response;
        response = "{\"" + key + "\":" + value + "}";
        return response;
    }

}
