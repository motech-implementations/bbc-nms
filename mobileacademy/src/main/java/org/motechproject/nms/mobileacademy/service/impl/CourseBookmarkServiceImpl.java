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
		List<Bookmark> bookmarks = bookmarkService
				.getAllBookmarksForUser(callingNo);
		if (CollectionUtils.isEmpty(bookmarks)) {
			return null;
		} else {
			return bookmarks.get(0);
		}
	}

	@Override
	public Bookmark updateBookmark(Bookmark bookmark) {
		return bookmarkService.updateBookmark(bookmark);
	}

	@Override
	public void createBookmark(Bookmark bookmark) {
		bookmarkService.createBookmark(bookmark);
	}

	@Override
	public String getBookmarkWithScore(String callingNo) {
		Bookmark bookmark = getMtrainingBookmarkByMsisdn(callingNo);
		String bookmarkToReturn = "";

		/*
		 * If there is no bookmark for user or the bookmark for user has been
		 * reset, in which case, the bookmarkId will be blank; then return empty
		 * JSON.
		 */
		if ((bookmark == null)
				|| (StringUtils.isBlank((String) bookmark.getProgress().get(
						MobileAcademyConstants.BOOKMARK_ID)))) {
			LOGGER.debug("There is no bookmark in the system for MSISDN: {}",
					callingNo);
			bookmarkToReturn = MobileAcademyConstants.EMPTY_JSON;
		} else {
			bookmarkToReturn = BookmarkHelper.getBookmarkJson(bookmark);
		}
		return bookmarkToReturn;
	}

	@Override
	public void saveBookmarkWithScore(String bookmarkId,
			Map<String, String> scoresByChapter, String callingNo)
			throws DataValidationException {

		Bookmark courseBookmark = getMtrainingBookmarkByMsisdn(callingNo);
		
		/*
		 * In case of first time bookmark, initialize and put the calling no in
		 * external ID field
		 */
		if (courseBookmark == null) {
			courseBookmark = new Bookmark();
			courseBookmark.setProgress(new HashMap<String, Object>());
			courseBookmark.setExternalId(callingNo);
		}

		BookmarkHelper.validateAndPopulateBookmark(courseBookmark, bookmarkId,
				scoresByChapter);

		updateBookmark(courseBookmark);

		/*
		 * If user has completed the course successfully, compute the score,
		 * process for SMS and reset the user's bookmark for next call
		 */
		if (bookmarkId!=null && bookmarkId
				.equalsIgnoreCase(MobileAcademyConstants.COURSE_COMPLETED)) {
			LOGGER.info("MSISDN: {} has completed the course", callingNo);
			// SEND SMS: To be done in sprint 1505
			resetTheBookmark(courseBookmark);
		}
	}

	/*
	 * This is used for resetting the bookmark of a MSISDN. It resets the
	 * progress map
	 */
	private void resetTheBookmark(Bookmark courseBookmark) {
		courseBookmark.setProgress(new HashMap<String, Object>());
		updateBookmark(courseBookmark);
		LOGGER.debug("Bookmark has been reset for MSISDN: {}",
				courseBookmark.getExternalId());
	}

	@Override
	public void deleteBookmark(String callingNo) {
		bookmarkService.deleteAllBookmarksForUser(callingNo);
	}
}
