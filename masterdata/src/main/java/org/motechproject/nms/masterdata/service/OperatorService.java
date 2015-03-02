package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.Operator;

public interface OperatorService {

    /**
     * creates Operator in database
     *
     * @param record of Operator to create
     */
    void create(Operator record);

    /**
     * updates Operator in database
     *
     * @param record of Operator to update
     */
    void update(Operator record);

    /**
     * deletes Operator from database
     *
     * @param record Operator from database
     */
    void delete(Operator record);

    /**
     * get Operator record for given Operator Census code
     *
     * @param operatorCode Operator Census Code
     * @return Operator object corresponding to the census code
     */
    Operator getRecordByCode(String operatorCode);

    Operator findById(Long id);
}
