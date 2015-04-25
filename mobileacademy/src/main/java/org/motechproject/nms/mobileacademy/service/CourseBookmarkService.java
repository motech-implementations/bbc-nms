package org.motechproject.nms.mobileacademy.service;

import java.util.Map;

import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.nms.util.helper.DataValidationException;

/**
 * CourseBookmarkService is used for Creating, updating and fetching the course
 * bookmarks of a user
 * 
 */
public interface CourseBookmarkService {

	/**
	 * This returns bookmark object stored in mTraining for the callingNo
	 * 
	 * @param callingNo
	 * @return Mtraining Bookmark Object
	 */
	public Bookmark getMtrainingBookmarkByMsisdn(String callingNo);

	/**
	 * This is used to update the bookmark in mTraining tables
	 * 
	 * @param bookmark
	 *            to be updated
	 * @return bookmark which is updated in system
	 */
	public Bookmark updateMtrainingBookmark(Bookmark bookmark);

	/**
	 * This is used to create the bookmark in mTraining tables
	 * 
	 * @param bookmark
	 *            to be created
	 */
	public void createMtrainingBookmark(Bookmark bookmark);

	/**
	 * To delete the bookmark of a user from MTraining tables
	 * 
	 * @param callingNo
	 *            : for which bookmark need to be deleted
	 */
	public void deleteMtrainingBookmark(String callingNo);

	/**
	 * @param callingNo
	 *            : for which bookmark need to be retrieved
	 * @return Bookmark in JSON format with bookmarkID and the scores of user in
	 *         different chapters
	 */
	public String getBookmarkWithScore(String callingNo);

	/**
	 * @param bookmarkId
	 *            : id of the node sent by IVR to be bookmarked
	 * @param scoresByChapter
	 *            : user's score in each chapter of the course
	 * @param callingNo
	 *            : 10 digit MSISDN of health worker
	 * @throws DataValidationException
	 *             : when there is any error in input request
	 */
	public void saveBookmarkWithScore(String bookmarkId,
			Map<String, String> scoresByChapter, String callingNo)
			throws DataValidationException;
}
