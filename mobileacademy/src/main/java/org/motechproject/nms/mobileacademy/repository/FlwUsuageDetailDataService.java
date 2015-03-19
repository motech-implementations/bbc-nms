package org.motechproject.nms.mobileacademy.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobileacademy.domain.FlwUsuageDetail;

/**
 * Interface for repository that persists simple records and allows CRUD on
 * FlwUsuageDetail table. MotechDataService base class will provide the
 * implementation of this class as well as methods for adding, deleting, saving
 * and finding all instances. In this class we define and custom lookups we may
 * need.
 */
public interface FlwUsuageDetailDataService extends
        MotechDataService<FlwUsuageDetail> {

    /**
     * find By flwId
     * 
     * @param flwId
     * @return FlwUsuageDetail
     */
    @Lookup
    FlwUsuageDetail findByFlwId(@LookupField(name = "flwId") Long flwId);

}
