package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;

public class CourseContentCsvTest {

    CourseContentCsv courseContentCsv = new CourseContentCsv();

    // "ADD", "14",
    // "AP", "14", "null", "Content", "ch1_l1.wav", "150", ""

    @Test
    public void testContentId() {
        courseContentCsv.setContentId("100014");
        assertEquals("100014", courseContentCsv.getContentId());
    }

    @Test
    public void testCircle() {
        courseContentCsv.setCircle("AP");
        ;
        assertEquals("AP", courseContentCsv.getCircle());
    }

    @Test
    public void testLanguageLocationCode() {
        courseContentCsv.setLanguageLocationCode("14");
        ;
        assertEquals("14", courseContentCsv.getLanguageLocationCode());
    }

    @Test
    public void testContentName() {
        courseContentCsv.setContentName("Chapter01_Lesson01");
        assertEquals("Chapter01_Lesson01", courseContentCsv.getContentName());
    }

    @Test
    public void testContentType() {
        courseContentCsv.setContentType("content");
        assertEquals("content", courseContentCsv.getContentType());
    }

    @Test
    public void testContentFile() {
        courseContentCsv.setContentFile("ch1_l1.wav");
        ;
        assertEquals("ch1_l1.wav", courseContentCsv.getContentFile());
    }

    @Test
    public void testContentDuration() {
        courseContentCsv.setContentDuration("150");
        ;
        assertEquals("150", courseContentCsv.getContentDuration());
    }

    @Test
    public void testMetaData() {
        courseContentCsv.setMetaData("CorrectAnswer : 1");
        assertEquals("CorrectAnswer : 1", courseContentCsv.getMetaData());
    }

}
