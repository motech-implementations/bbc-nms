package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;

/**
 * This class Models data for Content Upload records
 */
@Entity(recordHistory = true)
public class ContentUpload extends MdsEntity {

    @Field(required = true)
    @Unique
    private Integer contentId;

    @Field(required = true)
    private String circleCode;

    @Field(required = true)
    private String languageLocationCode;

    @Field(required = true)
    private String contentName;

    @Field(required = true)
    private ContentType contentType;

    @Field(required = true)
    private String contentFile;

    @Field(required = true)
    private String cardCode;

    @Field(required = true)
    private Integer contentDuration;

    public ContentUpload() {

    }

    public ContentUpload(Integer contentId, String circleCode, String languageLocationCode, String contentName, ContentType contentType, String contentFile, String cardCode, Integer contentDuration) {
        this.contentId = contentId;
        this.circleCode = circleCode;
        this.languageLocationCode = languageLocationCode;
        this.contentName = contentName;
        this.contentType = contentType;
        this.contentFile = contentFile;
        this.cardCode = cardCode;
        this.contentDuration = contentDuration;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
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

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Integer getContentDuration() {
        return contentDuration;
    }

    public void setContentDuration(Integer contentDuration) {
        this.contentDuration = contentDuration;
    }
}
