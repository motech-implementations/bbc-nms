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
    private int contentId;

    @Field(required = true)
    private String circleCode;

    @Field(required = true)
    private Integer languageLocationCode;

    @Field(required = true)
    private String contentName;

    @Field(required = true)
    private ContentType contentType;

    @Field(required = true)
    private String contentFile;

    @Field(required = true)
    private int cardNumber;

    @Field(required = true)
    private int contentDuration;

    public ContentUpload() {

    }

    public ContentUpload(int contentId, String circleCode, int languageLocationCode, String contentName, ContentType contentType, String contentFile, int cardNumber, int contentDuration) {
        this.contentId = contentId;
        this.circleCode = circleCode;
        this.languageLocationCode = languageLocationCode;
        this.contentName = contentName;
        this.contentType = contentType;
        this.contentFile = contentFile;
        this.cardNumber = cardNumber;
        this.contentDuration = contentDuration;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
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

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getContentDuration() {
        return contentDuration;
    }

    public void setContentDuration(int contentDuration) {
        this.contentDuration = contentDuration;
    }


}
