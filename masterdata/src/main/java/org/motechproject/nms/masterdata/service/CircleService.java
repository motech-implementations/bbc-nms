package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.Circle;

public interface CircleService {

    /**
     * create Circle type object
     *
     * @param record of the Circle
     */
    void create(Circle record);

    /**
     * update Circle type object
     *
     * @param record of the Circle
     */
    void update(Circle record);

    /**
     * delete Circle type object
     *
     * @param record of the Circle
     */
    void delete(Circle record);

    /**
     * get Circle record for given Circle Census code
     *
     * @param circleCode Circle Census Code
     * @return State object corresponding to the census code
     */
    Circle getRecordByCode(String circleCode);

    Circle findById(Long id);
}
