package org.motechproject.nms.frontlineworker.domain;

/**
 * This Class models data for User Profile Details. The object of this class will be returned as the response
 * to the API calls from other  modules to get details of front line worker.
 */

public class UserProfile {

    private boolean isCreated;

    private Long nmsId;

    private String msisdn;

    private boolean isDefaultLanguageLocationCode;

    private Integer languageLocationCode;

    private Integer defaultLanguageLocationCode;

    private String circle;

    private Integer maxStateLevelCappingValue;

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public Long getNmsId() {
        return nmsId;
    }

    public boolean isDefaultLanguageLocationCode() {
        return isDefaultLanguageLocationCode;
    }

    public void setIsDefaultLanguageLocationCode(boolean isDefaultLanguageLocationCode) {
        this.isDefaultLanguageLocationCode = isDefaultLanguageLocationCode;
    }

    public Integer getDefaultLanguageLocationCode() {
        return defaultLanguageLocationCode;
    }

    public void setDefaultLanguageLocationCode(Integer defaultLanguageLocationCode) {
        this.defaultLanguageLocationCode = defaultLanguageLocationCode;
    }

    public void setNmsId(Long nmsId) {

        this.nmsId = nmsId;
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
                ", nmsId=" + nmsId +
                ", msisdn='" + msisdn + '\'' +
                ", isDefaultLanguageLocationCode=" + isDefaultLanguageLocationCode +
                ", languageLocationCode=" + languageLocationCode +
                ", defaultLanguageLocationCode=" + defaultLanguageLocationCode +
                ", circle='" + circle + '\'' +
                ", maxStateLevelCappingValue=" + maxStateLevelCappingValue +
                '}';
    }

}


