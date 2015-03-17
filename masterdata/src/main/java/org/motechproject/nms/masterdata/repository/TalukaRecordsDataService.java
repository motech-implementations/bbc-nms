package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Taluka;

/**
 * Created by abhishek on 29/1/15.
 */
public interface TalukaRecordsDataService extends MotechDataService<Taluka> {
    @Lookup
    Taluka findTalukaByParentCode(@LookupField(name = "stateCode") Long stateCode,
                                  @LookupField(name = "districtCode") Long districtCode,
                                  @LookupField(name = "talukaCode") Long talukaCode);
}
