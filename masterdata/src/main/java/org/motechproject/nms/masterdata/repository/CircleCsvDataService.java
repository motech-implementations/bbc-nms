package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.CircleCsv;

public interface CircleCsvDataService extends MotechDataService<CircleCsv> {
    @Lookup
    Circle getCircleByCode(@LookupField(name = "code") String recordName);
}
