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
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerCsvService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.frontlineworker.service.impl.UserProfileDetailsServiceImpl;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.masterdata.service.StateService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
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
    
    private UserProfileDetailsServiceImpl userProfileDetailsImpl;
    
    private static boolean setUpIsDone = false;
    
    private State state = null;
    
    private State stateData = null;
    
    private District district = null;
    
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
        }

        // do the setup
        setUpIsDone = true;

        FrontLineWorker frontLineWorker;
        FrontLineWorker frontLineWorkerdb;

        // Record 1 LanguageLocationCodeId is not null, circleCode is Unknown And Status is ACTIVE

        frontLineWorker = new FrontLineWorker(1500L, "1234512345", "Rashi", Designation.USHA,
                "123", 12L, stateData, district, null, null, null,
                null, null, null, null, Status.ACTIVE, 123, ConfigurationConstants.UNKNOWN_CIRCLE);

        frontLineWorker.setCreator("Etasha");
        frontLineWorker.setModifiedBy("Etasha");
        frontLineWorker.setOwner("Etasha");

        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234512345");
        assertNotNull(frontLineWorkerdb);

        // Record 2 LanguageLocationCodeId is not null, circleCode is not Null And Status is ACTIVE

        frontLineWorker = new FrontLineWorker(1501L, "1212121212", "Rashi", Designation.ASHA,
                "123", 12L, stateData, district, null, null, null,
                null, null, null, null, Status.ACTIVE, 123, "circleCode");

        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1212121212");
        assertNotNull(frontLineWorkerdb);


        // Record 3 LanguageLocationCodeId is null, circleCode is Null And Status is ACTIVE

        frontLineWorker = new FrontLineWorker(1502L, "9999999999", "Rashi", Designation.ANM,
                "123", 12L, stateData, district, null, null, null,
                null, null, null, null, Status.ACTIVE, null, ConfigurationConstants.UNKNOWN_CIRCLE);

        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("9999999999");
        assertNotNull(frontLineWorkerdb);

        // Record 4 Status is INACTIVE AND LanguageLocationCodeId is null

        frontLineWorker = new FrontLineWorker(1503L, "2121212121", "Etasha", Designation.AWW,
                "123", 12L, stateData, district, null, null, null,
                null, null, null, null, Status.INACTIVE, null, null);

        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("2121212121");
        assertNotNull(frontLineWorkerdb);

        // Record 5 status is ANONYMOUS LanguageLocationCodeId is not null, Circlecode is null.

        frontLineWorker = new FrontLineWorker(1505L, "1111111111", "Rashi", Designation.USHA,
                null, null, null, null, null, null, null,
                null, null, null, null, Status.ANONYMOUS, null, ConfigurationConstants.UNKNOWN_CIRCLE);

        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1111111111");
        assertNotNull(frontLineWorkerdb);

        // Record 6 status is ANONYMOUS LanguageLocationCodeId is not null, Circlecode is not null.

        frontLineWorker = new FrontLineWorker(1507L, "1234567999", "Rashi", Designation.USHA,
                null, null, null, null, null, null, null,
                null, null, null, null, Status.ANONYMOUS, 123, "circleCode");

        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1234567999");
        assertNotNull(frontLineWorkerdb);

        // Record 7 status is ANONYMOUS and front line worker is not present.

        // Record 8 status is INVALID and LanguageLocationCode, circleCode is not Null

        frontLineWorker = new FrontLineWorker(1508L, "9876543210", "Rashi", Designation.USHA,
                "123", 12L, stateData, district, null, null, null,
                null, null, null, null, Status.INVALID, 123, "circleCode");

        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("9876543210");
        assertNotNull(frontLineWorkerdb);

        // Record 9  status is ACTIVE and service is MOBILEKUNJI

        frontLineWorker = new FrontLineWorker(1509L, "2222222222", "Rashi", Designation.USHA,
                "123", 12L, stateData, district, null, null, null,
                null, null, null, null, Status.INVALID, 123, ConfigurationConstants.UNKNOWN_CIRCLE);


        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("2222222222");
        assertNotNull(frontLineWorkerdb);

        // Record 10 operatorCode is not present in Database.

        frontLineWorker = new FrontLineWorker(1510L, "1234123412", "Rashi", Designation.USHA,
                null, null, null, null, null, null, null,
                null, null, null, null, Status.ANONYMOUS, null, null);

        // Record 11 status is ANONYMOUS, circleCode is unknown and languageLocationCode is null.

        frontLineWorker = new FrontLineWorker(1511L, "8989898989", "Rashi", Designation.USHA,
                null, null, null, null, null, null, null,
                null, null, null, null, Status.ANONYMOUS, null, ConfigurationConstants.UNKNOWN_CIRCLE);
        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("8989898989");
        assertNotNull(frontLineWorkerdb);

        // Record 12 LanguageLocationCode not Exist in Database.

        frontLineWorker = new FrontLineWorker(1512L, "1121121121", "Rashi", Designation.USHA,
                null, null, stateData, district, null, null, null,
                null, null, null, null, Status.ANONYMOUS, null, ConfigurationConstants.UNKNOWN_CIRCLE);


        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1121121121");
        assertNotNull(frontLineWorkerdb);

        // Record 13 LanguageLocationCode should Exist in Database.

        frontLineWorker = new FrontLineWorker(1513L, "1313131313", "Rashi", Designation.USHA,
                null, null, stateData, district, null, null, null,
                null, null, null, null, Status.ANONYMOUS, null, "circleCode");


        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("1313131313");
        assertNotNull(frontLineWorkerdb);

        // Record 14 LanguageLocationCode should Exist but FrontlineWorker not present.

        // Record 15 Operator is present in Database

        // Record 16 Operator is not present in Database

        // Record 17 Circle doesn't Exist in Database.

        frontLineWorker = new FrontLineWorker(1517L, "5050505050", "Rashi", Designation.USHA,
                null, null, stateData, district, null, null, null,
                null, null, null, null, Status.ANONYMOUS, null, null);


        frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        frontLineWorkerdb = frontLineWorkerService.getFlwBycontactNo("5050505050");
        assertNotNull(frontLineWorkerdb);
    }

    
    @Test
    public void testprocessUserDetails() throws DataValidationException {
        
        UserProfile userProfile;
        FrontLineWorker frontLineWorker;
        LanguageLocationCode languageLocationCodeTemp = new LanguageLocationCode();
        
        // Record 1 LanguageLocationCodeId is not null, circleCode is Unknown And Status is ACTIVE
        
        userProfile = userProfileDetailsService.processUserDetails("1234512345", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("1234512345", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234512345");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ACTIVE, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertEquals(Designation.USHA, frontLineWorker.getDesignation());
        
        
        //  Record 2 LanguageLocationCodeId is not null, circleCode is not Null And Status is ACTIVE
        
        userProfile = userProfileDetailsService.processUserDetails("1212121212", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("1212121212", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1212121212");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ACTIVE, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertEquals(Designation.ASHA, frontLineWorker.getDesignation());
        
        // Record 3 LanguageLocationCodeId is null, circleCode is Unknown And Status is ACTIVE
        
        userProfile = userProfileDetailsService.processUserDetails("9999999999", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("9999999999", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9999999999");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ACTIVE, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertEquals(Designation.ANM, frontLineWorker.getDesignation());
        
        // Record 4 status is INACTIVE AND LanguageLocationCodeId is null
        
        userProfile = userProfileDetailsService.processUserDetails("2121212121", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("2121212121", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("2121212121");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ACTIVE, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertEquals(Designation.AWW, frontLineWorker.getDesignation());
        

        // Record 5 status is ANONYMOUS and LanguageLocationCodeId is null, circleCode is unknown.
        
        userProfile = userProfileDetailsService.processUserDetails("1111111111", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("1111111111", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1111111111");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());

        // Record 6 status is ANONYMOUS LanguageLocationCodeId is not null, Circlecode is not null.
        
        userProfile = userProfileDetailsService.processUserDetails("1234567999", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("1234567999", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1234567999");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        
        // Record 7 status is ANONYMOUS and front line worker is not present.
        
        userProfile = userProfileDetailsService.processUserDetails("9879879879", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("9879879879", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9879879879");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        
        // Record 8 status is INVALID and LanguageLocationCode, circleCode is not null.
        
        userProfile = userProfileDetailsService.processUserDetails("9876543210", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEACADEMY);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("9876543210", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("9876543210");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertEquals("123", frontLineWorker.getOperatorCode());
        assertTrue(null == frontLineWorker.getFlwId());
        
        // Record 9  status is ACTIVE and service is MOBILEKUNJI
        
        userProfile = userProfileDetailsService.processUserDetails("2222222222", "circleCode", "123", ServicesUsingFrontLineWorker.MOBILEKUNJI);
        
        assertEquals("circleCode", userProfile.getCircle());
        assertEquals("2222222222", userProfile.getMsisdn());
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(20 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(true, userProfile.isCreated());
        assertEquals(false, userProfile.isDefaultLanguageLocationCode());
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("2222222222");
        assertNotNull(frontLineWorker);
        assertEquals("circleCode", frontLineWorker.getCircleCode());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
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

        //  Record 11 status is ANONYMOUS, circleCode is unknown and languageLocationCode is null.

        languageLocationCodeTemp.setDistrict(district);
        languageLocationCodeTemp.setState(stateData);
        languageLocationCodeTemp.setCircle(circle);
        languageLocationCodeTemp.setCircleCode(circle.getCode());
        languageLocationCodeTemp.setDistrictCode(district.getDistrictCode());
        languageLocationCodeTemp.setStateCode(stateData.getStateCode());
        languageLocationCodeTemp.setLanguageLocationCode(1234);
        languageLocationCodeTemp.setLanguageMA("LanguageMA");
        languageLocationCodeTemp.setLanguageMK("LanguageMK");
        languageLocationCodeTemp.setLanguageKK("LanguageKK");
        languageLocationCodeTemp.setStateCode(1L);
        languageLocationCodeTemp.setDistrictCode(2L);
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
        assertTrue(123 == userProfile.getLanguageLocationCode());
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
        assertEquals(false, userProfile.isCreated());
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
    public void testUpdateLanguageLocationCodeFromMsisdn() throws DataValidationException {
        
        
        FrontLineWorker frontLineWorker = new FrontLineWorker();
        UserProfile userProfile = new UserProfile();
        
        
        // Record 12 LanguageLocationCode not Exist in Database.
        
        try {
            userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(234, "1121121121");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1121121121");
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertTrue(null == userProfile.getLanguageLocationCode());
        
        // Record 13 LanguageLocationCode and contactNO Exist in Database.
        
        userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(123, "1313131313");
        frontLineWorker = frontLineWorkerService.getFlwBycontactNo("1313131313");
        
        assertEquals(Status.ANONYMOUS, frontLineWorker.getStatus());
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
        
        // Record 14 LanguageLocationCode should Exist but FrontlineWorker not present.
        
        try {
            userProfileDetailsService.updateLanguageLocationCodeFromMsisdn(123, "1414141414");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
    }
    
    @Test
    public void testValidateOperator() throws DataValidationException {
        
        // Record 15 Operator is present in Database
        
        userProfileDetailsService.validateOperator("123");
        
        // Record 16 Operator is not present in Database
        
        try {
            userProfileDetailsService.validateOperator("1234");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }
        
        
    }

}
