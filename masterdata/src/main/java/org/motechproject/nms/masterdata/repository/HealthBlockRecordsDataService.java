package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.HealthBlock;

/**
 * Created by abhishek on 31/1/15.
 */
public interface HealthBlockRecordsDataService extends MotechDataService<HealthBlock> {
    @Lookup
    HealthBlock findHealthBlockByParentCode(@LookupField(name = "stateCode") Long stateCode, @LookupField(name = "districtCode") Long districtCode,
                                            @LookupField(name = "talukaCode") Long talukaCode, @LookupField(name = "healthBlockCode") Long healthBlockCode);

}
