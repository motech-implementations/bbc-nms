package org.motechproject.nms.mobileacademy.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;

/**
 * Interface for repository that persists simple records and allows CRUD on
 * Chapter content table and other tables related to chapter.MotechDataService
 * base class will provide the implementation of this class as well as methods
 * for adding, deleting, saving and finding all instances. In this class we
 * define and custom lookups we may need.
 */
public interface ChapterContentDataService extends
        MotechDataService<ChapterContent> {

    /**
     * find Chapter Content By Number
     * 
     * @param chapterNumber chapter number identifier i.e 1,2..,11
     * @return ChapterContent object
     */
    @Lookup
    ChapterContent findChapterContentByNumber(
            @LookupField(name = "chapterNumber") Integer chapterNumber);

}