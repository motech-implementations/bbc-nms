package org.motechproject.nms.mobilekunji.ut.domain;

import org.junit.Test;
import org.motechproject.nms.mobilekunji.domain.CsvContentUpload;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to test the CsvContentUpload Domain Class.
 */
public class CsvContentUploadTest {

    CsvContentUpload csvContentUpload = new CsvContentUpload();

    @Test
    public void testShouldSetWithAllData() {
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("1");
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setLanguageLocationCode("2");
        csvContentUpload.setContentName("contentName");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setContentFile("contentFile");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setCardCode("20");
        assertEquals("Content Index[1] Content ID[1] Circle Code [CircleCode] Language Location Code [" +
                "2] Content Name [contentName] Content Type [PROMPT] Content File [contentFile] Content Duration [" +
                "120] Card Number [20]", csvContentUpload.toString());
    }


    @Test
    public void testShouldSetWithNoData() {
        assertEquals("Content Index[null] Content ID[null] Circle Code [null] Language Location Code [" +
                "null] Content Name [null] Content Type [null] Content File [null] Content Duration [null]" +
                " Card Number [null]", csvContentUpload.toString());
    }

}
