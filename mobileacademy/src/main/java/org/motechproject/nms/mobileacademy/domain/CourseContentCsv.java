package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * CourseContentCsv object to refer Course Content csv table (mobile academy
 * course related data) which populate using CSV import from Motech GUI.
 *
 */
@Entity
public class CourseContentCsv extends MdsEntity {

    @Field
    private String contentId;

    @Field
    private String circle;

    @Field
    private String languageLocationCode;

    @Field
    private String contentName;

    @Field
    private String contentType;

    @Field
    private String contentFile;

    @Field
    private String contentDuration;

    @Field
    private String metaData;

    /**
     * constructor with 0 arguments.
     */
    public CourseContentCsv() {

    }

    /**
     * constructor with all arguments.
     * 
     * @param contentId Unique identifier of the content.
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
    public CourseContentCsv(String contentId, String circle,
            String languageLocationCode, String contentName,
            String contentType, String contentFile, String contentDuration,
            String metaData) {
        this.contentId = contentId;
        this.circle = circle;
        this.languageLocationCode = languageLocationCode;
        this.contentName = contentName;
        this.contentType = contentType;
        this.contentFile = contentFile;
        this.contentDuration = contentDuration;
        this.metaData = metaData;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public String getContentDuration() {
        return contentDuration;
    }

    public void setContentDuration(String contentDuration) {
        this.contentDuration = contentDuration;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

}