package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.CsvState;
import org.motechproject.nms.masterdata.repository.StateCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.StateCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on StateCsv
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
    public void delete(CsvState record) {
        stateCsvRecordsDataService.delete(record);
    }

    /**
     * create StateCsv type object
     *
     * @param record of the StateCsv
     */
    @Override
    public CsvState create(CsvState record) {
        return stateCsvRecordsDataService.create(record);
    }

    /**
     * Gets the StateCsv Details by Id
     *
     * @param id
     * @return StateCsv
     */
    @Override
    public CsvState findById(Long id) {
        return stateCsvRecordsDataService.findById(id);
    }
}
