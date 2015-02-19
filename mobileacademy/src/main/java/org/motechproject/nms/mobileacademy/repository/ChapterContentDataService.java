package org.motechproject.nms.mobileacademy.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;

/**
 * Interface for repository that persists simple records and allows CRUD on
 * Chapter content table that contains chapter related meta data.
 */

public interface ChapterContentDataService extends
        MotechDataService<ChapterContent> {

    @Lookup
    ChapterContent findChapterContentByNumber(
            @LookupField(name = "chapterNumber") Integer chapterNumber);

}
