package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Village;

/**
 * This interface is used to operate on Village using Motech Data service
 */
public interface VillageRecordsDataService extends MotechDataService<Village> {

    /**
     * Finds the village details by its parent code
     *
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param villageCode
     * @return Village
     */
    @Lookup
    Village findVillageByParentCode(@LookupField(name = "stateCode") Long stateCode, @LookupField(name = "districtCode") Long districtCode,
                                    @LookupField(name = "talukaCode") Long talukaCode, @LookupField(name = "villageCode") Long villageCode);

}
