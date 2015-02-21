package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.District;

public interface DistrictRecordsDataService extends MotechDataService<District> {

    @Lookup
    District findRecordByDistrictCode(@LookupField(name = "districtCode") Long districtCode);

    @Lookup
    District findRecordByDistrictCodeAndStateCode(@LookupField(name = "districtCode") Long districtCode,@LookupField(name = "stateCode") Long stateCode);
}
