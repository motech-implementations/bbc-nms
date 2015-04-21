package org.motechproject.nms.mobileacademy.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This is a helper class for implementation of CourseBookmark Service
 */
public class BookmarkHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookmarkHelper.class);

	/**
	 * This function returns bookmark JSON to be sent to IVR on getBookmark
	 * request on the basis of MTraining bookmark
	 * 
	 * @param bookmark
	 *            : mTraining bookmark
	 * @return : JSON response to be returned to IVR
	 */
	public static String getBookmarkJson(Bookmark bookmark) {
		JsonObject bookmarkJson = new JsonObject();
		bookmarkJson.addProperty(
				MobileAcademyConstants.BOOKMARK,
				(String) bookmark.getProgress().get(
						MobileAcademyConstants.BOOKMARK_ID));
		JsonElement scoresByChapter = getScoresJson(bookmark);

		/*
		 * If there are any scores for user in the system, then only put the
		 * scoresByChapterNode
		 */
		if (scoresByChapter != null) {
			bookmarkJson.add(MobileAcademyConstants.SCORE_BY_CHAPTER_NODE,
					scoresByChapter);
		}
		return bookmarkJson.toString();
	}

	/*
	 * This function returns scoreByChapter node's JSON content to be put in
	 * Bookmark JSON. If there is no any score of the user in the system, in
	 * that case it returns null
	 * 
	 * @param bookmark : mTraining bookmark
	 * 
	 * @return scoreByChapter node's JSON content
	 */
	private static JsonElement getScoresJson(Bookmark bookmark) {

		JsonObject scoresJson = null;
		Map<String, Object> progressMap = bookmark.getProgress();

		/*
		 * If there are any scores for user in the system, then only put the
		 * scoresByChapterNode otherwise scoresJson will not be initialized
		 */
		for (Integer chapterNo = 1; chapterNo <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterNo++) {
			if (progressMap.containsKey(chapterNo.toString())) {
				if (scoresJson == null) {
					scoresJson = new JsonObject();
				}
				scoresJson.addProperty(chapterNo.toString(),
						(Integer) progressMap.get(chapterNo.toString()));
			}
		}
		return scoresJson;
	}

	/**
	 * This function validates the input request of IVR to save the bookmark in
	 * the system using MTraining. In case of any errors, generates Data
	 * validation exception.
	 * 
	 * @param courseBookmark
	 *            : MTraining bookmark to be populated
	 * @param bookmarkID
	 *            : bookmark node id as mentioned in course structure
	 * @param scoresByChapter
	 *            : user's score in different chapters
	 * @throws DataValidationException
	 */
	public static void validateAndPopulateBookmark(Bookmark courseBookmark,
			String bookmarkID, Map<String, String> scoresByChapter)
			throws DataValidationException {

		/* If there is no bookmark ID in input request */
		if (bookmarkID != null) {
			/* In case of no value in bookmark ID */
			if (StringUtils.isBlank(bookmarkID)) {
				LOGGER.warn(
						"There is no bookmark ID in Save bookmark request for MSISDN: {}",
						courseBookmark.getExternalId());
				ParseDataHelper.raiseMissingDataException(
						MobileAcademyConstants.BOOKMARK_ID, bookmarkID);
			}
			courseBookmark.getProgress().put(
					MobileAcademyConstants.BOOKMARK_ID, bookmarkID);
		}

		/* If there are scores in input request */
		if (scoresByChapter != null) {
			validateAndPopulateScoresInBookmark(courseBookmark, scoresByChapter);
		}
	}

	/*
	 * This function validates the scores received in input request and in case
	 * of any scores received, it saves them in progress map of MTraining
	 * bookmark
	 */
	private static void validateAndPopulateScoresInBookmark(
			Bookmark courseBookmark, Map<String, String> scoresByChapter)
			throws DataValidationException {

		Map<String, Object> progressMap = courseBookmark.getProgress();

		/* List to keep track if there are scores only for chapters 1-11 */
		List<String> chapterKeys = new ArrayList<String>(scoresByChapter.keySet());

		/* If the scoresByChapter field contains some score */
		    if (CollectionUtils.isNotEmpty(chapterKeys)) {

                        /*
			 * Traverse to check if score for a particular chapter has been
			 * received
			 */
			for (Integer chapterNo = 1; chapterNo <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterNo++) {

				if (scoresByChapter.containsKey(chapterNo.toString())) {
					chapterKeys.remove(chapterNo.toString());
					Integer scoreInChapter = Integer.parseInt(scoresByChapter
							.get(chapterNo.toString()));

					/* If the score is not in range of 0-4, its a bad request */
					if (!RecordsProcessHelper.verifyRange(scoreInChapter, 0,
							MobileAcademyConstants.MAX_SCORE)) {
						LOGGER.debug(
								"scores out of range in Save bookmark request for MSISDN: {}",
								courseBookmark.getExternalId());
						ParseDataHelper.raiseInvalidDataException(
								MobileAcademyConstants.SCORE_BY_CHAPTER_NODE,
								scoreInChapter.toString());
					}
					progressMap.put(chapterNo.toString(), scoreInChapter);
				}
			}

			/*
			 * If scoresByChapter field contains score for other than chapter
			 * 1-11, it's a bad request.
			 */
			if (CollectionUtils.isNotEmpty(chapterKeys)) {
				LOGGER.debug(
						"Invalid Chapter Indices in ScoresByChapter field of Save bookmark request for MSISDN: {}",
						courseBookmark.getExternalId());
				ParseDataHelper.raiseInvalidDataException(
						MobileAcademyConstants.SCORE_BY_CHAPTER_NODE,
						chapterKeys.get(0));
			}
		}
	}
}
