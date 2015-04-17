package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.service.CourseBookmarkService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CourseBookmarkServiceIT extends BasePaxIT {

	@Inject
	private CourseBookmarkService courseBookmarkService;

	@Test
	public void testNoBookmarkCase() {
		String callingNo = "9718228124";
		courseBookmarkService.deleteBookmark(callingNo);
		/*
		 * In case of no bookmark in the system for a particular mobile no.,
		 * service should return blank JSON string
		 */
		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				MobileAcademyConstants.EMPTY_JSON);
		System.out.println("test for no bookmark case passed");

		Map<String, String> scoresByChapter = new HashMap<String, String>();

		/*
		 * Providing empty scoresByChapter node, Service should not insert it in
		 * the bookmark
		 */
		try {
			courseBookmarkService.saveBookmarkWithScore("Chapter01_Lesson01",
					scoresByChapter, callingNo);
		} catch (DataValidationException e) {
			assertTrue(false);
		}
		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				"{\"bookmark\":\"Chapter01_Lesson01\"}");
		System.out.println("test for empty scoresByChapter node passed");

		try {
			courseBookmarkService.saveBookmarkWithScore(null, null, callingNo);
		} catch (DataValidationException e) {
			assertTrue(false);
		}
		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				"{\"bookmark\":\"Chapter01_Lesson01\"}");
		System.out
				.println("test for No bookmarkID and no scoresByChapter node passed");

		try {
			courseBookmarkService.saveBookmarkWithScore(null, scoresByChapter,
					callingNo);
		} catch (DataValidationException e) {
			assertTrue(false);
		}
		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				"{\"bookmark\":\"Chapter01_Lesson01\"}");
		System.out
				.println("test for No bookmarkID and empty scoresByChapter node passed");

		try {
			courseBookmarkService.saveBookmarkWithScore("  ", scoresByChapter,
					callingNo);
		} catch (DataValidationException e) {
			assertTrue(true);
			System.out
					.println("expected data validation exception occured for blank bookmarkID");
		}
		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				"{\"bookmark\":\"Chapter01_Lesson01\"}");

		System.out
				.println("test for blank bookmarkID and empty scoresByChapter node passed");

	}

	@Test
	public void testBookmarkServiceInvalidParams() {
		String callingNo = "9718228124";
		courseBookmarkService.deleteBookmark(callingNo);

		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				MobileAcademyConstants.EMPTY_JSON);

		Map<String, String> scoresByChapter = new HashMap<String, String>();
		scoresByChapter.put("1", "5");
		/*
		 * Providing scoresByChapter node with invalid value of Score (greater
		 * than 4). Data validation exception will occur
		 */
		try {
			courseBookmarkService.saveBookmarkWithScore("Chapter01_Lesson01",
					scoresByChapter, callingNo);
		} catch (DataValidationException e) {
			assertTrue(true);
			System.out
					.println("expected data validation exception occured for invalid score no");

		}

		scoresByChapter.put("1", "3");
		scoresByChapter.put("12", "2");
		/*
		 * Providing scoresByChapter node with invalid value of Chapter No
		 * (greater than 11). Data validation exception will occur
		 */
		try {
			courseBookmarkService.saveBookmarkWithScore("Chapter01_Lesson01",
					scoresByChapter, callingNo);
		} catch (DataValidationException e) {
			assertTrue(true);
			System.out
					.println("expected data validation exception occured for invalid chapter no");
		}

		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				MobileAcademyConstants.EMPTY_JSON);
		System.out.println("Bookmark remains unaffected for any exception");
	}

	@Test
	public void testBookmarkSuccess() {
		String callingNo = "9718228124";
		courseBookmarkService.deleteBookmark(callingNo);

		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				MobileAcademyConstants.EMPTY_JSON);

		Map<String, String> scoresByChapter = new HashMap<String, String>();
		scoresByChapter.put("1", "3");
		scoresByChapter.put("2", "0");

		try {
			courseBookmarkService.saveBookmarkWithScore("Chapter01_Lesson01",
					scoresByChapter, callingNo);
		} catch (DataValidationException e) {
			assertTrue(false);
		}

		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				"{\"bookmark\":\"Chapter01_Lesson01\",\"scoresByChapter\":{\"1\":3,\"2\":0}}");

		scoresByChapter.put("1", "4");
		scoresByChapter.put("3", "3");

		/*
		 * ScoresByChapter input request doesn't contain the data for chapter 2,
		 * hence the score for chapter 2 will remain unchanged while score for
		 * chapter 1 will be overwritten
		 */
		try {
			courseBookmarkService.saveBookmarkWithScore("Chapter01_Lesson01",
					scoresByChapter, callingNo);
		} catch (DataValidationException e) {
			assertTrue(false);
		}

		assertEquals(
				courseBookmarkService.getBookmarkWithScore(callingNo),
				"{\"bookmark\":\"Chapter01_Lesson01\",\"scoresByChapter\":{\"1\":4,\"2\":0,\"3\":3}}");
	}

	@Test
	public void testCourseCompletionSuccess() {
		String callingNo = "9718228124";
		courseBookmarkService.deleteBookmark(callingNo);

		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				MobileAcademyConstants.EMPTY_JSON);

		Map<String, String> scoresByChapter = new HashMap<String, String>();
		scoresByChapter.put("1", "4");

		/*
		 * Checking if after completing the course, service resets the bookmark
		 * or not
		 */
		try {
			courseBookmarkService.saveBookmarkWithScore(
					MobileAcademyConstants.COURSE_COMPLETED, scoresByChapter,
					callingNo);
		} catch (DataValidationException e) {
			assertTrue(false);
		}
		assertEquals(courseBookmarkService.getBookmarkWithScore(callingNo),
				MobileAcademyConstants.EMPTY_JSON);
		System.out
				.println("test for resetting the bookmark on course competion passed");
	}

}
