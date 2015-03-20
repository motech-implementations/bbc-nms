package org.motechproject.nms.kilkari.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface SubscriptionDataService extends MotechDataService<Subscription> {
    
    @Lookup
    Subscription getSubscriptionByMsisdnPackStatus(
            @LookupField(name = "msisdn") String msisdn, 
            @LookupField(name = "packName") SubscriptionPack packName,
            @LookupField(name = "status") Status status);
    
    @Lookup
    Subscription getSubscriptionByMctsIdPackStatus(
            @LookupField(name = "mctsId") String mctsId, 
            @LookupField(name = "packName") SubscriptionPack packName,
            @LookupField(name = "status") Status status,
            @LookupField(name = "stateCode") Long stateCode);

    @Lookup
    Subscription getSubscriptionByMctsIdState(
            @LookupField(name = "mctsId") String mctsId, 
            @LookupField(name = "stateCode") Long stateCode);

    @Lookup
    List<Subscription> getSubscriptionByStatus(
            @LookupField(name = "status") Status active);

}
