package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.domain.CourseRawContent;

/**
 * Created by nitin on 2/9/15.
 */
public interface CourseRawContentService {

    void delete(CourseRawContent courseRawContent);

    void deleteAll();

}
