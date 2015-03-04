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
 * Service implementation for CourseProcessedContent Records
 */
@Service("CourseProcessedContentService")
public class CourseProcessedContentServiceImpl implements
        CourseProcessedContentService {

    @Autowired
    private CourseProcessedContentDataService courseProcessedContentDataService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseProcessedContentService
     * #
     * create(org.motechproject.nms.mobileacademy.domain.CourseProcessedContent)
     */
    @Override
    public void create(CourseProcessedContent courseProcessedContent) {
        courseProcessedContentDataService.create(courseProcessedContent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseProcessedContentService
     * #deleteAll()
     */
    @Override
    public void deleteAll() {
        courseProcessedContentDataService.deleteAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseProcessedContentService
     * #
     * update(org.motechproject.nms.mobileacademy.domain.CourseProcessedContent)
     */
    @Override
    public void update(CourseProcessedContent courseProcessedContent) {
        courseProcessedContentDataService.update(courseProcessedContent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseProcessedContentService
     * #doesLLCExistsInCPC(int)
     */
    @Override
    public boolean doesLlcExistsInCpc(int languageLocationCode) {
        List<CourseProcessedContent> courseProcessedContents = courseProcessedContentDataService
                .findContentByLlc(languageLocationCode);
        if (CollectionUtils.isNotEmpty(courseProcessedContents)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseProcessedContentService
     * #getListOfAllExistingLLcs()
     */
    @Override
    public List<Integer> getListOfAllExistingLlcs() {
        List<Integer> llcIdsList = courseProcessedContentDataService
                .executeQuery(new CourseProcessedContentCustomQuery().new LlcListQueryExecutionImpl());
        return llcIdsList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseProcessedContentService
     * #getRecordforModification(java.lang.String, int, java.lang.String)
     */
    @Override
    public CourseProcessedContent getRecordforModification(String circle,
            int languageLocationCode, String contentName) {
        return courseProcessedContentDataService.findByCircleLlcContentName(
                circle, languageLocationCode, contentName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseProcessedContentService
     * #deleteRecordsByLLC(int)
     */
    @Override
    public void deleteRecordsByLlc(int languageLocationCode) {
        courseProcessedContentDataService
                .executeQuery(new CourseProcessedContentCustomQuery().new DeleteProcessedContentQueryExecutionImpl(
                        languageLocationCode));

    }

}
