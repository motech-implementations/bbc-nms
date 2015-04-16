package org.motechproject.nms.mobileacademy.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.dto.BookmarkWithScore;
import org.motechproject.nms.mobileacademy.helper.BookmarkHelper;
import org.motechproject.nms.mobileacademy.service.ConfigurationService;
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

	@Autowired
	private ConfigurationService configurationService;

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
	 * @param callingNumber
	 *            mobile number of the caller
	 * @param callId
	 *            unique call id assigned by IVR
	 */
	@RequestMapping(value = "/bookmarkWithScore", method = RequestMethod.GET)
	@ResponseBody
	public String getBookmarkWithScore(
			@RequestParam(value = CourseController.REQUEST_PARAM_CALLING_NUMBER) String callingNo,
			@RequestParam(value = CourseController.REQUEST_PARAM_CALL_ID) String callId)
			throws DataValidationException {
		LOGGER.debug("getBookmarkWithScore: Started for MSISDN: {}",
				callingNo);

		callingNo = validateAndGetCallingNo(callingNo);

		validateCallId(callId);

		if (!userDetailsService.doesMsisdnExists(callingNo)) {
			LOGGER.error("MSISDN: {} doesn't exist with FLW service",
				callingNo);
			ParseDataHelper.raiseInvalidDataException("MSISDN", callingNo);
		}

		Bookmark bookmark = courseBookmarkService
				.getBookmarkByMsisdn(callingNo);
		String bookmarkToReturn = "";
		if ((bookmark == null)
				|| (StringUtils.isBlank((String) bookmark.getProgress().get(
						MobileAcademyConstants.BOOKMARK_ID)))) {
			LOGGER.debug("There is no bookmark in the system for MSISDN: {}",
					callingNo);
			bookmarkToReturn = MobileAcademyConstants.EMPTY_JSON;
		} else {
			bookmarkToReturn = BookmarkHelper.getBookmarkJson(bookmark);
		}

		LOGGER.debug("getBookmarkWithScore: Ended for MSISDN: {}",
				callingNo);
		return bookmarkToReturn;
	}

	/**
	 * Save Bookmark With Score API
	 * 
	 * @param bookmarkWithScore
	 *            object contain input request
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

		boolean firstBookmark = false;
		String callingNo = validateAndGetCallingNo(bookmarkWithScore
				.getCallingNumber());

		String callId = bookmarkWithScore.getCallId();
		validateCallId(callId);

		if (!userDetailsService.doesMsisdnExists(callingNo)) {
			LOGGER.error("MSISDN: {} doesn't exist with FLW service",
					callingNo);
			ParseDataHelper.raiseInvalidDataException("MSISDN",
					bookmarkWithScore.getCallingNumber());
		}

		String bookmarkId = bookmarkWithScore.getBookmark();
		Map<String, String> scoresByChapter = bookmarkWithScore
				.getScoresByChapter();

		Bookmark courseBookmark = courseBookmarkService
				.getBookmarkByMsisdn(callingNo);
		if (courseBookmark == null) {
			firstBookmark = true;
			courseBookmark = new Bookmark();
			courseBookmark.setProgress(new HashMap<String, Object>());
			courseBookmark.setExternalId(callingNo);
		}

		BookmarkHelper.validateAndPopulateBookmark(courseBookmark, bookmarkId,
				scoresByChapter);
		if (firstBookmark) {
			courseBookmarkService.createBookmark(courseBookmark);
		} else {
			courseBookmarkService.updateBookmark(courseBookmark);
		}

		if (bookmarkId.equals(MobileAcademyConstants.COURSE_COMPLETED)) {
			LOGGER.info("MSISDN: {} has completed the course",
					callingNo);
			// SEND SMS: To be done in sprint 1505
			resetTheBookmark(courseBookmark);
		}

		LOGGER.debug("saveBookmarkWithScore: Ended for MSISDN: {}",
				callingNo);
	}

	private void resetTheBookmark(Bookmark courseBookmark) {
		courseBookmark.setProgress(new HashMap<String, Object>());
		courseBookmarkService.updateBookmark(courseBookmark);
		LOGGER.debug("Bookmark has been reset for MSISDN: {}",
				courseBookmark.getExternalId());
	}

	private void validateCallId(String callId) throws DataValidationException {
		ParseDataHelper.validateAndParseLong(
				MobileAcademyConstants.REQUEST_PARAM_CALL_ID, callId, true);
	}

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
