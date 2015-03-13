package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CircleCsv;

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
    CircleCsv getRecord(Long id);

    /**
     * deletes CircleCsv from database
     *
     * @param record CircleCsv type object
     */
    void delete(CircleCsv record);

}
