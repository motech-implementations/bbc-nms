package org.motechproject.nms.mobileacademy.service.ut;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.motechproject.nms.frontlineworker.ServicesUsingFrontLineWorker;
import org.motechproject.nms.frontlineworker.domain.UserProfile;
import org.motechproject.nms.frontlineworker.service.UserProfileDetailsService;
import org.motechproject.nms.frontlineworker.service.impl.UserProfileDetailsServiceImpl;
import org.motechproject.nms.mobileacademy.commons.CappingType;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.Configuration;
import org.motechproject.nms.mobileacademy.domain.FlwUsageDetail;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.service.ConfigurationService;
import org.motechproject.nms.mobileacademy.service.FlwUsageDetailService;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.mobileacademy.service.impl.UserDetailsServiceImpl;
import org.motechproject.nms.util.helper.DataValidationException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * class contain unit test cases of UserDetailsServiceImpl.java
 *
 */
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsService userDetailsService = new UserDetailsServiceImpl();

    @Mock
    private UserProfileDetailsService userProfileDetailsService;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private FlwUsageDetailService flwUsageDetailService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * test Find User Details when Default Llc is true And No Capping is
     * configured.
     * 
     * @throws DataValidationException
     * @throws Exception
     */
    @Test
    public void testFindUserDetailsForDefaultLlcAndNoCapping()
            throws DataValidationException, Exception {
        // set the input details
        String callingNumber = "9990632906";
        String operator = "A";
        String circle = "UP";
        String callId = "123456789";
        // Stub the service methods and responses
        UserProfile userProfile = new UserProfile();
        userProfile.setCircle("A");
        userProfile.setCreated(true);
        userProfile.setIsDefaultLanguageLocationCode(true);// default llc true
        userProfile.setLanguageLocationCode(1);
        userProfile.setNmsFlwId(11l);

        Configuration configuration = new Configuration();
        configuration
                .setIndex(MobileAcademyConstants.CONFIG_DEFAULT_RECORD_INDEX);
        // No capping
        configuration.setCappingType(CappingType.NO_CAPPING.getValue());
        configuration
                .setCourseQualifyingScore(MobileAcademyConstants.CONFIG_DEFAULT_COURSE_QUALIFYING_SCORE);
        configuration
                .setDefaultLanguageLocationCode(MobileAcademyConstants.CONFIG_DEFAULT_LANGUAGE_LOCATION_CODE);
        configuration
                .setMaxAllowedEndOfUsagePrompt(MobileAcademyConstants.CONFIG_DEFAULT_MAX_ALLOW_END_USAGE_PROMPT);
        configuration
                .setNationalCapValue(MobileAcademyConstants.CONFIG_DEFAULT_NATIONAL_CAP_VALUE);
        configuration
                .setSmsSenderAddress(MobileAcademyConstants.CONFIG_DEFAULT_SMS_SENDER_ADDRESS);

        FlwUsageDetail flwUsageDetail = new FlwUsageDetail();
        flwUsageDetail.setFlwId(userProfile.getNmsFlwId());
        flwUsageDetail.setCurrentUsageInPulses(0);
        flwUsageDetail.setEndOfUsagePromptCounter(0);
        flwUsageDetail.setLastAccessTime(new DateTime(System
                .currentTimeMillis()));

        when(
                userProfileDetailsService.processUserDetails(callingNumber,
                        circle, operator,
                        ServicesUsingFrontLineWorker.MOBILEACADEMY))
                .thenReturn(userProfile);
        when(configurationService.getConfiguration()).thenReturn(configuration);

        when(flwUsageDetailService.findByFlwId(11l)).thenReturn(flwUsageDetail);

        try {
            User userResponse = userDetailsService.findUserDetails(
                    callingNumber, operator, circle, callId);
            // Assertions
            assertEquals("A", userResponse.getCircle());
            assertEquals(null, userResponse.getLanguageLocationCode());
            assertTrue(1 == userResponse.getDefaultLanguageLocationCode());
            assertEquals(flwUsageDetail.getCurrentUsageInPulses(),
                    userResponse.getCurrentUsageInPulses());
            assertEquals(flwUsageDetail.getEndOfUsagePromptCounter(),
                    userResponse.getEndOfUsagePromptCounter());
            assertEquals(configuration.getMaxAllowedEndOfUsagePrompt(),
                    userResponse.getMaxAllowedEndOfUsagePrompt());
            assertEquals(
                    MobileAcademyConstants.MAX_ALLOWED_USAGE_PULSE_FOR_UNCAPPED,
                    userResponse.getMaxAllowedUsageInPulses());
        } catch (DataValidationException e) {
            assertFalse(true);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    /**
     * test Find User Details when Default Llc is true and its values is null.
     * 
     * @throws DataValidationException
     * @throws Exception
     */
    @Test
    public void testFindUserDetailsForDefaultLlcAndNullValue()
            throws DataValidationException, Exception {
        // set the input details
        String callingNumber = "9990632906";
        String operator = "A";
        String circle = "UP";
        String callId = "123456789";
        // Stub the service methods and responses
        UserProfile userProfile = new UserProfile();
        userProfile.setCircle("A");
        userProfile.setCreated(true);
        userProfile.setIsDefaultLanguageLocationCode(true);// default llc true
        userProfile.setLanguageLocationCode(null);
        userProfile.setNmsFlwId(11l);

        Configuration configuration = new Configuration();
        configuration
                .setIndex(MobileAcademyConstants.CONFIG_DEFAULT_RECORD_INDEX);
        // No capping
        configuration.setCappingType(CappingType.NO_CAPPING.getValue());
        configuration
                .setCourseQualifyingScore(MobileAcademyConstants.CONFIG_DEFAULT_COURSE_QUALIFYING_SCORE);
        // National default
        configuration.setDefaultLanguageLocationCode(121);
        configuration
                .setMaxAllowedEndOfUsagePrompt(MobileAcademyConstants.CONFIG_DEFAULT_MAX_ALLOW_END_USAGE_PROMPT);
        configuration
                .setNationalCapValue(MobileAcademyConstants.CONFIG_DEFAULT_NATIONAL_CAP_VALUE);
        configuration
                .setSmsSenderAddress(MobileAcademyConstants.CONFIG_DEFAULT_SMS_SENDER_ADDRESS);

        FlwUsageDetail flwUsageDetail = new FlwUsageDetail();
        flwUsageDetail.setFlwId(userProfile.getNmsFlwId());
        flwUsageDetail.setCurrentUsageInPulses(0);
        flwUsageDetail.setEndOfUsagePromptCounter(0);
        flwUsageDetail.setLastAccessTime(new DateTime(System
                .currentTimeMillis()));

        when(
                userProfileDetailsService.processUserDetails(callingNumber,
                        circle, operator,
                        ServicesUsingFrontLineWorker.MOBILEACADEMY))
                .thenReturn(userProfile);
        when(configurationService.getConfiguration()).thenReturn(configuration);

        when(flwUsageDetailService.findByFlwId(11l)).thenReturn(flwUsageDetail);

        try {
            User userResponse = userDetailsService.findUserDetails(
                    callingNumber, operator, circle, callId);
            // Assertions
            assertEquals("A", userResponse.getCircle());
            assertEquals(null, userResponse.getLanguageLocationCode());
            assertTrue(121 == userResponse.getDefaultLanguageLocationCode());
            assertEquals(flwUsageDetail.getCurrentUsageInPulses(),
                    userResponse.getCurrentUsageInPulses());
            assertEquals(flwUsageDetail.getEndOfUsagePromptCounter(),
                    userResponse.getEndOfUsagePromptCounter());
            assertEquals(configuration.getMaxAllowedEndOfUsagePrompt(),
                    userResponse.getMaxAllowedEndOfUsagePrompt());
            assertEquals(
                    MobileAcademyConstants.MAX_ALLOWED_USAGE_PULSE_FOR_UNCAPPED,
                    userResponse.getMaxAllowedUsageInPulses());
        } catch (DataValidationException e) {
            assertFalse(true);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    /**
     * test Find User Details when Default Llc is false and llc value is null.
     * 
     * @throws DataValidationException
     * @throws Exception
     */
    @Test
    public void testFindUserDetailsForLlcAndNullValue()
            throws DataValidationException, Exception {
        // set the input details
        String callingNumber = "9990632906";
        String operator = "A";
        String circle = "UP";
        String callId = "123456789";
        // Stub the service methods and responses
        UserProfile userProfile = new UserProfile();
        userProfile.setCircle("A");
        userProfile.setCreated(true);
        userProfile.setIsDefaultLanguageLocationCode(false);// default false
        userProfile.setLanguageLocationCode(null);
        userProfile.setNmsFlwId(11l);

        Configuration configuration = new Configuration();
        configuration
                .setIndex(MobileAcademyConstants.CONFIG_DEFAULT_RECORD_INDEX);
        // No capping
        configuration.setCappingType(CappingType.NO_CAPPING.getValue());
        configuration
                .setCourseQualifyingScore(MobileAcademyConstants.CONFIG_DEFAULT_COURSE_QUALIFYING_SCORE);
        // National default
        configuration.setDefaultLanguageLocationCode(121);
        configuration
                .setMaxAllowedEndOfUsagePrompt(MobileAcademyConstants.CONFIG_DEFAULT_MAX_ALLOW_END_USAGE_PROMPT);
        configuration
                .setNationalCapValue(MobileAcademyConstants.CONFIG_DEFAULT_NATIONAL_CAP_VALUE);
        configuration
                .setSmsSenderAddress(MobileAcademyConstants.CONFIG_DEFAULT_SMS_SENDER_ADDRESS);

        FlwUsageDetail flwUsageDetail = new FlwUsageDetail();
        flwUsageDetail.setFlwId(userProfile.getNmsFlwId());
        flwUsageDetail.setCurrentUsageInPulses(0);
        flwUsageDetail.setEndOfUsagePromptCounter(0);
        flwUsageDetail.setLastAccessTime(new DateTime(System
                .currentTimeMillis()));

        when(
                userProfileDetailsService.processUserDetails(callingNumber,
                        circle, operator,
                        ServicesUsingFrontLineWorker.MOBILEACADEMY))
                .thenReturn(userProfile);
        when(configurationService.getConfiguration()).thenReturn(configuration);

        when(flwUsageDetailService.findByFlwId(11l)).thenReturn(flwUsageDetail);

        try {
            User userResponse = userDetailsService.findUserDetails(
                    callingNumber, operator, circle, callId);
            // Assertions
            assertEquals("A", userResponse.getCircle());
            assertEquals(null, userResponse.getLanguageLocationCode());
            assertTrue(121 == userResponse.getDefaultLanguageLocationCode());
            assertEquals(flwUsageDetail.getCurrentUsageInPulses(),
                    userResponse.getCurrentUsageInPulses());
            assertEquals(flwUsageDetail.getEndOfUsagePromptCounter(),
                    userResponse.getEndOfUsagePromptCounter());
            assertEquals(configuration.getMaxAllowedEndOfUsagePrompt(),
                    userResponse.getMaxAllowedEndOfUsagePrompt());
            assertEquals(
                    MobileAcademyConstants.MAX_ALLOWED_USAGE_PULSE_FOR_UNCAPPED,
                    userResponse.getMaxAllowedUsageInPulses());
        } catch (DataValidationException e) {
            assertFalse(true);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    /**
     * test Find User Details when Default Llc is false, llc value is not null
     * and state capping in enabled.
     * 
     * @throws DataValidationException
     * @throws Exception
     */
    @Test
    public void testFindUserDetailsForLlcAndStateCapping()
            throws DataValidationException, Exception {
        // set the input details
        String callingNumber = "9990632906";
        String operator = "A";
        String circle = "UP";
        String callId = "123456789";
        // Stub the service methods and responses
        UserProfile userProfile = new UserProfile();
        userProfile.setCircle("A");
        userProfile.setCreated(true);
        userProfile.setIsDefaultLanguageLocationCode(false);// default false
        userProfile.setLanguageLocationCode(2);
        userProfile.setNmsFlwId(11l);
        userProfile.setMaxStateLevelCappingValue(5);// capping

        Configuration configuration = new Configuration();
        configuration
                .setIndex(MobileAcademyConstants.CONFIG_DEFAULT_RECORD_INDEX);
        // state capping
        configuration.setCappingType(CappingType.STATE_CAPPING.getValue());
        configuration
                .setCourseQualifyingScore(MobileAcademyConstants.CONFIG_DEFAULT_COURSE_QUALIFYING_SCORE);
        configuration
                .setDefaultLanguageLocationCode(MobileAcademyConstants.CONFIG_DEFAULT_LANGUAGE_LOCATION_CODE);
        configuration
                .setMaxAllowedEndOfUsagePrompt(MobileAcademyConstants.CONFIG_DEFAULT_MAX_ALLOW_END_USAGE_PROMPT);
        configuration
                .setNationalCapValue(MobileAcademyConstants.CONFIG_DEFAULT_NATIONAL_CAP_VALUE);
        configuration
                .setSmsSenderAddress(MobileAcademyConstants.CONFIG_DEFAULT_SMS_SENDER_ADDRESS);

        FlwUsageDetail flwUsageDetail = new FlwUsageDetail();
        flwUsageDetail.setFlwId(userProfile.getNmsFlwId());
        flwUsageDetail.setCurrentUsageInPulses(0);
        flwUsageDetail.setEndOfUsagePromptCounter(0);
        flwUsageDetail.setLastAccessTime(new DateTime(System
                .currentTimeMillis()));

        when(
                userProfileDetailsService.processUserDetails(callingNumber,
                        circle, operator,
                        ServicesUsingFrontLineWorker.MOBILEACADEMY))
                .thenReturn(userProfile);
        when(configurationService.getConfiguration()).thenReturn(configuration);

        when(flwUsageDetailService.findByFlwId(11l)).thenReturn(flwUsageDetail);

        try {
            User userResponse = userDetailsService.findUserDetails(
                    callingNumber, operator, circle, callId);
            // Assertions
            assertEquals("A", userResponse.getCircle());
            assertEquals(null, userResponse.getDefaultLanguageLocationCode());
            assertTrue(2 == userResponse.getLanguageLocationCode());
            assertEquals(flwUsageDetail.getCurrentUsageInPulses(),
                    userResponse.getCurrentUsageInPulses());
            assertEquals(flwUsageDetail.getEndOfUsagePromptCounter(),
                    userResponse.getEndOfUsagePromptCounter());
            assertEquals(configuration.getMaxAllowedEndOfUsagePrompt(),
                    userResponse.getMaxAllowedEndOfUsagePrompt());
            assertTrue(5 == userResponse.getMaxAllowedUsageInPulses());
        } catch (DataValidationException e) {
            assertFalse(true);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    /**
     * test Find User Details when Default Llc is false, llc value is not null
     * and national capping in enabled.
     * 
     * @throws DataValidationException
     * @throws Exception
     */
    @Test
    public void testFindUserDetailsForNationalCapping()
            throws DataValidationException, Exception {
        // set the input details
        String callingNumber = "9990632906";
        String operator = "99";
        String circle = "UP";
        String callId = "123456789";
        // Stub the service methods and responses
        UserProfile userProfile = new UserProfile();
        userProfile.setCircle("99");
        userProfile.setCreated(true);
        userProfile.setIsDefaultLanguageLocationCode(false);// default false
        userProfile.setLanguageLocationCode(2);
        userProfile.setNmsFlwId(11l);
        userProfile.setMaxStateLevelCappingValue(5);// capping

        Configuration configuration = new Configuration();
        configuration
                .setIndex(MobileAcademyConstants.CONFIG_DEFAULT_RECORD_INDEX);
        // national capping
        configuration.setCappingType(CappingType.NATIONAL_CAPPING.getValue());
        configuration
                .setCourseQualifyingScore(MobileAcademyConstants.CONFIG_DEFAULT_COURSE_QUALIFYING_SCORE);
        // national default llc
        configuration.setDefaultLanguageLocationCode(121);
        configuration
                .setMaxAllowedEndOfUsagePrompt(MobileAcademyConstants.CONFIG_DEFAULT_MAX_ALLOW_END_USAGE_PROMPT);
        // National cap value
        configuration.setNationalCapValue(9);
        configuration
                .setSmsSenderAddress(MobileAcademyConstants.CONFIG_DEFAULT_SMS_SENDER_ADDRESS);

        FlwUsageDetail flwUsageDetail = new FlwUsageDetail();
        flwUsageDetail.setFlwId(userProfile.getNmsFlwId());
        flwUsageDetail.setCurrentUsageInPulses(0);
        flwUsageDetail.setEndOfUsagePromptCounter(0);
        flwUsageDetail.setLastAccessTime(new DateTime(System
                .currentTimeMillis()));

        when(
                userProfileDetailsService.processUserDetails(callingNumber,
                        circle, operator,
                        ServicesUsingFrontLineWorker.MOBILEACADEMY))
                .thenReturn(userProfile);
        when(configurationService.getConfiguration()).thenReturn(configuration);

        when(flwUsageDetailService.findByFlwId(11l)).thenReturn(flwUsageDetail);

        try {
            User userResponse = userDetailsService.findUserDetails(
                    callingNumber, operator, circle, callId);
            // Assertions
            assertEquals(null, userResponse.getDefaultLanguageLocationCode());
            assertTrue(2 == userResponse.getLanguageLocationCode());
            assertEquals(flwUsageDetail.getCurrentUsageInPulses(),
                    userResponse.getCurrentUsageInPulses());
            assertEquals(flwUsageDetail.getEndOfUsagePromptCounter(),
                    userResponse.getEndOfUsagePromptCounter());
            assertEquals(configuration.getMaxAllowedEndOfUsagePrompt(),
                    userResponse.getMaxAllowedEndOfUsagePrompt());
            assertTrue(9 == userResponse.getMaxAllowedUsageInPulses());
        } catch (DataValidationException e) {
            assertFalse(true);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    /**
     * test Find User Details when Default Llc is false, llc value is not null
     * and national capping in enabled and national cap value is null.
     * 
     * @throws DataValidationException
     * @throws Exception
     */
    @Test
    public void testFindUserDetailsForNationalCappingWhenValueIsnull()
            throws DataValidationException, Exception {
        // set the input details
        String callingNumber = "9990632906";
        String operator = "99";
        String circle = "UP";
        String callId = "123456789";
        // Stub the service methods and responses
        UserProfile userProfile = new UserProfile();
        userProfile.setCircle("99");
        userProfile.setCreated(true);
        userProfile.setIsDefaultLanguageLocationCode(false);// default false
        userProfile.setLanguageLocationCode(2);
        userProfile.setNmsFlwId(11l);
        userProfile.setMaxStateLevelCappingValue(5);// capping

        Configuration configuration = new Configuration();
        configuration
                .setIndex(MobileAcademyConstants.CONFIG_DEFAULT_RECORD_INDEX);
        // national capping
        configuration.setCappingType(CappingType.NATIONAL_CAPPING.getValue());
        configuration
                .setCourseQualifyingScore(MobileAcademyConstants.CONFIG_DEFAULT_COURSE_QUALIFYING_SCORE);
        // national default llc
        configuration.setDefaultLanguageLocationCode(121);
        configuration
                .setMaxAllowedEndOfUsagePrompt(MobileAcademyConstants.CONFIG_DEFAULT_MAX_ALLOW_END_USAGE_PROMPT);
        // National cap value set to null
        configuration.setNationalCapValue(null);
        configuration
                .setSmsSenderAddress(MobileAcademyConstants.CONFIG_DEFAULT_SMS_SENDER_ADDRESS);

        FlwUsageDetail flwUsageDetail = new FlwUsageDetail();
        flwUsageDetail.setFlwId(userProfile.getNmsFlwId());
        flwUsageDetail.setCurrentUsageInPulses(0);
        flwUsageDetail.setEndOfUsagePromptCounter(0);
        flwUsageDetail.setLastAccessTime(new DateTime(System
                .currentTimeMillis()));

        when(
                userProfileDetailsService.processUserDetails(callingNumber,
                        circle, operator,
                        ServicesUsingFrontLineWorker.MOBILEACADEMY))
                .thenReturn(userProfile);
        when(configurationService.getConfiguration()).thenReturn(configuration);

        when(flwUsageDetailService.findByFlwId(11l)).thenReturn(flwUsageDetail);

        try {
            User userResponse = userDetailsService.findUserDetails(
                    callingNumber, operator, circle, callId);
            // Assertions
            assertEquals(null, userResponse.getDefaultLanguageLocationCode());
            assertTrue(2 == userResponse.getLanguageLocationCode());
            assertEquals(flwUsageDetail.getCurrentUsageInPulses(),
                    userResponse.getCurrentUsageInPulses());
            assertEquals(flwUsageDetail.getEndOfUsagePromptCounter(),
                    userResponse.getEndOfUsagePromptCounter());
            assertEquals(configuration.getMaxAllowedEndOfUsagePrompt(),
                    userResponse.getMaxAllowedEndOfUsagePrompt());
            assertTrue(MobileAcademyConstants.MAX_ALLOWED_USAGE_PULSE_FOR_UNCAPPED == userResponse
                    .getMaxAllowedUsageInPulses());
        } catch (DataValidationException e) {
            assertFalse(true);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    /**
     * test Set Language Location Code
     * 
     * @throws NumberFormatException
     * @throws DataValidationException
     */
    @Test
    public void testSetLanguageLocationCode() throws NumberFormatException,
            DataValidationException {
        String languageLocationCode = "1";
        String callingNumber = "9990635906";
        String callId = "123456789";
        UserProfileDetailsService userProfileDetailsServiceSpy = Mockito
                .spy(new UserProfileDetailsServiceImpl());
        Mockito.doNothing()
                .when(userProfileDetailsServiceSpy)
                .updateLanguageLocationCodeFromMsisdn(
                        Integer.parseInt(languageLocationCode), callingNumber);
        try {
            userDetailsService.setLanguageLocationCode(languageLocationCode,
                    callingNumber, callId);
        } catch (DataValidationException e) {
            assertFalse(true);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

}
