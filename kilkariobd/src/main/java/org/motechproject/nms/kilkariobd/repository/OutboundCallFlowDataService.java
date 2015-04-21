package org.motechproject.nms.kilkariobd.repository;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;

public interface OutboundCallFlowDataService extends MotechDataService<OutboundCallFlow>{

    @Lookup
    OutboundCallFlow findRecordByCallStatus(@LookupField(name = "status")CallFlowStatus status);
}
