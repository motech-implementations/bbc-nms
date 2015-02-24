package org.motechproject.nms.mobileacademy.service;

import java.util.List;

import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;

/**
 * Service Interface for operations on CourseProcessedContent Table
 */
public interface CourseProcessedContentService {

    /**
     * To get the list of all LLCs for which the course exists in the system
     * 
     * @return List of distinct language location codes
     */
    List<Integer> getListOfAllExistingLlcs();

    /**
     * This is used to get a courseProcessesContent Record based on content name
     * for a LLC and circle
     * 
     * @param circle String corresponding to a valid circle
     * @param LLC integer corresponding to a language location code
     * @param contentName contentName to uniquely identify different files of
     *            course
     * @return CPC Record for the content name
     */
    CourseProcessedContent getRecordforModification(String circle, int LLC,
            String contentName);

    /**
     * this is used to create a new courseProcessedContent Record
     * 
     * @param courseProcessedContent
     */
    void create(CourseProcessedContent courseProcessedContent);

    /**
     * This is used for deleting all the existing records in the system
     */
    void deleteAll();

    /**
     * This is used to update an existing CourseProcessedContent Object
     * 
     * @param courseProcessedContent new Object to be persisted in the system
     */
    void update(CourseProcessedContent courseProcessedContent);
}
