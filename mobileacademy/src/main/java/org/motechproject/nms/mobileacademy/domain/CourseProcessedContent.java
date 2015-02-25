package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.nms.mobileacademy.commons.ContentType;

/**
 * CourseProcessedContent object to refer course data processed from uploaded
 * CSV.
 *
 */
@Entity
public class CourseProcessedContent {

    private Integer contentID;

    private String circle;

    private Integer languageLocationCode;

    private String contentName;

    private ContentType contentType;

    private String contentFile;

    private Integer contentDuration;

    private String metadata;

    /**
     * Constructor with all arguments.
     * 
     * @param contentID Unique identifier of the content.
     * @param circle Circle code.
     * @param languageLocationCode Language location code for the content. One
     *            circle may have multiple language location codes.
     * @param contentName Name of the course content.
     * @param contentType Type of the content – prompt or content.
     * @param contentFile Name of the content audio file.
     * @param contentDuration Duration of the content audio file.
     * @param metaData Any additional information related to content. It can
     *            contain name-value pairs separated by semicolon(;). The name
     *            and value are separated by hyphen (-:).
     */
    public CourseProcessedContent(Integer contentID, String circle,
            Integer languageLocationCode, String contentName,
            ContentType contentType, String contentFile,
            Integer contentDuration, String metadata) {

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

    public Integer getContentDuration() {
        return contentDuration;
    }

    public void setContentDuration(Integer contentDuration) {
        this.contentDuration = contentDuration;
    }

    public Integer getContentID() {
        return contentID;
    }

    public void setContentID(Integer contentID) {
        this.contentID = contentID;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
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
