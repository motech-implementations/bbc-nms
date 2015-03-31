package org.motechproject.nms.mobileacademy.web;

import org.apache.log4j.Logger;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.InternalException;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.dto.BookmarkWithScore;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    private static final String REQUEST_PARAM_CALLING_NUMBER = "callingNumber";

    private static final String REQUEST_PARAM_CALL_ID = "callId";

    /**
     * Get Course API
     * 
     * @return the current course in system in JSON format
     */
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    @ResponseBody
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
    @ResponseBody
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

    /**
     * Get Bookmark With Score API
     * 
     * @param callingNumber mobile number of the caller
     * @param callId unique call id assigned by IVR
     */
    // This is just a placeholder for get bookmark with score API.
    // actual implementation would be done in sprint 1504
    @RequestMapping(value = "/bookmarkWithScore", method = RequestMethod.GET)
    @ResponseBody
    public void getBookmarkWithScore(
            @RequestParam(value = CourseController.REQUEST_PARAM_CALLING_NUMBER) String callingNumber,
            @RequestParam(value = CourseController.REQUEST_PARAM_CALL_ID) String callId) {
        LOGGER.debug("getBookmarkWithScore: Started");
        LOGGER.debug("Request Received");
        LOGGER.debug("getBookmarkWithScore: Ended");
    }

    /**
     * Save Bookmark With Score API
     * 
     * @param bookmarkWithScore object contain input request
     * @throws MissingServletRequestParameterException
     */
    // This is just a placeholder for save bookmark with score API.
    // actual implementation would be done in sprint 1504
    @RequestMapping(value = "/bookmarkWithScore", method = RequestMethod.POST)
    @ResponseBody
    public void saveBookmarkWithScore(
            @RequestBody BookmarkWithScore bookmarkWithScore)
            throws MissingServletRequestParameterException {
        LOGGER.debug("saveBookmarkWithScore: Started");
        LOGGER.debug("Input Request: " + bookmarkWithScore);
        LOGGER.debug("saveBookmarkWithScore: Ended");
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
