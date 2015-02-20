package org.motechproject.nms.mobileacademy.service;


import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;

/**
 * Created by nitin on 2/17/15.
 */
public interface CourseProcessedContentService {

    void create(CourseProcessedContent courseProcessedContent);

    void deleteAll();
}
