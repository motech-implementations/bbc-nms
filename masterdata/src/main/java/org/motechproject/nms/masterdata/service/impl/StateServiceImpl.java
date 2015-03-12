package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on State
 */
@Service("stateService")
public class StateServiceImpl implements StateService {

    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    public StateServiceImpl(StateRecordsDataService stateRecordsDataService) {
        this.stateRecordsDataService = stateRecordsDataService;
    }

    /**
     * create State type object
     *
     * @param record of the State
     */
    @Override
    public State create(State record) {
        return stateRecordsDataService.create(record);
    }

    /**
     * update Circle type object
     *
     * @param record of the State
     */
    @Override
    public void update(State record) {
        stateRecordsDataService.update(record);
    }

    /**
     * delete State type object
     *
     * @param record of the State
     */
    @Override
    public void delete(State record) {
        stateRecordsDataService.delete(record);
    }

    /**
     * delete All State type object
     */
    @Override
    public void deleteAll() {
        stateRecordsDataService.deleteAll();
    }

    /**
     * Gets the State Details by State Code
     * @param stateCode
     * @return State
     */
    @Override
    public State findRecordByStateCode(Long stateCode) {
        return stateRecordsDataService.findRecordByStateCode(stateCode);
    }

    /**
     * Gets the State details by Id
     * @param stateId
     * @return State
     */
    @Override
    public State findById(Long stateId) {
        return stateRecordsDataService.findById(stateId);
    }
}
