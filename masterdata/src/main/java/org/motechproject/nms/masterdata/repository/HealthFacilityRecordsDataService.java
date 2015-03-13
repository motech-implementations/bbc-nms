package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.HealthFacility;

/**
 * Created by abhishek on 31/1/15.
 */
public interface HealthFacilityRecordsDataService extends MotechDataService<HealthFacility> {
    @Lookup
    HealthFacility findHealthFacilityByParentCode(@LookupField(name = "stateCode") Long stateCode, @LookupField(name = "districtCode") Long districtCode,
                                                  @LookupField(name = "talukaCode") Integer talukaCode, @LookupField(name = "healthBlockCode") Long healthBlockCode,
                                                  @LookupField(name = "healthFacilityCode") Long healthFacilityCode);


}
