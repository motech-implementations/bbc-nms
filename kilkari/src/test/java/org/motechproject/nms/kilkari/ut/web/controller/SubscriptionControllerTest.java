//package org.motechproject.nms.kilkari.ut.web.controller;
//
//import org.apache.commons.io.IOUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.motechproject.nms.kilkari.domain.Subscriber;
//import org.motechproject.nms.kilkari.domain.Subscription;
//import org.motechproject.nms.kilkari.domain.SubscriptionPack;
//import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
//import org.motechproject.nms.kilkari.service.SubscriptionService;
//import org.motechproject.nms.kilkari.service.UserDetailsService;
//import org.motechproject.nms.kilkari.web.SubscriptionController;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.server.MockMvc;
//import org.springframework.test.web.server.setup.MockMvcBuilders;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import static junit.framework.Assert.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.MockitoAnnotations.initMocks;
//import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
//
//
//public class SubscriptionControllerTest {
//
//    @Mock
//    private UserDetailsService userDetailsService;
//
//    @Mock
//    private SubscriptionService subscriptionService;
//
//    private SubscriptionDataService subscriptionDataService;
//    private MockMvc controller;
//
//    @InjectMocks
//    private SubscriptionController subscriptionController = new SubscriptionController(userDetailsService, subscriptionService);
//
//    @Before
//    public void setUp() {
//        initMocks(this);
//        controller = MockMvcBuilders.standaloneSetup(subscriptionController).build();
//    }
//
//    @Test
//    public void shouldCreateSubscription() throws Exception {
//        initMocks(this);
//        controller = MockMvcBuilders.standaloneSetup(subscriptionController).build();
//        controller.perform(post("/subscription")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(loadJson("rest/subscription/createSubscription.json").getBytes("UTF-8"))
//        ).andExpect(
//                status().is(HttpStatus.OK.value()));
//        //ArgumentCaptor<Subscription> captor = ArgumentCaptor.forClass(Subscription.class);
//        //verify(subscriptionDataService).create(captor.capture());
//        //assertEquals(SubscriptionPack.PACK_48_WEEKS, captor.getValue().getPackName());
//        //assertEquals(CampaignType.ABSOLUTE, captor.getValue().getCampaignType());
//    }
//
//    private String loadJson(String filename) throws IOException {
//        try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
//            return IOUtils.toString(in);
//        }
//    }
//}
