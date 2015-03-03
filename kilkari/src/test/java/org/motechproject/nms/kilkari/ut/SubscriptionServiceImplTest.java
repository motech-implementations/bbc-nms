package org.motechproject.nms.kilkari.ut;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.nms.kilkari.builder.SubscriptionBuilder;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.ActiveSubscriptionCountService;
import org.motechproject.nms.kilkari.service.CommonValidatorService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.impl.SubscriberServiceImpl;
import org.motechproject.nms.kilkari.service.impl.SubscriptionServiceImpl;

import static org.mockito.Mockito.when;

public class SubscriptionServiceImplTest {
    @Mock
    private SubscriptionDataService subscriptionDataService;

    @Mock
    private SubscriberService subscriberService;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private ActiveSubscriptionCountService activeSubscriptionCountService;

    @Mock
    private CommonValidatorService commonValidatorService;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private SubscriptionBuilder subscriptionBuilder;

    private String msisdn = "1234567890";
    private String mctsId = "123456";

    @Before
    public void setUp() {
        subscriptionService = new SubscriptionServiceImpl();
        subscriptionBuilder = new SubscriptionBuilder();
        MockitoAnnotations.initMocks(this);
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
}
