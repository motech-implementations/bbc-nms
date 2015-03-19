package org.motechproject.nms.mobileacademy.dto;

public class User {

    private String circle;

    private Integer languageLocationCode;

    private Integer defaultLanguageLocationCode;

    private Integer currentUsageInPulses;

    private Integer maxAllowedUsageInPulses;

    private Integer endOfUsagePromptCounter;

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

}
