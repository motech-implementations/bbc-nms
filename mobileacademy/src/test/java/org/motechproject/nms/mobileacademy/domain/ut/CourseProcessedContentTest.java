package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.commons.ContentType;
import org.motechproject.nms.mobileacademy.domain.CourseProcessedContent;

public class CourseProcessedContentTest {

	CourseProcessedContent courseProcessedContent=new CourseProcessedContent(null, null, null, null, null, null, null, null);

	/*
	 * Test the Meta Data for the Course Processed
	 */
	@Test
	public void testMetadata() {
		courseProcessedContent.setMetadata("CorrectAnswer : 1");
		assertEquals("CorrectAnswer : 1",courseProcessedContent.getMetadata());
	}

	/*
	 * Test the Content Duration for the Course Processed
	 */
	@Test
	public void testContentDuration() {
		courseProcessedContent.setContentDuration(20);
		assertSame(20,courseProcessedContent.getContentDuration());
	}

	/*
	 * Test the Content Id for the Course Processed
	 */
	@Test
	public void testContentID() {
		courseProcessedContent.setContentID(111);
		assertSame(111,courseProcessedContent.getContentID());
	}

	/*
	 * Test the Circle for the Course Processed
	 */
	@Test
	public void testCircle() {
		courseProcessedContent.setCircle("AP");;
		assertEquals("AP",courseProcessedContent.getCircle());
	}

	/*
	 * Test the LanguageLocationCode for the Course Processed
	 */
	@Test
	public void testLanguageLocationCode() {
		courseProcessedContent.setLanguageLocationCode(14);
		assertSame(14,courseProcessedContent.getLanguageLocationCode());
	}

	/*
	 * Test the Content Name for the Course Processed
	 */
	@Test
	public void testContentName() {
		courseProcessedContent.setContentName("Chapter01_Lesson01");
		assertEquals("Chapter01_Lesson01",courseProcessedContent.getContentName());
	}

	/*
	 * Test the Content Type for the Course Processed
	 */
	@Test
	public void testContentType() {
		courseProcessedContent.setContentType(ContentType.CONTENT);
		assertEquals(ContentType.CONTENT,courseProcessedContent.getContentType());
	}

	/*
	 * Test the Content File for the Course Processed
	 */
	@Test
	public void testContentFile() {
		courseProcessedContent.setContentFile("ch1_l1.wav");;
		assertEquals("ch1_l1.wav",courseProcessedContent.getContentFile());
	}

}
