package org.motechproject.nms.kilkari.it.web;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkari.builder.LanguageLocationCodeBuilder;
import org.motechproject.nms.kilkari.builder.LocationBuilder;
import org.motechproject.nms.kilkari.builder.SubscriptionBuilder;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.dto.request.SubscriptionCreateApiRequest;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.repository.SubscriptionMeasureDataService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkari.service.UserDetailsService;
import org.motechproject.nms.kilkari.web.SubscriptionController;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.repository.CircleDataService;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.repository.OperatorDataService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class SubscriptionControllerIT extends BasePaxIT {

    @Inject
    private CircleService circleService;

    @Inject
    private OperatorService operatorService;

    @Inject
    private SubscriberService subscriberService;

    @Inject
    private SubscriberDataService subscriberDataService;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private CircleDataService circleDataService;

    @Inject
    private OperatorDataService operatorDataService;

    @Inject
    private SubscriptionDataService subscriptionDataService;

    @Inject
    private LanguageLocationCodeDataService llcDataService;

    @Inject
    private StateService stateService;

    @Inject
    private DistrictService districtService;
    
    @Inject
    private SubscriptionMeasureDataService subscriptionMeasureDataService;

    LanguageLocationCodeBuilder llcBuilder = new LanguageLocationCodeBuilder();
    SubscriptionBuilder subscriptionBuilder = new SubscriptionBuilder();
    LocationBuilder locationBuilder = new LocationBuilder();

    @After
    @Before
    public void setUp() {
        subscriptionMeasureDataService.deleteAll();
        subscriptionDataService.deleteAll();
        subscriberDataService.deleteAll();
        llcDataService.deleteAll();
        stateService.deleteAll();
        circleDataService.deleteAll();
        operatorDataService.deleteAll();
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenInvalidCallingNumber()  {
        preSetUp();
        SubscriptionController subscriptionController = new SubscriptionController(userDetailsService, subscriptionService);
        SubscriptionCreateApiRequest apiRequest = subscriptionBuilder.buildSubscriptionApiRequest("123456780","operatorCode","circleCode","testCallId",29,"48WeeksPack");
        try {
            subscriptionController.createSubscription(apiRequest);

        } catch (DataValidationException | NmsInternalServerError e) {
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
    }

    @Test
    public void shouldCreateSubscriberAndSubscriptionWithBeneficiaryTypeChild()  {
        preSetUp();
        SubscriptionController subscriptionController = new SubscriptionController(userDetailsService, subscriptionService);
        SubscriptionCreateApiRequest apiRequest = subscriptionBuilder.buildSubscriptionApiRequest("1234567890","operatorCode","circleCode","111111111111111",29,"48WeeksPack");

        try {
            subscriptionController.createSubscription(apiRequest);

        } catch (DataValidationException | NmsInternalServerError e) {
            e.printStackTrace();
        }
        Subscriber subscriber = subscriberDataService.findRecordByMsisdnMotherMctsIdChildMctsIdStateCodeAndBeneficiaryType("1234567890",null, null, null, BeneficiaryType.CHILD);
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus("1234567890", SubscriptionPack.PACK_48_WEEKS, Status.PENDING_ACTIVATION);
        Assert.assertNotNull(subscriber);
        Assert.assertEquals(BeneficiaryType.CHILD,subscriber.getBeneficiaryType());
        Assert.assertEquals("1234567890", subscriber.getMsisdn());
        Assert.assertEquals(null, subscriber.getDeactivationReason());
        Assert.assertNotNull(subscription);
    }

    @Test
    public void shouldCreateSubscriberAndSubscriptionWithBeneficiaryTypeMother()  {
        preSetUp();
        SubscriptionController subscriptionController = new SubscriptionController(userDetailsService, subscriptionService);
        SubscriptionCreateApiRequest apiRequest = subscriptionBuilder.buildSubscriptionApiRequest("1234567890","operatorCode","circleCode","111111111111111",29,"72WeeksPack");

        try {
            subscriptionController.createSubscription(apiRequest);

        } catch (DataValidationException | NmsInternalServerError e) {
            e.printStackTrace();
        }
        Subscriber subscriber = subscriberDataService.findRecordByMsisdnMotherMctsIdChildMctsIdStateCodeAndBeneficiaryType("1234567890",null, null, null, BeneficiaryType.MOTHER);
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus("1234567890", SubscriptionPack.PACK_72_WEEKS, Status.PENDING_ACTIVATION);
        Assert.assertNotNull(subscriber);
        Assert.assertEquals(BeneficiaryType.MOTHER,subscriber.getBeneficiaryType());
        Assert.assertEquals("1234567890", subscriber.getMsisdn());
        Assert.assertEquals(null, subscriber.getDeactivationReason());
        Assert.assertNotNull(subscription);
    }

    @Test
    public void shouldDeactivateSubscription()  {
        preSetUp();
        SubscriptionController subscriptionController = new SubscriptionController(userDetailsService, subscriptionService);
        SubscriptionCreateApiRequest apiRequest = subscriptionBuilder.buildSubscriptionApiRequest("1234567890","operatorCode","circleCode","testCallId",29,"48WeeksPack");

        subscriberService.create(subscriptionBuilder.buildSubscriber("1234567890", 29, BeneficiaryType.CHILD));
        Subscription subscription = subscriptionDataService.create(subscriptionBuilder.buildSubscription("1234567890", Channel.IVR, Status.PENDING_ACTIVATION));
        try {
            subscriptionController.deactivateSubscription(
                    subscriptionBuilder.buildSubscriptionDeactivateApiRequest("1234567890", "operatorCode", "circleCode", subscription.getId(), "45564654668"));

        } catch (DataValidationException e) {
            e.printStackTrace();
        }
        Subscription dbSubscription = subscriptionDataService.findById(subscription.getId());
        Assert.assertNotNull(subscriberDataService.findRecordByMsisdnMotherMctsIdChildMctsIdStateCodeAndBeneficiaryType("1234567890",null, null, null, BeneficiaryType.CHILD));
        Assert.assertNotNull(dbSubscription);
        Assert.assertEquals(dbSubscription.getStatus(), Status.DEACTIVATED);
    }

    public void preSetUp() {
        //create circle with code "testCode"
        Circle circle = llcBuilder.buildCircle(123,"circleCode", "test");
        circleService.create(circle);

        Operator operator = llcBuilder.buildOperator("operatorCode","testOperator");
        operatorService.create(operator);

        //create state with stateCode "1"
        State state = locationBuilder.buildState(1L);
        state = stateService.create(state);

        //create district with districtCode "1" and stateCode "1"
        District district = locationBuilder.buildDistrict(1L, 1L);
        state.getDistrict().add(district);
        stateService.update(state);

        district = districtService.findDistrictByParentCode(1L, 1L);

        //create LanguageLocationCodeCsv record with circleCode "testCode",
        // districtCode "1" and stateCode "1"
        LanguageLocationCode record = llcBuilder.buildLLCCode(state, circle, district, 29);
        llcDataService.create(record);
    }


}
