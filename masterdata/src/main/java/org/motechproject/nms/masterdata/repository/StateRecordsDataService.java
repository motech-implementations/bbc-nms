package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.State;

/**
 * This interface is used to operate on State using Motech Data service
 */
public interface StateRecordsDataService extends MotechDataService<State> {

    /**
     * Finds the State details by its state code
     *
     * @param stateCode
     * @return State
     */
    @Lookup
    State findRecordByStateCode(@LookupField(name = "stateCode") Long stateCode);
}
