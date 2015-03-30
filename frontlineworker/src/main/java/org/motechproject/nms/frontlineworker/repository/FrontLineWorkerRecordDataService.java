package org.motechproject.nms.frontlineworker.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;

import java.util.List;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need
 */


public interface FrontLineWorkerRecordDataService extends MotechDataService<FrontLineWorker> {

    @Lookup
    List<FrontLineWorker> getFlwByFlwIdAndStateId(@LookupField(name = "flwId") Long flwId, @LookupField(name = "stateCode") Long stateCode);

    @Lookup
    List<FrontLineWorker> getFlwByContactNo(@LookupField(name = "contactNo") String contactNo);

    @Lookup
    FrontLineWorker findById(@LookupField(name = "id") Long id);

}
