package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.State;

public interface StateRecordsDataService extends MotechDataService<State> {

    @Lookup
    State findRecordByStateCode(@LookupField(name = "stateCode") Long stateCode);
}
