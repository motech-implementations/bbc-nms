package org.motechproject.nms.kilkari.ut.domain;

import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.CsvContentUpload;

public class ContentUploadCsvTest {

    CsvContentUpload contentUpload = new CsvContentUpload();
    @Test
    public void shouldSetValuesInContentUpload() {

        contentUpload.setCircleCode("circleCode");
        Assert.assertEquals("circleCode", contentUpload.getCircleCode());

        contentUpload.setContentDuration("120");
        Assert.assertEquals("120", contentUpload.getContentDuration());

        contentUpload.setContentFile("contentFile");
        Assert.assertEquals("contentFile",contentUpload.getContentFile());

        contentUpload.setContentId("1L");
        Assert.assertEquals("1L", contentUpload.getContentId());

        contentUpload.setContentName("contentName");
        Assert.assertEquals("contentName",contentUpload.getContentName());

        contentUpload.setContentType("contentType");
        Assert.assertEquals("contentType",contentUpload.getContentType());

        contentUpload.setLanguageLocationCode("13");
        Assert.assertEquals("13", contentUpload.getLanguageLocationCode());

    }

    @Test
    public void shouldReturnStringOfFieldValues() {

        String string = contentUpload.toString();
        Assert.assertNotNull(string);

    }
}
