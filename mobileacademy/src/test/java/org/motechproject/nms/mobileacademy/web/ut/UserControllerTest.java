package org.motechproject.nms.mobileacademy.web.ut;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.nms.mobileacademy.dto.User;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.mobileacademy.service.impl.UserDetailsServiceImpl;
import org.motechproject.nms.mobileacademy.web.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

/**
 * class contain unit test cases of UserController
 *
 */
public class UserControllerTest {

    @InjectMocks
    UserController userController = new UserController();

    private MockMvc mockMvc;

    @Mock
    private UserDetailsService userDetailsService;

    @Before
    public void setup() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUserDetailsSuccess() {
        // set the input details
        String callingNumber = "9990632906";
        String operator = "A";
        String circle = "UP";
        String callId = "123456789123456";
        // Stub the service methods and responses
        User user = new User();
        user.setCircle(circle);
        user.setCurrentUsageInPulses(0);
        user.setDefaultLanguageLocationCode(1);
        user.setEndOfUsagePromptCounter(2);
        user.setLanguageLocationCode(null);
        user.setMaxAllowedEndOfUsagePrompt(3);
        user.setMaxAllowedUsageInPulses(5);

        StringBuilder url = new StringBuilder("/user?");
        url.append("callingNumber=" + callingNumber);
        url.append("&operator=" + operator);
        url.append("&circle=" + circle);
        url.append("&callId=" + callId);

        String expectedResponse = "{\"circle\":\"UP\",\"defaultLanguageLocationCode\":1,\"currentUsageInPulses\":0,\"maxAllowedUsageInPulses\":5,\"endOfUsagePromptCounter\":2,\"maxAllowedEndOfUsagePrompt\":3}";

        try {
            when(
                    userDetailsService.findUserDetails(callingNumber, operator,
                            circle, callId)).thenReturn(user);
            mockMvc.perform(get(url.toString()))
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().string(expectedResponse));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetUserDetailsWhenCallingNumberIsNotPresent() {
        // set the input details
        String operator = "A";
        String circle = "UP";
        String callId = "123456789123456";
        // Stub the service methods and responses
        User user = new User();
        user.setCircle(circle);
        user.setCurrentUsageInPulses(0);
        user.setDefaultLanguageLocationCode(1);
        user.setEndOfUsagePromptCounter(2);
        user.setLanguageLocationCode(null);
        user.setMaxAllowedEndOfUsagePrompt(3);
        user.setMaxAllowedUsageInPulses(5);

        StringBuilder url = new StringBuilder("/user?");
        url.append("operator=" + operator);
        url.append("&circle=" + circle);
        url.append("&callId=" + callId);

        String expectedJson = "{\"failureReason\":\"callingNumber:Not Present\"}";

        try {
            mockMvc.perform(get(url.toString()))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetUserDetailsWhenCallingNumberIsNotNumeric() {
        // set the input details
        String callingNumber = "99906asdaA";// String value
        String operator = "A";
        String circle = "UP";
        String callId = "123456789123456";
        // Stub the service methods and responses
        User user = new User();
        user.setCircle(circle);
        user.setCurrentUsageInPulses(0);
        user.setDefaultLanguageLocationCode(1);
        user.setEndOfUsagePromptCounter(2);
        user.setLanguageLocationCode(null);
        user.setMaxAllowedEndOfUsagePrompt(3);
        user.setMaxAllowedUsageInPulses(5);

        StringBuilder url = new StringBuilder("/user?");
        url.append("callingNumber=" + callingNumber);
        url.append("&operator=" + operator);
        url.append("&circle=" + circle);
        url.append("&callId=" + callId);

        String expectedJson = "{\"failureReason\":\"callingNumber:Invalid Value\"}";

        try {
            mockMvc.perform(get(url.toString()))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetUserDetailsWhenCallingNumberIsNumericAndShort() {
        // set the input details
        String callingNumber = "999063290";// 9 length
        String operator = "A";
        String circle = "UP";
        String callId = "123456789123456";
        // Stub the service methods and responses
        User user = new User();
        user.setCircle(circle);
        user.setCurrentUsageInPulses(0);
        user.setDefaultLanguageLocationCode(1);
        user.setEndOfUsagePromptCounter(2);
        user.setLanguageLocationCode(null);
        user.setMaxAllowedEndOfUsagePrompt(3);
        user.setMaxAllowedUsageInPulses(5);

        StringBuilder url = new StringBuilder("/user?");
        url.append("callingNumber=" + callingNumber);
        url.append("&operator=" + operator);
        url.append("&circle=" + circle);
        url.append("&callId=" + callId);

        String expectedJson = "{\"failureReason\":\"callingNumber:Invalid Value\"}";

        try {
            mockMvc.perform(get(url.toString()))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSetLanguageLocationCodeSuccess() {
        String inputJson = "{\"callingNumber\":\"9990632906\" ,\"callId\":123456789123456,\"languageLocationCode\":1}";
        try {
            UserDetailsService userDetailsServiceSpy = Mockito
                    .spy(new UserDetailsServiceImpl());
            Mockito.doNothing()
                    .when(userDetailsServiceSpy)
                    .setLanguageLocationCode("1", "9990632906",
                            "123456789123456");
            mockMvc.perform(
                    post("/languageLocationCode").contentType(
                            MediaType.APPLICATION_JSON).body(
                            inputJson.getBytes("UTF-8"))).andExpect(
                    status().is(HttpStatus.OK.value()));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSetLanguageLocationCodeWhenCallingNumberNotPresent() {
        String inputJson = "{\"callId\":232323421424,\"languageLocationCode\":1}";
        String expectedJson = "{\"failureReason\":\"callingNumber:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/languageLocationCode").contentType(
                            MediaType.APPLICATION_JSON).body(
                            inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSetLanguageLocationCodeWhenCallIdNotPresent() {
        String inputJson = "{\"callingNumber\":\"9990632906\" ,\"languageLocationCode\":1}";
        String expectedJson = "{\"failureReason\":\"callId:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/languageLocationCode").contentType(
                            MediaType.APPLICATION_JSON).body(
                            inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSetLanguageLocationCodeWhenLlcNotPresent() {
        String inputJson = "{\"callingNumber\":\"9990632906\" ,\"callId\":232323421424}";
        String expectedJson = "{\"failureReason\":\"languageLocationCode:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/languageLocationCode").contentType(
                            MediaType.APPLICATION_JSON).body(
                            inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSetLanguageLocationCodeWhenCallingNumberIsInvalid() {
        String inputJson = "{\"callingNumber\":\"9990632906da\" ,\"callId\":232323421424,\"languageLocationCode\":1}";
        String expectedJson = "{\"failureReason\":\"callingNumber:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/languageLocationCode").contentType(
                            MediaType.APPLICATION_JSON).body(
                            inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }
}
