package org.motechproject.nms.frontlineworker.ut.domain;


import org.junit.Test;
import org.motechproject.nms.frontlineworker.domain.WhiteListUsers;

import static org.junit.Assert.assertEquals;

/**
 * This class performs the UT of FrontLineWorker.
 */

public class WhiteListUsersTest {

    WhiteListUsers whiteListUsers = new WhiteListUsers();

    @Test
    public void testContactNo() {

        whiteListUsers.setContactNo("1298765432");
        assertEquals("1298765432", whiteListUsers.getContactNo());
    }
}
