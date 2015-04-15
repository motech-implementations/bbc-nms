package org.motechproject.nms.mobileacademy.service;

import org.motechproject.mtraining.domain.Bookmark;

public interface CourseBookmarkService {

    public Bookmark getBookmarkByMsisdn(String callingNo);

    public Bookmark updateBookmark(Bookmark bookmark);

    public void createBookmark(Bookmark bookmark);
}
