package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentCustomQuery;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseRawContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Created by nitin on 2/9/15.
 */

@Service("CourseRawContentService")
public class CourseRawContentServiceImpl implements CourseRawContentService {

    @Autowired
    private CourseRawContentDataService courseRawContentDataService;

    /**
     * find course raw content distinct LLC identifiers
     *
     * @param operation operation identifier i.e ADD/MOD/DEL
     * @return list of string type that contains distinct LLC identifiers.
     */
    public List<String> findCourseRawContentLlcIds(String operation) {
        return courseRawContentDataService
                .executeQuery(new CourseRawContentCustomQuery().new LlcListQueryExecutionImpl(
                        operation));
    }

    @Override
    public List<CourseRawContent> findContentByLlcAndOperation(String languageLocationCode, String operation) {
        return courseRawContentDataService.findContentByLlcAndOperation(languageLocationCode, operation);
    }

    @Override
    public void delete(CourseRawContent courseRawContent) {
        courseRawContentDataService.delete(courseRawContent);
    }

    @Override
    public void deleteAll() {
        courseRawContentDataService.deleteAll();
    }

    /**
     * delete course raw content records by LLc And Operation
     *
     * @param languageLocationCode LLC Identifier
     * @param operation operation identifier i.e ADD/MOD/DEL
     * @return number of records deleted
     */
    public Long deleteCourseRawContentByLLcAndOperation(
            String languageLocationCode, String operation) {
        return courseRawContentDataService
                .executeQuery(new CourseRawContentCustomQuery().new DeleteRawContentQueryExecutionImpl(
                        languageLocationCode, operation));
    }
}
