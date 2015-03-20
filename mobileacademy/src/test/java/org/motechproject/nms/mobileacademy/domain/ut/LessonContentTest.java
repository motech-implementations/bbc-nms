package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.LessonContent;

public class LessonContentTest {

	LessonContent lessonContent = new LessonContent();
	
	/*
	 * Test the Lesson Number of the Lesson
	 */
	@Test
	public void testGetLessonNumber() {
		lessonContent.setLessonNumber(1);
		assertSame(1, lessonContent.getLessonNumber());
	}
	/*
	 * Test the Name of the Lesson
	 */
	@Test
	public void testGetName() {
		lessonContent.setName("lesson");
		assertEquals("lesson", lessonContent.getName());
	}
	/*
	 * Test the AudioFile of the Lesson
	 */
	@Test
	public void testGetAudioFile() {
		lessonContent.setAudioFile("ch1_l1.wav");
		assertEquals("ch1_l1.wav",lessonContent.getAudioFile());
	}

}
