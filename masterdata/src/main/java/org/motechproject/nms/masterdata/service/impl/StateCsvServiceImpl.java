package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.StateCsv;
import org.motechproject.nms.masterdata.repository.StateCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.StateCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 12/3/15.
 */
@Service("stateCsvService")
public class StateCsvServiceImpl implements StateCsvService {

    private StateCsvRecordsDataService stateCsvRecordsDataService;

    @Autowired
    public StateCsvServiceImpl(StateCsvRecordsDataService stateCsvRecordsDataService) {
        this.stateCsvRecordsDataService = stateCsvRecordsDataService;
    }

    /**
     * delete StateCsv type object
     *
     * @param record of the State
     */
    @Override
    public void delete(StateCsv record) {
        stateCsvRecordsDataService.delete(record);
    }

    /**
     * create StateCsv type object
     *
     * @param record of the StateCsv
     */
    @Override
    public StateCsv create(StateCsv record) {
        return stateCsvRecordsDataService.create(record);
    }

    @Override
    public StateCsv findById(Long id) {
        return stateCsvRecordsDataService.findById(id);
    }
}
