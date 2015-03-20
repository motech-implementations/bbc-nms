package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;

public class QuestionContentTest {

	QuestionContent questionContent = new QuestionContent();
	
	/*
	 * Test the Question Number of the Question Content
	 */
	@Test
	public void testGetQuestionNumber() {
		questionContent.setQuestionNumber(1);
		assertSame(1,questionContent.getQuestionNumber());
	}

	/*
	 * Test the Name of the Question Content
	 */
	@Test
	public void testGetName() {
		questionContent.setName("question");
		assertEquals("question",questionContent.getName());
	}

	/*
	 * Test the AudioFile of the Question Content
	 */
	@Test
	public void testGetAudioFile() {
		questionContent.setAudioFile("ch1_q1.wav");
		assertSame("ch1_q1.wav",questionContent.getAudioFile());
	}

}
