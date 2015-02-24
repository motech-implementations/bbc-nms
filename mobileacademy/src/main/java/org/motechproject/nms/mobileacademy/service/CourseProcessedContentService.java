package org.motechproject.nms.mobileacademy.service;

import java.util.List;

import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;

/**
 * Created by nitin on 2/17/15.
 */
public interface CourseProcessedContentService {

    // Check if course for a particular LLC exists in CPC or not
    public boolean doesLLCExistsInCPC(int LLC);

    // Provide list of distinct LLCs existing in CPC
    List<Integer> getListOfAllExistingLLcs();

    // Provide the record having combination of below three in CPC
    CourseProcessedContent getRecordforModification(String circle, int lLC,
            String contentName);

    // Already been implemented
    void create(CourseProcessedContent courseProcessedContent);

    void deleteAll();

    void update(CourseProcessedContent courseProcessedContent);

    public void deleteRecordsByLLC(int languageLocationCode);

}
