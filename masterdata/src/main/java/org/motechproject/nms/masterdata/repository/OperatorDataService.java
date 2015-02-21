package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Operator;

public interface OperatorDataService extends MotechDataService<Operator> {
    @Lookup
    Operator findByCode(@LookupField(name = "code") String code);
}
