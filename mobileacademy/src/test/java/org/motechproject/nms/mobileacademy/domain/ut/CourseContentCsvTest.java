package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;

public class CourseContentCsvTest {

    CourseContentCsv courseContentCsv = new CourseContentCsv();

    /*
	 * Test the Content Id for the CourseContent
	 */
    @Test
    public void testContentId() {
        courseContentCsv.setContentId("100014");
        assertEquals("100014", courseContentCsv.getContentId());
    }

    /*
	 * Test the Circle for the CourseContent
	 */
    @Test
    public void testCircle() {
        courseContentCsv.setCircle("AP");
        ;
        assertEquals("AP", courseContentCsv.getCircle());
    }

    /*
	 * Test the LanguageLocationCode for the CourseContent
	 */
    @Test
    public void testLanguageLocationCode() {
        courseContentCsv.setLanguageLocationCode("14");
        ;
        assertEquals("14", courseContentCsv.getLanguageLocationCode());
    }

    /*
	 * Test the Content Name for the CourseContent
	 */
    @Test
    public void testContentName() {
        courseContentCsv.setContentName("Chapter01_Lesson01");
        assertEquals("Chapter01_Lesson01", courseContentCsv.getContentName());
    }

    /*
	 * Test the Content type for the CourseContent
	 */
    @Test
    public void testContentType() {
        courseContentCsv.setContentType("content");
        assertEquals("content", courseContentCsv.getContentType());
    }

    /*
	 * Test the Content File for the CourseContent
	 */
    @Test
    public void testContentFile() {
        courseContentCsv.setContentFile("ch1_l1.wav");
        ;
        assertEquals("ch1_l1.wav", courseContentCsv.getContentFile());
    }

    /*
	 * Test the Content Duration for the CourseContent
	 */
    @Test
    public void testContentDuration() {
        courseContentCsv.setContentDuration("150");
        ;
        assertEquals("150", courseContentCsv.getContentDuration());
    }

    /*
	 * Test the Meta Data for the CourseContent
	 */
    @Test
    public void testMetaData() {
        courseContentCsv.setMetaData("CorrectAnswer : 1");
        assertEquals("CorrectAnswer : 1", courseContentCsv.getMetaData());
    }

}
