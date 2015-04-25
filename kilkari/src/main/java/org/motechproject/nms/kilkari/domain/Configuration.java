package org.motechproject.nms.kilkari.domain;


import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This entity represents the configuration parameters of Kilkari.
 */
@Entity(recordHistory = true)
public class Configuration {
    
    public static final Long CONFIGURATION_INDEX = 1L;

    @Unique
    @Field(required = true)
    @Min(1)
    @Max(1)
    @NotNull
    private Long index;
    
    @Field(required = true)
    private Integer numMsgPerWeek;

    @Field(required = true)
    private Integer nationalDefaultLanguageLocationCode;
    
    @Field(required = true)
    private Integer maxAllowedActiveBeneficiaryCount;
    
    @Field(required = true)
    private Integer expiredSubscriptionAgeDays;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getNumMsgPerWeek() {
        return numMsgPerWeek;
    }

    public void setNumMsgPerWeek(Integer numMsgPerWeek) {
        this.numMsgPerWeek = numMsgPerWeek;
    }

    public Integer getMaxAllowedActiveBeneficiaryCount() {
        return maxAllowedActiveBeneficiaryCount;
    }

    public void setMaxAllowedActiveBeneficiaryCount(Integer maxAllowedActiveBeneficiaryCount) {
        this.maxAllowedActiveBeneficiaryCount = maxAllowedActiveBeneficiaryCount;
    }

    public Integer getNationalDefaultLanguageLocationCode() {
        return nationalDefaultLanguageLocationCode;
    }

    public void setNationalDefaultLanguageLocationCode(Integer nationalDefaultLanguageLocationCode) {
        this.nationalDefaultLanguageLocationCode = nationalDefaultLanguageLocationCode;
    }

    public Integer getExpiredSubscriptionAgeDays() {
        return expiredSubscriptionAgeDays;
    }

    public void setExpiredSubscriptionAgeDays(Integer expiredSubscriptionAgeDays) {
        this.expiredSubscriptionAgeDays = expiredSubscriptionAgeDays;
    }
    
}
