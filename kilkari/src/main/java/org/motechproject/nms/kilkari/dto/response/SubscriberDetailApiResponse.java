package org.motechproject.nms.kilkari.dto.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;

import java.util.List;

/**
 * API response object for subscriber detail.
 */
public class SubscriberDetailApiResponse {

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String circle;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer languageLocationCode;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private List<SubscriptionPack> subscriptionPackList;

    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer defaultLanguageLocationCode;

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
