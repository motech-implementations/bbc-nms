package org.motechproject.nms.kilkari.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscription;

public interface SubscriptionDataService extends MotechDataService<Subscription> {
    
    @Lookup
    Subscription getSubscriptionByMsisdnPackStatus(
            @LookupField(name = "msisdn") String msisdn, 
            @LookupField(name = "packName") String packName,
            @LookupField(name = "status") Status status);
    
    @Lookup
    Subscription getSubscriptionByMctsIdPackStatus(
            @LookupField(name = "mctsId") String mctsId, 
            @LookupField(name = "packName") String packName,
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
