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
    private Integer nmsMkCappingType;

    @Field(required = true)
    private Integer nmsMkNationalCapValue;

    @Field(required = true)
    private Integer nmsMkMaxWelcomeMessage;

    @Field(required = true)
    private Integer nmsMkMaxEndofusageMessage;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getNmsMkCappingType() {
        return nmsMkCappingType;
    }

    public void setNmsMkCappingType(Integer nmsMkCappingType) {
        this.nmsMkCappingType = nmsMkCappingType;
    }

    public Integer getNmsMkNationalCapValue() {
        return nmsMkNationalCapValue;
    }

    public void setNmsMkNationalCapValue(Integer nmsMkNationalCapValue) {
        this.nmsMkNationalCapValue = nmsMkNationalCapValue;
    }

    public Integer getNmsMkMaxWelcomeMessage() {
        return nmsMkMaxWelcomeMessage;
    }

    public void setNmsMkMaxWelcomeMessage(Integer nmsMkMaxWelcomeMessage) {
        this.nmsMkMaxWelcomeMessage = nmsMkMaxWelcomeMessage;
    }

    public Integer getNmsMkMaxEndofusageMessage() {
        return nmsMkMaxEndofusageMessage;
    }

    public void setNmsMkMaxEndofusageMessage(Integer nmsMkMaxEndofusageMessage) {
        this.nmsMkMaxEndofusageMessage = nmsMkMaxEndofusageMessage;
    }
}

