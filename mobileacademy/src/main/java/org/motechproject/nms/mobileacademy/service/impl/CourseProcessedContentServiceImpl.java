package org.motechproject.nms.mobileacademy.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;
import org.motechproject.nms.mobileacademy.repository.CourseProcessedContentCustomQuery;
import org.motechproject.nms.mobileacademy.repository.CourseProcessedContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nitin on 2/17/15.
 */
@Service("CourseProcessedContentService")
public class CourseProcessedContentServiceImpl implements
        CourseProcessedContentService {

    @Autowired
    private CourseProcessedContentDataService courseProcessedContentDataService;

    @Override
    public void create(CourseProcessedContent courseProcessedContent) {
        courseProcessedContentDataService.create(courseProcessedContent);
    }

    @Override
    public void deleteAll() {
        courseProcessedContentDataService.deleteAll();
    }

    @Override
    public void update(CourseProcessedContent courseProcessedContent) {
        courseProcessedContentDataService.update(courseProcessedContent);
    }

    @Override
    public boolean doesLLCExistsInCPC(int languageLocationCode) {
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(languageLocationCode);
        if (CollectionUtils.isNotEmpty(courseProcessedContents)) {
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> getListOfAllExistingLLcs() {
        List<Integer> llcIdsList = courseProcessedContentDataService
                .executeQuery(new CourseProcessedContentCustomQuery().new LlcListQueryExecutionImpl());
        return llcIdsList;
    }

    @Override
    public CourseProcessedContent getRecordforModification(String circle,
            int languageLocationCode, String contentName) {
        return courseProcessedContentDataService.findByCircleLlcContentName(
                circle, languageLocationCode, contentName);
    }

    @Override
    public void deleteRecordsByLLC(int languageLocationCode) {
        courseProcessedContentDataService
                .executeQuery(new CourseProcessedContentCustomQuery().new DeleteProcessedContentQueryExecutionImpl(
                        languageLocationCode));

    }

}
