package org.motechproject.nms.frontlineworker.domain;

/**
 * This Class models data for User Profile Details. The object of this class will be returned as the response
 * to the API calls from other  modules to get details of front line worker.
 */

public class UserProfile {

    private boolean isCreated;

    private Long systemGeneratedFlwId;

    private String msisdn;

    private boolean isDefaultLanguageLocationCode;

    private Integer languageLocationCode;

    private String circle;

    private Integer maxStateLevelCappingValue;

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public Long getSystemGeneratedFlwId() {
        return systemGeneratedFlwId;
    }

    public boolean isDefaultLanguageLocationCode() {
        return isDefaultLanguageLocationCode;
    }

    public void setIsDefaultLanguageLocationCode(boolean isDefaultLanguageLocationCode) {
        this.isDefaultLanguageLocationCode = isDefaultLanguageLocationCode;
    }

    public void setSystemGeneratedFlwId(Long systemGeneratedFlwId) {

        this.systemGeneratedFlwId = systemGeneratedFlwId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }


    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }


    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public Integer getMaxStateLevelCappingValue() {
        return maxStateLevelCappingValue;
    }

    public void setMaxStateLevelCappingValue(Integer maxStateLevelCappingValue) {
        this.maxStateLevelCappingValue = maxStateLevelCappingValue;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "isCreated=" + isCreated +
                ", systemGeneratedFlwId=" + systemGeneratedFlwId +
                ", msisdn='" + msisdn + '\'' +
                ", isDefaultLanguageLocationCode=" + isDefaultLanguageLocationCode +
                ", languageLocationCode=" + languageLocationCode +
                ", circle='" + circle + '\'' +
                ", maxStateLevelCappingValue=" + maxStateLevelCappingValue +
                '}';
    }

}


