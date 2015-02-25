package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.domain.CourseRawContent;

/**
 * Service Interface for operations on CourseRawContentService Table
 * 
 * @author YOGESH
 *
 */
public interface CourseRawContentService {

    /**
     * Used to delete a record from the CourseRawContent table
     * 
     * @param courseRawContent
     *            : Object to be deleted
     */
    void delete(CourseRawContent courseRawContent);

    /**
     * Used to delete all the existing CourseRawContent records in the system
     */
    void deleteAll();

}
