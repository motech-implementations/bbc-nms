package org.motechproject.nms.mobilekunji.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFlw;

/**
 * Created by abhishek on 13/3/15.
 */
public interface ServiceConsumptionFlwRecordDataService extends MotechDataService<ServiceConsumptionFlw> {

    @Lookup
    public ServiceConsumptionFlw findServiceConsumptionByNmsFlwId(@LookupField Long nmsFlwId);
}
