package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.State;

/**
 * Created by abhishek on 26/1/15.
 */
public interface StateRecordsDataService extends MotechDataService<State> {
    @Lookup
    State findRecordByStateCode(@LookupField(name = "stateCode") Long code);
}
