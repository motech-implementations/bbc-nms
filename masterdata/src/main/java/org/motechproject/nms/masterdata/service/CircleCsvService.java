package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvCircle;

/**
 * This interface is used for crud operations on CircleCsv
 */

public interface CircleCsvService {

    /**
     * gets CircleCsv object based by id
     *
     * @param id primary key of the record
     * @return CircleCsv type object
     */
    CsvCircle getRecord(Long id);

    /**
     * deletes CircleCsv from database
     *
     * @param record CircleCsv type object
     */
    void delete(CsvCircle record);

}
