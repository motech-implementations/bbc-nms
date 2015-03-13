package org.motechproject.nms.kilkari.ut;


/**
 * Class to test SubscriptionService
 */
public class SubscriptionServiceTest {
/*
    @Mock
    SubscriptionDataService subscriptionDataService;

    @InjectMocks
    SubscriptionServiceImpl subscriptionService;

    @Before
    public void setUp() {
        subscriptionService = new SubscriptionServiceImpl();
        initMocks(this);
    }
*/
    /**
     * Test case for getting Active/Pending Subscription
     * on the basis of Msisdn and Pack
     */
/*    @Test
    public void shouldGetSubscriptionPendingForActivationByMsisdnAndPack() {
        String msisdn = "9876543210";
        String packName = "Pack1";

        Subscription subscription = new Subscription();
        when(subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.ACTIVE)).thenReturn(subscription);
        subscriptionService.getActiveSubscriptionByMsisdnPack(msisdn, packName);
    }

    @Test
    public void shouldGetSubscriotionPendingForActivationByMctsIdAndPack() {
        String mctsId = "123";
        String packName = "Pack1";
        Long stateCode = 1L;

        Subscription subscription = new Subscription();
        when(subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.ACTIVE, stateCode)).thenReturn(subscription);
        subscriptionService.getActiveSubscriptionByMctsIdPack(mctsId, packName, stateCode);
    }
    */
}
