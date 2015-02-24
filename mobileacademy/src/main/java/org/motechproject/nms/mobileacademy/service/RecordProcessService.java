package org.motechproject.nms.mobileacademy.service;

import java.util.List;

import org.motechproject.nms.mobileacademy.domain.CourseRawContent;

/**
 * Created by nitin on 2/9/15.
 */
public interface RecordProcessService {

    public String processRawRecords(List<CourseRawContent> listOfRecords);
}
