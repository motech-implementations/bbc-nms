package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.ActiveSubscriptionCount;

public class ActiveUserTest {

    @Test
    public void shouldSetValuesInActiveUser() {

        ActiveSubscriptionCount activeUser = new ActiveSubscriptionCount();
        Long activeUserCount = 1L;
        Long index = 1L;

        activeUser.setActiveUserCount(activeUserCount);
        Assert.assertEquals(activeUserCount,activeUser.getActiveUserCount());

        activeUser.setIndex(index);
        Assert.assertEquals(index,activeUser.getIndex());
    }
}
