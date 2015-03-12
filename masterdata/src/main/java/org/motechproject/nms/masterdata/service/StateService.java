package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.State;

/**
 * This interface is used for crud operations on State
 */
public interface StateService {

    /**
     * create State type object
     *
     * @param record of the State
     */
    State create(State record);

    /**
     * update Circle type object
     *
     * @param record of the State
     */
    void update(State record);

    /**
     * delete State type object
     *
     * @param record of the State
     */
    void delete(State record);

    /**
     * delete All State type object
     */
    void deleteAll();


    State findRecordByStateCode(Long stateCode);

    State findById(Long id);
}
