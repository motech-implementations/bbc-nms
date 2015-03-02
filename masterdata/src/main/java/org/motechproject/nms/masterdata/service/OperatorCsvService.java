package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.OperatorCsv;

public interface OperatorCsvService {

    /**
     * gets OperatorCsv object based by id
     *
     * @param id primary key of the record
     * @return OperatorCsv type object
     */
    OperatorCsv getRecord(Long id);

    /**
     * deletes OperatorCsv from database
     *
     * @param record OperatorCsv type object
     */
    void delete(OperatorCsv record);

}
