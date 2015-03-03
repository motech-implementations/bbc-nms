package org.motechproject.nms.kilkari.ut.dto;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.dto.request.SubscriptionCreateApiRequest;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;

public class SubscriptionCreateApiRequestTest {

    SubscriptionCreateApiRequest subscriptionApiRequest = new SubscriptionCreateApiRequest();

    @Test
    public void shouldSetValuesInSubscriptionApiRequest() {

        subscriptionApiRequest.setSubscriptionPack("subscriptionPack");
        Assert.assertEquals("subscriptionPack", subscriptionApiRequest.getSubscriptionPack());

        subscriptionApiRequest.setLanguageLocationCode(23);
        Assert.assertTrue(23 == subscriptionApiRequest.getLanguageLocationCode());

        subscriptionApiRequest.setCallId("callId");
        Assert.assertEquals("callId", subscriptionApiRequest.getCallId());

        subscriptionApiRequest.setCircle("circle");
        Assert.assertEquals("circle", subscriptionApiRequest.getCircle());

        subscriptionApiRequest.setCallingNumber("1234567890");
        Assert.assertEquals("1234567890", subscriptionApiRequest.getCallingNumber());

        subscriptionApiRequest.setOperator("operator");
        Assert.assertEquals("operator", subscriptionApiRequest.getOperator());

    }

    @Test
    public void shouldReturnSubscriberWithBeneficiaryTypeChild() {

        Subscriber subscriber = new Subscriber();
        BeneficiaryType beneficiaryType = BeneficiaryType.CHILD;
        DeactivationReason deactivationReason = DeactivationReason.NONE;

        subscriptionApiRequest.setSubscriptionPack("48WeeksPack");
        subscriber = subscriptionApiRequest.toSubscriber();

        Assert.assertEquals(beneficiaryType, subscriber.getBeneficiaryType());
        Assert.assertEquals(deactivationReason, subscriber.getDeactivationReason());
        Assert.assertEquals(subscriptionApiRequest.getCallingNumber(), subscriber.getMsisdn());
        Assert.assertEquals(subscriptionApiRequest.getLanguageLocationCode(), subscriber.getLanguageLocationCode());
        Assert.assertNull(subscriber.getLmp());
        Assert.assertNotNull(subscriber.getDob());
    }

    @Test
    public void shouldReturnSubscriberWithBeneficiaryTypeMother() {

        Subscriber subscriber = new Subscriber();
        BeneficiaryType beneficiaryType = BeneficiaryType.MOTHER;
        DeactivationReason deactivationReason = DeactivationReason.NONE;
        DateTime dateTime = new DateTime();

        subscriptionApiRequest.setSubscriptionPack("72WeeksPack");
        subscriber = subscriptionApiRequest.toSubscriber();

        Assert.assertEquals(beneficiaryType, subscriber.getBeneficiaryType());
        Assert.assertEquals(deactivationReason, subscriber.getDeactivationReason());
        Assert.assertEquals(subscriptionApiRequest.getCallingNumber(), subscriber.getMsisdn());
        Assert.assertEquals(subscriptionApiRequest.getLanguageLocationCode(), subscriber.getLanguageLocationCode());
        Assert.assertNotNull(subscriber.getLmp());
        Assert.assertNull(subscriber.getDob());
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenLLCIsNull() {

        subscriptionApiRequest.setSubscriptionPack("subscriptionPack");
        subscriptionApiRequest.setCallId("callId");
        subscriptionApiRequest.setCircle("circle");
        subscriptionApiRequest.setCallingNumber("1234567890");
        subscriptionApiRequest.setOperator("operator");

        try {
            subscriptionApiRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)ex).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
    }

}
