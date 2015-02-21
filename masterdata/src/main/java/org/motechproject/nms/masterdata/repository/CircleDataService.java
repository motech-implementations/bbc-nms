package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Circle;

public interface CircleDataService extends MotechDataService<Circle> {
    @Lookup
    Circle findByCode(@LookupField(name = "code") String code);
}
