package org.motechproject.nms.kilkari.dto;

import org.motechproject.nms.kilkari.domain.SubscriptionPack;

import java.util.List;

/**
 * API response object for subscriber detail.
 */
public class SubscriberDetailApiResponse {

    String circle;

    String languageLocationCode;

    List<SubscriptionPack> subscriptionPackList;

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

    public List<SubscriptionPack> getSubscriptionPackList() {
        return subscriptionPackList;
    }

    public void setSubscriptionPackList(List<SubscriptionPack> subscriptionPackList) {
        this.subscriptionPackList = subscriptionPackList;
    }
}
