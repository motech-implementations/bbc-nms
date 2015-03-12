package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.HealthBlock;

/**
 * This interface is used to operate on Health Block using Motech Data service
 */
public interface HealthBlockRecordsDataService extends MotechDataService<HealthBlock> {

    /**
     * finds the Health Block details by its parent code
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @return HealthBlock
     */
    @Lookup
    HealthBlock findHealthBlockByParentCode(@LookupField(name = "stateCode") Long stateCode, @LookupField(name = "districtCode") Long districtCode,
                                            @LookupField(name = "talukaCode") Long talukaCode, @LookupField(name = "healthBlockCode") Long healthBlockCode);

}
