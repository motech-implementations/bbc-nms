package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.Operator;

public interface OperatorService {

    /**
     * creates Operator from database
     *
     * @param record Operator from database
     */
    void create(Operator record);

    /**
     * updates Operator from database
     *
     * @param record Operator from database
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
}
