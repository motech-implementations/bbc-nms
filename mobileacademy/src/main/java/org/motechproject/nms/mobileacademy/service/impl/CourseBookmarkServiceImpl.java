package org.motechproject.nms.mobileacademy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.mtraining.service.BookmarkService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.helper.BookmarkHelper;
import org.motechproject.nms.mobileacademy.service.CourseBookmarkService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of CourseBookmarkService
 */
@Service("CourseBookmarkService")
public class CourseBookmarkServiceImpl implements CourseBookmarkService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CourseBookmarkServiceImpl.class);

	@Autowired
	private BookmarkService bookmarkService;

	@Override
	public Bookmark getMtrainingBookmarkByMsisdn(String callingNo) {
		Bookmark mtrainingBookmark = null;
		List<Bookmark> bookmarks = bookmarkService
				.getAllBookmarksForUser(callingNo);
		if (CollectionUtils.isNotEmpty(bookmarks)) {
			mtrainingBookmark = bookmarks.get(0);
		}
		return mtrainingBookmark;
	}

	@Override
	public Bookmark updateMtrainingBookmark(Bookmark bookmark) {
		return bookmarkService.updateBookmark(bookmark);
	}

	@Override
	public void createMtrainingBookmark(Bookmark bookmark) {
		bookmarkService.createBookmark(bookmark);
	}

	@Override
	public String getBookmarkWithScore(String callingNo) {
		Bookmark mtrainingBookmark = getMtrainingBookmarkByMsisdn(callingNo);
		LOGGER.trace("Received bookmark from MTraining for calling No {} : {}",
				callingNo, mtrainingBookmark);
		String bookmarkToReturn = "";

		/*
		 * If there is no bookmark for user or the bookmark for user has been
		 * reset, in which case, the bookmarkId will be blank; then return empty
		 * JSON.
		 */
		if ((mtrainingBookmark == null)
				|| (StringUtils.isBlank((String) mtrainingBookmark
						.getProgress().get(MobileAcademyConstants.BOOKMARK_ID)))) {
			LOGGER.debug("There is no bookmark in the system for MSISDN: {}",
					callingNo);
			bookmarkToReturn = MobileAcademyConstants.EMPTY_JSON;
		} else {
			bookmarkToReturn = BookmarkHelper
					.getBookmarkJson(mtrainingBookmark);
		}
		LOGGER.trace("Sending bookmark JSON for calling No {} : {}", callingNo,
				bookmarkToReturn);
		return bookmarkToReturn;
	}

	@Override
	public void saveBookmarkWithScore(String bookmarkId,
			Map<String, String> scoresByChapter, String callingNo)
			throws DataValidationException {

		Bookmark mtrainingBookmark = getMtrainingBookmarkByMsisdn(callingNo);

		/*
		 * In case of first time bookmark, initialize and put the calling no in
		 * external ID field
		 */
		if (mtrainingBookmark == null) {
			mtrainingBookmark = new Bookmark();
			mtrainingBookmark.setProgress(new HashMap<String, Object>());
			mtrainingBookmark.setExternalId(callingNo);
		}

		BookmarkHelper.validateAndPopulateBookmark(mtrainingBookmark,
				bookmarkId, scoresByChapter);

		updateMtrainingBookmark(mtrainingBookmark);

		/*
		 * If user has completed the course successfully, compute the score,
		 * process for SMS and reset the user's bookmark for next call
		 */
		if (bookmarkId != null
				&& bookmarkId
						.equalsIgnoreCase(MobileAcademyConstants.COURSE_COMPLETED)) {
			LOGGER.info("FLW with MSISDN: {} has completed the course", callingNo);
			// SEND SMS: To be done in sprint 1505
			resetTheBookmark(mtrainingBookmark);
		}
	}

	/*
	 * This is used for resetting the bookmark of a MSISDN. It resets the
	 * progress map
	 */
	private void resetTheBookmark(Bookmark courseBookmark) {
		courseBookmark.setProgress(new HashMap<String, Object>());
		updateMtrainingBookmark(courseBookmark);
		LOGGER.debug("Bookmark has been reset for MSISDN: {}",
				courseBookmark.getExternalId());
	}

	@Override
	public void deleteMtrainingBookmark(String callingNo) {
		bookmarkService.deleteAllBookmarksForUser(callingNo);
	}
}
