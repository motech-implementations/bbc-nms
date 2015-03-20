package org.motechproject.nms.kilkari.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.Subscriber;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface SubscriberDataService extends MotechDataService<Subscriber> {
    @Lookup
    List<Subscriber> findRecordByMsisdn(@LookupField(name = "msisdn") String msisdn);

}
