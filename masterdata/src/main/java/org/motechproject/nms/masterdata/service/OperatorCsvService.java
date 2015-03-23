package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvOperator;

/**
 * This interface is used for crud operations on OperatorCsv
 */

public interface OperatorCsvService {

    /**
     * gets OperatorCsv object based by id
     *
     * @param id primary key of the record
     * @return OperatorCsv type object
     */
    CsvOperator getRecord(Long id);

    /**
     * deletes OperatorCsv from database
     *
     * @param record OperatorCsv type object
     */
    void delete(CsvOperator record);

}
