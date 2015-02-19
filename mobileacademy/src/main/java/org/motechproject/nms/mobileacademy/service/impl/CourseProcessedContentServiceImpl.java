package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;
import org.motechproject.nms.mobileacademy.repository.CourseProcessedContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseProcessedContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nitin on 2/17/15.
 */
@Service("CourseProcessedContentService")
public class CourseProcessedContentServiceImpl implements CourseProcessedContentService{

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


}
