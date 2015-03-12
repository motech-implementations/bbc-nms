package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;

/**
 * Created by abhishek on 26/1/15.
 * This class Models data records provided in the Content Csv Upload
 */
@Entity(recordHistory = true)
public class ContentUploadCsv extends MdsEntity {

    @Field(required = true)
    @Unique
    private Long index;

    @Field
    private String operation = "ADD";

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
    private String cardNumber;

    @Field
    private String contentDuration;


    public ContentUploadCsv(Long index, String operation, String contentId, String circleCode,
                            String languageLocationCode, String contentName, String contentType, String contentFile,
                            String cardNumber, String contentDuration) {
        this.index = index;
        this.operation = operation;
        this.contentId = contentId;
        this.circleCode = circleCode;
        this.languageLocationCode = languageLocationCode;
        this.contentName = contentName;
        this.contentType = contentType;
        this.contentFile = contentFile;
        this.cardNumber = cardNumber;
        this.contentDuration = contentDuration;
    }

    public ContentUploadCsv() {
    }

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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getContentDuration() {
        return contentDuration;
    }

    public void setContentDuration(String contentDuration) {
        this.contentDuration = contentDuration;
    }


    public boolean validateParameters() {
        /*validation to add*/
        return true;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    @Override
    public String toString() {

        return "Content Index[" + this.getIndex() + "] Operation[" + this.getOperation() + "] Content ID["
                + this.getContentId() + "] Circle Code [" + this.getCircleCode() + "] Language Location Code [" +
                this.getLanguageLocationCode() + "] Content Name [" + this.getContentName() + "] Content Type [" +
                this.getContentType() + "] Content File [" + this.contentFile + "] Content Duration [" +
                this.getContentDuration() + "] Card Number [" + this.getCardNumber() + "]";


    }
}
