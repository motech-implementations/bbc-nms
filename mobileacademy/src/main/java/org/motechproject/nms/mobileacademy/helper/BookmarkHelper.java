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

public class BookmarkHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookmarkHelper.class);

	public static String getBookmarkJson(Bookmark bookmark) {
		JsonObject bookmarkJson = new JsonObject();
		bookmarkJson.addProperty(
				MobileAcademyConstants.BOOKMARK,
				(String) bookmark.getProgress().get(
						MobileAcademyConstants.BOOKMARK_ID));
		JsonElement scoresByChapter = getScoresJson(bookmark);
		if(scoresByChapter != null) {
			bookmarkJson.add("scoresByChapter", scoresByChapter);
		}		
		return bookmarkJson.toString();
	}

	private static JsonElement getScoresJson(Bookmark bookmark) {
		
		boolean doesAnyScoreExists = false;
		JsonObject scoresJson = new JsonObject();
		Map<String, Object> progressMap = bookmark.getProgress();

		for (Integer chapterNo = 1; chapterNo <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterNo++) {
			if (progressMap.containsKey(chapterNo.toString())) {
				doesAnyScoreExists = true;
				scoresJson.addProperty(chapterNo.toString(),
						(Integer) progressMap.get(chapterNo));
			}
		}
		if(!doesAnyScoreExists) {
			return null;
		}
		return scoresJson;
	}

	public static void validateAndPopulateBookmark(Bookmark courseBookmark,
			String bookmarkID, Map<String, String> scoresByChapter)
			throws DataValidationException {

		if (StringUtils.isBlank(bookmarkID)) {
			LOGGER.debug(
					"There is no bookmark ID in Save bookmark request for MSISDN: {}",
					courseBookmark.getExternalId());
			ParseDataHelper.raiseMissingDataException(
					MobileAcademyConstants.BOOKMARK_ID, "");
		}
		courseBookmark.getProgress().put(MobileAcademyConstants.BOOKMARK_ID,
				bookmarkID);

		if (scoresByChapter != null) {
			validateScoresPutInBookmark(courseBookmark, scoresByChapter);
		}
	}

	private static void validateScoresPutInBookmark(Bookmark courseBookmark,
			Map<String, String> scoresByChapter) throws DataValidationException {

		Map<String, Object> progressMap = courseBookmark.getProgress();
		List<String> keys = new ArrayList<String>(scoresByChapter.keySet());
		if(CollectionUtils.isEmpty(keys)) {
			return;
		}

		for (Integer chapterNo = 1; chapterNo <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterNo++) {
			try {
				if (scoresByChapter.containsKey(chapterNo.toString())) {
					keys.remove(chapterNo.toString());
					Integer scoreInChapter = Integer.parseInt(scoresByChapter
							.get(chapterNo.toString()));
					if (!RecordsProcessHelper.verifyRange(scoreInChapter, 0,
							MobileAcademyConstants.NUM_OF_SCORES)) {
						LOGGER.debug(
								"scores out of range in Save bookmark request for MSISDN: {}",
								courseBookmark.getExternalId());
						ParseDataHelper.raiseInvalidDataException(
								"ScoresByChapter", scoreInChapter.toString());
					}
					progressMap.put(chapterNo.toString(), scoreInChapter);
				}
			} catch (NumberFormatException e) {
				LOGGER.debug(
						"Unable to parse the scores in Save bookmark request for MSISDN: {}",
						courseBookmark.getExternalId());
				ParseDataHelper.raiseInvalidDataException("ScoresByChapter",
						scoresByChapter.toString());
			}
		}
		
		if(CollectionUtils.isNotEmpty(keys)) {
			LOGGER.debug(
					"Invalid Chapter Indices in ScoresByChapter field of Save bookmark request for MSISDN: {}",
					courseBookmark.getExternalId());
			ParseDataHelper.raiseInvalidDataException(
					"ScoresByChapter", keys.get(0));
		}
	}
}
