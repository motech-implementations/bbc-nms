package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;

public class QuizContentTest {

	QuizContent quizContent = new QuizContent();
	@Test
	public void testName() {
		quizContent.setName("quiz header");
		assertEquals("quiz header",quizContent.getName());
	}

	@Test
	public void testAudioFile() {
		quizContent.setAudioFile("ch1_qp.wav");
		assertSame("ch1_qp.wav",quizContent.getAudioFile());
	}

	@SuppressWarnings("serial")
	@Test
	public void testQuestions() {
		List<QuestionContent> questions = new ArrayList<QuestionContent>() {};
		questions.add(new QuestionContent(1, "question", "ch1_q1.wav"));
		questions.add(new QuestionContent(1, "correctAnswer", "ch1_q1_ca.wav"));
		questions.add(new QuestionContent(2, "question", "ch1_q2.wav"));
		quizContent.setQuestions(questions);
		assertEquals(questions,quizContent.getQuestions());
	}

}
