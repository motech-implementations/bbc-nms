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

    public OperatorServiceImpl(OperatorDataService operatorDataService) {
        this.operatorDataService = operatorDataService;
    }

    /**
     * creates Operator from database
     *
     * @param record Operator from database
     */
    @Override
    public void create(Operator record) {
        operatorDataService.create(record);
    }

    /**
     * updates Operator from database
     *
     * @param record Operator from database
     */
    @Override
    public void update(Operator record) {
        operatorDataService.update(record);
    }

    /**
     * deletes Operator from database
     *
     * @param record Operator from database
     */
    @Override
    public void delete(Operator record) {
        operatorDataService.delete(record);
    }

    /**
     * get Operator record for given Operator Census code
     *
     * @param code Operator Census Code
     * @return Operator object corresponding to the census code
     */
    @Override
    public Operator getRecordByCode(String code) {
        return operatorDataService.findByCode(code);

    }

    @Override
    public Operator findById(Long id) {
        return operatorDataService.findById(id);
    }
}
