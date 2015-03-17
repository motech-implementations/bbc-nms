package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 12/3/15.
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

    @Override
    public State findRecordByStateCode(Long stateCode) {
        return stateRecordsDataService.findRecordByStateCode(stateCode);
    }

    @Override
    public State findById(Long stateId) {
        return stateRecordsDataService.findById(stateId);
    }
}
