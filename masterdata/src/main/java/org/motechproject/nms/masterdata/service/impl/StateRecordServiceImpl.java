package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.StateCsv;
import org.motechproject.nms.masterdata.repository.StateCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.service.StateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abhishek on 26/1/15.
 */
@Service("stateRecordService")
public class StateRecordServiceImpl implements StateRecordService {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private StateCsvRecordsDataService stateCsvRecordsDataService;

    @Override
    public void add(State record) {

        stateRecordsDataService.create(record);
    }

    public List<StateCsv> retrieveAllRecords() {

        return stateCsvRecordsDataService.retrieveAll();

    }
}
