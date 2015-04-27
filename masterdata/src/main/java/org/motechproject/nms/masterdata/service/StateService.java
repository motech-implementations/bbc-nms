package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;

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

    /**
     * Finds the State details by its Id
     *
     * @param stateCode
     * @return State
     */
    State findRecordByStateCode(Long stateCode);

    /**
     * Finds the State details by its Id
     *
     * @param id
     * @return State
     */
    State findById(Long id);

    /**
     * Get StateRecordsDataService object
     */
    public StateRecordsDataService getStateRecordsDataService();
}
