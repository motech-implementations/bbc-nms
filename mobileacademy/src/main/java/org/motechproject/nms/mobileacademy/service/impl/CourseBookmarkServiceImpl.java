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
import org.motechproject.nms.mobileacademy.service.ConfigurationService;
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

    @Autowired
    private ConfigurationService configurationService;

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
    public void deleteMtrainingBookmark(String callingNo) {
        bookmarkService.deleteAllBookmarksForUser(callingNo);
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
            LOGGER.debug("FLW with MSISDN: {} has completed the course",
                    callingNo);

            calculateScoreAndProcessForSms(mtrainingBookmark);

            resetTheBookmark(mtrainingBookmark);
        }
    }

    /*
     * This method calculates the score on the basis of mTraining bookmark and
     * based on the minimum qualifying score configured in the system, it
     * processes for SMS.
     */
    private void calculateScoreAndProcessForSms(Bookmark mtrainingBookmark) {
        String callingNo = mtrainingBookmark.getExternalId();
        int totalScore = getTotalScoreByMtrainingBookmark(mtrainingBookmark);
        int minPassingScore = configurationService.getConfiguration()
                .getCourseQualifyingScore();

        LOGGER.info("FLW with MSISDN: {} has secured total score: {}",
                callingNo, totalScore);

        // SEND SMS: dummy method for sending the SMS. It will be replaced
        // by actual method in sprint 1505
        if (totalScore >= minPassingScore) {
            sendSms(callingNo, totalScore);
            LOGGER.info(
                    "FLW with calling no. {} has passed the course successfully with total score: {}",
                    callingNo, totalScore);
        } else {
            LOGGER.info("FLW with MSISDN: {} has failed in the course",
                    callingNo);
        }
    }

    /*
     * This is a dummy method for sending the SMS. the actual implementation
     * will be done in sprint 1505
     */
    private void sendSms(String callingNo, int totalScore) {
        LOGGER.info(
                "SMS sent for successful course completion of FLW with MSISDN: {}",
                callingNo);
    }

    /*
     * This method calculates a FLW's score in the complete course through the
     * mTraining bookmark. MTraining bookmark of particular FLW must be passed
     * as a input parameter to this function.
     */
    private int getTotalScoreByMtrainingBookmark(Bookmark mtrainingBookmark) {
        int totalScore = 0;
        if (mtrainingBookmark != null) {
            Map<String, Object> progressMap = mtrainingBookmark.getProgress();
            for (Integer chapterNo = 1; chapterNo <= MobileAcademyConstants.NUM_OF_CHAPTERS; chapterNo++) {
                if (progressMap.containsKey(chapterNo.toString())) {
                    totalScore += (Integer) progressMap.get(chapterNo
                            .toString());
                }
            }
        }
        LOGGER.debug("Total score of FLW with MSISDN {} is {}",
                mtrainingBookmark.getExternalId(), totalScore);
        return totalScore;
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

}
