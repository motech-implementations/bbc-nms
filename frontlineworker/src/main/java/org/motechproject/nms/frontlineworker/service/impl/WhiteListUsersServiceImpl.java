package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.WhiteListUsers;
import org.motechproject.nms.frontlineworker.repository.WhiteListUsersRecordDataService;
import org.motechproject.nms.frontlineworker.service.WhiteListUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class acts as implementation class for the interface WhiteListUsersService.
 * It uses WhiteListUsersRecordDataService which further takes MotechDataService as
 * base class to performs the CRUD operations on WhiteListUsers records. It also adds lookup
 * procedures to fetch the WhiteListUsers record from Database.
 */

@Service("whiteListUsersService")
public class WhiteListUsersServiceImpl implements WhiteListUsersService {

    public WhiteListUsersRecordDataService whiteListUsersRecordDataService;

    @Autowired
    public WhiteListUsersServiceImpl(WhiteListUsersRecordDataService whiteListUsersRecordDataService) {
        this.whiteListUsersRecordDataService = whiteListUsersRecordDataService;
    }


    @Override
    public WhiteListUsers findContactNo(String contactNo) {

        WhiteListUsers whiteListUsers = whiteListUsersRecordDataService.getContactNo(contactNo);
        return whiteListUsers;

    }

    @Override
    public void createWhiteListUsers(WhiteListUsers whiteListUsers) {
        whiteListUsersRecordDataService.create(whiteListUsers);
    }

}
