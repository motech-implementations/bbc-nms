package org.motechproject.nms.mobileacademy.helper;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class BookmarkHelper {

	public static String getBookmarkJson(Bookmark bookmark) {
		JsonObject bookmarkJson = new JsonObject();
		bookmarkJson.addProperty(
				MobileAcademyConstants.BOOKMARK,
				(String) bookmark.getProgress().get(
						MobileAcademyConstants.BOOKMARK_ID));
		bookmarkJson.add("scoresByChapter", getScoresJson(bookmark));
		return bookmarkJson.toString();
	}

	private static JsonElement getScoresJson(Bookmark bookmark) {
		JsonObject scoresJson = new JsonObject();
		Map<String, Object> progressMap = bookmark.getProgress();
		String[] chapterKeys = (String[]) progressMap.keySet().toArray();
		Arrays.sort(chapterKeys);
		for (String chapterNo : chapterKeys) {
			if (chapterNo.equals(MobileAcademyConstants.BOOKMARK_ID)) {
				continue;
			}
			scoresJson.addProperty(chapterNo,
					(Integer) progressMap.get(chapterNo));
		}
		return scoresJson;
	}
	
	public static void validateAndPopulateBookmark(Bookmark courseBookmark,
			String bookmarkID, Map<String, String> scoresByChapter)
			throws DataValidationException {

		if (StringUtils.isBlank(bookmarkID)) {
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

		String[] chapterNos = (String[]) scoresByChapter.keySet().toArray();
		Arrays.sort(chapterNos);
		for (String chapterNo : chapterNos) {
			try {
				Integer.parseInt(chapterNo);
				String scoreInChapter = scoresByChapter.get(chapterNo);
				progressMap.put(chapterNo, Integer.parseInt(scoreInChapter));
			} catch (NumberFormatException e) {
				ParseDataHelper.raiseInvalidDataException("ScoresByChapter",
						scoresByChapter.toString());
			}
		}
	}
}
