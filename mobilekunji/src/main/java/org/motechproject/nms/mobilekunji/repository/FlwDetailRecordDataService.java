package org.motechproject.nms.mobilekunji.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobilekunji.domain.FlwDetail;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need. it will be used to find ServiceConsumption by nmsFlwId or Msisdn
 */
public interface FlwDetailRecordDataService extends MotechDataService<FlwDetail> {


    /**
     * Finds the FlwDetail record by its nmsFlwId
     * @param nmsFlwId
     * @return FlwDetail
     */
    @Lookup
    public FlwDetail findFlwDetailByNmsFlwId(@LookupField(name = "nmsFlwId") Long nmsFlwId);

    /**
     * Finds the FlwDetail record by its msisdn
     * @param msisdn
     * @return FlwDetail
     */
    @Lookup
    public FlwDetail findFlwDetailByMsisdn(@LookupField(name = "msisdn") String msisdn);
}
