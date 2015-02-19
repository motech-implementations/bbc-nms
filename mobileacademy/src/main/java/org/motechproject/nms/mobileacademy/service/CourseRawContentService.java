package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.domain.CourseRawContent;

import java.util.List;

/**
 * Created by nitin on 2/9/15.
 */
public interface CourseRawContentService{

    Long deleteCourseRawContentByLLcAndOperation(String languageLocationCode, String operation);

    List<String> findCourseRawContentLlcIds(String operation);

    List<CourseRawContent> findContentByLlcAndOperation(String languageLocationCode, String operation);

    void delete(CourseRawContent courseRawContent);

    void deleteAll();

}
