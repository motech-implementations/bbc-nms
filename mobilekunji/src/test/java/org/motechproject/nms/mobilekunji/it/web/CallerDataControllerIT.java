package org.motechproject.nms.mobilekunji.it.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.UserDetailsService;
import org.motechproject.nms.mobilekunji.web.CallerDataController;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by abhishek on 25/3/15.
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
    private ConfigurationService configurationService;

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
        assertNotNull(configurationService);
        assertNotNull(languageLocationCodeService);
    }

    @Test
    public void testGetUserDetails() throws DataValidationException {

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

        UserDetailApiResponse userDetailApiResponse = controller.getUserDetails("9810179788", "AL", "DL", "111111111111111");
        assertNotNull(userDetailApiResponse);
        assertTrue(userDetailApiResponse.getCircle().equals(circleData.getCode()));
        assertTrue(userDetailApiResponse.getLanguageLocationCode() == languageLocationCodeData.getLanguageLocationCode());
        assertTrue(userDetailApiResponse.getCurrentUsageInPulses() == ConfigurationConstants.DEFAULT_CURRENT_USAGE_IN_PULSES);
        assertFalse(userDetailApiResponse.getWelcomePromptFlag());



        //for wrong msisdn
        UserDetailApiResponse userDetailApiResponseM1 = controller.getUserDetails("1", "AL", "DL", "111111111111111");
        //for not numeric msisdn
        UserDetailApiResponse userDetailApiResponseM2 = controller.getUserDetails("ABCKD", "AL", "DL", "111111111111111");
        //for bigger length of msisdn
        UserDetailApiResponse userDetailApiResponseM3 = controller.getUserDetails("12222222222222", "AL", "DL", "111111111111111");

        //for wrong operator
        UserDetailApiResponse userDetailApiResponseO1 = controller.getUserDetails("9810179788", "98", "DL", "111111111111111");
        //for null operator value
        UserDetailApiResponse userDetailApiResponse02 = controller.getUserDetails("9810179788", null, "DL", "111111111111111");

        //for wrong circle
        UserDetailApiResponse userDetailApiResponseC1 = controller.getUserDetails("9810179788", "AL", "8", "111111111111111");
        //for null circle code
        UserDetailApiResponse userDetailApiResponseC2 = controller.getUserDetails("9810179788", "AL", null, "111111111111111");

        //for wrong callid
        UserDetailApiResponse userDetailApiResponseCID1 = controller.getUserDetails("9810179788", "AL", "DL", "001");
        //for null callid
        UserDetailApiResponse userDetailApiResponseCID2 = controller.getUserDetails("9810179788", "AL", "DL", null);

    }




}
