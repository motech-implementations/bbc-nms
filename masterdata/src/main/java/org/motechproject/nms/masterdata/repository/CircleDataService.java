package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Circle;

/**
 * This interface is used to operate on Circle Csv using Motech Data service
 */
public interface CircleDataService extends MotechDataService<Circle> {

    /**
     * Finds the Circle details by its code
     * @param code
     * @return Circle
     */
    @Lookup
    Circle findByCode(@LookupField(name = "code") String code);
}
