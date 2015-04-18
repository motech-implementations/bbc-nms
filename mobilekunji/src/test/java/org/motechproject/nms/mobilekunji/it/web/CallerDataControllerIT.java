package org.motechproject.nms.mobilekunji.it.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.WhiteListUsers;
import org.motechproject.nms.frontlineworker.exception.FlwNotInWhiteListException;
import org.motechproject.nms.frontlineworker.exception.ServiceNotDeployedException;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.WhiteListUsersService;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.CallDetail;
import org.motechproject.nms.mobilekunji.domain.Configuration;
import org.motechproject.nms.mobilekunji.domain.FlwDetail;
import org.motechproject.nms.mobilekunji.dto.LanguageLocationCodeApiRequest;
import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiRequest;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.*;
import org.motechproject.nms.mobilekunji.web.CallerDataController;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * This Test class is used to test CallerDataController Functionality.
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CallerDataControllerIT extends BasePaxIT {

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private SaveCallDetailsService saveCallDetailsService;

    @Inject
    private OperatorService operatorService;

    @Inject
    private CircleService circleService;

    @Inject
    private DistrictService districtService;

    @Inject
    private StateService stateService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private FrontLineWorkerService frontLineWorkerService;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private FlwDetailService flwDetailService;

    @Inject
    private CallDetailService callDetailService;

    @Inject
    private WhiteListUsersService whiteListUsersService;


    private CallerDataController controller;

    @Before
    public void setUp() {
        controller = new CallerDataController(userDetailsService, saveCallDetailsService);
    }

    @Test
    public void testDataServiceInstance() throws Exception {
        assertNotNull(userDetailsService);
        assertNotNull(saveCallDetailsService);
        assertNotNull(circleService);
        assertNotNull(operatorService);
        assertNotNull(whiteListUsersService);
        assertNotNull(configurationService);
        assertNotNull(languageLocationCodeService);
    }

    @Test
    public void testController() throws DataValidationException, NmsInternalServerError,FlwNotInWhiteListException,ServiceNotDeployedException {

        State stateData = TestHelper.getStateData();
        District districtData = TestHelper.getDistrictData();
        stateData.getDistrict().add(districtData);
        stateService.create(stateData);

        Operator operatorData = TestHelper.getOperatorData();
        operatorService.create(operatorData);

        Circle circleData = TestHelper.getCircleData();
        circleService.create(circleData);


        LanguageLocationCode languageLocationCodeData = TestHelper.getLanguageLocationCode();
        languageLocationCodeData.setState(stateData);
        languageLocationCodeData.setDistrict(districtData);
        languageLocationCodeData.setCircle(circleData);
        languageLocationCodeService.create(languageLocationCodeData);

        WhiteListUsers whilteListUserData = TestHelper.getWhiteListUserData();
        whiteListUsersService.createWhiteListUsers(whilteListUserData);


         /*------------------This case is used to Test getUserDetails------------------------- */

        UserDetailApiResponse userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());


        //For State Level Capping Type
        Configuration configurationData = configurationService.getConfiguration();
        configurationData.setCappingType(ConfigurationConstants.DEFAULT_STATE_CAPPING_TYPE);
        configurationService.updateConfiguration(configurationData);

        //This call is for State Capping Type
        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());

        //For Null Capping Value
        stateData.setMkCapping(null);
        stateService.update(stateData);

        //This call is for State Capping Type with Null Capping Value
        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());


        //For Default National Cappings
        configurationData = configurationService.getConfiguration();
        configurationData.setCappingType(ConfigurationConstants.DEFAULT_NATIONAL_CAPPING_TYPE);
        configurationService.updateConfiguration(configurationData);

        //This call is for Default National Capping Type
        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());


        FrontLineWorker frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9810179788");

        FlwDetail flwDetail = flwDetailService.findFlwDetailByNmsFlwId(frontLineWorker.getId());
        flwDetail.setLastAccessDate(flwDetail.getLastAccessDate().plusYears(2));
        flwDetailService.update(flwDetail);

        //This call is for Next Time Access Date
        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());

        //For Null Access Date
        flwDetail = flwDetailService.findFlwDetailByNmsFlwId(frontLineWorker.getId());
        flwDetail.setLastAccessDate(null);
        flwDetailService.update(flwDetail);

        //This call is for Null Access Date
        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());

        assertNotNull(frontLineWorker);
        assertNotNull(userDetailApiResponse);
        assertTrue(userDetailApiResponse.getCircle().equals(circleData.getCode()));
       /* assertTrue(userDetailApiResponse.getLanguageLocationCode() == languageLocationCodeData.getLanguageLocationCode());*/
        assertTrue(userDetailApiResponse.getCurrentUsageInPulses() == ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
        assertFalse(userDetailApiResponse.getWelcomePromptFlag());

        //For LanguageLocationCode is Null
        circleService.create(TestHelper.getInvalidCircleData());
        userDetailApiResponse = controller.getUserDetails("9837241545", "AL", "99", "111111111111111", TestHelper.getHttpRequest());

        assertNotNull(userDetailApiResponse.getCircle().equals("99"));
        assertNull(userDetailApiResponse.getLanguageLocationCode());
        assertTrue(userDetailApiResponse.getDefaultLanguageLocationCode().equals("1"));
        assertFalse(userDetailApiResponse.getWelcomePromptFlag());
        assertTrue(userDetailApiResponse.getCurrentUsageInPulses() == ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);



        /*------------------This case is used to Test SaveCallDetail------------------------- */

        SaveCallDetailApiRequest saveCallDetailApiRequest = TestHelper.getSaveCallDetailApiRequest();

        controller.saveCallDetails(saveCallDetailApiRequest, TestHelper.getHttpRequest());

        saveCallDetailApiRequest = TestHelper.getSaveCallDetailApiRequest();
        saveCallDetailApiRequest.setContent(null);

        //This test case is used to Check Null Content in SaveCallDetail
        controller.saveCallDetails(saveCallDetailApiRequest, TestHelper.getHttpRequest());

        FrontLineWorker flwWorker = frontLineWorkerService.getFlwBycontactNo("9810179788");
        CallDetail callDetail = callDetailService.findCallDetailByCallingNumber("9810179788");

        assertNotNull(flwWorker);
        assertNotNull(callDetail);

        /*------------------This case is used to Test UpdateLanguageLocationCode------------------------- */

        //Update Language Location Code
        flwWorker = frontLineWorkerService.getFlwBycontactNo("9810179788");
        flwWorker.setLanguageLocationCodeId("33");
        frontLineWorkerService.updateFrontLineWorker(flwWorker);

        LanguageLocationCodeApiRequest languageLocationCodeApiRequest = TestHelper.getLanguageLocationCodeRequest();

        controller.setLanguageLocationCode(languageLocationCodeApiRequest, TestHelper.getHttpRequest());

        flwWorker = frontLineWorkerService.getFlwBycontactNo("9810179788");
        assertNotNull(flwWorker);
        assertTrue(flwWorker.getLanguageLocationCodeId().equals("29"));


      /*  flwDetail.setLastAccessDate(flwDetail.getLastAccessDate().plusYears(2));
        flwDetailService.update(flwDetail);
        configurationService.updateConfiguration(configurationData);

        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());

        //For State Level Capping Type and Next Date Condition
        configurationData.setCappingType(ConfigurationConstants.DEFAULT_STATE_CAPPING_TYPE);
        configurationService.updateConfiguration(configurationData);

       */



       /* flwDetail = flwDetailService.findFlwDetailByMsisdn("9810179788");
        flwDetail.setLastAccessDate(flwDetail.getLastAccessDate().plusMonths(2));
        flwDetailService.update(flwDetail);

        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());

        //For Null Access Date
        flwDetail = flwDetailService.findFlwDetailByMsisdn("9810179788");
        flwDetail.setLastAccessDate(null);
        flwDetailService.update(flwDetail);

        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());

        assertNotNull(userDetailApiResponse);
        assertTrue(userDetailApiResponse.getCircle().equals(circleData.getCode()));
        assertTrue(userDetailApiResponse.getLanguageLocationCode() == languageLocationCodeData.getLanguageLocationCode());
        assertTrue(userDetailApiResponse.getCurrentUsageInPulses() == ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
        assertFalse(userDetailApiResponse.getWelcomePromptFlag());

        //For LanguageLocationCode is Null
        circleService.create(TestHelper.getInvalidCircleData());
        userDetailApiResponse = controller.getUserDetails("9837241545", "AL", "99", "111111111111111", TestHelper.getHttpRequest());

        assertNotNull(userDetailApiResponse.getCircle().equals("99"));
        assertNull(userDetailApiResponse.getLanguageLocationCode());
        assertTrue(userDetailApiResponse.getDefaultLanguageLocationCode().equals("1"));
        assertFalse(userDetailApiResponse.getWelcomePromptFlag());
        assertTrue(userDetailApiResponse.getCurrentUsageInPulses() == ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);


        //Update Language Location Code
        FrontLineWorker flwWorker = frontLineWorkerService.getFlwBycontactNo("9810179788");
        flwWorker.setLanguageLocationCodeId("33");
        frontLineWorkerService.updateFrontLineWorker(flwWorker);
        LanguageLocationCodeApiRequest languageLocationCodeApiRequest = TestHelper.getLanguageLocationCodeRequest();

        controller.setLanguageLocationCode(languageLocationCodeApiRequest, TestHelper.getHttpRequest());
        flwWorker = frontLineWorkerService.getFlwBycontactNo("9810179788");
        assertNotNull(flwWorker);
        assertTrue(flwWorker.getLanguageLocationCodeId().equals("29"));


        This case is added to check whether FlwDetail is Updated or Not

        frontLineWorkerService.deleteFrontLineWorker(flwWorker);

        userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111", TestHelper.getHttpRequest());
        assertNotNull(userDetailApiResponse);


        This case is used to Test SaveCallDetail

        SaveCallDetailApiRequest saveCallDetailApiRequest = TestHelper.getSaveCallDetailApiRequest();

        controller.saveCallDetails(saveCallDetailApiRequest, TestHelper.getHttpRequest());

        flwDetail = flwDetailService.findFlwDetailByMsisdn("9810179788");
        CallDetail callDetail = callDetailService.findCallDetailByNmsId(flwDetail.getNmsFlwId());

        assertNotNull(flwDetail);
        assertNotNull(callDetail);

         This case is for Calling Number not existing in FlwDetail
        * In this case InvalidDataException Occurs

        try {
            saveCallDetailApiRequest.setCallingNumber("8888888888");
            controller.saveCallDetails(saveCallDetailApiRequest, TestHelper.getHttpRequest());
        } catch (DataValidationException e) {
            assertEquals(e.getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }*/
    }

}
