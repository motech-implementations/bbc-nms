package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvState;

/**
 * This interface is used for crud operations on StateCsv
 */
public interface StateCsvService {

    /**
     * delete StateCsv type object
     *
     * @param record of the StateCsv
     */
    void delete(CsvState record);

    /**
     * create StateCsv type object
     *
     * @param record of the StateCsv
     */
    CsvState create(CsvState record);

    /**
     * Finds the State Csv details by its Id
     *
     * @param id
     * @return StateCsv
     */
    CsvState findById(Long id);

}
