package org.motechproject.nms.mobileacademy.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.mtraining.domain.Bookmark;
import org.motechproject.mtraining.service.BookmarkService;
import org.motechproject.nms.mobileacademy.service.CourseBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of CourseBookmarkService
 */
@Service("CourseBookmarkService")
public class CourseBookmarkServiceImpl implements CourseBookmarkService {

    @Autowired
    private BookmarkService bookmarkService;

    @Override
    public Bookmark getBookmarkByMsisdn(String callingNo) {
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
}
