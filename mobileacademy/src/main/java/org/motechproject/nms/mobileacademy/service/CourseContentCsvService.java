package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;

/**
 * Service Interface for operations on CourseContentCsv Table
 */
public interface CourseContentCsvService {

    /**
     * Used to delete a record from the CourseContentCsv table
     * 
     * @param courseContentCsv : Object to be deleted
     */
    void delete(CourseContentCsv courseContentCsv);

    /**
     * Used to delete all the existing CourseContentCsv records in the system
     */
    void deleteAll();

}
