package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.Circle;

/**
 * This interface is used for crud operations on Circle
 */

public interface CircleService {

    /**
     * create Circle type object
     *
     * @param record of the Circle
     */
    Circle create(Circle record);

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
     * delete All Circle type object
     */
    void deleteAll();

    /**
     * get Circle record for given Circle Census code
     *
     * @param circleCode Circle Census Code
     * @return State object corresponding to the census code
     */
    Circle getRecordByCode(String circleCode);
}
