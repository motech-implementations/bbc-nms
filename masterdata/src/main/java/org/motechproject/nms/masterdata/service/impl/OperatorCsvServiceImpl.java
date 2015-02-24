package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.OperatorCsv;
import org.motechproject.nms.masterdata.repository.OperatorCsvDataService;
import org.motechproject.nms.masterdata.service.OperatorCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on OperatorCsv
 */

@Service("operatorCsvService")
public class OperatorCsvServiceImpl implements OperatorCsvService {

    private OperatorCsvDataService operatorCsvDataService;

    @Autowired
    public OperatorCsvServiceImpl(OperatorCsvDataService operatorCsvDataService) {
        this.operatorCsvDataService = operatorCsvDataService;
    }

    /**
     * gets OperatorCsv object based by id
     *
     * @param id primary key of the record
     * @return OperatorCsv type object
     */
    @Override
    public OperatorCsv getRecord(Long id) {
        return operatorCsvDataService.findById(id);
    }

    /**
     * deletes OperatorCsv from database
     *
     * @param record OperatorCsv type object
     */
    @Override
    public void delete(OperatorCsv record) {
        operatorCsvDataService.delete(record);
    }

}
