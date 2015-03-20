package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.ChapterContent;
import org.motechproject.nms.mobileacademy.domain.LessonContent;
import org.motechproject.nms.mobileacademy.domain.QuestionContent;
import org.motechproject.nms.mobileacademy.domain.QuizContent;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;

public class ChapterContentTest {

	ChapterContent chapterContent = new ChapterContent();

	/*
	 * Test the Chapter Number for the Chapter Content
	 */
	@Test
	public void testChapterNumber() {
		chapterContent.setChapterNumber(1);
		assertSame(1, chapterContent.getChapterNumber());
	}
	
	/*
	 * Test the Name for the Chapter
	 */
	@Test
	public void testName() {
		chapterContent.setName("content");
		assertEquals("content",chapterContent.getName());
	}

	/*
	 * Test the AudioFile for the Chapter
	 */
	@Test
	public void testAudioFile() {
		chapterContent.setAudioFile("ch1_l1.wav");
		assertEquals("ch1_l1.wav",chapterContent.getAudioFile());
	}

	/*
	 * Test the Lesson Content for the Chapter
	 */
	@SuppressWarnings("serial")
	@Test
	public void testLesson() {	
		List<LessonContent> lessons = new ArrayList<LessonContent>() {};
		lessons.add(new LessonContent(1, "lesson", "Ch1_l1.wav"));
		lessons.add(new LessonContent(2, "lesson", "Ch1_l3.wav"));
		lessons.add(new LessonContent(4, "lesson", "Ch1_l4.wav"));
		lessons.add(new LessonContent(3, "lesson", "Ch1_l1.wav"));
		chapterContent.setLessons(lessons);
		assertEquals(lessons,chapterContent.getLessons());
	}

	/*
	 * Test the Score Content for the Chapter
	 */
	@SuppressWarnings("serial")
	@Test
	public void testScore() {
		List<ScoreContent> scoreContent = new ArrayList<ScoreContent>() {};
		scoreContent.add(new ScoreContent("score01", "Ch1_1ca.wav"));
		scoreContent.add(new ScoreContent("score00", "Ch1_0ca.wav"));
		scoreContent.add(new ScoreContent("score02", "Ch1_2ca.wav"));
		chapterContent.setScores(scoreContent);
		assertEquals(scoreContent,chapterContent.getScores());
	}

	/*
	 * Test the Quiz Content for the Chapter
	 */
	@SuppressWarnings("serial")
	@Test
	public void testQuiz() {
		List<QuestionContent> quizContent = new ArrayList<QuestionContent>() {};
		quizContent.add(new QuestionContent(1, "question", "ch1_q1.wav"));
		quizContent.add(new QuestionContent(1, "correctAnswer", "ch1_q1_ca.wav"));
		quizContent.add(new QuestionContent(2, "question", "ch1_q2.wav"));
		QuizContent quiz = new QuizContent("quiz header", "ch1_qp.wav",quizContent);
		chapterContent.setQuiz(quiz);;
		assertEquals(quiz,chapterContent.getQuiz());
	}

}
