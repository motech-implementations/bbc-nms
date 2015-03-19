package org.motechproject.nms.frontlineworker.domain;

/**
 *  This Class models data for User Profile Details.
 */

public class UserProfile {

    private boolean isCreated;

    private Long nmsId;

    private boolean isDefaultLanguageLocationCode;

    private Integer languageLocationCode;

    private Integer defaultLanguageLocationCode;

    private String circle;

    private Integer maxCappingValue;

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

    public Integer getMaxCappingValue() {
        return maxCappingValue;
    }

    public void setMaxCappingValue(Integer maxCappingValue) {
        this.maxCappingValue = maxCappingValue;
    }
}
