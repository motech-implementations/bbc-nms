package org.motechproject.nms.frontlineworker.it.event.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.domain.WhiteListUsers;
import org.motechproject.nms.frontlineworker.exception.FlwNotInWhiteListException;
import org.motechproject.nms.frontlineworker.exception.ServiceNotDeployedException;
import org.motechproject.nms.frontlineworker.service.*;
import org.motechproject.nms.frontlineworker.service.impl.UserProfileDetailsServiceImpl;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
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
    private static boolean setUpIsDone = false;
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
    private CsvFrontLineWorkerService csvFrontLineWorkerService;
    @Inject
    private WhiteListUsersService whiteListUsersService;
    @Inject
    private CsvWhiteListUsersService csvWhiteListUsersService;

    private UserProfileDetailsServiceImpl userProfileDetailsImpl;
    private State state = null;

    private State stateData = null;

    private State stateTemp = null;

    private State stateTemp1 = null;

    private District district = null;

    private District districtTemp = null;

    private Circle circle = null;

    private LanguageLocationCode languageLocationCode = null;

    private Operator operator = null;

    private TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() {

        if (!setUpIsDone) {
            state = testHelper.createState();
            stateService.create(state);
            assertNotNull(state);

            district = testHelper.createDistrict();
            stateData = stateService.findRecordByStateCode(district.getStateCode());
            stateData.getDistrict().add(district);
            stateService.update(stateData);
            assertNotNull(district);


            circle = testHelper.createCircle();
            circleService.create(circle);
            assertNotNull(circle);

            stateTemp = testHelper.createStateTemp();
            stateService.create(stateTemp);
            assertNotNull(stateTemp);

            districtTemp = testHelper.createDistrictTemp();
            stateTemp1 = stateService.findRecordByStateCode(districtTemp.getStateCode());
            stateTemp1.getDistrict().add(districtTemp);
            stateService.update(stateTemp1);
            assertNotNull(districtTemp);

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

            WhiteListUsers whiteListUsers;
            WhiteListUsers whiteListUsersdb;

            // Record 1 LanguageLocationCodeId is not null, circleCode is Unknown And Status is ACTIVE

            frontLineWorker = new FrontLineWorker(1500L, "1234512345", "Rashi", Designation.USHA,
                    "123", 12L, stateData, district, null, null, null,
                    null, null, null, Status.ACTIVE, "LLC", ConfigurationConstants.UNKNOWN_CIRCLE, "1781781781", "1231231212");

            frontLineWorker.setCreator("Etasha");
            frontLineWorker.setModifiedBy("Etasha");
            frontLineWorker.setOwner("Etasha");

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234512345");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("1234512345");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("1234512345");
            assertNotNull(whiteListUsersdb);

            // Record 2 LanguageLocationCodeId is not null, circleCode is not Null And Status is ACTIVE

            frontLineWorker = new FrontLineWorker(1501L, "1212121212", "Rashi", Designation.ASHA,
                    "123", 12L, stateData, district, null, null, null,
                    null, null, null, Status.ACTIVE, "LLC", "circleCode", "1010910109", "1010191010");

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1212121212");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("1212121212");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("1212121212");
            assertNotNull(whiteListUsersdb);

            // Record 3 LanguageLocationCodeId is null, circleCode is Null And Status is ACTIVE

            frontLineWorker = new FrontLineWorker(1502L, "9999999999", "Rashi", Designation.ANM,
                    "123", 12L, stateData, district, null, null, null,
                    null, null, null, Status.ACTIVE, null, ConfigurationConstants.UNKNOWN_CIRCLE, null, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("9999999999");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("9999999999");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("9999999999");
            assertNotNull(whiteListUsersdb);

            // Record 4 Status is INACTIVE AND LanguageLocationCodeId is null

            frontLineWorker = new FrontLineWorker(1503L, "2121212121", "Etasha", Designation.AWW,
                    "123", 12L, stateData, district, null, null, null,
                    null, null, null, Status.INACTIVE, null, null, null, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("2121212121");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("2121212121");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("2121212121");
            assertNotNull(whiteListUsersdb);

            // Record 5 status is ANONYMOUS LanguageLocationCodeId is null, Circlecode is unknown.

            frontLineWorker = new FrontLineWorker(1505L, "1111111111", "Rashi", Designation.USHA,
                    null, null, null, null, null, null, null,
                    null, null, null, Status.ANONYMOUS, null, ConfigurationConstants.UNKNOWN_CIRCLE, null, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1111111111");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("1111111111");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("1111111111");
            assertNotNull(whiteListUsersdb);

            // Record 6 status is ANONYMOUS LanguageLocationCodeId is not null, Circlecode is not null.

            frontLineWorker = new FrontLineWorker(1507L, "1234567999", "Rashi", Designation.USHA,
                    null, null, null, null, null, null, null,
                    null, null, null, Status.ANONYMOUS, "LLC", "circleCode", "2132132134", "22222234567");

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234567999");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("1234567999");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("1234567999");
            assertNotNull(whiteListUsersdb);

            // Record 7 status is ANONYMOUS and front line worker is not present.

            whiteListUsers = new WhiteListUsers("9879879879");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("9879879879");
            assertNotNull(whiteListUsersdb);

            // Record 8 status is INVALID and LanguageLocationCode, circleCode is not Null

            frontLineWorker = new FrontLineWorker(1508L, "9876543210", "Rashi", Designation.USHA,
                    "123", 12L, stateData, district, null, null, null,
                    null, null, null, Status.INVALID, "LLC", "circleCode", null, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("9876543210");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("9876543210");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("9876543210");
            assertNotNull(whiteListUsersdb);

            // Record 9  status is ACTIVE and service is MOBILEKUNJI

            frontLineWorker = new FrontLineWorker(1509L, "2222222222", "Rashi", Designation.USHA,
                    "123", 12L, stateData, district, null, null, null,
                    null, null, null, Status.INVALID, "LLC", ConfigurationConstants.UNKNOWN_CIRCLE, null, null);


            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("2222222222");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("2222222222");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("2222222222");
            assertNotNull(whiteListUsersdb);

            // Record 10 operatorCode is not present in Database.

            frontLineWorker = new FrontLineWorker(1510L, "1234123412", "Rashi", Designation.USHA,
                    null, null, null, null, null, null, null,
                    null, null, null, Status.ANONYMOUS, null, null, "2345234567", null);

            whiteListUsers = new WhiteListUsers("1234123412");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("1234123412");
            assertNotNull(whiteListUsersdb);

            // Record 11 LanguageLocationCodeId is not null, circleCode is Unknown, Status is ACTIVE and IsWhiteListEnable
            // is false and MA and MK services are deployed in state.

            frontLineWorker = new FrontLineWorker(1520L, "9876549876", "Rashi", Designation.USHA,
                    "123", 13L, stateTemp1, districtTemp, null, null, null,
                    null, null, null, Status.ACTIVE, "LLC", ConfigurationConstants.UNKNOWN_CIRCLE, null, null);


            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("9876549876");
            assertNotNull(frontLineWorkerdb);

            // Record 12 LanguageLocationCodeId is not null, circleCode is Unknown, Status is ACTIVE and IsWhiteListEnable
            // is false and MA and MK services are not deployed in state.

            frontLineWorker = new FrontLineWorker(1521L, "9809809800", "Rashi", Designation.USHA,
                    "123", 13L, stateTemp1, districtTemp, null, null, null,
                    null, null, null, Status.ACTIVE, "LLC", ConfigurationConstants.UNKNOWN_CIRCLE, null, null);


            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("9809809800");
            assertNotNull(frontLineWorkerdb);

            // Record 13 LanguageLocationCodeId is not null, circleCode is Unknown, Status is ACTIVE and IsWhiteListEnable
            // is true and MA service is deployed in state and MK service is not deployed in state.

            frontLineWorker = new FrontLineWorker(1522L, "1233211234", "Rashi", Designation.USHA,
                    "123", 13L, stateTemp1, districtTemp, null, null, null,
                    null, null, null, Status.ACTIVE, "LLC", ConfigurationConstants.UNKNOWN_CIRCLE, null, null);


            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1233211234");
            assertNotNull(frontLineWorkerdb);

            // Record 14 status is INVALID and LanguageLocationCode, circleCode is not Null, IsWhiteListEnable
            // is true and MA service is deployed in state and MK service is not deployed in state.

            frontLineWorker = new FrontLineWorker(1550L, "8978978970", "Rashi", Designation.USHA,
                    "123", 13L, stateTemp1, districtTemp, null, null, null,
                    null, null, null, Status.INVALID, "LLC", "circleCode", null, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("8978978970");
            assertNotNull(frontLineWorkerdb);

            // Record 15 status is INVALID and LanguageLocationCode, circleCode is not Null, IsWhiteListEnable
            // is true and MA and MK services are not deployed in state.

            frontLineWorker = new FrontLineWorker(1551L, "3234325678", "Rashi", Designation.USHA,
                    "123", 13L, stateTemp1, districtTemp, null, null, null,
                    null, null, null, Status.INVALID, "LLC", "circleCode", null, null);

            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("3234325678");
            assertNotNull(frontLineWorkerdb);

            // Record 16 status is ANONYMOUS, circleCode is unknown and languageLocationCode is null.

            frontLineWorker = new FrontLineWorker(1511L, "8989898989", "Rashi", Designation.USHA,
                    null, null, null, null, null, null, null,
                    null, null, null, Status.ANONYMOUS, null, ConfigurationConstants.UNKNOWN_CIRCLE, null, "1111222233");
            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("8989898989");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("8989898989");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("8989898989");
            assertNotNull(whiteListUsersdb);

            // Record 17 Circle doesn't Exist in Database.

            frontLineWorker = new FrontLineWorker(1517L, "5050505050", "Rashi", Designation.USHA,
                    null, null, stateData, district, null, null, null,
                    null, null, null, Status.ANONYMOUS, null, null, "6543265432", "7877111111");


            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("5050505050");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("5050505050");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("5050505050");
            assertNotNull(whiteListUsersdb);

            // Record 18 LanguageLocationCode not Exist in Database.

            frontLineWorker = new FrontLineWorker(1512L, "1121121121", "Rashi", Designation.USHA,
                    null, null, stateData, district, null, null, null,
                    null, null, null, Status.ANONYMOUS, null, ConfigurationConstants.UNKNOWN_CIRCLE, null, null);


            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1121121121");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("1121121121");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("1121121121");
            assertNotNull(whiteListUsersdb);

            // Record 19 LanguageLocationCode should Exist in Database.

            frontLineWorker = new FrontLineWorker(1513L, "1313131313", "Rashi", Designation.USHA,
                    null, null, stateData, district, null, null, null,
                    null, null, null, Status.ANONYMOUS, null, "circleCode", "8111111888", null);


            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
            frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1313131313");
            assertNotNull(frontLineWorkerdb);

            whiteListUsers = new WhiteListUsers("1313131313");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("1313131313");
            assertNotNull(whiteListUsersdb);

            // Record 20 LanguageLocationCode should Exist but FrontlineWorker not present.

            whiteListUsers = new WhiteListUsers("1414141414");

            whiteListUsersService.createWhiteListUsers(whiteListUsers);
            whiteListUsersdb = whiteListUsersService.findContactNo("1414141414");
            assertNotNull(whiteListUsersdb);

            // Record 21 Operator is present in Database

            // Record 22 Operator is not present in Database
        }

        // do the setup
        setUpIsDone = true;
    }


    @Test
    public void testprocessUserDetails() throws DataValidationException, NmsInternalServerError,
            FlwNotInWhiteListException, ServiceNotDeployedException {

        UserProfile userProfile;
        FrontLineWorker frontLineWorker;
        LanguageLocationCode languageLocationCodeTemp = new LanguageLocationCode();
        Circle circleTemp = new Circle();

        // Record 1 LanguageLocationCodeId is not null, circleCode is Unknown And Status is ACTIVE

        userProfile = userProfileDetailsService.processUserDetails("1234512345", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("1234512345", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234512345");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ACTIVE, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertEquals(Designation.USHA, frontLineWorker.getDesignation());
        assertEquals("1781781781", frontLineWorker.getAlternateContactNo());
        assertEquals("1231231212", frontLineWorker.getOldMobileNo());


        //  Record 2 LanguageLocationCodeId is not null, circleCode is not Null And Status is ACTIVE

        userProfile = userProfileDetailsService.processUserDetails("1212121212", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("1212121212", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1212121212");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ACTIVE, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertEquals(Designation.ASHA, frontLineWorker.getDesignation());

        // Record 3 LanguageLocationCodeId is null, circleCode is Unknown And Status is ACTIVE

        userProfile = userProfileDetailsService.processUserDetails("9999999999", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("9999999999", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9999999999");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ACTIVE, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertEquals(Designation.ANM, frontLineWorker.getDesignation());


        // Record 4 status is INACTIVE AND LanguageLocationCodeId is null

        userProfile = userProfileDetailsService.processUserDetails("2121212121", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("2121212121", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("2121212121");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ACTIVE, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertEquals(Designation.AWW, frontLineWorker.getDesignation());

        // Record 5 status is ANONYMOUS and LanguageLocationCodeId is null, circleCode is unknown.

        userProfile = userProfileDetailsService.processUserDetails("1111111111", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("1111111111", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1111111111");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());

        // Record 6 status is ANONYMOUS LanguageLocationCodeId is not null, Circlecode is not null.

        userProfile = userProfileDetailsService.processUserDetails("1234567999", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("1234567999", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234567999");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());

        // Record 7 status is ANONYMOUS and front line worker is not present.

        userProfile = userProfileDetailsService.processUserDetails("9879879879", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("9879879879", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9879879879");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());

        // Record 8 status is INVALID and LanguageLocationCode, circleCode is not null.

        userProfile = userProfileDetailsService.processUserDetails("9876543210", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("9876543210", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9876543210");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertTrue(null == frontLineWorker.getFlwId());

        // Record 9  status is ACTIVE and service is MOBILEKUNJI

        userProfile = userProfileDetailsService.processUserDetails("2222222222", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEKUNJI);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("2222222222", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(20 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("2222222222");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertTrue(null == frontLineWorker.getFlwId());

        // Record 10 operatorCode is not Present in database

        try {
            userProfile = userProfileDetailsService.processUserDetails("1234123412", "circleCode", "12345", ServicesUsingFrontLineWorker.MOBILEKUNJI);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }


        // Record 11 LanguageLocationCodeId is not null, circleCode is Unknown, Status is ACTIVE and IsWhiteListEnable
        // is false and MA and MK services are deployed in state.

        userProfile = userProfileDetailsService.processUserDetails("9876549876", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        assertNotNull(userProfile);

        // Record 12 LanguageLocationCodeId is not null, circleCode is Unknown, Status is ACTIVE and IsWhiteListEnable
        // is false and MA and MK services are not deployed in state.

        stateTemp1.setIsMaDeployed(false);
        stateTemp1.setIsMkDeployed(false);
        stateTemp1.setIsWhiteListEnable(false);

        stateService.update(stateTemp1);
        assertNotNull(stateTemp1);

        try {
            userProfile = userProfileDetailsService.processUserDetails("9809809800", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        } catch (FlwNotInWhiteListException e) {
            Assert.assertEquals(e.getErrorCode(),ErrorCategoryConstants.INCONSISTENT_DATA);
        } catch (ServiceNotDeployedException e) {
            Assert.assertEquals(e.getErrorCode(),ErrorCategoryConstants.INCONSISTENT_DATA);
        }

        // Record 13 LanguageLocationCodeId is not null, circleCode is Unknown, Status is ACTIVE and IsWhiteListEnable
        // is true and MA service is deployed in state and MK service is not deployed in state.

        stateTemp1.setIsMaDeployed(true);
        stateTemp1.setIsMkDeployed(false);
        stateTemp1.setIsWhiteListEnable(true);

        stateService.update(stateTemp1);
        assertNotNull(stateTemp1);

        try {
            userProfile = userProfileDetailsService.processUserDetails("1233211234", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEKUNJI);
        } catch (FlwNotInWhiteListException e) {
            Assert.assertEquals(e.getErrorCode(),ErrorCategoryConstants.INCONSISTENT_DATA);
        } catch (ServiceNotDeployedException e) {
            Assert.assertEquals(e.getErrorCode(),ErrorCategoryConstants.INCONSISTENT_DATA);
        }

        // Record 14 status is INVALID and LanguageLocationCode, circleCode is not Null, IsWhiteListEnable
        // is true and MA service is deployed in state and MK service is not deployed in state.

        stateTemp1.setIsMaDeployed(true);
        stateTemp1.setIsMkDeployed(false);
        stateTemp1.setIsWhiteListEnable(true);

        stateService.update(stateTemp1);
        assertNotNull(stateTemp1);

        try {
        userProfile = userProfileDetailsService.processUserDetails("8978978970", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        } catch (FlwNotInWhiteListException e) {
            Assert.assertEquals(e.getErrorCode(),ErrorCategoryConstants.INCONSISTENT_DATA);
        } catch (ServiceNotDeployedException e) {
            Assert.assertEquals(e.getErrorCode(),ErrorCategoryConstants.INCONSISTENT_DATA);
        }

        // Record 15 status is INVALID and LanguageLocationCode, circleCode is not Null, IsWhiteListEnable
        // is true and MA and MK services are not deployed in state.

        stateTemp1.setIsMaDeployed(false);
        stateTemp1.setIsMkDeployed(false);
        stateTemp1.setIsWhiteListEnable(true);

        stateService.update(stateTemp1);
        assertNotNull(stateTemp1);

        try {
            userProfile = userProfileDetailsService.processUserDetails("3234325678", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        } catch (FlwNotInWhiteListException e) {
            Assert.assertEquals(e.getErrorCode(),ErrorCategoryConstants.INCONSISTENT_DATA);
        } catch (ServiceNotDeployedException e) {
            Assert.assertEquals(e.getErrorCode(),ErrorCategoryConstants.INCONSISTENT_DATA);
        }

        //  Record 16 status is ANONYMOUS, circleCode is unknown and languageLocationCode is null.

        languageLocationCodeTemp.setDistrict(district);
        languageLocationCodeTemp.setState(stateData);
        languageLocationCodeTemp.setCircle(circle);
        languageLocationCodeTemp.setCircleCode(circle.getCode());
        languageLocationCodeTemp.setDistrictCode(district.getDistrictCode());
        languageLocationCodeTemp.setStateCode(stateData.getStateCode());
        languageLocationCodeTemp.setLanguageLocationCode("LLC2");
        languageLocationCodeTemp.setLanguageMA("LanguageMA");
        languageLocationCodeTemp.setLanguageMK("LanguageMK");
        languageLocationCodeTemp.setLanguageKK("LanguageKK");
        languageLocationCodeTemp.setCircleCode("circleCode");//same as LLC with code 123
        languageLocationCodeTemp.setCreator("Etasha");
        languageLocationCodeTemp.setOwner("Etasha");
        languageLocationCodeTemp.setModifiedBy("Etasha");

        languageLocationCodeService.create(languageLocationCodeTemp);
        assertNotNull(languageLocationCodeTemp);

        //languageLocationCodeTemp = testHelper.createLanguageLocationCodeTemp();

        userProfile = userProfileDetailsService.processUserDetails("8989898989", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);

        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("8989898989", userProfile.getMsisdn());
        assertEquals("LLC", userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isDefaultLanguageLocationCode());

        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("8989898989");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());

        // Record 17 Circle doesn't Exist in Database.

        try {
            userProfile = userProfileDetailsService.processUserDetails("5050505050", "invalidCircle", "123",
                    ServicesUsingFrontLineWorker.MOBILEACADEMY);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }


    }

    @Test
    public void testUpdateLanguageLocationCodeFromMsisdn() throws DataValidationException, FlwNotInWhiteListException,
            ServiceNotDeployedException{


        FrontLineWorker frontLineWorker = new FrontLineWorker();
        UserProfile userProfile = new UserProfile();
        State stateTemp = new State();

        // Record 18 LanguageLocationCode not Exist in Database.

        try {

            userProfileDetailsService.updateLanguageLocationCodeFromMsisdn("LLC2", "1121121121", ServicesUsingFrontLineWorker.MOBILEKUNJI);

            frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1121121121");
            assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
            assertTrue(null == userProfile.getLanguageLocationCode());
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        // Record 19 LanguageLocationCode and contactNO Exist in Database.

        userProfileDetailsService.updateLanguageLocationCodeFromMsisdn("LLC", "1313131313", ServicesUsingFrontLineWorker.MOBILEKUNJI);
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1313131313");

        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("LLC", frontLineWorker.getLanguageLocationCodeId());

        // Record 20 LanguageLocationCode should Exist but FrontlineWorker not present.

        try {
            userProfileDetailsService.updateLanguageLocationCodeFromMsisdn("LLC", "1414141414",  ServicesUsingFrontLineWorker.MOBILEKUNJI);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }


    }

    @Test
    public void testValidateOperator() throws DataValidationException {

        // Record 21 Operator is present in Database

        userProfileDetailsService.validateOperator("123");

        // Record 22 Operator is not present in Database

        try {
            userProfileDetailsService.validateOperator("1234");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }


    }

}
