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

    @Test
    public void testSaveCallDetailSuccess() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8"))).andExpect(
                    status().is(HttpStatus.OK.value()));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidCallId() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"1234567890123457\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callId:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailNotNumericCallingNumber() {
        String inputJson = "{" + "\"callingNumber\": 99999888xy,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"Invalid JSON\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidCallingNumber() {
        String inputJson = "{" + "\"callingNumber\": 99999888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callingNumber:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidCallStatus() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":4,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callStatus:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidCallDisconnectReason() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 7," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callDisconnectReason:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCallingNumber() {
        String inputJson = "{" + "\"operator\": \"VF\","
                + "\"circle\": \"DL\"," + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callingNumber:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCallId() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callId:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCallStartTime() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callStartTime:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCallEndTime() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callEndTime:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingOperator() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"circle\": \"DL\"," + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"operator:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCircle() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"circle:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCallDurationInPulses() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callDurationInPulses:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingEndOfUsagePromptCounter() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"endOfUsagePromptCounter:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCallStatus() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callStatus:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCallDisconnectReason() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"content\": [" + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"callDisconnectReason:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingContent() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1" + "}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8"))).andExpect(
                    status().is(HttpStatus.OK.value()));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidContentType() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"menu\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"type:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidContentName() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\"," + "\"contentName\": \" \","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"contentName:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidContentFile() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \" \"," + "\"startTime\": 1200000000,"
                + "\"endTime\": 1222222221," + "\"completionFlag\": true" + "}"
                + "]}";
        String expectedJson = "{\"failureReason\":\"contentFile:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidStartTime() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": \"1200000xyz\"," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"startTime:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidEndTime() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": \"12222222xy\","
                + "\"completionFlag\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"endTime:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidCompletionFlag() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": \"1222222221\","
                + "\"completionFlag\": \"yes\","
                + "\"correctAnswerReceived\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"completionFlag:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailInvalidCorrectAnswerReceived() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": \"1222222221\","
                + "\"completionFlag\": true,"
                + "\"correctAnswerReceived\": \"yes\"" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"correctAnswerReceived:Invalid Value\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingCompletionFlag() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": \"1222222221\","
                + "\"correctAnswerReceived\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"completionFlag:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingEndTime() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"completionFlag\": true,"
                + "\"correctAnswerReceived\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"endTime:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingStartTime() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"endTime\": 1222222221," + "\"completionFlag\": true,"
                + "\"correctAnswerReceived\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"startTime:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingContentFile() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\","
                + "\"contentName\": \"Chapter-01lesson-04\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true,"
                + "\"correctAnswerReceived\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"contentFile:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingContentName() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"type\": \"lesson\"," + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true,"
                + "\"correctAnswerReceived\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"contentName:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveCallDetailMissingContentType() {
        String inputJson = "{" + "\"callingNumber\": 9999988888,"
                + "\"operator\": \"VF\"," + "\"circle\": \"DL\","
                + "\"callId\": \"123456789012345\","
                + "\"callStartTime\": 1422879903,"
                + "\"callEndTime\": 1422879923,"
                + "\"callDurationInPulses\": 20,"
                + "\"endOfUsagePromptCounter\": 0," + "\"callStatus\":1,"
                + "\"callDisconnectReason\": 1," + "\"content\": ["
                + "{\"contentName\": \"Chapter-01lesson-04\","
                + "\"contentFile\": \"ch1_l4.wav\","
                + "\"startTime\": 1200000000," + "\"endTime\": 1222222221,"
                + "\"completionFlag\": true,"
                + "\"correctAnswerReceived\": true" + "}" + "]}";
        String expectedJson = "{\"failureReason\":\"type:Not Present\"}";
        try {
            mockMvc.perform(
                    post("/callDetails")
                            .contentType(MediaType.APPLICATION_JSON).body(
                                    inputJson.getBytes("UTF-8")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

}
