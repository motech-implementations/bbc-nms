package org.motechproject.nms.mobileacademy.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.nms.mobileacademy.domain.CourseRawContent;
import org.motechproject.nms.mobileacademy.repository.CourseRawContentDataService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by nitin on 2/23/15.
 */
public class Helper {

    @Autowired
    private static CourseRawContentDataService courseRawContentDataService;

    // Remove the objects from CRC and Map both;
    public static void deleteCourseRawContentsByList(
            List<CourseRawContent> courseRawContents) {
        if (CollectionUtils.isNotEmpty(courseRawContents)) {
            for (CourseRawContent courseRawContent : courseRawContents) {
                courseRawContentDataService.delete(courseRawContent);
            }
        }
    }
}
