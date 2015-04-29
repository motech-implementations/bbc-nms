package org.motechproject.nms.kilkariobd.builder;

import org.motechproject.nms.kilkari.domain.ContentType;
import org.motechproject.nms.kilkari.domain.ContentUpload;

public class ContentBuilder {

    public ContentUpload buildContent(String languageLocationCode, String contentName) {
        ContentUpload contentUpload = new ContentUpload();
        contentUpload.setContentName(contentName);
        contentUpload.setContentId(1L);
        contentUpload.setLanguageLocationCode(languageLocationCode);
        contentUpload.setContentDuration(2);
        contentUpload.setContentFile("contentFile");
        contentUpload.setCircleCode("circleCode");
        contentUpload.setContentType(ContentType.CONTENT);
        return contentUpload;
    }
}
