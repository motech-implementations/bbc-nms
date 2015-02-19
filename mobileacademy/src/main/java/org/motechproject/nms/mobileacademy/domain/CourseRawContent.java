package org.motechproject.nms.mobileacademy.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * CourseRawContent correspond to Course Csv Content table (containing mobile
 * academy course related data) which populate using CSV import.
 *
 */
@Entity
public class CourseRawContent {

    /**
     * ADD/MOD/DEL. Default ADD.
     */
    @Field
    private String operation;
    /**
     * Unique identifier of the content.
     */
    @Field
    private String contentId;
    /**
     * Circle code.
     */
    @Field
    private String circle;
    /**
     * Language location code for the content. One circle may have multiple
     * language location codes.
     */
    @Field
    private String languageLocationCode;
    /**
     * Name of the course content.
     */
    @Field
    private String contentName;
    /**
     * Type of the content – prompt or content.
     */
    @Field
    private String contentType;
    /**
     * Name of the content audio file.
     */
    @Field
    private String contentFile;
    /**
     * Duration of the content audio file.
     */
    @Field
    private String contentDuration;
    /**
     * Any additional information related to content. It can contain name-value
     * pairs separated by semicolon(;). The name and value are separated by
     * hyphen (-:).
     */
    @Field
    private String metaData;

    /**
     * flag correspond to record update status.
     */

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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
