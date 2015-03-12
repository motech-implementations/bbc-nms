package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.StateCsv;

/**
 * This interface is used for crud operations on StateCsv
 */
public interface StateCsvService {

    /**
     * delete StateCsv type object
     *
     * @param record of the StateCsv
     */
    void delete(StateCsv record);

    /**
     * create StateCsv type object
     *
     * @param record of the StateCsv
     */
    StateCsv create(StateCsv record);

    StateCsv findById(Long id);

}
