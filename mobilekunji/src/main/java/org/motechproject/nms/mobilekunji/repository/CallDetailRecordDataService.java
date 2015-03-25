package org.motechproject.nms.mobilekunji.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobilekunji.domain.CallDetail;

/**
 * Purpose of this Interface is to find call details of user by nmsFlwId.
 */
/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need. it finds callDetails of user by nmsFlwId
 */
public interface CallDetailRecordDataService extends MotechDataService<CallDetail> {

    /**
     * Finds call details by its nmsFlwId
     * @param nmsFlwId
     * @return CallDetail
     */
    @Lookup
    public CallDetail findCallDetailByNmsFlwId(@LookupField(name = "nmsFlwId") Long nmsFlwId);
}
