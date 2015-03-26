package org.motechproject.nms.kilkari.ut.domain;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.*;

public class SubscriptionTest {

    @Test
    public void shouldSetValuesInSubscription() {

        Subscription subscription = new Subscription();
        Channel channel = Channel.IVR;
        SubscriberTest subscriberTest = new SubscriberTest();
        Subscriber subscriber = subscriberTest.createSubscriber();
        DeactivationReason deactivationReason = DeactivationReason.INVALID_MSISDN;
        DateTime dateTime = new DateTime();
        SubscriptionPack subscriptionPack = SubscriptionPack.PACK_48_WEEKS;
        Status status = Status.DEACTIVATED;

        subscription.setChannel(channel);
        Assert.assertEquals(channel, subscription.getChannel());

        subscription.setMsisdn("msisdn");
        Assert.assertEquals("msisdn", subscription.getMsisdn());

        subscription.setSubscriber(subscriber);
        Assert.assertEquals(subscriber, subscription.getSubscriber());

        subscription.setDeactivationReason(deactivationReason);
        Assert.assertEquals(deactivationReason, subscription.getDeactivationReason());

        subscription.setMctsId("mctsId");
        Assert.assertEquals("mctsId", subscription.getMctsId());

        subscription.setMessageNumber(6);
        Assert.assertTrue(6 == subscription.getMessageNumber());

        subscription.setNextObdDate(dateTime);
        Assert.assertEquals(dateTime, subscription.getNextObdDate());

        subscription.setOperatorCode("operatorCode");
        Assert.assertEquals("operatorCode", subscription.getOperatorCode());

        subscription.setStartDate(dateTime);
        Assert.assertEquals(dateTime, subscription.getStartDate());

        subscription.setStateCode(1L);
        Assert.assertTrue(1L == subscription.getStateCode());

        subscription.setWeekNumber(4);
        Assert.assertTrue(4 == subscription.getWeekNumber());

        subscription.setPackName(subscriptionPack);
        Assert.assertEquals(subscriptionPack, subscription.getPackName());

        subscription.setStatus(status);
        Assert.assertEquals(status, subscription.getStatus());

    }

}
