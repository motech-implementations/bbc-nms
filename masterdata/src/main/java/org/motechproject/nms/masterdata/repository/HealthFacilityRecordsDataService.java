package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.HealthFacility;

/**
 * This interface is used to operate on health facility using Motech Data service
 */
public interface HealthFacilityRecordsDataService extends MotechDataService<HealthFacility> {

    /**
     * Finds the health facility details by its parent code
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @param healthFacilityCode
     * @return Healthfacility
     */
    @Lookup
    HealthFacility findHealthFacilityByParentCode(@LookupField(name = "stateCode") Long stateCode, @LookupField(name = "districtCode") Long districtCode,
                                                  @LookupField(name = "talukaCode") Long talukaCode, @LookupField(name = "healthBlockCode") Long healthBlockCode,
                                                  @LookupField(name = "healthFacilityCode") Long healthFacilityCode);


}
