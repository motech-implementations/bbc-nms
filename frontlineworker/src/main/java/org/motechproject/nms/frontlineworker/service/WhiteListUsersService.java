package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.WhiteListUsers;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * Its implementation uses the repository interface WhiteListUsersRecordDataService whose base class
 * MotechDataService will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances. In this interface we
 * also declare lookups we may need to find record from Database.
 */
public interface WhiteListUsersService {

    /**
     * This procedure is used to find a WhiteListUsers object using the contact number
     *
     * @param contactNo contact number for which WhiteListUsers object is to be returned
     * @return
     */
    public WhiteListUsers findContactNo(String contactNo);

    /**
     * This procedure is used to create a new WhiteListUsers object in databse
     *
     * @param whiteListUsers
     */
    public void createWhiteListUsers(WhiteListUsers whiteListUsers);
}
