package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;

public class ScoreContentTest {

	ScoreContent scoreContent = new ScoreContent();
	
	/*
	 * Test the Name of the Score
	 */
	@Test
	public void testGetName() {
		scoreContent.setName("score01");
		assertEquals("score01",scoreContent.getName());
	}

	/*
	 * Test the AudioFile of the Score
	 */
	@Test
	public void testGetAudioFile() {
		scoreContent.setAudioFile("Ch1_1ca.wav");
		assertEquals("Ch1_1ca.wav", scoreContent.getAudioFile());
	}

}
