package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;

/**
 * This interface is used to operate on Health Sub facility using Motech Data service
 */
public interface HealthSubFacilityRecordsDataService extends MotechDataService<HealthSubFacility> {

    /**
     * Finds the health sub facility details by its parent code
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @param healthFacilityCode
     * @param healthSubFacilityCode
     * @return HealthSubFacility
     */
    @Lookup
    HealthSubFacility findHealthSubFacilityByParentCode(@LookupField(name = "stateCode") Long stateCode, @LookupField(name = "districtCode") Long districtCode,
                                                        @LookupField(name = "talukaCode") Long talukaCode, @LookupField(name = "healthBlockCode") Long healthBlockCode,
                                                        @LookupField(name = "healthFacilityCode") Long healthFacilityCode,
                                                        @LookupField(name = "healthSubFacilityCode") Long healthSubFacilityCode);

}
