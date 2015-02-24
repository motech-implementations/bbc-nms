package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This entity represents the content uploadCSV record in kilkari module
 */
@Entity
public class ContentUploadCsv extends MdsEntity {

    @Field
    private String contentId;

    @Field
    private String circleCode;

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

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
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

    /**
     * This method override the toString method to create string for contentId,
     * circleCode,languageLocationCode,contentName,contentFile,contentType,and
     * contentDuration for the instance variables

     * @return String of contentId,
     * circleCode,languageLocationCode,contentName,contentFile,contentType,and
     * contentDuration
     */
    public String toString() {

        StringBuffer recordStr = new StringBuffer();
        recordStr.append("contentId [" + this.contentId);
        recordStr.append("] circleCode [" + this.circleCode);
        recordStr.append("] languageLocationCode [" + this.languageLocationCode);
        recordStr.append("] contentName [" + this.contentName);
        recordStr.append("] contentType [" + this.contentType);
        recordStr.append("] contentFile [" + this.contentFile);
        recordStr.append("] contentDuration [" + this.contentDuration);
        return recordStr.toString();
    }
}
