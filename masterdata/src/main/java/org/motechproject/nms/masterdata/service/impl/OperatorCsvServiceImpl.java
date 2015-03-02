package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.OperatorCsv;
import org.motechproject.nms.masterdata.repository.OperatorCsvDataService;
import org.motechproject.nms.masterdata.service.OperatorCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("operatorCsvService")
public class OperatorCsvServiceImpl implements OperatorCsvService {


    private OperatorCsvDataService operatorCsvDataService;

    @Autowired
    public OperatorCsvServiceImpl(OperatorCsvDataService operatorCsvDataService) {
        this.operatorCsvDataService = operatorCsvDataService;
    }

    @Override
    public OperatorCsv getRecord(Long id) {
        return operatorCsvDataService.findById(id);
    }

    @Override
    public void delete(OperatorCsv record) {
        operatorCsvDataService.delete(record);
    }

}
