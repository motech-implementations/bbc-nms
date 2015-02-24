package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.motechproject.nms.mobileacademy.service.CourseRawContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nitin on 2/9/15.
 */

@Service("CourseRawContentService")
public class CourseRawContentServiceImpl implements CourseRawContentService {

    @Autowired
    private CourseRawContentDataService courseRawContentDataService;

    @Override
    public void delete(CourseRawContent courseRawContent) {
        courseRawContentDataService.delete(courseRawContent);
    }

    @Override
    public void deleteAll() {
        courseRawContentDataService.deleteAll();
    }
}
