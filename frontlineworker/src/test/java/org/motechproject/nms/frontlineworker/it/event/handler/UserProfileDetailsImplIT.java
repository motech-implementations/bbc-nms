package org.motechproject.nms.frontlineworker.it.event.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerCsvService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.frontlineworker.service.impl.UserProfileDetailsImpl;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * This class models the IT of UserProfileDetails
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class UserProfileDetailsImplIT extends BasePaxIT {
    @Inject
    private StateService stateService;

    @Inject
    private DistrictService districtService;

    @Inject
    private CircleService circleService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private OperatorService operatorService;


    @Inject
    private UserProfileDetailsService userProfileDetailsService;

    @Inject
    private FrontLineWorkerService frontLineWorkerService;

    @Inject
    private FrontLineWorkerCsvService frontLineWorkerCsvService;

    private UserProfileDetailsImpl userProfileDetailsImpl;

    private static boolean setUpIsDone = false;

    private State state = null ;

    private District district = null;

    private Circle circle = null;

    private LanguageLocationCode languageLocationCode = null;

    private Operator operator = null;

    private TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() {

        if (!setUpIsDone) {
            System.out.println("");
/*            state = new State();
            district = new District();
            circle = new Circle();*/
            state = testHelper.createState();
            stateService.create(state);
            assertNotNull(state);

            district = testHelper.createDistrict();
            State stateData = stateService.findRecordByStateCode(district.getStateCode());
            stateData.getDistrict().add(district);
            stateService.update(stateData);
            assertNotNull(district);


            circle = testHelper.createCircle();
            circleService.create(circle);
            assertNotNull(circle);

            languageLocationCode = testHelper.createLanguageLocationCode();

            languageLocationCode.setDistrict(district);
            languageLocationCode.setState(stateData);
            languageLocationCode.setCircle(circle);
            languageLocationCode.setCircleCode(circle.getCode());
            languageLocationCode.setDistrictCode(district.getDistrictCode());
            languageLocationCode.setStateCode(stateData.getStateCode());


            languageLocationCodeService.create(languageLocationCode);
            assertNotNull(languageLocationCode);

            operator = testHelper.createOperator();
            operatorService.create(operator);
            assertNotNull(operator);

            FrontLineWorker frontLineWorker;
            FrontLineWorker frontLineWorkerdb;

            // Record 1 defaultLanguageLocationCodeId is null And Status is ACTIVE

            frontLineWorker = new FrontLineWorker(150L,"1234512345", "Rashi", Designation.USHA,
                              123L, 12L, stateData, district, null, null, null,
                              null, null, null, null, Status.ACTIVE, 123, null);

            frontLineWorker.setCreator("Etasha");
            frontLineWorker.setModifiedBy("Etasha");
            frontLineWorker.setOwner("Etasha");

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234512345");
            assertNotNull(frontLineWorkerdb);

           /* // Record 2 LanguageLocationCodeId is null And Status is ACTIVE

            frontLineWorker = new FrontLineWorker(2L,"1234512345", "Rashi", Designation.USHA,
                              123L, 12L, state, district, null, null, null,
                              null, null, null, null, Status.ACTIVE, null, 123);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234512345");
            assertNotNull(frontLineWorkerdb);

            // Record 3 Status is INACTIVE AND defaultLanguageLocationCodeId is null

            frontLineWorker = new FrontLineWorker(1L,"1234512345", "Rashi", Designation.USHA,
                              123L, 12L, state, district, null, null, null,
                              null, null, null, null, Status.INACTIVE, 123, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234512345");
            assertNotNull(frontLineWorkerdb);

            // Record 4 Status is INACTIVE AND LanguageLocationCodeId is null

            frontLineWorker = new FrontLineWorker(1L,"1234512345", "Rashi", Designation.USHA,
                    123L, 12L, state, district, null, null, null,
                    null, null, null, null, Status.INACTIVE, null, 123);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234512345");
            assertNotNull(frontLineWorkerdb);

            // Record 5 PhoneNo is Absent

            frontLineWorker = new FrontLineWorker(1L, null, "Rashi", Designation.USHA,
                              123L, 12L, state, district, null, null, null,
                              null, null, null, null, Status.INACTIVE, 123, 123);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb=frontLineWorkerService.getFlwByFlwIdAndStateId(1L, 12L);
            assertNotNull(frontLineWorkerdb);

            // Record 6 status is ANONYMOUS and defaultLanguageLocationCodeId is NULL.

            frontLineWorker = new FrontLineWorker(1L,"1234512345", "Rashi", Designation.USHA,
                              123L, 12L, state, district, null, null, null,
                              null, null, null, null, Status.ANONYMOUS, 123, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234512345");
            assertNotNull(frontLineWorkerdb);

            // Record 7 status is ANONYMOUS and LanguageLocationCodeId is NULL

            frontLineWorker = new FrontLineWorker(1L,"1234512345", "Rashi", Designation.USHA,
                              123L, 12L, state, district, null, null, null,
                              null, null, null, null, Status.ANONYMOUS, null, 123);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234512345");
            assertNotNull(frontLineWorkerdb);

            // Record 8 status is INVALID

            frontLineWorker = new FrontLineWorker(1L,"9876543210", "Rashi", Designation.USHA,
                    123L, 12L, state, district, null, null, null,
                    null, null, null, null, Status.INVALID, 123, 123);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("9876543210");
            assertNotNull(frontLineWorkerdb);*/


        }
        // do the setup
        setUpIsDone = true;

    }


    @Test
    public void testUserProfileDetailsAll() throws DataValidationException {

        UserProfile userProfile;

        // Record 1 defaultLanguageLocationCodeId is null

        userProfile = userProfileDetailsService.processUserDetails("1234512345", "12", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("12", userProfile.getCircle());
        assertEquals("1234512345", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(null == userProfile.getDefaultLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

       /* // Record 2 LanguageLocationCodeId is null

        userProfile = userProfileDetailsService.processUserDetails("1234512345", "12", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("12", userProfile.getCircle());
        assertEquals("1234512345", userProfile.getMsisdn());
        assertTrue(null == userProfile.getLanguageLocationCode());
        assertTrue(123 == userProfile.getDefaultLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertTrue(12L == userProfile.getNmsId());
        assertEquals(true, userProfile.isDefaultLanguageLocationCode());

        // Record 3 status is INACTIVE

        userProfile = userProfileDetailsService.processUserDetails("1234512345", "12", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("12", userProfile.getCircle());
        assertEquals("1234512345", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(null == userProfile.getDefaultLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertTrue(12L == userProfile.getNmsId());
        assertEquals(true, userProfile.isDefaultLanguageLocationCode());

        // Record 4 Status is INACTIVE AND LanguageLocationCodeId is null

        userProfile = userProfileDetailsService.processUserDetails("1234512345", "12", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("12", userProfile.getCircle());
        assertEquals("1234512345", userProfile.getMsisdn());
        assertTrue(null == userProfile.getLanguageLocationCode());
        assertTrue(123 == userProfile.getDefaultLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertTrue(12L == userProfile.getNmsId());
        assertEquals(true, userProfile.isDefaultLanguageLocationCode());

        // Record 5 PhoneNo is Absent

        userProfile = userProfileDetailsService.processUserDetails(null, "12", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertNull(userProfile);

        // Record 6 status is ANONYMOUS and defaultLanguageLocationCodeId is NULL.

        userProfile = userProfileDetailsService.processUserDetails("1234512345", "12", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("12", userProfile.getCircle());
        assertEquals("1234512345", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(null == userProfile.getDefaultLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertTrue(12L == userProfile.getNmsId());
        assertEquals(true, userProfile.isDefaultLanguageLocationCode());

        // Record 7 Status is ANONYMOUS and LanguageLocationCodeId is NULL

        userProfile = userProfileDetailsService.processUserDetails("1234512345", "12", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("12", userProfile.getCircle());
        assertEquals("1234512345", userProfile.getMsisdn());
        assertTrue(null == userProfile.getLanguageLocationCode());
        assertTrue(123 == userProfile.getDefaultLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertTrue(12L == userProfile.getNmsId());
        assertEquals(true, userProfile.isDefaultLanguageLocationCode());

        // Record 8 status is INVALID

        userProfile = userProfileDetailsService.processUserDetails("9876543210", "12", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("12", userProfile.getCircle());
        assertEquals("9876543210", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(123 == userProfile.getDefaultLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertTrue(12L == userProfile.getNmsId());
        assertEquals(true, userProfile.isDefaultLanguageLocationCode());

        // Record 9

        //userProfile = userProfileDetailsService.processUserDetails("1234512345", "12", "123", ServicesUsingFrontLineWorker.MOBILEKUNJI);
        */


    }

}


/*
    public FrontLineWorker(Long flwId, String contactNo, String name, Designation designation, Long operatorId,
                           Long stateCode, State stateId, District districtId, Taluka talukaId,
                           HealthBlock healthBlockId, HealthFacility healthFacilityId, HealthSubFacility
            healthSubFacilityId, Village villageId, String ashaNumber, String adhaarNumber,
                           Status status, Integer languageLocationCodeId, Integer defaultLanguageLocationCodeId)

 getUserDetailsByCircle(String msisdn,String circleCode, ServicesUsingFrontLineWorker service)

*/
