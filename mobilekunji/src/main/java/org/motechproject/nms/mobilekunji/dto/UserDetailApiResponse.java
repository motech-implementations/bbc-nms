package org.motechproject.nms.mobilekunji.dto;

import java.io.Serializable;

/**
 * API response object for user detail.
 */
public class UserDetailApiResponse implements Serializable{

    private String circle;

    private Integer languageLocationCode;

    private Integer defaultLanguageLocationCode;

    private Integer currentUsageInPulses;

    private Integer maxAllowedUsageInPulses;

    private Boolean welcomePromptFlag;

    private Integer endOfUsagePromptCounter;

    private Integer maxAllowedEndOfUsagePrompt;

    private String failureReason;

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

    public Integer getDefaultLanguageLocationCode() {
        return defaultLanguageLocationCode;
    }

    public void setDefaultLanguageLocationCode(Integer defaultLanguageLocationCode) {
        this.defaultLanguageLocationCode = defaultLanguageLocationCode;
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

    public Boolean getWelcomePromptFlag() {
        return welcomePromptFlag;
    }

    public void setWelcomePromptFlag(Boolean welcomePromptFlag) {
        this.welcomePromptFlag = welcomePromptFlag;
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

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        String response;
        response = "{'circle:'" + this.circle;
        if (this.languageLocationCode != null) {
            response = response + "languageLocationCode: " + this.languageLocationCode + " }";
        } else {
            response = response + "defaultLanguageLocationCode: " + this.defaultLanguageLocationCode + " }";
        }
        return response;
    }
}
