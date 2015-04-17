package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.repository.CircleDataService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on Circle
 */

@Service("circleService")
public class CircleServiceImpl implements CircleService {

    private CircleDataService circleDataService;

    @Autowired
    public CircleServiceImpl(CircleDataService circleDataService) {
        this.circleDataService = circleDataService;
    }

    /**
     * create Circle type object
     *
     * @param record of the Circle
     */
    @Override
    public Circle create(Circle record) {
        return circleDataService.create(record);
    }

    /**
     * update Circle type object
     *
     * @param record of the Circle
     */
    @Override
    public void update(Circle record) {
        circleDataService.update(record);
    }

    /**
     * delete Circle type object
     *
     * @param record of the Circle
     */
    @Override
    public void delete(Circle record) {
        circleDataService.delete(record);
    }

    /**
     * delete All Circle type object
     */
    @Override
    public void deleteAll() {
        circleDataService.deleteAll();
    }

    /**
     * get Circle record for given Circle Census code
     *
     * @param circleCode Circle Census Code
     * @return State object corresponding to the census code
     */
    @Override
    public Circle getRecordByCode(String circleCode) {
        return circleDataService.findByCode(circleCode);

    }

    /**
     * Get CircleDataService object
     */
    @Override
    public CircleDataService getCircleDataService() {
        return circleDataService;
    }
}
