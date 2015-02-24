package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.ScoreContent;

public class ScoreContentTest {
//"score01", 
	ScoreContent scoreContent = new ScoreContent();
	@Test
	public void testGetName() {
		scoreContent.setName("score01");
		assertEquals("score01",scoreContent.getName());
	}

	@Test
	public void testGetAudioFile() {
		scoreContent.setAudioFile("Ch1_1ca.wav");
		assertEquals("Ch1_1ca.wav", scoreContent.getAudioFile());
	}

}
