package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ActiveUser;

public interface ActiveUserService {

    ActiveUser create(ActiveUser activeUser);

    Long getActiveUserCount();
    
}
