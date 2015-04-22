package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;


public class SubscriptionPackTest {

    @Test
    public void shouldGetStartWeekNumberOf72WeekPack() {

        SubscriptionPack subscriptionPack = SubscriptionPack.PACK_72_WEEKS;
        Assert.assertTrue(1 == subscriptionPack.getStartWeekNumber());
    }

    @Test
    public void shouldGetStartWeekNumberOf48WeekPack() {

        SubscriptionPack subscriptionPack = SubscriptionPack.PACK_48_WEEKS;
        Assert.assertTrue(25 == subscriptionPack.getStartWeekNumber());
    }

    @Test
    public void shouldGetDurationOf72WeekPack() {

        SubscriptionPack subscriptionPack = SubscriptionPack.PACK_72_WEEKS;
        Assert.assertTrue(72 == subscriptionPack.getDuration());
    }

    @Test
    public void shouldGetDurationOf48WeekPack() {

        SubscriptionPack subscriptionPack = SubscriptionPack.PACK_48_WEEKS;
        Assert.assertTrue(48 == subscriptionPack.getDuration());
    }

    @Test
    public void shouldFindSubscriptionPackByName() {

        SubscriptionPack subscriptionPack = SubscriptionPack.PACK_48_WEEKS;
        Assert.assertEquals(null, SubscriptionPack.findByName(null));
        Assert.assertEquals(null, SubscriptionPack.findByName("invalidPackName"));
        Assert.assertEquals(subscriptionPack, SubscriptionPack.findByName("48WeeksPack"));
    }
}
