package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;


/**
 * Created by nitin on 2/9/15.
 */
@Entity
public class CourseProcessedContent {
    private int contentID;
    private String circle;
    private int languageLocationCode;
    private String contentName;
    private ContentType contentType;
    private String contentFile;
    private int contentDuration;
    private String metadata;

    public CourseProcessedContent(int contentID, String circle, int languageLocationCode, String contentName, ContentType contentType, String contentFile, int contentDuration, String metadata) {

        this.contentID = contentID;
        this.circle = circle;
        this.languageLocationCode = languageLocationCode;
        this.contentName = contentName;
        this.contentType = contentType;
        this.contentFile = contentFile;
        this.contentDuration = contentDuration;
        this.metadata = metadata;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public int getContentDuration() {
        return contentDuration;
    }

    public void setContentDuration(int contentDuration) {
        this.contentDuration = contentDuration;
    }

    public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public int getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(int languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }
}

