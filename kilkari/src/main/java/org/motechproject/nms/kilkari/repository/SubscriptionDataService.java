package org.motechproject.nms.kilkari.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;

public interface SubscriptionDataService extends MotechDataService<Subscription> {
	@Lookup
	Subscription findRecordIsDeactivatedBySystem(@LookupField(name = "isDeactivatedBySystem") Boolean isDeactivatedBySystem);
	
	@Lookup
    Subscription getSubscriptionByMsisdnPackStatus(
            @LookupField(name = "msisdn") String msisdn, 
            @LookupField(name = "packName") String packName,
            @LookupField(name = "status") Status status);
	
	@Lookup
    Subscription getPackSubscriptionByMctsIdPackStatus(
            @LookupField(name = "mctsId") String mctsId, 
            @LookupField(name = "packName") String packName,
            @LookupField(name = "status") Status status);

}
