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

    public WhiteListUsers findContactNo(String contactNo);

    public void createWhiteListUsers(WhiteListUsers whiteListUsers);
}
