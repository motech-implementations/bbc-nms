package org.motechproject.nms.kilkari.it.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkari.builder.LanguageLocationCodeBuilder;
import org.motechproject.nms.kilkari.builder.SubscriptionBuilder;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.dto.request.SubscriptionCreateApiRequest;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkari.service.UserDetailsService;
import org.motechproject.nms.kilkari.web.SubscriptionController;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.repository.CircleDataService;
import org.motechproject.nms.masterdata.repository.OperatorDataService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.List;

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

    LanguageLocationCodeBuilder llcBuilder = new LanguageLocationCodeBuilder();
    SubscriptionBuilder subscriptionBuilder = new SubscriptionBuilder();

    @Before
    public void setUp() {
        circleDataService.deleteAll();
        operatorDataService.deleteAll();
        subscriberDataService.deleteAll();
        subscriberDataService.deleteAll();
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenInvalidCallingNumber()  {

        preSetUp();
        SubscriptionController subscriptionController = new SubscriptionController(userDetailsService, subscriptionService);
        SubscriptionCreateApiRequest apiRequest = subscriptionBuilder.buildSubscriptionApiRequest("123456780","operatorCode","circleCode","testCallId",29,"PACK_48_WEEKS");
        //subscriber = apiRequest.toSubscriber();
        //subscriberService.create(subscriber);

        try {
            subscriptionController.createSubscription(apiRequest);

        } catch (DataValidationException e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
    }

    @Test
    public void shouldCreateSubscriberWithBeneficiaryTypeChild()  {

        preSetUp();
        List<Subscriber> subscriberList = null;
        BeneficiaryType beneficiaryType;
        SubscriptionController subscriptionController = new SubscriptionController(userDetailsService, subscriptionService);
        SubscriptionCreateApiRequest apiRequest = subscriptionBuilder.buildSubscriptionApiRequest("1234567890","operatorCode","circleCode","testCallId",29,"PACK_48_WEEKS");

        try {
            subscriptionController.createSubscription(apiRequest);

        } catch (DataValidationException e) {

        }
        Subscriber subscriber1 = subscriberDataService.findRecordByMsisdnAndChildMctsId("1234567890",null);
        beneficiaryType = BeneficiaryType.CHILD;
        Assert.assertNotNull(subscriber1);
        Assert.assertEquals(beneficiaryType,subscriber1.getBeneficiaryType());
    }

    @Test
    public void shouldCreateSubscriber()  {

        preSetUp();
        SubscriptionController subscriptionController = new SubscriptionController(userDetailsService, subscriptionService);
        SubscriptionCreateApiRequest apiRequest = subscriptionBuilder.buildSubscriptionApiRequest("1234567890","operatorCode","circleCode","testCallId",29,"PACK_48_WEEKS");

        try {
            subscriptionController.createSubscription(apiRequest);

        } catch (DataValidationException e) {

        }
        Assert.assertNotNull(subscriberDataService.findRecordByMsisdnAndChildMctsId("1234567890",null));
    }
    public void preSetUp() {
        //create circle with code "testCode"
        Circle circle = new Circle();
        circle = llcBuilder.buildCircle(123,"circleCode");
        circle.setName("testCircle");
        circleService.create(circle);

        Operator operator = new Operator();
        operator = llcBuilder.buildOperator("operatorCode","teatOperator");
        operatorService.create(operator);
    }


}
