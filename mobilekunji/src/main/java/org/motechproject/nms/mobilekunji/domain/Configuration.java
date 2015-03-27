package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Models data for simple records in a portable manner.
 */
@Entity(recordHistory = true)
public class Configuration extends MdsEntity {

    @Field(required = true)
    @Unique
    @Min(1)
    @Max(1)
    private Long index;

    @Field(required = true)
    private Integer cappingType;

    @Field(required = true)
    private Integer nationalCapValue;

    @Field(required = true)
    private Integer maxEndofusageMessage;

    @Field(required = true)
    private Integer nationalDefaultLanguageLocationCode;

    public Integer getNationalDefaultLanguageLocationCode() {
        return nationalDefaultLanguageLocationCode;
    }

    public void setNationalDefaultLanguageLocationCode(Integer nationalDefaultLanguageLocationCode) {
        this.nationalDefaultLanguageLocationCode = nationalDefaultLanguageLocationCode;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getCappingType() {
        return cappingType;
    }

    public void setCappingType(Integer cappingType) {
        this.cappingType = cappingType;
    }

    public Integer getNationalCapValue() {
        return nationalCapValue;
    }

    public void setNationalCapValue(Integer nationalCapValue) {
        this.nationalCapValue = nationalCapValue;
    }

    public Integer getMaxEndofusageMessage() {
        return maxEndofusageMessage;
    }

    public void setMaxEndofusageMessage(Integer maxEndofusageMessage) {
        this.maxEndofusageMessage = maxEndofusageMessage;
    }
}

