package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.repository.OperatorDataService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on Operator
 */

@Service("operatorService")
public class OperatorServiceImpl implements OperatorService {

    private OperatorDataService operatorDataService;

    @Autowired
    public OperatorServiceImpl(OperatorDataService operatorDataService) {
        this.operatorDataService = operatorDataService;
    }

    /**
     * creates Operator from database
     *
     * @param record Operator from database
     */
    @Override
    public Operator create(Operator record) {
        return operatorDataService.create(record);
    }

    /**
     * updates Operator from database
     *
     * @param record Operator from database
     */
    @Override
    public Operator update(Operator record) {
        return operatorDataService.update(record);
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
     * delete All Operator type object
     */
    @Override
    public void deleteAll() {
        operatorDataService.deleteAll();
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

    /**
     * Get OperatorDataService object
     */
    @Override
    public OperatorDataService getOperatorDataService() {
        return operatorDataService;
    }
}