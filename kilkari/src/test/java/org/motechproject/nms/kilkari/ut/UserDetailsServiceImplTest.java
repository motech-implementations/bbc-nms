//package org.motechproject.nms.kilkari.ut;
//
//import junit.framework.TestCase;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.motechproject.nms.kilkari.builder.SubscriptionBuilder;
//import org.motechproject.nms.kilkari.domain.Subscriber;
//import org.motechproject.nms.kilkari.domain.SubscriptionPack;
//import org.motechproject.nms.kilkari.dto.SubscriberDetailApiResponse;
//import org.motechproject.nms.kilkari.service.SubscriberService;
//import org.motechproject.nms.kilkari.service.SubscriptionService;
//import org.motechproject.nms.kilkari.service.impl.UserDetailsServiceImpl;
//import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
//
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//public class UserDetailsServiceImplTest extends TestCase {
//    @Mock
//    private SubscriberService subscriberService;
//
//    @Mock
//    private SubscriptionService subscriptionService;
//
//    @Mock
//    private LanguageLocationCodeService llcService;
//
//    private UserDetailsServiceImpl registrationService;
//
//    private String msisdn = "1234567890";
//    private Subscriber subscriber;
//    private List<SubscriptionPack> activePackList;
//
//    @Before
//    public void init() {
//        initMocks(this);
//        registrationService = new UserDetailsServiceImpl();
//        SubscriptionBuilder builder = new SubscriptionBuilder();
//        subscriber = builder.buildSubscriber(msisdn, 123);
//    }
//
//    @Test
//    @Ignore
//    public void shouldGetSubscriberDetailsForExistingSubscriber() {
//        activePackList.add(SubscriptionPack.PACK_48_WEEKS);
//        activePackList.add(SubscriptionPack.PACK_72_WEEKS);
//        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
//        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(activePackList);
//        SubscriberDetailApiResponse response= registrationService.getSubscriberDetails(msisdn, "AP");
////        Assert.assertEquals(response.getLanguageLocationCode(), String.valueOf(123));
////        Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
////        Assert.assertEquals(response.getCircle(), "AP");
//    }
//
//    @Test
//    @Ignore
//    public void shouldGetAllSubscriberDetails() {
//        activePackList.add(SubscriptionPack.PACK_48_WEEKS);
//        activePackList.add(SubscriptionPack.PACK_72_WEEKS);
//        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
//        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(activePackList);
//        SubscriberDetailApiResponse response= registrationService.getSubscriberDetails(msisdn, "AP");
////        Assert.assertEquals(response.getLanguageLocationCode(), String.valueOf(123));
////        Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
////        Assert.assertEquals(response.getCircle(), "AP");
//    }
//}
