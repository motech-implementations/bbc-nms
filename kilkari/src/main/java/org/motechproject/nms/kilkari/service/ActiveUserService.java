package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ActiveUser;

/**
 * This interface provide methods to creates and get active user count
 */
public interface ActiveUserService {


    /**
     * This method creates active user.
     * @param activeUser ActiveUser type object
     * @return ActiveUser type object
     */
    ActiveUser create(ActiveUser activeUser);

    /**
     * finds active user count by index
     * @return Long type object
     */
    Long getActiveUserCount();
    
    
    /**
     * Checks ActiveUserCount record exists
     * @return true if exists else false
     */
    Boolean isActiveUserCountPresent();

}
