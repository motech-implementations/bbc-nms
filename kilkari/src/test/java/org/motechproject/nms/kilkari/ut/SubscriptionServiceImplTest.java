package org.motechproject.nms.kilkari.ut;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.motechproject.nms.kilkari.builder.SubscriptionBuilder;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.ActiveSubscriptionCountService;
import org.motechproject.nms.kilkari.service.CommonValidatorService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.impl.SubscriptionServiceImpl;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;

import static org.mockito.Mockito.*;

public class SubscriptionServiceImplTest {

    @Mock
    private SubscriberService subscriberService;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private ActiveSubscriptionCountService activeSubscriptionCountService;

    @Mock
    private SubscriptionDataService subscriptionDataService;

    @Mock
    private CommonValidatorService commonValidatorService;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService = new SubscriptionServiceImpl();

    private SubscriptionBuilder subscriptionBuilder;
    Subscription subscription = new Subscription();


    private String msisdn = "1234567890";
    private String mctsId = "123456";

    @Before
    public void setUp() {
        subscriptionService = new SubscriptionServiceImpl();
        subscriptionBuilder = new SubscriptionBuilder();
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        subscription.setPackName(SubscriptionPack.PACK_48_WEEKS);
        subscription.setMsisdn("1234567890");
        subscription.setStatus(Status.ACTIVE);
        subscription.setMctsId("mctsId");
        subscription.setStateCode(1L);
    }

    @Test
    public void shouldGetActiveSubscriptionByMsisdnPackNonNull() {
        when(subscriptionDataService.getSubscriptionByMsisdnPackStatus(
                msisdn, SubscriptionPack.PACK_48_WEEKS, Status.ACTIVE))
                .thenReturn(subscriptionBuilder.buildSubscription(msisdn, Channel.IVR, Status.PENDING_ACTIVATION));
        Subscription subscription = subscriptionService.getActiveSubscriptionByMsisdnPack(msisdn, SubscriptionPack.PACK_48_WEEKS);
        Assert.assertNotNull(subscription);
    }

    @Test
    public void shouldGetActiveSubscriptionByMsisdnPackNull() {
        when(subscriptionDataService.getSubscriptionByMsisdnPackStatus(
                msisdn, SubscriptionPack.PACK_48_WEEKS, Status.ACTIVE)).thenReturn(null);
        Subscription subscription = subscriptionService.getActiveSubscriptionByMsisdnPack(msisdn, SubscriptionPack.PACK_48_WEEKS);
        Assert.assertNull(subscription);
    }

    @Test
    public void shouldGetActiveSubscriptionByMctsIdPackNonNull() {
        when(subscriptionDataService.getSubscriptionByMctsIdPackStatus(
                mctsId, SubscriptionPack.PACK_48_WEEKS, Status.ACTIVE, 1L))
                .thenReturn(subscriptionBuilder.buildSubscription(msisdn, Channel.IVR, Status.PENDING_ACTIVATION));
        Subscription subscription = subscriptionService.getActiveSubscriptionByMctsIdPack(mctsId, SubscriptionPack.PACK_48_WEEKS, 1L);
        Assert.assertNotNull(subscription);
    }

    @Test
    public void shouldGetActiveSubscriptionByMctsIdPackNull() {
        when(subscriptionDataService.getSubscriptionByMctsIdPackStatus(
                mctsId, SubscriptionPack.PACK_48_WEEKS, Status.ACTIVE, 1L)).thenReturn(null);
        Subscription subscription = subscriptionService.getActiveSubscriptionByMctsIdPack(mctsId, SubscriptionPack.PACK_48_WEEKS, 1L);
        Assert.assertNull(subscription);

    }

    @Test
    public void shouldReturnSubscriptionByMsisdnPack() {

        Subscription returnSubscription = new Subscription();

        when(subscriptionDataService.getSubscriptionByMsisdnPackStatus("1234567890",SubscriptionPack.PACK_48_WEEKS, Status.ACTIVE)).thenReturn(subscription);
        returnSubscription = subscriptionService.getActiveSubscriptionByMsisdnPack("1234567890",SubscriptionPack.PACK_48_WEEKS);

        Assert.assertEquals(subscription,returnSubscription);

    }

    @Test
    public void shouldReturnSubscriptionByMctsId() {

        Subscription returnSubscription = new Subscription();

        when(subscriptionDataService.getSubscriptionByMctsIdPackStatus("mctsId", SubscriptionPack.PACK_48_WEEKS, Status.ACTIVE, 1L)).thenReturn(subscription);
        returnSubscription = subscriptionService.getActiveSubscriptionByMctsIdPack("mctsId", SubscriptionPack.PACK_48_WEEKS,1L);

        Assert.assertEquals(subscription,returnSubscription);

    }


    @Test
    public void shouldNotDeactivateAnySubscriptionWithNullSubscription() {

        when(subscriptionDataService.findById(1L)).thenReturn(null);

        try {
            subscriptionService.deactivateSubscription(1L, "op", "cc");
        } catch (DataValidationException e) {
        }

        verify(subscriptionDataService).findById(1L);
    }



    @Test
    public void handleIVRSubscriptionRequestTest() {

        SubscriptionServiceImpl subscriptionServiceSpy = new SubscriptionServiceImpl();
        SubscriptionServiceImpl spy;
        spy = Mockito.spy(subscriptionServiceSpy);

        Subscriber subscriber = new Subscriber();
        subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        subscriber.setMsisdn("1234567890");

        doReturn(subscription).when(spy).getActiveSubscriptionByMsisdnPack("1234567890", SubscriptionPack.PACK_72_WEEKS);

        try {
            spy.handleIVRSubscriptionRequest(subscriber, "OP", "CC", 123);
        } catch (DataValidationException e) {
        } catch (NmsInternalServerError nmsInternalServerError) {

            Assert.assertTrue(nmsInternalServerError instanceof NmsInternalServerError );
            Assert.assertEquals(((NmsInternalServerError)nmsInternalServerError).getErrorCode(), ErrorCategoryConstants.INCONSISTENT_DATA);
        }

    }
}
