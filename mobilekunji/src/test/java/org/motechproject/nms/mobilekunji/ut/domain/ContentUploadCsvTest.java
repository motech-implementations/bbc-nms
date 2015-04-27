package org.motechproject.nms.mobilekunji.ut.domain;

import org.junit.Test;
import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to test the ContentUploadCsv Domain Class.
 */
public class ContentUploadCsvTest {

    ContentUploadCsv contentUploadCsv = new ContentUploadCsv();

    @Test
    public void testShouldSetWithAllData() {
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("1");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("2");
        contentUploadCsv.setContentName("contentName");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("contentFile");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setCardCode("20");
        assertEquals("Content Index[1] Content ID[1] Circle Code [CircleCode] Language Location Code [" +
                "2] Content Name [contentName] Content Type [PROMPT] Content File [contentFile] Content Duration [" +
                "120] Card Number [20]", contentUploadCsv.toString());
    }


    @Test
    public void testShouldSetWithNoData() {
        assertEquals("Content Index[null] Content ID[null] Circle Code [null] Language Location Code [" +
                "null] Content Name [null] Content Type [null] Content File [null] Content Duration [null]" +
                " Card Number [null]", contentUploadCsv.toString());
    }

}
