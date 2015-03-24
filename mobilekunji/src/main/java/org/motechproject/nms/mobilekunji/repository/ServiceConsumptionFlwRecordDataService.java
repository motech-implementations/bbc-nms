package org.motechproject.nms.mobilekunji.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobilekunji.domain.FlwDetail;

/**
 * Created by abhishek on 13/3/15.
 */
public interface ServiceConsumptionFlwRecordDataService extends MotechDataService<FlwDetail> {

    @Lookup
    public FlwDetail findServiceConsumptionByNmsFlwId(@LookupField(name = "nmsFlwId") Long nmsFlwId);

    @Lookup
    public FlwDetail findServiceConsumptionByMsisdn(@LookupField(name = "msisdn") String msisdn);
}
