package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ActiveUser;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.repository.ActiveUserDataService;
import org.motechproject.nms.kilkari.service.ActiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activeUserService")
public class ActiveUserServiceImpl implements ActiveUserService {

    @Autowired
    private ActiveUserDataService activeUserDataService;
    
    public static final Long ACTIVEUSER_INDEX = 1L;
    
    @Override
    public ActiveUser create(ActiveUser activeUser) {
        return activeUserDataService.create(activeUser);
    }
    
    @Override
    public ActiveUser getActiveUserCount() {
        return activeUserDataService.findActiveUserCountByIndex(ACTIVEUSER_INDEX);
    }

}
