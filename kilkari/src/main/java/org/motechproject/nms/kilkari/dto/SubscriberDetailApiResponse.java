package org.motechproject.nms.kilkari.dto;

import java.util.List;

/**
 * API response object for subscriber detail.
 */
public class SubscriberDetailApiResponse {

    String circle;

    String languageLocationCode;

    List<String> subscriptionPackList;

    String defaultLanguageLocationCode;

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getDefaultLanguageLocationCode() {
        return defaultLanguageLocationCode;
    }

    public void setDefaultLanguageLocationCode(String defaultLanguageLocationCode) {
        this.defaultLanguageLocationCode = defaultLanguageLocationCode;
    }

    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public List<String> getSubscriptionPackList() {
        return subscriptionPackList;
    }

    public void setSubscriptionPackList(List<String> subscriptionPackList) {
        this.subscriptionPackList = subscriptionPackList;
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
