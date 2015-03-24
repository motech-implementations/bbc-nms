package org.motechproject.nms.mobileacademy.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;

/**
 * Interface for repository that persists simple records and allows CRUD on
 * CourseProcessedContent table. MotechDataService base class will provide the
 * implementation of this class as well as methods for adding, deleting, saving
 * and finding all instances. In this class we define and custom lookups we may
 * need.
 */
public interface CourseProcessedContentDataService extends
        MotechDataService<CourseProcessedContent> {

    /**
     * find Content By LLC
     * 
     * @param languageLocationCode LLC identifier
     * @return List<CourseProcessedContent>
     */
    @Lookup
    List<CourseProcessedContent> findContentByLlc(
            @LookupField(name = "languageLocationCode") Integer languageLocationCode);

    /**
     * find By Circle Llc and ContentName
     * 
     * @param circle circle identifier
     * @param languageLocationCode LLC identifier
     * @param contentName content name
     * @return
     */
    /* equalsIgnoreCase introduced as a fix for Bug ID:31 in bugzilla */
    @Lookup
    CourseProcessedContent findByCircleLlcContentName(
            @LookupField(name = "circle", customOperator = "equalsIgnoreCase()") String circle,
            @LookupField(name = "languageLocationCode") Integer languageLocationCode,
            @LookupField(name = "contentName", customOperator = "equalsIgnoreCase()") String contentName);
}
