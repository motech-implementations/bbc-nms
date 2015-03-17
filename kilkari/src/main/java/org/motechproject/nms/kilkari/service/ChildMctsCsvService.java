package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ChildMctsCsv;

/**
 * This interface is used for crud operations on ChildMctsCsv
 */

public interface ChildMctsCsvService {

    /**
     * deletes ChildMctsCsv from database
     *
     * @param record ChildMctsCsv type object
     */
    void delete(ChildMctsCsv record);

    /**
     * gets ChildMctsCsv object based by id
     *
     * @param id primary key of the record
     * @return ChildMctsCsv type object
     */
    ChildMctsCsv findRecordById(Long id);
    
}