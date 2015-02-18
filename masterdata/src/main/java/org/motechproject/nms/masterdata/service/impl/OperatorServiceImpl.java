package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.repository.OperatorDataService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;

public class OperatorServiceImpl implements OperatorService {

    @Autowired
    private OperatorDataService operatorDataService;

    @Override
    public void create(Operator record){
        operatorDataService.create(record);
    }

    @Override
    public void update(Operator record) {
        operatorDataService.update(record);
    }
}
