package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.*;

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
	}

}
