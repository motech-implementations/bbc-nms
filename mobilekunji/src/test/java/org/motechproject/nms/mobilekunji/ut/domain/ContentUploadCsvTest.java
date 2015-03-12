package org.motechproject.nms.mobilekunji.ut.domain;

import org.junit.Test;
import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;

import static org.junit.Assert.assertEquals;

/**
 * Created by abhishek on 12/3/15.
 */
public class ContentUploadCsvTest {

    ContentUploadCsv contentUploadCsv = new ContentUploadCsv();

    @Test
    public void testShouldSetWithAllData() {
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setOperation("ADD");
        contentUploadCsv.setContentId("1");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("2");
        contentUploadCsv.setContentName("contentName");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("contentFile");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setCardNumber("20");
        assertEquals("Content Index[1] Operation[ADD] Content ID[1] Circle Code [CircleCode] Language Location Code [" +
                "2] Content Name [contentName] Content Type [PROMPT] Content File [contentFile] Content Duration [" +
                "120] Card Number [20]", contentUploadCsv.toString());
    }


    @Test
    public void testShouldSetWithNoData() {
/*        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setOperation("ADD");
        contentUploadCsv.setContentId("1");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("2");
        contentUploadCsv.setContentName("contentName");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("contentFile");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setCardNumber("20");*/
        assertEquals("Content Index[null] Operation[ADD] Content ID[null] Circle Code [null] Language Location Code [" +
                "null] Content Name [null] Content Type [null] Content File [null] Content Duration [null]" +
                " Card Number [null]", contentUploadCsv.toString());
    }

}
