package org.motechproject.nms.mobileacademy.web.ut;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.domain.CourseUnitState;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.dto.BookmarkWithScore;
import org.motechproject.nms.mobileacademy.service.CourseBookmarkService;
import org.motechproject.nms.mobileacademy.service.CourseService;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.mobileacademy.web.CourseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.google.gson.Gson;

/**
 * class contain unit test cases of CourseController
 *
 */
public class CourseControllerTest {

    @InjectMocks
    CourseController courseController = new CourseController();

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private CourseBookmarkService courseBookmarkService;

    @Before
    public void setup() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    public void testGetCourseSuccess() {
        Course course = new Course("Course", CourseUnitState.Active, null);
        String url = "/course";
        try {
            when(courseService.getMtrainingCourse()).thenReturn(course);
            when(courseService.getCourseJson()).thenReturn(
                    "JSON will be returned");
            mockMvc.perform(get(url))
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().string("JSON will be returned"));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetCourseFailure() {
        String url = "/course";
        String expectedJson = "{\"failureReason\":\""
                + MobileAcademyConstants.NO_COURSE_PRESENT + "\"}";

        try {
            when(courseService.getMtrainingCourse()).thenReturn(null);
            mockMvc.perform(get(url))
                    .andExpect(
                            status().is(
                                    HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetCourseVersionSuccess() {
        String url = "/courseVersion";
        DateTime currentTime = DateTime.now();

        String expectedJson = "{\"" + MobileAcademyConstants.COURSE_KEY_VERSION
                + "\":" + currentTime.getMillis() / 1000 + "}";
        try {
            when(courseService.getCurrentCourseVersion()).thenReturn(
                    (int) (currentTime.getMillis() / 1000));

            mockMvc.perform(get(url))
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetCourseVersionFailure() {
        String url = "/courseVersion";
        String expectedJson = "{\"failureReason\":\""
                + MobileAcademyConstants.NO_COURSE_PRESENT + "\"}";

        try {
            when(courseService.getCurrentCourseVersion()).thenReturn(null);
            mockMvc.perform(get(url))
                    .andExpect(
                            status().is(
                                    HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetBookmarkWithScore() {
        String callingNumber = "9990632906";
        String callId = "123456789123456";

        StringBuilder url = new StringBuilder("/bookmarkWithScore?");
        url.append("callingNumber=" + callingNumber);
        url.append("&callId=" + callId);

        try {
            when(userDetailsService.doesMsisdnExist(callingNumber)).thenReturn(
                    true);

            when(courseBookmarkService.getBookmarkWithScore(callingNumber))
                    .thenReturn("Bookmark Json");
            mockMvc.perform(get(url.toString()))
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().string("Bookmark Json"));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetBookmarkWithScoreFailure() {
        String callingNumber = "9990632906";
        String callId = "123456789123456";

        String expectedJson = "{\"failureReason\":\"" + "MSISDN"
                + ":Invalid Value\"}";

        StringBuilder url = new StringBuilder("/bookmarkWithScore?");
        url.append("callingNumber=" + callingNumber);
        url.append("&callId=" + callId);

        try {
            when(userDetailsService.doesMsisdnExist(callingNumber)).thenReturn(
                    false);

            when(courseBookmarkService.getBookmarkWithScore(callingNumber))
                    .thenReturn("Bookmark Json");
            mockMvc.perform(get(url.toString()))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveBookmarkWithScore() {
        String callingNumber = "9990632906";
        String callId = "123456789123456";

        BookmarkWithScore bookmarkWithScore = new BookmarkWithScore();
        bookmarkWithScore.setCallId(callId);
        bookmarkWithScore.setCallingNumber(callingNumber);
        bookmarkWithScore.setBookmark("Chapter01_Lesson01");
        bookmarkWithScore.setScoresByChapter(new HashMap<String, String>());

        String url = "/bookmarkWithScore";
        Gson gson = new Gson();
        String bookmarkJSon = gson.toJson(bookmarkWithScore);

        try {
            when(userDetailsService.doesMsisdnExist(callingNumber)).thenReturn(
                    true);

            when(courseBookmarkService.getBookmarkWithScore(callingNumber))
                    .thenReturn("Bookmark Json");
            mockMvc.perform(
                    post(url).contentType(MediaType.APPLICATION_JSON).body(
                            bookmarkJSon.getBytes())).andExpect(
                    status().is(HttpStatus.OK.value()));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSaveBookmarkWithScoreFailure() {
        String callingNumber = "9990632906";
        String callId = "123456789123456";
        String expectedJson = "{\"failureReason\":\"" + "MSISDN"
                + ":Invalid Value\"}";

        BookmarkWithScore bookmarkWithScore = new BookmarkWithScore();
        bookmarkWithScore.setCallId(callId);
        bookmarkWithScore.setCallingNumber(callingNumber);
        bookmarkWithScore.setBookmark("Chapter01_Lesson01");
        bookmarkWithScore.setScoresByChapter(new HashMap<String, String>());

        String url = "/bookmarkWithScore";
        Gson gson = new Gson();
        String bookmarkJSon = gson.toJson(bookmarkWithScore);

        try {
            when(userDetailsService.doesMsisdnExist(callingNumber)).thenReturn(
                    false);

            mockMvc.perform(
                    post(url).contentType(MediaType.APPLICATION_JSON).body(
                            bookmarkJSon.getBytes()))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(content().string(expectedJson));

        } catch (Exception e) {
            assertFalse(true);
        }
    }
}
