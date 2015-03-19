package org.motechproject.nms.frontlineworker.domain;

/**
 * This Class models data for User Profile Details. The object of this class will be returned as the response
 * to the API calls from other  modules to get details of front line worker.
 */

public class UserProfile {

    private boolean isCreated;

    private Long nmsId;

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

    public void setNmsId(Long nmsId) {
        this.nmsId = nmsId;
    }

    public boolean isDefaultLanguageLocationCode() {
        return isDefaultLanguageLocationCode;
    }

    public void setDefaultLanguageLocationCode(boolean isDefaultLanguageLocationCode) {
        this.isDefaultLanguageLocationCode = isDefaultLanguageLocationCode;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public Integer getDefaultLanguageLocationCode() {
        return defaultLanguageLocationCode;
    }

    public void setDefaultLanguageLocationCode(Integer defaultLanguageLocationCode) {
        this.defaultLanguageLocationCode = defaultLanguageLocationCode;
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
}


