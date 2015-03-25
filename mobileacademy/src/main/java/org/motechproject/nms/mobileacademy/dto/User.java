package org.motechproject.nms.mobileacademy.dto;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * User object contain details required in get user detail API response.
 *
 */
public class User implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String circle;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer languageLocationCode;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer defaultLanguageLocationCode;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer currentUsageInPulses;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer maxAllowedUsageInPulses;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer endOfUsagePromptCounter;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer maxAllowedEndOfUsagePrompt;

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public Integer getCurrentUsageInPulses() {
        return currentUsageInPulses;
    }

    public void setCurrentUsageInPulses(Integer currentUsageInPulses) {
        this.currentUsageInPulses = currentUsageInPulses;
    }

    public Integer getMaxAllowedUsageInPulses() {
        return maxAllowedUsageInPulses;
    }

    public void setMaxAllowedUsageInPulses(Integer maxAllowedUsageInPulses) {
        this.maxAllowedUsageInPulses = maxAllowedUsageInPulses;
    }

    public Integer getEndOfUsagePromptCounter() {
        return endOfUsagePromptCounter;
    }

    public void setEndOfUsagePromptCounter(Integer endOfUsagePromptCounter) {
        this.endOfUsagePromptCounter = endOfUsagePromptCounter;
    }

    public Integer getMaxAllowedEndOfUsagePrompt() {
        return maxAllowedEndOfUsagePrompt;
    }

    public void setMaxAllowedEndOfUsagePrompt(Integer maxAllowedEndOfUsagePrompt) {
        this.maxAllowedEndOfUsagePrompt = maxAllowedEndOfUsagePrompt;
    }

    public Integer getDefaultLanguageLocationCode() {
        return defaultLanguageLocationCode;
    }

    public void setDefaultLanguageLocationCode(
            Integer defaultLanguageLocationCode) {
        this.defaultLanguageLocationCode = defaultLanguageLocationCode;
    }

    @Override
    public String toString() {
        return "User{circle=" + circle + ", languageLocationCode="
                + languageLocationCode + ", defaultLanguageLocationCode="
                + defaultLanguageLocationCode + ", currentUsageInPulses="
                + currentUsageInPulses + ", maxAllowedUsageInPulses="
                + maxAllowedUsageInPulses + ", endOfUsagePromptCounter="
                + endOfUsagePromptCounter + ", maxAllowedEndOfUsagePrompt="
                + maxAllowedEndOfUsagePrompt + "}";
    }

}
