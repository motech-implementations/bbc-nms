package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.MotherMctsCsv;

/**
 * This interface is used for crud operations on MotherMctsCsv
 */

public interface MotherMctsCsvService {

    /**
     * gets MotherMctsCsv object based by id
     *
     * @param id primary key of the record
     * @return MotherMctsCsv type object
     */
    MotherMctsCsv findRecordById(Long id);

    /**
     * deletes MotherMctsCsv from database
     *
     * @param record MotherMctsCsv type object
     */
    void delete(MotherMctsCsv record);

}
