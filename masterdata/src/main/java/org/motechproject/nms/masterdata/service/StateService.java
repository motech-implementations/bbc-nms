package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.State;

/**
 * Created by abhishek on 12/3/15.
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
