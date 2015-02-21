package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.Circle;

public interface CircleService {

    void create(Circle record);

    void update(Circle record);

    /**
     * get Circle record for given Circle Census code
     * @param circleCode State Census Code
     * @return State object corresponding to the census code
     */
    Circle getRecordByCode(String circleCode);
}
