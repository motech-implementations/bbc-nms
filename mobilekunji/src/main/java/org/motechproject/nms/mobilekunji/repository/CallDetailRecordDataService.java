package org.motechproject.nms.mobilekunji.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobilekunji.domain.CallDetail;

/**
 * Created by abhishek on 13/3/15.
 */
public interface CallDetailRecordDataService extends MotechDataService<CallDetail> {

    @Lookup
    public CallDetail findCallDetailByNmsFlwId(@LookupField(name = "nmsFlwId") Long nmsFlwId);
}
