package org.motechproject.nms.kilkari.ut;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.nms.kilkari.builder.LanguageLocationCodeBuilder;
import org.motechproject.nms.kilkari.builder.LocationBuilder;
import org.motechproject.nms.kilkari.builder.SubscriptionBuilder;
import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.dto.response.SubscriberDetailApiResponse;
import org.motechproject.nms.kilkari.service.ActiveSubscriptionCountService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkari.service.impl.UserDetailsServiceImpl;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;

public class UserDetailsServiceImplTest {
    @Mock
    private SubscriberService subscriberService;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private LanguageLocationCodeService llcService;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private ActiveSubscriptionCountService activeSubscriptionCountService;

    @Mock
    private CircleService circleService;

    @Mock
    private OperatorService operatorService;



    @InjectMocks
    private UserDetailsServiceImpl userDetailsService  = new UserDetailsServiceImpl();

    private String msisdn = "1234567890";
    private Subscriber subscriber;
    private List<SubscriptionPack> activePackList = new ArrayList<>();
    SubscriptionBuilder builder = new SubscriptionBuilder();
    LocationBuilder locationBuilder = new LocationBuilder();
    LanguageLocationCodeBuilder llcBuilder = new LanguageLocationCodeBuilder();

    @Test
    public void shouldGetSubscriberDetailsWhenLlcCodeIsPresentInSubscriberDetail() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //set the subscriber details
        activePackList.add(SubscriptionPack.PACK_48_WEEKS);
        activePackList.add(SubscriptionPack.PACK_72_WEEKS);
        subscriber = builder.buildSubscriber(msisdn, 123, null, null,BeneficiaryType.CHILD);

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(activePackList);
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertTrue(response.getLanguageLocationCode() == 123);
            Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertNull(response.getDefaultLanguageLocationCode());
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void shouldGetAllSubscriberDetailsWhenLlcCodeIsDeterminedByStateAndDistrict() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //set the subscriber details
        activePackList.add(SubscriptionPack.PACK_48_WEEKS);
        activePackList.add(SubscriptionPack.PACK_72_WEEKS);

        //create a subscriber with languageLocationCode, state and district.
        subscriber = builder.buildSubscriber(msisdn, null, locationBuilder.buildState(1L), locationBuilder.buildDistrict(1L, 1L) ,BeneficiaryType.CHILD);

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(activePackList);
        when(llcService.getLanguageLocationCodeByLocationCode(1L, 1L)).thenReturn(789);
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertTrue(response.getLanguageLocationCode() == 789);
            Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertNull(response.getDefaultLanguageLocationCode());
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void shouldGetAllSubscriberDetailsWhenLlcCodeIsDeterminedByCircleCode() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //set the subscriber details
        activePackList.add(SubscriptionPack.PACK_48_WEEKS);
        activePackList.add(SubscriptionPack.PACK_72_WEEKS);

        //create a subscriber with languageLocationCode.
        subscriber = builder.buildSubscriber(msisdn, null, null, null, BeneficiaryType.CHILD);

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(activePackList);
        when(llcService.getLanguageLocationCodeByLocationCode(1L, 1L)).thenReturn(null);
        //when(llcService.getLanguageLocationCodeByCircleCode("AP")).thenReturn(333);
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertNull(response.getDefaultLanguageLocationCode());
            Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertNull(response.getLanguageLocationCode());
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertTrue(err.getCause() instanceof Exception);
        }
    }

    @Test
    public void shouldGetAllSubscriberDetailsWhenDefaultLLCIsDeterminedByCircleCode() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //set the subscriber details
        activePackList.add(SubscriptionPack.PACK_48_WEEKS);
        activePackList.add(SubscriptionPack.PACK_72_WEEKS);

        //create a subscriber with languageLocationCode.
        subscriber = builder.buildSubscriber(msisdn, null, null, null, BeneficiaryType.CHILD);

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(activePackList);
        when(llcService.getLanguageLocationCodeByLocationCode(1L, 1L)).thenReturn(null);
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertNull(response.getLanguageLocationCode());
            Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertNull(response.getDefaultLanguageLocationCode());
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertTrue(err.getCause() instanceof Exception);
        }
    }

    @Test
    public void shouldGetAllSubscriberDetailsWhenDefaultLLCIsDeterminedByNationalDefaultLLC() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //set the subscriber details
        activePackList.add(SubscriptionPack.PACK_48_WEEKS);
        activePackList.add(SubscriptionPack.PACK_72_WEEKS);

        //create a subscriber with languageLocationCode.
        subscriber = builder.buildSubscriber(msisdn, null, null, null, BeneficiaryType.CHILD);

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(activePackList);
        when(llcService.getLanguageLocationCodeByLocationCode(1L, 1L)).thenReturn(null);
        when(llcService.getLanguageLocationCodeByCircleCode("AP")).thenReturn(null);
        //when(llcService.getDefaultLanguageLocationCodeByCircleCode("AP")).thenReturn(null);
        //when(configurationService.getConfiguration()).thenReturn(createConfiguration(555));
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertNull(response.getLanguageLocationCode());
            Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertNull(response.getDefaultLanguageLocationCode());
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertTrue(err.getCause() instanceof Exception);
        }
    }

    @Test
    public void  shouldGetLLCByCircleCodeWithNullSubscriber() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(null);
        when(llcService.getLanguageLocationCodeByCircleCode("AP")).thenReturn(123);
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertTrue(response.getLanguageLocationCode() == 123);
            Assert.assertNull(response.getSubscriptionPackList());
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertNull(response.getDefaultLanguageLocationCode());
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void  shouldDetermineDefaultLLCbyCircleCodeWithNullSubscriber() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(null);
        when(llcService.getLanguageLocationCodeByCircleCode("AP")).thenReturn(null);
        when(llcService.getDefaultLanguageLocationCodeByCircleCode("AP")).thenReturn(555);
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertNull(response.getLanguageLocationCode());
            Assert.assertNull(response.getSubscriptionPackList());
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertEquals(response.getDefaultLanguageLocationCode(), Integer.valueOf(555));
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void  shouldgetDefaultLLCByNationalDefaultLLCWithNullSubscriber() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(null);
        when(llcService.getLanguageLocationCodeByCircleCode("AP")).thenReturn(null);
        when(llcService.getDefaultLanguageLocationCodeByCircleCode("AP")).thenReturn(null);
        when(configurationService.getConfiguration()).thenReturn(createConfiguration(555));
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertNull(response.getLanguageLocationCode());
            Assert.assertNull(response.getSubscriptionPackList());
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertEquals(response.getDefaultLanguageLocationCode(), Integer.valueOf(555));
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void shouldGetSubscriberDetailsWhenActivePackListIsNull() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //set the subscriber details
        subscriber = builder.buildSubscriber(msisdn, 123, null, null, BeneficiaryType.CHILD);

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(null);
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", null);

            //Do Assertions.
            Assert.assertTrue(response.getLanguageLocationCode() == 123);
            Assert.assertNull(response.getSubscriptionPackList());
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertNull(response.getDefaultLanguageLocationCode());
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void shouldGetSubscriberDetailsWhenValidCircleCodeAndOperatorCode() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //set the subscriber details
        activePackList.add(SubscriptionPack.PACK_48_WEEKS);
        activePackList.add(SubscriptionPack.PACK_72_WEEKS);
        subscriber = builder.buildSubscriber(msisdn, 123, null, null, BeneficiaryType.CHILD);

        //Stub the service methods
        when(subscriberService.getSubscriberByMsisdn(msisdn)).thenReturn(subscriber);
        when(subscriptionService.getActiveSubscriptionPacksByMsisdn(msisdn)).thenReturn(activePackList);
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));
        when(operatorService.getRecordByCode("OC")).thenReturn(llcBuilder.buildOperator("OC", "XYZ"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", "OC");

            //Do Assertions.
            Assert.assertTrue(response.getLanguageLocationCode() == 123);
            Assert.assertEquals(response.getSubscriptionPackList(), activePackList);
            Assert.assertEquals(response.getCircle(), "AP");
            Assert.assertNull(response.getDefaultLanguageLocationCode());
        } catch (DataValidationException ex) {
            Assert.assertNull(response);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenInvalidCircleCodeAndOperatorCode() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //Stub the service methods
        when(circleService.getRecordByCode("AP")).thenReturn(null);
        when(operatorService.getRecordByCode("OC")).thenReturn(null);

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", "OC");

        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)ex).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenInvalidCircleCode() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //Stub the service methods
        when(circleService.getRecordByCode("AP")).thenReturn(null);
        when(operatorService.getRecordByCode("OC")).thenReturn(llcBuilder.buildOperator("OC", "XYZ"));

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", "OC");

        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)ex).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenInvalidOperatorCode() {
        initMocks(this);
        SubscriberDetailApiResponse response = new SubscriberDetailApiResponse();

        //Stub the service methods
        when(circleService.getRecordByCode("AP")).thenReturn(llcBuilder.buildCircle(123, "AP", "test"));
        when(operatorService.getRecordByCode("OC")).thenReturn(null);

        //invoke the userDetailService.
        try {
            response = userDetailsService.getSubscriberDetails(msisdn, "AP", "OC");

        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)ex).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        } catch (Exception err) {
            Assert.assertNull(response);
        }
    }

    private Configuration createConfiguration(Integer nationLLCCode) {
        Configuration conf = new Configuration();
        conf.setNationalLanguageLocationCode(nationLLCCode);
        return conf;
    }
}
