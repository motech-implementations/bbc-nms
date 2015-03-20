package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ActiveUser;
import org.motechproject.nms.kilkari.repository.ActiveUserDataService;
import org.motechproject.nms.kilkari.service.ActiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class creates and get active user count
 */
@Service("activeUserService")
public class ActiveUserServiceImpl implements ActiveUserService {

    @Autowired
    private ActiveUserDataService activeUserDataService;
    
    public static final Long ACTIVEUSER_INDEX = 1L;

    /**
     * This method creates active user.
     * @param activeUser ActiveUser type object
     * @return ActiveUser type object
     */
    @Override
    public ActiveUser create(ActiveUser activeUser) {
        return activeUserDataService.create(activeUser);
    }

    /**
     * finds active user count by index
     * @return Long type object
     */
    @Override
    public Long getActiveUserCount() {
        return activeUserDataService.findActiveUserCountByIndex(ACTIVEUSER_INDEX).getActiveUserCount();
    }

}
