package org.motechproject.nms.kilkari.dto.response;

import org.motechproject.nms.kilkari.domain.SubscriptionPack;

import java.util.List;

/**
 * API response object for subscriber detail.
 */
public class SubscriberDetailApiResponse {

    String circle;

    Integer languageLocationCode;

    List<SubscriptionPack> subscriptionPackList;

    Integer defaultLanguageLocationCode;

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

    public List<SubscriptionPack> getSubscriptionPackList() {
        return subscriptionPackList;
    }

    public void setSubscriptionPackList(List<SubscriptionPack> subscriptionPackList) {
        this.subscriptionPackList = subscriptionPackList;
    }
}
