package org.motechproject.nms.mobileacademy.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobileacademy.domain.FlwUsageDetail;

/**
 * Interface for repository that persists simple records and allows CRUD on
 * FlwUsageDetail table. MotechDataService base class will provide the
 * implementation of this class as well as methods for adding, deleting, saving
 * and finding all instances. In this class we define and custom lookups we may
 * need.
 */
public interface FlwUsageDetailDataService extends
        MotechDataService<FlwUsageDetail> {

    /**
     * find Flw Usage Detail By flwId
     * 
     * @param flwId
     * @return FlwUsuageDetail
     */
    @Lookup
    FlwUsageDetail findByFlwId(@LookupField(name = "flwId") Long flwId);

}
