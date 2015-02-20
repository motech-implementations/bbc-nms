package org.motechproject.nms.mobileacademy.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobileacademy.domain.CourseRawContent;

/**
 * Interface for repository that persists simple records and allows CRUD on
 * CourseRawContent table. MotechDataService base class will provide the
 * implementation of this class as well as methods for adding, deleting, saving
 * and finding all instances. In this class we define and custom lookups we may
 * need.
 */
public interface CourseRawContentDataService extends
        MotechDataService<CourseRawContent> {

    /**
     * find course raw content records by language Location Code and operation
     * 
     * @param languageLocationCode language Location Code identifier.
     * @param operation operation i.e ADD/MOD/DEL
     * @return list of CourseRawContent records.
     */
    @Lookup
    List<CourseRawContent> findContentByLlcAndOperation(
            @LookupField(name = "languageLocationCode") String languageLocationCode,
            @LookupField(name = "operation") String operation);

}