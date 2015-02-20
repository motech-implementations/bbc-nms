package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Models data for simple records in a portable manner.
 */
@Entity
public class ServiceConfiguration {

    @Field(required = true)
    @Unique
    @Min(1)
    @Max(1)
    private int index;

    @Field(required = true)
    private int nmsMkCappingType;

    @Field(required = true)
    private int nmsMkNationalCapValue;
    @Field(required = true)
    private int nmsMkMaxHealthCards;
    @Field(required = true)
    private int nmsMkMaxWelcomeMessage;
    @Field(required = true)
    private int nmsMkMaxEndofusageMessage;

    public ServiceConfiguration(int index, int nmsMkCappingType, int nmsMkNationalCapValue, int nmsMkMaxHealthCards, int nmsMkMaxWelcomeMessage, int nmsMkMaxEndofusageMessage) {
        this.index = index;
        this.nmsMkCappingType = nmsMkCappingType;
        this.nmsMkNationalCapValue = nmsMkNationalCapValue;
        this.nmsMkMaxHealthCards = nmsMkMaxHealthCards;
        this.nmsMkMaxWelcomeMessage = nmsMkMaxWelcomeMessage;
        this.nmsMkMaxEndofusageMessage = nmsMkMaxEndofusageMessage;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNmsMkCappingType() {
        return nmsMkCappingType;
    }

    public void setNmsMkCappingType(int nmsMkCappingType) {
        this.nmsMkCappingType = nmsMkCappingType;
    }

    public int getNmsMkNationalCapValue() {
        return nmsMkNationalCapValue;
    }

    public void setNmsMkNationalCapValue(int nmsMkNationalCapValue) {
        this.nmsMkNationalCapValue = nmsMkNationalCapValue;
    }

    public int getNmsMkMaxHealthCards() {
        return nmsMkMaxHealthCards;
    }

    public void setNmsMkMaxHealthCards(int nmsMkMaxHealthCards) {
        this.nmsMkMaxHealthCards = nmsMkMaxHealthCards;
    }

    public int getNmsMkMaxWelcomeMessage() {
        return nmsMkMaxWelcomeMessage;
    }

    public void setNmsMkMaxWelcomeMessage(int nmsMkMaxWelcomeMessage) {
        this.nmsMkMaxWelcomeMessage = nmsMkMaxWelcomeMessage;
    }

    public int getNmsMkMaxEndofusageMessage() {
        return nmsMkMaxEndofusageMessage;
    }

    public void setNmsMkMaxEndofusageMessage(int nmsMkMaxEndofusageMessage) {
        this.nmsMkMaxEndofusageMessage = nmsMkMaxEndofusageMessage;
    }

}