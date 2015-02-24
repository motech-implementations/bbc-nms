package org.motechproject.nms.mobileacademy.repository;

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

}