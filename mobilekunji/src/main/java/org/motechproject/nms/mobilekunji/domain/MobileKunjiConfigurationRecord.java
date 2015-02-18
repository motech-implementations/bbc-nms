package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;

/**
 * Models data for simple records in a portable manner.
 */
@Entity
public class MobileKunjiConfigurationRecord {

    @Field(required = true)
    @Unique
    private String index;

    @Field(name = "NMS_MK_CAPPING_TYPE", required = true)
    private Integer nmsMkCappingType;

    @Field(name = "NMS_MK_NATIONAL_CAP_VALUE", required = true)
    private Integer nmsMkNationalCapValue;
    @Field(name = "NMS_MK_MAX_HEALTH_CARDS", required = true)
    private Integer nmsMkMaxHealthCards;
    @Field(name = "NMS_MK_MAX_WELCOME_MESSAGE", required = true)
    private Integer nmsMkMaxWelcomeMessage;
    @Field(name = "NMS_MK_MAX_ENDOFUSAGE_MESSAGE", required = true)
    private Integer nmsMkMaxEndofusageMessage;

    public MobileKunjiConfigurationRecord(String index, Integer nmsMkCappingType, Integer nmsMkNationalCapValue, Integer nmsMkMaxHealthCards, Integer nmsMkMaxWelcomeMessage, Integer nmsMkMaxEndofusageMessage) {
        this.index = index;
        this.nmsMkCappingType = nmsMkCappingType;
        this.nmsMkNationalCapValue = nmsMkNationalCapValue;
        this.nmsMkMaxHealthCards = nmsMkMaxHealthCards;
        this.nmsMkMaxWelcomeMessage = nmsMkMaxWelcomeMessage;
        this.nmsMkMaxEndofusageMessage = nmsMkMaxEndofusageMessage;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
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

    public Integer getNmsMkMaxHealthCards() {
        return nmsMkMaxHealthCards;
    }

    public void setNmsMkMaxHealthCards(Integer nmsMkMaxHealthCards) {
        this.nmsMkMaxHealthCards = nmsMkMaxHealthCards;
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