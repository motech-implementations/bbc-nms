package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Operator;

/**
 * This interface is used to operate on Operator using Motech Data service
 */
public interface OperatorDataService extends MotechDataService<Operator> {

    /**
     * Finds the operator details by its code
     *
     * @param code
     * @return Operator
     */
    @Lookup
    Operator findByCode(@LookupField(name = "code") String code);
}
