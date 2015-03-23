package org.motechproject.nms.mobileacademy.web;

import org.apache.log4j.Logger;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * To be implemented in next review cycle
 * 
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
     * Get MA Course in JSON format
     */
    @RequestMapping(value = "/getMACourse", method = RequestMethod.GET)
    public ResponseEntity<String> getMACourse() {
        LOGGER.debug("getMACourse: Started");
        Course course = courseService.getMtrainingCourse();
        if (course == null) {
            LOGGER.error("No Course Present in the System");
            String responseJson = "{\"failureReason\":\""
                    + MobileAcademyConstants.NO_COURSE_PRESENT + "\"}";
            LOGGER.debug("getMACourse: Ended");
            return new ResponseEntity<String>(responseJson,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (course.getState() == CourseUnitState.Inactive) {
            LOGGER.error("No Course Present in the System");
            String responseJson = "{\"failureReason\":\""
                    + MobileAcademyConstants.COURSE_UPLOAD_ONGOING + "\"}";
            LOGGER.debug("getMACourse: Ended");
            return new ResponseEntity<String>(responseJson,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            String courseJson = courseService.getCourseJson();
            LOGGER.debug("getMACourse: Ended");
            return new ResponseEntity<String>(courseJson, HttpStatus.OK);
        }
    }

    /**
     * Get Current MA Course version
     */
    @RequestMapping(value = "/getMACourseVersion", method = RequestMethod.GET)
    public ResponseEntity<String> getMACourseVersion() {
        LOGGER.debug("getMACourseVersion: Started");
        Course course = courseService.getMtrainingCourse();
        String responseJson;
        if (course == null) {
            LOGGER.error(MobileAcademyConstants.NO_COURSE_PRESENT);
            responseJson = "{\"failureReason\":\""
                    + MobileAcademyConstants.NO_COURSE_PRESENT + "\"}";
            LOGGER.debug("getMACourseVersion: Ended");
            return new ResponseEntity<String>(responseJson,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (course.getState() == CourseUnitState.Inactive) {
            LOGGER.error(MobileAcademyConstants.COURSE_UPLOAD_ONGOING);
            responseJson = "{\"failureReason\":\""
                    + MobileAcademyConstants.COURSE_UPLOAD_ONGOING + "\"}";
            LOGGER.debug("getMACourseVersion: Ended");
            return new ResponseEntity<String>(responseJson,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            long courseVersion = courseService.getCurrentCourseVersion();
            responseJson = "{\"courseVersion\":\" " + courseVersion + "\"}";
            LOGGER.debug("getMACourseVersion: Ended");
            return new ResponseEntity<String>(responseJson, HttpStatus.OK);
        }
    }
}
