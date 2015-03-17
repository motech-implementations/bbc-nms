package org.motechproject.nms.kilkari.ut;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.kilkari.builder.SubscriptionBuilder;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.dto.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkari.service.impl.UserDetailsServiceImpl;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RegistrationServiceImplTest extends TestCase {
    @Mock
    private SubscriberService subscriberService;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private LanguageLocationCodeService llcService;

    private UserDetailsServiceImpl registrationService;

    private String msisdn = "1234567890";
    private Subscriber subscriber;
    private List<String> activePackList;

    @Before
    public void init() {
        initMocks(this);
        registrationService = new UserDetailsServiceImpl();
        SubscriptionBuilder builder = new SubscriptionBuilder();
        subscriber = builder.buildSubscriber(msisdn, 123);
    }

    @Test
    public void shouldGetSubscriberDetailsForExistingSubscriber() {
        activePackList.add("48WeeksPack");
        activePackList.add("72WeeksPack");
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionByMsisdn(msisdn)).thenReturn(activePackList);
        SubscriberDetailApiResponse response= registrationService.getSubscriberDetails(msisdn, "AP");
        Assert.assertEquals(response.getLanguageLocationCode(), String.valueOf(123));
        Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
        Assert.assertEquals(response.getCircle(), "AP");
    }

    @Test
    public void shouldGetAllSubscriberDetails() {
        activePackList.add("48WeeksPack");
        activePackList.add("72WeeksPack");
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionByMsisdn(msisdn)).thenReturn(activePackList);
        SubscriberDetailApiResponse response= registrationService.getSubscriberDetails(msisdn, "AP");
        Assert.assertEquals(response.getLanguageLocationCode(), String.valueOf(123));
        Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
        Assert.assertEquals(response.getCircle(), "AP");
    }
}
