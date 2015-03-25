package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.ContentType;
import org.motechproject.nms.kilkari.domain.ContentUpload;

public class ContentUploadTest {

    @Test
    public void shouldSetValuesInContentUpload() {

        ContentUpload contentUpload = new ContentUpload();
        ContentType contentType = ContentType.CONTENT;

        contentUpload.setCircleCode("circleCode");
        Assert.assertEquals("circleCode",contentUpload.getCircleCode());

        contentUpload.setContentDuration(120);
        Assert.assertTrue(120 == contentUpload.getContentDuration());

        contentUpload.setContentFile("contentFile");
        Assert.assertEquals("contentFile",contentUpload.getContentFile());

        contentUpload.setContentId(1L);
        Assert.assertTrue(1L == contentUpload.getContentId());

        contentUpload.setContentName("contentName");
        Assert.assertEquals("contentName",contentUpload.getContentName());

        contentUpload.setContentType(contentType);
        Assert.assertEquals(contentType,contentUpload.getContentType());

        contentUpload.setLanguageLocationCode(13);
        Assert.assertTrue(13 == contentUpload.getLanguageLocationCode());

    }
}
