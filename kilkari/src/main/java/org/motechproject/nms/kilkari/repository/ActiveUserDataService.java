package org.motechproject.nms.kilkari.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.ActiveUser;
import org.motechproject.nms.kilkari.domain.Configuration;

public interface ActiveUserDataService extends MotechDataService<ActiveUser> {

    @Lookup
    ActiveUser findActiveUserCountByIndex(@LookupField(name = "index") Long index);
}
