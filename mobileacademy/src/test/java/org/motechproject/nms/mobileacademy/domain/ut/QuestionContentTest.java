package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;

public class QuestionContentTest {
//(1, "question", "ch1_q1.wav")
	QuestionContent questionContent = new QuestionContent();
	@Test
	public void testGetQuestionNumber() {
		questionContent.setQuestionNumber(1);
		assertSame(1,questionContent.getQuestionNumber());
	}

	@Test
	public void testGetName() {
		questionContent.setName("question");
		assertEquals("question",questionContent.getName());
	}

	@Test
	public void testGetAudioFile() {
		questionContent.setAudioFile("ch1_q1.wav");
		assertSame("ch1_q1.wav",questionContent.getAudioFile());
	}

}
