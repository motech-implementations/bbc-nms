package org.motechproject.nms.frontlineworker.ut.domain;


import org.junit.Test;
import org.motechproject.nms.frontlineworker.domain.CsvWhiteListUsers;

import static org.junit.Assert.assertEquals;

/**
 * This class performs the UT of CsvWhiteListUsers.
 */
public class CsvWhiteListUsersTest {

    CsvWhiteListUsers csvWhiteListUsers = new CsvWhiteListUsers();

    @Test
    public void testShouldSetWithContactNumber() {

        csvWhiteListUsers.setContactNo("1234567890");
        assertEquals("Contact No[1234567890]", csvWhiteListUsers.toString());
    }
}
