package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Taluka;

/**
 * This interface is used to operate on Taluka using Motech Data service
 */
public interface TalukaRecordsDataService extends MotechDataService<Taluka> {

    /**
     * Finds the Taluka details by its parent code
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @return Taluka
     */
    @Lookup
    Taluka findTalukaByParentCode(@LookupField(name = "stateCode") Long stateCode,
                                  @LookupField(name = "districtCode") Long districtCode,
                                  @LookupField(name = "talukaCode") Long talukaCode);
}
