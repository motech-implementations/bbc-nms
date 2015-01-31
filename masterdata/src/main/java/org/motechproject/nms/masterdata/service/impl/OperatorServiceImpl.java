package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.repository.OperatorDataService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("operatorService")
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

    @Override
    public Operator getRecordByCode(String code) {
        return operatorDataService.findByCode(code);

    }
}
