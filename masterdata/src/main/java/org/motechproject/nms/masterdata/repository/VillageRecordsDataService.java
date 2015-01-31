package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.Village;

/**
 * Created by abhishek on 31/1/15.
 */
public interface VillageRecordsDataService extends MotechDataService<Village> {


    @Lookup
    Village findVillageByParentCode(@LookupField(name = "stateCode") Long stateCode,@LookupField(name = "districtCode") Long districtCode,
                                                       @LookupField(name = "talukaCode") String talukaCode,@LookupField(name = "villageCode") Long villageCode);

}
