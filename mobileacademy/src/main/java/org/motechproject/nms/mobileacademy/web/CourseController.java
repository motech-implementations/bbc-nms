package org.motechproject.nms.mobileacademy.web;

import java.util.Map;

import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.dto.BookmarkWithScore;
import org.motechproject.nms.mobileacademy.service.CourseBookmarkService;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseBookmarkService courseBookmarkService;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String REQUEST_PARAM_CALLING_NUMBER = "callingNumber";

    private static final String REQUEST_PARAM_CALL_ID = "callId";

    /**
     * Get Course API
     * 
     * @return the current course in system in JSON format
     */
    @RequestMapping(value = "/course", method = RequestMethod.GET)
    @ResponseBody
    public String getCourse() throws NmsInternalServerError {
        LOGGER.debug("getCourse: Started");
        Course course = courseService.getMtrainingCourse();
        if (course == null || course.getState() == CourseUnitState.Inactive) {
            LOGGER.error(MobileAcademyConstants.NO_COURSE_PRESENT);
            throw new NmsInternalServerError(
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
    public String getCourseVersion() throws NmsInternalServerError {
        LOGGER.debug("getCourseVersion: Started");
        Integer courseVersion = courseService.getCurrentCourseVersion();
        if (courseVersion == null) {
            LOGGER.error(MobileAcademyConstants.NO_COURSE_PRESENT);
            throw new NmsInternalServerError(
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
    @RequestMapping(value = "/bookmarkWithScore", method = RequestMethod.GET)
    @ResponseBody
    public String getBookmarkWithScore(
            @RequestParam(value = CourseController.REQUEST_PARAM_CALLING_NUMBER) String callingNo,
            @RequestParam(value = CourseController.REQUEST_PARAM_CALL_ID) String callId)
            throws DataValidationException {
        LOGGER.debug("getBookmarkWithScore: Started for MSISDN: {}", callingNo);
        String originalCallingNo = callingNo;

        callingNo = validateAndGetCallingNo(callingNo);

        validateCallId(callId);

        if (!userDetailsService.doesMsisdnExist(callingNo)) {
            LOGGER.error("MSISDN: {} doesn't exist with FLW service",
                    originalCallingNo);
            ParseDataHelper.raiseInvalidDataException("MSISDN", callingNo);
        }

        String bookmarkJson = courseBookmarkService
                .getBookmarkWithScore(callingNo);
        LOGGER.debug("Sending bookmark JSON as : {} for calling no.: {}",
                bookmarkJson, callingNo);
        LOGGER.debug("getBookmarkWithScore: Ended for MSISDN: {}", callingNo);
        return bookmarkJson;
    }

    /**
     * Save Bookmark With Score API
     * 
     * @param bookmarkWithScore object contain input request
     * @throws MissingServletRequestParameterException
     * @throws DataValidationException
     */
    @RequestMapping(value = "/bookmarkWithScore", method = RequestMethod.POST)
    @ResponseBody
    public void saveBookmarkWithScore(
            @RequestBody BookmarkWithScore bookmarkWithScore)
            throws MissingServletRequestParameterException,
            DataValidationException {
        LOGGER.debug("saveBookmarkWithScore: Started");
        LOGGER.debug("Input Request: " + bookmarkWithScore);

        String originalCallingNo = bookmarkWithScore.getCallingNumber();

        String callingNo = validateAndGetCallingNo(originalCallingNo);

        String callId = bookmarkWithScore.getCallId();
        validateCallId(callId);

        if (!userDetailsService.doesMsisdnExist(callingNo)) {
            LOGGER.error("MSISDN: {} doesn't exist with FLW service",
                    originalCallingNo);
            ParseDataHelper.raiseInvalidDataException("MSISDN",
                    bookmarkWithScore.getCallingNumber());
        }

        String bookmarkId = bookmarkWithScore.getBookmark();
        Map<String, String> scoresByChapter = bookmarkWithScore
                .getScoresByChapter();

        courseBookmarkService.saveBookmarkWithScore(bookmarkId,
                scoresByChapter, callingNo);

        LOGGER.debug("saveBookmarkWithScore: Ended for MSISDN: {}", callingNo);
    }

    /*
     * Used for validating the callID as per Util module
     */
    private void validateCallId(String callId) throws DataValidationException {
        ParseDataHelper.validateLengthOfCallId(
                MobileAcademyConstants.REQUEST_PARAM_CALL_ID, callId);
    }

    /*
     * Used for validating and getting the last 10 digits of MSISDN.
     */
    private String validateAndGetCallingNo(String callingNumber)
            throws DataValidationException {
        ParseDataHelper.validateAndParseString(
                MobileAcademyConstants.REQUEST_PARAM_CALLING_NUMBER,
                callingNumber, true);
        return ParseDataHelper.validateAndTrimMsisdn(
                MobileAcademyConstants.REQUEST_PARAM_CALLING_NUMBER,
                callingNumber);
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
