package org.motechproject.nms.kilkariobd.repository;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface OutboundCallFlowDataService extends MotechDataService<OutboundCallFlow>{

    @Lookup
    OutboundCallFlow findRecordByCallStatus(@LookupField(name = "status")CallFlowStatus status);

    @Lookup
    OutboundCallFlow findRecordByFileName(@LookupField(name = "obdFileName")String fileName);

    @Lookup
    OutboundCallFlow findRecordByCreationDate(@LookupField(name = "creationDate")DateTime date);
}
