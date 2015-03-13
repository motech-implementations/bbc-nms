package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.District;

/**
 * This interface is used to operate on district using Motech Data service
 */
public interface DistrictRecordsDataService extends MotechDataService<District> {

    /**
     * Finds the district details by its parent code
     *
     * @param districtCode
     * @param stateCode
     * @return District
     */
    @Lookup
    District findDistrictByParentCode(@LookupField(name = "districtCode") Long districtCode, @LookupField(name = "stateCode") Long stateCode);
}
