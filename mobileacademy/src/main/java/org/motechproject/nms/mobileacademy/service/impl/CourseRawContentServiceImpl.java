package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseRawContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This file contains the implementation for CourseRawContentService. It will be
 * used to operate on CourseRawContent Tables
 */
@Service("CourseRawContentService")
public class CourseRawContentServiceImpl implements CourseRawContentService {

    @Autowired
    private CourseRawContentDataService courseRawContentDataService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseRawContentService#delete
     * (org.motechproject.nms.mobileacademy.domain.CourseRawContent)
     * 
     * to delete the a CourseRawContent record
     */
    @Override
    public void delete(CourseRawContent courseRawContent) {
        courseRawContentDataService.delete(courseRawContent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseRawContentService#deleteAll
     * ()
     * 
     * to delete all the CourseRawContent records existing in the system
     */
    @Override
    public void deleteAll() {
        courseRawContentDataService.deleteAll();
    }
}
