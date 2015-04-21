package org.motechproject.nms.frontlineworker.domain;

/**
 * This Class models data for User Profile Details. The object of this class will be returned as the response
 * to the API calls from other  modules to get details of front line worker.
 */

public class UserProfile {

    private boolean isCreated;

    private Long nmsFlwId;

    private String msisdn;

    private boolean isDefaultLanguageLocationCode;

    private String languageLocationCode;

    private String circle;

    private Integer maxStateLevelCappingValue;

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public Long getNmsFlwId() {
        return nmsFlwId;
    }

    public void setNmsFlwId(Long nmsFlwId) {

        this.nmsFlwId = nmsFlwId;
    }

    public boolean isDefaultLanguageLocationCode() {
        return isDefaultLanguageLocationCode;
    }

    public void setIsDefaultLanguageLocationCode(boolean isDefaultLanguageLocationCode) {
        this.isDefaultLanguageLocationCode = isDefaultLanguageLocationCode;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }


    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
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
                ", nmsFlwId=" + nmsFlwId +
                ", msisdn='" + msisdn + '\'' +
                ", isDefaultLanguageLocationCode=" + isDefaultLanguageLocationCode +
                ", languageLocationCode=" + languageLocationCode +
                ", circle='" + circle + '\'' +
                ", maxStateLevelCappingValue=" + maxStateLevelCappingValue +
                '}';
    }

}


